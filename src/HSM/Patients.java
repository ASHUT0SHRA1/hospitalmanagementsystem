package HSM;

import java.sql.*;
import java.util.Scanner;

public class Patients {
    private Connection connection ;
    private Scanner scanner;

    public Patients(Connection connection , Scanner scanner){
        this.connection = connection ;
        this.scanner = scanner;

    }

    public void addPatient(){
        System.out.println("Enter Patient Name : ");
        String name = scanner.next();
        System.out.println("Enter age : ");
        int age = scanner.nextInt();
        System.out.println("Enter Gender : ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name ,age , gender) values(? , ? , ? )";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            int affectedRow = preparedStatement.executeUpdate();
            if(affectedRow > 0 ){
                System.out.println("Patient added successfully");
            }
            else{
                System.out.println("Patient not added successfully");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void viewPatient(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(" Patients Data ");
            System.out.println("+----+-------+-----+--------+");
            System.out.println("| id | name  | age | gender |");
            System.out.println("+----+-------+-----+--------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id") ;
                String name = resultSet.getString("name") ;
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.println("|"+ id+ " |" + name + "   |" + age +  "  | " +  gender + "  |"  );
                System.out.println("+----+-------+-----+--------+");


            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }
    public void deletePatient(){
        System.out.println("enter patient id :");
        int id = scanner.nextInt() ;

        String query = "delete from patients where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected> 0){
                System.out.println("patient record deleted successfully");
            }
            else{
                System.out.println("Not deleted");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id){
        String query = "Select name,age,gender from patients where id = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return  true ;

            }else{
                return false;
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
