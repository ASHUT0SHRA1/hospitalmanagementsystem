package HSM;

import javax.print.Doc;
import java.sql.*;
import java.util.Scanner;

public class HosManSys {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userName = "root";
    private static final String password = "0909";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url , userName , password);
            Patients patient = new Patients(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
                System.out.println("Hospital Management System ");
                System.out.println("1.Add Patients ");
                System.out.println("2.View Patients");
                System.out.println("3.View Doctor");
                System.out.println("4.Book Appointment");
                System.out.println("5.delete Patient");
                System.out.println("6.exit");

                System.out.println("Enter Your choice : ");
                int choice = scanner.nextInt();
                switch (choice){
                    case 1 :
                        //add Patients
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2  :
                        patient.viewPatient();
                        System.out.println();
                        break;

                    case 3 :
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4 :
                        bookAppointment(patient , doctor ,  connection , scanner);
                        System.out.println();
                        break;

                    case 5 :
                        patient.deletePatient();
                        break;
                    case 6 :
                        return;
                    default:
                        System.out.println("Enter your choice ");
                }


            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public static void bookAppointment(Patients patients , Doctor doctor , Connection connection , Scanner scanner){
        System.out.println("Enter Patient id : ");
        int pid = scanner.nextInt();
        System.out.println("Enter Doctor id : ");
        int did = scanner.nextInt() ;

        System.out.println("Enter appointment date (YYYY - MM - DD)");
        String appointmentDate = scanner.next();
        if(patients.getPatientById(pid) && doctor.getDoctorById(did)){
            if(checkDoctorAvailablity(did ,  appointmentDate, connection)){
                String apppointmentQuery = "INSERT INTO Appointments(patient_id , doctor_id , date ) values(? ,? , ? )";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(apppointmentQuery);
                    preparedStatement.setInt(1,pid);
                    preparedStatement.setInt(2,did);
                    preparedStatement.setString(3,appointmentDate);
                    int rowAffected = preparedStatement.executeUpdate() ;
                    if(rowAffected > 0){
                        System.out.println("Appointment Booked");
                    }else{
                        System.out.println("failed to book appointment");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                System.out.println("Doctor not available on this date ");
            }
        }
        else{
            System.out.println("either doctor or patients does not exist");
        }
    }


    public static boolean checkDoctorAvailablity(int did, String appointmentDate, Connection connection){
        String query = "select count(*) from appointments where doctor_id = ? and appointmet_date = ? ";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,did);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0 ){
                    return true;
                }
                else{
                    return  false;
                }
            }

        }catch (Exception e ){
            System.out.println(e.getMessage());
        }
        return  false;

    }

}
