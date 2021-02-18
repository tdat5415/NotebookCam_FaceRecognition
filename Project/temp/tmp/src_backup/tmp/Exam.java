import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
 
import javax.swing.JFrame;

import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.io.*;
import java.net.*;

public class Exam {
	public static void main(String[] args) throws InterruptedException, AWTException {
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
		
        //Ű���� �Է� ����
        Robot rb = new Robot();      
        
        /*���콺 ����
         * Robot rb = new Robot();
         * 
		
		while(true) {
			rb.mouseMove(200, 200);
		}
        */
        
        //Ű �Է� ������ Ŭ����
        class key implements KeyListener{
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub		
			}	
			//��� Ű �Է� ���� ��
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
				if(e.getKeyCode() == KeyEvent.VK_ALT)
					fr.setVisible(false);
				//rb.keyRelease(KeyEvent.VK_TAB);
				//rb.keyRelease(KeyEvent.VK_F4);
				//rb.keyRelease(KeyEvent.VK_CONTROL);
				//rb.keyRelease(KeyEvent.VK_ESCAPE);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
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
			temp_rb.mouseMove(200, 200);
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
			//1�� �������� �÷��װ� Ȯ��
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("FLAG : "+ MultiThread.flag);
			if(MultiThread.flag == 1) {
				System.out.println("Screen ON");
				temp_fr.setVisible(true);
			}
			else if(MultiThread.flag == 2) {
				System.out.println("Screen OFF");
				temp_fr.setVisible(false);
			}
		}
	}
}


//��� ������
class MultiThread extends Thread{
	static int flag;
	
	public void run() {
		//���� ����
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

	//������ �ְ�ޱ�
	private static class Check extends Thread{	
		private Socket connection;
		Check(Socket connection){
			this.connection=connection;
		}

		public void run(){
			try{
	    		while (true) {
				BufferedReader in = new BufferedReader (
	    				new InputStreamReader(connection.getInputStream()));
	    		//PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
	    		
	    		String rev = in.readLine();
	    
	    		int i = Integer.parseInt(rev);
	    		if (i==10)
	    			flag = 1;
	    		else if (i==20)
	    			flag = 2;
	    		
	    		System.out.println("Received : "+i);
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









