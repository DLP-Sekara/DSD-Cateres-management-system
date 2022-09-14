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
import model.Employee;
import model.Supplier;
import util.Validation;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EmployerSettingFormController implements Initializable {
    public TextField txtEmployeeNIC;
    public Button saveButton;
    public TableView<Employee> tblEmployee;
    public TableColumn colEmployeeNIC;
    public TableColumn colEmployeeName;
    public TableColumn colEmployeeAddress;
    public TableColumn colEmployeeContact;
    public TableColumn colEmployeeSalary;
    public TextField txtEmployeeName;
    public TextField txtEmployeeAddress;
    public TextField txtEmployeeContact;
    public TextField txtEmployeeSalary;
    public Button updateButton;

    Object SelectedRowForRemove = -1;
    ObservableList<Employee> obList = FXCollections.observableArrayList();

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    Pattern nicPattern = Pattern.compile("^[0-9]{9}[VX]$");
    Pattern namePattern = Pattern.compile("^[A-z ]{3,20}$");
    Pattern addressPattern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    Pattern contactPattern = Pattern.compile("^[0-9 ]{10}$");
    Pattern salaryPattern = Pattern.compile("^[0-9. ]{2,10}$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveButton.setDisable(true);
        updateButton.setDisable(true);
        storeValidations();

        try {
            colEmployeeNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
            colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
            colEmployeeAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colEmployeeContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            colEmployeeSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
            loadEmployeeTbl(getAllEmployee());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                SelectedRowForRemove = newValue;
                txtEmployeeNIC.setText(newValue.getNic());
                txtEmployeeName.setText(newValue.getEmployeeName());
                txtEmployeeAddress.setText(newValue.getAddress());
                txtEmployeeContact.setText(newValue.getContact());
                txtEmployeeSalary.setText(String.valueOf(newValue.getSalary()));
            }
        });
    }

    private void loadEmployeeTbl(ArrayList<Employee> allEmployee) {
        allEmployee.forEach(e -> {
            obList.add(new Employee(e.getNic(), e.getEmployeeName(), e.getAddress(), e.getContact(), e.getSalary()));
        });
        tblEmployee.setItems(obList);
    }

    private ArrayList<Employee> getAllEmployee() throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Employee");
        ResultSet rst = stm.executeQuery();
        //ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        ArrayList<Employee> employees = new ArrayList<>();

        while (rst.next()) {
            employees.add(new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)
            ));
        }
        return employees;
    }

    private void storeValidations() {
        map.put(txtEmployeeNIC, nicPattern);
        map.put(txtEmployeeName, namePattern);
        map.put(txtEmployeeAddress, addressPattern);
        map.put(txtEmployeeContact, contactPattern);
        map.put(txtEmployeeSalary, salaryPattern);
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        Object response = Validation.validateEmployee(map, saveButton,updateButton);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (response instanceof TextField) {
                TextField errorText = (TextField) response;
                errorText.requestFocus();
            } else if (response instanceof Boolean) {
                //new Alert(Alert.AlertType.INFORMATION, "Aded").showAndWait();
            }
        }
    }

    public void saveEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Employee e1=new Employee(txtEmployeeNIC.getText(),txtEmployeeName.getText(),txtEmployeeAddress.getText(),txtEmployeeContact.getText(),Double.parseDouble(txtEmployeeSalary.getText()));

        if (checkNIC(txtEmployeeNIC.getText())) {
            new Alert(Alert.AlertType.WARNING, "Employee already exists..").show();
        } else {
            if (saveEmployee(e1)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved..");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    this.tblEmployee.refresh();
                    tblEmployee.getItems().clear();
                    loadEmployeeTbl(getAllEmployee());
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Try Again..").show();
            }
        }
    }

    private boolean saveEmployee(Employee e1) throws SQLException, ClassNotFoundException {
        Connection con = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO Employee VALUES(?,?,?,?,?)";
        PreparedStatement stm = con.prepareStatement(query);
        stm.setObject(1, e1.getNic());
        stm.setObject(2, e1.getEmployeeName());
        stm.setObject(3, e1.getAddress());
        stm.setObject(4, e1.getContact());
        stm.setObject(5, e1.getSalary());
        //System.out.println("end");
        return stm.executeUpdate() > 0;
    }

    private boolean checkNIC(String text) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Employee WHERE nic=?");
        stm.setObject(1, text);
        ResultSet rst = stm.executeQuery();
        if (rst.next()) {
            new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)
            );
            return true;
        } else {
            return false;
        }
    }

    public void deleteEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (deleteEmployee(txtEmployeeNIC.getText())) {
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

    private boolean deleteEmployee(String text) throws SQLException, ClassNotFoundException {
        return DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Employee WHERE nic='" + text + "'").executeUpdate() > 0;
    }

    public void updateEmployeeOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Employee e1 = new Employee(txtEmployeeNIC.getText(), txtEmployeeName.getText(), txtEmployeeAddress.getText(), txtEmployeeContact.getText(), Double.parseDouble(txtEmployeeSalary.getText()));
        if (updateEmployee(e1)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated..");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                this.tblEmployee.refresh();
                tblEmployee.getItems().clear();
                loadEmployeeTbl(getAllEmployee());
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again").show();
        }
    }

    private boolean updateEmployee(Employee e1) throws SQLException, ClassNotFoundException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Employee SET employeeName=?, address=?, contact=?,salary=? Where nic='" + e1.getNic() + "'");
        //stm.setObject(1, d1.getDriverId());
        stm.setObject(1, e1.getEmployeeName());
        stm.setObject(2, e1.getAddress());
        stm.setObject(3, e1.getContact());
        stm.setObject(4, e1.getSalary());
        return stm.executeUpdate() > 0;
    }


    public void saveTransportOnAction(ActionEvent actionEvent) {
    }

    public void deleteTransportOnAction(ActionEvent actionEvent) {
    }

    public void updateTransportOnAction(ActionEvent actionEvent) {
    }
}
