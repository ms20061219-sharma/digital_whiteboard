package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * UITheme defines modern aesthetic constants, vibrant color palettes,
 * and custom component factory methods for the Digital Whiteboard application.
 */
public final class UITheme {

    // Prevent instantiation
    private UITheme() {}

    // Core Theme Colors - Slate Dark & Deep Navy
    public static final Color BG_TOOLBAR       = new Color(30, 41, 59);    // Slate Dark (#1E293B)
    public static final Color BG_PANEL         = new Color(15, 23, 42);    // Navy Dark (#0F172A)
    public static final Color BG_CANVAS_AREA   = new Color(226, 232, 240); // Soft Slate Light (#E2E8F0)
    public static final Color CANVAS_DEFAULT   = Color.WHITE;

    // Canvas Background Presets
    public static final Color CANVAS_BLACKBOARD= new Color(24, 32, 47);    // Dark Blackboard mode (#18202F)
    public static final Color CANVAS_CREAM     = new Color(254, 252, 232); // Vintage Warm Cream (#FEFCE8)
    public static final Color CANVAS_BLUEPRINT = new Color(15, 33, 64);    // Technical Blueprint (#0F2140)
    public static final Color CANVAS_MINT      = new Color(240, 253, 244); // Calming Mint Sage (#F0FDF4)

    // Vibrant Primary & Accent Palette
    public static final Color PRIMARY          = new Color(99, 102, 241);  // Indigo (#6366F1)
    public static final Color PRIMARY_HOVER    = new Color(79, 70, 229);   // Indigo Darker (#4F46E5)
    public static final Color ACCENT           = new Color(59, 130, 246);  // Sky Blue (#3B82F6)
    public static final Color ACCENT_CYAN      = new Color(6, 182, 212);   // Electric Cyan (#06B6D4)
    public static final Color ACCENT_PURPLE    = new Color(168, 85, 247);  // Electric Violet (#A855F7)
    public static final Color ACCENT_PINK      = new Color(236, 72, 153);  // Neon Pink (#EC4899)
    public static final Color ACCENT_AMBER     = new Color(245, 158, 11);  // Vivid Amber (#F59E0B)
    public static final Color SUCCESS          = new Color(16, 185, 129);  // Emerald (#10B981)
    public static final Color DANGER           = new Color(239, 68, 68);   // Red (#EF4444)
    public static final Color DANGER_HOVER     = new Color(220, 38, 38);

    // Text & Border Colors
    public static final Color TEXT_LIGHT       = new Color(248, 250, 252); // White/Off-white (#F8FAFC)
    public static final Color TEXT_MUTED       = new Color(148, 163, 184); // Muted slate gray (#94A3B8)
    public static final Color BORDER_DARK      = new Color(51, 65, 85);    // Slate border (#334155)
    public static final Color ACTIVE_TOOL_BG   = new Color(79, 70, 229);

    // Fonts
    public static final Font FONT_TITLE        = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE     = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_HEADER       = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR      = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BOLD         = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONT_SMALL        = new Font("Segoe UI", Font.PLAIN, 11);

    /**
     * Creates an ultra-attractive modern gradient push button with smooth hover highlights.
     */
    public static JButton createGradientButton(String text, Color startColor, Color endColor, Color textColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color c1 = startColor;
                Color c2 = endColor;

                if (getModel().isPressed()) {
                    c1 = c1.darker();
                    c2 = c2.darker();
                } else if (getModel().isRollover()) {
                    c1 = c1.brighter();
                    c2 = c2.brighter();
                }

                // Render dynamic diagonal gradient
                GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Subtle inner glow border
                g2.setColor(new Color(255, 255, 255, 70));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);

                // Paint centered text
                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(textColor);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        btn.setFont(FONT_BOLD);
        btn.setForeground(textColor);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    /**
     * Creates a styled modern push button with custom paintComponent so OS Look and Feel
     * CANNOT override colors with native white buttons.
     */
    public static JButton createStyledButton(String text, Color baseColor, Color hoverColor, Color textColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color currentBg = getModel().isPressed()
                        ? baseColor.darker()
                        : (getModel().isRollover() ? hoverColor : baseColor);

                g2.setColor(currentBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(BORDER_DARK);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(textColor);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        btn.setFont(FONT_BOLD);
        btn.setForeground(textColor);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        return btn;
    }

    /**
     * Creates a toggle tool button with custom painting that guarantees high contrast,
     * beautiful dark-slate background, and glowing cyan/indigo active state.
     */
    public static JButton createToolButton(String text, String tooltip) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isActive = Boolean.TRUE.equals(getClientProperty("active"));

                Color bg;
                Color border;
                Color textColor;

                if (isActive) {
                    bg = PRIMARY; // Vibrant Indigo #6366F1
                    border = ACCENT_CYAN; // Electric Cyan border
                    textColor = Color.WHITE;
                } else if (getModel().isRollover()) {
                    bg = new Color(51, 65, 85); // Slate Lighter #334155
                    border = new Color(100, 116, 139);
                    textColor = Color.WHITE;
                } else {
                    bg = new Color(15, 23, 42); // Deep Navy Slate #0F172A
                    border = BORDER_DARK;
                    textColor = new Color(241, 245, 249); // Crisp White/Slate
                }

                // Fill rounded button
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Draw border (2px for active)
                g2.setColor(border);
                g2.setStroke(new BasicStroke(isActive ? 2f : 1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                // Draw text
                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
                int textX = (getWidth() - stringBounds.width) / 2;
                int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();

                g2.setColor(textColor);
                g2.setFont(getFont());
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        btn.setPreferredSize(new Dimension(86, 30));
        return btn;
    }
}
