package tools;

import gui.DrawingCanvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * BrushTool handles continuous smooth freehand drawing.
 * Implements DrawingTool, demonstrating Polymorphism and Encapsulation.
 */
public class BrushTool implements DrawingTool {

    private Point lastPoint;

    @Override
    public void onMousePressed(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        this.lastPoint = p;
        int size = canvas.getCurrentStrokeSize();
        Color color = canvas.getCurrentColor();

        // Draw an initial dot at the pressed location so clicking without dragging still creates a mark
        g2d.setColor(color);
        g2d.fillOval(p.x - size / 2, p.y - size / 2, Math.max(1, size), Math.max(1, size));
        canvas.repaint();
    }

    @Override
    public void onMouseDragged(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        if (lastPoint == null) {
            lastPoint = p;
            return;
        }

        int size = canvas.getCurrentStrokeSize();
        Color color = canvas.getCurrentColor();

        g2d.setColor(color);
        // Use CAP_ROUND and JOIN_ROUND for perfectly smooth, unbroken brush strokes
        g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawLine(lastPoint.x, lastPoint.y, p.x, p.y);

        this.lastPoint = p;
        canvas.repaint();
    }

    @Override
    public void onMouseReleased(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        this.lastPoint = null;
        canvas.repaint();
    }

    @Override
    public void renderPreview(Graphics2D g2d, Point start, Point current, Color color, int size, boolean filled) {
        // Freehand brush renders directly onto the canvas buffer, so preview is not needed
    }
}

