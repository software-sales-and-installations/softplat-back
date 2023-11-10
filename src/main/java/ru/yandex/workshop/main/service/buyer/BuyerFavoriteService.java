package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.user.mapper.FavoriteMapper;
import ru.yandex.workshop.main.dto.user.response.FavoriteDto;
import ru.yandex.workshop.main.exception.DuplicateException;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.repository.buyer.BuyerRepository;
import ru.yandex.workshop.main.repository.buyer.FavoriteRepository;
import ru.yandex.workshop.main.repository.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BuyerFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;
    private final FavoriteMapper mapper;

    public FavoriteDto create(String buyerEmail, Long productId) {
        if (favoriteRepository.existsByBuyerEmailAndProductId(buyerEmail, productId))
            throw new DuplicateException("Предмет был добавлен в избранное ранее.");
        return mapper.toDTO(favoriteRepository.save(mapper.toModel(
                null,
                buyerRepository.findByEmail(buyerEmail).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)),
                productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label))
        )));
    }

    public void delete(String buyerEmail, Long productId) {
        favoriteRepository.delete(mapper.toModel(
                null,
                buyerRepository.findByEmail(buyerEmail).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)),
                productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label))
        ));
    }

    public List<FavoriteDto> getAll(String buyerEmail) {
        if (!buyerRepository.existsByEmail(buyerEmail))
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
        return favoriteRepository.findAllByBuyerEmail(buyerEmail)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
