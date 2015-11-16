package conceptPractice;

import java.io.*;
import java.net.*;

public class SocketsPractice {
	
	static private int portNumber = 12000;
	
	public static void main (String[] args) {
		try (
				ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket client = new Socket("serverSocket", portNumber);
			    PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
			    BufferedReader clientIn = new BufferedReader(
			        new InputStreamReader(client.getInputStream()));
//			    Socket clientSocket = serverSocket.accept();
				
		) {
			String userInput;
			while ((userInput = clientIn.readLine()) != null) {
			    clientOut.println(userInput);
			    System.out.println("echo: " + clientIn.readLine());
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
