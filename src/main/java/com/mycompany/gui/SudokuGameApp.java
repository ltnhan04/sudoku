package com.mycompany.gui;

import com.mycompany.gui.model.Player;
import static com.mycompany.gui.SudokuGame.APP_GREEN;
import static com.mycompany.gui.SudokuGame.BKGD_DARK_GRAY;
import static com.mycompany.gui.SudokuGame.BKGD_LIGHT_GRAY;
import com.mycompany.gui.model.Cell;
import com.mycompany.gui.model.CellPosition;
import com.mycompany.gui.model.Difficulty;
import com.mycompany.gui.model.Generator;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;


public class SudokuGameApp extends JFrame {
    private Stack<Cell> undoStack;
    private final SudokuGame model;
    private final SudokuGamePanel view;
    private ArrayList<Player> listUsers;
    private String rulesCaller;
    private final KeyListener cellKeyListener;
    private final MouseListener cellMouseListener;


    public SudokuGameApp(String name) {
        super(name);        
        this.model = new SudokuGame();
        this.view = new SudokuGamePanel();
        this.undoStack = new Stack<>();
        getContentPane().add(this.view);
        setSize(1000, 650);
        setResizable(false);

        for (Difficulty diff : Difficulty.values()) {
            view.getHomePanel().getLevelSelectionModel().addElement(diff);
        }

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Thoát game", "Hủy"};
                int result = JOptionPane.showOptionDialog(getParent(), "Bạn có muốn thoát game không?\nKết quả sẽ không được lưu.", "Thoát?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (result == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        this.view.getWelcomePanel().getSignUpPanel().getSigninButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getWelcomePanel().getCardLayoutManager().next(view.getWelcomePanel().getSlider());
            }
        });
        this.view.getWelcomePanel().getSignUpPanel().getSignupButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpEvt();
            }
        });
        this.view.getWelcomePanel().getSignInPanel().getSignupButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getWelcomePanel().getCardLayoutManager().next(view.getWelcomePanel().getSlider());
            }
        });
        this.view.getWelcomePanel().getSignInPanel().getSigninButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signInEvt();
            }
        });

        this.view.getHomePanel().getNewGameBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Difficulty level = Difficulty.valueOf(view.getHomePanel().getLevelSelector().getSelectedItem().toString().toUpperCase());

                Generator puzzle = new Generator();
                puzzle.generateGrid(level);
                model.setPuzzle(puzzle.getGrid());

                view.getGamePanel().setViewCellList(model.getPuzzle().getCellList());
                view.getGamePanel().getLevelTitle().setText(String.valueOf(level));
                update();

                // Switch to Game Panel
                view.getCardLayoutManager().show(view.getContent(), "game");

                // Set up Game Timer & Start
                long start = Calendar.getInstance().getTimeInMillis() / 1000;
                model.setTimer(new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long secondsSinceInit = ((Calendar.getInstance().getTimeInMillis() / 1000) - start);
                        view.getGamePanel().getTimeLabel().setText(String.format("%02d:%02d", secondsSinceInit / 60 % 60, secondsSinceInit % 60));
                    }
                }));
                model.getTimer().setInitialDelay(0);
                model.getTimer().start();
            }
        });
        this.view.getHomePanel().getViewRulesBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rulesCaller = "home"; // -> Rules was called from the 'home' panel, so return to it when done
                view.getCardLayoutManager().show(view.getContent(), "rules");
            }
        });
        this.view.getHomePanel().getSignoutBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Đăng xuất", "Hủy!"};
                int result = JOptionPane.showOptionDialog(getParent(), "Bạn có muốn đăng xuất không?", "Sẵn sàng thoát?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (result == 0) {
                    view.getCardLayoutManager().show(view.getContent(), "welcome");
                    model.setPlayer(null);
                    model.setPuzzle(null);
                    model.setHintsUsed(0);
                    model.setTimer(null);
                    view.getGamePanel().getHintBtn().setEnabled(true);
                    view.getHomePanel().getLevelSelector().setSelectedIndex(0);
                }
            }
        });
        
    this.view.getGamePanel().getUndoBtn().addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!undoStack.isEmpty()) {
            Cell previousState = undoStack.pop();
            Cell cell = model.getPuzzle().getCellAt(previousState.getPosition());
            cell.setUserValue(previousState.getUserValue());
            cell.setText(previousState.getUserValue() == 0 ? "" : String.valueOf(previousState.getUserValue()));
            update();
        } else {
            JOptionPane.showMessageDialog(null, "Không có hành động nào để hoàn tác!", "Undo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
});


        this.view.getGamePanel().getHintBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getHintsUsed() < model.getPuzzle().getDifficulty().getMaxHints()) {
                    model.getPuzzle().hint(false);
                    model.setHintsUsed(model.getHintsUsed() + 1);
                    update();
                    System.out.println("Số gợi ý đã dùng: " + model.getStringHintsUsed());
                    if (model.getHintsUsed() == model.getPuzzle().getDifficulty().getMaxHints()) {
                        view.getGamePanel().getHintBtn().setEnabled(false);
                        JOptionPane.showOptionDialog(getParent(), "Đừng làm game dễ như vậy!\nĐây là gợi ý cuối cùng.", "Hết lượt gợi ý", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    }
                    checkGridCompletion();
                }
            }
        });
        this.view.getGamePanel().getViewRulesBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rulesCaller = "game"; 
                view.getCardLayoutManager().show(view.getContent(), "rules");
            }
        });
        this.view.getRulesPanel().getBackBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getCardLayoutManager().show(view.getContent(), rulesCaller);
            }
        });
        this.view.getGamePanel().getEndGameBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Đúng vậy", "Hủy"};
                int result = JOptionPane.showOptionDialog(getParent(), "Bạn muốn kết thúc game phải không?\n\nMàn này chỉ xuất hiện 1 lần duy nhất,\nvà không thể chơi lại được.", "Thoát?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (result == 0) {
                    view.getCardLayoutManager().show(view.getContent(), "home");
                    destroyGameInstance();
                }
            }
        });


