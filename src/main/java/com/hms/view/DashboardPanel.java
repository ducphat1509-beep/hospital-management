package com.hms.view;

import com.hms.view.ui.HmsTheme;
import com.hms.view.ui.RoundedPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private final Runnable onCta;
    private final com.hms.service.AppointmentService appointmentService;

    public DashboardPanel(com.hms.service.AppointmentService appointmentService, Runnable onCta) {
        this.appointmentService = appointmentService;
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

        JPanel left = new JPanel(new GridLayout(3, 2, 6, 6));
        left.setOpaque(false);

        JLabel title = new JLabel("Cần tìm bác sĩ? Kết nối trực tuyến với chúng tôi!");
        title.setFont(HmsTheme.fontBold(18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("GET YOUR FIRST MEDICAL SERVICE AT YOUR HOME.");
        sub.setFont(HmsTheme.fontRegular(12));
        sub.setForeground(new Color(0xD6E6F2));
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        JButton cta = new JButton("Đặt lịch tái khám");
        HmsTheme.styleSecondaryButton(cta);
        cta.setHorizontalAlignment(SwingConstants.CENTER);
        cta.addActionListener(e -> {
            if (onCta != null) onCta.run();
        });

        JLabel timeLabel1 = new JLabel();
        timeLabel1.setFont(HmsTheme.fontBold(18));
        timeLabel1.setForeground(new Color(0xD6E6F2));
        timeLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel timeLabel2 = new JLabel();
        timeLabel2.setFont(HmsTheme.fontBold(18));
        timeLabel2.setForeground(new Color(0xD6E6F2));
        timeLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        // update time mỗi giây
        Timer timer = new Timer(1000, e -> {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();

            java.time.format.DateTimeFormatter formatter1 =
                    java.time.format.DateTimeFormatter.ofPattern("'         Date:' dd/MM/yyyy");
            java.time.format.DateTimeFormatter formatter2 =
                    java.time.format.DateTimeFormatter.ofPattern("'         Time:' HH:mm:ss");

            timeLabel1.setText(now.format(formatter1));
            timeLabel1.setFont(new Font("Bauhaus 93", Font.BOLD, 24));
            timeLabel2.setText(now.format(formatter2));
            timeLabel2.setFont(new Font("Bauhaus 93", Font.BOLD, 24));
        });
        timer.start();

        left.add(title);
        left.add(timeLabel1);
        left.add(sub);
        left.add(timeLabel2);
        left.add(cta);

        hero.add(left, BorderLayout.WEST);
        return hero;
    }

    private JComponent buildBody() {
        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);

        java.util.List<com.hms.model.entity.Appointment> allAppointments = appointmentService.getAppointmentsByDoctor(null);
        int total = 0;
        int confirmed = 0;
        int canceled = 0;
        com.hms.model.entity.Appointment nextAppointment = null;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        if (allAppointments != null) {
            total = allAppointments.size();
            for (com.hms.model.entity.Appointment a : allAppointments) {
                if (a.getStatus() == com.hms.model.entity.Appointment.AppointmentStatus.CONFIRMED) {
                    confirmed++;
                    if (a.getAppointmentTime() != null && a.getAppointmentTime().isAfter(now)) {
                        if (nextAppointment == null || a.getAppointmentTime().isBefore(nextAppointment.getAppointmentTime())) {
                            nextAppointment = a;
                        }
                    }
                } else if (a.getStatus() == com.hms.model.entity.Appointment.AppointmentStatus.CANCELLED) {
                    canceled++;
                }
            }
        }

        String nextIdStr = nextAppointment != null ? "ID: " + nextAppointment.getId() : "N/A";

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 12));
        stats.setOpaque(false);
        stats.add(statCard(String.valueOf(total), "Tổng lượt đặt"));
        stats.add(statCard(String.valueOf(confirmed), "Đã xác nhận"));
        stats.add(statCard(String.valueOf(canceled), "Đã hủy"));
        stats.add(statCard(nextIdStr, "Lịch hẹn gần nhất"));

        body.add(stats, BorderLayout.NORTH);

        RoundedPanel tableCard = new RoundedPanel(18, Color.WHITE);
        tableCard.setLayout(new BorderLayout(12, 12));
        tableCard.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel heading = new JLabel("Lịch hẹn sắp tới");
        heading.setFont(HmsTheme.fontBold(14));
        heading.setForeground(HmsTheme.TEXT);
        tableCard.add(heading, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Bác sĩ", "Chuyên khoa", "Bệnh nhân", "Thời gian khám"}, 0
        );
        JTable table = new JTable(model);
        HmsTheme.styleTable(table);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (allAppointments != null) {
            allAppointments.stream()
                .filter(a -> a.getStatus() == com.hms.model.entity.Appointment.AppointmentStatus.CONFIRMED && a.getAppointmentTime() != null && a.getAppointmentTime().isAfter(now))
                .sorted(java.util.Comparator.comparing(com.hms.model.entity.Appointment::getAppointmentTime))
                .forEach(a -> {
                    String doctorName = a.getDoctor() != null ? a.getDoctor().getFullName() : "";
                    String department = (a.getDoctor() != null && a.getDoctor().getDepartment() != null) ? a.getDoctor().getDepartment().getName() : "";
                    String patientName = a.getPatient() != null ? a.getPatient().getFullName() : "";
                    String time = a.getAppointmentTime().format(formatter);
                    model.addRow(new Object[]{doctorName, department, patientName, time});
                });
        }

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

