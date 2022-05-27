module com.example.carrental {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.carrental to javafx.fxml;
    exports com.example.carrental;
}