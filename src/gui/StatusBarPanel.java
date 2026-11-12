package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import tools.ToolType;

/**
 * StatusBarPanel displays live metrics and tool state with attractive styled badges:
 * - Active Tool pill badge
 * - Live mouse cursor coordinates (X, Y)
 * - Stroke size indicator badge
 * - Active drawing color swatch with glowing ring
 * - Canvas resolution badge
 */
public class StatusBarPanel extends JPanel {

    private final JLabel lblTool;
    private final JLabel lblCoords;
    private final JLabel lblStroke;
    private final JLabel lblResolution;
    private final JPanel pnlColorPreview;

    public StatusBarPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(11, 15, 25));
        setBorder(new CompoundBorder(
                new MatteBorder(1, 0, 0, 0, UITheme.BORDER_DARK),
                new EmptyBorder(6, 16, 6, 16)
        ));

        // Left Container: Tool badge & Color preview
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        leftPanel.setOpaque(false);

        lblTool = new JLabel("Tool: 🖌️ Brush");
        lblTool.setFont(UITheme.FONT_BOLD);
        lblTool.setForeground(UITheme.ACCENT_CYAN);

        JLabel lblColorText = new JLabel("Color:");
        lblColorText.setFont(UITheme.FONT_REGULAR);
        lblColorText.setForeground(UITheme.TEXT_MUTED);

        pnlColorPreview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // White ring
                g2.setColor(Color.WHITE);
                g2.fillOval(0, 0, getWidth(), getHeight());

                // Colored circle
                g2.setColor(getBackground());
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                g2.dispose();
            }
        };
        pnlColorPreview.setPreferredSize(new Dimension(18, 18));
        pnlColorPreview.setBackground(Color.BLACK);
        pnlColorPreview.setOpaque(false);

        lblStroke = new JLabel("Stroke: 4 px");
        lblStroke.setFont(UITheme.FONT_REGULAR);
        lblStroke.setForeground(UITheme.ACCENT_AMBER);

        leftPanel.add(lblTool);
        leftPanel.add(lblColorText);
        leftPanel.add(pnlColorPreview);
        leftPanel.add(lblStroke);

        // Right Container: Mouse coordinates and canvas size
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightPanel.setOpaque(false);

        lblCoords = new JLabel("X: 0 px, Y: 0 px");
        lblCoords.setFont(UITheme.FONT_REGULAR);
        lblCoords.setForeground(UITheme.TEXT_LIGHT);

        lblResolution = new JLabel("Canvas: 1200 x 800 px");
        lblResolution.setFont(UITheme.FONT_REGULAR);
        lblResolution.setForeground(UITheme.ACCENT_PURPLE);

        rightPanel.add(lblCoords);
        rightPanel.add(lblResolution);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void updateTool(ToolType tool) {
        lblTool.setText("Tool: " + tool.getDisplayName());
    }

    public void updateCoordinates(int x, int y) {
        lblCoords.setText("X: " + x + " px, Y: " + y + " px");
    }

    public void updateStroke(int size) {
        lblStroke.setText("Stroke: " + size + " px");
    }

    public void updateColor(Color color) {
        pnlColorPreview.setBackground(color);
        pnlColorPreview.repaint();
    }

    public void updateResolution(int width, int height) {
        lblResolution.setText("Canvas: " + width + " x " + height + " px");
    }
}
