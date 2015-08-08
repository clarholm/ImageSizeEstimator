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
import java.text.DecimalFormat;
import java.util.*;



/**
 *
 * @author Jens
 * This program lets the user input data about images in text and the program will
 * calculate the storage space required to store the described images.
 */
public class ImageSizeEstimator {
static ArrayList<ArrayList<String>> userInputList = new ArrayList<>();
static ArrayList<ImageGroup> imageStackList = new ArrayList<>();
static HashMap<Integer, Image> images = new HashMap<>();
static long totalSize = 0;
//static ArrayList<Image> images  = new ArrayList<>();    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    printWelcomeText();
    collectUserInput();
    }

    static void printWelcomeText(){
    System.out.println("Image storage estimator by Jens Clarholm.\n"
            + "Enter info about either an image or an image group on each line.\n"
            + "An image is defined by entering image type followed by image width and height in the format \"type width height\" E.g.\"J 2048 1024\" \n"
            + "Supported image formats are JPEG (J or JPG), JPEG2000 (JP2 or JPEG2000) or bitmap (BMP).\n"
            + "An image group is defined by the letter G followed by the line numbers of the images that should be a part of the group."
            + " E.g. \"G i i\"\n"
            + "End input and display result with \"Q\". Exit program with EXIT");
    }
    
    
    static void collectUserInput(){
    System.out.println("Enter image and group info or EXIT to end program: ");
    Scanner userInput = new Scanner( System.in );
    
    //Split each line that is beeing enetered by the user into individual words and store each word in an ArrayList called userInputRow
    //Add each input as an element in a second ArrayList called userInputList.
    //If Q is detected stop reading input and parse the collected data.
    while (userInput.hasNext() == true){
        String current = userInput.nextLine();
        ArrayList<String> userInputRow  = new ArrayList<>();
        userInputRow.addAll(Arrays.asList(current.split(" ")));
        userInputList.add(userInputRow);
        if ("Q".equals(userInputRow.get(0)) || "q".equals(userInputRow.get(0))){
            processUserInputRowList();
        }
        else if ("EXIT".equals(userInputRow.get(0))){
                System.exit(0);
                        }
    }
    }
   
   static void processUserInputRowList(){
       //Extract one row of user input from the userInputList and send it to processing.
       for (int i = 0; i<userInputList.size();i++){
        ArrayList<String> tempRowArrayList = userInputList.get(i);
        //+1 to account for user not starting to calculate at row 0.
         processUserInputRow(i, tempRowArrayList);
       }
   }
   /** processUserInputRow(int row, ArrayList<String> currentRowInput) 
    * Metod processes the input from a row. It checks the format of the input data
    * and process it.
    * 
    * @param row			The number of the line that is 
    * @param currentRowInput	An array list containg the user input for a row as strings 
    */
    static void processUserInputRow(int row, ArrayList<String> currentRowInput) {
        //Check if row starts with G to indicate an image stack.
        if (currentRowInput.get(0).equals("g") || currentRowInput.get(0).equals("G")) {

            //check that the image stack contains at least two images
            if (currentRowInput.size() < 3) {
                System.out.println("Error: An image stack has to contain at least two images. Please try again.");
                clearAllAndStartOver();
            }
            //check that the inital G is followed by integers only start at 1 since first element in list is G.
            for (int i = 1; i < currentRowInput.size(); i++) {
                if (tryParseInt(currentRowInput.get(i)) == false) {
                    System.out.println("Error: An image stack has to be defined by integers only. Please try again.");
                    clearAllAndStartOver();
                }
                //check that the image stack is defined by only possitive integers.
                if (verifyOnlyPossitiveIntegers(currentRowInput.get(i)) == false) {
                    System.out.println("Error: Only possitive integers above 0 can be used to define a stack. Please try again.");
                    clearAllAndStartOver();
                }
            }

            //Format for creating an imageGroups is fullfilled, create an image group object.
            ImageGroup currentImageGroup = new ImageGroup(imageStackList.size(), currentRowInput);
            //check that all images in defined in the created image stack arent already a part of an image stack
            //step throug all image stacks currently defined and verify that every image in the new stack
            //is not already defined in another stack.
            for (int n = 0; n < imageStackList.size(); n++) {
                ImageGroup currentImageStackInImageStackList = imageStackList.get(n);
                //compare each entry in the current list to all entrys in the list beeing created.
                for (int i = 0; i < currentImageStackInImageStackList.getImageGroupSize(); i++) {
                    for (int j = 0; j < currentImageGroup.getImageGroupSize(); j++) {
                        if (Objects.equals(currentImageStackInImageStackList.getImagesInGroup().get(i), currentImageGroup.getImagesInGroup().get(j))) {
                            System.out.println("Error: An image can only belong to one group at the time. Image nr " + (currentImageStackInImageStackList.getImagesInGroup().get(i)+1) + " is already a part of image stack " + (n+1) + ". Please start again.");
                            clearAllAndStartOver();
                        }
                    }
                }
            }
            //Add image stack to imageStack list since no images were already present in other stacks,
            imageStackList.add(currentImageGroup);

        } //Check if line starts with and image indicator.
        else if (currentRowInput.get(0).equals("J") || currentRowInput.get(0).equals("JPG") || currentRowInput.get(0).equals("JPEG2000") || currentRowInput.get(0).equals("JP2") || currentRowInput.get(0).equals("BMP")) {
            //Check that nr of elements in row is three and also that first is string and followed by two strings that can be parsed as integers.
            if (currentRowInput.size() == 3 && tryParseInt(currentRowInput.get(1)) && verifyOnlyPossitiveIntegers(currentRowInput.get(1)) && tryParseInt(currentRowInput.get(2)) && verifyOnlyPossitiveIntegers(currentRowInput.get(2))) {
                //Create an image object based on user input, the line number is used as uniqe identifier for the image.
                Image currentImage = new Image(row, currentRowInput.get(0), parseInt(currentRowInput.get(1)), parseInt(currentRowInput.get(2)));
                //Add the image object to an hashmap with image line number as key.
                images.put(currentImage.getLineNumber(), currentImage);

            } else {
                System.out.println("Error: An image must be defined by image type folowed by two possitive integers.");
                clearAllAndStartOver();
            }

        } else if (currentRowInput.get(0).equals("Q") || currentRowInput.get(0).equals("q")) {
            //If Q is detected sum up and calculate total size.
            calculateTotalSize();
        } else {
            System.out.println("Error: Unknown input format please start again");
            clearAllAndStartOver();
        }
    }
        
