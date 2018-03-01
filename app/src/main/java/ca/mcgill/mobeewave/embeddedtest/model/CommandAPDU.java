package ca.mcgill.mobeewave.embeddedtest.model;

import java.security.InvalidParameterException;
import java.util.InputMismatchException;

/**
 * This is the Command APDU class for the embedded test.
 * Note: I will assume as described in the document that a APDU cannot be larger than 250 bytes.
 * However, the 3 bytes Lc and Le will still be implemented since they can range from 1-65535
 * which includes the 250 byte limit.
 * @author Christos Panaritis
 */
public class CommandAPDU {
    private byte data[];
    private byte apdu[];

    /**
     * This is the constructor for Case 1 command APDU (only includes the header).
     * @param cla 1st byte of the header
     * @param ins 2nd byte of the header
     * @param p1 3rd byte of the header
     * @param p2 4th byte of the header (last)
     */
    public CommandAPDU(byte cla, byte ins, byte p1, byte p2){
       this(cla, ins, p1, p2, (short)0, (short)0, null, (short)-1);
    }

    /**
     * This is the constructor for Case 2 command APDU (included header and Le).
     * @param le Le indicates the number of response bytes. Without Lc this must
     *           be 3 bytes long and the first byte must be 0.
     */
    public CommandAPDU(byte cla, byte ins, byte p1, byte p2, short le){
        this(cla, ins, p1, p2, (short)0, (short)0, null, le);
    }

    /**
     * This is the constructor for Case 3 command APDU (header, Lc and data).
     * @param lc Lc indicates the number of data bytes to follow. In our case,
     *           this is limited to 1-245 bytes. (in reality it can have much more)
     * @param lcByteSize Indicates the size of Lc. Since 1-245 can be expressed in both
     *                   1 and 3 bytes this is necessary to tell the two apart.
     * @param data Data passed.
     */
    public CommandAPDU(byte cla, byte ins, byte p1, byte p2, short lc, short lcByteSize, byte data[]){
        this(cla,ins,p1,p2,lc,lcByteSize, data, (short)-1);
    }

    /**
     * This is the constructor for Case 4 command APDU (header, Lc, data, Le)
     * It is also the general constructor for all the other cases.
     */
    public CommandAPDU(byte cla, byte ins, byte p1, byte p2, short lc, short lcByteSize, byte data[], short le){
        //Setting the attributes of the object
        this.data = data;
        short leSize;
        if(lcByteSize>3 || lcByteSize<0) {
            throw new InvalidParameterException();
        }
        //Case 4. Lc is one byte long
        else if((lcByteSize == 1) && (le >= 0)) {
            this.apdu = new byte[4 + 2 * lcByteSize + data.length];
            leSize = 1;
            this.apdu[4] = (byte) (lc & 0xff);
            this.apdu[apdu.length-1] = (byte) (le & 0xff);

            //Place the data in the APDU byte array
            for (short i = 0; i + 4 + lcByteSize < data.length - leSize; i++) {
                this.apdu[i + 4 + lcByteSize] = data[i];
            }
        }
        //Case 4. Lc is 3 bytes long
        else if((lcByteSize == 3) && (le >= 0)) {
            this.apdu = new byte[4 + lcByteSize + data.length + 2];
            leSize = 2;
            this.apdu[4] = 0;
            this.apdu[6] = (byte) (lc & 0xff);
            this.apdu[5] = (byte) ((lc >> 8) & 0xff);
            //Since Le can be represented in 2 bytes (never the case in a valid APDU in our scenario)
            this.apdu[apdu.length-1] = (byte) (le & 0xff); //We clear the upper values
            this.apdu[apdu.length-2] = (byte) ((le >>> 8) & 0xff); //We need to shift the upper values of Le into 8 bits.

            //Place the data in the APDU byte array
            for (short i = 0; i + 4 + lcByteSize < data.length - leSize; i++) {
                this.apdu[i + 4 + lcByteSize] = data[i];
            }
        }
        //Case 3.
        else if(((lcByteSize == 1) || lcByteSize == 3) && (le == -1)){
            this.apdu = new byte[4+lcByteSize+data.length];

            if(lcByteSize == 1) {   //Lc is one byte
                this.apdu[4] = (byte) (lc & 0xff);
            }
            else {  //Lc is 3 bytes
                this.apdu[4] = 0;
                this.apdu[6] = (byte) (lc & 0xff);  //Same reasoning as before
                this.apdu[5] = (byte) ((lc >>> 8) & 0xff);
            }

            //Put the data in the APDU
            for(int i=0; i+4+lcByteSize<data.length; i++){
                this.apdu[i+4+lcByteSize] = data[i];
            }
        }
        //Case 2
        else if((le >= 0) && (lcByteSize == 0)){
            this.apdu = new byte[7];
            //Le is put into the array
            this.apdu[4] = 0;
            this.apdu[6] = (byte) (le & 0xff);
            this.apdu[5] = (byte) ((le >>> 8) & 0xff);
        }
        else {
            this.apdu = new byte[4]; //Only header present
        }
        //Header values (CLA, INS, P1, P2) are put into the byte array
        this.apdu[0] = cla;
        this.apdu[1] = ins;
        this.apdu[2] = p1;
        this.apdu[3] = p2;
    }

