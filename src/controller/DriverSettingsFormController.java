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
import model.Driver;
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

public class DriverSettingsFormController implements Initializable {
    public TextField txtDriverId;
    public TextField txtDriverName;
    public TextField txtDriverContact;
    public TextField txtDriverVehicle;
    public Button saveButton;
    public Label lblDriverId;
    public TableView <Driver>tblDrivers;
    public TableColumn<Object, Object> colDriverid;
    public TableColumn<Object, Object> colDriverName;
    public TableColumn colDriverContact;
    public TableColumn colDriverVehicleType;
    Object SelectedRowForRemove = -1;
    ObservableList<Driver> obList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern contactPattern = Pattern.compile("^[0-9 ]{10}$");
    Pattern vehicleType = Pattern.compile("^[A-z0-9/ ]{3,30}$");

    public static String getLastDriverId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT driverId FROM Driver ORDER BY driverId DESC LIMIT 1");
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("driverId");
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveButton.setDisable(true);
        storeValidations();
        try {
            colDriverid.setCellValueFactory(new PropertyValueFactory<>("driverId"));
            colDriverName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
            colDriverContact.setCellValueFactory(new PropertyValueFactory<>("driverContact"));
            colDriverVehicleType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
            loadDriverTbl(getAllDrivers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
      tblDrivers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          if (newValue != null) {
              SelectedRowForRemove = newValue;
              lblDriverId.setText(newValue.getDriverId());
              txtDriverName.setText(newValue.getDriverName());
              txtDriverContact.setText(newValue.getDriverContact());
              txtDriverVehicle.setText(newValue.getVehicleType());
          }
      });

    }

    private void loadDriverTbl(ArrayList<Driver> allDrivers) {
        allDrivers.forEach(e -> {
            obList.add(new Driver(e.getDriverId(), e.getDriverName(), e.getDriverContact(), e.getVehicleType()));
        });
        tblDrivers.setItems(obList);
    }

    private ArrayList<Driver> getAllDrivers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Driver");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Driver> drivers = new ArrayList<>();

        while (rst.next()) {
            drivers.add(new Driver(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            ));
        }
        return drivers;
    }

    private void storeValidations() {
        map.put(txtDriverName, namePattern);
        map.put(txtDriverContact, contactPattern);
        map.put(txtDriverVehicle, vehicleType);

    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateDriver(map, saveButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();

            }
        }
    }

    public void saveDriverOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String did = getLastDriverId();
        String finalId = "D-001";
        if (did != null) {
            String[] splitString = did.split("-");
            int id = Integer.parseInt(splitString[1]);
            id++;

            if (id < 10) {
                finalId = "D-00" + id;
            } else if (id < 100) {
                finalId = "D-0" + id;
            } else {
                finalId = "D-" + id;
            }
            lblDriverId.setText(finalId);
            lblDriverId.setVisible(false);
        } else {
            lblDriverId.setText(finalId);
            lblDriverId.setVisible(false);
        }
        Driver d1 = new Driver(finalId, txtDriverName.getText(), txtDriverContact.getText(), txtDriverVehicle.getText());
        if (checkName(txtDriverName.getText())) {
            new Alert(Alert.AlertType.WARNING, "Driver already exists..").show();
        } else {
            if (saveDriver(d1)) {
                //new Alert(Alert.AlertType.CONFIRMATION, "Saved..").show();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    this.tblDrivers.refresh();
                    //this.tblCustomers.setItems(obList);
                    /*obList.removeAll();*/
                    tblDrivers.getItems().clear();
                    loadDriverTbl(getAllDrivers());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again..").show();
            }
        }
    }

    private boolean checkName(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Driver WHERE driverName=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Driver(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
            return true;
        } else {
            return false;
        }
    }

    private boolean saveDriver(Driver d1) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Driver VALUES(?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, d1.getDriverId());
        stm.setObject(2, d1.getDriverName());
        stm.setObject(3, d1.getDriverContact());
        stm.setObject(4, d1.getVehicleType());
        return stm.executeUpdate() > 0;
    }

    public void deleteDriver(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteCustomer(lblDriverId.getText())) {
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
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Driver WHERE driverId='" + text + "'").executeUpdate() > 0;
    }

    public void updateDriver(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Driver d1=new Driver(lblDriverId.getText(),txtDriverName.getText(),txtDriverContact.getText(),txtDriverVehicle.getText());
        if (updateDrivers(d1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblDrivers.refresh();
                tblDrivers.getItems().clear();
                loadDriverTbl(getAllDrivers());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateDrivers(Driver d1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Driver SET driverName=?, driverContact=?, vehicleType=? Where driverId='" + d1.getDriverId() + "'");
        //stm.setObject(1, d1.getDriverId());
        stm.setObject(1, d1.getDriverName());
        stm.setObject(2, d1.getDriverContact());
        stm.setObject(3, d1.getVehicleType());
        return stm.executeUpdate() > 0;
    }
}
