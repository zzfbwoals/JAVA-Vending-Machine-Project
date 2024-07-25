package GUI;

import javax.swing.*;
import Element.Drink;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Container;

@SuppressWarnings("serial")
public class DrinkManageGUI extends JFrame {

    private Drink[] drinks; // 음료 객체 배열 선언
	
    // 생성자
    public DrinkManageGUI(Drink[] drinks) {
        this.drinks = drinks;  // 전달받은 Drink 객체 배열 저장
        setupFrame(); // 프레임 설정 메소드 호출
        setVisible(true); // 프레임을 보이게 설정
    }

    // 프레임 설정 메소드
    private void setupFrame() {
        setTitle("재고 관리"); // 프레임 제목 설정
        setSize(300, 400); // 프레임 크기 설정
        setResizable(false); // 크기 조절 불가 설정
        setLocationRelativeTo(null); // 화면 중앙에 배치
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 종료 동작 설정

        Container cont = getContentPane(); // Container 객체를 가져옵니다.
        cont.setLayout(new BorderLayout()); // Container의 레이아웃을 BorderLayout으로 설정

        new Menu(); // 메뉴바 생성
        cont.add(new MainPanel(), BorderLayout.CENTER); // 메인 패널을 Container 중앙에 추가
    }

    // 메뉴 클래스
    class Menu {

        private JMenuBar menuBar;
        private JMenu menu;
        private JMenuItem exitMenuItem;

        public Menu() {
            menuBar = new JMenuBar(); // 메뉴바 생성
            menu = new JMenu("메뉴"); // 메뉴 생성
            exitMenuItem = new JMenuItem("종료"); // 종료 메뉴 아이템 생성

            // 종료 메뉴 아이템에 액션 리스너 추가
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); // 프레임 종료
                }
            });

            menu.add(exitMenuItem); // 메뉴에 종료 아이템 추가
            menuBar.add(menu); // 메뉴바에 메뉴 추가
            setJMenuBar(menuBar); // 프레임에 메뉴바 설정
        }
    }

    // 메인 패널 클래스
    class MainPanel extends JPanel {
        public MainPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 메인 패널 레이아웃 설정
            
            // 음료 패널 추가
            for (int i = 0; i < drinks.length; i++) {
                add(new DrinkPanel(i)); 
            }
        }
    }

    // 음료 패널 클래스
    class DrinkPanel extends JPanel {

        public DrinkPanel(int index) {
            setLayout(new GridLayout(2, 3)); // 음료 패널 레이아웃 설정

            // 음료 정보 라벨 생성
            JLabel drinksName = new JLabel(drinks[index].getName(), SwingConstants.CENTER);
            JLabel drinksPrice = new JLabel(String.valueOf(drinks[index].getPrice()), SwingConstants.CENTER);
            JLabel drinksAmount = new JLabel(String.valueOf(drinks[index].getAmount()), SwingConstants.CENTER);

            // 음료 정보 변경 버튼 생성
            JButton drinksNameChange = new JButton("변경");
            JButton drinksPriceChange = new JButton("변경");
            JButton drinksAmountChange = new JButton("보충");
            
            // 가격 변경 버튼 이벤트 리스너
            drinksPriceChange.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 새 가격 입력 받기
                        String newPrice = JOptionPane.showInputDialog(DrinkManageGUI.this, "음료 새 가격:", "가격 변경", JOptionPane.PLAIN_MESSAGE);
                        if (newPrice != null && !newPrice.trim().isEmpty()) {
                            int price = Integer.parseInt(newPrice); // 숫자 변환 시도
                            if (price < 0) {
                                throw new NumberFormatException("가격은 음수가 될 수 없습니다.");
                            }
                            drinks[index].setPrice(price); // 가격 업데이트
                            drinksPrice.setText(newPrice); // 라벨 업데이트
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(DrinkManageGUI.this, "유효한 숫자를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(DrinkManageGUI.this, "오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            
            // 이름 변경 버튼 이벤트 리스너
            drinksNameChange.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        // 새 이름 입력 받기
                        String newName = JOptionPane.showInputDialog(DrinkManageGUI.this, "새 음료 이름:", "이름 변경", JOptionPane.PLAIN_MESSAGE);
                        if (newName != null && !newName.trim().isEmpty()) {
                            drinks[index].setName(newName); // 이름 업데이트
                            drinksName.setText(newName); // 라벨 업데이트
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(DrinkManageGUI.this, "오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // 보충 버튼 이벤트 리스너
            drinksAmountChange.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if(drinks[index].getAmount() < 10) {
                            int count = drinks[index].reset(); // 보충 실행
                            JOptionPane.showMessageDialog(DrinkManageGUI.this, drinks[index].getName() + " 을(를) " + count + " 개 보충했습니다.");
                        }
                        else {
                            JOptionPane.showMessageDialog(DrinkManageGUI.this, "이미 최대 개수 (10개) 입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                        drinksAmount.setText(String.valueOf(drinks[index].getAmount())); // 라벨 업데이트
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(DrinkManageGUI.this, "오류가 발생했습니다: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // 패널에 컴포넌트 추가
            add(drinksName);
            add(drinksPrice);
            add(drinksAmount);
            add(drinksNameChange);
            add(drinksPriceChange);
            add(drinksAmountChange);
        }
    }
}
