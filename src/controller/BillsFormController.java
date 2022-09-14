package controller;
import db.DbConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Customer;
import model.Order;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import view.tm.CustomerBillTm;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

public class BillsFormController implements Initializable {
    public Label lblDate;
    public Label lblTime;
    public ComboBox<String>cmbFactoryname;
    public TableView<CustomerBillTm> tblCustomerBills;
    public TableColumn colDate;
    public TableColumn colBreakfast;
    public TableColumn colLunch;
    public TableColumn colDinner;
    public TableColumn colAmount;
    public TableColumn colCustomerNames;
    public TableView <Customer>tblCustomernames;
    public Label lblCustomerName;
    public Label lblTotalCost;
    public Label lblTotal;
    Object SelectedRowForRemove = -1;
    ObservableList<CustomerBillTm> obList = FXCollections.observableArrayList();
    ObservableList<Customer> CustomerObList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDateAndTime();
        try {
            colCustomerNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            loadAllCustomers(getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbFactoryname.getItems().addAll("NINJA","DENTA");
        cmbFactoryname.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("NINJA")) {
                try {
                    colCustomerNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                    loadNinjaCustomers(getNinjaCustomers());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (newValue.equals("DENTA")) {
                try {
                    colCustomerNames.setCellValueFactory(new PropertyValueFactory<>("customerName"));
                    loadDentaCustomers(getDentaCustomers());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        tblCustomernames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           if(newValue!=null) {
               lblCustomerName.setText(newValue.getCustomerName());
               try {
                   loadCustomerBill(getUniqueCustomerBill(newValue.getCid()));
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
               try {
                   loadTotalPrice(newValue.getCid());
               } catch (SQLException throwables) {
                   throwables.printStackTrace();
               } catch (ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }

        });
        try {
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            colBreakfast.setCellValueFactory(new PropertyValueFactory<>("breakfast"));
            colLunch.setCellValueFactory(new PropertyValueFactory<>("lunch"));
            colDinner.setCellValueFactory(new PropertyValueFactory<>("dinner"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("price"));
            loadCustomerBill(getCustomerBill());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadTotalPrice(String cid) throws SQLException, ClassNotFoundException {
        int storeCount=0;
        double percentage=0;
        ResultSet rst1 = DbConnection.getInstance().getConnection().prepareStatement("SELECT SUM(amount) FROM Orders Where cid='" + cid + "'").executeQuery();
        if (rst1.next()) {
            storeCount = rst1.getInt(1);
            System.out.println(storeCount);
        }
        lblTotal.setText("Total :");
        lblTotalCost.setText(String.valueOf(storeCount)+" /=");
    }

    private ArrayList<CustomerBillTm> getUniqueCustomerBill(String cid) throws SQLException, ClassNotFoundException {
        ArrayList<CustomerBillTm> list = new ArrayList();
        ResultSet resultSet = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT o.date, d.breakfast,d.lunch, d.dinner,d.price FROM Orders o JOIN `Order Detail` d ON o.oid=d.oid Where o.cid='" + cid + "'").executeQuery();
        while (resultSet.next()) {
            list.add(
                    new CustomerBillTm(
                            resultSet.getString(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getInt(4),
                            resultSet.getDouble(5)
                    )
            );
        }
        return list;
    }

    private void loadCustomerBill(ArrayList<CustomerBillTm> customerBill) {
        tblCustomerBills.getItems().clear();
        customerBill.forEach(e -> {
            obList.add(new CustomerBillTm(e.getDate(), e.getBreakfast(), e.getLunch(), e.getDinner(), e.getPrice()));
        });
        tblCustomerBills.setItems(obList);
    }

    private ArrayList<CustomerBillTm> getCustomerBill() throws SQLException, ClassNotFoundException {
        ArrayList<CustomerBillTm> list = new ArrayList();
        ResultSet resultSet = DbConnection.getInstance().getConnection().
                prepareStatement("SELECT o.date, d.breakfast,d.lunch, d.dinner,d.price FROM Orders o JOIN `Order Detail` d ON o.oid=d.oid").executeQuery();
        while (resultSet.next()) {
            list.add(
                    new CustomerBillTm(
                            resultSet.getString(1),
                            resultSet.getInt(2),
                            resultSet.getInt(3),
                            resultSet.getInt(4),
                            resultSet.getDouble(5)
                    )
            );
        }
        return list;
    }

    private void loadAllCustomers(ArrayList<Customer> allCustomers) {
        allCustomers.forEach(e -> {
            CustomerObList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomernames.setItems(CustomerObList);
    }

    private ArrayList<Customer> getAllCustomers() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer");
        ResultSet rst = stm.executeQuery();
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

    private void loadDentaCustomers(ArrayList<Customer> dentaCustomers) {
        tblCustomernames.getItems().clear();
        dentaCustomers.forEach(e -> {
            CustomerObList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomernames.setItems(CustomerObList);
    }

    private ArrayList<Customer> getDentaCustomers() throws SQLException, ClassNotFoundException {
        String temp1 = "DENTA";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "'");
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

    private void loadNinjaCustomers(ArrayList<Customer> ninjaCustomers) {
        tblCustomernames.getItems().clear();
        ninjaCustomers.forEach(e -> {
            CustomerObList.add(new Customer(e.getCid(), e.getCustomerName(), e.getAddress(), e.getFactory(), e.getContact(), e.getGender(), e.getSid()));
        });
        tblCustomernames.setItems(CustomerObList);
    }

    private ArrayList<Customer> getNinjaCustomers() throws SQLException, ClassNotFoundException {
        String temp1 = "NINJA";
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Customer Where factory='" + temp1 + "'");
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

    public void PrintBillOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load(this.getClass().getResourceAsStream("/view/reports/CustomerBill.jrxml"));
            JasperReport compileReport = JasperCompileManager.compileReport(design);
            ObservableList<CustomerBillTm> items = tblCustomerBills.getItems();
            String customername=lblCustomerName.getText();
            String total=lblTotalCost.getText();

            HashMap map = new HashMap();
            map.put("CustomerName", customername);// id= param name of report // customerID= input value of text field
            map.put("Total", total);

            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, map, new JRBeanArrayDataSource(items.toArray()));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
