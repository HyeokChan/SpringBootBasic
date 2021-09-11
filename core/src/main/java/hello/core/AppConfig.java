package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*팩토리 메소드를 통한 방법*/
@Configuration
public class AppConfig {

    //@Bean memberService => new MemoryMemberRepository()
    //@Bean orderService => new MemoryMemberRepository()


    // @Bean memberService -> new MemoryMemberRepository()
    // @Bean orderService -> new MemoryMemberRepository()
    // 결과적으로 2개의 MemoryMemberRepository가 생성되면서 싱글톤이 깨지는 것 처럼 보인다.

    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.memberRepository
    //call AppConfig.orderService
    //call AppConfig.memberRepository
    //=>
    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.orderService
    //==> 싱글톤을 보장한다.

    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        //return new OrderServiceImpl(memberRepository(), discountPolicy());
        return null;
    }
    @Bean
    public DiscountPolicy discountPolicy(){
        /*return new FixDiscountPolicy();*/
        /*정률 할인 정책으로 변경*/
        return new RateDiscountPolicy();
    }
}
