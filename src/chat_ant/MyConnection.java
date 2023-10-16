/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_ant;

import java.sql.*;
import javax.swing.*;
/**
 *
 * @author PC
 */
public class MyConnection {
        public Connection getConnection() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost:3306/chatting?user=root&password=";
                Connection con = DriverManager.getConnection(url);
                return con;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.toString(), "Loi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
}
