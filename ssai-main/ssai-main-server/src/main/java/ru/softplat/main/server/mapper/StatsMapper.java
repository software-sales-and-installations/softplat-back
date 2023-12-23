package ru.softplat.main.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.buyer.OrderPosition;
import ru.softplat.main.server.model.product.Product;
import ru.softplat.stats.dto.create.StatsCreateDemoDto;
import ru.softplat.stats.dto.create.StatsCreateDto;

@Mapper
public interface StatsMapper {

    @Mapping(target = "dateBuy", source = "order.productionTime")
    @Mapping(target = "profit", ignore = true)
    StatsCreateDto orderToStatsCreateDto(Order order, OrderPosition orderPosition,
                                         Double profitSeller, Double profitAdmin);

    StatsCreateDemoDto mapToDemoDto(Buyer buyer, Product product);
}
