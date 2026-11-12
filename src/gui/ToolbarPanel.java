package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import tools.ToolType;

/**
 * ToolbarPanel organizes the drawing controls in a clean, two-tier layout:
 * - Tier 1: Drawing Tool selection buttons & Undo / Redo / Clear actions
 * - Tier 2: Stroke slider & S/M/L presets, 16-color palette, and Board Themes
 * Custom-painted to guarantee 100% visibility, crisp contrast, and zero emoji tofu boxes.
 */
public class ToolbarPanel extends JPanel {

    private final DrawingCanvas canvas;
    private final Map<ToolType, JButton> toolButtons = new HashMap<>();

    private JSlider strokeSlider;
    private JLabel lblStrokeValue;
    private JPanel colorPreviewBox;

    public ToolbarPanel(DrawingCanvas canvas) {
        this(canvas, null);
    }

    public ToolbarPanel(DrawingCanvas canvas, Runnable onBackToHome) {
        this.canvas = canvas;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.BG_TOOLBAR);
        setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, UITheme.BORDER_DARK),
                new EmptyBorder(4, 8, 4, 8)
        ));

        // Tier 1: Home Navigation, Tools & Canvas Action Buttons
        JPanel tier1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        tier1.setOpaque(false);

        if (onBackToHome != null) {
            JButton btnBack = UITheme.createGradientButton(
                    "<- Home",
                    new Color(99, 102, 241),
                    new Color(139, 92, 246),
                    Color.WHITE
            );
            btnBack.setFont(UITheme.FONT_BOLD);
            btnBack.setPreferredSize(new Dimension(86, 28));
            btnBack.setToolTipText("Return to Welcome Home Screen");
            btnBack.addActionListener(e -> onBackToHome.run());
            tier1.add(btnBack);
            addSeparator(tier1);
        }

        initToolSection(tier1);
        addSeparator(tier1);
        initActionSection(tier1);
        add(tier1);

        // Tier 2: Stroke Controls, Color Palette, and Board Themes
        JPanel tier2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        tier2.setOpaque(false);
        initStrokeSection(tier2);
        addSeparator(tier2);
        initColorSection(tier2);
        addSeparator(tier2);
        initBoardThemeSection(tier2);
        add(tier2);

        // Highlight initial default tool
        setActiveToolHighlight(canvas.getCurrentTool());
    }

    private void addSeparator(JPanel container) {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(2, 28));
        sep.setForeground(UITheme.BORDER_DARK);
        sep.setBackground(UITheme.BORDER_DARK);
        container.add(sep);
    }

    private void initToolSection(JPanel container) {
        JLabel lbl = new JLabel("Tools:");
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.ACCENT_CYAN);
        container.add(lbl);

        createToolBtn(container, ToolType.BRUSH, "Brush", "Freehand smooth brush");
        createToolBtn(container, ToolType.PENCIL, "Pencil", "Fine line pencil");
        createToolBtn(container, ToolType.ERASER, "Eraser", "Erase artwork");
        createToolBtn(container, ToolType.LINE, "Line", "Draw straight lines");
        createToolBtn(container, ToolType.RECTANGLE, "Rect", "Draw outlined rectangle");
        createToolBtn(container, ToolType.FILLED_RECTANGLE, "Fill Rect", "Draw solid rectangle");
        createToolBtn(container, ToolType.OVAL, "Oval", "Draw outlined circle/oval");
        createToolBtn(container, ToolType.FILLED_OVAL, "Fill Oval", "Draw solid circle/oval");
        createToolBtn(container, ToolType.TEXT, "Text", "Click to insert text");
    }

    private void createToolBtn(JPanel container, ToolType type, String label, String tooltip) {
        JButton btn = UITheme.createToolButton(label, tooltip);
        btn.addActionListener(e -> {
            canvas.setCurrentTool(type);
            setActiveToolHighlight(type);
        });
        toolButtons.put(type, btn);
        container.add(btn);
    }

    private void setActiveToolHighlight(ToolType activeType) {
        for (Map.Entry<ToolType, JButton> entry : toolButtons.entrySet()) {
            JButton btn = entry.getValue();
            boolean isActive = (entry.getKey() == activeType);
            btn.putClientProperty("active", isActive);
            btn.repaint();
        }
    }

    private void initActionSection(JPanel container) {
        JButton btnUndo = UITheme.createStyledButton("Undo", UITheme.BG_PANEL, UITheme.BORDER_DARK, UITheme.TEXT_LIGHT);
        btnUndo.setFont(UITheme.FONT_SMALL);
        btnUndo.setToolTipText("Undo last stroke (Ctrl+Z)");
        btnUndo.addActionListener(e -> canvas.undo());
        container.add(btnUndo);

        JButton btnRedo = UITheme.createStyledButton("Redo", UITheme.BG_PANEL, UITheme.BORDER_DARK, UITheme.TEXT_LIGHT);
        btnRedo.setFont(UITheme.FONT_SMALL);
        btnRedo.setToolTipText("Redo last action (Ctrl+Y)");
        btnRedo.addActionListener(e -> canvas.redo());
        container.add(btnRedo);

        JButton btnClear = UITheme.createStyledButton("Clear Board", UITheme.DANGER, UITheme.DANGER_HOVER, Color.WHITE);
        btnClear.setFont(UITheme.FONT_SMALL);
        btnClear.setToolTipText("Wipe entire canvas");
        btnClear.addActionListener(e -> canvas.clearCanvas(true));
        container.add(btnClear);
    }

    private void initStrokeSection(JPanel container) {
        JLabel lbl = new JLabel("Size:");
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_LIGHT);
        container.add(lbl);

        strokeSlider = new JSlider(1, 60, canvas.getCurrentStrokeSize());
        strokeSlider.setPreferredSize(new Dimension(80, 24));
        strokeSlider.setBackground(UITheme.BG_TOOLBAR);
        strokeSlider.setOpaque(false);
        strokeSlider.addChangeListener(e -> {
            int val = strokeSlider.getValue();
            canvas.setCurrentStrokeSize(val);
            lblStrokeValue.setText(val + "px");
        });
        container.add(strokeSlider);

        lblStrokeValue = new JLabel(canvas.getCurrentStrokeSize() + "px");
        lblStrokeValue.setFont(UITheme.FONT_BOLD);
        lblStrokeValue.setForeground(UITheme.ACCENT_CYAN);
        lblStrokeValue.setPreferredSize(new Dimension(34, 20));
        container.add(lblStrokeValue);

        container.add(createSizePresetBtn("S", 3));
        container.add(createSizePresetBtn("M", 8));
        container.add(createSizePresetBtn("L", 22));
    }

    private JButton createSizePresetBtn(String label, int size) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isPressed()
                        ? new Color(30, 41, 59)
                        : (getModel().isRollover() ? new Color(51, 65, 85) : new Color(15, 23, 42));

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(UITheme.BORDER_DARK);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle b = fm.getStringBounds(getText(), g2).getBounds();
                int tx = (getWidth() - b.width) / 2;
                int ty = (getHeight() - b.height) / 2 + fm.getAscent();

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(UITheme.FONT_BOLD);
        btn.setPreferredSize(new Dimension(28, 24));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText("Set stroke to " + size + "px");
        btn.addActionListener(e -> {
            strokeSlider.setValue(size);
            canvas.setCurrentStrokeSize(size);
        });
        return btn;
    }

    private void initColorSection(JPanel container) {
        JLabel lbl = new JLabel("Colors:");
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_LIGHT);
        container.add(lbl);

        // Active Color Swatch with glowing white ring
        colorPreviewBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());

                g2.setColor(getBackground());
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);

                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.dispose();
            }
        };
        colorPreviewBox.setPreferredSize(new Dimension(26, 26));
        colorPreviewBox.setBackground(canvas.getCurrentColor());
        colorPreviewBox.setOpaque(false);
        colorPreviewBox.setToolTipText("Active drawing color");
        container.add(colorPreviewBox);

        // 16 High-Contrast Vibrant Colors
        Color[] vibrantColors = {
                new Color(15, 23, 42),    // Obsidian Black
                Color.WHITE,              // Pure White
                new Color(239, 68, 68),   // Crimson Red
                new Color(244, 63, 94),   // Rose Coral
                new Color(249, 115, 22),  // Vivid Orange
                new Color(245, 158, 11),  // Amber Gold
                new Color(132, 204, 22),  // Lime Neon
                new Color(16, 185, 129),  // Emerald Green
                new Color(20, 184, 166),  // Mint Teal
                new Color(6, 182, 212),   // Electric Cyan
                new Color(14, 165, 233),  // Sky Blue
                new Color(59, 130, 246),  // Royal Blue
                new Color(99, 102, 241),  // Indigo
                new Color(139, 92, 246),  // Purple Violet
                new Color(217, 70, 239),  // Deep Magenta
                new Color(236, 72, 153)   // Hot Pink
        };

        for (Color c : vibrantColors) {
            JButton swatch = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(c);
                    g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);

                    // Selection border if active
                    if (canvas.getCurrentColor().equals(c)) {
                        g2.setColor(Color.WHITE);
                        g2.setStroke(new BasicStroke(2.5f));
                        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
                    } else {
                        g2.setColor(new Color(255, 255, 255, 70));
                        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 6, 6);
                    }
                    g2.dispose();
                }
            };
            swatch.setPreferredSize(new Dimension(18, 22));
            swatch.setContentAreaFilled(false);
            swatch.setBorderPainted(false);
            swatch.setFocusPainted(false);
            swatch.setCursor(new Cursor(Cursor.HAND_CURSOR));
            swatch.setToolTipText(String.format("Color RGB: %d, %d, %d", c.getRed(), c.getGreen(), c.getBlue()));
            swatch.addActionListener(e -> selectColor(c));
            container.add(swatch);
        }

        JButton btnCustom = UITheme.createGradientButton("Palette...", UITheme.PRIMARY, UITheme.ACCENT_PURPLE, Color.WHITE);
        btnCustom.setPreferredSize(new Dimension(80, 26));
        btnCustom.setFont(UITheme.FONT_SMALL);
        btnCustom.setToolTipText("Open full RGB color chooser");
        btnCustom.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Select Custom Color", canvas.getCurrentColor());
            if (chosen != null) {
                selectColor(chosen);
            }
        });
        container.add(btnCustom);
    }

    private void selectColor(Color c) {
        canvas.setCurrentColor(c);
        colorPreviewBox.setBackground(c);
        colorPreviewBox.repaint();
        repaint();
    }

    private void initBoardThemeSection(JPanel container) {
        JLabel lbl = new JLabel("Theme:");
        lbl.setFont(UITheme.FONT_BOLD);
        lbl.setForeground(UITheme.TEXT_LIGHT);
        container.add(lbl);

        container.add(createThemeButton("White", UITheme.CANVAS_DEFAULT, Color.BLACK, "Classic Whiteboard"));
        container.add(createThemeButton("Blackboard", UITheme.CANVAS_BLACKBOARD, UITheme.ACCENT_CYAN, "Dark Blackboard mode"));
        container.add(createThemeButton("Cream", UITheme.CANVAS_CREAM, new Color(120, 53, 15), "Warm Cream / Parchment"));
        container.add(createThemeButton("Blueprint", UITheme.CANVAS_BLUEPRINT, Color.WHITE, "Technical Blueprint mode"));
    }

    private JButton createThemeButton(String label, Color bgColor, Color recommendedDrawColor, String tooltip) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isPressed()
                        ? new Color(30, 41, 59)
                        : (getModel().isRollover() ? new Color(51, 65, 85) : new Color(15, 23, 42));

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(UITheme.BORDER_DARK);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);

                FontMetrics fm = g2.getFontMetrics(getFont());
                Rectangle b = fm.getStringBounds(getText(), g2).getBounds();
                int tx = (getWidth() - b.width) / 2;
                int ty = (getHeight() - b.height) / 2 + fm.getAscent();

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                g2.drawString(getText(), tx, ty);
                g2.dispose();
            }
        };
        btn.setFont(UITheme.FONT_SMALL);
        btn.setPreferredSize(new Dimension(74, 25));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        btn.addActionListener(e -> {
            canvas.fillCanvasBackground(bgColor);
            if (canvas.getCurrentColor().equals(bgColor)) {
                selectColor(recommendedDrawColor);
            }
        });
        return btn;
    }
}
