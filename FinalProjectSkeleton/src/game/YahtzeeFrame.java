package game;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import server.SaveServer;

public class YahtzeeFrame extends JFrame implements ActionListener, ItemListener, Serializable  {

	//private static final AbstractButton dice1l = null;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem menuItem;
	
	private JTextField nameBox;
	JPanel topPanel;
	JPanel mainPanel;
	JPanel gamePanel;
	JPanel upperPanel;
	JPanel lowerPanel;
	JLabel nameLabel;
	JPanel dicePanel;
	JPanel rollPanel;
	ImagePanel dice1Panel;
	ImagePanel dice2Panel;
	ImagePanel dice3Panel;
	ImagePanel dice4Panel;
	ImagePanel dice5Panel;
	
	JButton AcesButton;
	JButton TwosButton;
	JButton ThreesButton;
	JButton FoursButton;
	JButton FivesButton;
	JButton SixesButton;
	
	private JTextField AcesBox;
	private JTextField TwosBox;
	private JTextField ThreesBox;
	private JTextField FoursBox;
	private JTextField FivesBox;
	private JTextField SixesBox;
	
	private JCheckBox checkBox1; 
	private JCheckBox checkBox2;
	private JCheckBox checkBox3;
	private JCheckBox checkBox4;
	private JCheckBox checkBox5;
	
	boolean cb1, cb2, cb3, cb4, cb5;
	
	JPanel d1 = new JPanel();
	JPanel d2 = new JPanel();
	JPanel d3 = new JPanel();
	JPanel d4 = new JPanel();
	JPanel d5 = new JPanel();
	//database
	Connection con;
	
	JLabel dicel1;
	JLabel dicel2;
	JLabel dicel3;
	JLabel dicel4;
	JLabel dicel5;
	
	ImageIcon icon1 = new ImageIcon("die1.png");
	ImageIcon icon2 = new ImageIcon("die2.png");
	ImageIcon icon3 = new ImageIcon("die3.png");
	ImageIcon icon4 = new ImageIcon("die4.png");
	ImageIcon icon5 = new ImageIcon("die5.png");
	
	String turnString = "Turn: ";
	String rollString = "Roll: ";
	int turnInt = 0;
	int rollInt = 0;
	
	JLabel turnLabel;
	JLabel rollLabel;
	JButton rollButton;
	
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream out = null;
	
