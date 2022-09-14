package controller;

import db.DbConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import model.Customer;
import model.Meal;
import model.Order;
import model.Transport;
import util.Validation;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

public class OrdersFormController implements Initializable {
    public TableView<Customer> tblCustomer;
    public TableColumn colCustomerName;
    public Button placeOrderButton;
    public TextField txtCustomername;
    public ComboBox<String> cmbFoodType;
    public TextField txtBreakfast;
    public TextField txtDinner;
    public TextField txtLunch;
    public Button updateButton;
    public ComboBox<String> cmbTransport;
    public Label lblBreakfast;
    public Label lblLunch;
    public Label lblDinner;
    public Label lblTotal;
    public ComboBox<String> cmbSelectCompany;
    public TableView<Order> tblOrder;
    public TableColumn colOrderID;
    public TableColumn colCustomer;
    public TableColumn colFoodType;
    public TableColumn colMealTime;
    public TableColumn colDate;
    public TableColumn colAmount;
    public TextField txtSearchCustomer;
    public DatePicker calOrderHistory;
    public Button changePricePlan;
    public Label lblDate;
    public Label lblCustomerCID;
    public Label lblTransportNo;
    public TableColumn colTransportNo;
    public Label lblOrderID;
    public Label lblTempOrderId;
    public Label lblTempCustomername;
    public Button deleteButton;
    public ComboBox<String> cmbTodayMeal;
    public Label lblOrderId;
    public Label lblTime;
    String tempMealType;
    String tempTransport;
    String tempMealTime;
    double tempBreakfastPrice;
    double tempLunchPrice;
    double tempDinnerPrice;
    double amount;
    String tempCustomerName;
    String tempCustomerCID;
    String tempMealNo;

    int tempBreakfastQty;
    int tempLunchQty;
    int tempDinnerQty;

    Object SelectedRowForRemove = -1;
    ObservableList<Order> obList = FXCollections.observableArrayList();
    ObservableList<Customer> CustomerObList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern BreakfastCountPattern = Pattern.compile("^[0-9]{1,8}$");
    Pattern LunchCountPattern = Pattern.compile("^[0-9]{1,8}$");
    Pattern DinnerCountPattern = Pattern.compile("^[0-9]{1,8}$");

    public static List<Customer> searchItems(String value) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();

        PreparedStatement pstm = con.prepareStatement("SELECT * FROM Customer WHERE customerName LIKE '%" + value + "%'");
        ResultSet rs = pstm.executeQuery();
        List<Customer> items = new ArrayList<>();

        while (rs.next()) {
            items.add(new Customer(
                    rs.getString("cid"),
                    rs.getString("customerName"),
                    rs.getString("address"),
                    rs.getString("factory"),
                    rs.getString("contact"),
                    rs.getString("gender"),
                    rs.getString("sid")
            ));
        }

        return items;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        placeOrderButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        storeValidations();

