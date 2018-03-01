package ca.mcgill.mobeewave.embeddedtest.model;

import java.security.InvalidParameterException;

/**
 * This is the TLV class for the embedded system test.
 * Note: I will be assuming that the tag will not be larger than 1 byte.
 * Note2: that nested TLVs (like in the stream) will not be dealt with.
 * This means that it will all be put into the same TLV without necessarily knowing there is
 * a second tag in that TLV. This design choice is done since it is not necessary to handle
 * for the described problem.
 * Note3:
 * @author Christos Panaritis
 */
public class TLV {
    /**
     * This method is used to parse a TLV stream into seperate TLV arrays.
     * @param array A byte array stream of TLVs
     * @return A 2D array where each row ([this][0]) represents a new TLV.
     */
    public static byte[][] parseTLVStream(byte array[]){
        short numberOfTLV = 0;
        short incrementValue = 1;
        boolean streamDone = false;
        //Get the number of TLVs in the stream
        while(!(streamDone)){
            if((incrementValue-1) == (array.length)) {
                streamDone = true;
            }
            else {
                incrementValue = (short) (array[incrementValue] + incrementValue + 2);
                numberOfTLV++;
            }
        }
        byte TLVList[][] = new byte[numberOfTLV][];
        incrementValue = 0;
        short arrayPosition = 0;
        //Fill in the 2D array
        for(short i = 0; i < numberOfTLV; i++){
            incrementValue = (short) (array[arrayPosition+1] + 2);
            TLVList[i] = new byte[incrementValue];
            //Fills the rows
            for(short j = arrayPosition; j<incrementValue; j++){
                TLVList[i][j] = array[j];
            }
            arrayPosition = (short)(arrayPosition + incrementValue);
        }
        return TLVList;
    }

    /**
     * Get the value bytes from the TLV
     * @param array Takes in a byte array corresponding to a TLV.
     * @return Returns a byte array corresponding to the contents of the TLV
     */
    public static byte [] getValue(byte array[]){
        short lengthValue = (short)(array[1]);  //The length value's content is expressed in the byte after the tag (view note)
        byte valueArray[] = new byte[lengthValue];  //Byte array that will old the value section
        if(lengthValue > array.length) {
            throw new InvalidParameterException();  //Invalid TLV (wrong length provided)
        }
        else {
            for (short i = 0; i < lengthValue; i++) {
                valueArray[i] = array[2 + i];
            }
            return valueArray;
        }
    }

    /**
     * This method gets the tag present in the TLV and returns what it represents.
     * @param array A byte array that represents the TLV
     * @return An array of chars that gives information on the tag
     * [0] = Class name, [1] = P/C, [2-3] = tag number
     */
    public static char[] getTag(byte array[]) {
        char tag[] = new char[4];
        //Get the bits for the class name
        byte classValue = (byte)(array[0]&0xC0);
        if(classValue == 0){
            tag[0] = 'U';   //Universal
        }
        else if(classValue == 0x1){
            tag[0] = 'A';   //Application
        }
        else if(classValue == 0x2){
            tag[0] = 'C';   //Context Specific
        }
        else {
            tag[0] = 'P';   //Private
        }
        //Get the bit for the P/C
        classValue = (byte)(array[0]&0x40);
        if(classValue == 0){
            tag[1] = 'P';    //Primitive
        }
        else {
            tag[1] = 'C';    //Constructive
        }
        //Get the bits for the tag value
        classValue = (byte)(array[0]&0x1F);
        if(classValue < 0xA){
            tag[2] = 0;
            tag[3] = (char)classValue;
        }
        else {
            tag[2] = (char)((short)(classValue)/10);
            tag[3] = (char)((short)(classValue)%10);
        }
        return tag;
    }
}
