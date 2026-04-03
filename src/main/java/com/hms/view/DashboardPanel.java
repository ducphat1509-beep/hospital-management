package com.hms.view;

import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private final Runnable onCta;

    public DashboardPanel() {
        this(null);
    }

    public DashboardPanel(Runnable onCta) {
        this.onCta = onCta;
        setOpaque(false);
        setLayout(new BorderLayout(16, 16));

        add(buildHero(), BorderLayout.NORTH);
        add(buildBody(), BorderLayout.CENTER);
    }

    private JComponent buildHero() {
        RoundedPanel hero = new RoundedPanel(18, new Color(0x214A60));
        hero.setLayout(new BorderLayout(16, 16));
        hero.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel left = new JPanel(new GridLayout(0, 1, 0, 6));
        left.setOpaque(false);

        JLabel title = new JLabel("Cần tìm bác sĩ? Kết nối trực tuyến với chúng tôi!");
        title.setFont(HmsTheme.fontBold(18));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Get your first medical service at your home.");
        sub.setFont(HmsTheme.fontRegular(12));
        sub.setForeground(new Color(0xD6E6F2));

        JButton cta = new JButton("Đặt lịch khám");
        HmsTheme.styleSecondaryButton(cta);
        cta.addActionListener(e -> {
            if (onCta != null) onCta.run();
        });

        left.add(title);
        left.add(sub);
        left.add(new JLabel());
        left.add(cta);

        hero.add(left, BorderLayout.WEST);
        return hero;
    }

    private JComponent buildBody() {
        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 12));
        stats.setOpaque(false);
//        stats.add(statCard("34", "Tổng lượt đặt"));
//        stats.add(statCard("21", "Đặt thành công"));
//        stats.add(statCard("4", "Đặt bị hủy"));
//        stats.add(statCard("120€", "Số tiền đã trả"));

        body.add(stats, BorderLayout.NORTH);

        RoundedPanel tableCard = new RoundedPanel(18, Color.WHITE);
        tableCard.setLayout(new BorderLayout(12, 12));
        tableCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel heading = new JLabel("Lịch hẹn sắp tới");
        heading.setFont(HmsTheme.fontBold(14));
        heading.setForeground(HmsTheme.TEXT);
        tableCard.add(heading, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Bác sĩ", "Chuyên khoa", "Date & Time", "Trạng thái"}, 0
        );
        JTable table = new JTable(model);
        HmsTheme.styleTable(table);
        model.addRow(new Object[]{"Dr. Rita Ora", "Dermatologist", "Apr 4, 2023 14:00", "PENDING"});
        model.addRow(new Object[]{"Dr. Adam Sol", "Psychologist", "Apr 11, 2023 16:30", "DONE"});

        tableCard.add(new JScrollPane(table), BorderLayout.CENTER);

        body.add(tableCard, BorderLayout.CENTER);
        return body;
    }

    private JComponent statCard(String value, String label) {
        RoundedPanel card = new RoundedPanel(18, Color.WHITE);
        card.setLayout(new GridLayout(0, 1, 0, 2));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel v = new JLabel(value);
        v.setFont(HmsTheme.fontBold(18));
        v.setForeground(HmsTheme.TEXT);
        JLabel l = new JLabel(label);
        l.setFont(HmsTheme.fontRegular(12));
        l.setForeground(HmsTheme.TEXT_MUTED);

        card.add(v);
        card.add(l);
        return card;
    }
}

