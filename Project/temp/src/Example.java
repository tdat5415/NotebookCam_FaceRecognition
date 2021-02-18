import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;


import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Example exturn User{
	public static void main(String[] args) throws InterruptedException, AWTException, IOException {
		
		String command = "python C:\\Project\\src\\socket_test2.py";
		
		//ó�� ����� ��ϵ� �̹����� �ִ��� Ȯ��
		int cnt=0;
		File dir = new File("C:\\Project\\images");
		File[] list = dir.listFiles();
		
		//���� ����� ����Ʈ ��¿� ���
		for (File i : list) {
			if (i.isFile()) {
				cnt++;
			}
		}
		if(cnt == 0) {	//����� ���� �� ����� ���
			System.out.println("����ڰ� �����ϴ�.");
			
			User usr = new User();
			if (usr.useradd() != 0)
				System.out.println("���� ��ϵ��� �ʾҽ��ϴ�.");
			else
				System.out.println("���� ��� �Ǿ����ϴ�.");
		}		

		JFrame fr = new JFrame("Frame");				//��������� ����
		ImageIcon ic = new ImageIcon("C:\\Project\\src\\background\\background.jpg");
		JLabel Image1 = new JLabel(ic);
		fr.add(Image1);
		Toolkit tk = Toolkit.getDefaultToolkit();		
		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""), new Point(), null);
		fr.setExtendedState(JFrame.MAXIMIZED_BOTH);		//��ü ȭ��
        fr.setUndecorated(true);						//Ÿ��Ʋ�� ����
        fr.getContentPane().setBackground(Color.darkGray);
        fr.setCursor(invisCursor);
        fr.setVisible(true);
        fr.setAlwaysOnTop(true);						//�ֻ�� ��ġ
        
        
        
        Thread mtp = new MultiThread_python();			//��� ������
		mtp.start();
        Thread mtc = new MultiThread_c();				//��� ������
		mtc.start();
		Runnable r = new FlagCheck(fr);					//�÷��� üũ
		new Thread(r).start();
		
		//�� �ν� ��ũ��Ʈ ȣ��
		try { 
			Process p = Runtime.getRuntime().exec(command); 
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		//key,mouse hook
		try {
			Process p1 = Runtime.getRuntime().exec("C:\\Project\\src\\keyhook.exe");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }
}

//�÷��� üũ ������
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
	    		//PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader in = new BufferedReader (
	    				new InputStreamReader(connection.getInputStream()));
	    		
				
	    		String rev = in.readLine();
	    		//out.println(rev);
	    		//System.out.println("Send : "+rev);
	    		
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
	public void run() {
		try(ServerSocket server = new ServerSocket(8078)){
			while(true) {
				try{
					Socket connection = server.accept();
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
	    			if (MultiThread_python.flag == 2)
	    				send = "20";
	    			else if (MultiThread_python.flag == 1)
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




