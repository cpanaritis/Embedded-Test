package ca.mcgill.mobeewave.embeddedtest.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ca.mcgill.ecse321.embeddedtest.R;
import ca.mcgill.mobeewave.embeddedtest.model.CommandAPDU;

import static ca.mcgill.mobeewave.embeddedtest.model.Encryption.decrypt;
import static ca.mcgill.mobeewave.embeddedtest.model.Encryption.encrypt;
import static ca.mcgill.mobeewave.embeddedtest.model.TLV.getValue;

/**
 * Android UI class for the test.
 * Note: this is purposusly left extremely simple because UI was not the main task (or a task at all) for this test.
 * @author Christos Panaritis
 */
public class MainActivity extends AppCompatActivity {
    private CommandAPDU apdu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create the byte stream we want to solve
        byte stream[] = new byte[]{(byte) 0x80, (byte) 0xE2, 0, 0, 0x0A, (byte) 0xaf, (byte) 0x82, 0x11,
                (byte) 0xdb, (byte) 0xdb, (byte) 0xd9, 0x08, 0x12, (byte) 0x9b, (byte) 0xd8};
        //Create the APDU object
        apdu = new CommandAPDU(stream);
    }

    public void displayAnswer(View view) {
        TextView displayTextBox = findViewById(R.id.answer_text);
        //Decrypt the APDU data
        byte decryptedData[] = decrypt(apdu.getData());
        //Parse the TLV and get the value
        decryptedData = getValue(decryptedData);
        //Convert the ASCII sequence to a string
        String answer = new String(decryptedData);
        //Display answer
        displayTextBox.setText(answer);
    }
}
