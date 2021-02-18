import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//lockscreen
public class Main {
	static final String PY_COMMAND = "python C:\\Project\\bin\\face_recog.py";
	static final String HOOK_PATH = "C:\\Project\\bin\\keyhook.exe";
	static int c_pid;
	static String kill_cmd = "taskkill /F /PID ";
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		User.getUserList();		//ó�� �����Ҷ����� ����� üũ
		
		//���������
		JFrame fr = new JFrame("Frame");			
		ImageIcon ic = new ImageIcon("C:\\Project\\src\\background\\background.jpg");
		JLabel img = new JLabel(ic);
		fr.add(img);
		Toolkit tk = Toolkit.getDefaultToolkit();		
		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""), new Point(), null);
		fr.setCursor(invisCursor);						//Ŀ�� �����
		fr.setExtendedState(JFrame.MAXIMIZED_BOTH);		//��ü ȭ��
	    fr.setUndecorated(true);						//Ÿ��Ʋ�� ����
	    fr.setVisible(true);							
	    fr.setAlwaysOnTop(true);						//�ֻ�� ��ġ
		
	    //������ ����
        Thread mtp = new MultiThread_python();			//python_��� ������
		mtp.start();
        Thread mtc = new MultiThread_c();				//c_��� ������
		mtc.start();
		Runnable r = new FlagCheck(fr);					//�÷��� üũ
		new Thread(r).start();
		
		
		//��ũ��Ʈ ����
		try { 
			Process c = Runtime.getRuntime().exec(HOOK_PATH);
			c_pid = (int) c.pid();
			Process py = Runtime.getRuntime().exec(PY_COMMAND);
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
		
		//tray  ���
		TrayIcon trayicon=null;

		if(SystemTray.isSupported()) {
			SystemTray tray=SystemTray.getSystemTray();
			Image trayImage=Toolkit.getDefaultToolkit().getImage("\\icon.png");
		  
			ActionListener listener=new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.toString().contains("����")) {
						System.out.println("����â ����");
					}
					else if(e.toString().contains("����")) {
						try { 
							Process kill = Runtime.getRuntime().exec(kill_cmd+c_pid);
						}	
						catch (IOException e1) { 
							e1.printStackTrace(); 
						}
						System.exit(0);
					}
				}
			};
		   
			PopupMenu popup=new PopupMenu();
		   
			MenuItem defaultItem=new MenuItem("����");
			defaultItem.addActionListener(listener);
			 
			MenuItem exit=new MenuItem("����");
			exit.addActionListener(listener);
		   
			popup.add(defaultItem);
			popup.add(exit);
			trayicon=new TrayIcon(trayImage,"���α׷�", popup);
			trayicon.addActionListener(listener);
		  
		   
			try {
				tray.add(trayicon);
			} catch(AWTException e) {
				e.printStackTrace();
				}
		}
		else {
			
		}
		//tray
	}
}

class FlagCheck implements Runnable {
	JFrame temp_fr;
	
	FlagCheck(JFrame fr) {
		temp_fr = fr;
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if( MultiThread_python.flag == 1) {
				temp_fr.setVisible(true);
			}
			else if(MultiThread_python.flag == 2) {
				temp_fr.setVisible(false);
			}
		}
	}
}

class MultiThread_python extends Thread{
	static int flag;
	
	public void run() {
		try(ServerSocket server = new ServerSocket(8077)){
			while(true) {
				try{
					Socket connection = server.accept();
					System.out.println("python : " + connection.getInetAddress());
					Thread task = new Check(connection);
					task.start();
				}catch(IOException e){}
			}
		}catch(IOException e){
			System.err.println("���� ����.");
		}
	}
	private static class Check extends Thread{	
		private Socket connection;
		
		Check(Socket connection){
			this.connection=connection;
		}

		public void run(){
			try{				
				Thread.sleep(1000);
				
	    		while (true) {
	    		//BufferedWriter out = new BufferedWriter(
    			//			new OutputStreamWriter(connection.getOutputStream()));
				BufferedReader in = new BufferedReader (
	    				new InputStreamReader(connection.getInputStream()));
	    		
	    		String rev = in.readLine();
	    		//out.write(rev);
	    		
	    		int i = Integer.parseInt(rev);
	    		if (i==10) {	//lock on
	    			flag = 1;
	    		}
	    		else if (i==20)	//lock off
	    			flag = 2;
	    		}
	    		
			}catch(IOException e){
				System.err.println(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				try{
					connection.close();
				}catch(IOException e){}
			}
		}
	}
}


class MultiThread_c extends Thread{
	static int pid;
	
	public void run() {
		try(ServerSocket server = new ServerSocket(8078)){
			while(true) {
				try{
					Socket connection = server.accept();
					System.out.println("c : " + connection.getInetAddress());
					Thread task = new Check(connection);
					task.start();
				}catch(IOException e){}
			}
		}catch(IOException e){
			System.err.println("���� ����.");
		}
	}
	private static class Check extends Thread{	
		private Socket connection;
		String send;
		
		Check(Socket connection){
			this.connection=connection;
		}

		public void run(){
			try{				
	    		while (true) {
	    			Thread.sleep(1000);
	    			PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
	    			if (MultiThread_python.flag == 2)		//lock off
	    				send = "20";
	    			else if (MultiThread_python.flag == 1)	//lock on
	    				send = "10";
    				out.println(send);
	    		}
			}catch(IOException e){
				System.err.println(e);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				try{
					connection.close();
				}catch(IOException e){}
			}
		}
	}
}
