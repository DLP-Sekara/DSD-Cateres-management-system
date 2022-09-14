package controller;

import db.DbConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Supplier;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class DashBoardPreviewFormController implements Initializable {
    public AnchorPane dashBoardPreviewPagePane;
    public Label customerCount;
    public Label lblDate;
    public Label lblTime;
    public Label lblOrderPrecentage;
    public Label lblStoresPrecentage;
    public Label lblCustomerPrecentage;
    public Label lblRevenuePrecentage;
    public Label lblName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadDateAndTime();
        try {
            loadOrderPercentage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadCustomerPercentage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadStoresPercentage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadRevenuePercentage();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadDashBoardContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDashBoardContent() throws IOException {
        loadUi("TotalOrders");
    }

    private void loadRevenuePercentage() throws SQLException, ClassNotFoundException {
        int storeCount=0;
        double percentage=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM Orders ").executeQuery();
        if (rst1.next()) {
            storeCount = rst1.getInt(1);
            System.out.println(storeCount);
        }
        percentage=storeCount;
        lblRevenuePrecentage.setText(String.valueOf(percentage));
    }

    private void loadStoresPercentage() throws SQLException, ClassNotFoundException {
        int storeCount=0;
        int percentage=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(*) FROM Ingredient ").executeQuery();
        if (rst1.next()) {
            storeCount = rst1.getInt(1);
            System.out.println(storeCount);
        }
        percentage=storeCount;
        lblStoresPrecentage.setText(String.valueOf(percentage));
    }

    private void loadCustomerPercentage() throws SQLException, ClassNotFoundException {
        int customerCount=0;
        int percentage=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(*) FROM Customer ").executeQuery();
        if (rst1.next()) {
            customerCount = rst1.getInt(1);
            System.out.println(customerCount);
        }
        percentage=customerCount;
        lblCustomerPrecentage.setText(String.valueOf(percentage));
    }

    private void loadOrderPercentage() throws SQLException, ClassNotFoundException {
        int customerCount=0;
        int orderCount = 0;
        int percentage=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(*) FROM Customer ").executeQuery();
        if (rst1.next()) {
            customerCount = rst1.getInt(1);
            System.out.println(customerCount);
        }

        ResultSet rst2 = DbConnection.getInstance().getConnection().prepareStatement("SELECT COUNT(*) FROM Orders ").executeQuery();
        if (rst2.next()) {
            orderCount = rst2.getInt(1);
            System.out.println(orderCount);
        }
        percentage=orderCount;
        System.out.println(percentage);
        lblOrderPrecentage.setText(String.valueOf(percentage));
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
        lblDate.setText(f.format(date));

        Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(
                    currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void openAllCustomers(MouseEvent mouseEvent) throws IOException {
        loadUi("AllCustomersForm");
    }

    public void openStore(MouseEvent mouseEvent) throws IOException {
        loadUi("StorageContentForm");
    }

    public void openTotalOrders(MouseEvent mouseEvent) throws IOException {
        loadUi("TotalOrders");
    }

    public void openPayments(MouseEvent mouseEvent) throws IOException {
        loadUi("TotalRevenue");
    }

    void loadUi(String filName) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/" + filName + ".fxml"));
        pane = fxmlLoader.load();
        dashBoardPreviewPagePane.getChildren().setAll(pane);

    }


    public void changeNameOnAction(ActionEvent actionEvent) {
        lblName.setText("lahiru");
    }
}