    /**
     * Constructor used when passing a byte array to be made into a command APDU.
     * @param array The byte array being passed into the constructor.
     */
    public CommandAPDU(byte array[]){
        this.apdu = array;
        //Case 1 and 2 are done here since they hold no data
        if(array.length > 7) {
            //Lc of 3 bytes
            if(array[4] == 0){
                short lcUpper = (short) (array[5] << 8);
                short lcLower = (short) (array[6]);
                short lc = (short) (lcUpper + lcLower);
                this.data = new byte[lc];
                for(short i = 0; i<lc; i++){
                    this.data[i] = array[7+i]; //Data
                }
            }
            //Lc of 1 byte
            else {
                short lc = (short) (array[4]);
                this.data = new byte[lc];
                for(short i = 0; i<lc; i++){
                    this.data[i] = array[5+i];
                }
            }
        }
    }

    /**
     * This method is used to check the validity of the APDU and which type of APDU it is.
     * @param apdu The command APDU being tested.
     * @return Returns which type of APDU it is (i.e. case 1, 2, 3 or 4). If it is not valid
     *          then it returns -1 indicating the APDU is invalid.
     */
    public static short isValid(byte[] apdu){
        //We are only considering APDU length of 250 bytes at most.
        if(apdu.length < 251) {
            //Case 1 is trivial
            if(apdu.length == 4){
                return 1;
            }
            //Case 2 is also trivial
            else if(apdu.length == 7){
                return 2;
            }

            //Case 3 and 4
            else if(apdu.length > 4) {
                if (apdu[4] != 0) { //i.e. Lc is one byte
                    short lc = (short) (apdu[4]);
                    //If it reaches the end of the array then there is no Le (Case 3)
                    if(lc+5 == apdu.length){
                        return 3;
                    }
                    //If not then check the last byte (Le) and make sure it is equivalent to Lc (Case 4)
                    else if((lc+6 == apdu.length) && (lc == (short)(apdu[apdu.length-1]))) {
                        return 4;
                    }
                }
                else {
                    if(apdu[4] == 0) { //i.e. Lc is 3 bytes where the first one must be 0
                        //Since we only consider a max of 250, Lc cannot contain any bits in the second byte
                        short lc = (short) (apdu[6]);
                        if (lc + 7 == apdu.length) {    //Case 3
                            return 3;
                        } else if (lc + 9 == apdu.length) { //Case 4
                            if(apdu[apdu.length - 2] == 0) {
                                short leUpper = (short) (apdu[apdu.length - 2]<< 8);
                                short leLower = (short) (apdu[apdu.length - 1]);
                                short le = (short) (leUpper + leLower);
                                if (lc == le) {
                                    return 4;
                                }
                            }
                        }
                    }
                }
            }
        }
        return -1;  //Return -1 when the APDU is invalid.
    }

    /**
     * Getter for the data in the APDU.
     * @return A byte array containing only the APDU's data.
     */
    public byte[] getData(){
        return this.data;
    }

    /**
     * Getter for the whole APDU.
     * @return A byte array containing the whole command APDU.
     */
    public byte[] getApdu(){
        return this.apdu;
    }
}
