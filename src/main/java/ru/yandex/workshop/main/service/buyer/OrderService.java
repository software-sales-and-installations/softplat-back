package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.configuration.PageRequestOverride;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.mapper.OrderPositionMapper;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Buyer;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.main.model.buyer.OrderPosition;
import ru.yandex.workshop.main.repository.buyer.BasketPositionRepository;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BuyerService buyerService;
    private final BasketPositionRepository basketPositionRepository;
    private final OrderPositionMapper mapper;

    @Transactional
    public Order createOrder(String email, List<Long> productBaskets) {
        Buyer buyer = buyerService.getBuyerByEmail(email);
        Order order = new Order();
        order.setBuyer(buyer);
        order.setProductionTime(LocalDateTime.now());
        List<OrderPosition> productOrderList = new ArrayList<>();
        float wholePrice = 0F;
        for (Long productBasketId : productBaskets) {
            OrderPosition productOrder = mapper.basketPositionToOrderPosition(
                    basketPositionRepository.findById(productBasketId).orElseThrow(()
                            -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
            if (productOrder.getProduct().getQuantity() >= productOrder.getQuantity()) {
                productOrder.setId(null);
                if (productOrder.getInstallation()) {
                    productOrder.setProductAmount(productOrder.getProduct().getPrice() +
                            productOrder.getProduct().getInstallationPrice());
                } else productOrder.setProductAmount(productOrder.getProduct().getPrice());
                wholePrice += productOrder.getProductAmount() * productOrder.getQuantity();
                productOrderList.add(productOrder);
            } else
                throw new WrongConditionException("Товара у продавца осталось меньше, чем в заказе");
        }
        order.setProductsOrdered(productOrderList);
        order.setOrderAmount(wholePrice);
        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
    }

    public List<Order> getAllOrders(String email) {
        Buyer buyer = buyerService.getBuyerByEmail(email);
        return orderRepository.findAllByBuyer_Id(buyer.getId(), PageRequestOverride.of(0, 20));
    }
}
