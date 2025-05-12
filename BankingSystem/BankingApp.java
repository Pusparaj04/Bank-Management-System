package BankingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.Class.forName;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "UserUser";
    private  static final String password = "20-07-2005#myDoB";
    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            System.out.println("Exception: "+e.getMessage());
        }
        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(connection, sc);
            AccountManager accountManager = new AccountManager(connection, sc);
            Accounts accounts = new Accounts(connection, sc);

            String email;
            long account_number;
            System.out.println("***WELCOME TO BANKING SYSTEM***");
            while(true){

                System.out.println();
                System.out.println("1. Don't have account? Register now!!");
                System.out.println("2. Already have an account? Login now!!");
                System.out.println("3. Exit.");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if(email != null){
                            System.out.println();
                            System.out.println("User Logged in!!");
                            if(!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account.");
                                System.out.println("2. Exit");
                                if(sc.nextInt() == 1){
                                    account_number = accounts.openAccount(email);
                                    System.out.println("Congratulation!! you've successfully created your new Bank Account.");
                                    System.out.println("Your new account number is: "+account_number);
                                } else{
                                    break;
                                }
                            } else{
                                account_number = accounts.getAccount_number(email);
                                int choice2 = 0;
                                while (choice2 != 5){
                                    System.out.println();
                                    System.out.println("***Available Services***");
                                    System.out.println("1. Withdraw Money.");
                                    System.out.println("2. Deposit Money.");
                                    System.out.println("3. Transfer Money.");
                                    System.out.println("4. Check Balance.");
                                    System.out.println("5. Log out.");

                                    System.out.print("\nGet your desired service. Enter your choice:- ");
                                    choice2 = sc.nextInt();
                                    switch (choice2){
                                        case 1:
                                            accountManager.debit_money(account_number);
                                            break;
                                        case 2:
                                            accountManager.credit_money(account_number);
                                            break;
                                        case 3:
                                            accountManager.transfer_money(account_number);
                                            break;
                                        case 4:
                                            accountManager.getBalance(account_number);
                                            break;
                                        case 5:
                                            break;
                                        default:
                                            System.out.println("Please enter a valid choice.");
                                            break;
                                    }
                                }
                            }
                        } else{
                            System.out.println("OPPs!! Login failed.\nPlease enter a valid email.");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR USING THIS BANKING SYSTEM.");
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Please enter a valid choice.");
                        break;

                }
            }
        }
        catch(SQLException e){
            System.out.println("Exception: "+e.getMessage());
        }
    }
}
