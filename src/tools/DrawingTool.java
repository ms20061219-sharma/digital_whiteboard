package tools;

import gui.DrawingCanvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * DrawingTool Interface.
 * Demonstrates Abstraction and Polymorphism in Object-Oriented Programming.
 * Each drawing tool (Brush, Eraser, Shapes, Text) implements this contract.
 */
public interface DrawingTool {

    /**
     * Triggered when the user presses the mouse button on the canvas.
     */
    void onMousePressed(Point p, DrawingCanvas canvas, Graphics2D g2d);

    /**
     * Triggered continuously as the mouse is dragged across the canvas.
     */
    void onMouseDragged(Point p, DrawingCanvas canvas, Graphics2D g2d);

    /**
     * Triggered when the mouse button is released.
     */
    void onMouseReleased(Point p, DrawingCanvas canvas, Graphics2D g2d);

    /**
     * Renders a real-time rubberband preview during drag operations without
     * permanently modifying the underlying canvas buffer.
     */
    void renderPreview(Graphics2D g2d, Point start, Point current, Color color, int size, boolean filled);
}

