package util;

//import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;


public class Validation {

    public static Object validate(LinkedHashMap<TextField, Pattern> map, Button btn) {
        btn.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    //textFieldKey.getParent().setStyle("-fx-border-color: red");
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                   // textFieldKey.setStyle("-fx-text-fill: red ;");
                    //((AnchorPane) textFieldKey.getParent()).getChildren().get(1).setStyle("-fx-text-fill: red");
                }
                return textFieldKey;
            }
            //textFieldKey.getParent().setStyle("-fx-border-color: green");
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
            //((TextField) textFieldKey.getParent()).getChildren().get(1).setStyle("-fx-text-fill: green");
        }
        btn.setDisable(false);
        return true;
    }
    public static Object validateDriver(LinkedHashMap<TextField, Pattern> map, Button saveButton) {
        saveButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        saveButton.setDisable(false);
        return true;
    }
    public static Object validateSupplier(LinkedHashMap<TextField, Pattern> map, Button saveButton,Button updateButton) {
        saveButton.setDisable(true);
        updateButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        saveButton.setDisable(false);
        updateButton.setDisable(false);
        return true;
    }
    public static Object validateEmployee(LinkedHashMap<TextField, Pattern> map, Button saveButton, Button updateButton) {
        saveButton.setDisable(true);
        updateButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        saveButton.setDisable(false);
        updateButton.setDisable(false);
        return true;
    }
    public static Object validateMeal(LinkedHashMap<TextField, Pattern> map, Button addButton, Button updateButton) {
        addButton.setDisable(true);
        updateButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        addButton.setDisable(false);
        updateButton.setDisable(false);
        return true;
    }
    public static Object validateItem(LinkedHashMap<TextField, Pattern> map, Button addButton, Button updateButton) {
        addButton.setDisable(true);
        updateButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        addButton.setDisable(false);
        updateButton.setDisable(false);
        return true;
    }
    public static Object validateTransport(LinkedHashMap<TextField, Pattern> map, Button addButton, Button updateButton) {
        addButton.setDisable(true);
        updateButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        addButton.setDisable(false);
        updateButton.setDisable(false);
        return true;
    }
    public static Object validateOrderCount(LinkedHashMap<TextField, Pattern> map, Button placeOrderButton, Button updateButton) {
        placeOrderButton.setDisable(true);
        updateButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                }
                return textFieldKey;
            }
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
        }
        placeOrderButton.setDisable(false);
        updateButton.setDisable(false);
        return true;
    }

    public static Object validateAccount(LinkedHashMap<TextField, Pattern> map, Button submitButton) {
        submitButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    //textFieldKey.getParent().setStyle("-fx-border-color: red");
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                    // textFieldKey.setStyle("-fx-text-fill: red ;");
                    //((AnchorPane) textFieldKey.getParent()).getChildren().get(1).setStyle("-fx-text-fill: red");
                }
                return textFieldKey;
            }
            //textFieldKey.getParent().setStyle("-fx-border-color: green");
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
            //((TextField) textFieldKey.getParent()).getChildren().get(1).setStyle("-fx-text-fill: green");
        }
        submitButton.setDisable(false);
        return true;
    }

    public static Object validateLogin(LinkedHashMap<TextField, Pattern> map, Button signInButton) {
        signInButton.setDisable(true);
        for (TextField textFieldKey : map.keySet()) {
            Pattern patternValue = map.get(textFieldKey);
            if (!patternValue.matcher(textFieldKey.getText()).matches()) {
                if (!textFieldKey.getText().isEmpty()) {
                    //textFieldKey.getParent().setStyle("-fx-border-color: red");
                    textFieldKey.setStyle("-fx-border-color: red ;-fx-text-fill: red ; -fx-border-width: 1px ;");
                    // textFieldKey.setStyle("-fx-text-fill: red ;");
                    //((AnchorPane) textFieldKey.getParent()).getChildren().get(1).setStyle("-fx-text-fill: red");
                }
                return textFieldKey;
            }
            //textFieldKey.getParent().setStyle("-fx-border-color: green");
            textFieldKey.setStyle("-fx-border-color: #32ff7e ; -fx-border-width: 1px ;");
            //((TextField) textFieldKey.getParent()).getChildren().get(1).setStyle("-fx-text-fill: green");
        }
        signInButton.setDisable(false);
        return true;
    }
}
