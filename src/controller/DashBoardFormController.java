package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardFormController implements Initializable {
    public AnchorPane DashBoardPage;
    public AnchorPane context;
    public StackPane stackone;
    public BorderPane boardOne;
    public VBox vBoxone;
    public  StackPane panalRoot;
    public AnchorPane lblBasePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadUi("DashBoardPreviewForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openDashBoard(ActionEvent actionEvent) throws IOException {
        loadUi("DashBoardPreviewForm");
    }

    public void ExitButtonOnAction(ActionEvent actionEvent) throws IOException {

        URL resource = getClass().getResource("../view/LogingForm.fxml");
        Parent load = FXMLLoader.load(resource);
        Stage window = (Stage) stackone.getScene().getWindow();
        window.setTitle("LoginPage");
        //window.initStyle(StageStyle.UNDECORATED);
        //window.initStyle(StageStyle.UTILITY);
        window.setMaximized(true);
        window.setScene(new Scene(load));

    }

    public void openSettingsPage(ActionEvent actionEvent) throws IOException {
        loadUi("SettingForm");
    }

    public void openBillsFormPage(ActionEvent actionEvent) throws IOException {
        loadUi("BillsForm");
    }

   /* public void openPaymentPage(ActionEvent actionEvent) throws IOException {
        loadUi("PaymentForm");
    }*/

    public void openStoresPage(ActionEvent actionEvent) throws IOException {
        loadUi("StoresForm");
    }

    public void openMealsPage(ActionEvent actionEvent) throws IOException {
        loadUi("MealsForm");
    }

    public void openOrdersPage(ActionEvent actionEvent) throws IOException {
        loadUi("OrdersForm");
    }

    public void openCustomerPage(ActionEvent actionEvent) throws IOException {
        loadUi("CustomersForm");
    }

    void loadUi(String filName) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/"+filName+".fxml"));
        pane = fxmlLoader.load();
        panalRoot.getChildren().setAll(pane);

    }


}
