package hello.core.singleton;

public class SingletonServce {
    private static final SingletonServce instance = new SingletonServce();

    public static SingletonServce getInstance() {
        return instance;
    }

    /*생성자를 private로 선언해서 외부에서 new 키워드를 사용하여 객체를 생성하지 못하게 막는다.*/
    private SingletonServce(){

    }

    public void logic(){
        System.out.println("싱긍톤 객체 로직 호출");
    }



}
