package com.example.carrental;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;


//    STARTAPP BTN
    @FXML
    private Button startAppBtn;

//    MENU BUTTONS
    @FXML
    private Label statusText;
    @FXML
    private Button rentBtn;
    @FXML
    private Button returnBtn;
    @FXML
    private Button repairBtn;
    @FXML
    private Button repairReturnBtn;
    @FXML
    private Button descriptionBtn;
    @FXML
    private Button statisticsBtn;
    @FXML
    private Button backBtn;

//    RENT OBJECTS
    @FXML
    private ChoiceBox<String> availableCars;
    @FXML
    private ChoiceBox<String> availableClients;
    @FXML
    private Label carPrice;
    @FXML
    private Button rentCarBtn;
    @FXML
    private Button addClientBtn;

//    RENT RETURN OBJECTS
    @FXML
    private ChoiceBox<String> rentedCars;
    @FXML
    private Label clientData;
    @FXML
    private Button carRentReturnBtn;

//    ADD CLIENT OBJECTS
    @FXML
    private TextField clientName;
    @FXML
    private ChoiceBox<String> clientType;
    @FXML
    private TextField clientNIP;
    @FXML
    private Button clientAddBtn;

//    REPAIR CAR OBJECTS
    @FXML
    private ChoiceBox<String> availableEmployees;
    @FXML
    private TextField repairReason;
    @FXML
    private Button repairCarBtn;

//    REPAIR RETURN OBJECTS
    @FXML
    private ChoiceBox<String> availableRepairedCars;
    @FXML
    private Button returnRepairBtn;

//    STATISTICS OBJECTS
    @FXML
    private Button feesSumBtn;
    @FXML
    private Button brokenCarsBtn;
    @FXML
    private Button nonperishableCarsBtn;
    @FXML
    private Button mostRentedCarBtn;
    @FXML
    private Button longestRentedCarBtn;

