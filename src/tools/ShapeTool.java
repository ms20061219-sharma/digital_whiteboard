package tools;

import gui.DrawingCanvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * ShapeTool handles geometric shapes: Lines, Rectangles, and Circles/Ovals.
 * Supports both hollow outlines and solid filled shapes.
 * Implements DrawingTool.
 */
public class ShapeTool implements DrawingTool {

    private final ToolType shapeType;

    public ShapeTool(ToolType shapeType) {
        this.shapeType = shapeType;
    }

    @Override
    public void onMousePressed(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        // Shapes are drawn on mouse release; drag triggers dynamic rubberband preview in paintComponent
    }

    @Override
    public void onMouseDragged(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        // Handled via canvas preview rendering and repaint()
    }

    @Override
    public void onMouseReleased(Point p, DrawingCanvas canvas, Graphics2D g2d) {
        Point start = canvas.getDragStartPoint();
        if (start == null) return;

        Color color = canvas.getCurrentColor();
        int size = canvas.getCurrentStrokeSize();

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        drawGeometry(g2d, start, p, shapeType, size);
        canvas.repaint();
    }

    @Override
    public void renderPreview(Graphics2D g2d, Point start, Point current, Color color, int size, boolean filled) {
        if (start == null || current == null) return;

        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawGeometry(g2d, start, current, shapeType, size);
    }

    /**
     * Helper method to render the geometry for both preview and final commit.
     */
    private void drawGeometry(Graphics2D g2d, Point p1, Point p2, ToolType type, int size) {
        int x = Math.min(p1.x, p2.x);
        int y = Math.min(p1.y, p2.y);
        int width = Math.abs(p1.x - p2.x);
        int height = Math.abs(p1.y - p2.y);

        switch (type) {
            case LINE:
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                break;

            case RECTANGLE:
                g2d.drawRect(x, y, width, height);
                break;

            case FILLED_RECTANGLE:
                g2d.fillRect(x, y, width, height);
                break;

            case OVAL:
                g2d.drawOval(x, y, width, height);
                break;

            case FILLED_OVAL:
                g2d.fillOval(x, y, width, height);
                break;

            default:
                break;
        }
    }
}

