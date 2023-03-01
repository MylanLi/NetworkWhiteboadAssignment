package whiteboard;

import java.awt.*;  
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton; 

public class WhiteboardClient extends JFrame implements IClient{
	static WhiteboardClient theFrame;
	static DrawPanelClient mainPanel;
	static Registry registry;
	static IClient stub;
	static final Color purple = new Color(128, 0, 128);
	static final Color darkGreen = new Color(0, 100, 0);
	static final Color lavendar = new Color(230, 230, 250);
	static final Color crimson = new Color(220, 20, 60);
	static final Color brown = new Color(184, 134, 11);
	
	private JTextField textFieldDraw;
	static JTextArea txtMsgArea;
	private JTextField textFieldChat;
	private JTextField textFieldName;
	static IHost hostStub;
	
	public WhiteboardClient (){
		setSize(1200,780);
		getContentPane().setLayout(null);
		
		//button setup code
        JRadioButton rdbtnRect = new JRadioButton("Rectangle");
        rdbtnRect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedBrush = "rectangle";
        	}
        });
        rdbtnRect.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rdbtnRect.setBounds(730, 191, 150, 45);
        getContentPane().add(rdbtnRect);
        
        JRadioButton rdbtnCircle = new JRadioButton("Circle");
        rdbtnCircle.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedBrush = "circle";
        	}
        });
        rdbtnCircle.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rdbtnCircle.setBounds(730, 95, 150, 45);
        getContentPane().add(rdbtnCircle);
        
        JRadioButton rdbtnTriangle = new JRadioButton("Triangle");
        rdbtnTriangle.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedBrush = "triangle";
        	}
        });
        rdbtnTriangle.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rdbtnTriangle.setBounds(730, 143, 150, 45);
        getContentPane().add(rdbtnTriangle);
        
        JRadioButton rdbtnLine = new JRadioButton("Line");
        rdbtnLine.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedBrush = "line";
        	}
        });
        rdbtnLine.setSelected(true);
        rdbtnLine.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rdbtnLine.setBounds(730, 47, 150, 45);
        getContentPane().add(rdbtnLine);

        JRadioButton rdbtnText = new JRadioButton("Text");
        rdbtnText.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedBrush = "text";
        	}
        });
        rdbtnText.setFont(new Font("Tahoma", Font.PLAIN, 24));
        rdbtnText.setBounds(925, 161, 150, 45);
        getContentPane().add(rdbtnText);
        
        ButtonGroup brushBG = new ButtonGroup();
        brushBG.add(rdbtnLine);
        brushBG.add(rdbtnTriangle);
        brushBG.add(rdbtnCircle);
        brushBG.add(rdbtnRect);
        brushBG.add(rdbtnText);

        ButtonGroup colourBG = new ButtonGroup();
        
        JRadioButton rdbtnRed = new JRadioButton("");
        rdbtnRed.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.red;
        	}
        });
        rdbtnRed.setBackground(Color.RED);
        rdbtnRed.setBounds(925, 47, 53, 23);
        getContentPane().add(rdbtnRed);
        colourBG.add(rdbtnRed);
        
        JRadioButton rdbtnBlue = new JRadioButton("");
        rdbtnBlue.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.BLUE;
        	}
        });
        rdbtnBlue.setBackground(Color.BLUE);
        rdbtnBlue.setBounds(925, 73, 53, 23);
        getContentPane().add(rdbtnBlue);
        colourBG.add(rdbtnBlue);
        
        JRadioButton rdbtnGreen = new JRadioButton("");
        rdbtnGreen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.GREEN;
        	}
        });
        rdbtnGreen.setBackground(Color.GREEN);
        rdbtnGreen.setBounds(925, 125, 53, 23);
        getContentPane().add(rdbtnGreen);
        colourBG.add(rdbtnGreen);
        
        JRadioButton rdbtnYellow = new JRadioButton("");
        rdbtnYellow.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.YELLOW;
        	}
        });
        rdbtnYellow.setBackground(Color.YELLOW);
        rdbtnYellow.setBounds(925, 99, 53, 23);
        getContentPane().add(rdbtnYellow);
        colourBG.add(rdbtnYellow);
        
        JRadioButton rdbtnOrange = new JRadioButton("");
        rdbtnOrange.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.ORANGE;
        	}
        });
        rdbtnOrange.setBackground(Color.ORANGE);
        rdbtnOrange.setBounds(980, 47, 53, 23);
        getContentPane().add(rdbtnOrange);
        colourBG.add(rdbtnOrange);
        
        JRadioButton rdbtnPurple = new JRadioButton("");
        rdbtnPurple.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = purple;
        	}
        });
        rdbtnPurple.setBackground(purple);
        rdbtnPurple.setBounds(980, 73, 53, 23);
        getContentPane().add(rdbtnPurple);
        colourBG.add(rdbtnPurple);
        
        JRadioButton rdbtnCyan = new JRadioButton("");
        rdbtnCyan.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.CYAN;
        	}
        });
        rdbtnCyan.setBackground(Color.CYAN);
        rdbtnCyan.setBounds(980, 99, 53, 23);
        getContentPane().add(rdbtnCyan);
        colourBG.add(rdbtnCyan);
        
        JRadioButton rdbtnMagenta = new JRadioButton("");
        rdbtnMagenta.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.MAGENTA;
        	}
        });
        rdbtnMagenta.setBackground(Color.MAGENTA);
        rdbtnMagenta.setBounds(980, 125, 53, 23);
        getContentPane().add(rdbtnMagenta);
        colourBG.add(rdbtnMagenta);
        
        JRadioButton rdbtnBlack = new JRadioButton("");
        rdbtnBlack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.BLACK;
        	}
        });
        rdbtnBlack.setSelected(true);
        rdbtnBlack.setBackground(Color.BLACK);
        rdbtnBlack.setBounds(1035, 47, 53, 23);
        getContentPane().add(rdbtnBlack);
        colourBG.add(rdbtnBlack);
        
        JRadioButton rdbtnGray = new JRadioButton("");
        rdbtnGray.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.GRAY;
        	}
        });
        rdbtnGray.setBackground(Color.GRAY);
        rdbtnGray.setBounds(1035, 73, 53, 23);
        getContentPane().add(rdbtnGray);
        colourBG.add(rdbtnGray);
        
        JRadioButton rdbtnWhite = new JRadioButton("");
        rdbtnWhite.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.WHITE;
        	}
        });
        rdbtnWhite.setBackground(Color.WHITE);
        rdbtnWhite.setBounds(1035, 99, 53, 23);
        getContentPane().add(rdbtnWhite);
        colourBG.add(rdbtnWhite);
        
        JRadioButton rdbtnPink = new JRadioButton("");
        rdbtnPink.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = Color.PINK;
        	}
        });
        rdbtnPink.setBackground(Color.PINK);
        rdbtnPink.setBounds(1035, 125, 53, 23);
        getContentPane().add(rdbtnPink);
        colourBG.add(rdbtnPink);
        
        JRadioButton rdbtnDarkGreen = new JRadioButton("");
        rdbtnDarkGreen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = darkGreen;
        	}
        });
        rdbtnDarkGreen.setBackground(darkGreen);
        rdbtnDarkGreen.setBounds(1090, 47, 53, 23);
        getContentPane().add(rdbtnDarkGreen);
        colourBG.add(rdbtnDarkGreen);
        
        JRadioButton rdbtnLavendar = new JRadioButton("");
        rdbtnLavendar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = lavendar;
        	}
        });
        rdbtnLavendar.setBackground(lavendar);
        rdbtnLavendar.setBounds(1090, 73, 53, 23);
        getContentPane().add(rdbtnLavendar);
        colourBG.add(rdbtnLavendar);
        
        JRadioButton rdbtnCrimson = new JRadioButton("");
        rdbtnCrimson.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = crimson;
        	}
        });
        rdbtnCrimson.setBackground(crimson);
        rdbtnCrimson.setBounds(1090, 99, 53, 23);
        getContentPane().add(rdbtnCrimson);
        colourBG.add(rdbtnCrimson);
        
        JRadioButton rdbtnBrown = new JRadioButton("");
        rdbtnBrown.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.selectedColor = brown;
        		//textArea.append("brown selected\n");
        	}
        });
        rdbtnBrown.setBackground(brown);
        rdbtnBrown.setBounds(1090, 125, 53, 23);
        getContentPane().add(rdbtnBrown);
        colourBG.add(rdbtnBrown);
        
        textFieldDraw = new JTextField();
        textFieldDraw.setText("Enter the text to draw here");
        textFieldDraw.setBounds(925, 216, 218, 20);
        getContentPane().add(textFieldDraw);
        textFieldDraw.setColumns(10);
        textFieldDraw.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.brushText = textFieldDraw.getText();
        	}
        });
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(730, 312, 413, 366);
        getContentPane().add(scrollPane);
        
        txtMsgArea = new JTextArea();
        txtMsgArea.setBackground(Color.BLACK);
        txtMsgArea.setText("hello\r\n");
        txtMsgArea.setForeground(Color.YELLOW);
        scrollPane.setViewportView(txtMsgArea);
        
        textFieldChat = new JTextField();
        textFieldChat.setText("Enter a message to send to chat here");
        textFieldChat.setBounds(730, 689, 314, 20);
        getContentPane().add(textFieldChat);
        textFieldChat.setColumns(10);
        
        JButton btnChat = new JButton("Send");
        btnChat.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					hostStub.clientMsg(textFieldName.getText(), textFieldChat.getText());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		//txtMsgArea.append(textFieldName.getText() + ": " + textFieldChat.getText() +"\n");
        	}
        });
        btnChat.setBounds(1054, 689, 89, 20);
        getContentPane().add(btnChat);
        
        textFieldName = new JTextField();
        textFieldName.setText("Please enter a name and press connect");
        textFieldName.setColumns(10);
        textFieldName.setBounds(730, 281, 314, 20);
        getContentPane().add(textFieldName);
       
        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textFieldName.setText(textFieldName.getText().replaceAll("\\s",""));
    			mainPanel.active = true;
    			try {
					registry.rebind((textFieldName.getText() + "Client"), stub);
					//txtMsgArea.append(hostStub.connect(textFieldName.getText()));
					String reply = hostStub.connect(textFieldName.getText());
					if(reply.equals("Refused")) {
						txtMsgArea.append(reply);
						System.exit(0);
					} else if(reply.equals("NameExists")) {
						try {
							TimeUnit.SECONDS.sleep(1);
							System.exit(0);
						} catch (InterruptedException e1) {
							System.exit(0);
							e1.printStackTrace();
						}
						
					} else {
						txtMsgArea.append(reply);
					}
				} catch (AccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			textFieldName.setEnabled(false);
    			btnConnect.setEnabled(false);
        	}
        });
        btnConnect.setBounds(1054, 281, 89, 20);
        getContentPane().add(btnConnect);
	}

	public static void main(String[] args) {  
	    
	    
	    try {
	    	theFrame = new WhiteboardClient();
			registry = LocateRegistry.getRegistry("localhost");
			hostStub = (IHost)Naming.lookup("rmi://localhost/HostService");
			stub = (IClient) UnicastRemoteObject.exportObject(theFrame, 0);
			//ImageIcon icon = new ImageIcon(DrawPanel.image);
			//hostStub.updateImage(icon);
			onStart();
			
		} catch (Exception e) {
			System.out.println("Host has not started. Or there is an error with your connection");
			e.printStackTrace();
		}
	}
	
	public static void onStart() {
		
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//TestDrawingCanvas mainPanel = new TestDrawingCanvas();
		mainPanel = new DrawPanelClient(hostStub);
		theFrame.getContentPane().add(mainPanel);
        mainPanel.setBounds(10, 10, 700, 700);
        
		//theFrame.pack();
		theFrame.setVisible(true);
	}

	@Override
	public void hostMsg(String msg) throws RemoteException {
		txtMsgArea.append("Host sent a msg:" + msg + "\n");
	}

	@Override
	public void updateImage(ImageIcon img) throws RemoteException {
		Image upImg = img.getImage();
		Graphics g = DrawPanelClient.image.createGraphics();
		g.drawImage(upImg,0,0, mainPanel);
		g.dispose();
		repaint();
		
	}

	@Override
	public void passMsg(String who, String msg) throws RemoteException {
		txtMsgArea.append(who + ": " + msg + "\n");
	}

	@Override
	public void disconnect() throws RemoteException {
		
		try {
			//unbind own listing
			registry.unbind(textFieldName.getText() + "Client");
			//forget host listing
			hostStub = null;
			mainPanel.stub = null;
			txtMsgArea.append("You have been kicked, client closing in 5...");
			TimeUnit.SECONDS.sleep(5);
			System.exit(0);
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			
		}
		
		//close
		
	}
}
