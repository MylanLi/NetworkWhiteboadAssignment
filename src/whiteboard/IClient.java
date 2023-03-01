package whiteboard;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;

public interface IClient extends Remote{
	public void hostMsg(String msg) throws RemoteException;
	public void updateImage(ImageIcon i) throws RemoteException;
	public void passMsg(String who, String msg) throws RemoteException;
	public void disconnect() throws RemoteException;
}
