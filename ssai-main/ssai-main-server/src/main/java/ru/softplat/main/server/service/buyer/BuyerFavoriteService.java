package ru.softplat.main.server.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.model.buyer.Favorite;
import ru.softplat.main.server.repository.buyer.FavoriteRepository;
import ru.softplat.main.server.service.product.ProductService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BuyerService buyerService;
    private final ProductService productService;

    public Favorite create(long userId, Long productId) {
        if (favoriteRepository.existsByBuyerIdAndProductId(userId, productId))
            throw new DuplicateException("Предмет был добавлен в избранное ранее.");
        Favorite favorite = getFavorite(userId, productId);
        return favoriteRepository.save(favorite);
    }

    public void delete(long userId, Long productId) {
        Favorite favorite = getFavorite(userId, productId);
        favoriteRepository.delete(favorite);
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
}
