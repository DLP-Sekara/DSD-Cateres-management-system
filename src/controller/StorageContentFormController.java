package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Ingredient;
import model.Supplier;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class StorageContentFormController implements Initializable {
    public TableView<Ingredient> tblStorageContent;
    public TableColumn colIngredientName;
    public TableColumn ingredientQty;
    public TableColumn ingredientPrice;
    public TableColumn colSupplier;
    String tempShopName;
    ObservableList<Ingredient> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colIngredientName.setCellValueFactory(new PropertyValueFactory<>("ingredientName"));
            ingredientQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
            ingredientPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
            colSupplier.setCellValueFactory(new PropertyValueFactory<>("shopNo"));
            loadStoragetbl(getContent());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadStoragetbl(ArrayList<Ingredient> content) {
        content.forEach(e -> {
            try {
                obList.add(new Ingredient(e.getMealNo(), getShopName(e.getShopNo()), e.getIngredientName(), e.getQty(), e.getPrice()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
        tblStorageContent.setItems(obList);
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
            tempShopName = e.getShopName();
        });
        return tempShopName;
    }

    private ArrayList<Ingredient> getContent() throws SQLException, ClassNotFoundException {
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
}
