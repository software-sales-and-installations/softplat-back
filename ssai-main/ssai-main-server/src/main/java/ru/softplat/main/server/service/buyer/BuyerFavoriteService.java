package ru.softplat.main.server.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Favorite;
import ru.softplat.main.server.model.product.ProductList;
import ru.softplat.main.server.repository.buyer.FavoriteRepository;
import ru.softplat.main.server.service.product.ProductService;
import ru.softplat.main.server.service.product.SearchProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BuyerService buyerService;
    private final ProductService productService;
    private final SearchProductService searchProductService;

    public Favorite create(long userId, Long productId) {
        if (favoriteRepository.existsByBuyerIdAndProductId(userId, productId))
            throw new DuplicateException("Предмет был добавлен в избранное ранее.");
        Favorite favorite = getFavorite(userId, productId);
        return favoriteRepository.save(favorite);
    }

    public void delete(long userId, Long productId) {
        Favorite favorite = favoriteRepository.findByBuyerIdAndProductId(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(productId, Favorite.class)));
        favoriteRepository.deleteById(favorite.getId());
    }

    @Transactional(readOnly = true)
    public List<Favorite> getAll(Long userId) {
        buyerService.getBuyer(userId);
        return favoriteRepository.findAllByBuyerId(userId);
    }

    private Favorite getFavorite(long userId, Long productId) {
        Favorite favorite = new Favorite();
        favorite.setBuyer(buyerService.getBuyer(userId));
        favorite.setProduct(productService.getAvailableProduct(productId));
        return favorite;
    }

    @Transactional(readOnly = true)
    public ProductList getRecommendations(long userId, int from, int size) {
        PageRequest page = PageRequestOverride.of(from, size);
        buyerService.getBuyer(userId);
        List<Long> recommendations = favoriteRepository.getRecommendations(userId, page);
        return new ProductList(searchProductService.getProductsByIds(recommendations), (long) size);
    }
}
