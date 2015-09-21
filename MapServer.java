
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *UDP server class. Keeps a HashMap of key value pairs according to what clients save and remove etc.
 * more info on the arguments and commands taken by the server can be found in the README.md file.
*/
public class MapServer {
	
	HashMap<String,String> database = new HashMap<String,String>();
	
	public static void main(String[] args) {
		int port = args.length!=0 ? Integer.parseInt(args[0]): 31357;
		try {
			MapServer server = new MapServer(port);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public MapServer(int port) throws IOException{
		DatagramSocket socket = new DatagramSocket(port);
		byte [] iBuf = new byte[1000];
		DatagramPacket iPkt = new DatagramPacket(iBuf,iBuf.length);
		while(true){
			socket.receive(iPkt);
			InetAddress iAdd = iPkt.getAddress();
			int iPort = iPkt.getPort();
			int iLength = iPkt.getLength();
			String rData = new String(iPkt.getData(),0,iLength);
			String[] input = parseInput(rData);
			String result = action(input[0],input[1],input[2]);
			byte [] oBuf = result.getBytes();
			DatagramPacket oPkt = new DatagramPacket(oBuf,oBuf.length,iAdd,iPort);
			socket.send(oPkt);
		}
	}
	
	/**
	 * Parses the input arrived from the client
	 * @param input String input coming from the client
	 * @return String[] a list of the command and all the given values
	 */
	public String[] parseInput(String input){
		String [] inputs = new String[3];
		String regEx = "(?<command>[^:]+):(?<value>[^:]+):*(?<value2>.+)*";
		Pattern cmd = Pattern.compile(regEx);
		Matcher match = cmd.matcher(input);
		if(match.find()){
			inputs[0] = match.group("command");
			inputs[1] = match.group("value");
			inputs[2] = match.group("value2");
			return inputs;
		}
		return inputs;
	}
	
	/**
	 * acts according to the client input (removes,swaps,puts etc )
	 * @param cmd String command given by client
	 * @param value String key or value given by client
	 * @param value2 String key or value given by client
	 * @return String successful or faulty termination of operation.
	 */
	public String action(String cmd,String value,String value2){
		if(cmd==null){return "error:unrecognizable input";}
		switch(cmd){
		case "get":
			if(value!=null && !value.equals("")){
				String result = get(value);
				if(result==null){return "no match";}
				return "success:"+result;
			}break;
		case "put":
			if(value!=null && !value.equals("") &&
			value2!=null && !value2.equals("")){ 	
				String result = put(value,value2);
				return "updated:"+result;
			}break;
		case "swap":
			if(value!=null && !value.equals("") 
			&& value2!=null && !value2.equals("")){ 	
				if(swap(value,value2)){return "success";}
				return "no match";
			}break;
		case "remove":
			if(value!=null && !value.equals("")){
				if(remove(value)){return "success";}
				return "no match";
			}break;
		}return "error:unrecognizable input:"+cmd+":"+value+":"+value2;
	}
	
	/**
	 * Gets the requested value from the database
	 * @param k String key we want to know the value of
	 * @return String value of the requested key
	 */
	public String get(String k){
		return database.get(k);
	}
	
	/**
	 * Inserts a new key-value pair in the database 
	 * @param k String key 
	 * @param v String value
	 * @return String key inserted
	 */
	public String put(String k, String v){
		database.put(k, v);
		return k;
	}
	
	/**
	 * Swaps the value of the keys
	 * @param k1 String first key
	 * @param k2 String second key
	 * @return boolean true if swap happened correctly, false otherwise 
	 */
	public boolean swap(String k1, String k2){
		String v1 = database.get(k1);
		String v2 = database.get(k2);
		if(v1==null || v2==null){return false;}
		database.put(k1, v2);
		database.put(k2,v1);
		return true;
	}
	
	/**
	 * removes a key-value pair from the database
	 * @param k String key to be removed
	 * @return boolean if removed happened successfully, false otherwise
	 */
	public boolean remove(String k){
		if(database.remove(k)==null){return false;}
		return true;
	}

	public HashMap<String, String> getDatabase() {
		return database;
	}
	
