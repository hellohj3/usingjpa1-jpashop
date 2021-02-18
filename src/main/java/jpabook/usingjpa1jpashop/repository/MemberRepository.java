package jpabook.usingjpa1jpashop.repository;

import jpabook.usingjpa1jpashop.doamin.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
}
