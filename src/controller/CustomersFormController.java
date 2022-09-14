package controller;

import db.DbConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import model.SystemUser;
import util.Validation;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

public class CustomersFormController implements Initializable {
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public TextField txtCustomerContact;
    public ComboBox<String> cmbCustomerCompany;
    public ComboBox<String> cmbCustomerGender;
    public Button customerAddBtn;
    public Label lblSid;
    public TableView<Customer> tblCustomers;
    public TableColumn colCustomerId;
    public TableColumn colCustomerName;
    public TableColumn colCustomerAddress;
    public TableColumn colCustomerContact;
    public TableColumn colCustomerGender;
    public TableColumn colCustomerCompanyname;
    public Label lblOrderId;
    public TextField setCustomerName;
    public TextField setCustomerAddress;
    public TextField setCustomerContact;
    public TextField setCustomerCompany;
    public Label lblTempCustomerId;
    public Label lblDate;
    public Label lblTime;
    Customer SelectedRowForRemove;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern contactPattern = Pattern.compile("^[0-9 ]{10}$");
    ObservableList<Customer> obList = FXCollections.observableArrayList();

    public static String getLastCustId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT cid FROM Customer ORDER BY cid DESC LIMIT 1");
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("cid");
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDateAndTime();
        cmbCustomerGender.getItems().addAll("Male", "Female");
        cmbCustomerCompany.getItems().addAll("NINJA", "DENTA");

        customerAddBtn.setDisable(true);
        storeValidations();

        try {
            getSystemUserId();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            colCustomerId.setCellValueFactory(new PropertyValueFactory<>("cid"));
            colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colCustomerContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            colCustomerGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
            colCustomerCompanyname.setCellValueFactory(new PropertyValueFactory<>("factory"));

            loadCustomerTbl(getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                lblTempCustomerId.setText(newValue.getCid());
                setCustomerName.setText(newValue.getCustomerName());
                setCustomerAddress.setText(newValue.getAddress());
                setCustomerContact.setText(newValue.getContact());
                setCustomerCompany.setText(newValue.getFactory());
            }
        });
        String gender = (String) cmbCustomerGender.getSelectionModel().getSelectedItem();
        String company = (String) cmbCustomerCompany.getSelectionModel().getSelectedItem();
        setCustomerCompany.setDisable(true);
        lblOrderId.setVisible(false);

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

    private void getSystemUserId() throws SQLException, ClassNotFoundException {
        SystemUser s1 = getId();
        if (s1 == null) {
            new Alert(Alert.AlertType.WARNING, "Empty Result Set").show();
        } else {
            setData(s1);
        }
    }

    void setData(SystemUser s1) {
        lblSid.setText(s1.getSid());
        lblSid.setVisible(false);
    }

    private SystemUser getId() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM SystemUser");
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            return new SystemUser(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            );
        } else {
            return null;
        }
    }

    private void storeValidations() {
        map.put(txtCustomerName, namePattern);
        map.put(txtCustomerAddress, addressPattern);
        map.put(txtCustomerContact, contactPattern);

    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validate(map, customerAddBtn);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
                cmbCustomerGender.requestFocus();
            }
        }
    }

    public void cmbKeyReleased(KeyEvent keyEvent) {
        /*String gender = (String) cmbCustomerGender.getSelectionModel().getSelectedItem();
        String company = (String) cmbCustomerCompany.getSelectionModel().getSelectedItem();
        if(gender!=null & company!=null){
            customerAddBtn.setDisable(true);
        }*/
    }

    public void addCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String gender = (String) cmbCustomerGender.getSelectionModel().getSelectedItem();
        String company = (String) cmbCustomerCompany.getSelectionModel().getSelectedItem();

        if (gender != null & company != null) {
            String cid = getLastCustId();
            String finalId = "C-001";
            if (cid != null) {
                String[] splitString = cid.split("-");
                int id = Integer.parseInt(splitString[1]);
                id++;

                if (id < 10) {
                    finalId = "C-00" + id;
                } else if (id < 100) {
                    finalId = "C-0" + id;
                } else {
                    finalId = "C-" + id;
                }
                lblOrderId.setText(finalId);
            } else {
                lblOrderId.setText(finalId);
            }

            Customer c1 = new Customer(finalId, txtCustomerName.getText(), txtCustomerAddress.getText(), company, txtCustomerContact.getText(), gender, lblSid.getText());
            if (checkName(txtCustomerName.getText())) {
                new Alert(Alert.AlertType.WARNING, "customer already exists..").show();
            } else {
                if (saveCustomer(c1)) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                    // tblItemData.getItems().clear();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }
            }

            //tblCustomers.refresh();
            tblCustomers.getItems().clear();
            loadCustomerTbl(getAllCustomers());
        } else {
            //customerAddBtn.setDisable(true);
            new Alert(Alert.AlertType.WARNING, "Enter Correct Data..").show();
        }
    }

    private void loadCustomerTbl(ArrayList<Customer> allCustomers) {

        allCustomers.forEach(e -> {
            obList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomers.setItems(obList);
    }

    private ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
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

    private boolean checkName(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer WHERE customerName=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)

            );
            return true;
        } else {
            return false;
        }
    }

    private boolean saveCustomer(Customer c1) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, c1.getCid());
        stm.setObject(2, c1.getCustomerName());
        stm.setObject(3, c1.getAddress());
        stm.setObject(4, c1.getFactory());
        stm.setObject(5, c1.getContact());
        stm.setObject(6, c1.getGender());
        stm.setObject(7, c1.getSid());
        return stm.executeUpdate() > 0;
    }

    public void cancelAddCustomer(ActionEvent actionEvent) {
        txtCustomerName.clear();
        txtCustomerContact.clear();
        txtCustomerAddress.clear();

    }

    public void updateCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String gender = (String) cmbCustomerGender.getSelectionModel().getSelectedItem();
        String company = (String) cmbCustomerCompany.getSelectionModel().getSelectedItem();
        Customer c1 = new Customer(lblTempCustomerId.getText(), setCustomerName.getText(), setCustomerAddress.getText(), setCustomerCompany.getText(), setCustomerContact.getText(), gender, company);
        if (updateCustomer(c1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblCustomers.refresh();
                //this.tblCustomers.setItems(obList);
                /*obList.removeAll();*/
                tblCustomers.getItems().clear();
                loadCustomerTbl(getAllCustomers());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateCustomer(Customer c1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Customer SET customerName=?, address=?, contact=?, factory=? Where cid='" + c1.getCid() + "'");
        stm.setObject(1, c1.getCustomerName());
        stm.setObject(2, c1.getAddress());
        stm.setObject(3, c1.getContact());
        stm.setObject(4, c1.getFactory());
        return stm.executeUpdate() > 0;
    }

    public void deleteCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteCustomer(lblTempCustomerId.getText())) {
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

    private boolean deleteCustomer(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Customer WHERE cid='" + text + "'").executeUpdate() > 0;
    }


}
//INSERT INTO Customer VALUES(C-001,nimal,colombo,NINJA,1234567890,Male,);