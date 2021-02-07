package jpabook.usingjpa1jpashop.service;

import jpabook.usingjpa1jpashop.doamin.Delivery;
import jpabook.usingjpa1jpashop.doamin.Member;
import jpabook.usingjpa1jpashop.doamin.Order;
import jpabook.usingjpa1jpashop.doamin.OrderItem;
import jpabook.usingjpa1jpashop.doamin.item.Item;
import jpabook.usingjpa1jpashop.repository.ItemRepository;
import jpabook.usingjpa1jpashop.repository.MemberRepository;
import jpabook.usingjpa1jpashop.repository.OrderRepository;
import jpabook.usingjpa1jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배솟정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
