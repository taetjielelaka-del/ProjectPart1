package com.loginapp;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class AppLauncher {

    private AppLauncher() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            System.err.println("Could not apply system look and feel: " + exception.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
