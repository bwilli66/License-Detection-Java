
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BradWilliams on 1/25/17.
 */
public class LicenseDetection {

    public void run() {

        // ------------------ set up tesseract for later use ------------------
        ITesseract tessInstance = new Tesseract();
        tessInstance.setDatapath("/Users/BradWilliams/Downloads/Tess4J");
        tessInstance.setLanguage("eng");



        // ------------------  Save image first ------------------
        Mat img;
        img = Imgcodecs.imread(getClass().getResource("/resources/car_2_shopped2.jpg").getPath());
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/True_Image.png", img);



        // ------------------ Convert to grayscale ------------------
        Mat imgGray = new Mat();
        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/Gray.png", imgGray);



        // ------------------ Blur so edge detection wont pick up noise ------------------
        Mat imgGaussianBlur = new Mat();
        Imgproc.GaussianBlur(imgGray, imgGaussianBlur, new Size(3, 3),0);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/gaussian_blur.png", imgGaussianBlur);





        // ****************** Create image that will be cropped at end of program before OCR ***************************

        // ------------------ Binary theshold for OCR (used later)------------------
        Mat imgThresholdOCR = new Mat();
        Imgproc.adaptiveThreshold(imgGaussianBlur, imgThresholdOCR,255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,7,10);
        //Imgproc.threshold(imgSobel,imgThreshold,120,255,Imgproc.THRESH_TOZERO);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgThresholdOCR.png", imgThresholdOCR);


        // ------------------ Erosion operation------------------
        Mat kern = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_CROSS, new Size(3, 3));
        Mat imgErodeOCR = new Mat();
        Imgproc.morphologyEx(imgThresholdOCR,imgErodeOCR,Imgproc.MORPH_DILATE,kern); //Imgproc.MORPH_DILATE is performing erosion, wtf?
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgErodeOCR.png", imgErodeOCR);


         //------------------ Dilation operation  ------------------
        Mat kernall = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3));
        Mat imgDilateOCR = new Mat();
        Imgproc.morphologyEx(imgErodeOCR,imgDilateOCR,Imgproc.MORPH_ERODE,kernall);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgDilateOCR.png", imgDilateOCR);

        // *************************************************************************************************************








//        // ------------------ Close operation (dilation followed by erosion) to reduce noise ------------------
//        Mat k = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3, 3));
//        Mat imgCloseOCR = new Mat();
//        Imgproc.morphologyEx(imgThresholdOCR,imgCloseOCR,1,k);
//        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgCloseOCR.png", imgCloseOCR);


        // ------------------ Sobel vertical edge detection ------------------
        Mat imgSobel = new Mat();
        Imgproc.Sobel(imgGaussianBlur,imgSobel,-1,1,0);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgSobel.png", imgSobel);



        // ------------------ Binary theshold ------------------
        Mat imgThreshold = new Mat();
        Imgproc.adaptiveThreshold(imgSobel, imgThreshold,255,Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY,99,-60);
        //Imgproc.threshold(imgSobel,imgThreshold,120,255,Imgproc.THRESH_TOZERO);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgThreshold.png", imgThreshold);


