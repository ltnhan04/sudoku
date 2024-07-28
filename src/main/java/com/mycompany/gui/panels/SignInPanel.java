package com.mycompany.gui.panels;

import com.mycompany.gui.AppJButton;
import static com.mycompany.gui.SudokuGame.APP_GREEN;
import static com.mycompany.gui.SudokuGame.BKGD_DARK_GRAY;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class SignInPanel extends JPanel {

    private final JTextField emailText;
    private final JPasswordField passwordText;
    private final AppJButton signupButton;
    private final AppJButton signinButton;

    public SignInPanel() {
        this.setLayout(new GridLayout(7, 0));
        this.setBackground(BKGD_DARK_GRAY);

        JLabel actionLabel = new JLabel("ĐĂNG NHẬP");
        actionLabel.setFont(new Font("Avenir", Font.PLAIN, 24));
        actionLabel.setForeground(Color.white);
        actionLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(actionLabel);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        emailLabel.setForeground(Color.white);
        emailLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(emailLabel);

        emailText = new JTextField();
        emailText.setBackground(BKGD_DARK_GRAY);
        emailText.setForeground(Color.white);
        emailText.setHorizontalAlignment(JLabel.CENTER);
        emailText.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        emailText.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, APP_GREEN));
        this.add(emailText);

        JLabel passwordLabel = new JLabel("MẬT KHẨU");
        passwordLabel.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.white);
        passwordLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBackground(BKGD_DARK_GRAY);
        passwordText.setForeground(Color.white);
        passwordText.setHorizontalAlignment(JLabel.CENTER);
        passwordText.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        passwordText.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, APP_GREEN));
        this.add(passwordText);

        signinButton = new AppJButton("ĐĂNG NHẬP", 14, APP_GREEN, BKGD_DARK_GRAY);
        this.add(signinButton);

        signupButton = new AppJButton("TÔI CHƯA ĐĂNG KÍ", 10, BKGD_DARK_GRAY, APP_GREEN);
        this.add(signupButton);
    }

    public void clear() {
        emailText.setText("");
        passwordText.setText("");
    }


    public JButton getSignupButton() {
        return signupButton;
    }


    public JButton getSigninButton() {
        return signinButton;
    }


    public JTextField getEmailText() {
        return emailText;
    }


    public JPasswordField getPasswordText() {
        return passwordText;
    }
}
