import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by BradWilliams on 1/18/17.
 */
public final class CarProfGenerator {

    private CarProfGenerator(){}

    //example: 2007 Toyota Tacoma V6 4-door, VIN: D3H4JS74GH22GH87D, License Plate: DJ298TH, Manufactured In: Kenya, Age: 8 years old
    //create CarProf by calling all other methods
    public static CarProf generateCarProf(){
        CarProf result = new CarProf();
        int yearManufactured = getYear();

        // ------------ DEBUG ---------------
//        StringBuilder s = new StringBuilder();
//
//
//        s.append(yearManufactured + " ");
//        s.append(getMakeModelXML() + " ");
//        s.append(getEngine() + " ");
//        s.append(getBodyType() + ", ");
//        s.append("VIN: " + getVin() + ", ");
//        s.append("License Plate: " + getLicensePlate() + ", ");
//        s.append("Manufactured In: " + getManufacturedIn() + ", ");
//        s.append("Age: " + getAge(yearManufactured) + " years old");
//
//        System.out.println(s.toString());

        result.setLicensePlate(getLicensePlate());
        result.setVin(getVin());

        String[] makeModel = getMakeModelXML();

        result.setMake(makeModel[0]);
        result.setModel(makeModel[1]);
        result.setYear(yearManufactured);
        result.setBodyType(getBodyType());
        result.setEngine(getEngine());
        result.setManufacturedIn(getManufacturedIn());
        result.setAge(getAge(yearManufactured));



        return result;
    }

    //licensePlate
    //randomly generate
    private static String getLicensePlate(){
        return generateCharNum(7);
    }

    //vin
    //randomly generate
    private static String getVin(){
        return generateCharNum(17);
    }

    //make
    //model
    //xml file
    private static String[] getMakeModelXML(){

        /*
        XML structure

        <carlist>
            <car>
                <carname>Yugo</carname>
                <carmodellist>
                    <carmodel>55</carmodel>
                    <carmodel>Cabrio</carmodel>
                    <carmodel>GV</carmodel>
                </carmodellist>
            </car>
        </carlist>

         */

        Random r = new Random();

        String[] result = new String[2];

        //StringBuilder result = new StringBuilder();
        try {
            //Eventually refactor so it can be put in JAR
            File inputFile = new File("/Users/BradWilliams/Downloads/cars.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("car"); //get all cars (makes)


            //Select random make
            Node carMakeNode = nList.item(r.nextInt(nList.getLength())); // select one make


            //append make
            if (carMakeNode.getNodeType() == Node.ELEMENT_NODE) {
                Element carMakeElement = (Element) carMakeNode;
                //result.append(carMakeElement.getElementsByTagName("carname").item(0).getTextContent());
                result[0] = carMakeElement.getElementsByTagName("carname").item(0).getTextContent();

                Node carModelListNode = carMakeElement.getElementsByTagName("carmodellist").item(0);

                if(carModelListNode.getNodeType() == Node.ELEMENT_NODE){
                    Element carModelListElement = (Element) carModelListNode;

                    NodeList carModels = carModelListElement.getElementsByTagName("carmodel");

                    //result.append(" " + carModels.item(r.nextInt(carModels.getLength())).getTextContent());
                    result[1] = carModels.item(r.nextInt(carModels.getLength())).getTextContent();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //year
    //randomly generate
    private static int getYear(){
        Calendar cal = Calendar.getInstance();

        int thisYear = cal.get(Calendar.YEAR);
        int startYear = 1950;

        // Random year between startYear and this year
        return startYear + ((int) (Math.random() * (thisYear-startYear)));
    }

    //bodyType
    //two-door & four-door
    private static String getBodyType(){
        String[] bodyTypes = {"2-door", "4-door"};
        return bodyTypes[(int) (Math.random() * bodyTypes.length)];
    }

    //engine
    // 4-cylinder, V6, V8
    private static String getEngine(){
        String[] engineTypes = {"4-cylinder", "V6", "V8"};
        return engineTypes[(int) (Math.random() * engineTypes.length)];
    }

    //manufacturedIn
    //text file
    private static String getManufacturedIn(){

        int lineCount = 0;
        ArrayList<String> countries = new ArrayList<>();
        String stringTemp;
        try {
            BufferedReader in = new BufferedReader(new FileReader("/Users/BradWilliams/Downloads/countries.txt"));

            //Fill arraylist with countries and count how many
            while ((stringTemp = in.readLine()) != null) {

                countries.add(stringTemp.replace("\n",""));
                lineCount++;
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countries.get((int) (Math.random() * lineCount));

    }

    //age
    //randomly generate
    private static int getAge(int yearManufactured){
        Calendar cal = Calendar.getInstance();

        int thisYear = cal.get(Calendar.YEAR);

        // Random year between startYear and this year
        return thisYear - yearManufactured;
    }

    private static String generateCharNum(int num){
        Random random = new Random();
        String characters = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

        char[] text = new char[num];

        for (int i = 0; i < text.length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }

        return new String(text);
    }

    public static void main(String[] args){
        //testing

        System.out.println(CarProfGenerator.generateCarProf().toString());
    }
}
