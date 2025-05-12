package BankingSystem;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner sc;

    public Accounts(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }


    public long openAccount(String email){
        if(!account_exist(email)){
            String open_Acc_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Enter full name: ");
            String full_name = sc.nextLine();
            System.out.print("Enter initial amount: ");
            double balance = sc.nextDouble();
//            sc.nextLine();
            System.out.print("Enter security pin: ");
            int security_pin = sc.nextInt();
            try{
                long account_number = generateAccountNumber() ;
                PreparedStatement preparedStatement = connection.prepareStatement(open_Acc_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setInt(5, security_pin);
                int roesAffected = preparedStatement.executeUpdate();
                if(roesAffected > 0){
                    return account_number;
                } else{
                    throw new RuntimeException("OOPs! something went wrong while creating your account.\nPlease try again.");
                }

            } catch(SQLException e){
                System.out.println("Exception: "+e.getMessage());
            }
        }
        throw new RuntimeException("Account already exist");
    }

    public long getAccount_number(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        throw new RuntimeException("Account number doesn't exists!!");
    }
    public long generateAccountNumber(){
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
            if(resultSet.next()){
                long last_acc_no = resultSet.getLong("account_number");
                return last_acc_no+1;
            } else{
                return 10000100;
            }
        } catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        return 10000100;
    }
    public boolean account_exist(String email){
        String query = "SELECT * FROM accounts WHERE email =?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            } else{
                return  false;
            }
        } catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        return false;
    }
}
