package com.example.carrental;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Date;

public class Database {
    private String userName;
    private String password;
    private String serverName;
    private String portNumber;
    private String dbName;
    private static Connection conn = null;

    public Database(String userName, String password, String serverName, String portNumber, String dbName) {
        this.userName = userName;
        this.password = password;
        this.serverName = serverName;
        this.portNumber = portNumber;
        this.dbName = dbName;
    }

    public Connection getConnection() throws SQLException {
        System.out.println("Connecting to database " + this.dbName + "...");
        conn = null;
        conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=carrent;encrypt=true;trustServerCertificate=true;", "sa", "sa");
        System.out.println("Connected to database");
        System.out.println();
        return conn;
    }

    public List<String> getAvailableCars() {
        List<String> availableCars = new ArrayList<String>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa, moc_silnika, klasa_samochodu FROM samochody WHERE status=0");
            while(result.next()){
                String car = result.getInt("id") + ". " +  result.getString("nazwa") + ", " + result.getInt("moc_silnika") + "KM" + ", " + result.getString("klasa_samochodu");
                availableCars.add(car);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return availableCars;
    }

    public List<String> getClients() {
        List<String> clients = new ArrayList<String>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM klienci");
            while(result.next()){
                String client = result.getInt("id") + ". " +  result.getString("nazwa");
                clients.add(client);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return clients;
    }

    public int getCarPrice(int carId) {
        int carPrice = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT cena FROM samochody WHERE id=" + carId);
            while(result.next()){
                carPrice = result.getInt("cena");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return carPrice;
    }

    public void rentCar() {

    }

    public void repairCar(int carId, String reason, int employeeId) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("INSERT INTO naprawy VALUES ('" + carId + "', '" + reason + "', '" + employeeId + "');UPDATE samochody SET status=2 WHERE id=" + carId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addClient(String name, String type, int NIP) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = null;
            if(NIP == 0) {
               stmt.executeQuery("INSERT INTO klienci(rodzaj, nazwa) VALUES ('" + type + "', '" + name + "')");
            } else {
                stmt.executeQuery("INSERT INTO klienci VALUES ('" + type + "', '" + name + "', '" + NIP + "')");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<String> getEmployees() {
        List<String> employees = new ArrayList<String>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwisko FROM pracownicy");
            while(result.next()){
                String employee = result.getInt("id") + ". " +  result.getString("nazwisko");
                employees.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employees;
    }

    public List<String> getRepairedCars() {
        List<String> cars = new ArrayList<String>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM samochody WHERE status=2");
            while(result.next()){
                String car = result.getInt("id") + ". " +  result.getString("nazwa");
                cars.add(car);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cars;
    }

    public void takeRepairedCar(int carId) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery("UPDATE samochody SET status=0 WHERE id=" + carId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getMostRepairedCar() {
        String car = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM samochody WHERE id = (SELECT TOP 1 id_samochodu FROM naprawy GROUP BY id_samochodu ORDER BY COUNT(id_samochodu) desc);");
            while(result.next()){
                car = "Najczęściej psujący się samochód to " + result.getString("nazwa") + " o id: " + result.getInt("id") + ".";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return car;
    }

    public String getNonperishableCars() {
        String cars = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM samochody WHERE id NOT IN (SELECT id_samochodu FROM naprawy);");
            while(result.next()){
                cars += result.getInt("id") + ". " + result.getString("nazwa") + "\n";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cars;
    }

    public String getMostRentedCar() {
        String car = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM samochody WHERE id = (SELECT TOP 1 id_samochodu FROM datywynajmu GROUP BY id_samochodu ORDER BY COUNT(id_samochodu) desc);");
            while(result.next()){
                car = "Najczęściej wypożyczany samochód to " + result.getString("nazwa") + " o id: " + result.getInt("id") + ".";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return car;
    }

    public String getLongestRentedCar() {
        String car = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM samochody WHERE id = (SELECT TOP 1 id_samochodu AS days FROM datywynajmu GROUP BY id_samochodu ORDER BY SUM(DATEDIFF(day, data_start, data_koniec)) desc);");
            while(result.next()){
                car = "Najdłużej wypożyczany samochód to " + result.getString("nazwa") + " o id: " + result.getInt("id") + ".";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return car;
    }

    public void addCarRentBilling(int carId, int clientId) {
        String clientType = "";
        String billingType;
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT rodzaj FROM klienci WHERE id=" +  clientId);
            while (result.next()) {
                clientType = result.getString("rodzaj");
            }
            billingType = clientType.equals("firma") ? "faktura" : "paragon";
            Random ran = new Random();
            int r = ran.nextInt(10000 - 1 + 1) + 1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
            Date datee = new Date();
            java.sql.Timestamp date = new Timestamp(new Date().getTime());
            String rentDate = formatter.format(date);
            stmt.execute("INSERT INTO rachunki (nr_rachunku, rodzaj, klient, kwota, id_samochodu, id_pracownika) VALUES (" + r + ", '" + billingType + "', " + clientId + ", " + 0 + ", " + carId + ", NULL)");
            stmt.execute("UPDATE samochody SET status=1 WHERE id=" + carId);
            stmt.execute("INSERT INTO datywynajmu (id_wynajmu, id_samochodu, data_start, data_koniec) VALUES (" + r + ", " + carId + ", '" + date + "', '')");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<String> getRentedCars() throws SQLException {
        List<String> cars = new ArrayList<String>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT id, nazwa FROM samochody WHERE status=1");
            while (result.next()) {
                String car = result.getInt("id") + ". " + result.getString("nazwa");
                cars.add(car);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return cars;
    }

    public String getRentedCarClient(int carId) {
        String client = "";
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery("SELECT nazwa FROM klienci WHERE id = (SELECT klient FROM rachunki WHERE id_samochodu=" + carId + " AND kwota=0);");
            while (result.next()) {
                client = result.getString("nazwa");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return client;
    }

    public String returnRentedCar(int carId) {
        String rentInfo = "";
        try {
            java.sql.Timestamp returnDate = new Timestamp(new Date().getTime());
            Statement stmt = conn.createStatement();
            int days = 0;
            int price = 0;
            int finalPrice;
            int rentId = 0;
            stmt.execute("UPDATE datywynajmu SET data_koniec='" + returnDate +  "' WHERE data_start > data_koniec AND id_samochodu=" + carId);
            ResultSet result = stmt.executeQuery("SELECT SUM(DATEDIFF(day, data_start, data_koniec)) as dni, id_wynajmu FROM datywynajmu WHERE data_koniec='" +  returnDate + "' AND id_samochodu=" + carId + " GROUP BY id_wynajmu");
            while (result.next()) {
//                days = result.getInt("dni");
                rentId = result.getInt("id_wynajmu");
            }
            result = stmt.executeQuery("SELECT cena FROM samochody WHERE id=" + carId);
            while (result.next()) {
                price = result.getInt("cena");
            }
            finalPrice = days == 0 ? price : (price * days);
            stmt.execute("UPDATE samochody SET status=0 WHERE id=" + carId);
            stmt.execute("UPDATE rachunki SET kwota=" + finalPrice + " WHERE nr_rachunku=" + rentId);

            rentInfo = "ilość dni: " + days + ", kwota: " + finalPrice;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rentInfo;
    }

    public Connection getConnectionStatus() {
        return conn;
    }

    public String getDbName() {
        return dbName;
    }
}
