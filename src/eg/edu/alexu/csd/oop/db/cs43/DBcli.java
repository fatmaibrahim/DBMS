package eg.edu.alexu.csd.oop.db.cs43;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class DBcli {
	static MyDataBase dbms = new MyDataBase();
	public static void main(String[] args) throws SQLException, IOException{
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(is);
		while(true){
			boolean success;
			int numRows;
			System.out.println("Enter SQL Command: (Press 0 to end)");
			String query = br.readLine();
			if(query.toLowerCase().contains("create database")){
				String path =query.substring(15,query.length());
				System.out.println("Do you want to recreate if it exists ? (yes/no) ");
				String choice = br.readLine();
				if(choice.toLowerCase().equals("yes")){
					dbms.createDatabase(path, true);
				}else if(choice.toLowerCase().equals("no")){
					dbms.createDatabase(path, false);
				}else{
					 System.out.println("invalid input");
				}
			}
			else{
				if(query.toLowerCase().contains("create table")){
					success = dbms.executeStructureQuery(query);
					System.out.println("Creating table: "+ success);
				}else if(query.toLowerCase().contains("drop table")){
					success = dbms.executeStructureQuery(query);
					System.out.println("Droping table: "+ success);
				}
				else if(query.toLowerCase().contains("drop database")){
					success = dbms.executeStructureQuery(query);
					System.out.println("Droping database: "+ success);
				}else if(query.toLowerCase().contains("select")){
					System.out.println("selected Rows:");
					printRows(dbms.executeQuery(query));
				}
				else if(query.toLowerCase().contains("insert")){
					dbms.executeUpdateQuery(query);
					System.out.println("Row is inserted");
				}else if(query.toLowerCase().contains("delete")){
					dbms.executeUpdateQuery(query);
				}else if(query.toLowerCase().contains("update")){
					numRows = dbms.executeUpdateQuery(query);
					System.out.println("Number of updated rows = "+ numRows);
				}else if(query.equals("0")){
					break;
				}
				else{
					System.out.println("INVALID QUERY");
				}
			}
		}

	}
	public static void printRows(Object[][] array){
		for(int i=0;i<array.length;i++){
			for(int j=0;j<array[i].length;i++){
				System.out.println("Row "+(i+1));
				System.out.println(array[i][j]);
			}
		}
	}
}
