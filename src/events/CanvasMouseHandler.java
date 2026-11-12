package events;

import gui.DrawingCanvas;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import tools.DrawingTool;
import tools.ToolType;

/**
 * CanvasMouseHandler handles all mouse user events on the drawing canvas.
 * Extends MouseAdapter to demonstrate:
 * - Event-Driven Programming in Java Swing
 * - MouseListener and MouseMotionListener handling
 * - Clean decoupling of UI input from internal drawing logic
 */
public class CanvasMouseHandler extends MouseAdapter {

    private final DrawingCanvas canvas;

    public CanvasMouseHandler(DrawingCanvas canvas) {
        this.canvas = canvas;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        Point point = e.getPoint();
        canvas.setDragStartPoint(point);
        canvas.setDragCurrentPoint(point);
        canvas.setDrawing(true);

        // Capture an undo snapshot before modifying the canvas image
        canvas.saveSnapshotForUndo();

        DrawingTool tool = canvas.getTool(canvas.getCurrentTool());
        if (tool != null) {
            tool.onMousePressed(point, canvas, canvas.getCanvasGraphics());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point point = e.getPoint();
        canvas.setDragCurrentPoint(point);
        canvas.updateCursorPosition(point.x, point.y);

        DrawingTool tool = canvas.getTool(canvas.getCurrentTool());
        if (tool != null) {
            tool.onMouseDragged(point, canvas, canvas.getCanvasGraphics());
        }

        // Trigger a repaint so the live rubberband preview updates smoothly
        canvas.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        Point point = e.getPoint();
        DrawingTool tool = canvas.getTool(canvas.getCurrentTool());
        if (tool != null) {
            tool.onMouseReleased(point, canvas, canvas.getCanvasGraphics());
        }

        canvas.setDrawing(false);
        canvas.setDragStartPoint(null);
        canvas.setDragCurrentPoint(null);
        canvas.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        canvas.updateCursorPosition(point.x, point.y);

        // If eraser is active, track coordinates to display the circular eraser cursor ring
        if (canvas.getCurrentTool() == ToolType.ERASER) {
            canvas.setDragCurrentPoint(point);
            canvas.repaint();
        }
    }
}
