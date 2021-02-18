package jpabook.usingjpa1jpashop.api;

import jpabook.usingjpa1jpashop.doamin.Address;
import jpabook.usingjpa1jpashop.doamin.Order;
import jpabook.usingjpa1jpashop.doamin.OrderItem;
import jpabook.usingjpa1jpashop.doamin.OrderStatus;
import jpabook.usingjpa1jpashop.repository.OrderRepository;
import jpabook.usingjpa1jpashop.repository.OrderSearch;
import jpabook.usingjpa1jpashop.repository.order.query.OrderFlatDto;
import jpabook.usingjpa1jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.usingjpa1jpashop.repository.order.query.OrderQueryDto;
import jpabook.usingjpa1jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.yaml.snakeyaml.nodes.NodeId.mapping;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(orderItem -> orderItem.getItem().getName());
        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrdersDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrdersDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(toList());

        return collect;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrdersDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.finfAllWithMemberDelivery(offset, limit);
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(toList());

        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        // 왜안대 ㅅㅂ
        /*return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());*/
        return flats;
    }

    @Data
    static class OrdersDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrdersDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.orderPrice = orderItem.getCount();
        }

    }
}
