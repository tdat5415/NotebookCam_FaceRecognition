import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

class User {
	private String name;
	private String path = "C:\\Project\\images\\";	//picture path
	private String command = "python C:\\Project\\src\\capture.py";	
	
	public int check_file() {
		String temp_path = path+name+".jpg";
		File file = new File(temp_path);
		
		if(file.exists()) {
			System.out.println("�̹� ���� �̸��� ����ڰ� �����մϴ�.");
			return -1;
		}
		return 0;
	}
	
	public int useradd() throws IOException, InterruptedException {
		System.out.print("����� �̸��� �Է����ּ��� : ");
		Scanner sc = new Scanner(System.in);
		String name = sc.nextLine();
		
		setUser(name);
		
		if (check_file() == 0) {
			try { 
				Process p = Runtime.getRuntime().exec(command); 
				p.waitFor();
			}	
			catch (IOException e) { 
				e.printStackTrace(); 
				return -1;	//��� ����
			}
    	}
		return 0;	//���� ���
	}
	
	public void setUser(String name) {
		this.name = name;
		this.command = command.concat(" "+name);
	}
	
	/*
	public static ArrayList<String> getUserList() {
		File file = new File(path);
		File[] listFiles = file.listFiles();
		
		ArrayList<String> names = new ArrayList<String>();//���⿡ �̸��� ��´�
		String name;
		
		System.out.println("###����ڸ��###");
		for(File f : listFiles) {
			if(f.isFile()) {
				String fileName = f.getName();
				if(fileName.contains(".png") || fileName.contains(".jpg")) {//�̹����� 
					name = fileName.split("\\.")[0];
					names.add(name);//����Ʈ�� �̸� �߰�
					
					
					System.out.println(name);
					//�̹��������� �̸��� ���   ��, ��������
				}
			}
		}
		return names;//����Ʈ ���
	}
	*/
	/*
	public static void delUser(String name) {
		File file = new File(path);
		File[] listFiles = file.listFiles();
		
		for(File f : listFiles) {
			
			if(f.isFile()) {//������ �ƴϰ� �����ΰ�
				String fileName = f.getName();
				
				if(fileName.contains(".png") || fileName.contains(".jpg")) {//�̹����ΰ� 
					
					if(fileName.indexOf(name) != -1) {//xxx.jpg �� name�� ������ 
						f.delete();
						
						System.out.println(name + "����ڰ� �����Ǿ����ϴ�.");
						return ;
					}
				}
			}
		}
		
		System.out.println("������ ���ų� �������� �ʾҽ��ϴ�.");
	}
	*/
}