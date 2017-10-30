import java.util.List;
import java.util.Arrays;
import java.lang.String;
import javax.smartcardio.*;

public class HelloFidesmoTest {
   // don't forget to set the HELLO_FIDESMO_APPID environment variable with the AppID
   // assigned by Fidesmo to your app in https://developer.fidesmo.com
   public static String applicationId = System.getenv().get("HELLO_FIDESMO_APPID");
   final private static String aidPrefix = "A00000061700";
   final private static String aidSuffix = "0101"; 

   public static void main(String[] args) {
      try {
         // Display the list of terminals
         TerminalFactory factory = TerminalFactory.getDefault();
         List<CardTerminal> terminals = factory.terminals().list();
         System.out.println("Terminals: " + terminals);

         // Use the last terminal in the list (most likely to be the contactless one)
         CardTerminal terminal = terminals.get(terminals.size() - 1);

         // Connect to the card
         Card card = terminal.connect("*");
         CardChannel channel = card.getBasicChannel();

         // Build the AID and send "Select Applet" command
         byte[] aid = decodeHex(aidPrefix + applicationId + aidSuffix);  
         ResponseAPDU answer = channel.transmit(new CommandAPDU(0x00, 0xA4, 0x04, 0x00, aid));
         System.out.println("Answer to SELECT APDU: " + answer.toString());
         byte r[] = answer.getData();
         for (int i=0; i<r.length; i++)
            System.out.print((char)r[i]);
         System.out.println();

         // Disconnect the card
         card.disconnect(false);
        } catch(Exception e) {
           System.out.println("Ouch: " + e.toString());
        }
   }
   
   // function to convert Hex strings into byte arrays
   private static byte[] decodeHex(String hexString) { 
      	char[] hexArray = "0123456789ABCDEF".toCharArray();
      	if ((hexString.length() & 0x01) != 0) {
              throw new IllegalArgumentException("Odd number of characters.");
        }
        char[] hexChars = hexString.toUpperCase().toCharArray();
        byte[] result = new byte[hexChars.length / 2];
        for (int i = 0; i < hexChars.length; i += 2) {
            result[i / 2] = (byte) (Arrays.binarySearch(hexArray, hexChars[i]) * 16 +
                                    Arrays.binarySearch(hexArray, hexChars[i + 1]));
        }
        return result;
   }
}