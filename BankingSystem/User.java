package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection connection;
    private Scanner sc;

    public User(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }

//    User registration
    public void register(){
        sc.nextLine();
        System.out.print("Enter your full name: ");
        String full_name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        if(user_exist(email)){
            System.out.println("User already exists with this email. Please register with new email.\n");
            return;
        }

        String register_query = "INSERT INTO user(full_name, email, password) VALUES (?, ?, ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            int affectedrows = preparedStatement.executeUpdate();
            if(affectedrows > 0){
                System.out.println("User registration successfully completed with name "+full_name);
            } else{
                System.out.println("OOPS! Registration filed.\nPlease try again.");
            }
        }
        catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
    }

//    User login (if user account already exist)
    public String login(){
        sc.nextLine();
        System.out.print("Enter your valid Email: ");
        String email = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try{
            PreparedStatement preparedStatement =connection.prepareStatement(login_query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return email;
            } else{
                return null;
            }
        }
        catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        return null;
    }

//    To prevent duplication (1 email => 1 user)
    public boolean user_exist(String email){
        String query = "SELECT * FROM user WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }

        }
        catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        return false;
    }
}
