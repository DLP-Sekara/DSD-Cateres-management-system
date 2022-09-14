package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

public class SettingFormController implements Initializable {
    public AnchorPane settingFormBackGround;
    public Label lblDate;
    public Label lblTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDateAndTime();
        try {
            loadFreeContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFreeContent() throws IOException {
        loadUi("AccountSettingForm");
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

    public void openSetting1(MouseEvent mouseEvent) throws IOException {
        loadUi("AccountSettingForm");
    }

    public void openSetting3(MouseEvent mouseEvent) throws IOException {
        loadUi("SupplierSettingForm");
    }

    public void openSetting4(MouseEvent mouseEvent) throws IOException {
        loadUi("DriverSettingsForm");
    }

    public void openSetting2(MouseEvent mouseEvent) throws IOException {
        loadUi("EmployerSettingForm");
    }
    public void openSetting5(MouseEvent mouseEvent) throws IOException {
        loadUi("TransportSettingForm");
    }
    void loadUi(String filName) throws IOException {
        AnchorPane pane;
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("../view/"+filName+".fxml"));
        pane = fxmlLoader.load();
        settingFormBackGround.getChildren().setAll(pane);

    }



}
