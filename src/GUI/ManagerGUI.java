package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Element.*;

// 관리자 인터페이스를 위한 클래스
@SuppressWarnings("serial")
public class ManagerGUI extends JFrame {

    // 변수 선언
    private Drink[] drinks; // 음료 정보 배열
    private Money[] userMoney; // 사용자 돈 배열
	private Money[] vmMoney; // 자판기 돈 배열
	private Integer sum; // 총액
	
    // 생성자
    public ManagerGUI(Drink[] drinks, Money[] userMoney, Money[] vmMoney, Integer sum) {
    	this.drinks = drinks;  // 전달받은 Drink 객체 배열 저장
    	this.userMoney = userMoney; // 전달받은 userMoney 객체 배열 저장
    	this.vmMoney = vmMoney; // 전달받은 vmMoney 객체 배열 저장
    	this.sum = sum; // 전달받은 sum 값 저장
    	
        setupFrame(); // 프레임 설정 메소드 호출
        setVisible(true); // 프레임을 화면에 표시
    }

    // 프레임 기본 설정 메소드
    private void setupFrame() {
        setTitle("관리자"); // 프레임 제목 설정
        setResizable(false); // 크기 조정 불가 설정
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 닫기 버튼 동작 설정

        Container cont = getContentPane(); // ContentPane 가져오기
        cont.setLayout(new BorderLayout()); // 레이아웃 설정
        cont.add(new ButtonPanel(), BorderLayout.CENTER); // 버튼 패널 추가
        new Menu(); // 메뉴바 생성 및 추가
        pack(); // 창 크기 자동 조정
        setLocationRelativeTo(null); // 화면 가운데 배치
    }
    
    // 메뉴 클래스
    class Menu {

        // 변수 선언
        private JMenuBar menuBar;
        private JMenu menu;
        private JMenuItem exitMenuItem;

        public Menu() { 
            // 메뉴바 설정
            menuBar = new JMenuBar();
            menu = new JMenu("메뉴");
            exitMenuItem = new JMenuItem("종료");

            // 종료 메뉴 아이템에 액션 리스너 추가
            exitMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); // 프레임 종료
                    new VendingMachineGUI(drinks, userMoney, vmMoney, sum); // 자판기 GUI 호출
                }
            });

            menu.add(exitMenuItem); // 메뉴에 종료 아이템 추가
            menuBar.add(menu); // 메뉴바에 메뉴 추가
            setJMenuBar(menuBar); // 프레임에 메뉴바 설정
        }
    }

    // 버튼 패널 클래스
    class ButtonPanel extends JPanel {
        // 버튼 변수 선언
        private JButton passwordChangeGUIButton = new JButton("비밀번호 변경");
        private JButton purchaseGUIButton = new JButton("일별 / 월별 매출");
        private JButton drinkManageGUIButton = new JButton("재고 관리");
        private JButton MoneyManageGUIButton = new JButton("화폐 관리");

        // 생성자
        public ButtonPanel() {
            // 비밀번호 변경 버튼 추가 및 액션 리스너 설정
            add(passwordChangeGUIButton); 
            passwordChangeGUIButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new PasswordChangeGUI(); // 비밀번호 변경 GUI 호출
                }
            });

            // 일별 / 월별 매출 버튼 추가 및 액션 리스터 설정
            add(purchaseGUIButton);
            purchaseGUIButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new PurchaseGUI(); // 비밀번호 변경 GUI 호출
                }
            });

            // 재고 관리 버튼 추가 및 액션 리스너 설정
            add(drinkManageGUIButton); 
            drinkManageGUIButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new DrinkManageGUI(drinks); // 재고 관리 GUI 호출
                }
            });

            // 화폐 관리 버튼 추가 및 액션 리스너 설정
            add(MoneyManageGUIButton); 
            MoneyManageGUIButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new MoneyManageGUI(vmMoney); // 화폐 관리 GUI 호출
                }
            });
        }
    }
}
