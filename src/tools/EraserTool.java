package tools;

import gui.DrawingCanvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * EraserTool removes drawings by painting with the canvas's current background color.
 * Implements DrawingTool.
 */
public class EraserTool implements DrawingTool {

    private Point lastPoint;

    @Override
    public void onMousePressed(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        this.lastPoint = p;
        int size = canvas.getCurrentStrokeSize();
        Color bg = canvas.getCanvasBackground();

        g2d.setColor(bg);
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
        Color bg = canvas.getCanvasBackground();

        g2d.setColor(bg);
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
        // Renders a visual ring indicator around the eraser cursor
        if (current != null) {
            g2d.setColor(new Color(100, 100, 100, 180));
            g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawOval(current.x - size / 2, current.y - size / 2, size, size);
        }
    }
}

