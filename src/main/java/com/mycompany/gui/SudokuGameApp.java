package com.mycompany.gui;
import java.util.Comparator;

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
import java.util.Collections;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

/**
 * This is the Sudoku Game (CONTROLLER).
 *
 * 
 * @version 1.0
 */
public class SudokuGameApp extends JFrame {

    private final SudokuGame model;
    private final SudokuGamePanel view;
    private ArrayList<Player> listUsers;
    private String rulesCaller;
    private final KeyListener cellKeyListener;
    private final MouseListener cellMouseListener;
    /**
     * Constructs the Sudoku Game Frame
     *
     * @param name title of the application window
     */
    public SudokuGameApp(String name) {
        super(name);
        this.model = new SudokuGame();
        this.view = new SudokuGamePanel();
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

        // Actions Listeners on Game & Rules Panel
        this.view.getGamePanel().getHintBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // User wants a hint, check if game has unused hints
                if (model.getHintsUsed() < model.getPuzzle().getDifficulty().getMaxHints()) {
                    model.getPuzzle().hint(false);
                    model.setHintsUsed(model.getHintsUsed() + 1);
                    update();
                    System.err.println("Số gợi ý đã dùng: " + model.getStringHintsUsed());
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
                // Show Rules Panel
                rulesCaller = "game"; // -> Rules was called from the 'game' panel, so return to it when done
                view.getCardLayoutManager().show(view.getContent(), "rules");
            }
        });
        this.view.getRulesPanel().getBackBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getCardLayoutManager().show(view.getContent(), rulesCaller); // -> Return to caller panel
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

        // Cell Listener Adapters
        this.cellKeyListener = new KeyAdapter() {
            /**
             * Validates the user input for the cell
             *
             * @param evt the key event trigger
             */
            @Override
            public void keyTyped(KeyEvent evt) {
                Cell cell = (Cell) evt.getSource();
                // Disregard entry if not 1-9 or text already exists
                if (!String.valueOf(evt.getKeyChar()).matches("^[1-9]$") || cell.getText().length() == 1) {
                    System.out.println("Input: " + evt.getKeyChar() + " khong hop le.");
                    evt.consume();
                } else {
                    // Check if input meets contraints
                    if (!model.getPuzzle().meetsConstraints(cell, Integer.valueOf(String.valueOf(evt.getKeyChar()).trim()))) {
                        System.err.println("Gia tri " + evt.getKeyChar() + " tai " + cell.getPosition() + " khong hop le");
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
            // Cell Hover Attribute
            private Color preActionColor;

            /**
             * Event Handler for mouse button press
             *
             * @param evt the event trigger
             */
            @Override
            public void mousePressed(MouseEvent evt) {
                Cell cell = (Cell) evt.getSource();

                // On Right-Click, clear cell
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    cell.setText("");
                    cell.setUserValue(0);
                }

                cell.selectAll();
            }

            /**
             * Highlights game constraints for the hovered cell
             *
             * @param evt the cell being hovered
             */
            @Override
            public void mouseEntered(MouseEvent evt) {
                Cell cell = (Cell) evt.getSource();
                preActionColor = cell.getBackground();

                // Highlight Valid Cells
                for (Cell aCell : view.getGamePanel().getViewCellList()) {
                    if (cell.getPosition().getRow() == aCell.getPosition().getRow()) {
                        aCell.setBackground(APP_GREEN.darker().darker());
                    }
                    if (cell.getPosition().getColumn() == aCell.getPosition().getColumn()) {
                        aCell.setBackground(APP_GREEN.darker().darker());
                    }
                    if (cell.getPosition().getSubgrid() == aCell.getPosition().getSubgrid()) {
                        aCell.setBackground(APP_GREEN.darker().darker());
                    }
                }

                cell.setBackground(APP_GREEN);
            }

            /**
             * Restores hover colors from hover event
             *
             * @param evt the hovered cell being exited from
             */
            @Override
            public void mouseExited(MouseEvent evt) {
                Cell cell = (Cell) evt.getSource();

                // Restore Color
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

    /**
     * Application entry point.
     *
     * @param args Optional startup arguments
     */
    public static void main(String[] args) {
        JFrame frame = new SudokuGameApp("Sudoku Game");
 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Signs the user into the application on correct credentials, else rejects
     * them.
     */
    private void signInEvt() {
    // Retrieve Details
    String email = this.view.getWelcomePanel().getSignInPanel().getEmailText().getText().trim();
    String password = new String(this.view.getWelcomePanel().getSignInPanel().getPasswordText().getPassword()).trim();
    if (!email.equals("") && !password.equals("")) {
        Player player = new Player(email, password); // Khởi tạo Player với email và password
        if (player.checkLogin()) {
            // Set Player
            model.setPlayer(player);
            // Clear Fields
            view.getWelcomePanel().getSignInPanel().clear();
            // Show Home Screen
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


    /**
     * Signs the user up and registers them in the database.
     */
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
            // Email doesn't meet requirement
            Object[] options = {"Được thôi"};
            JOptionPane.showOptionDialog(this, "Email phải hợp lệ.", "Email không hợp lệ", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        }
    } else {
        // Empty Fields
        Object[] options = {"Được rồi"};
        JOptionPane.showOptionDialog(this, "Nhập đủ thông tin để đăng ký.", "Ô trống", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
    }
}


    /**
     * Updates (refreshes) the Home panel.
     */
private void refreshHomePanel() {
    view.getHomePanel().getNameLabel().setText(SudokuGame.getCurrentPlayer().getUsername().toUpperCase());

    // Sắp xếp listUsers trước khi cập nhật bảng

    view.getHomePanel().getTableModel().setRowCount(0);

    updateListUsers(model.getListUsers());
}


private void updateListUsers(ArrayList<Player> listUsers) {
    DefaultTableModel tableModel = view.getHomePanel().getTableModel();
    for (Player user : listUsers) {
        tableModel.addRow(new Object[]{user.getScore(), user.getUsername(), user.getTime()});
    }
}

    /**
     * View update event handler.
     */
    private void update() {
        // Set for each cell
        for (Cell cell : this.view.getGamePanel().getViewCellList()) {
            cell.setBackground(BKGD_DARK_GRAY);
            cell.setForeground(Color.WHITE);
            cell.setFont(new Font("Halvetica Neue", Font.PLAIN, 36));
            cell.setBorder(new LineBorder(Color.BLACK, 0));
            cell.setHorizontalAlignment(JTextField.CENTER);
            cell.setCaretColor(new Color(32, 44, 53));
            cell.setDragEnabled(false);
            cell.setTransferHandler(null);

            // Add subgrid separators
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

            // Validate User's Cell Input + Mouse Listeners
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
            if (cell.isEmpty()) {
                cell.setText("");
            } else {
                cell.setText(String.valueOf(cell.getUserValue()));
            }

            // Adds cell to the view's grid
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

    /**
     * Checks the player's current grid's completion
     */
    private void checkGridCompletion() {
        if (this.model.getPuzzle().isFilled()) {
            if (this.model.getPuzzle().isSolved()) {
                puzzleCompleted();
            }
        }
    }

    /**
     * Events which fire at completion of Sudoku grid
     */
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
    /**
     * Clears game settings after game
     */
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
