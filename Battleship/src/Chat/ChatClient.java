package Chat;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class ChatClient implements ActionListener, Runnable {

	JTextArea message;
	TextField chatBox;
	
	Socket client;
	BufferedReader in;
	PrintWriter out;
	String name;
	
	public ChatClient(int i) {
		name = new String("Client" + i);
	}

	private void init() {
		JFrame frame = new JFrame(name);
        frame.setSize(300,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1));
        message = new JTextArea("Empty");
        message.setEditable(true);
        chatBox = new TextField();
        JButton enter = new JButton("Send");
        enter.addActionListener(this);
        panel.add(message);
        panel.add(chatBox);
        panel.add(enter);
        frame.add(panel);
        frame.setVisible(true);
	}

	public static void main(String[] args) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		out.println(name + ": " + chatBox.getText());
		chatBox.setText("");
	}

	@Override
	public void run() {
		try {
			client = new Socket("0.0.0.0", 20000);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
			init();
			while (true) {
				if (in.ready()) {
					String text = in.readLine();
					if (text.equals("RESET")) {
						message.setText("");
					}
					else if (!text.equals("")) {
						text = text.replace("    ", "\n");
						message.setText(text);
					}
				}
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
