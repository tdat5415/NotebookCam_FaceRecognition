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
		
		//처음 실행시 등록된 이미지가 있는지 확인
		int cnt=0;
		File dir = new File("C:\\Project\\images");
		File[] list = dir.listFiles();
		
		//나중 사용자 리스트 출력에 사용
		for (File i : list) {
			if (i.isFile()) {
				cnt++;
			}
		}
		if(cnt == 0) {	//사용자 없을 시 사용자 등록
			System.out.println("사용자가 없습니다.");
			
			User usr = new User();
			if (usr.useradd() != 0)
				System.out.println("정상 등록되지 않았습니다.");
			else
				System.out.println("정상 등록 되었습니다.");
		}		

		JFrame fr = new JFrame("Frame");				//잠금프레임 생성
		ImageIcon ic = new ImageIcon("C:\\Project\\src\\background\\background.jpg");
		JLabel Image1 = new JLabel(ic);
		fr.add(Image1);
		Toolkit tk = Toolkit.getDefaultToolkit();		
		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""), new Point(), null);
		fr.setExtendedState(JFrame.MAXIMIZED_BOTH);		//전체 화면
        fr.setUndecorated(true);						//타이틀바 삭제
        fr.getContentPane().setBackground(Color.darkGray);
        fr.setCursor(invisCursor);
        fr.setVisible(true);
        fr.setAlwaysOnTop(true);						//최상단 위치
        
        
        
        Thread mtp = new MultiThread_python();			//통신 스레드
		mtp.start();
        Thread mtc = new MultiThread_c();				//통신 스레드
		mtc.start();
		Runnable r = new FlagCheck(fr);					//플래그 체크
		new Thread(r).start();
		
		//얼굴 인식 스크립트 호출
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

//플레그 체크 스레드
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