        loadDateAndTime();
        cmbFoodType.getItems().addAll("Veg", "Non veg");
        cmbSelectCompany.getItems().addAll("NINJA", "DENTA");
        cmbSelectCompany.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("NINJA")) {
                try {
                    colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                    loadNinjaCustomers(getNinjaCustomers());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (newValue.equals("DENTA")) {
                try {
                    colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                    loadDentaCustomers(getDentaCustomers());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            loadAllCustomers(getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadMealType();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadTransportType();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtSearchCustomer.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    List<Customer> customers = searchItems(newValue);
                    loadAllCustomers((ArrayList<Customer>) customers);
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                txtCustomername.setText(newValue.getCustomerName());
                lblCustomerCID.setText(newValue.getCid());
                lblCustomerCID.setVisible(false);
                txtCustomername.setDisable(true);
            }
        });
        cmbTodayMeal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadTodayMealBreakfast(getBreakfastprice(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                loadTodayMealLunch(getLunchPrice(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                loadTodayMealDinner(getDinnerPrice(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        cmbTransport.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                loadTransportNo(getTransportNo(newValue));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        try {
            lblTransportNo.setText(getlastTransportno());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        colOrderID.setCellValueFactory(new PropertyValueFactory<>("oid"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("cid"));
        colFoodType.setCellValueFactory(new PropertyValueFactory<>("mealType"));
        colMealTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colTransportNo.setCellValueFactory(new PropertyValueFactory<>("transportNo"));
        try {

            loadOrderTbl(getAllOrders());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblOrder.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                lblTempOrderId.setText(newValue.getOid());
                txtCustomername.setText(newValue.getCid());
                cmbFoodType.getSelectionModel().select(newValue.getMealType());

                try {
                    loadCustomerCid(getCustomerId(newValue.getCid()));
                    lblCustomerCID.setText(tempCustomerCID);
                    lblCustomerCID.setVisible(false);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                lblTotal.setText(String.valueOf(newValue.getAmount()));
                cmbTransport.getSelectionModel().select(newValue.getTransportNo());
                txtCustomername.setDisable(true);
                lblTempOrderId.setVisible(false);
                deleteButton.setDisable(false);

            }
        });


    }

    private void loadCustomerCid(ArrayList<Customer> customerId) {
        customerId.forEach(e -> {
            tempCustomerCID = e.getCid();
        });
    }

    private ArrayList<Customer> getCustomerId(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where customerName='" + cid + "'");
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
        return customers;
    }

    private void loadOrderTbl(ArrayList<Order> allOrders) {

        allOrders.forEach(e -> {
            try {
                loadCustomerNames(getCustomerNames(e.getCid()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            obList.add(new Order(e.getOid(), e.getDate(), e.getTime(), e.getMealType(), e.getAmount(), tempCustomerName, e.getTransportNo()));
        });
        tblOrder.setItems(obList);
    }

    private void loadCustomerNames(ArrayList<Customer> customerNames) throws SQLException, ClassNotFoundException {
        customerNames.forEach(e -> {
            tempCustomerName = e.getCustomerName();
        });
    }

    private ArrayList<Customer> getCustomerNames(String cid) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where cid='" + cid + "'");
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
        return customers;
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

    private void loadTransportNo(ArrayList<Transport> transportNo) {
        transportNo.forEach(e -> {
            lblTransportNo.setText(String.valueOf(e.getTransportNo()));
            //System.out.println(e.getTransportNo());
        });
    }

    private ArrayList<Transport> getTransportNo(String newValue) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("Select * FROM Transport WHERE transportType='" + newValue + "'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Transport> transports = new ArrayList<>();

        while (rst.next()) {
            transports.add(new Transport(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6)
            ));
        }
        return transports;
    }

    private void storeValidations() {
        map.put(txtBreakfast, BreakfastCountPattern);
        map.put(txtLunch, LunchCountPattern);
        map.put(txtDinner, DinnerCountPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateOrderCount(map, placeOrderButton, updateButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    private void loadTodayMealBreakfast(ArrayList<Meal> breakfast) {
        breakfast.forEach(e -> {
            lblBreakfast.setText(String.valueOf(e.getCost()));
            tempBreakfastPrice = e.getCost();
        });
    }

    private void loadTodayMealLunch(ArrayList<Meal> lunchPrice) {
        lunchPrice.forEach(e -> {
            lblLunch.setText(String.valueOf(e.getCost()));
            tempLunchPrice = e.getCost();
        });
    }

    private void loadTodayMealDinner(ArrayList<Meal> dinnerPrice) {
        dinnerPrice.forEach(e -> {
            lblDinner.setText(String.valueOf(e.getCost()));
            tempDinnerPrice = e.getCost();
        });
    }

    private ArrayList<Meal> getBreakfastprice(String newValue) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("Select * FROM Meal WHERE mealName='" + newValue + "' AND mealType='" + "Breakfast" + "'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Meal> meals = new ArrayList<>();

        while (rst.next()) {
            meals.add(new Meal(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            ));
        }
        return meals;
    }

    private ArrayList<Meal> getLunchPrice(String newValue) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("Select * FROM Meal WHERE mealName='" + newValue + "' AND mealType='" + "Lunch" + "'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Meal> meals = new ArrayList<>();

        while (rst.next()) {
            meals.add(new Meal(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            ));
        }
        return meals;
    }

    private ArrayList<Meal> getDinnerPrice(String newValue) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("Select * FROM Meal WHERE mealName='" + newValue + "' AND mealType='" + "Dinner" + "'");
        ResultSet rst = stm.executeQuery();
        ArrayList<Meal> meals = new ArrayList<>();

        while (rst.next()) {
            meals.add(new Meal(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            ));
        }
        return meals;
    }

    private void loadTransportType() throws SQLException, ClassNotFoundException {
        List<String> transportTypes = getTransportTypes();
        cmbTransport.getItems().addAll(transportTypes);
    }

    private List<String> getTransportTypes() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Transport").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(2)
            );
        }
        return ids;
    }

    private void loadMealType() throws SQLException, ClassNotFoundException {
        List<String> mealNames = getDriverIds("Breakfast");

        cmbTodayMeal.getItems().addAll(mealNames);
    }

    private List<String> getDriverIds(String text) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Meal WHERE mealType='" + text + "'").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(2)
            );
        }
        return ids;
    }

    private void loadDentaCustomers(ArrayList<Customer> dentaCustomers) {
        tblCustomer.getItems().clear();
        dentaCustomers.forEach(e -> {
            CustomerObList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomer.setItems(CustomerObList);
    }

    private void loadNinjaCustomers(ArrayList<Customer> ninjaCustomers) {
        tblCustomer.getItems().clear();
        ninjaCustomers.forEach(e -> {
            CustomerObList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomer.setItems(CustomerObList);
    }

    private void loadAllCustomers(ArrayList<Customer> AllCustomers) {
        tblCustomer.getItems().clear();
        AllCustomers.forEach(e -> {
            CustomerObList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomer.setItems(CustomerObList);
    }

    private ArrayList<Customer> getNinjaCustomers() throws SQLException, ClassNotFoundException {
        String temp1 = "NINJA";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "'");
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
        return customers;
    }

    private ArrayList<Customer> getDentaCustomers() throws SQLException, ClassNotFoundException {
        String temp1 = "DENTA";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "'");
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
        return customers;
    }

    private ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer ");
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
        return customers;
    }

    private void loadDateAndTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
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

    public void placeOrderonAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String mealtype = cmbTodayMeal.getSelectionModel().getSelectedItem();
        tempMealType = cmbFoodType.getSelectionModel().getSelectedItem();
        tempTransport = cmbTransport.getSelectionModel().getSelectedItem();
        int breakfast = Integer.parseInt(txtBreakfast.getText());
        int lunch = Integer.parseInt(txtLunch.getText());
        int Dinner = Integer.parseInt(txtDinner.getText());

        if (breakfast != 0 & lunch == 0 & Dinner == 0) {
            tempMealTime = "B";
        } else if (breakfast == 0 & lunch != 0 & Dinner == 0) {
            tempMealTime = "L";
        } else if (breakfast == 0 & lunch == 0 & Dinner != 0) {
            tempMealTime = "D";
        } else if (breakfast != 0 & lunch != 0 & Dinner == 0) {
            tempMealTime = "B / L";
        } else if (breakfast != 0 & lunch == 0 & Dinner != 0) {
            tempMealTime = "B / D";
        } else if (breakfast == 0 & lunch != 0 & Dinner != 0) {
            tempMealTime = "L / D";
        } else if (breakfast != 0 & lunch != 0 & Dinner != 0) {
            tempMealTime = "All";
        }
        //System.out.println(tempMealTime);

        tempBreakfastQty = Integer.parseInt(txtBreakfast.getText());
        tempLunchQty = Integer.parseInt(txtLunch.getText());
        tempDinnerQty = Integer.parseInt(txtDinner.getText());
        amount = tempBreakfastQty * tempBreakfastPrice + tempLunchQty * tempLunchPrice + tempDinnerQty * tempDinnerPrice;
        lblTotal.setText(String.valueOf(amount));
        if (mealtype != null) {
            String sid = getLastOrderId();
            String finalId = "O-001";
            if (sid != null) {
                String[] splitString = sid.split("-");
                int id = Integer.parseInt(splitString[1]);
                id++;

                if (id < 10) {
                    finalId = "O-00" + id;
                } else if (id < 100) {
                    finalId = "O-0" + id;
                } else {
                    finalId = "O-" + id;
                }
                lblTempOrderId.setText(finalId);
                lblTempOrderId.setVisible(false);
            } else {
                lblTempOrderId.setText(finalId);
                lblTempOrderId.setVisible(false);
            }
            Order o1 = new Order(finalId, lblDate.getText(), tempMealTime, tempMealType, amount, lblCustomerCID.getText(), lblTransportNo.getText());
            if (checkName(lblCustomerCID.getText())) {
                if (checkDate(lblDate.getText(),lblCustomerCID.getText())) {
                    new Alert(Alert.AlertType.WARNING, "customer already exists..").show();
                }else{
                    makeOrder(o1);
                }
            } else {
                makeOrder(o1);

            }
        } else {
            //customerAddBtn.setDisable(true);
            new Alert(Alert.AlertType.WARNING, "Select Meal..").show();
        }
    }

    private void makeOrder(Order o1) throws SQLException, ClassNotFoundException {
        if (saveOrder(o1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblOrder.refresh();
                tblOrder.getItems().clear();
                loadOrderTbl(getAllOrders());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again..").show();
        }
    }

    private boolean checkDate(String text, String lblCustomerCIDText) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where date=? AND cid=?");
        stm.setObject(1, text);
        stm.setObject(2, lblCustomerCIDText);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7)
            );
            return true;
        } else {
            return false;
        }
    }

    private boolean saveOrder(Order o1) throws SQLException, ClassNotFoundException {
        Connection con = null;
        try {
            con = DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            String query = "INSERT INTO Orders VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stm = con.prepareStatement(query);
            stm.setObject(1, o1.getOid());
            stm.setObject(2, o1.getDate());
            stm.setObject(3, o1.getTime());
            stm.setObject(4, o1.getMealType());
            stm.setObject(5, o1.getAmount());
            stm.setObject(6, o1.getCid());
            stm.setObject(7, o1.getTransportNo());
            System.out.println("end");
            if (stm.executeUpdate() > 0) {
                if (saveOrderDetail(o1.getOid(), o1.getAmount(), o1.getTime(), o1.getMealType())) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }
            } else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    private boolean saveOrderDetail(String oid, double amount, String time, String mealType) throws SQLException, ClassNotFoundException {
        loadMealNo(getMealNo(mealType));
        PreparedStatement stm = DbConnection.getInstance().getConnection().
                prepareStatement("INSERT INTO `Order Detail` VALUES(?,?,?,?,?,?,?)");
        stm.setObject(1, tempMealNo);
        stm.setObject(2, oid);
        stm.setObject(3, tempBreakfastQty);
        stm.setObject(4, tempLunchQty);
        stm.setObject(5, tempDinnerQty);
        stm.setObject(6, amount);
        stm.setObject(7, time);
        return stm.executeUpdate() > 0;

    }

    private void loadMealNo(ArrayList<Meal> mealNo) {
        mealNo.forEach(e -> {
            tempMealNo = e.getMealNo();
        });
    }

    private ArrayList<Meal> getMealNo(String mealType) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Meal Where mealName='" + mealType + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Meal> meals = new ArrayList<>();

        while (rst.next()) {
            meals.add(new Meal(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            ));
        }
        return meals;
    }

    private boolean checkName(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE cid=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7)
            );
            return true;
        } else {
            return false;
        }
    }

    private String getLastOrderId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT oid FROM Orders ORDER BY oid DESC LIMIT 1");
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("oid");
        }
        return null;
    }

    private String getlastTransportno() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT transportNo FROM Transport ORDER BY transportNo DESC LIMIT 1");
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("transportNo");
        }
        return null;
    }

    public void UpdateOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String mealtype = cmbTodayMeal.getSelectionModel().getSelectedItem();
        tempMealType = cmbFoodType.getSelectionModel().getSelectedItem();
        tempTransport = cmbTransport.getSelectionModel().getSelectedItem();
        int breakfast = Integer.parseInt(txtBreakfast.getText());
        int lunch = Integer.parseInt(txtLunch.getText());
        int Dinner = Integer.parseInt(txtDinner.getText());

        if (breakfast != 0 & lunch == 0 & Dinner == 0) {
            tempMealTime = "B";
        } else if (breakfast == 0 & lunch != 0 & Dinner == 0) {
            tempMealTime = "L";
        } else if (breakfast == 0 & lunch == 0 & Dinner != 0) {
            tempMealTime = "D";
        } else if (breakfast != 0 & lunch != 0 & Dinner == 0) {
            tempMealTime = "B / L";
        } else if (breakfast != 0 & lunch == 0 & Dinner != 0) {
            tempMealTime = "B / D";
        } else if (breakfast == 0 & lunch != 0 & Dinner != 0) {
            tempMealTime = "L / D";
        } else if (breakfast != 0 & lunch != 0 & Dinner != 0) {
            tempMealTime = "All";
        }
        //System.out.println(tempMealTime);

        tempBreakfastQty = Integer.parseInt(txtBreakfast.getText());
        tempLunchQty = Integer.parseInt(txtLunch.getText());
        tempDinnerQty = Integer.parseInt(txtDinner.getText());
        amount = tempBreakfastQty * tempBreakfastPrice + tempLunchQty * tempLunchPrice + tempDinnerQty * tempDinnerPrice;
        lblTotal.setText(String.valueOf(amount));
        if (mealtype != null) {
            Order o1 = new Order(lblTempOrderId.getText(), lblDate.getText(), tempMealTime, tempMealType, amount, lblCustomerCID.getText(), lblTransportNo.getText());
            if (checkOID(lblTempOrderId.getText())) {
                if (updateOrder(o1)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        this.tblOrder.refresh();
                        tblOrder.getItems().clear();
                        loadOrderTbl(getAllOrders());
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }


            } else {
                new Alert(Alert.AlertType.WARNING, "customer already exists..").show();
            }
        } else {
            //customerAddBtn.setDisable(true);
            new Alert(Alert.AlertType.WARNING, "Select Meal..").show();
        }

    }

    private boolean checkOID(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders WHERE oid=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Order(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6),
                    rst.getString(7)
            );
            return true;
        } else {
            return false;
        }
    }

    private boolean updateOrder(Order o1) throws SQLException, ClassNotFoundException {
        Connection con = null;
        try {
            con=DbConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            PreparedStatement stm = con.prepareStatement("UPDATE Orders SET  time=?, mealType=?,amount=?,transportNo=? Where oid='" + o1.getOid() + "'");
            stm.setObject(1, o1.getTime());
            stm.setObject(2, o1.getMealType());
            stm.setObject(3, o1.getAmount());
            stm.setObject(4, o1.getTransportNo());
            if (stm.executeUpdate() > 0) {
                if(updateOrderDetail(o1.getOid(), o1.getAmount(), o1.getTime(), o1.getMealType())){
                    con.commit();
                    return true;
                }else{
                    con.rollback();
                    return false;
                }
            } else {
                con.rollback();
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    private boolean updateOrderDetail(String oid, double amount, String time, String mealType) throws SQLException, ClassNotFoundException {
        loadMealNo(getMealNo(mealType));
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE `Order Detail` SET  oid=?, price=?,mealTime=?,mealNo=?,breakfast=?,lunch=?,dinner=? Where oid='" + oid + "'");
        stm.setObject(1, oid);
        stm.setObject(2, amount);
        stm.setObject(3, time);
        stm.setObject(4, tempMealNo);
        stm.setObject(5, tempBreakfastQty);
        stm.setObject(6, tempLunchQty);
        stm.setObject(7, tempDinnerQty);
        return stm.executeUpdate() > 0;


    }

    public void DeleteOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteOrder(lblTempOrderId.getText())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deleted");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                obList.remove(SelectedRowForRemove);
                System.out.println("OK chosen");
            }

        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean deleteOrder(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Orders WHERE oid='" + text + "'").executeUpdate() > 0;
    }

    public void searchordersOnAction(ActionEvent actionEvent) {
        LocalDate date = calOrderHistory.getValue();
        System.out.println(date);
        try {
            loadHistory(getHistoryOrders(date));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Order> getHistoryOrders(LocalDate promptText) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Orders Where date='" + promptText + "'");
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

    private void loadHistory(ArrayList<Order> historyOrders) {
        tblOrder.getItems().clear();
        historyOrders.forEach(e -> {
            try {
                loadCustomerNames(getCustomerNames(e.getCid()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
            obList.add(new Order(e.getOid(), e.getDate(), e.getTime(), e.getMealType(), e.getAmount(), tempCustomerName, e.getTransportNo()));
        });
        System.out.println(obList);

        tblOrder.setItems(obList);
    }
}
