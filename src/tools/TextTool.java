package tools;

import gui.DrawingCanvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.JOptionPane;

/**
 * TextTool allows users to click on the canvas and insert custom text.
 * Demonstrates GUI dialog interaction and Font rendering in Java 2D.
 */
public class TextTool implements DrawingTool {

    @Override
    public void onMousePressed(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        String input = JOptionPane.showInputDialog(
                canvas,
                "Enter text to place on whiteboard:",
                "Insert Text",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input != null && !input.trim().isEmpty()) {
            Color color = canvas.getCurrentColor();
            int fontSize = Math.max(14, canvas.getCurrentStrokeSize() * 3);

            g2d.setColor(color);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
            g2d.drawString(input, p.x, p.y);
            canvas.repaint();
        }
    }

    @Override
    public void onMouseDragged(Point p, DrawingCanvas canvas, Graphics2D g2d) {}

    @Override
    public void onMouseReleased(Point p, DrawingCanvas canvas, Graphics2D g2d) {}

    @Override
    public void renderPreview(Graphics2D g2d, Point start, Point current, Color color, int size, boolean filled) {}
}

