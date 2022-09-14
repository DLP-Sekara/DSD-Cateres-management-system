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
import model.Employee;
import model.Meal;
import model.Supplier;
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

public class MealsFormController implements Initializable {

    public TableView<Meal>tblMeal;
    public TableColumn colMealNo;
    public TableColumn colMealName;
    public TableColumn colMealType;
    public TableColumn colMealPrice;
    public Button addButton;
    public Button updateButton;
    public TextField txtMealNo;
    public TextField txtMealName;
    public TextField txtMealprice;
    public ComboBox<String> cmbMealType;
    public Label lblDate;
    public Label lblTime;
    Object SelectedRowForRemove = -1;
    ObservableList<Meal> obList = FXCollections.observableArrayList();
    String mealType;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();

    Pattern mealNoPattern = Pattern.compile("^[0-9]{1,2}$");
    Pattern mealNamePattern = Pattern.compile("^[A-z ]{2,30}$");
    Pattern mealPricePattern = Pattern.compile("^[0-9][0-9]*([.][0-9]{2})?$");
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDateAndTime();
        addButton.setDisable(true);
        updateButton.setDisable(true);
        storeValidations();

        cmbMealType.getItems().addAll("Breakfast","Lunch","Dinner");
        try {
            colMealNo.setCellValueFactory(new PropertyValueFactory<>("mealNo"));
            colMealName.setCellValueFactory(new PropertyValueFactory<>("mealName"));
            colMealType.setCellValueFactory(new PropertyValueFactory<>("mealType"));
            colMealPrice.setCellValueFactory(new PropertyValueFactory<>("cost"));
            loadMealTbl(getAllMeals());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tblMeal.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
               // oblist2.addAll(newValue);
                SelectedRowForRemove = newValue;
                txtMealNo.setText(newValue.getMealNo());
                txtMealName.setText(newValue.getMealName());
                 cmbMealType.getSelectionModel().select(newValue.getMealType());
                txtMealprice.setText(String.valueOf(newValue.getCost()));
            }
            txtMealNo.setDisable(true);
        });
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
    private void loadMealTbl(ArrayList<Meal> allMeals) {
        allMeals.forEach(e -> {
            obList.add(new Meal(e.getMealNo(), e.getMealName(), e.getMealType(), e.getCost()));
        });
        tblMeal.setItems(obList);
    }

    private ArrayList<Meal> getAllMeals() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Meal");
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

    private void storeValidations() {
        map.put(txtMealNo, mealNoPattern);
        map.put(txtMealName, mealNamePattern);
        map.put(txtMealprice, mealPricePattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateMeal(map, addButton,updateButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void addMealOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
         mealType = (String) cmbMealType.getSelectionModel().getSelectedItem();
        Meal m1=new Meal(txtMealNo.getText(),txtMealName.getText(),mealType,Double.parseDouble(txtMealprice.getText()));

        if (checkMeal(txtMealNo.getText())) {
            new Alert(Alert.AlertType.WARNING, "Meal already exists..").show();
        } else {
            if (saveMeal(m1)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    this.tblMeal.refresh();
                    tblMeal.getItems().clear();
                    loadMealTbl(getAllMeals());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again..").show();
            }
        }
    }

    private boolean saveMeal(Meal m1) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Meal VALUES(?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, m1.getMealNo());
        stm.setObject(2, m1.getMealName());
        stm.setObject(3, m1.getMealType());
        stm.setObject(4, m1.getCost());

        //System.out.println("end");
        return stm.executeUpdate() > 0;
    }

    private boolean checkMeal(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Meal WHERE mealNo=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Meal(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            );
            return true;
        } else {
            return false;
        }
    }

    public void deleteMealOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteMeal(txtMealNo.getText())) {
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

    private boolean deleteMeal(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Meal WHERE mealNo='" + text + "'").executeUpdate() > 0;
    }

    public void updateOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        mealType = (String) cmbMealType.getSelectionModel().getSelectedItem();
        Meal m1=new Meal(txtMealNo.getText(),txtMealName.getText(),mealType,Double.parseDouble(txtMealprice.getText()));
        if (updateMeal(m1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblMeal.refresh();
                tblMeal.getItems().clear();
                loadMealTbl(getAllMeals());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateMeal(Meal m1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Meal SET  mealName=?, mealType=?,cost=? Where mealNo='" + m1.getMealNo() + "'");
        //stm.setObject(1, d1.getDriverId());
        //stm.setObject(1, m1.getMealNo());
        stm.setObject(1, m1.getMealName());
        stm.setObject(2, m1.getMealType());
        stm.setObject(3, m1.getCost());
        return stm.executeUpdate() > 0;
    }
}
