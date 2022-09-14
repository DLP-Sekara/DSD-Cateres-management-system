package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Supplier;
import model.SystemUser;
import util.Validation;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AccountSettingFormController implements Initializable {
    public TextField txtCurrentUsername;
    public TextField txtNewUsername;
    public TextField txtCurrentPassword;
    public TextField txtnewPassword;
    public Button SubmitButton;
    public Button cancelbutton;
    public Label lblSID;
    Object SelectedRowForRemove = -1;
    ObservableList<Supplier> obList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern oldUsernamePattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern newUsernamePattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern oldPassword = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern newPassword = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SubmitButton.setDisable(true);
        storeValidations();
    }

    private void storeValidations() {
        map.put(txtCurrentUsername, oldUsernamePattern);
        map.put(txtNewUsername, newUsernamePattern);
        map.put(txtCurrentPassword, oldPassword);
        map.put(txtnewPassword, newPassword);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateAccount(map, SubmitButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void submitAccountSetting(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    /*    String sid = getLastSid();
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
            lblSID.setText(finalId);
            lblSID.setVisible(false);
        } else {
            lblSID.setText(finalId);
            lblSID.setVisible(false);
        }*/
        SystemUser s1 = new SystemUser("S-001", txtNewUsername.getText(), txtnewPassword.getText());
        if (updateSuppliers(s1)) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated").show();
            /*Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblSupplier.refresh();
                tblSupplier.getItems().clear();
                loadSupplierTbl(getAllSupplirers());
            }*/
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateSuppliers(SystemUser s1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE SystemUser SET  userName=?,password=?");
        //stm.setObject(1, d1.getDriverId());

        stm.setObject(1, s1.getUserName());
        stm.setObject(2, s1.getPassword());

        return stm.executeUpdate() > 0;
    }

    public void cancelAccountSetting(ActionEvent actionEvent) {
        txtCurrentUsername.clear();
        txtNewUsername.clear();
        txtCurrentPassword.clear();
        txtnewPassword.clear();
    }


}
