package ca.mcgill.mobeewave.embeddedtest.model;

/**
 * This is the Encryption class for the embedded test.
 * Note: the arrays being passed in the methods, will be everything that needs to be encrypted
 * or decrypted. This means that they only accept the APDU data and not the whole APDU.
 * This can be done since, the APDU objects contain the data byte array attribute that
 * contains only the APDU data.
 * @author Christos Panaritis
 */
public class Encryption {
    /**
     * This is the encryption method used to encrypt data in the APDU. (View note above).
     * This does a 2 bit circular shift right.
     * @param array A byte array that contains the data to encrypt.
     * @return The new encrypted byte array.
     */
    public static byte[] encrypt(byte array[]){
        //We get the bits that will rotate to the front of the array.
        byte rightBits = 0;
        //Since every bit needs to be shifted we go through the whole array.
        for(short i=0; i<array.length; i++){
            byte temp = ((byte)((array[i] & 0x03)<<6));    //get the new 2 remainder bits to move to the next array
            array[i] = (byte)((array[i]>>>2)| rightBits);    //Add the previous remainder bits to the front after shift
            rightBits = temp;   //Make new remainder the old one
        }
        array[0] = (byte)(array[0] | rightBits);
        return array;
    }

    /**
     * This is the decryption method used to decrypt the data in the APDU. (View note)
     * This is the exact opposite of the encrypt method. i.e. circular shift left by 2.
     * @param array A byte array containing the data to decrypt.
     * @return The new decrypted byte array.
     */
    public static byte[] decrypt(byte array[]){
        byte leftBits = 0;
        //Same procedure as above but in the opposite direction
        for(short i=(short)(array.length-1); i>=0; i--){
            byte temp = (byte)((array[i] & 0xC0)>>>6);
            array[i] = (byte)((array[i]<<2)| leftBits);
            leftBits = temp;
        }
        //We get the bits that will rotate to the back of the array
        array[array.length-1] = (byte)(array[array.length-1]|leftBits);
        return array;
    }
}
