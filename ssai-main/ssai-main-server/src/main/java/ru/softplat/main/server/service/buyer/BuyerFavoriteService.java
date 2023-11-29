package ru.softplat.main.server.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.server.repository.buyer.FavoriteRepository;
import ru.softplat.main.server.service.product.ProductService;
import ru.softplat.main.server.exception.DuplicateException;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Favorite;

import java.util.List;

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
}
