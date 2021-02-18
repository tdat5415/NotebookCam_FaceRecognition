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
        fr.setExtendedState(JFrame.MAXIMIZED_BOTH);		//전체 화면
        fr.setUndecorated(true);						//타이틀바 삭제
        fr.getContentPane().setBackground(Color.darkGray);
        fr.setVisible(true);
        fr.setAlwaysOnTop(true);						//최상단 위치
        
		Thread mt = new MultiThread();
		mt.start();
		
		Runnable r = new FlagCheck(fr);
		new Thread(r).start();
		
		//얼굴 인식 스크립트 호출
		try { 
			Process p = Runtime.getRuntime().exec(command); 
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
        //키보드 입력 제어
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
        //프레임에서 키 입력받기 시작
        fr.addKeyListener(new key()); 
    }
}


//마우스 제어
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


//플레그 체크 스레드
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









