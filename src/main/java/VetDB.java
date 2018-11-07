import javax.naming.directory.SearchControls;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.Statement;

import static input.InputUtils.*;

public class VetDB {
    private static final String url = "jdbc:sqlite:/Users/Tony/IdeaProjects/myDBThing/vet.sqlite";
    public static void main(String[] args) throws SQLException {

int choice;
do {
    choice = intInput("Enter 1 to add a dog, 2 to search for a dog, 3 to update vax, 4 to delete a dog, 5 to quit");

    if (choice==1){
        addDog();
    }else if (choice==2){
        searchDog();
    }else if (choice==3){
        updateVax();
    }else if (choice==4){
        deleteDog();
    }else if (choice==5){
        System.out.println("Goodbye");
    }


}while (choice != 5);
    }



private static void addDog(){

    final String addSql = "insert into dogs (name,age,weight,vax) values (?,?,?,?)";

    try (Connection connection = DriverManager.getConnection(url);
    PreparedStatement ps = connection.prepareStatement(addSql)){

        String name = stringInput("Enter a dog name ");
        int age = intInput("Enter a dogs age: ");
        double weight = doubleInput("Enter a dogs weight");
        boolean vax = yesNoInput("Is the dog vaccinated? ");

        ps.setString(1,name);
        ps.setInt(2,age);
        ps.setDouble(3,weight);
        ps.setBoolean(4,vax);
        ps.execute();

    }catch (SQLException e){
        System.out.println("Error adding a new dog.");
        System.out.println(e);
    }
}
private static void searchDog(){
        final String searchSql = "select * from dogs where name like ?";
        try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement searchStatement = connection.prepareStatement(searchSql)){

            String searchName = stringInput("Enter name to search for");

            searchStatement.setString(1,searchName);
            ResultSet dogsRs = searchStatement.executeQuery();

            if (!dogsRs.isBeforeFirst()){
                System.out.println("Sorry no dogs found");
            }
            else {
                while (dogsRs.next()){
                    String name = dogsRs.getString("name");
                    int age = dogsRs.getInt("age");
                    double weight = dogsRs.getDouble("weight");
                    boolean vax = dogsRs.getBoolean("vax");
                    int id = dogsRs.getInt("ID");
                    System.out.printf("Name %s, age %d, weight %f, vaccinated %s\n",id,name,age,weight,vax);

                }
            }
        }catch(SQLException e ){
            System.out.println("Error searching for dog");
            System.out.println(e);
        }
}


private static void updateVax(){

        final String updateVaxSql = "update dogs set vax = ? where name like ?";

        try (Connection connection = DriverManager.getConnection(url);
        PreparedStatement ps = connection.prepareStatement(updateVaxSql)){

            String name = stringInput("enter dogs name");
            boolean vax = yesNoInput("Is this dog vaccinated?");

            ps.setBoolean(1,vax);
            ps.setString(2,name);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0){
                System.out.println("Sorry that dog was not found");
            }else {
                System.out.println("database updated ");
            }
        }catch (SQLException e ){
            System.out.println("An updating dog");
            System.out.println(e);
        }


}
private static void deleteDog(){
        final String deleteSql = "delete from dogs where name like ?";
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement ps = connection.prepareStatement(deleteSql)){

            String name = stringInput("Enter a dogs name: ");

            ps.setString(1,name);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 0){
                System.out.println("Sorry that dog was not found");
            }else {
                System.out.println();
        }

    }catch (SQLException e ){
            System.out.println("Error deleting dog");
            System.out.println(e);
        }



}


}