this.cellKeyListener = new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent evt) {
        Cell cell = (Cell) evt.getSource();
        if (!String.valueOf(evt.getKeyChar()).matches("^[1-9]$") || cell.getText().length() == 1) {
            evt.consume();
        } else {
            Cell previousState = new Cell(cell.getPosition(), cell.isLocked(), cell.getUserValue());
            undoStack.push(previousState);

            if (!model.getPuzzle().meetsConstraints(cell, Integer.valueOf(String.valueOf(evt.getKeyChar()).trim()))) {
                cell.setText("");
                cell.setUserValue(0);
                evt.consume();
            } else {
                cell.setUserValue(Integer.valueOf(String.valueOf(evt.getKeyChar()).trim()));
            }
            checkGridCompletion();
        }
    }
};


this.cellMouseListener = new MouseAdapter() {
    private Color preActionColor;

    @Override
    public void mousePressed(MouseEvent evt) {
        Cell cell = (Cell) evt.getSource();
        if (evt.getButton() == MouseEvent.BUTTON3) {
            cell.setText("");
            cell.setUserValue(0);
        }
        cell.selectAll();
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        Cell cell = (Cell) evt.getSource();
        preActionColor = cell.getBackground();
        for (Cell aCell : view.getGamePanel().getViewCellList()) {
            if (cell.getPosition().getRow() == aCell.getPosition().getRow() ||
                cell.getPosition().getColumn() == aCell.getPosition().getColumn() ||
                cell.getPosition().getSubgrid() == aCell.getPosition().getSubgrid()) {
                aCell.setBackground(APP_GREEN.darker().darker());
            }
        }
        cell.setBackground(APP_GREEN);
    }

    @Override
    public void mouseExited(MouseEvent evt) {
        Cell cell = (Cell) evt.getSource();
        for (Cell aCell : view.getGamePanel().getViewCellList()) {
            if (aCell.isLocked()) {
                aCell.setBackground(BKGD_DARK_GRAY);
            } else {
                aCell.setBackground(BKGD_LIGHT_GRAY);
            }
        }
        cell.setBackground(preActionColor);
    }
};

    }

    public static void main(String[] args) {
        JFrame frame = new SudokuGameApp("Sudoku Game");
 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void signInEvt() {
    // Retrieve Details
    String email = this.view.getWelcomePanel().getSignInPanel().getEmailText().getText().trim();
    String password = new String(this.view.getWelcomePanel().getSignInPanel().getPasswordText().getPassword()).trim();
    if (!email.equals("") && !password.equals("")) {
        Player player = new Player(email, password); 
        if (player.checkLogin()) {
            model.setPlayer(player);
            view.getWelcomePanel().getSignInPanel().clear();
            refreshHomePanel();
            view.getCardLayoutManager().show(view.getContent(), "home");
        } else {
            Object[] options = {"Thử lại"};
            JOptionPane.showOptionDialog(this, "Thông tin không hợp lệ. Vui lòng nhập lại hoặc tạo tài khoản mới.", "Thông tin không hợp lệ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        }
    } else {
        Object[] options = {"Được rồi"};
        JOptionPane.showOptionDialog(this, "Nhập đủ thông tin để đăng nhập", "Ô trống", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
    }
}



    private void signUpEvt() {
    String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    String fullname = this.view.getWelcomePanel().getSignUpPanel().getFullnameText().getText().trim();
    String email = this.view.getWelcomePanel().getSignUpPanel().getEmailText().getText().trim();
    String password = new String(this.view.getWelcomePanel().getSignUpPanel().getPasswordText().getPassword()).trim();
    if (!fullname.equals("") && !email.equals("") && !password.equals("")) {
        if (email.matches(emailRegex)) {
            Player player = new Player(fullname, email, password); 
            if (player.registerUser()) {
                view.getWelcomePanel().getCardLayoutManager().next(view.getWelcomePanel().getSlider());
                view.getWelcomePanel().getSignUpPanel().clear();
                Object[] options = {"OK"};
                JOptionPane.showOptionDialog(this, "Đăng ký thành công!\nBạn có thể đăng nhập.", "Đăng ký thành công", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
            } else {
                Object[] options = {"Hãy thử lại"};
                JOptionPane.showOptionDialog(this, "Đăng ký không thành công!\nTài khoản đã tồn tại.", "Đăng ký thông thành công", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, null);
            }
        } else {
            Object[] options = {"Được thôi"};
            JOptionPane.showOptionDialog(this, "Email phải hợp lệ.", "Email không hợp lệ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        }
    } else {
        // Empty Fields
        Object[] options = {"Được rồi"};
        JOptionPane.showOptionDialog(this, "Nhập đủ thông tin để đăng ký.", "Ô trống", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
    }
}



private void refreshHomePanel() {
    view.getHomePanel().getNameLabel().setText(SudokuGame.getCurrentPlayer().getUsername().toUpperCase());


    view.getHomePanel().getTableModel().setRowCount(0);

    updateListUsers(model.getListUsers());
}


private void updateListUsers(ArrayList<Player> listUsers) {
    DefaultTableModel tableModel = view.getHomePanel().getTableModel();
    for (Player user : listUsers) {
        tableModel.addRow(new Object[]{user.getScore(), user.getUsername(), user.getTime()});
    }
}


    private void update() {
    for (Cell cell : this.view.getGamePanel().getViewCellList()) {
        cell.setBackground(cell.isLocked() ? BKGD_DARK_GRAY : BKGD_LIGHT_GRAY);
        cell.setForeground(Color.WHITE);
        cell.setFont(new Font("Halvetica Neue", Font.PLAIN, 36));
        cell.setBorder(new LineBorder(Color.BLACK, 0));
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setCaretColor(new Color(32, 44, 53));
        cell.setDragEnabled(false);
        cell.setTransferHandler(null);

        CellPosition pos = cell.getPosition();
        if (pos.getColumn() == 2 || pos.getColumn() == 5) {
            cell.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(146, 208, 80)));
        } else if (pos.getRow() == 2 || pos.getRow() == 5) {
            cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(146, 208, 80)));
        }
        if ((pos.getColumn() == 2 && pos.getRow() == 2) || (pos.getColumn() == 5 && pos.getRow() == 5)
                || (pos.getColumn() == 2 && pos.getRow() == 5) || (pos.getColumn() == 5 && pos.getRow() == 2)) {
            cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(146, 208, 80)));
        }

        cell.removeKeyListener(cellKeyListener);
        cell.removeMouseListener(cellMouseListener);
        if (cell.isLocked()) {
            cell.setEditable(false);
            cell.setHighlighter(null);
        } else {
            cell.setBackground(BKGD_LIGHT_GRAY);
            cell.addMouseListener(cellMouseListener);
            cell.addKeyListener(cellKeyListener);
        }
        cell.setText(cell.isEmpty() ? "" : String.valueOf(cell.getUserValue()));

        this.view.getGamePanel().getGrid().add(cell);
    }
}


    public static Object[] playerToObjArray(Player player) {
        // Split Player into its respective sections
        Object[] initList = new Object[2];
        initList[0] = player.getScore();
        initList[1] = player.getUsername();
        initList[2] = player.getTime();
        return initList;
    }


    private void checkGridCompletion() {
        if (this.model.getPuzzle().isFilled()) {
            if (this.model.getPuzzle().isSolved()) {
                puzzleCompleted();
            }
        }
    }


    private void puzzleCompleted() {
        this.model.getTimer().stop();
        String gameTime = view.getGamePanel().getTimeLabel().getText();
        int totalSeconds = convertTimeToSeconds(gameTime);
        System.out.print(totalSeconds);
        for (Cell cell : this.model.getPuzzle().getCellList()) {
            cell.setLocked(true);
        }

        update();

        this.model.increaseScore(10, totalSeconds);
        Object[] options = {"Tuyệt vời!"};
        JOptionPane.showOptionDialog(this, "Chúc mừng bạn đã giải thành công.\nVới thời gian: " + gameTime + "\nSố gợi ý: " + this.model.getStringHintsUsed() +"\nĐiểm của bạn là: 10" +"\n\nTổng của bạn: " + this.model.getCurrentPlayer().getScore() + " điểm.", "Xin chúc mừng!", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);

        refreshHomePanel();
        view.getCardLayoutManager().show(view.getContent(), "home");

        destroyGameInstance();
    }
    private int convertTimeToSeconds(String gameTime) {
    String[] parts = gameTime.split(":");
    int minutes = Integer.parseInt(parts[0]);
    int seconds = Integer.parseInt(parts[1]);
    return minutes * 60 + seconds;
}

    private void destroyGameInstance() {
        // Destroy Game
        this.model.setPuzzle(null);
        this.model.setHintsUsed(0);
        this.model.getTimer().stop();
        this.model.setTimer(null);
        view.getGamePanel().getHintBtn().setEnabled(true);
        for (Cell cell : this.view.getGamePanel().getViewCellList()) {
            this.view.getGamePanel().getGrid().remove(cell);
        }
    }
}
