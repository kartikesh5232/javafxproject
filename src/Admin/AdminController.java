package Admin;

import dbUtil.dbConnection;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TextField id;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField email;

    @FXML
    private DatePicker dob;

    @FXML
    private Button loaddata;

    @FXML
    private AnchorPane anchorpane;


    @FXML
    private TableView<StudentData> studenttable;

    @FXML
    private TableColumn<StudentData,String> idcolumn;
    @FXML
    private TableColumn<StudentData, String> firstnamecolumn;
    @FXML
    private TableColumn<StudentData, String> lastnamecolumn;
    @FXML
    private TableColumn<StudentData, String> emailcolumn;
    @FXML
    private TableColumn<StudentData, String> dobcolumn;





    public static String databasename;

    public static void setdatabsename(String name) {
        databasename = name;
    }









    private dbConnection db;

    private ObservableList<StudentData> data;

    private String sql = "SELECT * FROM " + databasename;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.db = new dbConnection();

    }






    @FXML
    private void loadStudentData(ActionEvent event) throws SQLException {
        try {
            Connection connection = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            ResultSet rs = connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                this.data.add(new StudentData(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        } catch (SQLException e) {
            System.err.println("error" + e);
        }

        this.idcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("id"));
        this.firstnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("firstname"));
        this.lastnamecolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("lastname"));
        this.emailcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("email"));
        this.dobcolumn.setCellValueFactory(new PropertyValueFactory<StudentData, String>("dob"));


        this.studenttable.setItems(null);
        this.studenttable.setItems(this.data);







        this.studenttable.setRowFactory(new Callback<TableView<StudentData>, TableRow<StudentData>>() {
            @Override
            public TableRow<StudentData> call(TableView<StudentData> param) {

                final TableRow<StudentData> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem menuItem = new MenuItem("Delete");
                menuItem.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        deleteItemFromDatabase(row.getItem().idProperty().get());
                    }

                });
                row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getClickCount()==2 && (!row.isEmpty())){
                            showAtendanceDialogue(row.getItem().idProperty().get());
                        }
                    }
                });
                contextMenu.getItems().add(menuItem);
                row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
                return row;
            }
        });



    }






    public void deleteItemFromDatabase(String ID) {
        try {

            Connection connection = dbConnection.getConnection();
            String sqlstudents="DELETE FROM students WHERE id = '"+ID+"' ";
            String sqldata="DELETE FROM data WHERE id = '"+ID+"' ";
            String sqldelete = "DELETE FROM "+databasename+" WHERE id = '"+ID+"' ";


            connection.createStatement().execute(sqldelete);
            connection.createStatement().execute(sqldata);
            connection.createStatement().execute(sqlstudents);

            loaddata.fire();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    @FXML
    private void addStudentData(ActionEvent event) {


        String sqlinsert = "INSERT INTO " + databasename + "(id,fname,lname,email,DOB) VALUES (?,?,?,?,?)";
        String sqlstudents = "INSERT INTO students (id,dob) VALUES (?,?)";

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pr = connection.prepareStatement(sqlinsert);
            PreparedStatement pr1 = connection.prepareStatement(sqlstudents);

            pr.setString(1, this.id.getText());
            pr.setString(2, this.firstname.getText());
            pr.setString(3, this.lastname.getText());
            pr.setString(4, this.email.getText());
            pr.setString(5, this.dob.getEditor().getText());


            pr1.setString(1,id.getText() );
            pr1.setString(2,dob.getEditor().getText() );


            pr.execute();
            pr1.execute();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public void showAtendanceDialogue(String id)
    {
        Dialog<ButtonType> dialog=new Dialog<>();
        dialog.initOwner(this.anchorpane.getScene().getWindow());
        FXMLLoader fxmlLoader=new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogue.fxml"));
        try
        {

            dialog.getDialogPane().setContent(fxmlLoader.load());

        }
        catch (IOException e)
        {
         e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result=dialog.showAndWait();
        if(result.isPresent()&&result.get()==ButtonType.OK)
        {
         dialogueControler controler=fxmlLoader.getController();
         controler.addData(id);
        }
        else
        {
            System.out.println("data is not added");
        }

    }


    @FXML
    private void clearField(ActionEvent event) {
        this.id.setText("");
        this.firstname.setText("");
        this.lastname.setText("");
        this.email.setText("");
        this.dob.setValue(null);

    }
}
