package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import Element.*;

@SuppressWarnings("serial")
public class MoneyManageGUI extends JFrame {
    private Money[] money;

    public MoneyManageGUI(Money[] money) {
        this.money = money;
        setupFrame();
        setVisible(true); // 프레임을 보이게 설정
    }

    private void setupFrame() {
        // 프레임 설정
        setTitle("화폐 관리");
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(null); // 화면 중앙에 배치
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Container cont = getContentPane(); // Container 객체를 가져옵니다.
        cont.setLayout(new BorderLayout()); // Container의 레이아웃 설정

        new Menu(); // 메뉴바 생성, Container 객체를 전달
        cont.add(new MainPanel(), BorderLayout.CENTER); // 메인 패널 추가, Container를 이용
    }

    class Menu {
        private JMenuBar menuBar;
        private JMenu menu;
        private JMenuItem exitMenuItem;

        public Menu() { // 생성자에 Container 매개변수 추가
            // 메뉴바 설정
            menuBar = new JMenuBar();
            menu = new JMenu("메뉴");
            exitMenuItem = new JMenuItem("종료");

            // 종료 메뉴 아이템에 액션 리스너 추가
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        dispose(); // 프레임 종료
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(MoneyManageGUI.this, "프로그램 종료 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            menu.add(exitMenuItem); // 메뉴에 종료 아이템 추가
            menuBar.add(menu); // 메뉴바에 메뉴 추가
            setJMenuBar(menuBar); // 프레임에 메뉴바 설정
        }
    }

    class MainPanel extends JPanel {
        public MainPanel() {
            // 메인 패널 설정
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            for (int i = 0; i < money.length; i++) {
                try {
                    add(new MoneyPanel(i)); // 각 화폐 패널 추가
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MoneyManageGUI.this, "화폐 패널을 추가하는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    class MoneyPanel extends JPanel {
        public MoneyPanel(int index) { // 'i'를 'index'로 이름 변경하여 더 명확하게 함
            // 화폐 패널 설정
            setLayout(new GridLayout(1, 4));

            try {
                // 화폐 정보 라벨 생성
                JLabel moneyPrice = new JLabel(String.valueOf(money[index].getPrice()) + " 원", SwingConstants.CENTER);
                JLabel moneyAmount = new JLabel(String.valueOf(money[index].getAmount()) + " 개", SwingConstants.CENTER);
                JButton moneyAmountChange = new JButton("보충");
                JButton moneyAmountCollect = new JButton("수금");

                // 보충 버튼 액션 리스너 추가
                moneyAmountChange.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (money[index].getAmount() < 10) {
                                int count = money[index].reset();
                                JOptionPane.showMessageDialog(MoneyManageGUI.this, money[index].getPrice() + " 원을 " + count + " 개 보충했습니다.");
                            } else {
                                JOptionPane.showMessageDialog(MoneyManageGUI.this, "10 개 미만일때 보충 가능합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                            }
                            moneyAmount.setText(String.valueOf(money[index].getAmount()) + " 개"); // 라벨 업데이트
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(MoneyManageGUI.this, "보충 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                // 수금 버튼 액션 리스너 추가
                moneyAmountCollect.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            if (money[index].getAmount() > 10) {
                                int count = money[index].collectMoney();
                                JOptionPane.showMessageDialog(MoneyManageGUI.this, money[index].getPrice() + " 원을 " + count + " 개 수금했습니다.");
                            } else {
                                JOptionPane.showMessageDialog(MoneyManageGUI.this, "10 개 초과일때 수금 가능합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                            }
                            moneyAmount.setText(String.valueOf(money[index].getAmount()) + " 개"); // 라벨 업데이트
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(MoneyManageGUI.this, "수금 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                // 패널에 컴포넌트 추가
                add(moneyPrice);
                add(moneyAmount);
                add(moneyAmountChange);
                add(moneyAmountCollect);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MoneyManageGUI.this, "화폐 패널을 초기화하는 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
