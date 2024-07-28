package com.mycompany.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import javax.swing.JButton;


public class AppJButton extends JButton {


    public AppJButton(String buttonText, int fontSize, Color background, Color foreground) {

        this.setText(buttonText);
        this.setFont(new Font("Halvetica Neue", Font.PLAIN, fontSize));
        this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(true);
        this.setBackground(background);
        this.setForeground(foreground);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.addMouseListener(new MouseAdapter() {
          @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(161, 204, 165));
                setForeground(new Color(7, 79,87 ));

            }
           
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(background);
                setForeground(new Color(255,255,255));

            }

        });
    }
}
