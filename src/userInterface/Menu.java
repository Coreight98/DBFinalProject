package userInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.Statement;

import parser.Item;
import parser.Paser;
import api.KakaoAPI;
import database.Database;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Menu{
    
	static Tool function = new Tool();
	static Scanner in = new Scanner(System.in);
	static ArrayList<Item> list = new ArrayList<>();
	public static void main(String[] args) throws Exception {
		//�Ŵ� 1�ϰ� 15�Ͽ� db������Ʈ
		SimpleDateFormat format = new SimpleDateFormat("dd");
		Calendar time = Calendar.getInstance();
		int currentDay =Integer.valueOf(format.format(time.getTime()));
		if(currentDay==1||currentDay==15) {
			Paser.main(args);
		}
		
		loadFromCSV();
		Database database = new Database();
		System.out.println("������ ����ǰ �˻� ���α׷��Դϴ�.");
		menu();
		
	}
	public static void menu() throws FileNotFoundException, SQLException {
		
		
		System.out.println("--------------�޴�--------------");
		System.out.println("1.��ǰ �˻�\t2.��ü ��ǰ ����Ʈ\t3.�ֺ� ������ ���\t4.���α׷� ����");
		try {
		int menuSelect = in.nextInt();
		switch(menuSelect) {
		case 1:
			System.out.println("��ǰ�� �˻��մϴ�.");
			searchItemMenu();
			break;
		case 2:
			showEntryList();
			break;
			
		case 3:
			System.out.println("�� m �ݰ� ���� �ִ� �������� Ȯ���Ͻðڽ��ϱ�? : ");
			int radius = in.nextInt();
			System.out.println(radius+"m ���� ������ ����� �����ݴϴ�.");
			KakaoAPI.find(radius);
			menu();
		case 4:
			System.out.println("���α׷��� �����մϴ�. ���� �Ϸ� �Ǽ���~");
			System.exit(0);
		default :
			menu();
			break;
				
		}
		}catch(InputMismatchException e) {
			menu();
		}
	}
	private static void loadFromCSV() throws FileNotFoundException {
		
		Path path = Paths.get("Type_All.csv");
		File file = new File(path.toUri());
		Scanner sc = new Scanner(file);
		sc.nextLine();
		while(sc.hasNextLine()) {
			String temp = sc.nextLine();
			String[] splited = temp.split(",");
			
			list.add(new Item(splited[0],Integer.valueOf(splited[1]),splited[2],splited[3]));
		}
		System.out.println("Database load complete!!");
		sc.close();
	}
	
	private static void showEntryList() throws FileNotFoundException, SQLException {
		//20���� �����ֱ�, 1~maxPage���� �������� ����Ʈ ����, 0�Է½� �޴���
		System.out.println("�� ��ǰ ���� :"+list.size());
		System.out.println("�˻� ���͸� �������ּ���.");
		System.out.println("1. �⺻ ���� 2. ���� �������� 3. ���� �������� 4. ��纰");
		System.out.println("------------��ü ��ǰ ���------------");
		int idx=0;
		int next=-1;
		for(;;) {
			if(next==-1) {
				System.out.println((next+2)+" ������/"+((list.size()/20)+1)+"������");
			}else {
				System.out.println((next)+" ������/"+((list.size()/20)+1)+"������");
			}
			System.out.printf("\t%-15s\t\t%15s\t\t\t\t%s\t\t       %s\n","������","��ǰ��","����","���");
			
			for(int i=idx;i<idx+20;i++) {
				try {
					System.out.printf("\t%-15s\t\t%-40s\t    %-20s%s\n"
							,list.get(i).getBrand(),list.get(i).getName(),list.get(i).getPrice(),list.get(i).getEvent());
				}catch(IndexOutOfBoundsException e) {
					break;
				}
			}
			System.out.println("(�޴� : 0) \t���ϴ� ������ : ");
			try {
				next = in.nextInt();
				if(next == 0 ) {
					System.out.println("�޴�ȭ������ ���ư��ϴ�.");
					menu();
				}else if(next > ((list.size()/20)+1)) {
					System.out.println("���� ������ �Դϴ�. ù �������� ���ư��ϴ�.");
					showEntryList();
				}
				else {
					idx=(next-1)*20;
					continue;
				}
			}catch(InputMismatchException e) {
				System.out.println("�޴�ȭ������ ���ư��ϴ�.");
				in = new Scanner(System.in);
				menu();
			}
			
		}
	}
	
	private static void showList(ArrayList<Item> list) throws FileNotFoundException, SQLException {
		//20���� �����ֱ�, 1~maxPage���� �������� ����Ʈ ����, 0�Է½� �޴���
	
		Statement statement = Database.connect().createStatement();
		if(list.size()!=0) {
			in = new Scanner(System.in);
			System.out.println("�� ��ǰ ���� :"+list.size());
			
			
			System.out.println("--------------��ǰ ���--------------");
			int idx=0;
			int next=-1;
			for(;;) {
				if(next==-1) {
					System.out.println((next+2)+" ������/"+((list.size()/20)+1)+"������");
				}else {
					System.out.println((next)+" ������/"+((list.size()/20)+1)+"������");
				}
				System.out.printf("\t%-15s\t\t%15s\t\t\t\t%s\t\t       %s\n","������","��ǰ��","����","���");
				
				for(int i=idx;i<idx+20;i++) {
					try {
						System.out.printf("\t%-15s\t\t%-40s\t    %-20s%s\n"
								,list.get(i).getBrand(),list.get(i).getName(),list.get(i).getPrice(),list.get(i).getEvent());
					}catch(IndexOutOfBoundsException e) {
						break;
					}
				}
				
				System.out.println("(�޴� : 0) \t���ϴ� ������ : ");
				try {
					next = in.nextInt();
					if(next == 0 ) {
						System.out.println("�޴�ȭ������ ���ư��ϴ�.");
						menu();
					}else if(next > ((list.size()/20)+1)) {
						System.out.println("���� ������ �Դϴ�. ù �������� ���ư��ϴ�.");
						showList(list);
					}
					else {
						idx=(next-1)*20;
						continue;
					}
				}catch(InputMismatchException e) {
					System.out.println("�޴�ȭ������ ���ư��ϴ�.");
					in = new Scanner(System.in);
					menu();
				}
			}
		}
		System.out.println("�޴�ȭ������ ���ư��ϴ�.");
		menu();
	}
	
	private static void searchItemMenu() throws SQLException {
		//�̸����� ��ǰ �˻�. ���ڿ� �����ϴ� ��� ��ǰ �����ֱ�. 0�� �Է��ϸ� �޴���
		ArrayList<Item> found = new ArrayList<>();
		System.out.println("�˻� ���͸� �������ּ���.");
		System.out.println("1. ��ǰ��\t\t2. ���\t\t0. ���� �޴�");
		try {
			int selectMenu = in.nextInt();
			switch(selectMenu) {
			case 1:
				found = function.searchName(list);
				showList(found);
				break;
			case 2:
				found = function.searchEvent(list);
				showList(found);
				break;
			case 0:
				System.out.println("���� �޴��� ���ư��ϴ�.");
				try {
					menu();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch(InputMismatchException e) {
			System.out.println("�Է��� �����Դϴ�. �ٽ� �õ����ּ���.");
			searchItemMenu();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
	}
}

class Tool extends ToolClass{
	Scanner in = new Scanner(System.in);
	@Override
	public ArrayList<Item> searchName(ArrayList<Item> list){
		ArrayList<Item> found = new ArrayList<>();
		String ItemName="";
	try {
			Statement statement = Database.connect().createStatement();
		 do{ 
			System.out.println("ã����� ��ǰ���� �Է����ּ��� : ");
			ItemName =  in.next();
			if(ItemName.length()<2) {
				System.out.println("�� ���� �̻��� �Է����ּ���.");
				
			}
		}while(ItemName.length()<2);
		 String brand= searchBrand();
		 
		 System.out.println("�˻� ���͸� �������ּ���.");
			System.out.println("1. �⺻ ����\t2. ���� ��������\t3. ���� ��������\t4. �ֺ� ������");
			/*  
			 * KakaoAPI.find(radius); �ֺ� ������ ���̺� ����. 
			 * list = SQL.query(�ֺ� ������ ���̺� natural join ItemView ���̺�);
			 *
			 */
			
			int filter=in.nextInt();
			switch(filter) {
			case 1:
				break;
			case 2:
				list = SQL.SortByPrice(statement, ItemName, brand);
				return list;
	
			case 3:
				list = SQL.SortByPriceDesc(statement, ItemName, brand);
				return list;
			case 4:
				System.out.println("�� m �ݰ� ���� �ִ� �������� Ȯ���Ͻðڽ��ϱ�? : ");
				int radius = in.nextInt();
				System.out.println(radius+"m ���� ������ ����� �����ݴϴ�.");
				KakaoAPI.find(radius);
				System.out.println("�ش� ���������� �Ǹ��ϴ� ��ǰ ����Դϴ�.");
				//list = SQL.query(statement, "Select pID, bName, pName, price, eName From  ");
				break;
			}
		 
		 
		 
		//sql������ �˻� �� found�� ����
		
		
			if(brand != "all") {
				found = SQL.query(statement,
						"Select pID, bName, pName, price, eName From Product Where pName like concat('%','"+ItemName+"', '%') and bName like concat('%','"+brand+"','%');");
				
			}else {
				found =SQL.query(statement,
						"Create view ItemView as Select pID, bName, pName, price, eName From Product Where pName like concat('%','"+ItemName+"', '%');");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return found;
	}

	@Override
	public ArrayList<Item> searchEvent(ArrayList<Item> list) {
		ArrayList<Item> found = new ArrayList<>();
		int toFind=0;
		while(toFind==0) {
			try{
				Statement statement;
				String brand;
				statement = Database.connect().createStatement();
				System.out.println("���� �˻��մϴ�. ã����� ��縦 �������ּ���.");
				System.out.println("1. 1+1\t2. 2+1\t3. 3+1");
				toFind = in.nextInt();
				switch(toFind) {
				case 1:
					System.out.println("1+1��� ��ǰ�� �˻��մϴ�.");
					brand = searchBrand();
					found=SQL.query(statement,"Select pID, bName, pName, price, eName From Product Where eName = '1+1' and bName like concat('%','"+brand+"','%');");
					break;
				case 2:
					System.out.println("2+1��� ��ǰ�� �˻��մϴ�.");
					brand = searchBrand();
					found=SQL.query(statement,"Select pID, bName, pName, price, eName From Product Where eName = '2+1' and bName like concat('%','"+brand+"','%');");
					toFind=2;
					break;
				case 3:
					System.out.println("3+1��� ��ǰ�� �˻��մϴ�.");
					brand = searchBrand();
					found=SQL.query(statement,"Select pID, bName, pName, price, eName From Product Where eName = '3+1' and bName like concat('%','"+brand+"','%');");
					break;
				default:
					toFind=0;
					System.out.println("�ٽ� �������ּ���.");
					break;
				}
			}catch(InputMismatchException e) {
				toFind=0;
				System.out.println("�ٽ� �������ּ���.");
			}catch (SQLException e) {
					e.printStackTrace();
				}
		}	
		return found;
	}

	@Override
	public String searchBrand() {
		int toFind=-1;
		while(toFind==-1) {
			System.out.println("�������� �������ּ���.(1.�̴Ͻ���\t2.GS25\t3.CU\t4.�̸�Ʈ24\t5.�����Ϸ���)\n��� �������� ��ǰ�� ���÷��� '0'�� �Է����ּ���.");
			try{
				toFind =  in.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("1~5�� �Է����ּ���.");
				searchBrand();
			}
			switch(toFind) {
				case 1:
					/*
					 *Qeury�̿��� ã�� 
					 */
					System.out.println("�̴Ͻ��鿡�� �˻��մϴ�.");
					return "�̴Ͻ���";
				case 2:
					/*
					 *Qeury�̿��� ã�� 
					 */
					System.out.println("GS25���� �˻��մϴ�.");
					return "GS25";
				case 3:
					/*
					 *Qeury�̿��� ã�� 
					 */
					System.out.println("CU���� �˻��մϴ�.");
					return "CU";
				case 4:
					/*
					 *Qeury�̿��� ã�� 
					 */
					System.out.println("�̸�Ʈ24���� �˻��մϴ�.");
					return "�̸�Ʈ24";
				case 5:
					/*
					 *Qeury�̿��� ã�� 
					 */
					System.out.println("�����Ϸ��쿡�� �˻��մϴ�.");
					return "�����Ϸ���";
				case 0:
					System.out.println("��ü ������ ��Ͽ��� �˻��մϴ�.");
					return "all";
				default:
					toFind=-1;
					System.out.println("�ٽ� �������ּ���.");
					break;
			}
		}
		return "all";
	}

	@Override
	public ArrayList<Item> searchClosest(ArrayList<Item> list) {
		ArrayList<Item> found = new ArrayList<>();
		int radius=1000;
		System.out.println("�� ���� �̳��� �ִ� �������� ã���ðڽ��ϱ�?(�⺻ : 1000m) : ");
		radius = in.nextInt();
		try {
			KakaoAPI.find(radius);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return found;
	}

	@Override
	public ArrayList<Item> sortPrice(ArrayList<Item> list) {
		ArrayList<Item> found = new ArrayList<>();
		
		
		return found;
	}
}


