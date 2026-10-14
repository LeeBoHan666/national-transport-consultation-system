import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Login extends Application {
    public static Stage LoginStage;

    @Override
    public void start(Stage stage) throws Exception {

        VBox LoginPane = new VBox(20);
        Scene LoginScene = new Scene(LoginPane,350,350);
        LoginPane.setAlignment(Pos.CENTER);

        Label LoginLabel = new Label("欢迎使用全国交通智能查询系统");
        LoginLabel.setStyle("-fx-font-size:20;-fx-text-fill: #00adb5;-fx-font-weight: bold;");

        TextField UsernameField = new TextField();
        UsernameField.setPromptText("用户名");
        UsernameField.setPrefSize(200, 40);
        UsernameField.setMaxWidth(250);
        UsernameField.setStyle("-fx-font-size: 15;");

        PasswordField PasswordField = new PasswordField();
        PasswordField.setPromptText("密码");
        PasswordField.setPrefSize(200, 40);
        PasswordField.setMaxWidth(250);
        PasswordField.setStyle("-fx-font-size: 15;");

        TextField CodeField = new TextField();
        CodeField.setPrefSize(200, 40);
        CodeField.setMaxWidth(250);
        CodeField.setStyle("-fx-font-size: 15;");
        CodeField.setPromptText("验证码");

        Label CodeLabel = new Label(Main.getCode()+" = ?");
        CodeLabel.setStyle("-fx-font-size: 12;-fx-text-fill: dimgrey;");


        Button LoginButton = new Button("登录");
        LoginButton.setPrefSize(250, 40);
        LoginButton.setStyle("-fx-font-size: 15;-fx-text-fill: white;-fx-background-color: #00adb5;");

        VBox CodeBox = new VBox(4,CodeField,CodeLabel);
        CodeBox.setAlignment(Pos.CENTER);
        LoginPane.getChildren().addAll(LoginLabel,UsernameField,PasswordField,CodeBox, LoginButton);


        LoginButton.setOnAction(e -> {
            try {
                
                if(Main.MatchCode(CodeField.getText())) {

                    if(UsernameField.getText().isEmpty() || PasswordField.getText().isEmpty()) {
                        Alert EmptyField = new Alert(Alert.AlertType.ERROR);
                        EmptyField.setTitle("错误");
                        EmptyField.setHeaderText(null);
                        EmptyField.setContentText("用户名和密码不得为空！");
                        EmptyField.showAndWait();
                    }
                        else{

                            if (SQL.LoginCheck(UsernameField.getText(), PasswordField.getText())) {
                                stage.close();
                                new GUI().start(new Stage());
                            }
                                else{

                                    Alert MisMatchAlert = new Alert(Alert.AlertType.ERROR);
                                    MisMatchAlert.setTitle("错误");
                                    MisMatchAlert.setHeaderText(null);
                                    MisMatchAlert.setContentText("用户名或密码错误");
                                    MisMatchAlert.showAndWait();

                                   }
                         }
                }

                else{
                    Alert CodeAlert = new Alert(Alert.AlertType.ERROR);
                    CodeAlert.setTitle("错误");
                    CodeAlert.setHeaderText(null);
                    CodeAlert.setContentText("验证码错误");
                    CodeAlert.showAndWait();
                    CodeField.clear();
                    CodeLabel.setText(Main.getCode()+" = ?");
                }
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });


        stage.setScene(LoginScene);
        stage.show();
    }

}
