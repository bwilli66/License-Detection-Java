import org.opencv.core.Core;

/**
 * Created by BradWilliams on 1/24/17.
 */
public class Main {

    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        new LicenseDetection().run();


    }
}
