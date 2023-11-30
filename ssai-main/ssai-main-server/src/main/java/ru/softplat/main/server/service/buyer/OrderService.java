package ru.softplat.main.server.service.buyer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softplat.main.dto.product.ProductStatus;
import ru.softplat.main.server.configuration.PageRequestOverride;
import ru.softplat.main.server.exception.EntityNotFoundException;
import ru.softplat.main.server.exception.WrongConditionException;
import ru.softplat.main.server.mapper.OrderPositionMapper;
import ru.softplat.main.server.message.ExceptionMessage;
import ru.softplat.main.server.model.buyer.*;
import ru.softplat.main.server.repository.buyer.OrderPositionRepository;
import ru.softplat.main.server.repository.buyer.OrderRepository;
import ru.softplat.main.server.service.product.ProductService;
import ru.softplat.stats.client.model.StatsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BuyerService buyerService;
    private final BasketService basketService;
    private final OrderPositionRepository orderPositionRepository;
    private final OrderPositionMapper mapper;
    private final ProductService productService;
    private final StatsClient statClient;

    public Order createOrder(long userId, List<Long> basketPositionIds) {
        Order order = createNewEmptyOrder(userId);
        List<OrderPosition> orderPositionList = new ArrayList<>();
        float wholePrice = 0F;
        Basket basket = basketService.getBasketOrThrowException(userId);

        if (basketPositionIds.size() == 0) {
            throw new WrongConditionException(ExceptionMessage.WRONG_CONDITION_EXCEPTION.label
                    + " : необходимо указать id товарных позиций из корзины для заказа");
        }

        for (Long productBasketId : basketPositionIds) {
            for (int i = 0; i < basket.getProductsInBasket().size(); i++) {
                BasketPosition positionInBasket = basket.getProductsInBasket().get(i);
                if (Objects.equals(positionInBasket.getId(), productBasketId)) {
                    checkIfAvailable(positionInBasket);
                    OrderPosition orderPosition = mapper.basketPositionToOrderPosition(positionInBasket);
                    orderPosition.setProductCost(countCost(orderPosition));
                    wholePrice += orderPosition.getProductCost() * orderPosition.getQuantity();
                    orderPosition.setOrderId(order.getId());
                    orderPositionRepository.save(orderPosition);
                    orderPositionList.add(orderPosition);
                    basketService.removeBasketPosition(productBasketId);

                    productService.updateProductQuantityWhenOrder(
                            positionInBasket.getProduct().getId(),
                            positionInBasket.getQuantity());
                    break;
                }
                if (i == basket.getProductsInBasket().size() - 1) {
                    throw new EntityNotFoundException(
                            ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION
                                    .getMessage(productBasketId, BasketPosition.class)
                    );
                }
            }
        }
        if (orderPositionList.size() != basketPositionIds.size())
            throw new WrongConditionException("Выбраны некорректные позиции товаров.");

        order.setProductsOrdered(orderPositionList);
        order.setOrderCost(wholePrice);
        Order orderSave = orderRepository.save(order);
        createStats(orderSave);
        return orderSave;
    }

    private Order createNewEmptyOrder(long userId) {
        Buyer buyer = buyerService.getBuyer(userId);
        Order order = new Order();
        order.setBuyer(buyer);
        order.setProductionTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    private float countCost(OrderPosition orderPosition) {
        if (orderPosition.getInstallation()) {
            return orderPosition.getProduct().getPrice() +
                    orderPosition.getProduct().getInstallationPrice();
        } else return orderPosition.getProduct().getPrice();
    }

    private void checkIfAvailable(BasketPosition basketPosition) {
        if (basketPosition.getProduct().getQuantity() < basketPosition.getQuantity())
            throw new WrongConditionException("Товара у продавца осталось меньше, чем в заказе");
        if (basketPosition.getProduct().getProductStatus() != ProductStatus.PUBLISHED)
            throw new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label);
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(
                        ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.getMessage(orderId, Order.class)
                ));
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders(long userId) {
        Buyer buyer = buyerService.getBuyer(userId);
        return orderRepository.findAllByBuyer_Id(buyer.getId(), PageRequestOverride.ofSize(20));
    }

    private void createStats(Order order) {
        List<OrderPosition> orderPositionList = order.getProductsOrdered();
        for (OrderPosition orderPosition : orderPositionList) {
            statClient.addStats(mapper.orderPositionToStatDto(orderPosition)) ;
        }
    }
}
