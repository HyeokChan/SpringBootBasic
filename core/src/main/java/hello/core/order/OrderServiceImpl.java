package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


// @RequiredArgsConstructor : final 키워드가 붙은 객체를 가지는 생성자 만들어줌
@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    /*private final MemberRepository memberRepository = new MemoryMemberRepository();*/
    /*private final DiscountPolicy discountPolicy = new FixDiscountPolicy();*/
    /*private final DiscountPolicy discountPolicy = new RateDiscountPolicy();*/

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // 생성자주입은 객체가 빈으로 등록될 때 의존 관계 설정도 이루어진다.
    // 의존성 주입을 위해 빈을 찾을 때, 클래스명으로 먼저 검색하고 클래스가 여러개이면 여러개중에 필드명으로 찾는다.
    // @Qualifier :  추가 구분자
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        System.out.println("memberRepository = " + memberRepository);
        System.out.println("discountPolicy = " + discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    /*수정자주입*/
    /*private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }*/
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
