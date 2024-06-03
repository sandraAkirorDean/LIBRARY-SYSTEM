/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vulibrary;

/**
 *
 * @author DALL
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VUGUI extends JFrame {
    private JTextField bookIdField, titleField, authorField, yearField;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    // Constructor
    public VUGUI() {
        setTitle("VU Library");
        setSize(700, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Form panel for adding books
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        bookIdField = new JTextField(20);
        formPanel.add(bookIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        formPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        authorField = new JTextField(20);
        formPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        yearField = new JTextField(20);
        formPanel.add(yearField, gbc);

        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton refreshButton = new JButton("Refresh List");

        // Set button colors
        addButton.setBackground(Color.RED);
        addButton.setForeground(Color.BLACK);
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.BLACK);
        refreshButton.setBackground(Color.RED);
        refreshButton.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(addButton, gbc);
        gbc.gridx = 1;
        formPanel.add(deleteButton, gbc);
        gbc.gridx = 2;
        formPanel.add(refreshButton, gbc);

        add(formPanel, BorderLayout.NORTH);

        // Table to display books
        String[] columnNames = {"Book ID", "Title", "Author", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook(bookIdField.getText(), titleField.getText(), authorField.getText(), yearField.getText());
                refreshBookList();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookId = tableModel.getValueAt(selectedRow, 0).toString();
                    deleteBook(bookId);
                    refreshBookList();
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBookList();
            }
        });

        // Initialize the book list
        refreshBookList();

        setVisible(true);
    }

    private void addBook(String bookId, String title, String author, String year) {
        String sql = "INSERT INTO Books (BookID, Title, Author, Year) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection("jdbc:ucanaccess://C:/Users/DALL/Documents/Database2.accdb");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, year);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteBook(String bookId) {
        String sql = "DELETE FROM Books WHERE BookID = ?";

        try (Connection conn = DatabaseConnection.getConnection("jdbc:ucanaccess://C:/Users/DALL/Documents/Database2.accdb");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshBookList() {
        String sql = "SELECT * FROM Books";

        try (Connection conn = DatabaseConnection.getConnection("jdbc:ucanaccess://C:/Users/DALL/Documents/Database2.accdb");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            tableModel.setRowCount(0);  // Clear existing rows

            while (rs.next()) {
                String bookId = rs.getString("BookID");
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String year = rs.getString("Year");
                tableModel.addRow(new Object[]{bookId, title, author, year});
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new VUGUI();
    }
}

class DatabaseConnection {
    private static final String URL = "jdbc:ucanaccess://C:/Users/DALL/Documents/Database2.accdb";

    public static Connection getConnection(String jdbcucanaccessCUsersDALLDocumentsDatabase) throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
    

