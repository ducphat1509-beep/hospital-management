package com.hms.view.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public final class HmsTheme {

    private HmsTheme() {}

    // Colors (soft modern dashboard)
    public static final Color BG = new Color(0xEAF3F4);
    public static final Color CARD = new Color(0xF7FBFC);
    public static final Color SIDEBAR_BG = new Color(0xDDEDEE);
    public static final Color TOPBAR_BG = new Color(0xDDEDEE);

    public static final Color TEXT = new Color(0x102A2A);
    public static final Color TEXT_MUTED = new Color(0x5C6D6D);
    public static final Color BORDER = new Color(0xC9D9DA);

    public static final Color PRIMARY = new Color(0x1F6F78);
    public static final Color PRIMARY_SOFT = new Color(0xD6ECEE);

    public static Font fontRegular(int size) {
        return new Font("Segoe UI", Font.PLAIN, size);
    }

    public static Font fontBold(int size) {
        return new Font("Segoe UI", Font.BOLD, size);
    }

    public static JButton navButton(String text, String navKey) {
        JButton b = new JButton(text);
        b.putClientProperty("navKey", navKey);
        b.setFont(fontRegular(13));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBackground(new Color(0xF3FAFA));
        b.setForeground(TEXT);
        b.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(10, 12, 10, 12)));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        return b;
    }

    public static void stylePrimaryButton(JButton b) {
        b.setFont(fontBold(12));
        b.setFocusPainted(false);
        b.setBackground(PRIMARY);
        b.setForeground(Color.WHITE);
        b.setBorder(new CompoundBorder(new LineBorder(PRIMARY.darker(), 1, true), new EmptyBorder(8, 12, 8, 12)));
    }

    public static void styleSecondaryButton(JButton b) {
        b.setFont(fontRegular(12));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setForeground(TEXT);
        b.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 12, 8, 12)));
    }

    public static void stylePillButton(JButton b) {
        b.setFont(fontRegular(12));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setForeground(TEXT);
        b.setBorder(new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 14, 8, 14)));
    }

    public static Border roundedLineBorder(int radius) {
        return new CompoundBorder(new LineBorder(BORDER, 1, true), new EmptyBorder(8, 12, 8, 12));
    }

    public static void styleTable(JTable table) {
        table.setFont(fontRegular(12));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(fontBold(12));
        table.getTableHeader().setBackground(new Color(0xEEF6F6));
        table.getTableHeader().setForeground(TEXT);
    }
}

