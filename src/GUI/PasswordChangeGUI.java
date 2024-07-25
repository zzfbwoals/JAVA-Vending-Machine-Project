package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SuppressWarnings("serial")
public class PasswordChangeGUI extends JFrame {
    
    private PasswordPanel passwordPanel; // PasswordPanel 인스턴스를 멤버 변수로 선언

    public PasswordChangeGUI() {
        setupFrame();
        setVisible(true); // 프레임을 화면에 표시
    }
    
    // 프레임의 기본 설정을 수행하는 메소드입니다.
    private void setupFrame() {
        setTitle("비밀번호 변경");
        setSize(400, 100);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        Container cont = getContentPane(); // 컨텐트 팬 가져오기
        cont.setLayout(new BorderLayout()); // 컨텐트 팬의 레이아웃 설정

        passwordPanel = new PasswordPanel(); // 패널을 컨텐트 팬에 추가
        cont.add(passwordPanel, BorderLayout.CENTER);
        cont.add(new ButtonPanel(), BorderLayout.SOUTH);
    }

    // 비밀번호 입력 패널
    class PasswordPanel extends JPanel {

        private JLabel passwordLabel = new JLabel("비밀번호 변경");
        private JPasswordField passwordField = new JPasswordField(20);

        public PasswordPanel() {
            // 초기에는 echo char를 사용하지 않음
            passwordField.setEchoChar((char) 0); 
            passwordField.setText("숫자, 특수문자 포함 8자 이상 입력");

            // 포커스 리스너 추가
            passwordField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    // 포커스를 받으면 기본 텍스트 지우기
                    if (new String(passwordField.getPassword()).equals("숫자, 특수문자 포함 8자 이상 입력")) {
                        passwordField.setText("");
                        passwordField.setEchoChar('*'); // 사용자의 입력을 '*'로 표시
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    // 포커스를 잃으면 비어있으면 기본 텍스트 다시 표시
                    if (new String(passwordField.getPassword()).isEmpty()) {
                        passwordField.setText("숫자, 특수문자 포함 8자 이상 입력");
                        passwordField.setEchoChar((char) 0); // echo char를 다시 사용하지 않음
                    }
                }
            });

            add(passwordLabel);
            add(passwordField);
        }

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

            // 확인 버튼에 액션 리스너 추가
            passwordButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String password = passwordPanel.getPassword(); // 멤버 변수로 접근
                    
                    // 비밀번호 유효성 검사
                    if (isValidPassword(password)) {
                        savePasswordToFile(password);
                        // 현재 PasswordGUI 창 닫기
                        SwingUtilities.getWindowAncestor(ButtonPanel.this).dispose();
                    } else {
                        JOptionPane.showMessageDialog(ButtonPanel.this, 
                                                      "비밀번호는 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.", 
                                                      "오류", 
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            // 취소 버튼에 액션 리스너 추가
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 창 닫기
                    SwingUtilities.getWindowAncestor(ButtonPanel.this).dispose();
                }
            });

            // 프로그램 시작 시 로그인 버튼에 포커스 주기
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    passwordButton.requestFocusInWindow();
                }
            });
        }

        // 비밀번호 유효성 검사
        private boolean isValidPassword(String password) {
            if (password.length() < 8) return false;
            boolean hasDigit = false;
            boolean hasSpecialChar = false;
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) hasDigit = true;
                if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
            }
            return hasDigit && hasSpecialChar;
        }

        // 비밀번호를 파일에 저장 (SHA-256 해시 적용)
        private void savePasswordToFile(String password) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("txt/password.txt"))) {
                String hashedPassword = hashPassword(password);
                writer.write(hashedPassword);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(ButtonPanel.this, 
                                              "비밀번호 저장 중 오류가 발생했습니다.", 
                                              "오류", 
                                              JOptionPane.ERROR_MESSAGE);
            }
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
    }
}
