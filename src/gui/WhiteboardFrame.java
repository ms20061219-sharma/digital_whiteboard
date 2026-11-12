package gui;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import utilities.FileManager;

/**
 * WhiteboardFrame is the primary application window hosting the menu bar,
 * top toolbar, drawing canvas viewport, and bottom status bar.
 * Demonstrates:
 * - Complex Java Swing layout management (BorderLayout, JScrollPane)
 * - Menu bars, menus, and menu items with keyboard accelerators (Shortcuts)
 * - WindowListener lifecycle management and confirmation dialogs
 */
public class WhiteboardFrame extends JFrame {

    private final DrawingCanvas canvas;
    private final ToolbarPanel toolbar;
    private final StatusBarPanel statusBar;
    private final FileManager fileManager;

    public WhiteboardFrame() {
        super("Digital Whiteboard – Interactive Drawing Application");
        this.fileManager = new FileManager();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1280, 850);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        // Initialize Canvas & Status Bar
        this.canvas = new DrawingCanvas();
        this.statusBar = new StatusBarPanel();
        this.canvas.setStatusBar(statusBar);

        // Initialize Toolbar with Back to Home Screen callback
        this.toolbar = new ToolbarPanel(canvas, this::returnToHomeScreen);

        // Build Menu Bar
        setJMenuBar(createAppMenuBar());

        // Layout Components
        setLayout(new BorderLayout());
        add(toolbar, BorderLayout.NORTH);

