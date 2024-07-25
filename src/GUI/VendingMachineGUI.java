 package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import Element.*;

import java.awt.*;
import javax.swing.border.Border;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@SuppressWarnings("serial")
public class VendingMachineGUI extends JFrame {
	
    public Drink[] drinks = new Drink[6]; // 6개의 음료 배열 생성
    public Money[] vmMoney = new Money[5]; // 자판기 안의 화폐 (거스름돈)
    public Money[] userMoney = new Money[5]; // 사용자의 화폐
    public JLabel moneyLabel = new JLabel(); // 금액 표시 라벨
    public Integer sum; // 투입 금액
    private JLabel[] drinkLabel = new JLabel[6]; // 음료 이름, 가격, 이미지 라벨 배열
    private JButton[] buyButton = new JButton[6]; // 구매 버튼 배열
    private JButton returnButton = new JButton("반환"); // 반환 버튼
    
    // VendingMachineGUI 기본 생성자
    public VendingMachineGUI() {
        savePasswordToFile(); // 초기 비밀번호 설정
        initDrink(); // 음료 초기화
        initvmMoney(); // 자판기 화폐 초기화
        inituserMoney(); // 사용자 화폐 초기화
        sum = 0;
        
        // 금액 표시 라벨 업데이트
        moneyLabel.setText("금액: " + sum + " 원 ");
        
        setupFrame(); // 프레임 설정
        setVisible(true); // 창을 화면에 표시
    }
    
    // 초기화 이후 생성자
    public VendingMachineGUI(Drink[] drinks, Money[] userMoney, Money[] vmMoney, Integer sum) {
        this.drinks = drinks; // 음료 정보 저장
        this.userMoney = userMoney; // 사용자 화폐 정보 저장
        this.vmMoney = vmMoney; // 자판기 화폐 정보 저장
        this.sum = sum; // 현재 금액 정보 저장
        
        // 금액 표시 라벨 업데이트
        moneyLabel.setText("금액: " + sum + " 원 ");
        
        setupFrame(); // 프레임 설정
        updateBuyButton();
        updateDrinkPanel(); // 음료 패널 업데이트
        setVisible(true); // 창을 화면에 표시
    }
    
    // 음료 정보 초기화 메소드
    private void initDrink() {
        drinks[0] = new Drink("물", 450, 10);
        drinks[1] = new Drink("커피", 500, 10);
        drinks[2] = new Drink("이온음료", 550, 10);
        drinks[3] = new Drink("고급커피", 700, 10);
        drinks[4] = new Drink("탄산음료", 750, 10);
        drinks[5] = new Drink("특화음료", 800, 10);
    }
    
    // 자판기 화폐 정보 초기화 메소드
    private void initvmMoney() {
        vmMoney[0] = new Money(10, 10);
        vmMoney[1] = new Money(50, 10);
        vmMoney[2] = new Money(100, 10);
        vmMoney[3] = new Money(500, 10);
        vmMoney[4] = new Money(1000, 10);
    }
    
    // 사용자 화폐 정보 초기화 메소드
    public void inituserMoney() {
        userMoney[0] = new Money(10, 5);
        userMoney[1] = new Money(50, 5);
        userMoney[2] = new Money(100, 5);
        userMoney[3] = new Money(500, 5);
        userMoney[4] = new Money(1000, 5);
    }
    
