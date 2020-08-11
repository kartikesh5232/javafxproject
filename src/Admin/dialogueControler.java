package Admin;


import dbUtil.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class dialogueControler {

    @FXML
    private TextField sub1;

    @FXML
    private TextField sub2;


    public void addData(String id) {
        String sqldata = "INSERT INTO data (id,sub1,sub2) VALUES (?,?,?)";

        String sqldatadelete="DELETE FROM data WHERE id = '"+id+"' ";
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pr = connection.prepareStatement(sqldata);
            PreparedStatement pr1= connection.prepareStatement(sqldatadelete);
            pr1.execute();

            pr.setString(1, id);

            pr.setString(2, sub1.getText());
            pr.setString(3, sub2.getText());


            pr.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