//    FEES SUM OBJECTS
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateTo;
    @FXML
    private Button showStatsBtn;

    public void switchToInitScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("init.fxml"));
        loader.setController(this);
        root = loader.load();
        stage = new Stage();
        stage.setTitle("Car rental");
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

        startAppBtn.setOnAction(e -> {
            try {
                HelloApplication.dbConnect();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void switchToMenuScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("menu.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

//        event listeners in menu
        rentBtn.setOnAction(e -> {
            try {
                switchToCarRentScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        returnBtn.setOnAction(e -> {
            try {
                switchToCarReturnScene();
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
            }
        });
        repairBtn.setOnAction(e -> {
            try {
                switchToCarRepairScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        repairReturnBtn.setOnAction(e -> {
            try {
                switchToCarRepairReturnScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        descriptionBtn.setOnAction(e -> {
            try {
                switchToProjectDescriptionScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        statisticsBtn.setOnAction(e -> {
            try {
                switchToStatisticsScene();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    public void switchToCarRentScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("car-rent.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

        List<String> cars = HelloApplication.db.getAvailableCars();
        List<String> clients = HelloApplication.db.getClients();

        availableCars.getItems().addAll(cars);
        availableClients.getItems().addAll(clients);

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToMenuScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        availableCars.setOnAction(e -> {
            try {
                int carId = Integer.parseInt(String.valueOf(availableCars.getValue().charAt(0)));
                int price = HelloApplication.db.getCarPrice(carId);
                carPrice.setText(price + " zł / dzień");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        rentCarBtn.setOnAction(e -> {
            try {
                if(availableCars.valueProperty() != null && availableClients.getValue() != null) {
                    System.out.println("RENT");
//                    TODO
                } else {
                    System.out.println("NO!");
//                    TODO
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        addClientBtn.setOnAction(e -> {
            try {
                switchToAddClientScene();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        rentCarBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");

            if(availableCars.getValue() == null || availableClients.getValue() == null) {
                alert.setContentText("Nie wybrano klienta lub samochodu!");
            } else {
                int carId = Integer.parseInt(String.valueOf(availableCars.getValue().charAt(0)));
                int clientId = Integer.parseInt(String.valueOf(availableClients.getValue().charAt(0)));
                HelloApplication.db.addCarRentBilling(carId, clientId);
                alert.setContentText("Wypożyczono samochód!");
            }
            alert.showAndWait();
        });
    }

    public void switchToCarReturnScene() throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("car-return.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

        List<String> cars = HelloApplication.db.getRentedCars();

        rentedCars.getItems().addAll(cars);

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToMenuScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        rentedCars.setOnAction(e -> {
            try {
                int carId = Integer.parseInt(String.valueOf(rentedCars.getValue().charAt(0)));
                String client = HelloApplication.db.getRentedCarClient(carId);
                clientData.setText(client);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        carRentReturnBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            if(rentedCars.getValue() == null) {
                alert.setContentText("Musisz wybrać auto do zwrotu!");
            } else {
                int carId = Integer.parseInt(String.valueOf(rentedCars.getValue().charAt(0)));
                String data = HelloApplication.db.returnRentedCar(carId);
                alert.setContentText("Oddano samochód " + rentedCars.getValue() + ".\n" + data);
            }
            alert.showAndWait();
        });
    }

    public void switchToCarRepairScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("car-repair.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

        List<String> cars = HelloApplication.db.getAvailableCars();
        List<String> employees = HelloApplication.db.getEmployees();

        availableCars.getItems().addAll(cars);
        availableEmployees.getItems().addAll(employees);

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToMenuScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        repairCarBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            if(availableCars.getValue() == null || availableEmployees.getValue() == null) {
                alert.setContentText("Nie wybrano samochodu lub pracownika zlecającego naprawe!");
            } else {
                if(repairReason.getText().equals("")) {
                    alert.setContentText("Nie podano powodu naprawy!");
                } else {
                    String car = availableCars.getValue().split(" ")[1];
                    String employee = availableEmployees.getValue().split(" ")[1];
                    String reason = repairReason.getText();
                    int carId =  Integer.parseInt(String.valueOf(availableCars.getValue().charAt(0)));
                    int employeeId = Integer.parseInt(String.valueOf(availableEmployees.getValue().charAt(0)));

                    HelloApplication.db.repairCar(carId, reason, employeeId);

                    alert.setContentText("Samochód " + car + " został odesłany do naprawy przez pracownika " + employee + ", z powodu: " + reason + ".");
                }
            }
            alert.showAndWait();
        });
    }

    public void switchToCarRepairReturnScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("car-repair-return.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

        List<String> availableCars = HelloApplication.db.getRepairedCars();

        availableRepairedCars.getItems().addAll(availableCars);

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToMenuScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        returnRepairBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            try {
                if(availableRepairedCars.getValue().equals("")) {
                    alert.setContentText("Nie wybrano auta do odebrania z naprawy!");
                } else {
                    int carId = Integer.parseInt(String.valueOf(availableRepairedCars.getValue().charAt(0)));
                    String carname = availableRepairedCars.getValue().split(" ")[1];
                    HelloApplication.db.takeRepairedCar(carId);
                    alert.setContentText("Samochód " + carname + " został odebrany z naprawy.");
                }
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
            }
            alert.showAndWait();
        });
    }

    public void switchToStatisticsScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("statistics.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToMenuScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        feesSumBtn.setOnAction(e -> {
            try {
                switchToPaymentStatisticsScene();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        brokenCarsBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            String result = HelloApplication.db.getMostRepairedCar();
            if(result.equals("")) {
                alert.setContentText("Nie ma w bazie danych o naprawianych autach!");
            } else {
                alert.setContentText(result);
            }
            alert.showAndWait();
        });
        nonperishableCarsBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            String result = HelloApplication.db.getNonperishableCars();
            if(result.equals("")) {
                alert.setContentText("Nie ma w bazie danych o nienaprawianych autach!");
            } else {
                alert.setContentText("Auta, które nie były w naprawie to:\n" + result);
            }
            alert.showAndWait();
        });
        mostRentedCarBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            String result = HelloApplication.db.getMostRentedCar();
            if(result.equals("")) {
                alert.setContentText("Nie ma w bazie danych o wypożyczonych autach!");
            } else {
                alert.setContentText(result);
            }
            alert.showAndWait();
        });
        longestRentedCarBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            String result = HelloApplication.db.getLongestRentedCar();
            if(result.equals("")) {
                alert.setContentText("Nie ma w bazie danych o wypożyczonych autach!");
            } else {
                alert.setContentText(result);
            }
            alert.showAndWait();
        });
    }


    public void switchToPaymentStatisticsScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("payment-stats.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToStatisticsScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        showStatsBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            if(dateFrom.getValue() == null || dateTo.getValue() == null) {
                alert.setContentText("Nie podano wszystkich dat!");
            }
        });
    }

    public void switchToProjectDescriptionScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("project-description.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();

//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToMenuScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void switchToAddClientScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("add-client.fxml"));
        loader.setController(this);
        root = loader.load();
        scene = new Scene(root, 640, 400);
        stage.setScene(scene);
        stage.show();
        clientNIP.setEditable(false);

        String[] clientTypes = {"prywatny", "firma"};
        clientType.getItems().addAll(clientTypes);


//        event listeners in rentMenu
        backBtn.setOnAction(e -> {
            try {
                switchToCarRentScene();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        clientType.setOnAction(e -> {
            if(clientType.getValue().equals("prywatny")) {
                clientNIP.setText("");
                clientNIP.setEditable(false);
            } else {
                clientNIP.setEditable(true);
            }
        });
        clientAddBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.setTitle("Car rental");
            if(clientType.getValue() == null) {
                alert.setContentText("Wybierz typ klienta i wpisz dane! Dodawanie nie powiodło się...");
            } else if(clientType.getValue().equals("prywatny")) {
                if(!clientName.getText().equals("")) {
                    String type = "prywatny";
                    String name = clientName.getText();
                    HelloApplication.db.addClient(name, type, 0);
                    alert.setContentText("Dodano klienta " + name + "!");
                } else {
                    alert.setContentText("Brak nazwy klienta! Dodawanie nie powiodło się...");
                }
            } else if(clientType.getValue().equals("firma")) {
                if(!clientName.getText().equals("") && clientNIP.getText().length() == 10) {
                    String type = "firma";
                    String name = clientName.getText();
                    int NIP = Integer.parseInt(clientNIP.getText());
                    HelloApplication.db.addClient(name, type, NIP);
                    alert.setContentText("Dodano klienta " + name + "!");
                } else {
                    alert.setContentText("Brak nazwy klienta lub błędny NIP firmy! Dodawanie nie powiodło się...");
                }
            }
            alert.showAndWait();
        });
    }

        public void changeStatusText(Connection conn) {
        if(conn != null) {
            statusText.setText("Connected with database!");
        } else {
            statusText.setText("Connecting with database...");
        }
    }
}