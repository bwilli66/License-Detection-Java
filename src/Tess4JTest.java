import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * Created by BradWilliams on 1/23/17.
 */
public class Tess4JTest {

    public static void main(String[] args){

        File imageFile = new File("/Users/BradWilliams/ComputerVisionOut/rectMatCropped.png");
        ITesseract tessInstance = new Tesseract();
        tessInstance.setDatapath("/Users/BradWilliams/Downloads/Tess4J");
        tessInstance.setLanguage("eng");

        try {
            String result = tessInstance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