//        // ------------------ Open operation (erosion followed by dilation) ------------------
//        Mat ker = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_CROSS, new Size(3, 2));
//        Mat imgOpen = new Mat();
//        Imgproc.morphologyEx(imgThreshold,imgOpen,0,ker);
//        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgOpen.png", imgOpen);


        // ------------------ Close operation (dilation followed by erosion) to reduce noise ------------------
        Mat kernel = Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(22, 8));
        Mat imgClose = new Mat();
        Imgproc.morphologyEx(imgThreshold,imgClose,1,kernel);
        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgClose.png", imgClose);



        // ------------------ Find contours ------------------
        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.findContours(imgClose, contours, new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);





        // **************************** DEBUG CODE **************************

        Mat contourImg = new Mat(imgClose.size(),imgClose.type());
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(contourImg, contours, i, new Scalar(255, 255, 255), -1);
        }

        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/contours.png", contourImg);

        // ******************************************************************








        // --------------  Convert contours --------------------

        //Convert to MatOfPoint2f so that minAreaRect can be called
        List<MatOfPoint2f> newContours = new ArrayList<>();

        for(MatOfPoint mat : contours){

            MatOfPoint2f newPoint = new MatOfPoint2f(mat.toArray());
            newContours.add(newPoint);

        }


        //Get minAreaRects
        List<RotatedRect> minAreaRects = new ArrayList<>();

        for(MatOfPoint2f mat : newContours){

            RotatedRect rect = Imgproc.minAreaRect(mat);

            /*
             --------------- BUG WORK AROUND ------------

            Possible bug:
            When converting from MatOfPoint2f to RotatectRect the width height were reversed and the
            angle was -90 degrees from what it would be if the width and height were correct.

            When painting rectangle in image, the correct boxes were produced, but performing calculations on rect.angle
            rect.width, or rect.height yielded unwanted results.

            The following work around is buggy but works for my purpose
             */

            if(rect.size.width < rect.size.height){
                double temp;

                temp = rect.size.width;
                rect.size.width = rect.size.height;
                rect.size.height = temp;
                rect.angle = rect.angle + 90;


            }





            //check aspect ratio and area and angle
            if(rect.size.width/rect.size.height > 1 && rect.size.width/rect.size.height < 5
                    && rect.size.width * rect.size.height > 10000 && rect.size.width * rect.size.height < 50000
                    && Math.abs(rect.angle) < 20) {
                minAreaRects.add(rect);
            }


            //minAreaRects.add(rect);
        }








        // **************************** DEBUG CODE **************************
        /*
        The following code is used to draw the rectangles on top of the original image for debugging purposes
         */
        //Draw Rotated Rects
        Point[] vertices = new Point[4];

        Mat imageWithBoxes = img;

        // Draw color rectangles on top of binary contours
//        Mat imageWithBoxes = new Mat();
//        Mat temp = imgDilateOCR;
//        Imgproc.cvtColor(temp, imageWithBoxes, Imgproc.COLOR_GRAY2RGB);


        for(RotatedRect rect : minAreaRects) {

            rect.points(vertices);

            for (int i = 0; i < 4; i++) {
                Imgproc.line(imageWithBoxes, vertices[i], vertices[(i + 1) % 4], new Scalar(0, 0, 255), 2);
            }

        }

        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/imgWithBoxes.png", imageWithBoxes);

        // ******************************************************************








        // **************************** DEBUG CODE **************************
