package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Order;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TotalRevenueController implements Initializable {
    public TableView<Order> tblOrder;
    public TableColumn colOrderID;
    public TableColumn colCustomer;
    public TableColumn colFoodType;
    public TableColumn colMealTime;
    public TableColumn colDate;
    public TableColumn colAmount;
    public DatePicker calOrderHistory;
    public Label lblTotalAmount;
    String tempCustomerName;
    ObservableList<Order> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colOrderID.setCellValueFactory(new PropertyValueFactory<>("oid"));
            colCustomer.setCellValueFactory(new PropertyValueFactory<>("cid"));
            colFoodType.setCellValueFactory(new PropertyValueFactory<>("mealType"));
            colMealTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
            loadOrderTbl(getAllOrders());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadTotalAmount();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadTotalAmount() throws SQLException, ClassNotFoundException {
        int orderCount = 0;
        double amount=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM Orders ").executeQuery();
        if (rst1.next()) {
            amount = rst1.getInt(1);
            lblTotalAmount.setText(String.valueOf(amount)+" /=");
        }
    }

    private void loadOrderTbl(ArrayList<Order> allOrders) {
        allOrders.forEach(e -> {
            try {
                obList.add(new Order(e.getOid(), e.getDate(), e.getTime(), e.getMealType(), e.getAmount(),getCustomername(e.getCid()), e.getTransportNo()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        tblOrder.setItems(obList);
    }

    private String getCustomername(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where cid='" + cid + "'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Customer> customers = new ArrayList<>();

        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        customers.forEach(e -> {
            tempCustomerName = e.getCustomerName();
        });
        return tempCustomerName;
    }

    private ArrayList<Order> getAllOrders() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Order> orders = new ArrayList<>();

        while (rst.next()) {
            orders.add(new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return orders;
    }

    public void searchordersOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        LocalDate date = calOrderHistory.getValue();
        System.out.println(date);
        try {
            loadHistory(getHistoryOrders(date));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadDailyAmount(calOrderHistory.getValue());
    }

    private void loadDailyAmount(LocalDate value) throws SQLException, ClassNotFoundException {
        int orderCount = 0;
        double amount=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM Orders Where date='" + value + "'").executeQuery();
        if (rst1.next()) {
            amount = rst1.getInt(1);
            lblTotalAmount.setText(String.valueOf(amount)+" /=");
        }
    }

    private void loadHistory(ArrayList<Order> historyOrders) {
        tblOrder.getItems().clear();
        historyOrders.forEach(e -> {
            try {
                obList.add(new Order(e.getOid(), e.getDate(), e.getTime(), e.getMealType(), e.getAmount(),getCustomername(e.getCid()), e.getTransportNo()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        System.out.println(obList);

        tblOrder.setItems(obList);
    }


    private ArrayList<Order> getHistoryOrders(LocalDate date) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where date='" + date + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Order> orders = new ArrayList<>();

        while (rst.next()) {
            orders.add(new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return orders;
    }


}
