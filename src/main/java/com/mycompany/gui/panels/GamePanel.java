package com.mycompany.gui.panels;

import com.mycompany.gui.AppJButton;
import static com.mycompany.gui.SudokuGame.APP_GREEN;
import static com.mycompany.gui.SudokuGame.BKGD_DARK_GRAY;
import static com.mycompany.gui.SudokuGame.BKGD_LIGHT_GRAY;
import static java.awt.Component.CENTER_ALIGNMENT;
import com.mycompany.gui.model.Cell;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class GamePanel extends JPanel {
    
    private List<Cell> viewCellList;
    private final JButton endGameBtn;
    private final JButton viewRulesBtn;
    private final JButton hintBtn;
    private final JButton undoBtn;
    private final JPanel grid;
    private JLabel levelTitle;
    private JLabel timeLabel;
    

    public GamePanel() {
        
        this.setLayout(new BorderLayout());

        JPanel banner = new JPanel();
        banner.setLayout(new BoxLayout(banner, BoxLayout.LINE_AXIS));
        banner.setPreferredSize(new Dimension(1000, 115));
        banner.setBackground(BKGD_DARK_GRAY);
        banner.setAlignmentX(CENTER_ALIGNMENT);
        

            
            banner.add(Box.createRigidArea(new Dimension(5,0)));
            
            JPanel jP2 = new JPanel();
            jP2.setBackground(BKGD_DARK_GRAY);
            jP2.setPreferredSize(new Dimension(200, 100));
            jP2.setLayout(new GridLayout(2,0));
            
                timeLabel = new JLabel();
                timeLabel.setFont(new Font("Avenir", Font.PLAIN, 36));
                timeLabel.setForeground(Color.WHITE);
                timeLabel.setVerticalAlignment(JLabel.BOTTOM);
                timeLabel.setHorizontalAlignment(JLabel.RIGHT);
                jP2.add(timeLabel);

                levelTitle = new JLabel();
                levelTitle.setFont(new Font("Avenir", Font.PLAIN, 24));
                levelTitle.setForeground(Color.WHITE);
                levelTitle.setVerticalAlignment(JLabel.TOP);
                levelTitle.setHorizontalAlignment(JLabel.RIGHT);
                jP2.add(levelTitle);
                
            banner.add(jP2);
            banner.add(Box.createRigidArea(new Dimension(15,0)));
        this.add(banner, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(null);
        main.setBackground(BKGD_DARK_GRAY);           
                       
            JPanel actions = new JPanel();
            actions.setLayout(new GridLayout(4,1));
            actions.setSize(135, 90);
            actions.setLocation(0, 415 - actions.getHeight());
            
                undoBtn = new AppJButton("UNDO",14,BKGD_LIGHT_GRAY, APP_GREEN );
                actions.add(undoBtn);
                hintBtn = new AppJButton("GỢI Ý", 14, BKGD_LIGHT_GRAY, APP_GREEN);
                actions.add(hintBtn);
            
                viewRulesBtn = new AppJButton("XEM LUẬT", 14, BKGD_LIGHT_GRAY, APP_GREEN);
                actions.add(viewRulesBtn);
            
                endGameBtn = new AppJButton("NGHỈ GAME", 14, BKGD_LIGHT_GRAY, APP_GREEN);
                actions.add(endGameBtn);
            main.add(actions);
            
            grid = new JPanel();
            grid.setLayout(new GridLayout(9, 9));
            grid.setPreferredSize(new Dimension(120, 120));
            grid.setMaximumSize(new Dimension(433, 433));
            grid.setBorder(new LineBorder(APP_GREEN, 2));
            grid.setBackground(BKGD_DARK_GRAY.darker());
            grid.setForeground(Color.white);
            grid.setLocation(285, 0);
            grid.setSize(400, 400);
            
        main.add(grid);          
        this.add(main);
    }    
    

    public JButton getEndGameBtn() {
        return endGameBtn;
    }
    public JButton getUndoBtn(){
        return undoBtn;
    }

    public JButton getViewRulesBtn() {
        return viewRulesBtn;
    }


    public JButton getHintBtn() {
        return hintBtn;
    }


    public JLabel getLevelTitle() {
        return levelTitle;
    }

    public void setLevelTitle(JLabel levelTitle) {
        this.levelTitle = levelTitle;
    }


    public JLabel getTimeLabel() {
        return timeLabel;
    }


    public void setTimeLabel(JLabel timeLabel) {
        this.timeLabel = timeLabel;
    }


    public JPanel getGrid() {
        return grid;
    }


    public List<Cell> getViewCellList() {
        return viewCellList;
    }


    public void setViewCellList(List<Cell> viewCellList) {
        this.viewCellList = viewCellList;
    }
}