	public YahtzeeFrame() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,900);
		
		mainPanel = new JPanel();
		gamePanel = new JPanel();
		dicePanel = new JPanel();
		rollPanel = new JPanel();
		
		createMenus();
		createNameBox();
		createUpperSection();
		createLowerSection();
		createDice1Panel();
		createRollPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		dicePanel.setLayout(new BoxLayout(dicePanel, BoxLayout.Y_AXIS));
		rollPanel.setLayout(new BoxLayout(rollPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollP = new JScrollPane(dicePanel);
		
		gamePanel.add(rollPanel);
		mainPanel.add(gamePanel);
		mainPanel.add(scrollP);
		
		this.add(mainPanel);
		

		
		
	}
	

	
	public void createMenus() {
		
		
		menuBar = new JMenuBar();
		menu = new JMenu("Game");
		
		//load game
		JMenuItem menuItemLoad = new JMenuItem("Load Game");
		menu.add(menuItemLoad);
		
		//save game
		//menu.addSeparator();
		JMenuItem menuItemSave = new JMenuItem("Save Game");
		menu.add(menuItemSave);
		
		//exit 
		//menu.addSeparator();
		JMenuItem menuItemExit = new JMenuItem("Exit", KeyEvent.VK_Q);
		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//action listener for menu items
		menuItemLoad.addActionListener(this);
		menuItemSave.addActionListener(this);
	
		menu.add(menuItemLoad);
		menu.add(menuItemSave);
		menu.add(menuItemExit);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
	}
	
	public void createNameBox() {
		nameBox = new JTextField(12);
		nameLabel = new JLabel();
		topPanel = new JPanel();
		
		topPanel.setLayout(new FlowLayout());
		topPanel.add(nameLabel);
		topPanel.add(nameBox);
		nameLabel.setText("Player Name: ");
		
		this.setLayout(new BorderLayout());
		this.add(topPanel, BorderLayout.NORTH);
	}
	
	public void createUpperSection() {
		upperPanel = new JPanel();
		upperPanel.setBorder(new TitledBorder(new EtchedBorder(), "Upper Section"));
		upperPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
		
		
		AcesButton = new JButton("Aces");
		TwosButton = new JButton("Twos");
		ThreesButton = new JButton("Threes");
		FoursButton = new JButton("Fours");
		FivesButton = new JButton("Fives");
		SixesButton = new JButton("Sixes");
		
		AcesBox = new JTextField(6);
		TwosBox = new JTextField(6);
		ThreesBox = new JTextField(6);
		FoursBox = new JTextField(6);
		FivesBox = new JTextField(6);
		SixesBox = new JTextField(6);
		
		//add buttons and text boxes
		upperPanel.add(AcesButton);
		upperPanel.add(AcesBox,gbc);
	
		upperPanel.add(TwosButton);
		upperPanel.add(TwosBox, gbc);
		
		upperPanel.add(ThreesButton);
		upperPanel.add(ThreesBox, gbc);
		
		upperPanel.add(FoursButton);
		upperPanel.add(FoursBox, gbc);
		
		upperPanel.add(FivesButton);
		upperPanel.add(FivesBox, gbc);
		
		upperPanel.add(SixesButton);
		upperPanel.add(SixesBox, gbc);
		
		//create labels for score subTotal, bonus, grandtotal and text boxes
		JLabel scoreSubtotal = new JLabel();
		JLabel bonus = new JLabel();
		JLabel grandTotal = new JLabel();
		
		scoreSubtotal.setText("Score Subtotal");
		bonus.setText("Bonus");
		grandTotal.setText("Grand Total");
		
		JTextField scoreSubTextField = new JTextField();
		JTextField bonusTextField = new JTextField();
		JTextField grandTotalTextField = new JTextField();
		
		//add lables and text boxes
		upperPanel.add(scoreSubtotal);
		upperPanel.add(scoreSubTextField, gbc);
		
		upperPanel.add(bonus);
		upperPanel.add(bonusTextField, gbc);
		
		upperPanel.add(grandTotal);
		upperPanel.add(grandTotalTextField, gbc);
		 
		gamePanel.add(upperPanel);
	}
	
	public void createLowerSection() {
		lowerPanel = new JPanel();
		lowerPanel.setBorder(new TitledBorder(new EtchedBorder(), "Lower Section"));
		lowerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JButton threeOfAKindButton = new JButton("3 of a Kind");
        JButton fourOfAKindButton = new JButton("4 of a Kind");
        JButton fullHouseButton = new JButton("Full House");
        JButton smallStraightButton = new JButton("Small Straight");
        JButton largeStraightButton = new JButton("Large Straight");
        JButton yahtzeeButton = new JButton("Yahtzee");
        JButton chanceButton = new JButton("Chance");
        
        JTextField threeTF = new JTextField(6);
        JTextField fourTF = new JTextField(6);
        JTextField fullTF = new JTextField(6);
        JTextField smallTF = new JTextField(6);
        JTextField largeTF = new JTextField(6);
        JTextField yahtzeeTF = new JTextField(6);
        JTextField chanceTF = new JTextField(6);
        
        JTextField ybLabelTF = new JTextField();
        JTextField totalLowerLabelTF = new JTextField();
        JTextField grandLabelTF = new JTextField();
        
        JLabel yahtzeeBonusLabel = new JLabel();
        JLabel totalOfLowerLabel = new JLabel();
        JLabel grandTotalLabel = new JLabel();
        
        yahtzeeBonusLabel.setText("Yahtzee Bonus");
        totalOfLowerLabel.setText("Total of Lower Section");
        grandTotalLabel.setText("Grand Total");
        
        
        
        lowerPanel.add(threeOfAKindButton);
        lowerPanel.add(threeTF, gbc);
        
        lowerPanel.add(fourOfAKindButton);
        lowerPanel.add(fourTF, gbc);
        
        lowerPanel.add(fullHouseButton);
        lowerPanel.add(fullTF, gbc);
        
        lowerPanel.add(smallStraightButton);
        lowerPanel.add(smallTF, gbc);
        
        lowerPanel.add(largeStraightButton);
        lowerPanel.add(largeTF, gbc);
        
        lowerPanel.add(chanceButton);
        lowerPanel.add(chanceTF, gbc);
        
        lowerPanel.add(yahtzeeButton);
        lowerPanel.add(yahtzeeTF, gbc);
        
        lowerPanel.add(yahtzeeBonusLabel);
        lowerPanel.add(ybLabelTF, gbc);
        
        lowerPanel.add(totalOfLowerLabel);
        lowerPanel.add(totalLowerLabelTF, gbc);
        
        lowerPanel.add(grandTotalLabel);
        lowerPanel.add(grandLabelTF, gbc);
        
        gamePanel.add(lowerPanel);
	}
	
	public void createDice1Panel() {

		dice1Panel = new ImagePanel("die1.png");
		dice2Panel = new ImagePanel("die1.png");
		dice3Panel = new ImagePanel("die1.png");
		dice4Panel = new ImagePanel("die1.png");
		dice5Panel = new ImagePanel("die1.png");
		
		checkBox1 = new JCheckBox("Keep");
		checkBox2 = new JCheckBox("Keep");
		checkBox3 = new JCheckBox("Keep");
		checkBox4 = new JCheckBox("Keep");
		checkBox5 = new JCheckBox("Keep");
		
		checkBox1.addItemListener(this);
		
		dicel1 = new JLabel();
		dicel2 = new JLabel();
		dicel3 = new JLabel();
		dicel4 = new JLabel();
		dicel5 = new JLabel();
		
		dicel1.setIcon(icon1);
		dicel2.setIcon(icon2);
		dicel3.setIcon(icon3);
		dicel4.setIcon(icon4);
		dicel5.setIcon(icon5);

		d1.add(dicel1);
		d1.add(checkBox1);
		dicePanel.add(d1);
		
		d2.add(dicel2);
		d2.add(checkBox2);
		dicePanel.add(d2);
		
		d3.add(dicel3);
		d3.add(checkBox3);
		dicePanel.add(d3);
		
		d4.add(dicel4);
		d4.add(checkBox4);
		dicePanel.add(d4);
		
		d5.add(dicel5);
		d5.add(checkBox5);
		dicePanel.add(d5);
		
	}
	
	public void createRollPanel() {
		turnLabel = new JLabel(turnString);
		rollLabel = new JLabel(rollString);
		rollButton = new JButton("Roll");
		
		rollPanel.add(turnLabel);
		rollPanel.add(rollLabel);
		rollPanel.add(rollButton);
		
		rollButton.addActionListener(this);
	}
	
	
	public static void main(String args[]) throws Exception {
		
		YahtzeeFrame yahtzee = new YahtzeeFrame();
		
		yahtzee.setVisible(true);
	}
	
    

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals("Load Game")) {
			try {
				loadGame();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(cmd.equals("Save Game")) {
			try {
				saveGame();
			} catch (UnknownHostException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(cmd.equals("Roll")) {
			System.out.println("rollButton");
			Random rand = new Random();
			int randDice1 = rand.nextInt(6) + 1;
			int randDice2 = rand.nextInt(6) + 1;
			int randDice3 = rand.nextInt(6) + 1;
			int randDice4 = rand.nextInt(6) + 1;
			int randDice5 = rand.nextInt(6) + 1;
			System.out.println(randDice1);
		
			icon1 = new ImageIcon("die"+randDice1+".png");
			icon2 = new ImageIcon("die"+randDice2+".png");
			icon3 = new ImageIcon("die"+randDice3+".png");
			icon4 = new ImageIcon("die"+randDice4+".png");
			icon5 = new ImageIcon("die"+randDice5+".png");
			
			if(cb1 == false)
			dicel1.setIcon(icon1);
			dicel2.setIcon(icon2);
			dicel3.setIcon(icon3);
			dicel4.setIcon(icon4);
			dicel5.setIcon(icon5);
			
			rollInt += 1;
			rollString = "Roll: "+Integer.toString(rollInt);
			rollLabel.setText(rollString);
			repaint();
			//if at 3rd roll, disable roll button
			if(rollInt == 3)	
				rollButton .setEnabled(false);
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			cb1 = true;
			System.out.println("selected");
		} else {
			cb2 = false;
		}
		
	}
	
	
	public void loadGame() throws Exception{
	//	System.out.println("Load Game");
	//	LoadFrame lf = new LoadFrame();
	//	lf.setVisible(true);
		//establish connection
		Socket socket = new Socket("localhost", 8888);
        // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // read the list of messages from the socket
        Player playerObj = (Player) objectInputStream.readObject();
        System.out.println("Received [" + playerObj.getPlayerName() + "] messages from: " + socket);
	
        socket.close();
	}
	
	public void saveGame() throws Exception {
		
		System.out.println("Save Game");
		String inputName = nameBox.getText();
		
		Player playerObj = new Player();
		playerObj.setPlayerName(inputName);
		
		//establish connection
        // need host and port, we want to connect to the ServerSocket at port 7777
        Socket socket = new Socket("localhost", 7777);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);    

        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject(playerObj);

        System.out.println("Closing socket and terminating program.");
        socket.close();
		
	}




	
}
