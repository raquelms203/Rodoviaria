package model;

import java.sql.*;
import javax.swing.*;


/**
 * Classe para fazer a conecção com o banco de dados SQLite (Library sqlite-jdbc.jar)
 * @author raquelms203
 *
 */
public class SqliteConnection {

	public static Connection dbBilheteria() {		
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:D:\\Raquel\\Documents\\Programacao\\rodoviaria\\database\\bilheteria.sqlite");
			return conn;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Sistema caiu!\n"+e);
			return null;
		}
	}	
}
