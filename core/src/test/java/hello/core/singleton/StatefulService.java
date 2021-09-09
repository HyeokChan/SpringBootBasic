package hello.core.singleton;

public class StatefulService {
    private int price; //상태를 유지하는 필드 10000 => 20000

    public void order(String name, int price) {
        System.out.println("name = " + name + "price = " + price);
        this.price = price;  // 여기가 문제!
        // => 공유필드를 조심해야한다. 무상태로 설계해야한다.
    }

    public int getPrice(){
        return price;
    }


}
