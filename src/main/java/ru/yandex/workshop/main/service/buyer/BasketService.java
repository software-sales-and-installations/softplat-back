package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.BasketPosition;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;
import ru.yandex.workshop.main.service.product.SearchProductService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductBasketRepository productBasketRepository;
    private final SearchProductService searchProductService;
    private final BuyerService buyerService;

    public Basket addProduct(String buyerEmail, Long productId, Boolean installation) {
        Product product = getProduct(productId);
        if (!product.getInstallation() && installation)
            throw new WrongConditionException("Для товара не предусмотрена установка.");
        if (product.getQuantity() == 0) throw new WrongConditionException("Товара нет в наличии.");
        Basket basket = getBasketByBuyerEmail(buyerEmail);
        if (basket.getProductsInBasket() == null) {
            BasketPosition basketPosition = new BasketPosition(null, product, 1, installation);
            basket.setProductsInBasket(List.of(basketPosition));
            return basketRepository.save(basket);
        }
        for (BasketPosition basketPosition : basket.getProductsInBasket()) {
            if (basketPosition.getProduct().getId().equals(product.getId()) &&
                    basketPosition.getProduct().getInstallation().equals(installation)) {
                basketPosition.setQuantity(basketPosition.getQuantity() + 1);
                return basketRepository.save(basket);
            }
        }
        BasketPosition basketPosition = new BasketPosition(null, product, 1, installation);
        basket.getProductsInBasket().add(basketPosition);
        return basketRepository.save(basket);
    }

    public Basket removeProduct(String buyerEmail, Long productId, Boolean installation) {
        Basket basket = getBasketByBuyerEmail(buyerEmail);
        if (basket.getProductsInBasket() != null) {
            for (int i = 0; i < basket.getProductsInBasket().size(); i++) {
                BasketPosition basketPosition = basket.getProductsInBasket().get(i);
                if (basketPosition.getProduct().getId().equals(productId) &&
                        basketPosition.getProduct().getInstallation().equals(installation)) {
                    if (basketPosition.getQuantity() == 1) {
                        productBasketRepository.deleteById(basketPosition.getId());
                        basket.getProductsInBasket().remove(i);
                        if (basket.getProductsInBasket().size() == 0) basket.setProductsInBasket(null);
                    } else {
                        basketPosition.setQuantity(basketPosition.getQuantity() - 1);
                    }
                    return basketRepository.save(basket);
                }
            }
        }
        throw new WrongConditionException("Ошибка при удалении товара");
    }

    @Transactional(readOnly = true)
    public Basket getBasket(String buyerEmail) {
        return getBasketByBuyerEmail(buyerEmail);
    }

    @Transactional(readOnly = true)
    public Product getProduct(long id) {
        return searchProductService.getProductById(id);
    }

    @Transactional(readOnly = true)
    public Basket getBasketByBuyerEmail(String email) {
        Buyer buyer = buyerService.getBuyerByEmail(email);
        Optional<Basket> basket = basketRepository.findByBuyerId(buyer.getId());
        return basket.orElseGet(() -> basketRepository.save(Basket.builder()
                .buyerId(buyer.getId())
                .build()));
    }
}
