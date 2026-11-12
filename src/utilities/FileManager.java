package utilities;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * FileManager handles file I/O operations for saving and opening drawings.
 * Demonstrates:
 * - Java File Handling
 * - ImageIO read and write
 * - Exception Handling (try-catch-finally)
 * - JFileChooser integration
 */
public class FileManager {

    private final JFileChooser fileChooser;

    public FileManager() {
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);

        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Image (*.png)", "png");
        FileNameExtensionFilter jpgFilter = new FileNameExtensionFilter("JPEG Image (*.jpg, *.jpeg)", "jpg", "jpeg");

        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpgFilter);
        fileChooser.setFileFilter(pngFilter);
    }

    /**
     * Saves the provided canvas image to a user-selected file.
     * @return true if successfully saved, false otherwise
     */
    public boolean saveImage(Component parent, BufferedImage image) {
        if (image == null) {
            JOptionPane.showMessageDialog(parent, "Canvas is empty. Nothing to save!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String extension = "png";

            if (fileChooser.getFileFilter().getDescription().contains("JPEG")) {
                extension = "jpg";
            }

            // Ensure proper file extension
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith("." + extension)) {
                selectedFile = new File(filePath + "." + extension);
            }

            // Confirm overwrite if file exists
            if (selectedFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(
                        parent,
                        "File already exists: " + selectedFile.getName() + "\nDo you want to overwrite it?",
                        "Confirm Overwrite",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm != JOptionPane.YES_OPTION) {
                    return false;
                }
            }

            try {
                // For JPEG, we need an RGB image without transparency to prevent color distortion
                if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                    BufferedImage rgbImage = new BufferedImage(
                            image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = rgbImage.createGraphics();
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                    ImageIO.write(rgbImage, "jpg", selectedFile);
                } else {
                    ImageIO.write(image, "png", selectedFile);
                }

                JOptionPane.showMessageDialog(
                        parent,
                        "Drawing saved successfully to:\n" + selectedFile.getAbsolutePath(),
                        "Saved Successfully",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Error saving file: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return false;
    }

    /**
     * Opens an existing image file from disk and returns it as a BufferedImage.
     * @return BufferedImage if loaded, null otherwise
     */
    public BufferedImage openImage(Component parent) {
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage loadedImage = ImageIO.read(file);
                if (loadedImage == null) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "The selected file could not be read as an image.",
                            "Invalid Image Format",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return null;
                }
                JOptionPane.showMessageDialog(
                        parent,
                        "Loaded image successfully: " + file.getName(),
                        "Image Loaded",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return loadedImage;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                        parent,
                        "Failed to open image file: " + ex.getMessage(),
                        "File Open Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return null;
    }
}

