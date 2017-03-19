/**
 * Created by BradWilliams on 1/10/17.
 */
public class CarProf {
    private String licensePlate;
    private String vin;
    private String make;
    private String model;
    private int year;
    private String bodyType;
    private String engine;
    private String manufacturedIn;
    private int age;

    public CarProf() {

    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVin() {
        return vin;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public String getBodyType() {
        return bodyType;
    }

    public String getEngine() {
        return engine;
    }

    public String getManufacturedIn() {
        return manufacturedIn;
    }

    public int getAge() {
        return age;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public void setManufacturedIn(String manufacturedIn) {
        this.manufacturedIn = manufacturedIn;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();

        s.append(this.year + " ");
        s.append(this.make + " ");
        s.append(this.model + " ");
        s.append(this.engine + " ");
        s.append(this.bodyType + ", ");
        s.append("VIN: " + this.vin + ", ");
        s.append("License Plate: " + this.licensePlate + ", ");
        s.append("Manufactured In: " + this.manufacturedIn + ", ");
        s.append("Age: " + this.age + " years old");

        return s.toString();
    }
}
