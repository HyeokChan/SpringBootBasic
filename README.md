# Springboot Basic

## 개발 / 학습 환경
- Project : Gradle Project
- Spring Boot : 2.3
- Language : Java
- Pajaging : Jar
- Java : 11

## 비즈니스 요구사항 설계
### 회원
	1. 회원을 가입하고 조회 할 수 있다.
	2. 회원은 일반과 VIP 두 가지 등급이 있다.
	3. 회원 데이터는 자체 DB를 구축할 수 있고, 외부 시스템과 연동할 수 있다. (미확정)

### 주문과 할인 정책
	1. 회원은 상품을 주문할 수 있다.
	2. 회원 등급에 따라 할인 정책을 적용할 수 있다.
	3. 할인 정책은 모든 VIP는 1000원을 할인해주는 고정 금액 할인을 적용해달라. (나중에 변경 될 수 있다.)
	4. 할인 정책은 변경 가능성이 높다.

## 회원 도메인 설계
![domain](https://user-images.githubusercontent.com/48059565/136662498-b0cd1077-252a-4001-b7e9-e4c356e1f6f8.jpg)

## 주문 도메인 설계
![order_domain](https://user-images.githubusercontent.com/48059565/136662702-df78cbfe-008b-4652-a6e1-5540b808ddb3.jpg)

![order_domain2](https://user-images.githubusercontent.com/48059565/136662906-9ff0be2c-6536-4ef2-971a-efb93213a240.jpg)

## 새로운 할인 정책 적용의 문제점과 해결과정
	1. 역할과 구현을 분리해야 한다.
	2. OCP, DIP와 같은 객체지향 설계 원칙을 준수해야 한다.
	3. 의존성 주입을 사용하지 않고, 구현체를 new 키워드를 통해 생성하면 DIP 원칙을 지킬 수 없다.
	(클라이언트인 OrderServiceImpl이 DiscountPolicy인터페이스 뿐만 아니라 FixDiscountPolicy구현체에도 의존하고 있다.)
	4. 애플리케이션의 전체 동작 방식을 구성하기 위해 구현객체를 생성하고, 연결하는 책임을 가지는 별도의 설정 클래스를 만든다. (AppConfig)
	5. MemberServiceImpl의 생성자를 통해서 어떤 구현 객체가 주입될지는 외부 AppConfig클래스에서 결정한다. 
![appconfig](https://user-images.githubusercontent.com/48059565/136663374-a03afbe7-27f8-4690-8fc7-573a5f2167ba.jpg)
### 좋은 객체 지향 설계의 5가지 원칙의 적용
#### SRP 단일 책임 원칙 > 한 클래스는 하나의 책임만 가져야 한다.
	1. 클라이언트 객체는 직접 구현 객체를 생성하고, 연결하고, 실행하는 다양한 책임을 가지고 있음
	2. SRP 단일 책임 원칙을 따르면서 관심사를 분리함
	3. 구현 객체를 생성하고 연결하는 책임은 AppConfig가 담당
	4. 클라이언트 객체는 실행하는 책임만 담당
#### DIP 의존관계 역전 원칙 > 프로그래머는 "추상화에 의존해야지, 구체화에 의존하면 안된다" 의존성 주입은 이 원칙을 따르는 방법 중 하나다.
	1. 새로운 할인 정책을 개발하고, 적용하려고 하니 클라이언트 코드도 함께 변경해야 했다. 왜냐하면 기존
	클라이언트 코드( OrderServiceImpl )는 DIP를 지키며 DiscountPolicy 추상화 인터페이스에
	의존하는 것 같았지만, FixDiscountPolicy 구체화 구현 클래스에도 함께 의존했다. (new 구체화 클래스)
	2. 클라이언트 코드가 DiscountPolicy 추상화 인터페이스에만 의존하도록 코드를 변경했다.
	3. 하지만 클라이언트 코드는 인터페이스만으로는 아무것도 실행할 수 없다.
	4. AppConfig가 FixDiscountPolicy 객체 인스턴스를 클라이언트 코드 대신 생성해서 클라이언트
	코드에 의존관계를 주입했다. 이렇게해서 DIP 원칙을 따르면서 문제도 해결했다.
#### OCP > 소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.
	1. 다형성 사용하고 클라이언트가 DIP를 지킴
	2. 애플리케이션을 사용 영역과 구성 영역으로 나눔
	3. AppConfig가 의존관계를 FixDiscountPolicy RateDiscountPolicy 로 변경해서 클라이언트
	코드에 주입하므로 클라이언트 코드는 변경하지 않아도 됨
	4. 소프트웨어 요소를 새롭게 확장해도 사용 영역의 변경은 닫혀 있다!
#### IOC > 제어의 역전
	1. 기존 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성하고, 연결하고, 실행했다.
	한마디로 구현 객체가 프로그램의 제어 흐름을 스스로 조종했다. 개발자 입장에서는 자연스러운 흐름이다.
	2. 반면에 AppConfig가 등장한 이후에 구현 객체는 자신의 로직을 실행하는 역할만 담당한다. 프로그램의
	제어 흐름은 이제 AppConfig가 가져간다. 예를 들어서 OrderServiceImpl 은 필요한 인터페이스들을
	호출하지만 어떤 구현 객체들이 실행될지 모른다.
	3. 프로그램에 대한 제어 흐름에 대한 권한은 모두 AppConfig가 가지고 있다. 심지어 OrderServiceImpl
	도 AppConfig가 생성한다. 그리고 AppConfig는 OrderServiceImpl 이 아닌 OrderService
	인터페이스의 다른 구현 객체를 생성하고 실행할 수 도 있다. 그런 사실도 모른체 OrderServiceImpl 은
	묵묵히 자신의 로직을 실행할 뿐이다.
	4. 이렇듯 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 관리하는 것을 제어의 역전(IoC)이라
	한다.
#### 의존관계 주입 DI
	1. OrderServiceImpl 은 DiscountPolicy 인터페이스에 의존한다. 실제 어떤 구현 객체가 사용될지는
	모른다.
	2. 의존관계는 정적인 클래스 의존 관계와, 실행 시점에 결정되는 동적인 객체(인스턴스) 의존 관계 둘을
	분리해서 생각해야 한다.
		- 정적인 클래스 의존관계 > 클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단할 수 있다. ( OrderServiceImpl - MemberRepository / OrderServiceImpl - DiscountPolicy )
		- 동적인 객체 인스턴스 의존 관계 > 애플리케이션 실행 시점(런타임)에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달한다. ( MemoryMemberRepository, RateDiscountPolicy )
#### IoC 컨테이너, DI 컨테이너
	1. AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해 주는 것을 Ioc 컨테이너 또는 DI 컨테이너라고 한다.

> #### 프레임워크 vs 라이브러리
>  프레임워크가 내가 작성한 코드를 제어하고, 대신 실행하면 그것은 프레임워크다
>  반면에 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 프레임워크가 아니라 라이브러리다.

## 스프링 컨테이너와 스프링 빈
### 스프링 컨테이너의 생성과정
	1. 스프링 컨테이너 생성
	- new AnnotationConfigApplicationContext(AppConfig.class)
		- 스프링 컨테이너를 생성할 때는 구성 정보를 지정해주어야 한다.
	2. 스프링 빈 등록
		- 빈 이름은 메서드 이름을 사용한다.
		- 빈 이름을 직접 부여할 수 도 있다.
			@Bean(name="memberService2")
	3. 스프링 빈 의존관계 설정
		- 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.
### 컨테이너에 등록된 모든 빈 조회
	1. 모든 빈 출력하기
		- ac.getBeanDefinitionNames() : 스프링에 등록된 모든 빈 이름을 조회한다.
		- ac.getBean() : 빈 이름으로 빈 객체(인스턴스)를 조회한다.
	2. 애플리케이션 빈 출력하기
		- 스프링이 내부에서 사용하는 빈은 getRole() 로 구분할 수 있다.
		- ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈
		- ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
### 스프링 빈 조회 - 기본
	1. 스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법
		- ac.getBean(빈이름, 타입)
		- ac.getBean(타입)
		- 조회 대상 스프링 빈이 없으면 예외 발생 : NoSuchBeanDefinitionException: No bean named 'xxxxx' available
### 스프링 빈 조회 - 동일한 타입이 둘 이상
	1. 타입으로 조회 시 같은 타입의 스프링 빈이 둘 이상이면 오류가 발생한다. 이때는 빈 이름을 지정하자.
	2. ac.getBeansOfType() 을 사용하면 해당 타입의 모든 빈을 조회할 수 있다.
### 스프링 빈 조회 - 상속관계
	1. 부모 타입으로 조회하면, 자식 타입도 함께 조회한다.
	2. 그래서 모든 자바 객체의 최고 부모인 Object 타입으로 조회하면, 모든 스프링 빈을 조회한다.
### BeanFactory와 ApplicationContext
	1. ApplicationContext는 BeanFactory의 기능을 상속받는다.
	2. ApplicationContext는 빈관리가능 + 편리한 부가 기능을 제공한다
		- 메시지소스를 활용한 국제화 기능
		- 환경변수
		- 애플리케이션 이벤트
		- 편리한 리소스 조회
	3. BeanFactory를 직접 사용할 일은 거의 없다. 부가기능이 포함된 ApplicationContext를 사용한다.
	4. BeanFactory나 ApplicationContext를 스프링 컨테이너라고 한다.
### 다양한 설정 형식 지원 - 자바 코드, XML
	1. 애노테이션 기반 자바 코드 설정
		- AnnotationConfigApplicationContext
	2. XML 설정 사용
		- GenericXmlApplicationContext
### 스프링 빈 설정 메타정보 - BeanDefinition
	1. 스프링이 다양한 형식을 지원할 수 있는 이유
	2. 역할과 구현을 개념적으로 나눴다
		- XML을 읽어서 BeanDefinition을 만들면 된다.
		- 자바 코드를 읽어서 BeanDefinition을 만들면 된다.
		- 스프링 컨테이너는 자바 코드인지, XML인지 몰라도 된다. 오직 BeanDefinition만 알면된다.
![beanDefinition](https://user-images.githubusercontent.com/48059565/136667936-b3ce9078-bda3-4ff5-8371-a89bbd32b146.jpg)

## 싱글톤 컨테이너
### 싱글톤 패턴
	1. 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴이다.
	2. 객체 인스턴스를 2개 이상 생성하지 못하도록 막아야한다.
		- private 생성자를 사용하여 외부에서 임의로 new 키워드를 사용하지 못하도록 막아야 한다.
#### 싱글톤 패턴 문제점
	1. 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
	2. 의존관계상 클라이언트가 구체 클래스에 의존한다 > DIP를 위반한다.
	3. 클라이언트가 구체 클래스에 의존해서 OCP 원칙을 위반할 가능성이 높다.
	4. 테스트하기 어렵다.
	5. 내부 속성을 변경하거나 초기화 하기 어렵다.
	6. private 생성자로 자식 클래스를 만들기 어렵다.
	7. 결론적으로 유연성이 떨어진다.
	8. 안티패턴으로 불리기도 한다.
### 싱글톤 컨테이너
> 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다.
#### 싱글톤 컨테이너
	1. 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
	2. 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤	레지스트리라 한다.
	3.스프링 컨테이너의 이런 기능 덕분에 싱글턴 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
	4.싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도 된다.
	5.DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용할 수 있다.
#### 싱글톤 방식의 주의점
	1. 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다.
	2. 무상태(stateless)로 설계해야 한다!
		- 특정 클라이언트에 의존적인 필드가 있으면 안된다.
		- 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다!
		- 가급적 읽기만 가능해야 한다.
		- 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
### @Configuration과 바이트코드 조작의 마법
	1. 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한다.
	2. @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
	3. 덕분에 싱글톤이 보장되는 것이다.
## 컴포넌트 스캔
- 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을
제공한다.
- 컴포넌트 스캔은 이름 그대로 @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로
등록한다.
	
		- 빈 이름 기본 전략: MemberServiceImpl 클래스 memberServiceImpl
		- 빈 이름 직접 지정: 만약 스프링 빈의 이름을 직접 지정하고 싶으면 @Component("memberService2") 이런식으로 이름을 부여하면 된다.
- 생성자에 @Autowired 를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.
### 컴포넌트 스캔 기본 대상
	- @Component : 컴포넌트 스캔에서 사용
	- @Controlller : 스프링 MVC 컨트롤러로 인식
	- @Service : 스프링 핵심 비즈니스 로직에서 사용
	- @Repository : 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다.
	- @Configuration : 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.
## 의존관계 자동 주입
- 생성자 주입
		- 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
		- 불변, 필수 의존관계에 사용
- 수정자 주입(setter 주입)
		- 선택, 변경 가능성이 있는 의존관계에 사용
		- 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.
- 필드 주입
		- 코드가 간결해서 많은 개발자들을 유혹하지만 외부에서 변경이 불가능해서 테스트 하기 힘들다는	치명적인 단점이 있다.
		-	DI 프레임워크가 없으면 아무것도 할 수 없다.
		- 사용하지 말자!
 > 참고: @Autowired 의 기본 동작은 주입할 대상이 없으면 오류가 발생한다. 
 > 주입할 대상이 없어도 동작하게 하려면 @Autowired(required = false) 로 지정하면 된다.
### 옵션 처리
	 - @Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
	 - org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다.
	 - Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.
#### final 키워드
	생성자 주입을 사용하면 필드에 final 키워드를 사용할 수 있다. 그래서 생성자에서 혹시라도 값이
	설정되지 않는 오류를 컴파일 시점에 막아준다.
### 롬복과 최신 트랜드
	롬복 라이브러리가 제공하는 @RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서
	생성자를 자동으로 만들어준다.
### 조회 대상 빈이 2개 이상일 때 해결 방법
	1. @Autowired 필드 명 매칭
		- 타입 매칭
		- 타입 매칭의 결과가 2개 이상일 때 필드 명, 파라미터 명으로 빈 이름 매칭
	2. @Qualifier @Qualifier끼리 매칭 빈 이름 매칭
		- @Qualifier끼리 매칭
		- 빈 이름 매칭
		- NoSuchBeanDefinitionException 예외 발생
	3. @Primary 사용
		- @Primary 는 우선순위를 정하는 방법이다. @Autowired 시에 여러 빈이 매칭되면 @Primary 가 우선권을 가진다.
### 자동, 수동의 올바른 실무 운영 기준
	업무 로직 빈(자동): 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는
	리포지토리등이 모두 업무 로직이다. 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
	기술 지원 빈(수동): 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나,
	공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.
## 빈 생명주기 콜백



## 빈 스코프
