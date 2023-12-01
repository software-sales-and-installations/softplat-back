package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Favorite;
import ru.yandex.workshop.main.model.product.Product;
import ru.yandex.workshop.main.repository.buyer.FavoriteRepository;
import ru.yandex.workshop.main.service.product.ProductService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BuyerService buyerService;
    private final ProductService productService;

    public Favorite create(String buyerEmail, Long productId) {
        if (favoriteRepository.existsByBuyerEmailAndProductId(buyerEmail, productId))
            throw new DuplicateException("Предмет был добавлен в избранное ранее.");
        Favorite favorite = getFavorite(buyerEmail, productId);
        return favoriteRepository.save(favorite);
    }

    public void delete(String buyerEmail, Long productId) {
        Favorite favorite = getFavorite(buyerEmail, productId);
        favoriteRepository.delete(favorite);
    }

    @Transactional(readOnly = true)
    public List<Favorite> getAll(String buyerEmail) {
        if (!buyerService.checkIfUserExistsByEmail(buyerEmail))
            throw new EntityNotFoundException(
                    ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(buyerEmail, Buyer.class)
            );
        return favoriteRepository.findAllByBuyerEmail(buyerEmail);
    }

    private Favorite getFavorite(String buyerEmail, Long productId) {
        Favorite favorite = new Favorite();
        favorite.setBuyer(buyerService.getBuyerByEmail(buyerEmail));
        favorite.setProduct(productService.getAvailableProduct(productId));
        return favorite;
    }

    @Transactional(readOnly = true)
    public List<Product> getRecommendations(String email, int from, int size) {
        PageRequest page = PageRequestOverride.of(from, size);
        buyerService.checkIfUserExistsByEmail(email);
        List<Long> recommendations = favoriteRepository.getRecommendations(email, page);
        return productService.getProductsByIds(recommendations);
    }
}
