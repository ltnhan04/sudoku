package com.mycompany.gui.panels;

import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;


public class WelcomePanel extends JPanel {

    private final CardLayout cardLayoutManager = new CardLayout();
    private final SignUpPanel signUpPanel = new SignUpPanel();
    private final SignInPanel signInPanel = new SignInPanel();
    private final JPanel slider = new JPanel();


    public WelcomePanel() {

        this.setLayout(new GridLayout(0, 2));

        slider.setLayout(this.cardLayoutManager);
        slider.add(this.signUpPanel);
        slider.add(this.signInPanel);

        this.add(slider);
    }


    public CardLayout getCardLayoutManager() {
        return cardLayoutManager;
    }


    public JPanel getSlider() {
        return slider;
    }

    public SignUpPanel getSignUpPanel() {
        return signUpPanel;
    }


    public SignInPanel getSignInPanel() {
        return signInPanel;
    }
}
