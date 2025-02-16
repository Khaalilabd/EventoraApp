package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mydatasource {
    private static Mydatasource instance;
    private static final String URL = "jdbc:mysql://localhost:3306/eventora";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private Connection conn;

    // Constructeur privé pour empêcher l'instanciation directe
    private Mydatasource() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public static Mydatasource getInstance() {
        if (instance == null) {
            instance = new Mydatasource();
        }
        return instance;
    }

    public Connection getConnection() {
        return conn;
    }
}
