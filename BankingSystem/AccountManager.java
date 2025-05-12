package BankingSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner sc;

    public AccountManager(Connection connection, Scanner sc){
        this.connection = connection;
        this.sc = sc;
    }
    public void debit_money(long account_number) throws SQLException{
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
//        sc.nextLine();
        System.out.print("Enter security pin: ");
        int security_pin = sc.nextInt();
        try{
            connection.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setInt(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double curr_balance = resultSet.getDouble("balance");
                    if(amount <= curr_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? where account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if(rowsAffected > 0){
                            System.out.println("Rs."+amount+" debited successfully from account: "+account_number);
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else{
                            System.out.println("Transaction failed!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else{
                        System.out.println("Sorry, you don't have sufficient balance in your account.");
                    }
                } else{
                    System.out.println("Invalid security pin.");
                }
            }
        } catch (SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
//        sc.nextLine();
        System.out.print("Enter security pin: ");
        int security_pin = sc.nextInt();
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setInt(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Rs."+amount+" credited successfully.");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else{
                        System.out.println("Transaction failed!!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }

                } else{
                    System.out.println("Invalid security pin.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_acc_no) throws SQLException{
        sc.nextLine();
        System.out.print("Enter Receiver's account number: ");
        Long receiver_acc_no = sc.nextLong();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        System.out.print("Enter Security pin: ");
        int security_pin = sc.nextInt();
        try{
            connection.setAutoCommit(false);
            if(sender_acc_no != 0 && receiver_acc_no != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1, sender_acc_no);
                preparedStatement.setInt(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double curr_balance = resultSet.getDouble("balance");
                    if(amount <= curr_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);
                        PreparedStatement creditpreparedStatement = connection.prepareStatement(credit_query);
                        debitpreparedStatement.setDouble(1, amount);
                        debitpreparedStatement.setLong(2, sender_acc_no);
                        creditpreparedStatement.setDouble(1, amount);
                        creditpreparedStatement.setLong(2, receiver_acc_no);
                        int rowsAffected1 = debitpreparedStatement.executeUpdate();
                        int rowsAffected2 = creditpreparedStatement.executeUpdate();
                        if(rowsAffected1 > 0 && rowsAffected2 > 0){
                            System.out.println("Transaction Successful!!");
                            System.out.println("Rs."+amount+" successfully transferred from "+sender_acc_no+" to "+receiver_acc_no+".");
                            connection.commit();
                            connection.setAutoCommit(true);
                        } else{
                            System.out.println("Transaction failed!!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else{
                        System.out.println("Sorry, you don't have sufficient amount in your account.");
                    }
                } else{
                    System.out.println("Invalid Security Pin.");
                }
            } else{
                System.out.println("Invalid account number.");
            }
        } catch (SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number){
        sc.nextLine();
        System.out.print("Enter your security pin: ");
        int security_pin = sc.nextInt();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? and security_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setInt(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("You've Rs."+balance+" in your account.");
            } else{
                System.out.println("Invalid security pin.");
            }
        } catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
    }
}
