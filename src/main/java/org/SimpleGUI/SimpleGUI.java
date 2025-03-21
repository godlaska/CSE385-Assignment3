/**
 * @author Keigen Godlaski
 * @date 3/21/2025
 * @description Simple GUI Application to display data from a MySQL database
 * using HikariCP for connection pooling. ChatGPT assisted in writing the
 * code for connection pooling and loading Maven dependencies. Maven is used
 * mostly to import the HikariCP library. The GUI is created using Java Swing.
 */

package org.SimpleGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SimpleGUI {
    private static HikariDataSource dataSource;

    public static void main(String[] args) {
        // Configure HikariCP Connection Pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/world");
        config.setUsername("root");
        config.setPassword("pass");
        config.setMaximumPoolSize(3);
        // Load data
        dataSource = new HikariDataSource(config);

        // The invokeLater method is used to ensure that the GUI creation
        // and updates are done on the Event Dispatch Thread (Swing's handling
        // of GUI updates, user input, and component threads). Documentation
        // found at...
        // https://docs.oracle.com/javase/8/docs/api/javax/swing/SwingUtilities.html#invokeLater-java.lang.Runnable-
        SwingUtilities.invokeLater(SimpleGUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // JFrame documentation found at...
        // https://www.geeksforgeeks.org/java-jframe/
        JFrame frame = new JFrame("City Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Documentation found at...
        // https://docs.oracle.com/javase/8/docs/api/javax/swing/table/DefaultTableModel.html
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Add data into the table model from the world.city database
        loadData(model);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private static void loadData(DefaultTableModel model) {
        String query = "SELECT * FROM world.city";
        try {
            Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Get database column names and add them to the table model. This
            // isn't technically necessary, but it makes getting the column
            // names much easier than hardcoding them
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add data from the database to the table model
            while (resultSet.next()) {
                // Object array that holds row data
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    // Populate row data
                    row[i] = resultSet.getObject(i + 1);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the datasource connection, multiple attempts are made
            // to ensure it closes properly
            try {
                if (dataSource != null) {
                    dataSource.close();
                }
                if (dataSource != null) {
                    dataSource.close();
                }
                if (dataSource != null) {
                    dataSource.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
