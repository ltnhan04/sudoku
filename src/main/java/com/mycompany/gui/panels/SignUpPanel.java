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


public class SignUpPanel extends JPanel {

    private final JTextField fullnameText;
    private final JTextField emailText;
    private final JPasswordField passwordText;
    private final AppJButton signupButton;
    private final AppJButton signinButton;


    public SignUpPanel() {

        this.setLayout(new GridLayout(9, 0));
        this.setBackground(BKGD_DARK_GRAY);

        JLabel actionLabel = new JLabel("ĐĂNG KÍ HOẶC ĐĂNG NHẬP");
        actionLabel.setFont(new Font("Avenir", Font.PLAIN, 24));
        actionLabel.setForeground(Color.white);
        actionLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(actionLabel);

        JLabel fullnameLabel = new JLabel("HỌ VÀ TÊN");
        fullnameLabel.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
        fullnameLabel.setForeground(Color.white);
        fullnameLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(fullnameLabel);

        fullnameText = new JTextField();
        fullnameText.setBackground(BKGD_DARK_GRAY);
        fullnameText.setForeground(Color.white);
        fullnameText.setHorizontalAlignment(JLabel.CENTER);
        fullnameText.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
        fullnameText.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, APP_GREEN));
        this.add(fullnameText);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
        emailLabel.setForeground(Color.white);
        emailLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(emailLabel);

        emailText = new JTextField();
        emailText.setBackground(BKGD_DARK_GRAY);
        emailText.setForeground(Color.white);
        emailText.setHorizontalAlignment(JLabel.CENTER);
        emailText.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
        emailText.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, APP_GREEN));
        this.add(emailText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
        passwordLabel.setForeground(Color.white);
        passwordLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBackground(BKGD_DARK_GRAY);
        passwordText.setForeground(Color.white);
        passwordText.setHorizontalAlignment(JLabel.CENTER);
        passwordText.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
        passwordText.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, APP_GREEN));
        this.add(passwordText);

        signupButton = new AppJButton("ĐĂNG KÍ", 14, APP_GREEN, BKGD_DARK_GRAY);
        this.add(signupButton);

        signinButton = new AppJButton("TÔI ĐÃ ĐĂNG KÍ", 10, BKGD_DARK_GRAY, APP_GREEN);
        this.add(signinButton);

    }


    public void clear() {
        fullnameText.setText("");
        emailText.setText("");
        passwordText.setText("");
    }


    public JButton getSignupButton() {
        return signupButton;
    }


    public JButton getSigninButton() {
        return signinButton;
    }


    public JTextField getFullnameText() {
        return fullnameText;
    }


    public JTextField getEmailText() {
        return emailText;
    }


    public JPasswordField getPasswordText() {
        return passwordText;
    }
}
