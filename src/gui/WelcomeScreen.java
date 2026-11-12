package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * WelcomeScreen presents a modern, high-contrast, visually engaging landing interface
 * for the Digital Whiteboard application.
 * - 100% clean typography without broken emoji glyph boxes
 * - Custom vector-rendered icon badges for all feature cards
 * - Glassmorphic card styling with glowing accent borders
 * - Radiant ambient lighting and glowing gradient action buttons
 */
public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        super("Digital Whiteboard – Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 600);
        setResizable(false);
        setLocationRelativeTo(null);

        // Main Container with rich cosmic gradient background & glowing atmospheric orbs
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Deep space gradient: Dark Navy (#080C15) to Slate (#1A2338)
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(8, 12, 21),
                        getWidth(), getHeight(), new Color(26, 35, 56)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Glowing atmospheric light orbs
                // Indigo/Purple orb at top right
                g2d.setColor(new Color(139, 92, 246, 50));
                g2d.fillOval(getWidth() - 280, -70, 340, 340);

                // Electric Cyan orb at bottom left
                g2d.setColor(new Color(6, 182, 212, 45));
                g2d.fillOval(-80, getHeight() - 250, 320, 320);

                // Rose Pink orb at center bottom
                g2d.setColor(new Color(244, 63, 94, 30));
                g2d.fillOval(getWidth() - 180, getHeight() - 170, 240, 240);

                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(34, 46, 32, 46));

        // Header Section (Pill badge, Title, Subtitle)
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Glowing Pill Badge
        JPanel badgePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(6, 182, 212, 35));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(6, 182, 212, 160));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        badgePanel.setOpaque(false);
        badgePanel.setBorder(new EmptyBorder(4, 16, 4, 16));
        badgePanel.setMaximumSize(new Dimension(360, 28));
        badgePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBadge = new JLabel("COMPUTER SCIENCE  |  SEMESTER 3 PROJECT");
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblBadge.setForeground(new Color(103, 232, 249)); // Bright cyan
        badgePanel.add(lblBadge);

        JLabel lblTitle = new JLabel("DIGITAL WHITEBOARD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Interactive Desktop Drawing, Sketching & Diagramming Suite");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(148, 163, 184)); // Muted slate
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(badgePanel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(lblSubtitle);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Section: 6 Vibrant Feature Cards with Custom Vector Icon Painting
        JPanel featuresPanel = new JPanel(new GridLayout(2, 3, 14, 14));
        featuresPanel.setOpaque(false);
        featuresPanel.setBorder(new EmptyBorder(24, 4, 24, 4));

        featuresPanel.add(createFeatureCard("Freehand Drawing", "Smooth brush & pencil with anti-aliased curves", "BRUSH", UITheme.ACCENT_CYAN, 1));
        featuresPanel.add(createFeatureCard("Geometric Shapes", "Lines, rects, circles with live rubberband preview", "SHAPES", UITheme.ACCENT_AMBER, 2));
        featuresPanel.add(createFeatureCard("16-Color Palette", "Curated designer colors + RGB JColorChooser", "COLORS", UITheme.ACCENT_PINK, 3));
        featuresPanel.add(createFeatureCard("Variable Eraser", "Precision eraser tool + one-click canvas clear", "ERASER", UITheme.SUCCESS, 4));
        featuresPanel.add(createFeatureCard("State Undo / Redo", "Multi-level history with double-buffered snapshots", "HISTORY", UITheme.ACCENT_PURPLE, 5));
        featuresPanel.add(createFeatureCard("Export Artwork", "Save your drawings as high-resolution PNG & JPG", "EXPORT", UITheme.ACCENT, 6));

        mainPanel.add(featuresPanel, BorderLayout.CENTER);

        // Bottom Section: Clean, Bold Action Buttons without broken emojis
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        buttonPanel.setOpaque(false);

        // Vibrant Gradient "Start Drawing" Button
        JButton btnStart = UITheme.createGradientButton(
                "Start Drawing  ->",
                new Color(99, 102, 241),
                new Color(168, 85, 247),
                Color.WHITE
        );
        btnStart.setPreferredSize(new Dimension(200, 46));
        btnStart.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnStart.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> {
                WhiteboardFrame frame = new WhiteboardFrame();
                frame.setVisible(true);
            });
        });

        // About Project Button
        JButton btnAbout = UITheme.createStyledButton(
                "About Project",
                new Color(30, 41, 59),
                new Color(51, 65, 85),
                Color.WHITE
        );
        btnAbout.setPreferredSize(new Dimension(150, 46));
        btnAbout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAbout.addActionListener(e -> showAboutProjectDialog());

        // Exit Button
        JButton btnExit = UITheme.createStyledButton(
                "Exit",
                new Color(239, 68, 68),
                new Color(220, 38, 38),
                Color.WHITE
        );
        btnExit.setPreferredSize(new Dimension(120, 46));
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnStart);
        buttonPanel.add(btnAbout);
        buttonPanel.add(btnExit);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * Creates an attractive feature card with an integrated vector-drawn icon badge.
     */
    private JPanel createFeatureCard(String title, String desc, String tag, Color accentColor, int iconType) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Card body: Deep Dark Slate (#131B2E)
                g2.setColor(new Color(19, 27, 46, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                // Glowing Top Accent Line
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, getWidth(), 3, 2, 2);

                // Card Border with subtle accent glow
                g2.setColor(new Color(51, 65, 85, 180));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                // Draw Vector Icon Badge (28x28 circle at top left: x=14, y=14)
                int ix = 14;
                int iy = 14;
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40));
                g2.fillOval(ix, iy, 28, 28);
                g2.setColor(accentColor);
                g2.drawOval(ix, iy, 28, 28);

                // Render vector icon geometry based on type
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                renderVectorIcon(g2, iconType, ix, iy, accentColor);

                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 50, 12, 14)); // Left padding leaves space for 28px icon badge

        // Text Container
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        // Title + Tag row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        titleRow.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(Color.WHITE);
        titleRow.add(lblTitle);

        JLabel lblTag = new JLabel("[" + tag + "]");
        lblTag.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTag.setForeground(accentColor);
        titleRow.add(lblTag);

        JLabel lblDesc = new JLabel("<html><body style='width: 140px; color: #94A3B8; font-size: 10px; font-family: Segoe UI;'>" + desc + "</body></html>");

        textPanel.add(titleRow);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(lblDesc);

        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Renders clean, sharp vector shapes for each feature card icon badge without emojis.
     */
    private void renderVectorIcon(Graphics2D g2, int type, int x, int y, Color c) {
        g2.setColor(c);
        switch (type) {
            case 1: // Brush: Diagonal stroke with round tip
                g2.drawLine(x + 8, y + 20, x + 18, y + 8);
                g2.fillOval(x + 16, y + 7, 5, 5);
                break;
            case 2: // Shapes: Overlapping square and circle
                g2.drawRect(x + 7, y + 7, 9, 9);
                g2.drawOval(x + 12, y + 12, 9, 9);
                break;
            case 3: // Colors: 3 colored dots
                g2.fillOval(x + 8, y + 14, 5, 5);
                g2.fillOval(x + 15, y + 9, 5, 5);
                g2.fillOval(x + 16, y + 16, 5, 5);
                break;
            case 4: // Eraser: Slanted eraser block
                g2.drawRoundRect(x + 8, y + 9, 12, 10, 4, 4);
                g2.drawLine(x + 14, y + 9, x + 14, y + 19);
                break;
            case 5: // History: Curved undo arrow
                g2.drawArc(x + 8, y + 8, 12, 12, 45, 270);
                g2.drawLine(x + 8, y + 12, x + 12, y + 12);
                g2.drawLine(x + 8, y + 12, x + 8, y + 16);
                break;
            case 6: // Export: File with downward arrow
                g2.drawRect(x + 8, y + 7, 12, 14);
                g2.drawLine(x + 14, y + 10, x + 14, y + 17);
                g2.drawLine(x + 11, y + 14, x + 14, y + 17);
                g2.drawLine(x + 17, y + 14, x + 14, y + 17);
                break;
        }
    }

    private void showAboutProjectDialog() {
        String msg = """
                ============================================
                     DIGITAL WHITEBOARD APPLICATION
                ============================================
                
                Course: Object-Oriented Programming with Java
                Academic Year: Semester 3
                
                Key Highlights:
                * Freehand Brush & Pencil with smooth anti-aliased Graphics2D
                * Geometric Suite (Lines, Rectangles, Ovals & Filled shapes)
                * 16-Color Quick Palette + Full RGB JColorChooser picker
                * 4 Whiteboard Themes (Whiteboard, Blackboard, Cream, Blueprint)
                * Multi-Level Undo & Redo with double-buffered memory snapshots
                * PNG and JPEG high-resolution artwork export
                * Strategy Pattern, Event-Driven decoupled mouse architecture
                
                Technologies: Java Swing, Java AWT, ImageIO, Event Handling.
                """;

        JOptionPane.showMessageDialog(this, msg, "About Digital Whiteboard", JOptionPane.INFORMATION_MESSAGE);
    }
}
