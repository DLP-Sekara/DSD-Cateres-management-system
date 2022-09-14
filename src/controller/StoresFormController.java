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
import model.Ingredient;
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

public class StoresFormController implements Initializable {
    public TableView<Ingredient> tblItems;
    public TableColumn colItemName;
    public TableColumn coliseumQuantity;
    public TableColumn colShopName;
    public TableColumn colItemPrice;
    public Button addButton;
    public TextField txtItemName;
    public TextField txtItemQuantity;
    public Button updateButton;
    public TextField txtItemPrice;
    public ComboBox<String> cmbShopName;
    public ComboBox<String> cmbMealName;
    public Label lblDate;
    public Label lblTime;
    String tempShopNo;
    String tempShopName;
    String tempShopName2;
    String tempMealNo;
    Object SelectedRowForRemove = -1;
    ObservableList<Ingredient> obList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap();
    Pattern mealNamePattern = Pattern.compile("^[A-z ]{2,30}$");
    Pattern mealPricePattern = Pattern.compile("^[1-9][0-9]*([.][0-9]{2})?$");
    Pattern mealNoPattern = Pattern.compile("^[0-9]{1,8}$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDateAndTime();
        addButton.setDisable(true);
        updateButton.setDisable(true);
        storeValidations();
        try {
            loadShopnames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            loadMealnames();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            colItemName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
            coliseumQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
            colShopName.setCellValueFactory(new PropertyValueFactory<>("shopNo"));
            colItemPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            loadItemTbl(getAllitems());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbShopName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Supplier s1 = null;
            try {
                s1 = getShopNo(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (s1 == null) {
                new Alert(Alert.AlertType.WARNING, "Empty Result Set");
            } else {
                tempShopNo = s1.getShopNo();
                tempShopName=s1.getShopName();
            }
        });
        cmbMealName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Meal m1 = null;
            try {
                m1 = getMealNo(newValue);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (m1 == null) {
                new Alert(Alert.AlertType.WARNING, "Empty Result Set");
            } else {
                tempMealNo = m1.getMealNo();
            }
        });
        tblItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                txtItemName.setText(newValue.getIngredientName());
                txtItemPrice.setText(String.valueOf(newValue.getPrice()));
                txtItemQuantity.setText(String.valueOf(newValue.getQty()));
                cmbShopName.getSelectionModel().select(newValue.getShopNo());
                cmbMealName.getSelectionModel().select(newValue.getMealNo());

                txtItemName.setDisable(true);
            }

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

    private void loadItemTbl(ArrayList<Ingredient> allitems) {
        System.out.println(tempShopName);
        allitems.forEach(e -> {
            try {
                obList.add(new Ingredient(e.getMealNo(), getShopName(e.getShopNo()), e.getIngredientName(), e.getQty(), e.getPrice()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        tblItems.setItems(obList);
    }

    private String getShopName(String shopNo) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier WHERE shopNo='" + shopNo + "'");
        ResultSet rst = stm.executeQuery();
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
        suppliers.forEach(e -> {
            tempShopName2 = e.getShopName();
        });
        return tempShopName2;
    }

    private ArrayList<Ingredient> getAllitems() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Ingredient");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        while (rst.next()) {
            ingredients.add(new Ingredient(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5)
            ));
        }
        return ingredients;
    }

    private Meal getMealNo(String newValue) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Meal WHERE mealName='" + newValue + "'").executeQuery();
        if (rst.next()) {
            return new Meal(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            );
        } else {
            return null;
        }
    }

    private Supplier getShopNo(String newValue) throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier WHERE shopName='" + newValue + "'").executeQuery();
        if (rst.next()) {
            return new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)
            );
        } else {
            return null;
        }
    }

    private void loadMealnames() throws SQLException, ClassNotFoundException {
        List<String> customerIds = getmealIds();
        cmbMealName.getItems().addAll(customerIds);
    }

    private List<String> getmealIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Meal").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(2)
            );
        }
        return ids;
    }

    private void loadShopnames() throws SQLException, ClassNotFoundException {
        List<String> customerIds = getShopIds();
        cmbShopName.getItems().addAll(customerIds);
    }

    private List<String> getShopIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Supplier").executeQuery();
        List<String> ids = new ArrayList<>();
        while (rst.next()) {
            ids.add(
                    rst.getString(2)
            );
        }
        return ids;
    }

    private void storeValidations() {
        map.put(txtItemName, mealNamePattern);
        map.put(txtItemPrice, mealPricePattern);
        map.put(txtItemQuantity, mealNoPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateItem(map, addButton, updateButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void addItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (tempMealNo != null & tempShopNo != null) {
            Ingredient i1 = new Ingredient(tempMealNo, tempShopNo, txtItemName.getText(), Integer.parseInt(txtItemPrice.getText()), Double.parseDouble(txtItemQuantity.getText()));
            if (checkItem(txtItemName.getText())) {
                new Alert(Alert.AlertType.WARNING, "Meal already exists..").show();
            } else {
                if (saveMeal(i1)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                    Optional<ButtonType> option = alert.showAndWait();
                    if (option.get() == ButtonType.OK) {
                        this.tblItems.refresh();
                        tblItems.getItems().clear();
                        loadItemTbl(getAllitems());
                    }
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again..").show();
                }
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Enter Correct Data..").show();
        }
    }

    private boolean saveMeal(Ingredient i1) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Ingredient VALUES(?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, i1.getMealNo());
        stm.setObject(2, i1.getShopNo());
        stm.setObject(3, i1.getIngredientName());
        stm.setObject(4, i1.getPrice());
        stm.setObject(5, i1.getQty());


        //System.out.println("end");
        return stm.executeUpdate() > 0;
    }

    private boolean checkItem(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Ingredient WHERE ingredientName=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Ingredient(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getInt(4),
                    rst.getDouble(5)
            );
            return true;
        } else {
            return false;
        }
    }

    public void deleteItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteItem(txtItemName.getText())) {
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

    private boolean deleteItem(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Ingredient WHERE ingredientName='" + text + "'").executeUpdate() > 0;
    }

    public void updateItemOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Ingredient i1 = new Ingredient(tempMealNo, tempShopNo, txtItemName.getText(), Integer.parseInt(txtItemPrice.getText()), Double.parseDouble(txtItemQuantity.getText()));
        if (updateItem(i1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblItems.refresh();
                tblItems.getItems().clear();
                loadItemTbl(getAllitems());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateItem(Ingredient i1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Ingredient SET  mealNo=?, shopNo=?,qty=?,price=? Where ingredientName='" + i1.getIngredientName() + "'");
        //stm.setObject(1, d1.getDriverId());
        //stm.setObject(1, m1.getMealNo());
        stm.setObject(1, i1.getMealNo());
        stm.setObject(2, i1.getShopNo());
        stm.setObject(3, i1.getQty());
        stm.setObject(4, i1.getPrice());
        return stm.executeUpdate() > 0;
    }


}
