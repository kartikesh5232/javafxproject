package loginapp;

import Admin.AdminController;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import student.StudentsController;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class LoginController implements Initializable {


    LoginModel loginModel = new LoginModel();

    @FXML
    private Label dbstatus;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox<option> combobox;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginStatus;

    @FXML
    private Label signuplable;

    private boolean login = true;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (this.loginModel.isDatabaseConnected()) {
            this.dbstatus.setText("Connected to Database");
        } else {
            this.dbstatus.setText("Not Conneceted to Database");
        }

        this.combobox.setItems(FXCollections.observableArrayList(option.values()));


    }


    @FXML
    public void lableClicked() {
        if (login) {
            login = false;
            signuplable.setText("signIn");
            loginButton.setText("SignUp");


        } else {
            login = true;
            signuplable.setText("SignUp");
            loginButton.setText("SignIn");
        }

    }


    @FXML
    public void LoginSignup(ActionEvent event) {


        if (login) {
            try {


                if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((option) this.combobox.getValue()).toString())) {
                    AdminController.setdatabsename(this.username.getText()+this.password.getText());
                    StudentsController.setID(this.username.getText());
                    Stage stage = (Stage) this.loginButton.getScene().getWindow();
                    stage.close();
                    switch (((option) this.combobox.getValue()).toString()) {
                        case "Admin":
                            adminLogin();
                            break;
                        case "Student":
                            studentLogin();
                            break;

                    }

                } else {
                    loginStatus.setText("Wrong Credential");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            try {

                if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((option) this.combobox.getValue()).toString())) {
                    this.loginStatus.setText("already have account");
                } else {

                    try {


                        if (((option) this.combobox.getValue()).toString().equals("Admin")) {
                            this.loginModel.signUp(this.username.getText(), this.password.getText(), ((option) this.combobox.getValue()).toString());


                            try {
                                if (this.loginModel.isLogin(this.username.getText(), this.password.getText(), ((option) this.combobox.getValue()).toString())) {
                                    loginStatus.setText("signUp Succecessful");
                                    this.loginModel.createTable(this.username.getText(),this.password.getText());

                                } else {
                                    loginStatus.setText("signUp not complete");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            this.loginStatus.setText("Student cant Signup");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }















    public void studentLogin() {
        try {


            Stage userStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = (Pane) loader.load(getClass().getResource("/student/studentFXML.fxml").openStream());
            StudentsController studentsController = (StudentsController) loader.getController();
            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("Student Dashboard");
            userStage.setResizable(false);
            userStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void adminLogin() {

        try {
            Stage adminStage = new Stage();
            FXMLLoader adminloader = new FXMLLoader();
            Pane adminroot = (Pane) adminloader.load(getClass().getResource("/Admin/Admin.fxml").openStream());
            AdminController adminController = (AdminController) adminloader.getController();

            Scene scene = new Scene(adminroot);

            adminStage.setScene(scene);
            adminStage.setTitle("Admin Dashboard");
            adminStage.setResizable(false);
            adminStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
