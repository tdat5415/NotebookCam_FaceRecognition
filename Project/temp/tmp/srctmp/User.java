
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
			System.out.println("이미 같은 이름의 사용자가 존재합니다.");
			return -1;
		}
		return 0;
	}
	
	static public int useradd() throws IOException, InterruptedException {
		//종료
		String taskkill = "taskkill /F /IM \"python.exe\"";
		try { 
			Process task = Runtime.getRuntime().exec(taskkill); 
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		
		
		
		System.out.print("사용자 이름을 입력해주세요 : ");
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
				return -1;	//등록 에러
			}
    	}
		return 0;	//정상 등록
	}
	
	
	static public void setUser(String name) {
		User.name = name;
		User.command = command.concat(" "+name);
	}
	
	
	public static ArrayList<String> getUserList() throws IOException, InterruptedException {
		File file = new File(IMG_PATH);
		File[] listFiles = file.listFiles();
		int count=0;
		
		ArrayList<String> names = new ArrayList<String>();//여기에 이름을 담는다
		String name;
		
		//System.out.println("###사용자명단###");
		for(File f : listFiles) {
			if(f.isFile()) {
				String fileName = f.getName();
				if(fileName.contains(".png") || fileName.contains(".jpg")) {//이미지만 
					name = fileName.split("\\.")[0];
					names.add(name);//리스트에 이름 추가
					count++;
					//System.out.println(name);
					//이미지파일의 이름만 출력   즉, 사용자출력
				}
			}
		}
		if (count==0) {
			System.out.println("사용자가 없어 사용자를 추가합니다.");
			useradd();
		}
		return names;//리스트 출력
	}
	
	
	public static int delUser(String name) {
		File file = new File(IMG_PATH);
		File[] listFiles = file.listFiles();
		
		for(File f : listFiles) {
			
			if(f.isFile()) {//폴더가 아니고 파일인가
				String fileName = f.getName();
				
				if(fileName.contains(".png") || fileName.contains(".jpg")) {//이미지인가 
					
					if(fileName.indexOf(name) != -1) {//xxx.jpg 에 name이 있으면 
						f.delete();
						
						//System.out.println(name + "사용자가 삭제되었습니다.");
						return 0;
					}
				}
			}
		}
		
		//System.out.println("파일이 없거나 삭제되지 않았습니다.");
		return -1;
	}
	
}