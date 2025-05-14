package org.example.Backend;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Triggers {
    private DatabaseConnection connectNow = new DatabaseConnection();
    private Connection connectDB = connectNow.getConnection();
    public Triggers() throws SQLException {
        String trigger1SQL = "CREATE TRIGGER enforce_quantity_constraint "
                + "BEFORE INSERT ON book "
                + "FOR EACH ROW "
                + "BEGIN "
                + "   IF NEW.ISBN < 0 OR NEW.ISBN > 999999999 THEN "
                + "       SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Illegal ISBN Value'; "
                + "   END IF; "
                + "END";

        String trigger2SQL = "CREATE TRIGGER cart_update "
                + "AFTER INSERT ON cart "
                + "FOR EACH ROW "
                + "BEGIN "
                + "   IF NEW.num_items = 0 OR NEW.total_amount = 0 THEN "
                + "       SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Zero items cannot be added'; "
                + "   END IF; "
                + "END";
        Statement statement = connectDB.createStatement();

        //statement.executeUpdate(trigger1SQL);
        System.out.println("Trigger 'enforce_quantity_constraint' created successfully.");

        //statement.executeUpdate(trigger2SQL);
        System.out.println("Trigger 'cart_update' created successfully.");
        connectDB.close();
    }
}
