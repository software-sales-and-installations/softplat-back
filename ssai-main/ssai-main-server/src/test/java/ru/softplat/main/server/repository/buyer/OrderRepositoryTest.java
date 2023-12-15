package ru.softplat.main.server.repository.buyer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.softplat.main.server.model.buyer.Buyer;
import ru.softplat.main.server.model.buyer.Order;
import ru.softplat.main.server.model.buyer.OrderPosition;
import ru.softplat.main.server.model.product.Product;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTest {
    @Mock
    private OrderRepository orderRepository;

    @Test
    public void testFindOrderByBuyerIdAndProductId() {
        Buyer buyer = new Buyer();
        buyer.setId(1L);

        Product product = new Product();
        product.setId(1L);

        OrderPosition orderPosition = new OrderPosition();
        orderPosition.setId(1L);
        orderPosition.setOrderId(1L);
        orderPosition.setProduct(product);

        Order order = new Order();
        order.setId(1L);
        order.setBuyer(buyer);
        order.getProductsOrdered().add(orderPosition);

        when(orderRepository.findOrderByBuyerIdAndProductId(1L, 1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderRepository.findOrderByBuyerIdAndProductId(1L, 1L);

        assertEquals(Optional.of(order), result);

        verify(orderRepository, times(1)).findOrderByBuyerIdAndProductId(1L, 1L);
    }
}