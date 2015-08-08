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

import static java.lang.Integer.parseInt;
import java.util.ArrayList;

/**
 *
 * @author Jens
 */



public class ImageGroup {
    private int imageGroupNumber;
    private ArrayList<Integer> imagesInGroup = new ArrayList<>();

public ImageGroup(int imageGroupNumber, ArrayList<String> currentRow) {
        //Create image group and parse image numbers from string to int and store in an integer array list.
        this.imageGroupNumber= imageGroupNumber;
        for (int i = 1; i<currentRow.size(); i++){ 
            try{
                //-1 is to compensate for user not starting row count at 0.
                imagesInGroup.add((parseInt(currentRow.get(i))-1));
            }catch(NumberFormatException e){
                System.out.println("Error: Image indexes has to be integers, please try again");
                ImageSizeEstimator.clearAllAndStartOver();
            }
        

        }

}

 public int getImageGroupNumber() {
    return this.imageGroupNumber;
  }
  public int getImageGroupSize() {
      int imageGroupSize = imagesInGroup.size();
    return imageGroupSize;
  }
 
  public ArrayList<Integer> getImagesInGroup() {
    return this.imagesInGroup;
  }
}