# License-Detection-Java
License detection algorithm for a given photo. Not general purpose. 

# Program Specification

* Identify license plate region from picture  
* Read characters on license plate and convert to text  
* Compare against database and return relevant information about the car in the picture  

# Assumptions

* Standard blue on white California license plate 
* Single car in photo
  * Single license plate recognition 
* Use pre-written computer vision libraries  

# Program Design – 3 Steps  

* Image Pre-processing  
  * Finding the license plate  
* OCR (Optical Character Recognition)  
* Database Lookup 

# Language of Choice - Java

* Reasons for use  
  * Experience with language  
  * Pre-written libraries for computer vision 
* Drawbacks 
  * Syntax is verbose 
    * Makes high-level algorithm design harder
  * Limited support for OCR (Optical Character Recognition 

# Libraries Used

* JDBC 
  * Java Database Connector driver for MySQL 
* OpenCV 
  * Computer vision library 
* Tess4J 
  * Java wrapper library for Tesseract OCR engine (written in C++) 
    
# License Plate Detection Algorithm

1. Convert to Grayscale.
2. Gaussian Blur with 3x3 or 5x5 filter.
3. Apply Sobel Filter to find vertical edges.
4. Threshold the resultant image to get a binary image.
5. Apply a morphological close operation using suitable structuring element.
6. Find contours of the resulting image.
7. Find minAreaRect of each contour. Select rectangles based on aspect ratio, minimum/maximum area, and angle of rotation.
8. Clip the detected rectangular portion from the image after threshold and apply OCR.


This algorithm was derived from this source: https://stackoverflow.com/questions/37302098/image-preprocessing-with-opencv-before-doing-character-recognition-tesseract


