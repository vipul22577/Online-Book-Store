module org.example.Frontend{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.security.jgss;
    opens org.example.Frontend to javafx.fxml;
    exports org.example.Frontend;
    // Export your package to javafx.fxml which is necessary for FXML loading
    opens org.example.Backend to javafx.fxml;



}