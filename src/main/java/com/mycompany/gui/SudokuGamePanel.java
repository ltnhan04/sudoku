package com.mycompany.gui;

import com.mycompany.gui.panels.RulesPanel;
import com.mycompany.gui.panels.GamePanel;
import com.mycompany.gui.panels.HomePanel;
import com.mycompany.gui.panels.WelcomePanel;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;


public class SudokuGamePanel extends JPanel {

    private final WelcomePanel welcomePanel;
    private final CardLayout cardLayoutManager = new CardLayout();
    private final JPanel content = new JPanel();
    private final HomePanel homePanel;
    private final GamePanel gamePanel;
    private final RulesPanel rulesPanel;


    public SudokuGamePanel() {

        this.setLayout(new GridLayout());

        // Panel Setup
        this.welcomePanel = new WelcomePanel();
        this.homePanel = new HomePanel();
        this.gamePanel = new GamePanel();
        this.rulesPanel = new RulesPanel();

        // Content View Panels
        content.setLayout(cardLayoutManager);
        content.add(this.welcomePanel);
        content.add(this.homePanel);
        content.add(this.gamePanel);
        content.add(this.rulesPanel);

        // Set Panel Identifiers
        cardLayoutManager.addLayoutComponent(this.welcomePanel, "welcome");
        cardLayoutManager.addLayoutComponent(this.homePanel, "home");
        cardLayoutManager.addLayoutComponent(this.gamePanel, "game");
        cardLayoutManager.addLayoutComponent(this.rulesPanel, "rules");

        // Set Start Up Panel
        cardLayoutManager.show(content, "welcome");

        // Add Content
        this.add(content);
    }

    /**
     * @return the welcomePanel
     */
    public WelcomePanel getWelcomePanel() {
        return welcomePanel;
    }

    /**
     * @return the homePanel
     */
    public HomePanel getHomePanel() {
        return homePanel;
    }

    /**
     * @return the gamePanel
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    /**
     * @return the cardLayoutManager
     */
    public CardLayout getCardLayoutManager() {
        return cardLayoutManager;
    }

    /**
     * @return the content
     */
    public JPanel getContent() {
        return content;
    }

    /**
     * @return the rulesPanel
     */
    public RulesPanel getRulesPanel() {
        return rulesPanel;
    }
}
