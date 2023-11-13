package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Favorite;
import ru.yandex.workshop.main.repository.buyer.FavoriteRepository;
import ru.yandex.workshop.main.service.product.UserProductService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BuyerService buyerService;
    private final UserProductService userProductService;

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
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
        return new ArrayList<>(favoriteRepository.findAllByBuyerEmail(buyerEmail));
    }

    @Transactional(readOnly = true)
    public Favorite getFavorite(String buyerEmail, Long productId) {
        Favorite favorite = new Favorite();
        favorite.setBuyer(buyerService.getSecurityBuyer(buyerEmail));
        favorite.setProduct(userProductService.getProductFromDatabase(productId));
        return favorite;
    }
}