        // Wrap canvas in JScrollPane for seamless scrolling if canvas is larger than window
        JScrollPane scrollPane = new JScrollPane(canvas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(UITheme.BG_CANVAS_AREA);
        add(scrollPane, BorderLayout.CENTER);

        add(statusBar, BorderLayout.SOUTH);

        // Safe window close handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    private JMenuBar createAppMenuBar() {
        JMenuBar menuBar = new JMenuBar() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_PANEL); // #0F172A
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        menuBar.setOpaque(true);
        menuBar.setBackground(UITheme.BG_PANEL);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_DARK));

        // 1. FILE MENU
        JMenu menuFile = createStyledMenu("File");
        JMenuItem itemNew = createStyledMenuItem("New Canvas", KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        itemNew.addActionListener(e -> {
            if (canvas.hasUnsavedChanges()) {
                int res = JOptionPane.showConfirmDialog(
                        this,
                        "Current canvas has unsaved changes. Create a new canvas anyway?",
                        "New Canvas",
                        JOptionPane.YES_NO_OPTION
                );
                if (res != JOptionPane.YES_OPTION) return;
            }
            canvas.clearCanvas(false);
            canvas.setHasUnsavedChanges(false);
        });

        JMenuItem itemOpen = createStyledMenuItem("Open Image...", KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);
        itemOpen.addActionListener(e -> {
            BufferedImage loaded = fileManager.openImage(this);
            if (loaded != null) {
                canvas.loadExternalImage(loaded);
                statusBar.updateResolution(loaded.getWidth(), loaded.getHeight());
            }
        });

        JMenuItem itemSave = createStyledMenuItem("Save as PNG...", KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        itemSave.addActionListener(e -> {
            boolean saved = fileManager.saveImage(this, canvas.getCanvasImage());
            if (saved) {
                canvas.setHasUnsavedChanges(false);
            }
        });

        JMenuItem itemHome = createStyledMenuItem("Back to Home Screen", KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK);
        itemHome.addActionListener(e -> returnToHomeScreen());

        JMenuItem itemExit = createStyledMenuItem("Exit Application", KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
        itemExit.addActionListener(e -> confirmExit());

        menuFile.add(itemNew);
        menuFile.add(itemOpen);
        menuFile.add(itemSave);
        menuFile.addSeparator();
        menuFile.add(itemHome);
        menuFile.add(itemExit);

        // 2. EDIT MENU
        JMenu menuEdit = createStyledMenu("Edit");
        JMenuItem itemUndo = createStyledMenuItem("Undo", KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        itemUndo.addActionListener(e -> canvas.undo());

        JMenuItem itemRedo = createStyledMenuItem("Redo", KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);
        itemRedo.addActionListener(e -> canvas.redo());

        JMenuItem itemClear = createStyledMenuItem("Clear Canvas", KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK);
        itemClear.addActionListener(e -> canvas.clearCanvas(true));

        menuEdit.add(itemUndo);
        menuEdit.add(itemRedo);
        menuEdit.addSeparator();
        menuEdit.add(itemClear);

        // 3. CANVAS MENU
        JMenu menuCanvas = createStyledMenu("Canvas");
        JMenuItem itemBgColor = createStyledMenuItem("Change Background Color...", 0, 0);
        itemBgColor.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Select Whiteboard Background Color", canvas.getCanvasBackground());
            if (chosen != null) {
                canvas.fillCanvasBackground(chosen);
            }
        });
        menuCanvas.add(itemBgColor);

        // 4. HELP MENU
        JMenu menuHelp = createStyledMenu("Help");
        JMenuItem itemShortcuts = createStyledMenuItem("Keyboard Shortcuts", 0, 0);
        itemShortcuts.addActionListener(e -> showShortcutsDialog());

        JMenuItem itemAbout = createStyledMenuItem("About Project", 0, 0);
        itemAbout.addActionListener(e -> showAboutDialog());

        menuHelp.add(itemShortcuts);
        menuHelp.add(itemAbout);

        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuCanvas);
        menuBar.add(menuHelp);

        return menuBar;
    }

    private JMenu createStyledMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setFont(UITheme.FONT_BOLD);
        menu.setForeground(Color.WHITE);
        menu.setOpaque(false);
        return menu;
    }

    private JMenuItem createStyledMenuItem(String title, int keyCode, int modifiers) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(UITheme.FONT_REGULAR);
        if (keyCode != 0) {
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
        }
        return item;
    }

    public void returnToHomeScreen() {
        if (canvas.hasUnsavedChanges()) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "You have unsaved artwork!\nDo you want to save before returning to Home?",
                    "Return to Home Screen",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                boolean saved = fileManager.saveImage(this, canvas.getCanvasImage());
                if (saved) {
                    dispose();
                    SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
                }
            } else if (option == JOptionPane.NO_OPTION) {
                dispose();
                SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
            }
        } else {
            dispose();
            SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
        }
    }

    private void confirmExit() {
        if (canvas.hasUnsavedChanges()) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "You have unsaved artwork!\nDo you want to save your drawing before exiting?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                boolean saved = fileManager.saveImage(this, canvas.getCanvasImage());
                if (saved) {
                    dispose();
                    System.exit(0);
                }
            } else if (option == JOptionPane.NO_OPTION) {
                dispose();
                System.exit(0);
            }
        } else {
            dispose();
            System.exit(0);
        }
    }

    private void showShortcutsDialog() {
        String shortcuts = """
                Digital Whiteboard Shortcuts:
                ────────────────────────────────
                Ctrl + H       : Return to Home Screen
                Ctrl + N       : New Canvas
                Ctrl + O       : Open Existing Image
                Ctrl + S       : Save Drawing as PNG
                Ctrl + Z       : Undo Last Action
                Ctrl + Y       : Redo Undone Action
                Ctrl + Delete  : Clear Entire Canvas
                Ctrl + Q       : Exit Application
                """;
        JOptionPane.showMessageDialog(this, shortcuts, "Keyboard Shortcuts", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        String info = """
                Digital Whiteboard – Interactive Drawing Application
                Semester 3 College Project in Computer Science
                
                Developed using:
                • Core Java & Object-Oriented Principles
                • Java Swing (JFrame, JPanel, Menus, Event Handling)
                • Java AWT Graphics2D (Anti-aliased rendering)
                • High-performance BufferedImage Double-Buffering
                • Multi-Level Undo / Redo History
                • File I/O (ImageIO for PNG / JPEG export)
                """;
        JOptionPane.showMessageDialog(this, info, "About Digital Whiteboard", JOptionPane.INFORMATION_MESSAGE);
    }
}

