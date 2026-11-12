package gui;

import events.CanvasMouseHandler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tools.*;
import utilities.DrawingManager;

/**
 * DrawingCanvas is the core interactive component where artwork is drawn and displayed.
 * Extends JPanel, overriding paintComponent to support:
 * - High quality anti-aliased graphics
 * - Persistent off-screen BufferedImage buffer
 * - Live dynamic rubberband preview for shapes and eraser
 * - Multi-level Undo / Redo
 */
public class DrawingCanvas extends JPanel {

    private int canvasWidth = 1200;
    private int canvasHeight = 800;

    private BufferedImage canvasImage;
    private Graphics2D canvasGraphics;

    // Current Drawing State
    private Color currentColor = Color.BLACK;
    private Color canvasBackground = UITheme.CANVAS_DEFAULT;
    private int currentStrokeSize = 4;
    private ToolType currentTool = ToolType.BRUSH;

    // Mouse Interaction Tracking
    private Point dragStartPoint;
    private Point dragCurrentPoint;
    private boolean isDrawing = false;
    private boolean hasUnsavedChanges = false;

    // Managers & Handlers
    private final DrawingManager drawingManager = new DrawingManager();
    private final Map<ToolType, DrawingTool> toolRegistry = new EnumMap<>(ToolType.class);
    private StatusBarPanel statusBar;

