package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Supplier;
import util.Validation;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SupplierSettingFormController implements Initializable {
    public Button saveSupplierButton;
    public TextField supplierPayment;
    public TableView<Supplier> tblSupplier;
    public TableColumn colSupplierShopName;
    public TableColumn colSupplierAddress;
    public TableColumn colSupplierContact;
    public TableColumn colDate;
    public TableColumn ColSupplierPayment;
    public TextField supplierShopName;
    public TextField supplierAddress;
    public TextField supplierContact;
    public Label lblShopNo;
    public Button updateButton;
    Object SelectedRowForRemove = -1;
    ObservableList<Supplier> obList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern contactPattern = Pattern.compile("^[0-9 ]{10}$");
    Pattern paymentPattern = Pattern.compile("^[0-9. ]{2,10}$");
    //ObservableList<Supplier> obList = FXCollections.observableArrayList();

    public static String getLastSupliername() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT shopNo FROM Supplier ORDER BY shopNo DESC LIMIT 1");
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("shopNo");
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveSupplierButton.setDisable(true);
        updateButton.setDisable(true);
        storeValidations();

        try {
            colSupplierShopName.setCellValueFactory(new PropertyValueFactory<>("shopName"));
            colSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colSupplierContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            ColSupplierPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
            loadSupplierTbl(getAllSupplirers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                lblShopNo.setText(newValue.getShopNo());
                supplierShopName.setText(newValue.getShopName());
                supplierAddress.setText(newValue.getAddress());
                supplierContact.setText(newValue.getContact());
                supplierPayment.setText(String.valueOf(newValue.getPayment()));
            }
        });
        lblShopNo.setVisible(false);

    }

    private void loadSupplierTbl(ArrayList<Supplier> allSupplirers) {
        allSupplirers.forEach(e -> {
            obList.add(new Supplier(e.getShopNo(), e.getShopName(), e.getAddress(), e.getContact(), e.getPayment()));
        });
        tblSupplier.setItems(obList);
    }

    private ArrayList<Supplier> getAllSupplirers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Supplier> suppliers = new ArrayList<>();

        while (rst.next()) {
            suppliers.add(new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)
            ));
        }
        return suppliers;
    }

    private void storeValidations() {
        map.put(supplierShopName, namePattern);
        map.put(supplierAddress, addressPattern);
        map.put(supplierContact, contactPattern);
        map.put(supplierPayment, paymentPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateSupplier(map, saveSupplierButton,updateButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void saveSupplierOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String sid = getLastSupliername();
        String finalId = "S-001";
        if (sid != null) {
            String[] splitString = sid.split("-");
            int id = Integer.parseInt(splitString[1]);
            id++;

            if (id < 10) {
                finalId = "S-00" + id;
            } else if (id < 100) {
                finalId = "S-0" + id;
            } else {
                finalId = "S-" + id;
            }
            lblShopNo.setText(finalId);
            lblShopNo.setVisible(false);
        } else {
            lblShopNo.setText(finalId);
            lblShopNo.setVisible(false);
        }
        Supplier s1 = new Supplier(finalId, supplierShopName.getText(), supplierAddress.getText(), supplierContact.getText(), Double.parseDouble(supplierPayment.getText()));
        if (checkName(supplierShopName.getText())) {
            new Alert(Alert.AlertType.WARNING, "customer already exists..").show();
        } else {
            if (saveSupplier(s1)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    this.tblSupplier.refresh();
                    tblSupplier.getItems().clear();
                    loadSupplierTbl(getAllSupplirers());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again..").show();
            }
        }

    }

    private boolean saveSupplier(Supplier s1) throws SQLException, ClassNotFoundException {
        /*Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Supplier VALUES(?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, s1.getShopName());
        stm.setObject(2, s1.getAddress());
        stm.setObject(3, s1.getContact());
        stm.setObject(4, s1.getPayment());
        return stm.executeUpdate() > 0;*/
        System.out.println("start");
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Supplier VALUES(?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, s1.getShopNo());
        stm.setObject(2, s1.getShopName());
        stm.setObject(3, s1.getAddress());
        stm.setObject(4, s1.getContact());
        stm.setObject(5, s1.getPayment());
        System.out.println("end");
        return stm.executeUpdate() > 0;
    }

    private boolean checkName(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier WHERE shopName=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)
            );
            return true;
        } else {
            return false;
        }
    }

    public void supplierDeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteSupplier(lblShopNo.getText())) {
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

    private boolean deleteSupplier(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Supplier WHERE shopNo='" + text + "'").executeUpdate() > 0;
    }

    public void supplierUpdateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        if(lblShopNo.getText()!=null & supplierShopName.getText()!=null &supplierAddress.getText()!=null & supplierContact.getText()!=null & supplierPayment.getText()!=null) {
//            updateButton.setDisable(false);
            Supplier s1 = new Supplier(lblShopNo.getText(), supplierShopName.getText(), supplierAddress.getText(), supplierContact.getText(), Double.parseDouble(supplierPayment.getText()));
            if (updateSuppliers(s1)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    this.tblSupplier.refresh();
                    tblSupplier.getItems().clear();
                    loadSupplierTbl(getAllSupplirers());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again").show();
            }




    }

    private boolean updateSuppliers(Supplier s1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Supplier SET shopName=?, address=?, contact=?,payment=? Where shopNo='" + s1.getShopNo() + "'");
        //stm.setObject(1, d1.getDriverId());
        stm.setObject(1, s1.getShopName());
        stm.setObject(2, s1.getAddress());
        stm.setObject(3, s1.getContact());
        stm.setObject(4, s1.getPayment());
        return stm.executeUpdate() > 0;
    }
}
