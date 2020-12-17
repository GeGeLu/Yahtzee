package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class SaveServer extends JFrame implements Serializable{

	private JTextArea wordsBox;
	Connection con;
	PreparedStatement insertStatement, updateStatement; // only insertStatement is used in this example
	ResultSet rs ;

	
	public SaveServer() throws Exception {
		createMainPanel();
		wordsBox.append("Ready to Accept Connections");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setVisible(true);
		
		
        // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = new ServerSocket(7777);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
        System.out.println("Connection from " + socket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // read the list of messages from the socket
        Player playerObj = (Player) objectInputStream.readObject();
        System.out.println("Received [" + playerObj.getPlayerName() + "] messages from: " + socket);

        saveToDatabase(playerObj);
        
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
	
	public void saveToDatabase(Player playerObj) throws SQLException {
		try
		{
		    // Connect to a database
		    this.con = DriverManager.getConnection
		      ("jdbc:sqlite:yahtzee.db");
		} catch (Exception e) {
			System.exit(0);
		}
		//create table
		/*Statement tableStmt = con.createStatement();
	    String sql = "CREATE TABLE PLAYER " +
                  "(name not NULL)";
	    tableStmt.executeUpdate(sql);
	    
		/* sets up the prepared statement for SQL inserts */
		String insertSQL = "Insert Into PLAYER (name) " +
				"Values (?)";
		try {
			insertStatement = con.prepareStatement(insertSQL);
			
			insertStatement.setString(1, playerObj.getPlayerName());
			insertStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//
	    Statement statement = null;
		try {
			statement = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		   ResultSet resultSet = null;
			try {
				resultSet = statement.executeQuery
				  ("select * from PLAYER");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}

		    // Iterate through the result and print the player names
		    try {
				while (resultSet.next())
				  System.out.println(resultSet.getString(1) + "\t"
						  + resultSet.getString(2));
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(0);
			}

		    // Close the connection
		    try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
	}
	
	public static void main(String[] main) throws Exception {
		SaveServer saveServer = new SaveServer();
	}
}
