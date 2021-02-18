import java.util.Scanner;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;

class User {
	private String name;
	private String path;	//picture path
	private String tmp_path = "C:\\Test\\";
	private String command = "python C:\\Project\\capture.py";	
	
	//사용자 디렉터리 생성
	public void useradd() {
		System.out.print("사용자 이름을 입력해주세요 : ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		setUser(name);
		command = command.concat(" "+name);
		
		//폴더 생성
		File Folder = new File(path);
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //폴더 생성합니다.
			    System.out.println("폴더가 생성되었습니다.");
		    } 
		    catch(Exception e){
			    e.getStackTrace();
			}        
	    }
		else {
			System.out.println("이미 같은 이름의 사용자가 생성되어 있습니다.");
			return;
		}
			
		//스크립트 호출
		try { 
			Process p = Runtime.getRuntime().exec(command); 
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
	}
	
	
	
	
	//사용자 이름, 사진경로 설정
	public void setUser(String name) {
		this.name = name;
		this.path = tmp_path.concat(name);
	}
}//end User class




//main
public class login {
	public static void main(String[] args) throws AWTException {
		System.out.println("-Program Start-");
		
		
		User usr = new User();
		usr.useradd();
		
		/*
		Robot rb = new Robot();
		
		while(true) {
			rb.mouseMove(200, 200);
		}
		*/
	}
}