//        for(RotatedRect rect : minAreaRects) {
//            System.out.println(rect.toString());
//        }
        // ******************************************************************








        /*
        In order to rotate image without cropping it:

        1. Create new square image with dimension = diagonal of initial image.
        2. Draw initial image into the center of new image.
            • Insert initial image at ROI (Region of Interest) in new image
        3. Rotate new image
         */

        //Find diagonal/hypotenuse
        int hypotenuse = (int) Math.sqrt((img.rows() * img.rows()) + (img.cols() * img.cols()));

        //New Mat with hypotenuse as height and width
        Mat rotateSpace = new Mat(hypotenuse,hypotenuse,0);


        int ROI_x = (rotateSpace.width()-imgClose.width())/2; //x start of ROI
        int ROI_y = (rotateSpace.height()-imgClose.height())/2; //x start of ROI

        //designate region of interest
        Rect r = new Rect(ROI_x,ROI_y,imgClose.width(),imgClose.height());

        //Insert image into region of interest
        imgDilateOCR.copyTo(rotateSpace.submat(r));



        Mat rotatedTemp = new Mat(); //Mat to hold temporarily rotated mat
        Mat rectMat = new Mat();//Mat to hold rect contents (needed for looping through pixels)
        Point[] rectVertices = new Point[4];//Used to build rect to make ROI
        Rect rec = new Rect();

        List<RotatedRect> edgeDensityRects = new ArrayList<>(); //populate new arraylist with rects that satisfy edge density

        int count = 0;

        //Loop through Rotated Rects and find edge density
        for(RotatedRect rect : minAreaRects) {

            count++;

            rect.center = new Point((float) ROI_x + rect.center.x, (float) ROI_y + rect.center.y);

            //rotate image to math orientation of rotated rect
            rotate(rotateSpace,rotatedTemp,rect.center,rect.angle);

            //remove rect rotation
            rect.angle = 0;

            //get vertices from rotatedRect
            rect.points(rectVertices);

            // **************************** DEBUG CODE **************************
//
//            for (int k = 0; k < 4; k++) {
//                System.out.println(rectVertices[k]);
//                Imgproc.line(rotatedTemp, rectVertices[k], rectVertices[(k + 1) % 4], new Scalar(0, 0, 255), 2);
//            }
//
//            Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/rotated" + count + ".png", rotatedTemp);

            // *****************************************************************


            //build rect to use as ROI
            rec = new Rect(rectVertices[1],rectVertices[3]);

            rectMat = rotatedTemp.submat(rec);

            Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/extracted" + count + ".png", rectMat);

            //find edge density

//            // ------------------------ edge density check NOT IMPLEMENTED --------------------
//            /*
//            Checking for edge density was not necessary for this image so it was not implemented due to lack of time
//             */
//            for(int i = 0; i < rectMat.rows(); ++i){
//                for(int j = 0; j < rectMat.cols(); ++j){
//
//                  //add up white pixels
//                }
//            }
//
//            //check number of white pixels against total pixels
//            //only add rects to new arraylist that satisfy threshold

            edgeDensityRects.add(rect);
        }






        // **************************** DEBUG CODE **************************

        Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/rotatedSpace.png", rotateSpace);
        //Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/rotatedSpaceROTATED.png", rotatedTemp);

        //System.out.println(imgGray.type());

        // *****************************************************************







        // if there is only one rectangle left, its the license plate
        if(edgeDensityRects.size() == 1){

            String result = ""; //Hold result from OCR
            BufferedImage bimg;
            Mat cropped;

            cropped = rectMat.submat(new Rect(20,50,rectMat.width()-40,rectMat.height()-70));

            Imgcodecs.imwrite("/Users/BradWilliams/ComputerVisionOut/rectMatCropped.png", cropped);

            bimg = matToBufferedImage(cropped);

            BufferedImage image = bimg;

            try {
                result = tessInstance.doOCR(image);
            } catch (TesseractException e) {
                System.err.println(e.getMessage());
            }

            for( int i = 0; i < 10; ++i){

            }


            result = result.replace("\n", "");

            System.out.println(result);

            CarProfDBImpl db = new CarProfDBImpl();

            db.connect("localhost:3306/computer_vision", "root","*******");

            CarProf c = db.getCarProf(result);

            System.out.print(c.toString());

            db.close();

        }

    }





    public void rotate(Mat src, Mat dst, Point point, double angle){
        Mat M = Imgproc.getRotationMatrix2D(point, angle, 1.0);
        Imgproc.warpAffine(src, dst, M, src.size(), Imgproc.INTER_CUBIC);
    }

    public BufferedImage matToBufferedImage(Mat mat)
    {
        BufferedImage bimg;

        if ( mat != null ) {
            int cols = mat.cols();
            int rows = mat.rows();
            int elemSize = (int)mat.elemSize();
            byte[] data = new byte[cols * rows * elemSize];
            int type = BufferedImage.TYPE_BYTE_GRAY;
            mat.get(0, 0, data);

            bimg = new BufferedImage(cols, rows, type);

            bimg.getRaster().setDataElements(0, 0, cols, rows, data);
        } else { // mat was null
            bimg = null;
        }
        return bimg;
    }

}
