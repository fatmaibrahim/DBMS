package eg.edu.alexu.csd.oop.db.cs43;

import java.util.LinkedList;
import java.util.Stack;

public class Parser {

	public LinkedList<String> splitquery;
	public int index = 0;
	public String query;
	public String databasename;
	public LinkedList<String> column;
	public LinkedList<String> selectcolumn;
	public LinkedList<String> datatype;
	public LinkedList<String> insertvalues;
	public LinkedList<String> condition;
	public LinkedList<String> where;
	public String tablename;
	public String type;


	static Parser p = new Parser();

	private Parser() {

	}
	public static Parser getInstance() {
		return p;
	}


	public void parse(String query) {
		splitquery = new LinkedList<String>();
		index = 0;
		this.query = query;
		query = query.toLowerCase().trim();
		if(!query.toLowerCase().contains("where")){
			where = new LinkedList<String>();
		}
//		if (!isSyntaxCorrext(query)) {
//			throw new RuntimeException();///////////////////////////
//		}
		split(query);
		if (splitquery.isEmpty()) {
			throw new RuntimeException();///////////////////////////

		}
		String operation = splitquery.get(index++).toLowerCase();

		switch (operation) {
		case "create":
			if (splitquery.get(index).toLowerCase().equals("database")) {
				index++;
				Createdatabase();
				type="create database";

			} else if (splitquery.get(index).toLowerCase().equals("table")) {
				index++;
				Createtable();
				type="create table";
			} else {
				throw new RuntimeException();///////////////////////////
			}
			break;
		case "insert":
			Insertintotable();
			type="insert";
			break;
		case "delete":
			Deletefromtable();
			type="delete";
			break;
		case "drop":
			if (splitquery.get(index).toLowerCase().equals("database")) {
				index++;
				Dropdatabase();
				type="drop database";

			} else if (splitquery.get(index).toLowerCase().equals("table")) {
				index++;
				Droptable();
				type="drop table";

			} else {
				throw new RuntimeException();///////////////////////////
			}
			break;
		case "select":
			
			Selectfromtable();
			type="select";
			break;
		case "update":
			Updatetable();
			type="update";
			break;
		default:
			throw new RuntimeException();///////////////////////////
		}
	}

	private void split(String query) {
		if(!query.toLowerCase().contains("where")){
			where = new LinkedList<String>();
		}
		String word = "";
		for (int i = 0; i < query.length(); i++) {
			if (query.charAt(i) == ' ' || query.charAt(i) == ',' ||query.charAt(i) == ';'|| query.charAt(i) == '(' || query.charAt(i) == ')'
					|| query.charAt(i) == '\'' || query.charAt(i) == '"') {

				if (word.length() != 0) {
					splitquery.add(word);
					word = "";
				}

			} else if (query.charAt(i) == '=' || query.charAt(i) == '<' || query.charAt(i) == '>') {
				if (word.length() != 0) {
					splitquery.add(word);
					word = "";
				}
				splitquery.add("" + query.charAt(i));
			} else {
				word += query.charAt(i);
			}
		}
		if (word.length() != 0) {
			splitquery.add(word);
			word = "";
		}

	}

	private void Createdatabase() {
		if (splitquery.size() != 3) {
			throw new RuntimeException();///////////////////////////
		}
		databasename = splitquery.get(index);
	}

	private void Createtable() {
		column = new LinkedList<String>();
		datatype = new LinkedList<String>();
		//if (splitquery.size() < 5) {
			//throw new RuntimeException();///////////////////////////
		//}
		tablename = splitquery.get(index++);
		for (int i = index; i < splitquery.size(); i++) {
			if (i % 2 == 1) {
				column.add(splitquery.get(i));
			} else {
				if (splitquery.get(i).toLowerCase().equals("int") || splitquery.get(i).toLowerCase().equals("varchar")) {
					datatype.add(splitquery.get(i));
				} else {
					throw new RuntimeException();//////////////////////////
				}
			}
		}

	}

	private void Insertintotable() {
		insertvalues = new LinkedList<String>();
		selectcolumn = new LinkedList<String>();

		if (splitquery.size() < 5) {
			throw new RuntimeException();///////////////////////////
		}
		if (splitquery.get(index++).toLowerCase().equals("into")) {
			if(splitquery.get(index).toLowerCase().equals("*")){
				tablename = splitquery.get(index+1);
				index=index+2;
			}else{
				tablename = splitquery.get(index);
				index=index+1;
			}

		} else {
			throw new RuntimeException();///////////////////////////
		}
		boolean flag = false;
		if (splitquery.get(index).toLowerCase().equals("values")) {
			for (int i = index + 1; i < splitquery.size(); i++) {
				insertvalues.add(splitquery.get(i));
			}
		} else {
			for (int i = index; i < splitquery.size(); i++) {
				if (splitquery.get(i).toLowerCase().equals("values")) {
					i++;
					flag = true;
				}
				if (!flag) {
					selectcolumn.add(splitquery.get(i));
				} else {
					insertvalues.add(splitquery.get(i));
				}
			}
		}
		if (!selectcolumn.isEmpty() && (selectcolumn.size() != insertvalues.size())) {
			throw new RuntimeException();///////////////////////////
		}

	}

