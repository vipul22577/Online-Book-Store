package org.example.Backend;



import java.sql.*;
public class DatabaseConnection {
    public Connection databaseLink;
    public Connection getConnection(){
        String databaseName = "OnlineBookApplication";
        String databaseUser = "root";
        String databasePassword = "SQLvervipul03@2023";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        }
        catch (Exception e){e.printStackTrace();}
        return databaseLink;
    }
}
