package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.dto.basket.BasketMapper;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.ProductBasket;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;
import ru.yandex.workshop.security.model.user.Buyer;
import ru.yandex.workshop.security.repository.BuyerRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final ProductBasketRepository productBasketRepository;
    private final BuyerRepository buyerRepository;

    @Transactional
    public BasketDto addProduct(String buyerEmail, Long productId, Boolean installation) {
        Product product = getProduct(productId);
        if (!product.getInstallation() && installation)
            throw new WrongConditionException("Для товара не предусмотрена установка.");
        if (product.getQuantity() == 0) throw new WrongConditionException("Товара нет в наличии.");
        Basket basket = getBasketByBuyerEmail(buyerEmail);
        if (basket.getProductsInBasket() == null) {
            ProductBasket productBasket = new ProductBasket(null, product, 1, installation);
            basket.setProductsInBasket(List.of(productBasket));
            return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
        }
        for (ProductBasket productBasket : basket.getProductsInBasket()) {
            if (productBasket.getProduct().getId().equals(product.getId()) &&
                    productBasket.getProduct().getInstallation().equals(installation)) {
                productBasket.setQuantity(productBasket.getQuantity() + 1);
                return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
            }
        }
        ProductBasket productBasket = new ProductBasket(null, product, 1, installation);
        basket.getProductsInBasket().add(productBasket);
        return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
    }

    @Transactional
    public BasketDto removeProduct(String buyerEmail, Long productId, Boolean installation) {
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
                    return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
                }
            }
        }
        throw new WrongConditionException("Ошибка при удалении товара");
    }

    public BasketDto getBasket(String buyerEmail) {
        return BasketMapper.INSTANCE.basketToBasketDto(getBasketByBuyerEmail(buyerEmail));
    }

    private Product getProduct(long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private Basket getBasketByBuyerEmail(String email) {
        Buyer buyer = buyerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        Optional<Basket> basket = basketRepository.findByBuyerId(buyer.getId());
        return basket.orElseGet(() -> basketRepository.save(Basket.builder()
                .buyerId(buyer.getId())
                .build()));
    }
}