static void calculateTotalSize(){
        
        /*Go though every image group first, add the size of all images and then calculate total size
        after applying group compression. After an image has been added to the total grop size it is
        removed from the image hash map. If an image is defined in a group does not exist in the images
        hash map it the user is prompted to start over.*/
    
        imageStackList.stream().forEach((ig) -> {
        ArrayList<Integer> currentImageStackInImageStackList = ig.getImagesInGroup();
        long stackSizeUncompressed = 0;
        int noOfImagesInStack=0;
        for(int i = 0; i< ig.getImageGroupSize();i++){
        int imageInStack = currentImageStackInImageStackList.get(i);
        if(images.get(imageInStack) != null){
        stackSizeUncompressed = stackSizeUncompressed + images.get(imageInStack).getImageSize();
        noOfImagesInStack++;
        images.remove(imageInStack);
        }
        else {
        //The imageInStack+1 is to compensate for user not starting row numbering on line 0.
        System.out.println("Error: No image defined at row " + (imageInStack+1) + ". Please try again.");  
        clearAllAndStartOver();
        }
        
        }
        totalSize = totalSize + sizeStackCompression(stackSizeUncompressed,noOfImagesInStack);

    });
        /* After all images that belongs to image groups has been removed the image size of all
        remaining images is added to the total size by itterating over all remaining entrys in the hash map.*/
        Set set = images.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) { 
        Map.Entry mentry = (Map.Entry)iterator.next();
        Image imageInHashMap = (Image)mentry.getValue();
        totalSize = totalSize + imageInHashMap.getImageSize();
      }
        System.out.println("Total size: " + formatResult(totalSize) + " bytes");
        clearAllAndStartOver();
 }
 
static void clearAllAndStartOver(){
        totalSize = 0;
        imageStackList.clear();
        userInputList.clear();
        images.clear();
        collectUserInput();
        }

static boolean tryParseInt(String value)
        //Helper method that checks if a string can be parsed as an integer.
{  
     try  
     {  
         Integer.parseInt(value);  
         return true;  
      } catch(NumberFormatException e)  
      {  
          return false;  
      }  
}

static boolean verifyOnlyPossitiveIntegers(String s){
    //Helper method checks if a string can can be parsed into a positive integer.
    if (tryParseInt(s)){
    
    int numberToCheck = parseInt(s);
        return numberToCheck > 0;
    }
    return false;
}

static long sizeStackCompression(long uncompressedStackSize, int numberOfImagesInStack){
    //Method used to calculate the extra compression that an image stack provides.
    //formula to calculate compression: "(total storage size of images) / ln(number of base level images in stack + 3)",
 double compressedStackSize = uncompressedStackSize/(Math.log((double)numberOfImagesInStack+3));
 return truncate(compressedStackSize);
}

static long truncate(double nonTruncated){
    //Helper method used to truncate to whole bits.
long truncated = (long)Math.round(nonTruncated - 0.5f);
return truncated;
}

static String formatResult(long valueToFormat){
//Helper method to format size to contain spaces between each three digits.
DecimalFormat myFormatter = new DecimalFormat("###,###.###");
//myFormatter.setGroupingSize(3);
String output = myFormatter.format(valueToFormat);
return output;
}
}
