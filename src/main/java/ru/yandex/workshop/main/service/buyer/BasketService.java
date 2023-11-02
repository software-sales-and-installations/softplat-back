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
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.ProductBasket;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;
    private final ProductBasketRepository productBasketRepository;

    @Transactional
    public BasketDto addProduct(Long userId, Long productId) {
        Product product = getProduct(productId);
        if (product.getQuantity() == 0) throw new WrongConditionException("Товара нет в наличии.");
        Basket basket = getBasketByUserId(userId);
        if (basket.getProductsInBasket() == null) {
            ProductBasket productBasket = new ProductBasket(null, product, 1);
            basket.setProductsInBasket(List.of(productBasket));
            return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
        }
        for (ProductBasket productBasket : basket.getProductsInBasket()) {
            if (productBasket.getProduct().getId().equals(product.getId())) {
                productBasket.setQuantity(productBasket.getQuantity() + 1);
                return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
            }
        }
        ProductBasket productBasket = new ProductBasket(null, product, 1);
        basket.getProductsInBasket().add(productBasket);
        return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
    }

    @Transactional
    public BasketDto removeProduct(Long userId, Long productId) { //TODO exception
        Basket basket = basketRepository.findByBuyer_Id(userId).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        if (basket.getProductsInBasket() != null) {
            for (ProductBasket productBasket : basket.getProductsInBasket()) {
                if (productBasket.getProduct().getId().equals(productId)) {
                    if (productBasket.getQuantity() == 1) productBasketRepository.deleteById(productBasket.getId());
                    else {
                        productBasket.setQuantity(productBasket.getQuantity() - 1);
                    }
                    return BasketMapper.INSTANCE.basketToBasketDto(basketRepository.save(basket));
                }
            }
        }
        throw new WrongConditionException("Ошибка при удалении товара");
    }

    public BasketDto getBasket(Long userId) {
        return BasketMapper.INSTANCE.basketToBasketDto(getBasketByUserId(userId));
    }

    private Product getProduct(long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    private Basket getBasketByUserId(Long userId) {
        Optional<Basket> basket = basketRepository.findByBuyer_Id(userId);
        if (basket.isEmpty()) {
            Buyer buyer = buyerRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
            return basketRepository.save(Basket.builder()
                    .buyer(buyer)
                    .build());
        }
        return basket.get();
    }
}
