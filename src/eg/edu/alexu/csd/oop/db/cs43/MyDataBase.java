package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import eg.edu.alexu.csd.oop.db.Database;

public class MyDataBase implements Database {

	Parser p = Parser.getInstance();
	File database;
	File table;
	public String absolutePath;
	public String folder;
	int counter = 0;
	XMLtoArray xmltoarray ;
	public LinkedList<String> col = new LinkedList<String>(); // sala7y
																// el-warning
																// matesebesh
																// 7aga keda :D
	public LinkedList<String> action = new LinkedList<String>();
	public boolean createWDB;

	public MyDataBase() {
		File f = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "Databases");
		f.mkdir();
		absolutePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "Databases"
				+ System.getProperty("file.separator");
	}

	@Override
	public String createDatabase(String databaseName, boolean dropIfExists) {
		// folder.equals(databaseName);
		this.folder = databaseName;
		File file = new File(databaseName);
		if ((dropIfExists) && ((file.exists()))) {
			try {
				// delete the database
				executeStructureQuery("DROP DATABASE ".concat(databaseName).concat(";"));
				// then create it
				executeStructureQuery("CREATE DATABASE ".concat(databaseName).concat(";"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if ((!file.exists())) {
			try {
				executeStructureQuery("CREATE DATABASE ".concat(databaseName).concat(";"));

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if ((!dropIfExists) && ((file.exists()))) {
			return file.getAbsolutePath();

		}
		return absolutePath;

	}

	@Override
	public boolean executeStructureQuery(String query) throws java.sql.SQLException {
		Parser p = Parser.getInstance();
		try {
			p.parse(query);
			throw new RuntimeException(query);
		} catch (Exception e) {

		}

		boolean success = true;
		if (p.gettype().equals("create database")) {
			success = createDataBase(p.getDataBaseName());
			createWDB = true;
			// System.out.println("DEBUG: executeStructureQuery >> " + success);
		} else if (p.gettype().equals("drop database")) {
			createWDB = false;
			success = dropDataBase(p.getDataBaseName());
		} else if (p.gettype().equals("create table")) {
			Tables t = new Tables();
			if (createWDB) {
				success = t.createTable(absolutePath);
			} else {
				throw new SQLException();
			}
		} else if (p.gettype().equals("drop table")) {
			if (createWDB) {
				Tables t = new Tables();
				success = t.droptable();
			} else {
				throw new SQLException();
			}

		} else {
			throw new java.sql.SQLException();
		}

		return success;
	}

	@Override
	public Object[][] executeQuery(String query) throws SQLException {
		Parser p = Parser.getInstance();
		if (query.contains(">")) {
			int operationIndex = query.indexOf('>');
			if (query.substring(operationIndex).contains("'")) {
				throw new SQLException();
			}
		} else if (query.contains("<")) {
			int operationIndex = query.indexOf('<');
			if (query.substring(operationIndex).contains("'")) {
				throw new SQLException();
			}
		}
		p.parse(query);
	    xmltoarray = new XMLtoArray();
		String operation = p.gettype();
		LinkedList<String> columnNames = p.getselectColumns();
		ArrayList<Object> columnsNames = new ArrayList<Object>(columnNames);
		ArrayList<Object> condition = new ArrayList<Object>(p.getwhere());
		String path ;
		if(isValidPath(p.getDataBaseName())){
			path  =	System.getProperty("user.dir")+System.getProperty("file.separator")+p.getDataBaseName() + System.getProperty("file.separator") +p.getTableName()+".xml";
		}else{
		path  =	System.getProperty("user.dir")+System.getProperty("file.separator")+"Databases"+ System.getProperty("file.separator") +p.getDataBaseName() + System.getProperty("file.separator") +p.getTableName()+".xml";
		}
		File file = new File(path);
		if(!file.exists()){
			throw new SQLException();
		}
		String tablePath = path ;
		
		switch (operation.toLowerCase()) {
		case "select":
			return xmltoarray.selectAll(tablePath, columnsNames, condition);

		default:
			return null;
		}
	}

	@Override
	public int executeUpdateQuery(String query) throws SQLException {
		col = p.getColumns();
		Parser p = Parser.getInstance();
		p.parse(query);
		int counter = 0;
		if (query.toLowerCase().contains("insert")) {

			Insert i = new Insert();
			i.InsertinTable();
			counter = 1;
		}
		if (query.toLowerCase().contains("delete")) {
			if ((!col.isEmpty()) && (!(p.getcondition().isEmpty()))) {

				Delete d = new Delete();
				counter = d.deleteFromtable();
			} else {
				// throw new SQLException();
				//counter = 0;
			}
		}

		if (query.toLowerCase().contains("update")) {
			if (!col.isEmpty()) {

				Update u = new Update();
				counter = u.UpdateData();

			} else {
				// throw new SQLException();
				counter = 0;
			}
		}

		return counter;
	}

	public boolean createDataBase(String dataBaseName) {
		folder = dataBaseName;
		if (isValidPath(dataBaseName)) {
			// System.out.println("DEBUG: createDataBase >> valid");
			database = new File(dataBaseName);
			if (!database.exists()) {
				boolean done = database.mkdirs();
				if (!done) {
					return false;
				} else {
					absolutePath = database.getAbsolutePath();
				}
			}
			if (database.exists()) {
				absolutePath = database.getAbsolutePath();
			}
			return true;
		}
		// sd.save(dataBaseName);
		else {
			// System.out.println("DEBUG: createDataBase >> not valid");
			database = new File("Databases" + System.getProperty("file.separator") + dataBaseName);
			absolutePath = database.getAbsolutePath();
			if (!database.exists()) {
				boolean done = database.mkdirs();
				if (!done) {
					return false;
				} else {
					absolutePath = database.getAbsolutePath();
				}
			}
			if (database.exists()) {
				absolutePath = database.getAbsolutePath();
			}
			System.out.println("Created");
			return true;
		}
	}

	public boolean dropDataBase(String databaseName) {
		System.out.println(folder);
		String Path;

		if (folder == null) {
			return false;

		}
		if (isValidPath(folder)) {
			Path = folder;
		} else {
			Path = "Databases" + System.getProperty("file.separator") + databaseName;
		}
		database = new File(Path);
		if (!database.exists()) {
			return false;
		} else {
			delete(database);
			System.out.println("Deleted");

		}
		return true;

	}

	public void delete(File file) {
		if (file.isDirectory()) {

			// directory is empty, then delete it
			if (file.list().length == 0) {

				file.delete();
				System.out.println("Directory is deleted : " + file.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					System.out.println("Directory is deleted : " + file.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			file.delete();
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}

	}

	public static boolean isValidPath(String path) {
		if (path.contains(System.getProperty("file.separator"))) {
			return true;
		} else {
			return false;
		}

	}
	public ArrayList<String> getcolumnLabels(){
		return xmltoarray.columnLabels;
	}
	public ArrayList<String> getdataTypes(){
		return xmltoarray.dataTypes;
	}
}