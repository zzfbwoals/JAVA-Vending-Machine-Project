package Element;

public class Money {
    private int price;  // 화폐의 가격
    private int amount; // 화폐의 개수

    // 생성자
    public Money(int price, int amount) {
        this.price = price;
        this.amount = amount;
    }

    // 화폐 개수 1 증가
    public void inAmount() {
        amount++;
    }

    // 화폐 개수 num만큼 증가
    public void inAmount2(int num) {
        amount += num;
    }

    // 화폐 개수 1 감소
    public void deAmount() {
        amount--;
    }

    // 화폐 개수 num만큼 감소
    public void deAmount2(int num) {
        amount -= num;
    }

    // 화폐 개수 반환
    public int getAmount() {
        return amount;
    }

    // 화폐 가격 반환
    public int getPrice() {
        return price;
    }

    // 화폐가 없는지 확인
    public boolean isEmpty() {
        return amount == 0;
    }

    // 화폐가 가득 찼는지 확인
    public boolean isFull() {
        return amount == 10;
    }

    // 화폐 개수 보충
    public int reset() {
        int count = 0;
        while (this.amount < 10) {
            inAmount();
            count++;
        }
        return count;
    }

    // 수금 메소드 (거스름돈을 위해 10개 남겨놓기)
    public int collectMoney() {
        int count = 0;
        while (this.amount > 10) {
            deAmount();
            count++;
        }
        return count;
    }
}
