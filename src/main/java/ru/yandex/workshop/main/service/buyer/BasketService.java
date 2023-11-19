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
import ru.yandex.workshop.main.model.product.ProductStatus;
import ru.yandex.workshop.main.repository.buyer.BasketPositionRepository;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.service.product.SearchProductService;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final BasketPositionRepository basketPositionRepository;
    private final SearchProductService searchProductService;
    private final BuyerService buyerService;

    public Basket addProduct(String buyerEmail, Long productId, Boolean installation) {
        Product product = searchProductService.getProductById(productId);
        if (product.getProductStatus() != ProductStatus.PUBLISHED)
            throw new NullPointerException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
        if (!product.getInstallation() && installation)
            throw new WrongConditionException("Для товара не предусмотрена установка.");
        if (product.getQuantity() == 0) throw new WrongConditionException("Товара нет в наличии.");
        Basket basket = getOrCreateBasket(buyerEmail);
        BasketPosition productInBasket = basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(
                basket.getId(), productId, installation);
        if (productInBasket != null) {
            productInBasket.setQuantity(productInBasket.getQuantity() + 1);
            basketPositionRepository.save(productInBasket);
            return getBasketOrThrowException(buyerEmail);
        }
        basketPositionRepository.save(new BasketPosition(null, basket.getId(),
                product, 1, installation));
        return getBasketOrThrowException(buyerEmail);
    }

    public Basket removeProduct(String buyerEmail, Long productId, Boolean installation) {
        Basket basket = getOrCreateBasket(buyerEmail);
        BasketPosition productInBasket = basketPositionRepository.findAllByBasketIdAndProduct_IdAndInstallation(
                basket.getId(), productId, installation);
        if (productInBasket != null) {
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
                .build()));
    }

    @Transactional(readOnly = true)
    public Basket getBasketOrThrowException(String email) {
        Buyer buyer = buyerService.getBuyerByEmail(email);
        Optional<Basket> basket = basketRepository.findByBuyerId(buyer.getId());
        return basket.orElseThrow(() -> new NullPointerException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }
}
