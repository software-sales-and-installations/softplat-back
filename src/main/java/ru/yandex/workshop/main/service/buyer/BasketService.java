package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.BasketPosition;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.BasketPositionRepository;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.service.product.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketPositionRepository basketPositionRepository;
    private final ProductService productService;
    private final BuyerService buyerService;

    public Basket addProduct(String buyerEmail, Long productId, Boolean installation) {
        Product product = productService.getAvailableProduct(productId);
        checkIfProductAvailableForPurchase(product, installation);

        Basket basket = getOrCreateBasket(buyerEmail);
        List<BasketPosition> productsInBasket = basket.getProductsInBasket();

        Optional<BasketPosition> optionalBasketPosition
                = getOptionalBasketPosition(productsInBasket, productId, installation);

        BasketPosition basketPosition;
        if (optionalBasketPosition.isPresent()) {
            basketPosition = optionalBasketPosition.get();
            checkIfAddingNewProductPositionIsPossible(basketPosition, product);

            basketPosition.setQuantity(basketPosition.getQuantity() + 1);
            updateBasketPosition(productsInBasket, basketPosition);
        } else {
            basketPosition = createNewBasketPosition(basket, product, installation);
            productsInBasket.add(basketPosition);
        }
        basketPositionRepository.save(basketPosition);

        return basketRepository.save(basket);
    }

    private void checkIfAddingNewProductPositionIsPossible(BasketPosition basketPosition, Product product) {
        if (basketPosition.getQuantity() + 1 > product.getQuantity()) {
            throw new WrongConditionException("Товара нет в наличии.");
        }
    }

    private void checkIfProductAvailableForPurchase(Product product, Boolean installation) {
        if (!product.getInstallation() && installation)
            throw new WrongConditionException("Для товара не предусмотрена установка.");
        if (product.getQuantity() <= 0)
            throw new WrongConditionException("Товара нет в наличии.");
    }

    private BasketPosition createNewBasketPosition(Basket basket, Product product, Boolean installation) {
        return BasketPosition.builder()
                .basketId(basket.getId())
                .product(product)
                .quantity(1)
                .installation(installation)
                .build();
    }

    private void updateBasketPosition(List<BasketPosition> productsInBasket,
                                      BasketPosition updatedBasketPosition) {
        for (int i = 0; i < productsInBasket.size(); i++) {
            BasketPosition oldBasketPosition = productsInBasket.get(i);
            if (oldBasketPosition.getId().equals(updatedBasketPosition.getId())) {
                productsInBasket.set(i, updatedBasketPosition);
                break;
            }
        }
    }

    private Optional<BasketPosition> getOptionalBasketPosition(List<BasketPosition> productsInBasket,
                                                               Long productId,
                                                               Boolean installation) {
        return productsInBasket.stream()
                .filter(bp -> Objects.equals(bp.getProduct().getId(), productId))
                .filter(bp -> Objects.equals(bp.getInstallation(), installation))
                .findFirst();
    }

    public Basket removeProduct(String buyerEmail, Long productId, Boolean installation) {
        Basket basket = getOrCreateBasket(buyerEmail);
        Optional<BasketPosition> optionalBasketPosition
                = getOptionalBasketPosition(basket.getProductsInBasket(), productId, installation);

        if (optionalBasketPosition.isPresent()) {
            BasketPosition productInBasket = optionalBasketPosition.get();
            if (productInBasket.getQuantity() == 1) {
                basketPositionRepository.deleteById(productInBasket.getId());
                return getBasketOrThrowException(buyerEmail);
            } else {
                for (int i = 0; i < basket.getProductsInBasket().size(); i++) {
                    if (Objects.equals(basket.getProductsInBasket().get(i).getId(), productInBasket.getId())) {
                        basket.getProductsInBasket().get(i).setQuantity(productInBasket.getQuantity() - 1);
                        return basketRepository.save(basket);
                    }
                }
            }
        }
        throw new WrongConditionException("Ошибка при удалении товара");
    }

    public Basket getOrCreateBasket(String buyerEmail) {
        Buyer buyer = buyerService.getBuyerByEmail(buyerEmail);
        Optional<Basket> basket = basketRepository.findByBuyerId(buyer.getId());
        return basket.orElseGet(() -> basketRepository.save(Basket.builder()
                .buyerId(buyer.getId())
                .productsInBasket(new ArrayList<>())
                .build()));
    }

    @Transactional(readOnly = true)
    public Basket getBasketOrThrowException(String email) {
        Buyer buyer = buyerService.getBuyerByEmail(email);
        Optional<Basket> basket = basketRepository.findByBuyerId(buyer.getId());
        return basket.orElseThrow(() -> new NullPointerException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    public void removeBasketPosition(long basketPositionId) {
        basketPositionRepository.deleteById(basketPositionId);
    }
}
