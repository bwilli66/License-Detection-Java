# License-Detection-Java
License detection algorithm for a given photo. Not general purpose. 

# Program Specification

• Identify license plate region from picture  <br />
• Read characters on license plate and convert to text  <br />
• Compare against database and return relevant information about the car in the picture  <br />

# Assumptions

• Standard blue on white California license plate <br />
• Single car in photo  <br />
    ⋅⋅⋅• Single license plate recognition  <br />
• Use pre-written computer vision libraries  <br />

# Program Design – 3 Steps  

• Image Pre-processing  <br />
    ⋅⋅⋅• Finding the license plate  <br />
• OCR (Optical Character Recognition)  <br />
• Database Lookup  <br />

# Language of Choice - Java

• Reasons for use  <br />
    ⋅⋅⋅• Experience with language   <br />
    ⋅⋅⋅• Pre-written libraries for computer vision  <br />
• Drawbacks  <br />
    ⋅⋅⋅• Syntax is verbose  <br />
        ⋅⋅⋅ ⋅⋅⋅• Makes high-level algorithm design harder <br />
    ⋅⋅⋅• Limited support for OCR (Optical Character Recognition <br />

# Libraries Used

• JDBC <br />
    ⋅⋅⋅• Java Database Connector driver for MySQL <br />
• OpenCV <br />
   ⋅⋅⋅• Computer vision library  <br />
• Tess4J  <br />
    ⋅⋅⋅• Java wrapper library for Tesseract OCR engine (written in C++)  <br />
    
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
