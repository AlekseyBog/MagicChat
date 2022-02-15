package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegController {

    @FXML
    public TextArea textArea;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField loginField;
    @FXML
    public TextField nicknameField;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void clickBtnReg(ActionEvent actionEvent) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String nickname = nicknameField.getText().trim();
        controller.tryToReg(login, password, nickname);
    }

    public void regStatus(String status) {
        if (status.equals("/reg_ok")) {
            textArea.appendText("Регистрация прошла успешно\n");
        } else {
            textArea.appendText("Введённые логин или никнейм уже используются\n");
        }
    }
}
