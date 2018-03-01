package ca.mcgill.ecse321.embeddedtest;

import org.junit.Test;

import java.util.Arrays;

import ca.mcgill.mobeewave.embeddedtest.model.CommandAPDU;

import static ca.mcgill.mobeewave.embeddedtest.model.CommandAPDU.isValid;
import static org.junit.Assert.*;

/**
 * Note: I did not test all constructors because I will not be using them and they are trivially true (except case 4).
 * @author Christos Panaritis
 */
public class ValidAPDUTest {
    @Test
    public void case3Valid() throws Exception {
        //Check if the method works from the example given (corresponds to case 3)
        byte cla = (byte)0x80;
        byte ins = (byte)0xE2;
        byte P1 = 0;
        byte P2 = 0;
        short lc = 0x13;
        short lcSize = 1;
        byte data[] = new byte[]{(byte)0x10,(byte)0x02,(byte)0x10,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,
                (byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0xFE,(byte)0xDC,(byte)0xBA,(byte)0x98,
                (byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10};
        CommandAPDU validAPDU = new CommandAPDU(cla,ins,P1,P2,lc,lcSize,data);

        assertEquals(3, isValid(validAPDU.getApdu()));
    }
    @Test
    public void case3Invalid() throws Exception {
        //Check if the method works from the example given (corresponds to case 3)
        byte cla = (byte)0x80;
        byte ins = (byte)0xE2;
        byte P1 = 0;
        byte P2 = 0;
        short badlc = 0x10;
        byte data[] = new byte[]{(byte)0x10,(byte)0x02,(byte)0x10,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,
                (byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0xFE,(byte)0xDC,(byte)0xBA,(byte)0x98,
                (byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10};
        CommandAPDU invalidAPDU = new CommandAPDU(cla,ins,P1,P2,badlc,(short)1,data);

        assertEquals(-1,isValid(invalidAPDU.getApdu()) );
    }
    @Test
    public void case3OtherConstructor() throws Exception {
        //Check if the byte constructor works in both cases
        byte apdu[] = new byte[]{(byte)0x80,(byte)0xE2, 0, 0, 0x13, (byte)0x10,(byte)0x02,(byte)0x10,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,
                (byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0xFE,(byte)0xDC,(byte)0xBA,(byte)0x98,
                (byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10};
        CommandAPDU testAPDU = new CommandAPDU(apdu);

        byte badApdu[] = new byte[]{(byte)0x80,(byte)0xE2, 0, 0, 0x10, (byte)0x10,(byte)0x02,(byte)0x10,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,
                (byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0xFE,(byte)0xDC,(byte)0xBA,(byte)0x98,
                (byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10};
        CommandAPDU testBadAPDU = new CommandAPDU(badApdu);

        assertEquals(3, isValid(testAPDU.getApdu()));
        assertEquals(-1, isValid(testBadAPDU.getApdu()));
    }
    @Test
    public void case1() throws Exception {
        //Check if case 1 is detected
        byte case1[] = new byte[]{(byte)0x80,(byte)0xE2, 0, 0};
        CommandAPDU case1APDU = new CommandAPDU(case1);

        assertEquals(1, isValid(case1APDU.getApdu()));
    }
    @Test
    public void case2() throws Exception {
        //Check if case 2 is detected
        byte case2[] = new byte[]{(byte)0x80,(byte)0xE2, 0, 0, 0, 0, 0x10};
        CommandAPDU case2APDU = new CommandAPDU(case2);

        assertEquals(2, isValid(case2APDU.getApdu()));
    }
    @Test
    public void case4Valid() throws Exception {
        //Check if case 4 is detected
        byte case4[] = new byte[]{(byte)0x80,(byte)0xE2, 0, 0, 0x13, (byte)0x10,(byte)0x02,(byte)0x10,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,
                (byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0xFE,(byte)0xDC,(byte)0xBA,(byte)0x98,
                (byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10, 0x13};
        CommandAPDU case4APDU = new CommandAPDU(case4);

        assertEquals(4, isValid(case4APDU.getApdu()));
    }
    @Test
    public void case4Invalid() throws Exception {
        //Check if case 4 is invalid
        byte case4[] = new byte[]{(byte)0x80,(byte)0xE2, 0, 0, 0x13, (byte)0x10,(byte)0x02,(byte)0x10,(byte)0x01,(byte)0x23,(byte)0x45,(byte)0x67,
                (byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF,(byte)0xFE,(byte)0xDC,(byte)0xBA,(byte)0x98,
                (byte)0x76,(byte)0x54,(byte)0x32,(byte)0x10, 0x10};
        CommandAPDU case4APDU = new CommandAPDU(case4);

        assertEquals(-1, isValid(case4APDU.getApdu()));
    }
}