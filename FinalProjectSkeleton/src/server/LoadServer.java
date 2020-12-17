package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game.Player;

public class LoadServer extends JFrame implements Serializable{

	private JTextArea wordsBox;
	Connection con;
	PreparedStatement getStatement, updateStatement; // only insertStatement is used in this example
	ResultSet rs ;

	
	public LoadServer() throws Exception {
		createMainPanel();
		wordsBox.append("Ready to Accept Connections");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setVisible(true);
		
		
        // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = new ServerSocket(8888);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
        System.out.println("Connection from " + socket + "!");
        
        
        
        //get player data from database
        this.con = DriverManager.getConnection
  		      ("jdbc:sqlite:yahtzee.db");
        
        Statement statement = null;
        ResultSet resultSet = null;
        statement = con.createStatement();
        resultSet = statement.executeQuery	
				  ("select * from PLAYER");
        
        while (resultSet.next()) {
			  System.out.println(resultSet.getString(1) + "\t"
					  + resultSet.getString(2));
			  Player loaddata = new Player(resultSet.getString(2));
		        OutputStream outputStream = socket.getOutputStream();
		        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream); 
		        

		        objectOutputStream.writeObject(loaddata);
        }
       
        // create a DataInputStream so we can read data from it.
       // Player testload = new Player("tl");
       	//  ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

       // saveToDatabase(playerObj);
        
        System.out.println("Closing sockets.");
        ss.close();
        socket.close();
		
	}
	
	public void createMainPanel() {
		wordsBox = new JTextArea(35,10);

		JScrollPane listScroller = new JScrollPane(wordsBox);
		this.add(listScroller, BorderLayout.CENTER);
		listScroller.setPreferredSize(new Dimension(250, 80));
	}
	

	
	public static void main(String[] main) throws Exception {
		LoadServer lServer = new LoadServer();
	}
}
