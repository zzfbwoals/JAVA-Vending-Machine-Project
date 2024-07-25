package Element;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Drink {
    private String name; // 음료 이름
    private int price;   // 음료 가격
    private int amount;  // 음료 수량

    // 기본 생성자
    public Drink() {
    }

    // 매개변수가 있는 생성자
    public Drink(String name, int price, int amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    // 이름 설정
    public void setName(String name) {
        this.name = name;
    }

    // 이름 반환
    public String getName() {
        return name;
    }

    // 수량 설정
    public void setAmount(int amount) {
        this.amount = amount;
    }

    // 수량 반환
    public int getAmount() {
        return amount;
    }

    // 가격 설정
    public void setPrice(int price) {
        this.price = price;
    }

    // 가격 반환
    public int getPrice() {
        return price;
    }

    // 음료 한 개 구매
    public void buy() {
        amount--;
        savePurchaseInfo(1);
        if (isEmpty()) {
            saveStockOutInfo(); // 재고 소진 시, 정보 저장
        }
    }

    // 음료 수량 보충
    public int reset() {
        int count = 0;
        while (this.amount < 10) {
            amount++;
            count++;
        }
        return count;
    }

    // 음료가 비었는지 확인
    public boolean isEmpty() {
        return amount == 0;
    }
    
    // 구매 정보 파일에 저장
    private void savePurchaseInfo(int quantity) {
        LocalDate date = LocalDate.now(); // Get the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);

        int revenue = price * quantity; // Calculate revenue

        String data = formattedDate + " " + name + " " + price + " " + quantity + " " + revenue + "\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txt/data.txt", true))) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println("데이터 저장 오류.");
            e.printStackTrace();
        }
    }
    
    // 재고 소진 정보 파일에 저장
    private void saveStockOutInfo() {
        LocalDate date = LocalDate.now(); // 현재 날짜 가져오기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        
        String data = formattedDate + " " + name + " 재고 소진\n";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txt/soldout.txt", true))) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println("재고 소진 정보 저장 오류.");
            e.printStackTrace();
        }
    }
}
