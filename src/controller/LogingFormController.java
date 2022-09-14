package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Ingredient;
import util.Validation;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LogingFormController implements Initializable {
    public AnchorPane logingPage;
    public TextField txtUserName;
    public PasswordField txtpassword;
    public Button signInButton;
    public Label lblWarning;
    Object SelectedRowForRemove = -1;
    ObservableList<Ingredient> obList = FXCollections.observableArrayList();
    String tempUsername;
    String tempPassword;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern UserNamePattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern PasswordPattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signInButton.setDisable(true);
        storeValidations();
    }

    private void storeValidations() {
        map.put(txtUserName, UserNamePattern);
        map.put(txtpassword, PasswordPattern);

    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateLogin(map, signInButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void signonAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
      /*  String username=txtUserName.getText();
        String password=txtpassword.getText();
        System.out.println(username);
        System.out.println(password);*/

     /*   if(username=="lahiru" & password=="1234") {
            URL resource = getClass().getResource("../view/DashBoardForm.fxml");
            Parent load = FXMLLoader.load(resource);
            Stage window = (Stage) logingPage.getScene().getWindow();
            window.setTitle("DSD Caterers");
            window.setMaximized(true);
            window.setScene(new Scene(load));
        }*/
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM SystemUser ").executeQuery();
        if (rst1.next()) {
            String tempUsername = rst1.getString(2);
            String tempPassword = rst1.getString(3);
            if(txtUserName.getText().equals(rst1.getString(2)) & txtpassword.getText().equals(rst1.getString(3))){

                System.out.println(tempUsername);
                System.out.println(tempPassword);
                System.out.println(tempPassword);

                URL resource = getClass().getResource("../view/DashBoardForm.fxml");
                Parent load = FXMLLoader.load(resource);
                Stage window = (Stage) logingPage.getScene().getWindow();
                window.setTitle("DSD Caterers");
                window.setMaximized(true);
                window.setScene(new Scene(load));

          /*  URL resource = getClass().getResource("view/DashBoardForm.fxml");
            Parent load = FXMLLoader.load(resource);
            Scene scene = new Scene(load);
            primaryStage.setTitle("DSD Caterers");
            primaryStage.setScene(scene);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.initStyle(StageStyle.UTILITY);
            //primaryStage.resizableProperty().setValue(Boolean.FALSE);
            //primaryStage.setResizable(false);

            primaryStage.setMaximized(true);
            primaryStage.show();*/
            }else{
                lblWarning.setText("Wrong Details");
                System.out.println("fail");
            }
        }

        //========
       /* URL resource = getClass().getResource("../view/DashBoardForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) logingPage.getScene().getWindow();
        window.setTitle("DSD Caterers");
        window.setMaximized(true);
        window.setScene(new Scene(load));*/
        //========

    }


}
