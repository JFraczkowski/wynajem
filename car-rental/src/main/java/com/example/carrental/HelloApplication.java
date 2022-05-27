package com.example.carrental;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class HelloApplication extends Application {
    static HelloController controller = new HelloController();
    public static Database db;

    @Override
    public void start(Stage stage) throws IOException, SQLException {
        controller.switchToInitScene();
    }

    public static void main(String[] args) {
        launch();
    }

    static void dbConnect() throws SQLException, IOException {
        db = new Database("sa", "oracle12", "localhost", "1433", "carrent");
        db.getConnection();
        controller.switchToMenuScene();
    }
}