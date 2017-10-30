import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import com.licel.jcardsim.base.Simulator;
import javacard.framework.AID;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class ExampleTest {

	Simulator simulator;

	static final byte[] appletAIDBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
	static final AID appletAID = new AID(appletAIDBytes, (short) 0, (byte) appletAIDBytes.length);
	static final Class exampleCardlet = com.fidesmo.javacard.example.ExampleCardlet.class;

	private static final byte[] expect = {(byte)'H',(byte)'e',(byte)'l',(byte)'l',(byte)'o',(byte)' ',(byte)'F',(byte)'i',(byte)'d',(byte)'e',(byte)'s',(byte)'m',(byte)'o',(byte)'!',(byte) 0x90,(byte) 0x00};

	@Before
	public void setup() {
		simulator = new Simulator();
		simulator.resetRuntime();
        	simulator.installApplet(appletAID, exampleCardlet);
        	simulator.selectApplet(appletAID);
	}

	@Test
	public void testSelect() {
		// 00A404000C A00000 061700 E26B8F12 0101
        	CommandAPDU commandAPDU = new CommandAPDU(0x00, 0x01, 0x00, 0x00);
        	ResponseAPDU response = new ResponseAPDU(simulator.transmitCommand(commandAPDU.getBytes()));
        	assertEquals(0x9000, response.getSW());
		assertArrayEquals("Hello Fidesmo!".getBytes(), response.getData());
	}

	@Test
	public void testHello() {
		byte[] command = new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00 } ;
		byte[] resp = simulator.transmitCommand(command);
		assertArrayEquals(expect, resp);

	}


}
