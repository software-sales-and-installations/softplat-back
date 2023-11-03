package ru.yandex.workshop.main.dto.buyer;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Favorite;
import ru.yandex.workshop.main.model.product.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-03T14:38:55+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class FavoriteMapperImpl implements FavoriteMapper {

    @Override
    public Favorite toModel(Long id, Buyer buyer, Product product) {
        if ( id == null && buyer == null && product == null ) {
            return null;
        }

        Long id1 = null;
        if ( id != null ) {
            id1 = id;
        }
        Buyer buyer1 = null;
        if ( buyer != null ) {
            buyer1 = buyer;
        }
        Product product1 = null;
        if ( product != null ) {
            product1 = product;
        }

        Favorite favorite = new Favorite( id1, buyer1, product1 );

        return favorite;
    }

    @Override
    public FavoriteDto toDTO(Favorite model) {
        if ( model == null ) {
            return null;
        }

        Long buyerId = null;
        Long productId = null;

        FavoriteDto favoriteDto = new FavoriteDto( buyerId, productId );

        return favoriteDto;
    }
}
