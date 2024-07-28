
package com.mycompany.gui.panels;

import com.mycompany.gui.AppJButton;
import static com.mycompany.gui.SudokuGame.APP_GREEN;
import static com.mycompany.gui.SudokuGame.BKGD_DARK_GRAY;
import static com.mycompany.gui.SudokuGame.BKGD_LIGHT_GRAY;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class HomePanel extends JPanel {
    
    private final JButton signoutBtn;
    private final JButton newGameBtn;
    private final JButton viewRulesBtn;
    private JTable tableScores;
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel levelSelectionModel = new DefaultComboBoxModel();
    private final JComboBox levelSelector;
    private JLabel nameLabel;
    

    public HomePanel() {
        this.setLayout(new BorderLayout());

        JPanel banner = new JPanel();
        banner.setLayout(new BoxLayout(banner, BoxLayout.LINE_AXIS));
        banner.setPreferredSize(new Dimension(1000, 115));
        banner.setBackground(BKGD_DARK_GRAY);
        banner.setAlignmentX(CENTER_ALIGNMENT);
            

            
            banner.add(Box.createRigidArea(new Dimension(5,0)));
            banner.add(Box.createRigidArea(new Dimension(15,0)));

            JPanel jP2 = new JPanel();
            jP2.setBackground(BKGD_DARK_GRAY);
            jP2.setPreferredSize(new Dimension(200, 100));
            jP2.setLayout(new GridLayout(2,0));
                JLabel welcomeLabel = new JLabel("CHÀO MỪNG");
                welcomeLabel.setFont(new Font("Avenir", Font.PLAIN, 28));
                welcomeLabel.setForeground(Color.WHITE);
                welcomeLabel.setVerticalAlignment(JLabel.BOTTOM);
                jP2.add(welcomeLabel);

                nameLabel = new JLabel("NGƯỜI CHƠI");
                nameLabel.setFont(new Font("Avenir", Font.PLAIN, 18));
                nameLabel.setForeground(Color.WHITE);
                nameLabel.setVerticalAlignment(JLabel.TOP);
                jP2.add(nameLabel);
            banner.add(jP2);
            
        this.add(banner, BorderLayout.NORTH);

        JPanel main = new JPanel();
        main.setLayout(new GridLayout(0,2));

            JPanel left = new JPanel();
            left.setBackground(BKGD_DARK_GRAY);
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
                
                JPanel game = new JPanel();
                game.setLayout(new GridLayout(2,0));
                game.setMaximumSize(new Dimension(300, 120));
                game.setBackground(BKGD_LIGHT_GRAY);
                    newGameBtn = new AppJButton("BẮT ĐẦU GAME MỚI", 20, BKGD_LIGHT_GRAY, APP_GREEN);
                    game.add(newGameBtn);
                    
                    levelSelector = new JComboBox();
                    levelSelector.setModel(levelSelectionModel);
                    game.add(levelSelector);
                    
                left.add(Box.createVerticalStrut(50));
                left.add(game);
                
                game = new JPanel();
                game.setLayout(new GridLayout());
                game.setMaximumSize(new Dimension(300, 120));
                    viewRulesBtn = new AppJButton("LUẬT LỆ", 20, BKGD_LIGHT_GRAY, APP_GREEN);
                    game.add(viewRulesBtn);
                left.add(Box.createVerticalStrut(50));
                left.add(game);
                
                JPanel actions = new JPanel();
                actions.setLayout(new GridLayout());
                actions.setMaximumSize(new Dimension(140, 30));
                    signoutBtn = new AppJButton("ĐĂNG XUẤT", 14, BKGD_LIGHT_GRAY, APP_GREEN);
                    actions.add(signoutBtn);
                left.add(Box.createVerticalGlue());
                left.add(actions);
            main.add(left);

            JPanel right = new JPanel();
            right.setBackground(BKGD_DARK_GRAY);
            right.setLayout(null);
                    
                tableScores = new JTable() {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                
                tableModel = (DefaultTableModel) tableScores.getModel(); 
                tableModel.setColumnIdentifiers(new String[] {"Điểm", "Người chơi","Thời gian(s)"});
                tableScores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                tableScores.getTableHeader().setFont(new Font(this.getFont().getFontName(), Font.PLAIN, this.getFont().getSize()));
                tableScores.getTableHeader().setBackground(BKGD_LIGHT_GRAY);
                tableScores.getTableHeader().setForeground(APP_GREEN);
                tableScores.getTableHeader().setReorderingAllowed(true);
                tableScores.setAutoCreateRowSorter(true);
                tableScores.setFont(new Font("Halvetica Neue", Font.PLAIN, 14));
                tableScores.setBackground(BKGD_LIGHT_GRAY);
                tableScores.setForeground(APP_GREEN);
                tableScores.setShowVerticalLines(false);
                tableScores.setShowHorizontalLines(false);
                tableScores.getRowSorter().toggleSortOrder(0);
                
                JScrollPane scores = new JScrollPane(tableScores);
                scores.setLocation(100, 50);
                scores.setSize(300, 270);
                scores.setBorder(new LineBorder(Color.BLACK,0));
                scores.getViewport().setBackground(BKGD_LIGHT_GRAY);
                
                right.add(scores);
                
            main.add(right);

        this.add(main);
    }

    public JButton getSignoutBtn() {
        return signoutBtn;
    }


    public JButton getNewGameBtn() {
        return newGameBtn;
    }

    public JComboBox getLevelSelector() {
        return levelSelector;
    }

 
    public JButton getViewRulesBtn() {
        return viewRulesBtn;
    }


    public JLabel getNameLabel() {
        return nameLabel;
    }


    public JTable getHighscores() {
        return tableScores;
    }


    public void setHighscores(JTable highscores) {
        this.tableScores = highscores;
    }


    public DefaultTableModel getTableModel() {
        return tableModel;
    }


    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }


    public DefaultComboBoxModel getLevelSelectionModel() {
        return levelSelectionModel;
    }
}
