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
import model.OrderDetails;
import view.tm.OrderDetailsTm;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TotalOrdersController implements Initializable {
    public TableView<OrderDetailsTm> tblNinjaMaleOrders;
    public TableColumn colNinjaMaleNames;
    public TableColumn colNinjaMaleBreakfast;
    public TableColumn colNinjaMaleLunch;
    public TableColumn colNinjaMaleDinner;

    public TableView<OrderDetailsTm> tblNinjaFemaleOrders;
    public TableColumn colNinjaFemaleNames;
    public TableColumn colNinjaFemaleBreakfast;
    public TableColumn colNinjaFemaleLunch;
    public TableColumn colNinjaFemaleDeinner;

    public TableView<OrderDetailsTm> tblDentaMaleOrders;
    public TableColumn colDentaMaleNames;
    public TableColumn colDentaMaleBreakfast;
    public TableColumn colDentaMaleLunch;
    public TableColumn colDentaMaleDinner;

    public TableView<OrderDetailsTm> tblDentaFemaleOrders;
    public TableColumn colDentaFemaleNames;
    public TableColumn colDentaFemaleBreakfast;
    public TableColumn colDentaFemaleLunch;
    public TableColumn colDentaFemaleDinner;
    public Label lblNinjaMaleOrderCount;
    public Label lblNinjaFemaleOrdersCount;
    public Label lblDentamaleordersCount;
    public Label lblDentaFemaleOrdersCount;


    int tempBreakfast;
    int tempLunch;
    int tempDinner;
    String tempCustomername;
    String tempCustomerCid;
    String orderID;

    ObservableList<OrderDetailsTm> obList1 = FXCollections.observableArrayList();
    ObservableList<OrderDetailsTm> obList2 = FXCollections.observableArrayList();
    ObservableList<OrderDetailsTm> obList3 = FXCollections.observableArrayList();
    ObservableList<OrderDetailsTm> obList4 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colNinjaMaleNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colNinjaMaleBreakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            colNinjaMaleLunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
            colNinjaMaleDinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            loadNInjaMaleTbl(getNinjaMale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //========================================
        try {
            colNinjaFemaleNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colNinjaFemaleBreakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            colNinjaFemaleLunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
            colNinjaFemaleDeinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            loadNInjaFemaleTbl(getNinjaFemale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //==========================================
        try {
            colDentaMaleNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colDentaMaleBreakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            colDentaMaleLunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
            colDentaMaleDinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            loadDentaMaleTbl(getDentaMale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //==========================================
        try {
            colDentaFemaleNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colDentaFemaleBreakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            colDentaFemaleLunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
            colDentaFemaleDinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            loadDentaFemaleTbl(getDentaFemale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        lblNinjaMaleOrderCount.setText(String.valueOf(tblNinjaMaleOrders.getItems().size()));
        lblNinjaFemaleOrdersCount.setText(String.valueOf(tblNinjaFemaleOrders.getItems().size()));
        lblDentamaleordersCount.setText(String.valueOf(tblDentaMaleOrders.getItems().size()));
        lblDentaFemaleOrdersCount.setText(String.valueOf(tblDentaFemaleOrders.getItems().size()));
    }

    //======================================================first table==========================================================================
    private void loadNInjaMaleTbl(ArrayList<OrderDetails> ninjaMale) {
        ninjaMale.forEach(e -> {
            try {
                ArrayList<Customer> customers = loadNInjaMalenames(getNinjaMaleOrders(e.getOid()));
                customers.forEach(c -> {
                    //System.out.println(customers);
                    tempCustomername = c.getCustomerName();
                    try {
                        ArrayList<OrderDetails> orderDetails = loadNInjaMaleOrderCount(getNinjaMaleOrderIds(c.getCid()));
                        //System.out.println(orderDetails);
                        orderDetails.forEach(b -> {
                            tempBreakfast = b.getBreakfast();
                            tempLunch = b.getLunch();
                            tempDinner = b.getDinner();
                        });
                        obList1.add(new OrderDetailsTm(tempCustomername, tempBreakfast, tempLunch, tempDinner));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();

            }

        });

        //System.out.println(obList1);
        tblNinjaMaleOrders.setItems(obList1);
    }

    private ArrayList<OrderDetails> loadNInjaMaleOrderCount(ArrayList<Order> ninjaMaleOrderIds) throws SQLException, ClassNotFoundException {

        ninjaMaleOrderIds.forEach(e -> {
            orderID = e.getOid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail` WHERE oid='" + orderID + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    private ArrayList<Order> getNinjaMaleOrderIds(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE cid='" + cid + "'");
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

    private ArrayList<Customer> loadNInjaMalenames(ArrayList<Order> ninjaMaleOrders) throws SQLException, ClassNotFoundException {
        String factoryName = "NINJA";
        String gender = "Male";
        ninjaMaleOrders.forEach(c -> {
            tempCustomerCid = c.getCid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where cid='" + tempCustomerCid + "' AND gender='" + gender + "' AND factory='" + factoryName + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
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
        //System.out.println(customers);
        return customers;

    }

    private ArrayList<Order> getNinjaMaleOrders(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where oid='" + oid + "'");
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

    private ArrayList<OrderDetails> getNinjaMale() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail`");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    //======================================================second table==========================================================================
    private void loadNInjaFemaleTbl(ArrayList<OrderDetails> ninjaMale) {
        ninjaMale.forEach(e -> {
            try {
                ArrayList<Customer> customers = loadNInjaFemalenames(getNinjaFemaleOrders(e.getOid()));
                customers.forEach(c -> {
                    //System.out.println(customers);
                    tempCustomername = c.getCustomerName();
                    try {
                        ArrayList<OrderDetails> orderDetails = loadNInjaFemaleOrderCount(getNinjaFemaleOrderIds(c.getCid()));
                        //System.out.println(orderDetails);
                        orderDetails.forEach(b -> {
                            tempBreakfast = b.getBreakfast();
                            tempLunch = b.getLunch();
                            tempDinner = b.getDinner();
                        });
                        obList2.add(new OrderDetailsTm(tempCustomername, tempBreakfast, tempLunch, tempDinner));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();

            }

        });

        //System.out.println(obList1);
        tblNinjaFemaleOrders.setItems(obList2);
    }

    private ArrayList<OrderDetails> loadNInjaFemaleOrderCount(ArrayList<Order> ninjaMaleOrderIds) throws SQLException, ClassNotFoundException {

        ninjaMaleOrderIds.forEach(e -> {
            orderID = e.getOid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail` WHERE oid='" + orderID + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    private ArrayList<Order> getNinjaFemaleOrderIds(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE cid='" + cid + "'");
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

    private ArrayList<Customer> loadNInjaFemalenames(ArrayList<Order> ninjaMaleOrders) throws SQLException, ClassNotFoundException {
        String factoryName = "NINJA";
        String gender = "Female";
        ninjaMaleOrders.forEach(c -> {
            tempCustomerCid = c.getCid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where cid='" + tempCustomerCid + "' AND gender='" + gender + "' AND factory='" + factoryName + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
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
        //System.out.println(customers);
        return customers;

    }

    private ArrayList<Order> getNinjaFemaleOrders(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where oid='" + oid + "'");
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

    private ArrayList<OrderDetails> getNinjaFemale() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail`");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    //======================================================third table==========================================================================
    private void loadDentaMaleTbl(ArrayList<OrderDetails> ninjaMale) {
        ninjaMale.forEach(e -> {
            try {
                ArrayList<Customer> customers = loadDentaMalenames(getDentaMmaleOrders(e.getOid()));
                customers.forEach(c -> {
                    //System.out.println(customers);
                    tempCustomername = c.getCustomerName();
                    try {
                        ArrayList<OrderDetails> orderDetails = loadDentaMaleOrderCount(getDentaMaleOrderIds(c.getCid()));
                        //System.out.println(orderDetails);
                        orderDetails.forEach(b -> {
                            tempBreakfast = b.getBreakfast();
                            tempLunch = b.getLunch();
                            tempDinner = b.getDinner();
                        });
                        obList3.add(new OrderDetailsTm(tempCustomername, tempBreakfast, tempLunch, tempDinner));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();

            }

        });

        //System.out.println(obList1);
        tblDentaMaleOrders.setItems(obList3);
    }

    private ArrayList<OrderDetails> loadDentaMaleOrderCount(ArrayList<Order> ninjaMaleOrderIds) throws SQLException, ClassNotFoundException {

        ninjaMaleOrderIds.forEach(e -> {
            orderID = e.getOid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail` WHERE oid='" + orderID + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    private ArrayList<Order> getDentaMaleOrderIds(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE cid='" + cid + "'");
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

    private ArrayList<Customer> loadDentaMalenames(ArrayList<Order> ninjaMaleOrders) throws SQLException, ClassNotFoundException {
        String factoryName = "DENTA";
        String gender = "Male";
        ninjaMaleOrders.forEach(c -> {
            tempCustomerCid = c.getCid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where cid='" + tempCustomerCid + "' AND gender='" + gender + "' AND factory='" + factoryName + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
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
        //System.out.println(customers);
        return customers;

    }

    private ArrayList<Order> getDentaMmaleOrders(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where oid='" + oid + "'");
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

    private ArrayList<OrderDetails> getDentaMale() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail`");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    //======================================================fourth table==========================================================================
    private void loadDentaFemaleTbl(ArrayList<OrderDetails> ninjaMale) {
        ninjaMale.forEach(e -> {
            try {
                ArrayList<Customer> customers = loadDentaFemalenames(getDentaFemaleOrders(e.getOid()));
                customers.forEach(c -> {
                    //System.out.println(customers);
                    tempCustomername = c.getCustomerName();
                    try {
                        ArrayList<OrderDetails> orderDetails = loadDentaFemaleOrderCount(getDentaFemaleOrderIds(c.getCid()));
                        //System.out.println(orderDetails);
                        orderDetails.forEach(b -> {
                            tempBreakfast = b.getBreakfast();
                            tempLunch = b.getLunch();
                            tempDinner = b.getDinner();
                        });
                        obList4.add(new OrderDetailsTm(tempCustomername, tempBreakfast, tempLunch, tempDinner));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();

            }

        });

        //System.out.println(obList1);
        tblDentaFemaleOrders.setItems(obList4);
    }

    private ArrayList<OrderDetails> loadDentaFemaleOrderCount(ArrayList<Order> ninjaMaleOrderIds) throws SQLException, ClassNotFoundException {

        ninjaMaleOrderIds.forEach(e -> {
            orderID = e.getOid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail` WHERE oid='" + orderID + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

    private ArrayList<Order> getDentaFemaleOrderIds(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE cid='" + cid + "'");
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

    private ArrayList<Customer> loadDentaFemalenames(ArrayList<Order> ninjaMaleOrders) throws SQLException, ClassNotFoundException {
        String factoryName = "DENTA";
        String gender = "Female";
        ninjaMaleOrders.forEach(c -> {
            tempCustomerCid = c.getCid();
        });
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where cid='" + tempCustomerCid + "' AND gender='" + gender + "' AND factory='" + factoryName + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
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
        //System.out.println(customers);
        return customers;

    }

    private ArrayList<Order> getDentaFemaleOrders(String oid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where oid='" + oid + "'");
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

    private ArrayList<OrderDetails> getDentaFemale() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM `Order Detail`");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<OrderDetails> orderDetails = new ArrayList<>();

        while (rst.next()) {
            orderDetails.add(new OrderDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getInt(4),
                    rst.getInt(5),
                    rst.getDouble(6),
                    rst.getString(7)
            ));
        }
        return orderDetails;
    }

}
