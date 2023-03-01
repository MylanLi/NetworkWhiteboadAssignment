package whiteboard;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

public interface IHost extends Remote{
	public String connect(String who) throws RemoteException;
	public void clientMsg(String who, String msg) throws RemoteException;
	public void updateImage(ImageIcon i) throws RemoteException;
}