    public DrawingCanvas() {
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        setBackground(UITheme.BG_CANVAS_AREA);

        initializeCanvasBuffer();
        registerTools();

        CanvasMouseHandler handler = new CanvasMouseHandler(this);
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    /**
     * Initializes the offscreen BufferedImage and configures high-quality anti-aliasing hints.
     */
    private void initializeCanvasBuffer() {
        canvasImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        canvasGraphics = canvasImage.createGraphics();

        // Enable professional-grade anti-aliasing for smooth curves and strokes
        canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvasGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        canvasGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // Fill background with initial color
        fillCanvasBackground(canvasBackground);
    }

    /**
     * Registers all tool implementations according to the Open-Closed Principle.
     */
    private void registerTools() {
        toolRegistry.put(ToolType.PENCIL, new BrushTool());
        toolRegistry.put(ToolType.BRUSH, new BrushTool());
        toolRegistry.put(ToolType.ERASER, new EraserTool());
        toolRegistry.put(ToolType.LINE, new ShapeTool(ToolType.LINE));
        toolRegistry.put(ToolType.RECTANGLE, new ShapeTool(ToolType.RECTANGLE));
        toolRegistry.put(ToolType.FILLED_RECTANGLE, new ShapeTool(ToolType.FILLED_RECTANGLE));
        toolRegistry.put(ToolType.OVAL, new ShapeTool(ToolType.OVAL));
        toolRegistry.put(ToolType.FILLED_OVAL, new ShapeTool(ToolType.FILLED_OVAL));
        toolRegistry.put(ToolType.TEXT, new TextTool());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Enable anti-aliasing for on-screen painting
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw the persistent canvas buffer
        if (canvasImage != null) {
            g2d.drawImage(canvasImage, 0, 0, null);
        }

        // 2. Render active shape or eraser preview if user is currently dragging
        DrawingTool activeTool = toolRegistry.get(currentTool);
        if (activeTool != null) {
            if (isDrawing && dragStartPoint != null && dragCurrentPoint != null) {
                boolean filled = (currentTool == ToolType.FILLED_RECTANGLE || currentTool == ToolType.FILLED_OVAL);
                activeTool.renderPreview(g2d, dragStartPoint, dragCurrentPoint, currentColor, currentStrokeSize, filled);
            } else if (currentTool == ToolType.ERASER && dragCurrentPoint != null) {
                activeTool.renderPreview(g2d, null, dragCurrentPoint, currentColor, currentStrokeSize, false);
            }
        }

        g2d.dispose();
    }

    /**
     * Fills the canvas buffer with the specified background color.
     */
    public void fillCanvasBackground(Color color) {
        if (canvasGraphics == null) return;
        canvasBackground = color;
        canvasGraphics.setColor(color);
        canvasGraphics.fillRect(0, 0, canvasWidth, canvasHeight);
        repaint();
    }

    /**
     * Clears the entire canvas with a confirmation dialog.
     */
    public void clearCanvas(boolean promptConfirmation) {
        if (promptConfirmation) {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to clear the entire whiteboard?\nAll unsaved work will be lost.",
                    "Clear Canvas",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        saveSnapshotForUndo();
        fillCanvasBackground(canvasBackground);
        hasUnsavedChanges = true;
    }

    /**
     * Saves a snapshot to the undo stack before applying changes.
     */
    public void saveSnapshotForUndo() {
        if (canvasImage != null) {
            drawingManager.saveSnapshot(canvasImage);
            hasUnsavedChanges = true;
        }
    }

    /**
     * Undoes the last drawing action.
     */
    public void undo() {
        if (!drawingManager.canUndo()) {
            return;
        }
        BufferedImage restored = drawingManager.undo(canvasImage);
        if (restored != null) {
            applyLoadedImage(restored);
        }
    }

    /**
     * Redoes the previously undone action.
     */
    public void redo() {
        if (!drawingManager.canRedo()) {
            return;
        }
        BufferedImage restored = drawingManager.redo(canvasImage);
        if (restored != null) {
            applyLoadedImage(restored);
        }
    }

    /**
     * Loads an external image into the canvas buffer.
     */
    public void loadExternalImage(BufferedImage newImg) {
        if (newImg == null) return;
        saveSnapshotForUndo();
        applyLoadedImage(newImg);
    }

    private void applyLoadedImage(BufferedImage newImg) {
        this.canvasWidth = Math.max(canvasWidth, newImg.getWidth());
        this.canvasHeight = Math.max(canvasHeight, newImg.getHeight());
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        revalidate();

        canvasImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        canvasGraphics = canvasImage.createGraphics();
        canvasGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        canvasGraphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        canvasGraphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        canvasGraphics.setColor(canvasBackground);
        canvasGraphics.fillRect(0, 0, canvasWidth, canvasHeight);
        canvasGraphics.drawImage(newImg, 0, 0, null);

        repaint();
    }

    // Getters and Setters
    public BufferedImage getCanvasImage() { return canvasImage; }
    public Graphics2D getCanvasGraphics() { return canvasGraphics; }
    public Color getCurrentColor() { return currentColor; }
    public void setCurrentColor(Color color) {
        this.currentColor = color;
        if (statusBar != null) statusBar.updateColor(color);
    }
    public Color getCanvasBackground() { return canvasBackground; }
    public int getCurrentStrokeSize() { return currentStrokeSize; }
    public void setCurrentStrokeSize(int size) {
        this.currentStrokeSize = Math.max(1, Math.min(100, size));
        if (statusBar != null) statusBar.updateStroke(this.currentStrokeSize);
    }
    public ToolType getCurrentTool() { return currentTool; }
    public void setCurrentTool(ToolType tool) {
        this.currentTool = tool;
        if (statusBar != null) statusBar.updateTool(tool);
    }
    public Point getDragStartPoint() { return dragStartPoint; }
    public void setDragStartPoint(Point p) { this.dragStartPoint = p; }
    public Point getDragCurrentPoint() { return dragCurrentPoint; }
    public void setDragCurrentPoint(Point p) { this.dragCurrentPoint = p; }
    public boolean isDrawing() { return isDrawing; }
    public void setDrawing(boolean drawing) { this.isDrawing = drawing; }
    public boolean hasUnsavedChanges() { return hasUnsavedChanges; }
    public void setHasUnsavedChanges(boolean changes) { this.hasUnsavedChanges = changes; }
    public DrawingTool getTool(ToolType type) { return toolRegistry.get(type); }
    public void setStatusBar(StatusBarPanel statusBar) { this.statusBar = statusBar; }

    public void updateCursorPosition(int x, int y) {
        if (statusBar != null) {
            statusBar.updateCoordinates(x, y);
        }
    }
}
