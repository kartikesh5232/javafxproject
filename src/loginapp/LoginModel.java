package loginapp;

import dbUtil.dbConnection;

import java.sql.*;

public class LoginModel {




    Connection connection;

    public LoginModel()
    {
        try
        {

            this.connection= dbConnection.getConnection();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        if (this.connection==null)
        {
            System.exit(1);
        }
    }






    public boolean isDatabaseConnected()
    {
        return this.connection!=null;
    }








    public void signUp(String user,String pass,String opt)
    {


        String sqlsignup = "INSERT INTO login(username,password,division) VALUES (?,?,?)";
        try
        {

            PreparedStatement pr=this.connection.prepareStatement(sqlsignup);

            pr.setString(1, user);
            pr.setString(2, pass);
            pr.setString(3, opt);
            pr.execute();
            pr.close();



        }
        catch(SQLException e)
        {
            e.printStackTrace();

        }


    }












    public boolean isLogin(String user,String pass,String opt) throws Exception {
            PreparedStatement pr = null;
            ResultSet rs = null;

            String sql = "select * from login where username = ? and password = ? and division = ?";
            String studentsql="select * from students where id = ? and dob = ?";


            try {
                if (opt.equals("Admin")) {


                    pr = this.connection.prepareStatement(sql);
                    pr.setString(1, user);
                    pr.setString(2, pass);
                    pr.setString(3, opt);

                    rs = pr.executeQuery();

                    if (rs.next()) {
                        return true;
                    }
                    return false;

                }
                else
                {
                    pr=this.connection.prepareStatement(studentsql);
                    pr.setString(1, user);
                    pr.setString(2, pass);

                    rs = pr.executeQuery();

                    if (rs.next()) {
                        return true;
                    }
                    return false;
                }

            }catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (pr != null) {
                    try {
                        pr.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


    }




    public void createTable(String username,String password)

    {



        String createtablesql=" CREATE TABLE "+username+password+" (id TEXT, fname TEXT, lname TEXT, email TEXT, DOB   TEXT)";
        try {
            PreparedStatement pr = this.connection.prepareStatement(createtablesql);
            pr.execute();
            pr.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }


    }


}
