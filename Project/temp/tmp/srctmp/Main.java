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
		User.getUserList();		//처음 실행할때마다 사용자 체크
		
		//잠금프레임
		JFrame fr = new JFrame("Frame");			
		ImageIcon ic = new ImageIcon("C:\\Project\\src\\background\\background.jpg");
		JLabel img = new JLabel(ic);
		fr.add(img);
		Toolkit tk = Toolkit.getDefaultToolkit();		
		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""), new Point(), null);
		fr.setCursor(invisCursor);						//커서 숨기기
		fr.setExtendedState(JFrame.MAXIMIZED_BOTH);		//전체 화면
	    fr.setUndecorated(true);						//타이틀바 삭제
	    fr.setVisible(true);							
	    fr.setAlwaysOnTop(true);						//최상단 위치
		
	    //스레드 시작
        Thread mtp = new MultiThread_python();			//python_통신 스레드
		mtp.start();
        Thread mtc = new MultiThread_c();				//c_통신 스레드
		mtc.start();
		Runnable r = new FlagCheck(fr);					//플래그 체크
		new Thread(r).start();
		
		
		//스크립트 실행
		try { 
			Process c = Runtime.getRuntime().exec(HOOK_PATH);
			c_pid = (int) c.pid();
			Process py = Runtime.getRuntime().exec(PY_COMMAND);
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
		
		//tray  등록
		TrayIcon trayicon=null;

		if(SystemTray.isSupported()) {
			SystemTray tray=SystemTray.getSystemTray();
			Image trayImage=Toolkit.getDefaultToolkit().getImage("\\icon.png");
		  
			ActionListener listener=new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.toString().contains("열기")) {
						System.out.println("설정창 열기");
					}
					else if(e.toString().contains("종료")) {
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
		   
			MenuItem defaultItem=new MenuItem("열기");
			defaultItem.addActionListener(listener);
			 
			MenuItem exit=new MenuItem("종료");
			exit.addActionListener(listener);
		   
			popup.add(defaultItem);
			popup.add(exit);
			trayicon=new TrayIcon(trayImage,"프로그램", popup);
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
			System.err.println("연결 에러.");
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
			System.err.println("연결 에러.");
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
