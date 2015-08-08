/*
 * The MIT License
 *
 * Copyright 2015 Jens.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package imagesizecalc;

/**
 * This class calculate the size of an image based on height, width and image type. 
 * What line the image had when entered is used as individual indentifier for each image.
 * @author Jens
 */
public class Image {
    private String type;
    private int hight;
    private int width;
    private long size;
    private int lineNumber;
    
    
    
public Image(int lineNumber, String imageType, int imageHeight, int imageWidth) {
        this.lineNumber = lineNumber;
        this.type = imageType;
        this.hight = imageHeight;
        this.width = imageWidth;    
        this.size = calculateSize();
       
        }
        


private long calculateSize(){
    //Method to calculate size of images based on image type.
    long totalSize = 0;
        switch (type) {
            case "J":
            case "JPG":
            {
                //JPG images has pyramid level images that needs to be added to total size.
                totalSize = sizeJpgCompression(hight,width);
                int pyramidHight = hight;
                int pyramidWidth = width;
                int jpgPyramidLvlTreshold = 128;
                while(pyramidHight/2 >= jpgPyramidLvlTreshold && pyramidWidth/2 >= jpgPyramidLvlTreshold ){
                    pyramidHight = pyramidHight/2;
                    pyramidWidth = pyramidWidth/2;
                    totalSize = totalSize + ImageSizeEstimator.truncate(sizeJpgCompression(pyramidHight,pyramidWidth));
                }                
                
                return totalSize;
            }       
            case "JP2":
            case "JPEG2000":
            {
                
                totalSize = sizeJpg2000Compression(hight,width);
                return totalSize;
                
            }
            case "BMP":
            {
                //BMP images has pyramid level images that needs to be added to total size.
                totalSize = sizeBMPCompression(hight,width);
                int pyramidHight = hight;
                int pyramidWidth = width;
                int bmpPyramidLvlTreshold = 128;
                while(pyramidHight/2 >= bmpPyramidLvlTreshold && pyramidWidth/2 >= bmpPyramidLvlTreshold ){
                    pyramidHight = pyramidHight/2;
                    pyramidWidth = pyramidWidth/2;
                    totalSize = totalSize + ImageSizeEstimator.truncate(sizeBMPCompression(pyramidHight,pyramidWidth));
                }
        

        return totalSize;
            }   
        }    
    return 0;
}

private long sizeJpgCompression(int imageHeight, int imageWidth){
//"width * height * 0.2",
double currentImageSize = (double)imageHeight*(double)imageWidth*0.2;
long calculatedSize = ImageSizeEstimator.truncate(currentImageSize);
return calculatedSize;
}
private long sizeJpg2000Compression(int imageHeight, int imageWidth){
//Size of jpg200 = "width * height * 0.4 / ln(ln(width * height + 16))",
double currentImageSize = ((double)imageHeight*(double)imageWidth*0.4)/(Math.log(Math.log(((double)imageWidth*(double)imageHeight+16))));
long calculatedSize = ImageSizeEstimator.truncate(currentImageSize);
return calculatedSize;
}

private long sizeBMPCompression(int imageHeight, int imageWidth){
double currentImageSize = ((double)imageHeight*(double)imageWidth);
long calculatedSize = ImageSizeEstimator.truncate(currentImageSize);
return calculatedSize;
}



 public long getImageSize() {
    return this.size;
  }
  public int getImageWidth() {
    return this.width;
  }
   public int getImageHeight() {
    return this.hight;
  }
 public String getImageType() {
    return this.type;
  }
 public int getLineNumber() {
    return this.lineNumber;
  }

}
