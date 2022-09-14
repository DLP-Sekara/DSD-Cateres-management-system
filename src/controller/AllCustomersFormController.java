package controller;

import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllCustomersFormController implements Initializable {
    public TableView tblNinjaMale;
    public TableView tblNinjaFemale;
    public TableView tblDentaMale;
    public TableView tblDentaFemale;
    public TableColumn colNinjaMaleId;
    public TableColumn colNinjaMaleName;
    public TableColumn colNinjaFemaleId;
    public TableColumn colNinjaFemaleName;
    public TableColumn colDentaMaleId;
    public TableColumn colDentaMaleName;
    public TableColumn colDentaFemaleId;
    public TableColumn colDentaFemaleName;
    public Label lblNinjaMaleCustomerCount;
    public Label lblNinjaFemaleCustomerCount;
    public Label lblDentaMaleCustomerCount;
    public Label lblDentaFemaleCustomerCount;
    ObservableList<Customer> obList1 = FXCollections.observableArrayList();
    ObservableList<Customer> obList2 = FXCollections.observableArrayList();
    ObservableList<Customer> obList3 = FXCollections.observableArrayList();
    ObservableList<Customer> obList4 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            colNinjaMaleId.setCellValueFactory(new PropertyValueFactory<>("cid"));
            colNinjaMaleName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            loadNInjaMaleTbl(getNinjaMale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            colNinjaFemaleId.setCellValueFactory(new PropertyValueFactory<>("cid"));
            colNinjaFemaleName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            loadNinjaFemaleTbl(getNinjaFemale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            colDentaMaleId.setCellValueFactory(new PropertyValueFactory<>("cid"));
            colDentaMaleName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            loadDentaMaleTbl(getDentaMale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            colDentaFemaleId.setCellValueFactory(new PropertyValueFactory<>("cid"));
            colDentaFemaleName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            loadDentaFemaleTbl(getDentaFemale());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        lblNinjaMaleCustomerCount.setText(String.valueOf(tblNinjaMale.getItems().size()));
        lblNinjaFemaleCustomerCount.setText(String.valueOf(tblNinjaFemale.getItems().size()));
        lblDentaMaleCustomerCount.setText(String.valueOf(tblDentaMale.getItems().size()));
        lblDentaFemaleCustomerCount.setText(String.valueOf(tblDentaFemale.getItems().size()));
    }

    private void loadDentaFemaleTbl(ArrayList<Customer> dentaFemale) {
        dentaFemale.forEach(e -> {
            obList4.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblDentaFemale.setItems(obList4);
    }

    private void loadDentaMaleTbl(ArrayList<Customer> dentaMale) {
        dentaMale.forEach(e -> {
            obList3.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblDentaMale.setItems(obList3);
    }

    private void loadNinjaFemaleTbl(ArrayList<Customer> ninjaFemale) {
        ninjaFemale.forEach(e -> {
            obList2.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblNinjaFemale.setItems(obList2);
    }

    private void loadNInjaMaleTbl(ArrayList<Customer> ninjaMale) {
        ninjaMale.forEach(e -> {
            obList1.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblNinjaMale.setItems(obList1);
    }

    private ArrayList<Customer> getDentaFemale() throws SQLException, ClassNotFoundException {
        String temp1 = "DENTA";
        String temp2 = "female";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "' AND gender='" + temp2 + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Customer> customers = new ArrayList<>();

        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return customers;
    }

    private ArrayList<Customer> getDentaMale() throws SQLException, ClassNotFoundException {
        String temp1 = "DENTA";
        String temp2 = "Male";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "' AND gender='" + temp2 + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Customer> customers = new ArrayList<>();

        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return customers;
    }

    private ArrayList<Customer> getNinjaFemale() throws SQLException, ClassNotFoundException {
        String temp1 = "NINJA";
        String temp2 = "female";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "' AND gender='" + temp2 + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Customer> customers = new ArrayList<>();

        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return customers;
    }

    private ArrayList<Customer> getNinjaMale() throws SQLException, ClassNotFoundException {
        String temp1 = "NINJA";
        String temp2 = "Male";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "' AND gender='" + temp2 + "'");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Customer> customers = new ArrayList<>();

        while (rst.next()) {
            customers.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return customers;
    }
}
