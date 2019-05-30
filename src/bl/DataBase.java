package bl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Singelton Design Pattern besteht aus einem private Konstruktor, privaten
 * Instance und einer public Methode zum erstellen der Instance
 */
/**
 * Class for accessing the Database implemented as Singelton
 *
 * @author vizug
 */
public class DataBase {

    private final Connection conn;
    /**
     * the one and only instance of the DataBase class
     */
    private static DataBase theInstance;

    /**
     * Constructor is private for Singelton implementation
     */
    public DataBase() throws SQLException {
        conn = DriverManager.getConnection("jdbc:postgresql://localhost/Projekt", "postgres", "postgres");

    }

    /**
     * If the instance hasn't been created before, it gets created
     *
     * @return Return the Database instance
     */
    public synchronized static DataBase getInstance() throws SQLException {
        if (theInstance == null) {
            theInstance = new DataBase();
        }
        return theInstance;
    }

    public void insertHighscore(Highscore hs) throws SQLException {

        String sql = "INSERT INTO HIGHSCORE VALUES(?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, hs.getUsername());
        ps.setInt(2, hs.getScore());

        ps.executeUpdate();

    }

    public ArrayList<Highscore> getScores() throws Exception {
        ArrayList<Highscore> highscores = new ArrayList<>();

        String sql = "SELECT * FROM HIGHSCORE";

        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);

        while (rs.next()) {
            Highscore hs = new Highscore(rs.getInt("Score"), rs.getString("Username"));
            highscores.add(hs);
        }
        return highscores;
    }

}
