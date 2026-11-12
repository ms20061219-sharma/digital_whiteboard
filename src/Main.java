import gui.WelcomeScreen;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main application entry point for Digital Whiteboard.
 * Semester 3 Computer Science Project.
 * Initializes desktop look-and-feel and launches the WelcomeScreen on the Swing Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Enable sub-pixel text anti-aliasing for crisp typography across all Swing components
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                // Apply system look and feel for native window decorations where possible
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                // Ensure high-contrast dark theme defaults for menu components
                UIManager.put("MenuBar.background", new java.awt.Color(15, 23, 42));
                UIManager.put("Menu.foreground", java.awt.Color.WHITE);
                UIManager.put("MenuItem.selectionBackground", new java.awt.Color(99, 102, 241));
                UIManager.put("MenuItem.selectionForeground", java.awt.Color.WHITE);
            } catch (Exception ignored) {
                // Fallback to default Swing Look and Feel
            }

            WelcomeScreen welcomeScreen = new WelcomeScreen();
            welcomeScreen.setVisible(true);
        });
    }
}

