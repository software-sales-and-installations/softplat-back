package ru.yandex.workshop.main.service.buyer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.workshop.main.dto.basket.OrderMapper;
import ru.yandex.workshop.main.dto.basket.OrderResponseDto;
import ru.yandex.workshop.main.dto.basket.OrderToCreateDto;
import ru.yandex.workshop.main.dto.basket.ProductOrderMapper;
import ru.yandex.workshop.main.exception.EntityNotFoundException;
import ru.yandex.workshop.main.exception.WrongConditionException;
import ru.yandex.workshop.main.message.ExceptionMessage;
import ru.yandex.workshop.main.model.buyer.Order;
import ru.yandex.workshop.main.model.buyer.ProductOrder;
import ru.yandex.workshop.main.repository.buyer.OrderRepository;
import ru.yandex.workshop.main.repository.buyer.ProductBasketRepository;
import ru.yandex.workshop.security.model.user.Buyer;
import ru.yandex.workshop.security.repository.BuyerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final ProductBasketRepository productBasketRepository;

    @Transactional
    public OrderResponseDto createOrder(String email, OrderToCreateDto orderToCreateDto) {
        if (orderToCreateDto.getProductBaskets() == null || orderToCreateDto.getProductBaskets().size() == 0)
            throw new EntityNotFoundException("Перед оформлением заказа добавьте товары в корзину");
        Buyer buyer = buyerRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        Order order = new Order();
        order.setBuyer(buyer);
        order.setProductionTime(LocalDateTime.now());
        List<ProductOrder> productOrderList = new ArrayList<>();
        float wholePrice = 0F;
        for (Long productBasketId : orderToCreateDto.getProductBaskets()) {
            ProductOrder productOrder = ProductOrderMapper.INSTANCE.productBasketToProductOrder(
                    productBasketRepository.findById(productBasketId).orElseThrow(()
                    -> new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label)));
            if (productOrder.getProduct().getQuantity() >= productOrder.getQuantity()){
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
        return OrderMapper.INSTANCE.orderToOrderDto(orderRepository.save(order));
    }

    public OrderResponseDto getOrder(String email, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));
        if (!order.getBuyer().getEmail().equals(email))
            throw new WrongConditionException("Ошибка при выполнении запроса. Попробуйте ввести корректный orderId");
        return OrderMapper.INSTANCE.orderToOrderDto(order);
    }

    /*public List<OrderResponseDto> getAllOrders(String email) {
        Buyer buyer = buyerRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException(ExceptionMessage.ENTITY_NOT_FOUND_EXCEPTION.label));

    }*/
}