    // 프레임 기본 설정 메소드
    private void setupFrame() {
        setTitle("자판기");
        setSize(450, 380);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Container cont = getContentPane(); // 컨테이너 가져오기
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS)); // 컨테이너 레이아웃 설정

        new Menu(); // 메뉴 생성
        cont.add(new MainPanel()); // 메인 패널 추가
    }
    
    // 메뉴 클래스 정의
    class Menu {

        private JMenuBar menuBar = new JMenuBar(); // 메뉴 바 생성
        private JMenu menu = new JMenu("메뉴"); // 메뉴 생성
        private JMenuItem managerMenuItem = new JMenuItem("관리자"); // 관리자 메뉴 항목 생성
        private JMenuItem exitMenuItem = new JMenuItem("종료"); // 종료 메뉴 항목 생성
        
        public Menu() {
            // 종료 메뉴 항목에 이벤트 리스너 추가
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // 프로그램 종료
                }
            });

            managerMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        new PasswordGUI(drinks, userMoney, vmMoney, sum); // 관리자 인증 GUI 생성
                        dispose(); // 현재 창 닫기
                    } catch (Exception ex) {
                        ex.printStackTrace(); // 예외 처리
                    }
                }
            });

            menu.add(managerMenuItem); // 메뉴에 관리자 항목 추가
            menu.addSeparator(); // 메뉴에 구분선 추가
            menu.add(exitMenuItem); // 메뉴에 종료 항목 추가
            menuBar.add(menu); // 메뉴 바에 메뉴 추가
            setJMenuBar(menuBar); // JFrame에 메뉴 바 설정
        }
    }
    
    // 자판기의 메인 패널을 정의하는 클래스
    class MainPanel extends JPanel {
        
        // 메인 패널 생성자
        public MainPanel() {
            // 수직 박스 레이아웃 설정
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            
            // 각 기능별 패널을 메인 패널에 추가
            add(new DrinkPanel()); // 음료 선택 패널
            add(new BuyButtonPanel()); // 구매 버튼 패널
            add(new CoinBillPanel()); // 동전/지폐 투입 패널
            add(new MoneyReturnPanel()); // 금액 반환 패널
        }
    }

    // 음료 선택 패널 클래스
    class DrinkPanel extends JPanel {
        
        private ImageIcon[] drinkImg = new ImageIcon[6]; // 음료 이미지 배열
        private String[] drinkName = {
            "img/water.jpg", "img/Coffee.jpg", "img/Ion.jpg", 
            "img/goodCoffee.jpg", "img/cola.jpg", "img/specialDrink.jpg"
        }; // 음료 이미지 파일 경로
        
        private String[] drinkString = {
            "<html><body><div style='text-align: center;'>"+ drinks[0].getName() +"<br>"+ drinks[0].getPrice() +"</body></html>",
            "<html><body><div style='text-align: center;'>"+ drinks[1].getName() +"<br>"+ drinks[1].getPrice() +"</body></html>",
            "<html><body><div style='text-align: center;'>"+ drinks[2].getName() +"<br>"+ drinks[2].getPrice() +"</body></html>",
            "<html><body><div style='text-align: center;'>"+ drinks[3].getName() +"<br>"+ drinks[3].getPrice() +"</body></html>",
            "<html><body><div style='text-align: center;'>"+ drinks[4].getName() +"<br>"+ drinks[4].getPrice() +"</body></html>",
            "<html><body><div style='text-align: center;'>"+ drinks[5].getName() +"<br>"+ drinks[5].getPrice() +"</body></html>"
        }; // 음료 이름 및 가격 정보 문자열 배열

        // 음료 패널 생성자
        public DrinkPanel() {
            try {
                for (int i = 0; i < drinkLabel.length; i++) {
                    drinkImg[i] = new ImageIcon(drinkName[i]); // 이미지 아이콘 생성
                    drinkLabel[i] = new JLabel(drinkString[i], drinkImg[i], JLabel.CENTER); // 라벨 생성 및 설정

                    // 텍스트 위치 설정
                    drinkLabel[i].setHorizontalTextPosition(JLabel.CENTER); // 텍스트 수평 위치 설정
                    drinkLabel[i].setVerticalTextPosition(JLabel.TOP); // 텍스트 수직 위치 설정

                    add(drinkLabel[i]); // 패널에 라벨 추가
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("음료 라벨 배열 인덱스 초과: " + e.getMessage());
            } catch (NullPointerException e) {
                System.err.println("음료 라벨 초기화 오류: " + e.getMessage());
            }
        }
    }

    // 음료 구매 가능 여부를 업데이트하는 메서드
    public void updateDrinkPanel() {
        try {
            // 빨간색 테두리 생성
            Border redBorder = BorderFactory.createLineBorder(Color.RED, 2);
            for (int i = 0; i < drinkLabel.length; i++) {
                if (sum >= drinks[i].getPrice()) // 음료를 구매할 수 있는 상태이면
                    drinkLabel[i].setBorder(redBorder);
                else
                    drinkLabel[i].setBorder(null);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("음료 라벨 배열 인덱스 초과: " + e.getMessage());
        } catch (NullPointerException e) {
            System.err.println("음료 라벨 초기화 오류: " + e.getMessage());
        }
    }

    // 구매 버튼 패널 클래스
    class BuyButtonPanel extends JPanel {

        // 구매 버튼 패널 생성자
        public BuyButtonPanel() {
            updateBuyButton(); // 품절 상태 업데이트
            
            for (int i = 0; i < buyButton.length; i++) {
                int index = i;
                buyButton[i] = new JButton("구매"); // 구매 버튼 생성
                add(buyButton[i]); // 패널에 버튼 추가
                
                // 구매 버튼 클릭 이벤트 리스너
                buyButton[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (drinks[index].getAmount() > 0) { // 음료가 품절이 아닌 경우
                                if (sum == 0 || (sum - drinks[index].getPrice()<0)) {
                                    JOptionPane.showMessageDialog(VendingMachineGUI.this, "금액이 부족합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                                } else if (!returnCheck(drinks, index)) { // 거스름돈을 반환할 수 없으면
                                    JOptionPane.showMessageDialog(VendingMachineGUI.this, "거스름돈이 부족합니다. 투입한 금액을 반환 후 음료 가격에 맞는 금액을 넣어주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    drinks[index].buy(); // 음료 1개 감소
                                    sum -= drinks[index].getPrice(); // 투입 금액 - 음료 가격
                                    moneyLabel.setText("금액: " + sum + " 원 "); // 투입 금액 업데이트
                                    updateDrinkPanel(); // 음료 구매 가능 여부 업데이트
                                }
                            } else { // 음료가 품절인 경우
                                JOptionPane.showMessageDialog(VendingMachineGUI.this, "품절입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                            }
                            updateBuyButton(); // 품절 상태 업데이트
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            System.err.println("구매 버튼 배열 인덱스 초과: " + ex.getMessage());
                        } catch (NullPointerException ex) {
                            System.err.println("구매 버튼 초기화 오류: " + ex.getMessage());
                        } catch (Exception ex) {
                            System.err.println("구매 과정 중 오류 발생: " + ex.getMessage());
                        }
                    }
                });
            }
        }
    }

    // 구매 버튼 품절 상태 업데이트 메서드
    private void updateBuyButton() {
        try {
            for (int i = 0; i < buyButton.length; i++) {
                if (drinks[i].getAmount() == 0) { // 음료가 품절이거나 거스름돈을 반환할 수 없을 때
                    buyButton[i].setText("품절");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("구매 버튼 배열 인덱스 초과: " + e.getMessage());
        } catch (NullPointerException ex) {
            System.err.println("구매 버튼 초기화 오류: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("구매 과정 중 오류 발생: " + ex.getMessage());
        }
    }

    
    // 동전/지폐 패널 클래스
    class CoinBillPanel extends JPanel {
        
        private JButton[] coinbillButton = new JButton[5]; // 동전/지폐 버튼 배열
        private String[] coinbillString = {"10", "50", "100", "500", "1000"}; // 동전/지폐 금액 문자열 배열
        
        public CoinBillPanel() {
            // 각 동전/지폐 버튼을 초기화하고 이벤트 리스너를 추가하는 부분
            for (int i = 0; i < coinbillButton.length; i++) {
                int index = i;
                coinbillButton[i] = new JButton(coinbillString[i]); // 동전/지폐 버튼 생성
                add(coinbillButton[i]); // 패널에 버튼 추가
                
                // 버튼 클릭 이벤트
                coinbillButton[i].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // 사용자가 가진 동전/지폐가 없는 경우
                            if (userMoney[index] == null || userMoney[index].isEmpty()) {
                                JOptionPane.showMessageDialog(VendingMachineGUI.this, coinbillString[index] + "원이 더이상 없습니다.",
                                        "오류", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // 동전/지폐를 사용하여 금액 추가
                                vmMoney[index].inAmount();
                                userMoney[index].deAmount();
                                sum += userMoney[index].getPrice();
                                updateDrinkPanel(); // 음료 구매 가능 여부 업데이트
                                
                                // 총 금액이 7000원을 초과하는 경우
                                if (sum > 7000) {
                                    JOptionPane.showMessageDialog(VendingMachineGUI.this, "7000원 이상 투입할 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                                    sum -= userMoney[index].getPrice(); // 금액을 되돌림
                                    userMoney[index].inAmount();
                                    vmMoney[index].deAmount();
                                }
                                moneyLabel.setText("금액: " + sum + " 원 "); // 금액 라벨 업데이트
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            JOptionPane.showMessageDialog(VendingMachineGUI.this, "잘못된 인덱스 접근: " + ex.getMessage(),
                                    "오류", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(VendingMachineGUI.this, "예기치 않은 오류가 발생했습니다: " + ex.getMessage(),
                                    "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        }
    }

    // 금액 반환 패널 클래스
    class MoneyReturnPanel extends JPanel {
        public MoneyReturnPanel() {
            // 반환 버튼 클릭시 동작 구현
            returnButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 반환할 금액이 없는 경우
                        if (sum == 0) {
                            JOptionPane.showMessageDialog(VendingMachineGUI.this, "반환할 금액이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // 금액 반환 처리
                            returnMoney();
                            moneyLabel.setText("금액: " + sum + " 원 "); // 금액 라벨 업데이트
                            updateDrinkPanel(); // 음료 구매 가능 여부 업데이트
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(VendingMachineGUI.this, "예기치 않은 오류가 발생했습니다: " + ex.getMessage(),
                                "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            add(moneyLabel); // 패널에 금액 라벨 추가
            add(returnButton); // 패널에 반환 버튼 추가
        }
    }
    
    // 거스름 돈 알고리즘
    public void returnMoney() {
        int count[] = new int[5];
        int temp; // 거스름돈 계산을 위한 임시 변수

        try {
            // sum의 null 여부를 확인하여 temp 값을 설정
            if (sum != null) {
                temp = sum; // sum이 null이 아니면, temp에 sum의 값을 할당
            } else {
                temp = 0; // sum이 null이면, temp에 0을 할당
            }

            // 1000원 부터 ~ 10원 까지 거스름돈 계산
            for (int i = 4; i >= 0; i--) {
                int index = i;
                int numCoins = temp / vmMoney[index].getPrice(); // 반환 가능한 화폐 수 계산
                numCoins = Math.min(numCoins, vmMoney[index].getAmount()); // 반환할 화폐 수와 보유한 화폐 수 중 작은 값 선택
                
                temp -= numCoins * vmMoney[index].getPrice(); // 거스름돈 계산 업데이트
                if (vmMoney[index].getAmount() > 0) { // 화폐가 1개 이상 있으면
                    vmMoney[index].deAmount2(numCoins); // 화폐 수량 감소
                }
                count[index] = numCoins; // 반환된 화폐 수 기록
                
                // 반환된 화폐가 있으면 출력
                if (numCoins > 0 && vmMoney[index].getAmount() >= 0) {
                    System.out.println(vmMoney[index].getPrice() + "원 " + numCoins + "개 반환");
                    userMoney[index].inAmount2(numCoins); // 사용자 화폐 수량 업데이트
                }
                if (temp == 0) break; // 모든 거스름돈을 반환했으면 반복 종료
            }
            sum = temp; // sum 업데이트
        } catch (ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(VendingMachineGUI.this, "잘못된 인덱스 접근: " + ex.getMessage(),
                    "오류", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(VendingMachineGUI.this, "예기치 않은 오류가 발생했습니다: " + ex.getMessage(),
                    "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    
 // 사용자가 투입한 금액에서 음료 금액을 뺀 돈을 거스름돈으로 반환할 수 있는지 확인하는 메소드
    public boolean returnCheck(Drink[] drinks, int j) {
        // drinks 배열과 인덱스 j에 대한 유효성 검사
        if (drinks == null || j < 0 || j >= drinks.length) {
            return false; // 유효하지 않은 인덱스 또는 null 배열이면 false 반환
        }
        
        Money[] tempvmMoney = new Money[5]; // 임시 vmMoney 배열 생성

        // vmMoney 배열 복사
        for (int i = 0; i < vmMoney.length; i++) {
            tempvmMoney[i] = new Money(vmMoney[i].getPrice(), vmMoney[i].getAmount());
        }

        if (sum == null || sum <= 0) {
            return false; // sum이 null이거나 0 이하이면 거슬러 줄 돈이 없으므로 false 반환
        }

        int temp = sum - drinks[j].getPrice(); // 음료 가격을 뺀 잔액을 temp에 할당

        // 거스름돈 반환 가능 여부 확인
        for (int i = 4; i >= 0; i--) {
            int index = i;
            int numCoins = temp / tempvmMoney[index].getPrice(); // 반환 가능한 화폐 수 계산
            numCoins = Math.min(numCoins, tempvmMoney[index].getAmount()); // 반환할 화폐 수와 보유한 화폐 수 중 작은 값 선택

            temp -= numCoins * tempvmMoney[index].getPrice(); // temp 업데이트

            if (temp == 0) {
                return true; // 거스름돈을 충분히 반환할 수 있으면 true 반환
            }
        }

        return temp == 0; // 루프 종료 후에도 temp가 0이면 거스름돈을 정확히 반환할 수 있으므로 true, 아니면 false 반환
    }

    // 비밀번호를 파일에 저장하는 메서드
    private void savePasswordToFile() {
        String initialPassword = "qlalfqjsgh123!@#"; // 초기 비밀번호
        String hashedPassword = hashPassword(initialPassword); // 해시로 변환된 비밀번호

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("txt/password.txt"))) {
            writer.write(hashedPassword); // 해시된 비밀번호 파일에 저장
        } catch (IOException ex) {
            // 오류 발생 시 메시지 박스를 통해 사용자에게 알림
            JOptionPane.showMessageDialog(VendingMachineGUI.this, "비밀번호 저장 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // SHA-256 해시 함수
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(VendingMachineGUI.this, "암호화 알고리즘을 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    
}
