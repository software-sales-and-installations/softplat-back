package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.buyer.FavoriteDto;
import ru.yandex.workshop.main.dto.buyer.FavoriteMapper;
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

    public FavoriteDto create(Long buyerId, Long productId) {
        if (favoriteRepository.existsByBuyerIdAndProductId(buyerId, productId))
            throw new DuplicateException("Предмет был добавлен в избранное ранее.");
        return mapper.toDTO(favoriteRepository.save(mapper.toModel(
                null,
                buyerRepository.findById(buyerId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)),
                productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label))
        )));
    }

    public void delete(Long buyerId, Long productId) {
        favoriteRepository.delete(mapper.toModel(
                null,
                buyerRepository.findById(buyerId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)),
                productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label))
        ));
    }

    public List<FavoriteDto> getAll(Long buyerId) {
        if (!buyerRepository.existsBuyerById(buyerId))
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
        return favoriteRepository.findAllByBuyerId(buyerId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
