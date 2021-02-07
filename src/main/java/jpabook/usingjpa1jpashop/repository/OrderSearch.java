package jpabook.usingjpa1jpashop.repository;

import jpabook.usingjpa1jpashop.doamin.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
