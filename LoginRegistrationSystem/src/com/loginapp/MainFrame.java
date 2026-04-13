package com.loginapp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {
    private static final String LOGIN_CARD = "login";
    private static final String REGISTER_CARD = "register";

    private final UserStore userStore = new UserStore();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField registerFullNameField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JPasswordField confirmPasswordField;

    public MainFrame() {
        setTitle("Login and Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(520, 420));
        setLocationRelativeTo(null);
        setResizable(false);

        cards.add(createLoginPanel(), LOGIN_CARD);
        cards.add(createRegisterPanel(), REGISTER_CARD);

        add(cards, BorderLayout.CENTER);
        cardLayout.show(cards, LOGIN_CARD);
    }

    private JPanel createLoginPanel() {
        JPanel panel = createBasePanel("Welcome Back", "Log in with your username and password");

        loginUsernameField = new JTextField(18);
        loginPasswordField = new JPasswordField(18);

        JButton loginButton = createPrimaryButton("Login");
        loginButton.addActionListener(event -> handleLogin());

        JButton showRegisterButton = new JButton("Create account");
        showRegisterButton.addActionListener(event -> switchCard(REGISTER_CARD));

        addFormRow(panel, 2, "Username:", loginUsernameField);
        addFormRow(panel, 3, "Password:", loginPasswordField);
        addButtonRow(panel, 4, loginButton, showRegisterButton);
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = createBasePanel("Create Account", "Register a new user before logging in");

        registerFullNameField = new JTextField(18);
        registerUsernameField = new JTextField(18);
        registerPasswordField = new JPasswordField(18);
        confirmPasswordField = new JPasswordField(18);

        JButton registerButton = createPrimaryButton("Register");
        registerButton.addActionListener(event -> handleRegistration());

        JButton showLoginButton = new JButton("Back to login");
        showLoginButton.addActionListener(event -> switchCard(LOGIN_CARD));

        addFormRow(panel, 2, "Full name:", registerFullNameField);
        addFormRow(panel, 3, "Username:", registerUsernameField);
        addFormRow(panel, 4, "Password:", registerPasswordField);
        addFormRow(panel, 5, "Confirm password:", confirmPasswordField);
        addButtonRow(panel, 6, registerButton, showLoginButton);
        return panel;
    }

    private JPanel createBasePanel(String title, String subtitle) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 248, 252));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(0, 0, 8, 0);
        constraints.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titleLabel, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 20, 0);
        JLabel subtitleLabel = new JLabel(subtitle, SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(90, 90, 90));
        panel.add(subtitleLabel, constraints);

        return panel;
    }

    private void addFormRow(JPanel panel, int rowIndex, String labelText, javax.swing.JComponent field) {
        GridBagConstraints left = new GridBagConstraints();
        left.gridx = 0;
        left.gridy = rowIndex;
        left.insets = new Insets(8, 0, 8, 12);
        left.anchor = GridBagConstraints.LINE_END;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label, left);

        GridBagConstraints right = new GridBagConstraints();
        right.gridx = 1;
        right.gridy = rowIndex;
        right.weightx = 1.0;
        right.fill = GridBagConstraints.HORIZONTAL;
        right.insets = new Insets(8, 0, 8, 0);
        panel.add(field, right);
    }

    private void addButtonRow(JPanel panel, int rowIndex, JButton primaryButton, JButton secondaryButton) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = rowIndex;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(18, 0, 0, 0);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(primaryButton);
        buttons.add(secondaryButton);
        panel.add(buttons, constraints);
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(33, 115, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void switchCard(String cardName) {
        clearFields();
        cardLayout.show(cards, cardName);
    }

    private void handleRegistration() {
        String fullName = registerFullNameField.getText().trim();
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Please complete all registration fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match.");
            return;
        }

        try {
            boolean created = userStore.registerUser(username, fullName, password);
            if (!created) {
                showMessage("That username is already registered.");
                return;
            }

            showMessage("Registration successful. You can now log in.");
            switchCard(LOGIN_CARD);
        } catch (IOException exception) {
            showMessage("Could not save the new account: " + exception.getMessage());
        }
    }

    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Enter both username and password.");
            return;
        }

        try {
            Optional<User> user = userStore.authenticate(username, password);
            if (user.isPresent()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Welcome, " + user.get().getFullName() + "!",
                        "Login Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                clearFields();
            } else {
                showMessage("Invalid username or password.");
            }
        } catch (IOException exception) {
            showMessage("Could not read account data: " + exception.getMessage());
        }
    }

    private void clearFields() {
        if (loginUsernameField != null) {
            loginUsernameField.setText("");
        }
        if (loginPasswordField != null) {
            loginPasswordField.setText("");
        }
        if (registerFullNameField != null) {
            registerFullNameField.setText("");
        }
        if (registerUsernameField != null) {
            registerUsernameField.setText("");
        }
        if (registerPasswordField != null) {
            registerPasswordField.setText("");
        }
        if (confirmPasswordField != null) {
            confirmPasswordField.setText("");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login System", JOptionPane.WARNING_MESSAGE);
    }
}