	private void Deletefromtable() {
		where = new LinkedList<String>();
		if (splitquery.size() < 3) {
			throw new RuntimeException();///////////////////////////
		}
		if ((splitquery.size() == 3)) {
			if ((splitquery.get(index++).toLowerCase().equals("from"))) {
				tablename = splitquery.get(index);
			} else {
				throw new RuntimeException();///////////////////////////
			}

		} else if ((splitquery.size() == 4)) {
			if ((splitquery.get(index++).toLowerCase().equals("*")) && (splitquery.get(index++).toLowerCase().equals("from"))) {
				tablename = splitquery.get(index);
			} else {
				throw new RuntimeException();///////////////////////////
			}
		} else {
			if ((splitquery.size() == 7)) {
				if (splitquery.get(index).toLowerCase().equals("from") && splitquery.get(index + 2).toLowerCase().equals("where")) {
					tablename = splitquery.get(index + 1);
					for (int i = index + 3; i < splitquery.size(); i++) {
						where.add(splitquery.get(i));
					}

				} else {
					throw new RuntimeException();///////////////////////////
				}
			} else {
				throw new RuntimeException();///////////////////////////
			}
		}

	}

	private void Dropdatabase() {
		if (splitquery.size() != 3) {
			throw new RuntimeException();///////////////////////////
		}
		databasename = splitquery.get(index);

	}

	private void Droptable() {
		if (splitquery.size() != 3) {
			throw new RuntimeException();///////////////////////////
		}
		tablename = splitquery.get(index);
	}

	private void Selectfromtable() {
		selectcolumn = new LinkedList<String>();
		where = new LinkedList<String>();
		if (splitquery.size() < 4) {
			throw new RuntimeException();///////////////////////////
		}
		if (splitquery.get(index).toLowerCase().equals("*") && splitquery.get(index + 1).toLowerCase().equals("from")) {
			tablename = splitquery.get(index+2);
			index=index+3;
		} else {
			int i;
			for (i = index; i < splitquery.size(); i++) {
				if (splitquery.get(i).toLowerCase().equals("from")) {
					tablename = splitquery.get(i + 1);
					break;
				}
				selectcolumn.add(splitquery.get(i));

			}
           index=i+2;
			if (tablename.equals(null)) {
				throw new RuntimeException();///////////////////////////
			}
		}
		if(splitquery.contains("where")||splitquery.contains("WHERE")){ ///////////////////////
			if(splitquery.get(index).toLowerCase().equals("where")){
				for ( int j=index+1; j < splitquery.size(); j++) {
					where.add(splitquery.get(j));
				}
			}
	}
	}
	private void Updatetable() {
		condition = new LinkedList<String>();
		where = new LinkedList<String>();
		if (splitquery.size() < 6) {
			throw new RuntimeException();///////////////////////////
		}
		tablename = splitquery.get(index);
		boolean flag = false;
		if (splitquery.get(index + 1).toLowerCase().equals("set")) {
			for (int i = index + 2; i < splitquery.size(); i++) {
				if (splitquery.get(i).toLowerCase().equals("where")) {
					i++;
					flag = true;
				}
				if (!flag) {
					condition.add(splitquery.get(i));
				} else {
					where.add(splitquery.get(i));
				}
			}

		} else {
			throw new RuntimeException();///////////////////////////
		}

		if(condition.size()%3!=0){
			throw new RuntimeException();///////////////////////////
		}

	}



	public boolean isSyntaxCorrext(String query) {
		if (query.isEmpty()) {
			return false;
		}

//		if (!query.endsWith(";")) {
//			return false;
//		}
		char firstLetter = query.charAt(0);
		if (firstLetter < 'A' || firstLetter > 'z') {
			return false;
		}
		Stack<Character> stack = new Stack<Character>();
		for (int i = 0; i < query.length(); i++) {
			char c = query.charAt(i);
			if (c == '[' || c == '(' || c == '{' || c == '<') {
				stack.push(c);
			} else if (c == ']') {
				if (stack.isEmpty() || stack.pop() != '[' || (i > 0 && query.charAt(i - 1) == ',')) {
					return false;
				}
			} else if (c == ')') {
				if (stack.isEmpty() || stack.pop() != '(' || (i > 0 && query.charAt(i - 1) == ',')) {
					return false;
				}
			} else if (c == '}') {
				if (stack.isEmpty() || stack.pop() != '{' || (i > 0 && query.charAt(i - 1) == ',')) {
					return false;
				}
			} else if (c == '>') {
				if (stack.isEmpty() || stack.pop() != '<' || (i > 0 && query.charAt(i - 1) == ',')) {
					return false;
				}
			}
			if (c == '\'') {
				if (stack.isEmpty() || stack.peek() != '$') {
					stack.push('$');
				} else {
					stack.pop();
				}
			} else if (c == '"') {
				if (stack.isEmpty() || stack.peek() != '%') {
					stack.push('%');
				} else {
					stack.pop();
				}
			} else if (c == ',' && (stack.isEmpty() || stack.peek() != '(' || (i > 0 && query.charAt(i - 1) == ','))) {
				return false;
			}
		}
		if (!stack.isEmpty()) {
			return false;
		}
		return true;
	}


public String getDataBaseName() {
		return databasename;
	}

	public String getTableName() {
		return tablename;

	}

	public String gettype() {
		return type;

	}

	public LinkedList<String> getColumns(){
		return column;
	}
	public LinkedList<String> getDatatypes(){
		return datatype;
	}

	public LinkedList<String> getselectColumns(){
		return selectcolumn;
	}
	public LinkedList<String> getValues(){
		return insertvalues;
	}

	public LinkedList<String> getcondition(){
		return condition;
	}
	public LinkedList<String> getwhere(){
		return where;
	}


}
