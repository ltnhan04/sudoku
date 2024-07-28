package com.mycompany.gui.panels;

import com.mycompany.gui.AppJButton;
import static com.mycompany.gui.SudokuGame.APP_GREEN;
import static com.mycompany.gui.SudokuGame.BKGD_DARK_GRAY;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class RulesPanel extends JPanel {

    private final String rules;
    private final AppJButton backBtn;


    public RulesPanel() {

        this.rules = "<html>Luật chơi rất đơn giản, có 81 ô vuông trên một lưới.<br>"
                + "Một lưới được chia thành 9 lưới con (khối), mỗi lưới chứa 9 ô.<br><br>"
                + "Ban đầu, một ô có thể chứa một chữ số hoặc để trống.<br>"
                + "Bằng cách sử dụng những \\\"gợi ý\\\" này, bạn có thể điền chính xác vào từng ô "
                + "trên lưới có chữ số riêng (1-9).<br><br>"
                + "Lưu ý?<br>"
                + " > Mỗi chữ số chỉ được xuất hiện một lần trong mỗi hàng.<br>"
                + " > Mỗi chữ số chỉ được xuất hiện một lần trong mỗi cột.<br>"
                + " > Mỗi chữ số chỉ được xuất hiện một lần trong mỗi lưới con.<br><br>"
                + "</html>";

        this.setLayout(new GridLayout(1, 0));
        this.setBackground(BKGD_DARK_GRAY);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        content.setBackground(BKGD_DARK_GRAY);

            JLabel titleLabel = new JLabel("LUẬT CỦA GAME");
            titleLabel.setFont(new Font("Avenir", Font.PLAIN, 24));
            titleLabel.setForeground(Color.white);
            titleLabel.setHorizontalAlignment(JLabel.CENTER);
            content.add(titleLabel, BorderLayout.NORTH);

            JLabel text = new JLabel(this.rules);
            text.setFont(new Font("Avenir", Font.PLAIN, 14));
            text.setForeground(Color.white);
            text.setBackground(BKGD_DARK_GRAY);
            text.setBorder(new EmptyBorder(10, 10, 10, 10));
            content.add(text, BorderLayout.CENTER);

            backBtn = new AppJButton("QUAY LẠI", 24, APP_GREEN, BKGD_DARK_GRAY);
            content.add(backBtn, BorderLayout.SOUTH);

        this.add(content);

    }

    public AppJButton getBackBtn() {
        return backBtn;
    }
}
