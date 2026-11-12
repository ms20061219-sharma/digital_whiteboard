package tools;

/**
 * Enum representing all available tools in the Digital Whiteboard application.
 * Demonstrates the use of Enums for type-safe state management.
 */
public enum ToolType {
    PENCIL("Pencil", "Draw fine freehand lines"),
    BRUSH("Brush", "Draw smooth, thick brush strokes"),
    ERASER("Eraser", "Erase unwanted artwork"),
    LINE("Straight Line", "Draw straight line segments"),
    RECTANGLE("Rectangle", "Draw outlined rectangular borders"),
    FILLED_RECTANGLE("Filled Rect", "Draw solid filled rectangles"),
    OVAL("Oval / Circle", "Draw outlined circles or ovals"),
    FILLED_OVAL("Filled Oval", "Draw solid filled circles or ovals"),
    TEXT("Text Tool", "Insert custom text onto canvas");

    private final String displayName;
    private final String description;

    ToolType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

