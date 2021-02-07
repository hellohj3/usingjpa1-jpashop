package jpabook.usingjpa1jpashop.controller;

import jpabook.usingjpa1jpashop.doamin.Member;
import jpabook.usingjpa1jpashop.doamin.Order;
import jpabook.usingjpa1jpashop.doamin.item.Item;
import jpabook.usingjpa1jpashop.repository.OrderSearch;
import jpabook.usingjpa1jpashop.service.ItemService;
import jpabook.usingjpa1jpashop.service.MemberService;
import jpabook.usingjpa1jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        // Transaction 안에서 Entity 를 관리하기위해 Controller 에서는 식별자만 넘기고
        // 나머지는 비지니스 로직에서 조회 및 수정 로직을 수행한다
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cacelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
