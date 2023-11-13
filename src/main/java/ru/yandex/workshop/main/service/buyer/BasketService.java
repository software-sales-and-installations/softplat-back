package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.ProductBasket;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;
import ru.yandex.workshop.main.service.product.PublicProductService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductBasketRepository productBasketRepository;
    private final PublicProductService publicProductService;
    private final BuyerService buyerService;

    public Basket addProduct(String buyerEmail, Long productId, Boolean installation) {
        Product product = getProduct(productId);
        if (!product.getInstallation() && installation)
            throw new WrongConditionException("Для товара не предусмотрена установка.");
        if (product.getQuantity() == 0) throw new WrongConditionException("Товара нет в наличии.");
        Basket basket = getBasketByBuyerEmail(buyerEmail);
        if (basket.getProductsInBasket() == null) {
            ProductBasket productBasket = new ProductBasket(null, product, 1, installation);
            basket.setProductsInBasket(List.of(productBasket));
            return basketRepository.save(basket);
        }
        for (ProductBasket productBasket : basket.getProductsInBasket()) {
            if (productBasket.getProduct().getId().equals(product.getId()) &&
                    productBasket.getProduct().getInstallation().equals(installation)) {
                productBasket.setQuantity(productBasket.getQuantity() + 1);
                return basketRepository.save(basket);
            }
        }
        ProductBasket productBasket = new ProductBasket(null, product, 1, installation);
        basket.getProductsInBasket().add(productBasket);
        return basketRepository.save(basket);
    }

    public Basket removeProduct(String buyerEmail, Long productId, Boolean installation) {
        Basket basket = getBasketByBuyerEmail(buyerEmail);
        if (basket.getProductsInBasket() != null) {
            for (int i = 0; i < basket.getProductsInBasket().size(); i++) {
                ProductBasket productBasket = basket.getProductsInBasket().get(i);
                if (productBasket.getProduct().getId().equals(productId) &&
                        productBasket.getProduct().getInstallation().equals(installation)) {
                    if (productBasket.getQuantity() == 1) {
                        productBasketRepository.deleteById(productBasket.getId());
                        basket.getProductsInBasket().remove(i);
                        if (basket.getProductsInBasket().size() == 0) basket.setProductsInBasket(null);
                    } else {
                        productBasket.setQuantity(productBasket.getQuantity() - 1);
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
        return publicProductService.getProductById(id);
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
