package eg.edu.alexu.csd.oop.db.cs43;

import java.sql.SQLException;

public class data {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyDataBase db = new MyDataBase();
		@SuppressWarnings("unused")
		Parser p = Parser.getInstance();
		
		
			//String path = db.createDatabase("sample" + System.getProperty("file.separator") + "name", true);
			//System.out.println(path);
		//try {
		//System.out.println(	db.createDatabase("SaMple", true));
		//System.out.println(db.createDatabase("sAmPlE", false));
		System.out.println(db.createDatabase("SAMPLE", true));
			
			
			//db.executeStructureQuery("create database"+" sample" + System.getProperty("file.separator") + "name");
			//db.executeStructureQuery("create database corn");
			//System.out.println(db.isValidPath("name"));
			//System.out.println(db.executeStructureQuery("creaTE TABLE table(id int  ,name varchar, country varchar) "));
		
			//System.out.println(db.executeStructureQuery("CREATE TABLE incomplete_table_name1"));
		//db.executeUpdateQuery("INSERT INTO  table(id, name, country) VALUES (1, 'ashraqat', 'egypt')");
		//db.executeUpdateQuery("INSERT INTO  table(id, name, country) VALUES (1, 'sheta', 'egypt')");
		//db.executeUpdateQuery("INSERT INTO  table(id, name, country) VALUES (1, 'sheta', 'egypt')");
		//System.out.println(	db.executeStructureQuery("drop database name"));
	//System.out.println(db.executeQuery("select  id,name from table"));
		//System.out.println(db.executeStructureQuery("creaTE TABLE table(id int  ,name varchar, country varchar) "));
///p.parse("UPDATE table SET id = 3, name = 'ahmed' where id =1");
//p.parse("creaTE TABLE table(id int  ,name varchar, country varchar) ");
//System.out.println(p.getColumns());

		//System.out.println(db.executeUpdateQuery("DELETE  FROM TABLE where id =1"));
		//db.executeStructureQuery("drop table table");
		//System.out.println(db.executeUpdateQuery("UPDATE table SET id = 3, name = 'ahmed' where id =1"));
		//	//p.parse("INSERT INTO  table_name1(column_NAME1, COLUMN_name2, column_name3) VALUES ('value1', 'value3', 4)");
			//System.out.println(p.getselectColumns());
		//} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
	//	}
		//db.executeStructureQuery("CREATE TABLE table_name2(column_name1 varchar , column_name2 int, column_name3 varchar) ");
		

//	}

}
}
