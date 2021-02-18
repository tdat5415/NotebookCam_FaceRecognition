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
	
	//����� ���͸� ����
	public void useradd() {
		System.out.print("����� �̸��� �Է����ּ��� : ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		setUser(name);
		command = command.concat(" "+name);
		
		//���� ����
		File Folder = new File(path);
		if (!Folder.exists()) {
			try{
			    Folder.mkdir(); //���� �����մϴ�.
			    System.out.println("������ �����Ǿ����ϴ�.");
		    } 
		    catch(Exception e){
			    e.getStackTrace();
			}        
	    }
		else {
			System.out.println("�̹� ���� �̸��� ����ڰ� �����Ǿ� �ֽ��ϴ�.");
			return;
		}
			
		//��ũ��Ʈ ȣ��
		try { 
			Process p = Runtime.getRuntime().exec(command); 
		}	
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
	}
	
	
	
	
	//����� �̸�, ������� ����
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


