import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * This is a simple UDP client application that takes 4/5 arguments from command line,
 * sends a packet to the server and prints the server response. More info on the format
 * and validity of the arguments can be found in the README.md file
*/
public class MapClient {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		String hostname = args[0];
		int portNumber = Integer.parseInt(args[1]);
		String cmd = args[2]+":";
		String firstArg = args.length > 3 ? args[3] : "";
		String secondArg = args.length>4 ? ":"+args[4] : "";
		String command = cmd+firstArg+secondArg;
		DatagramSocket socket = new DatagramSocket();
		byte [] buf = command.getBytes();
		InetAddress host = InetAddress.getByName(hostname);
		DatagramPacket oPkt = new DatagramPacket(buf, buf.length,host,portNumber);
		socket.send(oPkt);
		byte [] buffer = new byte[1000];
		DatagramPacket iPkt = new DatagramPacket(buffer,buffer.length);
		socket.receive(iPkt);
		System.out.println("Server reply: "+new String(iPkt.getData(),0,iPkt.getLength()));
		socket.close();
	}


}
