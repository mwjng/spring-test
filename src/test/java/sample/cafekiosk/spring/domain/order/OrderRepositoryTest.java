package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("해당 일자에 결제완료된 주문들을 조회한다.")
    @Test
    void findOrdersBy() {
        // given
        LocalDate orderDate = LocalDate.of(2024, 10, 27);

        Order order1 = Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(orderDate.atStartOfDay())
                .products(List.of())
                .build();
        Order order2 = Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(orderDate.plusDays(1).atStartOfDay())
                .products(List.of())
                .build();
        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> orders = orderRepository.findOrdersBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED);

        // then
        assertThat(orders).hasSize(1)
                .extracting("orderStatus", "registeredDateTime")
                .contains(
                        tuple(OrderStatus.PAYMENT_COMPLETED, orderDate.atStartOfDay())
                );
    }
}