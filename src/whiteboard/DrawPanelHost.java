package whiteboard;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class DrawPanelHost extends JPanel{

	private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    public Color selectedColor = Color.BLACK;
    public String selectedBrush = "line";
    public String brushText = "";
    
    public static BufferedImage image;
    static HashMap<String, IClient> connectedClients= new HashMap<String, IClient>();
	static int clientCount = 0;
    
    public DrawPanelHost() {
        //setBackground(Color.WHITE);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        MyMouse myMouse = new MyMouse();
        addMouseListener(myMouse);
        addMouseMotionListener(myMouse);
        
        //make the image white
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.dispose();
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    public void clear () {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.dispose();
        repaint();
    }

    private class MyMouse extends MouseAdapter {
        private Graphics2D g2d;
        int mX = 0;
    	int mY = 0;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }

            g2d = image.createGraphics();
            mX = e.getX();
        	mY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        	g2d.setColor(selectedColor);
            //for when circles and rects are flippled
            int drawXStart = mX;
            int drawYStart = mY;
            int triXMid = e.getX() - (e.getX() - mX)/2;
            if((e.getX() - mX) < 0) {
            	drawXStart = e.getX();
            	triXMid = e.getX() + (mX - e.getX())/2;
            }
            if((e.getY() - mY) < 0) {
            	drawYStart = e.getY();
            }
            
            if(selectedBrush == "circle") {
            	g2d.drawOval(drawXStart,drawYStart,Math.abs(e.getX() - mX),Math.abs(e.getY() - mY)); 
            } else if(selectedBrush == "rectangle") {
            	//g2d.drawRect(drawXStart,drawYStart,Math.abs(e.getX() - mX),Math.abs(e.getY() - mY));
            	g2d.draw(new Rectangle2D.Double(drawXStart, drawYStart,Math.abs(e.getX() - mX),Math.abs(e.getY() - mY)));
            } else if(selectedBrush == "triangle") {
            	g2d.drawPolygon(new int[] {mX, triXMid, e.getX()}, new int[] {mY, e.getY(), mY}, 3);
            } else if (selectedBrush == "text") {
            	g2d.drawString(brushText, mX, mY);
            } else {
            	g2d.drawLine(mX, mY, e.getX(), e.getY());
            }        	
        	
            repaint();
            g2d.dispose();
            g2d = null;
            /*
			ImageIcon icon = new ImageIcon(DrawPanelHost.image);
			
			Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
			while(itr.hasNext()) {
				Map.Entry<String, IClient> entry = itr.next();
				try {
					//for(i = 0; i < clientCount; i++) {
					//	connectedClients.get(i).updateImage(icon);
					//}
					
					//stb.updateImage(icon);
					entry.getValue().updateImage(icon);
				} catch (RemoteException e1) {
					//connectedClients.remove(i);
					//connectedClients.remove(entry.getKey());
					itr.remove();
					clientCount = clientCount - 1;
				}
        	}
        	*/
            refresh();
        }
    }
    
    public void refresh() {
		ImageIcon icon = new ImageIcon(DrawPanelHost.image);
		
		Iterator<Map.Entry<String, IClient>> itr = connectedClients.entrySet().iterator();
		while(itr.hasNext()) {
			Map.Entry<String, IClient> entry = itr.next();
			try {
				//for(i = 0; i < clientCount; i++) {
				//	connectedClients.get(i).updateImage(icon);
				//}
				
				//stb.updateImage(icon);
				entry.getValue().updateImage(icon);
			} catch (RemoteException e1) {
				//connectedClients.remove(i);
				//connectedClients.remove(entry.getKey());
				itr.remove();
				clientCount = clientCount - 1;
			}
    	}
    }
    
    public void openImage(String f) {
    	
    }
    
    public void updateClientList(String who, IClient stub) {
    	connectedClients.put(who, stub);
    	clientCount += 1;
    }
}
