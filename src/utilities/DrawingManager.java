package utilities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * DrawingManager handles Undo and Redo operations using snapshots of the canvas.
 * Demonstrates:
 * - Data Structures (Deque / Stacks)
 * - Deep copying of BufferedImage objects
 * - Memory management with history bounds
 */
public class DrawingManager {

    private static final int MAX_HISTORY_STEPS = 25;

    private final Deque<BufferedImage> undoStack = new ArrayDeque<>();
    private final Deque<BufferedImage> redoStack = new ArrayDeque<>();

    /**
     * Creates an exact deep copy of a BufferedImage.
     */
    public static BufferedImage copyImage(BufferedImage source) {
        if (source == null) return null;
        BufferedImage copy = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = copy.createGraphics();
        g2.drawImage(source, 0, 0, null);
        g2.dispose();
        return copy;
    }

    /**
     * Saves the current canvas state before a new drawing stroke or modification.
     */
    public void saveSnapshot(BufferedImage currentCanvas) {
        if (currentCanvas == null) return;

        // If stack exceeds capacity, remove the oldest snapshot
        if (undoStack.size() >= MAX_HISTORY_STEPS) {
            undoStack.removeLast();
        }

        undoStack.push(copyImage(currentCanvas));
        // A new user action invalidates the redo history
        redoStack.clear();
    }

    /**
     * Performs an Undo operation.
     * @param currentCanvas Current visible canvas image
     * @return The previous canvas image snapshot, or null if cannot undo
     */
    public BufferedImage undo(BufferedImage currentCanvas) {
        if (!canUndo()) return null;

        redoStack.push(copyImage(currentCanvas));
        return undoStack.pop();
    }

    /**
     * Performs a Redo operation.
     * @param currentCanvas Current visible canvas image
     * @return The next canvas image snapshot, or null if cannot redo
     */
    public BufferedImage redo(BufferedImage currentCanvas) {
        if (!canRedo()) return null;

        undoStack.push(copyImage(currentCanvas));
        return redoStack.pop();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
    }
}

