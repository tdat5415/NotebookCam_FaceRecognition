
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

class User {
	static final private String IMG_PATH = "C:\\Project\\images\\";
	static private String name;
	static private String command = "python C:\\Project\\bin\\capture.py";
	
	static public int check_file() {
		String temp_path = IMG_PATH+name+".jpg";
		File file = new File(temp_path);
		
		if(file.exists()) {
			System.out.println("�̹� ���� �̸��� ����ڰ� �����մϴ�.");
			return -1;
		}
		return 0;
	}
	
	static public int useradd() throws IOException, InterruptedException {
		//����
		String taskkill = "taskkill /F /IM \"python.exe\"";
		try { 
			Process task = Runtime.getRuntime().exec(taskkill); 
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
		
		
		System.out.print("����� �̸��� �Է����ּ��� : ");
		Scanner sc = new Scanner(System.in);
		String temp_name = sc.nextLine();
		
		User.setUser(temp_name);
		
		if (check_file() == 0) {
			try { 
				Process p1 = Runtime.getRuntime().exec(command); 
				p1.waitFor();
				Process p2 = Runtime.getRuntime().exec("python C:\\Project\\bin\\face_regcog.py");
			}	
			catch (IOException e) { 
				e.printStackTrace(); 
				return -1;	//��� ����
			}
    	}
		return 0;	//���� ���
	}
	
	
	static public void setUser(String name) {
		User.name = name;
		User.command = command.concat(" "+name);
	}
	
	
	public static ArrayList<String> getUserList() throws IOException, InterruptedException {
		File file = new File(IMG_PATH);
		File[] listFiles = file.listFiles();
		int count=0;
		
		ArrayList<String> names = new ArrayList<String>();//���⿡ �̸��� ��´�
		String name;
		
		//System.out.println("###����ڸ��###");
		for(File f : listFiles) {
			if(f.isFile()) {
				String fileName = f.getName();
				if(fileName.contains(".png") || fileName.contains(".jpg")) {//�̹����� 
					name = fileName.split("\\.")[0];
					names.add(name);//����Ʈ�� �̸� �߰�
					count++;
					//System.out.println(name);
					//�̹��������� �̸��� ���   ��, ��������
				}
			}
		}
		if (count==0) {
			System.out.println("����ڰ� ���� ����ڸ� �߰��մϴ�.");
			useradd();
		}
		return names;//����Ʈ ���
	}
	
	
	public static int delUser(String name) {
		File file = new File(IMG_PATH);
		File[] listFiles = file.listFiles();
		
		for(File f : listFiles) {
			
			if(f.isFile()) {//������ �ƴϰ� �����ΰ�
				String fileName = f.getName();
				
				if(fileName.contains(".png") || fileName.contains(".jpg")) {//�̹����ΰ� 
					
					if(fileName.indexOf(name) != -1) {//xxx.jpg �� name�� ������ 
						f.delete();
						
						//System.out.println(name + "����ڰ� �����Ǿ����ϴ�.");
						return 0;
					}
				}
			}
		}
		
		//System.out.println("������ ���ų� �������� �ʾҽ��ϴ�.");
		return -1;
	}
	
}