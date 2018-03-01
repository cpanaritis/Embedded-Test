package ca.mcgill.ecse321.embeddedtest;

import org.junit.Test;

import ca.mcgill.mobeewave.embeddedtest.model.CommandAPDU;

import static ca.mcgill.mobeewave.embeddedtest.model.CommandAPDU.isValid;
import static ca.mcgill.mobeewave.embeddedtest.model.Encryption.decrypt;
import static ca.mcgill.mobeewave.embeddedtest.model.Encryption.encrypt;
import static org.junit.Assert.assertEquals;

/**
 * Note: This is not an exhaustive test. This is simply done to make sure the algorithm works (on obvious cases).
 * @author Christos Panaritis
 */
public class EncryptionTest {
    @Test
    public void singleByteEncryption() throws Exception {
        //Single byte test
        byte array[] = new byte[] {(byte)0x07};
        array = encrypt(array);
        assertEquals((byte)0xC1, array[0]);
    }
    @Test
    public void singleByteDecryption() throws Exception {
        //Single byte test
        byte array[] = new byte[] {(byte)0x38};
        array = decrypt(array);
        assertEquals((byte)0xE0, array[0]);
    }
    @Test
    public void TwoByteEncryption() throws Exception {
        //2 bytes test
        byte array2[] = new byte[] {(byte)0x07, (byte)0x07};
        array2 = encrypt(array2);
        assertEquals((byte)0xC1, array2[0]);
        assertEquals((byte)0xC1, array2[1]);
    }
    @Test
    public void TwoByteDecryption() throws Exception {
        //2 bytes test
        byte array2[] = new byte[] {(byte)0xE0, (byte)0xE0};
        array2 = decrypt(array2);
        assertEquals((byte)0x83, array2[0]);
        assertEquals((byte)0x83, array2[1]);
    }
    @Test
    public void ZerosEncryption() throws Exception {
        //All 0s
        byte zeros[] = new byte[] {0,0,0};
        zeros = encrypt(zeros);
        assertEquals(0, zeros[0]);
        assertEquals(0, zeros[1]);
        assertEquals(0, zeros[2]);
    }
    @Test
    public void ZerosDecryption() throws Exception {
        //All 0s
        byte zeros[] = new byte[] {0,0,0};
        zeros = decrypt(zeros);
        assertEquals(0, zeros[0]);
        assertEquals(0, zeros[1]);
        assertEquals(0, zeros[2]);
    }
    @Test
    public void OnesEncryption() throws Exception {
        //All 1s
        byte ones[] = new byte[] {(byte)0xFF,(byte)0xFF,(byte)0xFF};
        ones = encrypt(ones);
        assertEquals((byte)0xFF, ones[0]);
        assertEquals((byte)0xFF, ones[1]);
        assertEquals((byte)0xFF, ones[2]);
    }
    @Test
    public void OnesDecryption() throws Exception {
        //All 1s
        byte ones[] = new byte[] {(byte)0xFF,(byte)0xFF,(byte)0xFF};
        ones = decrypt(ones);
        assertEquals((byte)0xFF, ones[0]);
        assertEquals((byte)0xFF, ones[1]);
        assertEquals((byte)0xFF, ones[2]);
    }
}