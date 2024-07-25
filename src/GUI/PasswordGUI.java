package GUI;

import javax.swing.*;
import Element.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// JFrame을 상속하여 비밀번호 입력 GUI 구현
@SuppressWarnings("serial")
public class PasswordGUI extends JFrame {

    private PasswordPanel passwordPanel;
    private Drink[] drinks;
    private Money[] userMoney;
    private Money[] vmMoney;
    private Integer sum;

    public PasswordGUI(Drink[] drinks, Money[] userMoney, Money[] vmMoney, Integer sum) {
        this.drinks = drinks;  // 전달받은 Drink 객체 배열 저장
        this.userMoney = userMoney; // 전달받은 userMoney 값 저장
        this.vmMoney = vmMoney; // 전달받은 vmMoney 값 저장
        this.sum = sum; // 전달받은 sum 값 저장
        
        setupFrame();
        setVisible(true); // 프레임 표시
    }

    // 프레임 기본 설정
    private void setupFrame() {
        setTitle("비밀번호 입력");
        setSize(400, 100);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Container cont = getContentPane(); // 컨테이너 가져오기
        cont.setLayout(new BorderLayout()); // BorderLayout 설정

        passwordPanel = new PasswordPanel();
        cont.add(passwordPanel, BorderLayout.CENTER); // 컨테이너에 패널 추가
        cont.add(new ButtonPanel(), BorderLayout.SOUTH); // 컨테이너에 버튼 패널 추가
    }

    // 비밀번호 입력 패널
    class PasswordPanel extends JPanel {
        private JLabel passwordLabel = new JLabel("비밀번호");
        private JPasswordField passwordField = new JPasswordField(20);

        public PasswordPanel() {
            setLayout(new FlowLayout()); // 좌->우 배치
            
            passwordField.setEchoChar((char) 0); // 초기 echo char 미사용
            passwordField.setText("초기 비밀번호: 비밀번호123!@#");

            // 포커스 리스너 추가
            passwordField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    // 포커스 시 기본 텍스트 삭제
                    if ("초기 비밀번호: 비밀번호123!@#".equals(new String(passwordField.getPassword()))) {
                        passwordField.setText("");
                        passwordField.setEchoChar('*'); // 입력 '*'로 표시
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // 포커스 잃으면 텍스트 복원
                    if (new String(passwordField.getPassword()).isEmpty()) {
                        passwordField.setText("초기 비밀번호: 비밀번호123!@#");
                        passwordField.setEchoChar((char) 0); // echo char 미사용
                    }
                }
            });

            add(passwordLabel);
            add(passwordField);
        }

        // 비밀번호 반환
        public String getPassword() {
            return new String(passwordField.getPassword());
        }
    }

    // 버튼 패널
    class ButtonPanel extends JPanel {
        private JButton passwordButton = new JButton("확인");
        private JButton exitButton = new JButton("취소");

        public ButtonPanel() {
            add(passwordButton);
            add(exitButton);

            // 확인 버튼 리스너
            passwordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String enteredPassword = passwordPanel.getPassword();
                    if (isPasswordMatch(enteredPassword)) {
                        SwingUtilities.getWindowAncestor(ButtonPanel.this).dispose(); // 창 닫기
                        new ManagerGUI(drinks, userMoney, vmMoney, sum); // ManagerGUI 실행
                    } else {
                        JOptionPane.showMessageDialog(PasswordGUI.this, "비밀번호 불일치.", "오류", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // 취소 버튼 리스너
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.getWindowAncestor(ButtonPanel.this).dispose(); // 창 닫기
                    new VendingMachineGUI(drinks, userMoney, vmMoney, sum);
                }
            });

            // 로그인 버튼 초점 설정
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    passwordButton.requestFocusInWindow();
                }
            });
        }
        // SHA-256 해시 함수
        private String hashPassword(String password) {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes());
                return Base64.getEncoder().encodeToString(hash);
            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(ButtonPanel.this, 
                                              "암호화 알고리즘을 찾을 수 없습니다.", 
                                              "오류", 
                                              JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        // 비밀번호 일치 확인
        private boolean isPasswordMatch(String enteredPassword) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("txt/password.txt"));
                String storedPasswordHash = reader.readLine();
                String enteredPasswordHash = hashPassword(enteredPassword);
                return storedPasswordHash != null && storedPasswordHash.equals(enteredPasswordHash);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(ButtonPanel.this, 
                                              "패스워드 파일을 찾을 수 없습니다.", 
                                              "오류", 
                                              JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ButtonPanel.this, 
                                              "파일 읽기 오류가 발생했습니다.", 
                                              "오류", 
                                              JOptionPane.ERROR_MESSAGE);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        // 여기서는 별도의 처리가 필요하지 않습니다.
                    }
                }
            }
            return false;
        }
    }
}
    

