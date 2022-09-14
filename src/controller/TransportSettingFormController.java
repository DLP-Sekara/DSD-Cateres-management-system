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
import model.Meal;
import model.Transport;
import util.Validation;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TransportSettingFormController implements Initializable {

    public TextField txtTransportNo;
    public Button saveButton;
    public TableView<Transport> tblTransport;
    public TableColumn colTransportNo;
    public TableColumn colTransportType;
    public TableColumn colTransportCost;
    public TableColumn colTransportTime;
    public TableColumn colDeriverId;
    public TextField txtTransportType;
    public TextField txtTransportCost;
    public Button updateButton;
    public ComboBox<String> cmbTransportTime;
    public ComboBox <String>cmbTransportDriverID;
    public Label lblDate;
    public Label transportNo;
    String tempTransportTime;
    String tempDriverID;
    Object SelectedRowForRemove = -1;
    ObservableList<Transport> obList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();

    //Pattern TransportNoPattern = Pattern.compile("^[0-9]{1,2}$");
    Pattern TransportTypePattern = Pattern.compile("^[A-z ]{2,30}$");
    Pattern TransportCostPattern = Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveButton.setDisable(true);
        updateButton.setDisable(true);
        storeValidations();
      cmbTransportTime.getItems().addAll("Breakfast","Lunch","Dinner");
        try {
            loadDriverID();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadDate();
        cmbTransportDriverID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Driver d1 = null;
            try {
                d1 = getDriverId(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (d1 == null) {
                new Alert(Alert.AlertType.WARNING, "Empty Result Set");
            } else {
                tempDriverID=d1.getDriverId();
            }
        });

        try {
            colTransportNo.setCellValueFactory(new PropertyValueFactory<>("transportNo"));
            colTransportType.setCellValueFactory(new PropertyValueFactory<>("transportType"));
            colTransportTime.setCellValueFactory(new PropertyValueFactory<>("time"));
            colTransportCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
            colDeriverId.setCellValueFactory(new PropertyValueFactory<>("driverId"));
            loadTransportTbl(getAllTransport());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblTransport.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                transportNo.setText(newValue.getTransportNo());
                txtTransportType.setText(newValue.getTransportType());
                txtTransportCost.setText(String.valueOf(newValue.getCost()));
                cmbTransportTime.getSelectionModel().select(newValue.getTime());
                cmbTransportDriverID.getSelectionModel().select(newValue.getDriverId());
            }
        });
    }

    private void loadTransportTbl(ArrayList<Transport> allTransport) {
        allTransport.forEach(e -> {
            obList.add(new Transport(e.getTransportNo(), e.getTransportType(), e.getDate(), e.getTime(),e.getCost(),e.getDriverId()));
        });
        tblTransport.setItems(obList);
    }

    private ArrayList<Transport> getAllTransport() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Transport");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
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

    private Driver getDriverId(String newValue) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Driver WHERE driverName='" + newValue + "'").executeQuery();
        if (rst.next()) {
            return new Driver(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
        } else {
            return null;
        }
    }

    private void loadDate() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
        lblDate.setText(f.format(date));
    }

    private void loadDriverID() throws SQLException, ClassNotFoundException {
        List<String> customerIds =getDriverIds();
        cmbTransportDriverID.getItems().addAll(customerIds);
    }

    private List<String> getDriverIds() throws SQLException, ClassNotFoundException {
        ResultSet rst= DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Driver").executeQuery();
        List<String>ids=new ArrayList<>();
        while (rst.next()){
            ids.add(
                    rst.getString(2)
            );
        }
        return ids;
    }

    private void storeValidations() {
       // map.put(txtTransportNo, TransportNoPattern);
        map.put(txtTransportType, TransportTypePattern);
        map.put(txtTransportCost, TransportCostPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateTransport(map, saveButton,updateButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void saveTransportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        tempTransportTime=cmbTransportTime.getSelectionModel().getSelectedItem();
        if ( tempTransportTime!= null & tempDriverID != null) {
            String did = getLastTransportId();
            String finalId = "T-001";
            if (did != null) {
                String[] splitString = did.split("-");
                int id = Integer.parseInt(splitString[1]);
                id++;

                if (id < 10) {
                    finalId = "T-00" + id;
                } else if (id < 100) {
                    finalId = "T-0" + id;
                } else {
                    finalId = "T-" + id;
                }
                transportNo.setText(finalId);
                transportNo.setVisible(false);
            } else {
                transportNo.setText(finalId);
                transportNo.setVisible(false);
            }

            Transport t1 = new Transport(finalId, txtTransportType.getText(), lblDate.getText(), tempTransportTime, Double.parseDouble(txtTransportCost.getText()), tempDriverID);

            if (checkTransport(txtTransportType.getText())) {
                new Alert(Alert.AlertType.WARNING, "Transport already exists..").show();
            } else {
                if (saveTransport(t1)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        this.tblTransport.refresh();
                        tblTransport.getItems().clear();
                        loadTransportTbl(getAllTransport());
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Enter Correct Data..").show();
        }
    }

    private String getLastTransportId() throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT transportNo FROM Transport ORDER BY transportNo DESC LIMIT 1");
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("transportNo");
        }
        return null;
    }

    private boolean saveTransport(Transport t1) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Transport VALUES(?,?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, t1.getTransportNo());
        stm.setObject(2, t1.getTransportType());
        stm.setObject(3, t1.getDate());
        stm.setObject(4, t1.getTime());
        stm.setObject(5, t1.getCost());
        stm.setObject(6, t1.getDriverId());

        //System.out.println("end");
        return stm.executeUpdate() > 0;
    }

    private boolean checkTransport(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Transport WHERE transportType=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Transport(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5),
                    rst.getString(6)
            );
            return true;
        } else {
            return false;
        }
    }

    public void deleteTransportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteTransport(transportNo.getText())) {
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

    private boolean deleteTransport(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Transport WHERE transportNo='" + text + "'").executeUpdate() > 0;
    }

    public void updateTransportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Transport t1=new Transport(transportNo.getText(),txtTransportType.getText(),lblDate.getText(),tempTransportTime,Double.parseDouble(txtTransportCost.getText()),tempDriverID);
        if (updateTransport(t1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblTransport.refresh();
                tblTransport.getItems().clear();
                loadTransportTbl(getAllTransport());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateTransport(Transport t1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Transport SET  transportType=?, time=? ,cost=?,driverId=? Where transportNo='" + t1.getTransportNo() + "'");
        //stm.setObject(1, d1.getDriverId());
        //stm.setObject(1, t1.getTransportNo());
        stm.setObject(1, t1.getTransportType());
        stm.setObject(2, t1.getTime());
        stm.setObject(3, t1.getCost());
        stm.setObject(4, t1.getDriverId());
        return stm.executeUpdate() > 0;
    }


}
