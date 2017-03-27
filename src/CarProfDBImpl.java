/**
 * Created by BradWilliams on 1/11/17.
 */
import java.sql.*;

public class CarProfDBImpl implements CarProfDB {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public CarProfDBImpl(){
        this.connection = null;
        this.statement = null;
        this.resultSet = null;
    }

    @Override
    public CarProf getCarProf(String licensePlate) {
        CarProf result = new CarProf();
        try {
            // Create a statement
            statement = this.connection.createStatement();

            try {
                //Get car profile info
                resultSet = statement.executeQuery("SELECT * FROM car_profiles WHERE licensePlate = '" + licensePlate + "'");

                while(resultSet.next()){
                    //Get car prof info, fill object
                    result.setLicensePlate(resultSet.getString("licensePlate"));
                    result.setVin(resultSet.getString("vin"));
                    result.setMake(resultSet.getString("make"));
                    result.setModel(resultSet.getString("model"));
                    result.setYear(resultSet.getInt("year"));
                    result.setBodyType(resultSet.getString("bodyType"));
                    result.setEngine(resultSet.getString("engine"));
                    result.setManufacturedIn(resultSet.getString("manufacturedIn"));
                    result.setAge(resultSet.getInt("age"));

                }

            }
            catch (SQLException e){}
        }
        catch (SQLException e){}
        finally{
            try{
                if(statement != null)
                    statement.close();
            } catch(SQLException e){}
        }
        return result;
    }

    @Override
    public void addCarProf(CarProf carProf) {

        //INSERT INTO table_name ( field1, field2,...fieldN ) VALUES ( value1, value2,...valueN )

        try {
            // Create a statement
            statement = this.connection.createStatement();

            try {
                //Get car profile info
                int updateSuccess = statement.executeUpdate("INSERT INTO car_profiles " +
                        "(licensePlate,vin,make,model,year,bodyType,engine,manufacturedIn,age) " +
                        "VALUES (" +
                        "'" + carProf.getLicensePlate() + "'," +
                        "'" + carProf.getVin() + "'," +
                        "'" + carProf.getMake() + "'," +
                        "'" + carProf.getModel() + "'," +
                        "'" + carProf.getYear() + "'," +
                        "'" + carProf.getBodyType() + "'," +
                        "'" + carProf.getEngine() + "'," +
                        "'" + carProf.getManufacturedIn() + "'," +
                        "'" + carProf.getAge() + "')"
                );

                //executeStatement() return number of columns affected
                if (updateSuccess == 0)
                    System.err.println("Insert Car Profile into \"car_profiles\" table FAILED");
                else
                    System.out.println("Insert Car Profile into \"car_profiles\" table SUCCESSFUL");

            }
            catch (SQLException e){}
        }
        catch (SQLException e){}
        finally{
            try{
                if(statement != null)
                    statement.close();
            } catch(SQLException e){}
        }

    }

    public void connect(String path, String username, String password){

        try {
            // Get connection
            this.connection = DriverManager.getConnection("jdbc:mysql://" + path + "?autoReconnect=true&useSSL=false", username, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            if(this.connection != null)
                this.connection.close();
        } catch(SQLException e){}
    }

    public static void main(String[] args){

        // Test

        CarProfDBImpl db = new CarProfDBImpl();

        db.connect("localhost:3306/computer_vision", "root","********");

        CarProf c = db.getCarProf("ZG16NXQ");

        System.out.println(c);
    }
}
