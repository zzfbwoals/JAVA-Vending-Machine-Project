/* 일별 / 월별 매출 GUI
 * 
 * 
 */
package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class PurchaseGUI extends JFrame {
    private JTable table;
    private JComboBox<String> comboBoxDrink;
    private JComboBox<String> comboBoxMonth;
    private DefaultTableModel tableModel;
    private Map<String, Integer> salesMap;
    private List<String[]> allData;

    public PurchaseGUI() {
        setTitle("일별 / 월별 매출"); // 창 제목 설정
        setSize(400, 500); // 창 크기 설정
        setResizable(false); // 크기 조절 불가 설정
        setLocationRelativeTo(null); // 화면 중앙에 배치
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // 종료 동작 설정

        salesMap = new HashMap<>();
        allData = loadData("txt/data.txt"); // 파일로부터 데이터 로드

        // 파일로부터 로드한 데이터에서 월 정보 추출하여 콤보박스에 설정
        Set<String> months = allData.stream()
                                    .map(row -> row[0].substring(0, 7))
                                    .collect(Collectors.toSet());
        
        comboBoxMonth = new JComboBox<>(new Vector<>(months));
        comboBoxMonth.insertItemAt("모든 데이터", 0); // '모든 데이터' 옵션 추가
        comboBoxMonth.setSelectedIndex(0); // 기본 선택 항목 설정
        comboBoxMonth.addActionListener(e -> filterData()); // 이벤트 리스너 추가

        comboBoxDrink = new JComboBox<>(new Vector<>(salesMap.keySet()));
        comboBoxDrink.insertItemAt("모든 음료", 0); // '모든 음료' 옵션 추가
        comboBoxDrink.setSelectedIndex(0); // 기본 선택 항목 설정
        comboBoxDrink.addActionListener(e -> filterDataByDrink()); // 이벤트 리스너 추가

        String[] columnNames = {"날짜", "음료", "가격", "수량", "총액"}; // 테이블 컬럼 이름
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        loadTableData(allData); // 테이블에 데이터 로드

        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout());

        // 상단 패널에 컴포넌트 추가
        topPanel.add(new JLabel("월별 선택:"));
        topPanel.add(comboBoxMonth);
        topPanel.add(new JLabel("음료 선택:"));
        topPanel.add(comboBoxDrink);

        // 전체 패널 구성
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // getContentPane()을 사용하여 Content Pane을 가져옴
        Container cont = getContentPane(); 
        cont.add(panel); // 최종 패널을 Content Pane에 추가
        new Menu();
        setVisible(true);
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
                    dispose(); // 프레임 종료
                }
            });

            menu.add(exitMenuItem); // 메뉴에 종료 아이템 추가
            menuBar.add(menu); // 메뉴바에 메뉴 추가
            setJMenuBar(menuBar); // 프레임에 메뉴바 설정
        }
    }

    // 파일로부터 데이터를 로드하는 메서드
    private List<String[]> loadData(String fileName) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                data.add(parts);

                // 음료별 총 매출 계산
                String drink = parts[1];
                int total = Integer.parseInt(parts[4]);
                salesMap.put(drink, salesMap.getOrDefault(drink, 0) + total);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 콤보박스에 음료 목록 업데이트
        if (comboBoxDrink != null) {
            comboBoxDrink.setModel(new DefaultComboBoxModel<>(new Vector<>(salesMap.keySet())));
            comboBoxDrink.insertItemAt("모든 음료", 0);
            comboBoxDrink.setSelectedIndex(0);
        }

        return data;
    }

    // 테이블에 데이터를 로드하는 메서드
    private void loadTableData(List<String[]> data) {
        tableModel.setRowCount(0); // 테이블 초기화
        for (String[] row : data) {
            tableModel.addRow(row); // 행 데이터 추가
        }
    }

    // 콤보박스 선택에 따라 데이터 필터링 메서드
    private void filterData() {
        String selectedMonth = (String) comboBoxMonth.getSelectedItem();
        String selectedDrink = (String) comboBoxDrink.getSelectedItem();

        List<String[]> filteredData = allData;

        // 월별 필터링
        if (selectedMonth != null && !selectedMonth.equals("모든 데이터")) {
            filteredData = filteredData.stream()
                                       .filter(row -> row[0].startsWith(selectedMonth))
                                       .collect(Collectors.toList());
        }

        // 음료별 필터링
        if (selectedDrink != null && !selectedDrink.equals("모든 음료")) {
            filteredData = filteredData.stream()
                                       .filter(row -> row[1].equals(selectedDrink))
                                       .collect(Collectors.toList());
        }

        loadTableData(filteredData); // 필터링된 데이터로 테이블 업데이트

        // 총 매출 표시
        if (!filteredData.isEmpty()) {
            int totalSales = filteredData.stream().mapToInt(row -> Integer.parseInt(row[4])).sum();
            String[] totalRow = {"", "총 매출", "", "", String.valueOf(totalSales)};
            tableModel.addRow(totalRow);
        }
    }

    // 음료 선택에 따른 데이터 필터링 메서드
    private void filterDataByDrink() {
        filterData();
    }

}
