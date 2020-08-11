package student;

import dbUtil.dbConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class StudentsController implements Initializable {


    public static String databaseid;

    public static void setID(String id)
    {
        databaseid=id;
    }

    Connection connection = null;
    ResultSet rs = null;

    @FXML
    private TextField sub1;


    @FXML
    private TextField sub2;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = dbConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getAttendance() {
        String sql = "SELECT * FROM data where id = '"+databaseid+"' ";


        try {
            rs = connection.createStatement().executeQuery(sql);

            while (rs.next())
            {
                sub1.setText(rs.getString(2));
                sub2.setText(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
