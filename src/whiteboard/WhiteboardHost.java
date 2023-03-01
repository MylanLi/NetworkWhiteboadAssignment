package whiteboard;

import java.awt.*;  
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class WhiteboardHost extends JFrame implements IHost{
	
	static WhiteboardHost theFrame;
	static DrawPanelHost mainPanel;
	static HashMap<String, IClient> connectedClients = new HashMap<String, IClient>();
	static int clientCount = 0;
	static DefaultListModel<String> userList;
	static String fileName = "new";
	
	static final Color purple = new Color(128, 0, 128);
	static final Color darkGreen = new Color(0, 100, 0);
	static final Color lavendar = new Color(230, 230, 250);
	static final Color crimson = new Color(220, 20, 60);
	static final Color brown = new Color(184, 134, 11);
	
	private JTextField textFieldDraw;
	JTextArea txtMsgArea;
	private JTextField textFieldChat;
	
	public WhiteboardHost (){
		setSize(1400,780);
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
        		txtMsgArea.append("Host " + textFieldChat.getText() + "\n");
        		/*
        		int i = clientCount;
        		try {
        			for(i = 0; i < clientCount; i++) {
        				connectedClients.get(i).hostMsg(textFieldChat.getText());;
        			}
        		} catch (Exception eSend) {
        			connectedClients.remove(i);
        			clientCount -= 1;
        		}*/
        		Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
        		while(itr.hasNext()) {
        			Map.Entry<String, IClient> entry = itr.next();
        			try {
        				entry.getValue().hostMsg(textFieldChat.getText());
        			} catch (RemoteException e1) {
        				userList.removeElement(entry.getKey());
        				itr.remove();
        				clientCount = clientCount - 1;
        			}
        		}
        	}
        });
        btnChat.setBounds(1054, 689, 89, 20);
        getContentPane().add(btnChat);
        
        JButton btnNew = new JButton("New");
        btnNew.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mainPanel.clear();
        		mainPanel.refresh();
        		fileName = "new";
        	}
        });
        btnNew.setBounds(730, 11, 89, 23);
        getContentPane().add(btnNew);
        
        JButton btnOpen = new JButton("Open");
        btnOpen.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String toOpenFile = JOptionPane.showInputDialog(theFrame, "Enter file name (excluding extension, jpg only)");
        		if(toOpenFile != null && toOpenFile.trim().isEmpty()) {
        			txtMsgArea.append("Please enter a valid filename\n");
        		} else if(toOpenFile != null) {
        			//ImageIcon fileImage = new ImageIcon(toOpenFile + ".jpg");
        			//Image newImg = fileImage.getImage();
        			Image newImg;
					try {
						newImg = ImageIO.read(new File(toOpenFile + ".jpg"));
						Graphics g = DrawPanelHost.image.createGraphics();
	        			g.drawImage(newImg,0,0, mainPanel);
	        			g.dispose();
	        			repaint();
	        			mainPanel.refresh();
	        			fileName = toOpenFile;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        		} else {
        		}
        	}
        });
        btnOpen.setBounds(829, 11, 89, 23);
        getContentPane().add(btnOpen);
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		File outputfile = new File(fileName + ".jpg");
        	    try {
					ImageIO.write(mainPanel.image, "jpg", outputfile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        btnSave.setBounds(925, 11, 89, 23);
        getContentPane().add(btnSave);
        
        JButton btnSaveAs = new JButton("SaveAs");
        btnSaveAs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//option
        		String newfile = JOptionPane.showInputDialog(theFrame, "Enter file name");
        		if(newfile!= null && newfile.trim().isEmpty()) {
        			txtMsgArea.append("Please enter a valid filename\n");
        		} else if(newfile != null) {
        			//save
        			File outputfile = new File(newfile + ".jpg");
            	    try {
    					ImageIO.write(mainPanel.image, "jpg", outputfile);
    				} catch (IOException ex) {
    					// TODO Auto-generated catch block
    					ex.printStackTrace();
    				}
            	    fileName = newfile;
        		} else {
        		}
        	}
        });
        btnSaveAs.setBounds(1024, 11, 89, 23);
        getContentPane().add(btnSaveAs);
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		onClose();
        	}
        });
        btnClose.setBounds(1123, 11, 89, 23);
        getContentPane().add(btnClose);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(1149, 47, 218, 188);
        getContentPane().add(scrollPane_1);
        
        JLabel lblUsers = new JLabel("OnlineUsers");
        scrollPane_1.setColumnHeaderView(lblUsers);
        
        JList<String> listUsers = new JList<String>();
        scrollPane_1.setViewportView(listUsers);
        userList = new DefaultListModel<String>();
        userList.addElement("Host");
        listUsers.setModel(userList);
        
        JButton btnKick = new JButton("Kick User");
        btnKick.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String toKick = listUsers.getSelectedValue();
        		if(toKick.equals("Host")) {
        			txtMsgArea.append("Can't kick yourself, close instead\n");
        			return;
        		}
        		try {
					connectedClients.get(toKick).disconnect();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					System.out.println("i dont understand why it gives this when it closes properly");
					e1.printStackTrace();
				}
        		connectedClients.remove(toKick);
        		mainPanel.connectedClients.remove(toKick);

        		userList.remove(listUsers.getSelectedIndex());
        		//userList.removeElement(listUsers.getSelectedIndex());
        		//listUsers.setModel(userList);
        	}
        });
        btnKick.setBounds(1149, 246, 89, 23);
        getContentPane().add(btnKick);
	}

	public static void main(String[] args) {  
	    
	    try {
	    	theFrame = new WhiteboardHost();
	    	Registry registry = LocateRegistry.createRegistry(1099);
			IHost stub = (IHost) UnicastRemoteObject.exportObject(theFrame, 0);
			registry.rebind("HostService", stub);
			onStart(stub);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public static void onStart(IHost s) {
		
		//theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//TestDrawingCanvas mainPanel = new TestDrawingCanvas();
		mainPanel = new DrawPanelHost();
		theFrame.getContentPane().add(mainPanel);
        mainPanel.setBounds(10, 10, 700, 700);
        
		//theFrame.pack();
		theFrame.setVisible(true);
		theFrame.addWindowListener(new WindowAdapter() {
            //I skipped unused callbacks for readability

            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
                //System.exit(0);
            }
        });
	}
	
	public static void onClose() {
		//theFrame.txtMsgArea.append("Closing connected clients, please wait 5");
		Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
		while(itr.hasNext()) {
			Map.Entry<String, IClient> entry = itr.next();
			try {
				entry.getValue().hostMsg("Host has ended the session\n");
				entry.getValue().disconnect();
			} catch (RemoteException e1) {
				//itr.remove();
				//clientCount = clientCount - 1;
			}
		
		
		}
		System.exit(0);
	}
	
	public String connect(String who) throws RemoteException {
		//check for matching names
		Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
		while(itr.hasNext()) {
			Map.Entry<String, IClient> entry = itr.next();
			if(who.equals(entry.getKey())) {
				try {
					IClient temp = (IClient)Naming.lookup("rmi://localhost/" + who + "Client");
					temp.hostMsg("Please reopen and try a different name");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return "NameExists";
			}
		}
		txtMsgArea.append(who + " is joining\n");
		try {
			//connectedClients.add((IClient)Naming.lookup("rmi://localhost/" + who + "Client"));
			int confirm = JOptionPane.showConfirmDialog(theFrame, who + " wishes to connect. Approve?", "", JOptionPane.YES_NO_OPTION);
			if(confirm == 1) {
				return "Refused";
			}
			connectedClients.put(who, (IClient)Naming.lookup("rmi://localhost/" + who + "Client"));
			clientCount += 1;
			mainPanel.updateClientList(who, connectedClients.get(who));
			ImageIcon icon = new ImageIcon(mainPanel.image);
			connectedClients.get(who).updateImage(icon);
			userList.addElement(who);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "Connected\n";
	}
	
	public void clientMsg(String who, String msg) throws RemoteException {
		/*
		int i = 0;
		try {
			for(i = 0; i < clientCount; i++) {
				connectedClients.get(i).passMsg(who, msg);
			}
		} catch (Exception e) {
			connectedClients.remove(i);
			clientCount -= 1;
		}
		*/
		Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
		while(itr.hasNext()) {
			Map.Entry<String, IClient> entry = itr.next();
			try {
				entry.getValue().passMsg(who, msg);
			} catch (RemoteException e1) {
				itr.remove();
				clientCount = clientCount - 1;
			}
		}
		txtMsgArea.append(who + ": " + msg + "\n");
	}
	
	public void updateImage(ImageIcon img) throws RemoteException {
		Image upImg = img.getImage();
		Graphics g = DrawPanelHost.image.createGraphics();
		g.drawImage(upImg,0,0, mainPanel);
		g.dispose();
		repaint();
		
		ImageIcon icon = new ImageIcon(mainPanel.image);
		/*
		int i = 0;
		try {
			for(i = 0; i < clientCount; i++) {
				connectedClients.get(i).updateImage(icon);
			}
		} catch (Exception e) {
			connectedClients.remove(i);
			clientCount -= 1;
		}*/
		Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
		while(itr.hasNext()) {
			Map.Entry<String, IClient> entry = itr.next();
			try {
				entry.getValue().updateImage(icon);
			} catch (RemoteException e1) {
				itr.remove();
				clientCount = clientCount - 1;
			}
		}
	}
}
