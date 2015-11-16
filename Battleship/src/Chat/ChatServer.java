package Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.*;

public class ChatServer implements ActionListener {

	JTextArea message;
	TextField chatBox;
	StringBuilder line = new StringBuilder("");
	ArrayList<PrintWriter> outputs;
	
	private void init() {
		JFrame frame = new JFrame("Server");
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1));
        message = new JTextArea("");
        message.setEditable(false);
        //chatBox = new TextField();
        JButton enter = new JButton("Clear");
        enter.addActionListener(this);
        panel.add(message);
        //panel.add(chatBox);
        panel.add(enter);
        frame.add(panel);
        frame.setVisible(true);
	}
	
	public void receiveMessage() {
		try {
			ServerSocket chatServer = new ServerSocket(20000);
			int numClients = 2;
			ArrayList<BufferedReader> inputs = new ArrayList<BufferedReader>();
			outputs = new ArrayList<PrintWriter>();
			
			for (int i = 0; i < numClients; i++) {
				Thread t = new Thread(new ChatClient(i + 1));
				t.start();
				Socket client = chatServer.accept();
				inputs.add(new BufferedReader(new InputStreamReader(client.getInputStream())));
				outputs.add(new PrintWriter(client.getOutputStream(), true));
			}
			init();
			boolean go = true;
			while (go) {
				for (int i = 0; i < numClients; i++) {
					String currentLine;
					if (inputs.get(i).ready()) {
						currentLine = inputs.get(i).readLine();
						line.append(currentLine + "    ");
						//Send data back to client
						for (PrintWriter output : outputs) {
							output.println(line.toString());
						}
						String text = convertLine(line);
						message.setText(text);
					}
				}
			}
			chatServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private String convertLine(StringBuilder text) {
		String result = text.toString();
		result = result.replace("    ", "\n");
		return result;
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.receiveMessage();
		

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		message.setText("");
		line = new StringBuilder("");
		for (PrintWriter output : outputs) {
			output.println("RESET");
		}
	}

}
