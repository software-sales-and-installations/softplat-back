package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.basket.BasketDto;
import ru.yandex.workshop.main.dto.basket.BasketMapper;
import ru.yandex.workshop.main.exception.ProductNotFoundException;
import ru.yandex.workshop.main.exception.UserNotFoundException;
import ru.yandex.workshop.main.model.buyer.Basket;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.ProductBasket;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.BasketRepository;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository;

    @Transactional
    public BasketDto addProduct(Long userId, Long productId) { //TODO check quantity
        Product product = getProduct(productId);
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
    public BasketDto removeProduct(Long userId, Long productId) {
        return null;
    }

    public BasketDto getBasket(Long userId) {
        return BasketMapper.INSTANCE.basketToBasketDto(getBasketByUserId(userId));
    }

    private Product getProduct(long id) { //TODO exception
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("n"));
    }

    private Basket getBasketByUserId(Long userId) { //TODO exception
        Optional<Basket> basket = basketRepository.findByBuyer_Id(userId);
        if (basket.isEmpty()) {
            Buyer buyer = buyerRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(""));
            return basketRepository.save(Basket.builder()
                    .buyer(buyer)
                    .build());
        }
        return basket.get();
    }
}
