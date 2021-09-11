package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.swing.text.html.Option;
import java.util.Optional;

public class AutowiredTest {
    @Test
    void AutowiredOption() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);


    }

    static class TestBean {
        /*자동주입 할 빈이 없으면 메소드 자체가 호출 안된다.*/
        @Autowired(required = false)
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }

        /*자동주입 할 빈이 없으면 null이 들어간다.*/
        @Autowired
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);
        }

        /*자동주입 할 빈이 없으면 Optional.empty가 들어간다.*/
        @Autowired
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }

    }
}
