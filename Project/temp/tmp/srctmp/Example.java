import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
 
import javax.swing.JFrame;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Example {
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
        fr.setExtendedState(JFrame.MAXIMIZED_BOTH);		//��ü ȭ��
        fr.setUndecorated(true);						//Ÿ��Ʋ�� ����
        fr.getContentPane().setBackground(Color.darkGray);
        fr.setVisible(true);
        fr.setAlwaysOnTop(true);						//�ֻ�� ��ġ
        
		Thread mt = new MultiThread();
		mt.start();
		
		Runnable r = new FlagCheck(fr);
		new Thread(r).start();
		
		//�� �ν� ��ũ��Ʈ ȣ��
		try { 
			Process p = Runtime.getRuntime().exec(command); 
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
        //Ű���� �Է� ����
        Robot rb = new Robot();  
        Runnable mc = new MouseControl(rb);
        new Thread(mc).start();
        
        class key implements KeyListener{
			@Override
			public void keyTyped(KeyEvent e) {
			}	
			@Override
			public void keyPressed(KeyEvent e) {
				/*
				if(e.getKeyCode() == KeyEvent.VK_ALT)
					rb.keyRelease(KeyEvent.VK_ALT);
				else if(e.getKeyCode() == KeyEvent.VK_F4)
					rb.keyRelease(KeyEvent.VK_F4);
				else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
					rb.keyRelease(KeyEvent.VK_ESCAPE);
				else if(e.getKeyCode() == KeyEvent.VK_TAB)
					rb.keyRelease(KeyEvent.VK_TAB);
				else if(e.getKeyCode() == KeyEvent.VK_DELETE)
					rb.keyRelease(KeyEvent.VK_DELETE);
				*/
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
        }
        //�����ӿ��� Ű �Է¹ޱ� ����
        fr.addKeyListener(new key()); 
    }
}


//���콺 ����
class MouseControl implements Runnable {
	Robot temp_rb;
	MouseControl(Robot rb) {
		temp_rb = rb;
	}
	
	public void run() {
		while(true) {
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			System.out.println("FLAG : "+ MultiThread.flag);
				while(MultiThread.flag == 1) {
					temp_rb.mouseMove(540, 960);
				}
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
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			*/
			System.out.println("FLAG : "+ MultiThread.flag);
			if(MultiThread.flag == 1) {
				temp_fr.setVisible(true);
			}
			else if(MultiThread.flag == 2) {
				temp_fr.setVisible(false);
			}
		}
	}
}



class MultiThread extends Thread{
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
	    		while (true) {
	    		//PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader in = new BufferedReader (
	    				new InputStreamReader(connection.getInputStream()));
	    		
	    		
	    		String rev = in.readLine();
	    
	    		int i = Integer.parseInt(rev);
	    		if (i==10)
	    			flag = 1;
	    		else if (i==20)
	    			flag = 2;
	    		}
	    		//out.println("Echo : "+i);
	    		//System.out.println("Send : "+i);
			}catch(IOException e){
				System.err.println(e);
			}finally{
				try{
					connection.close();
				}catch(IOException e){}
			}
		}
	}
}









