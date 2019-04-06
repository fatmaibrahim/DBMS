package eg.edu.alexu.csd.oop.db.cs43;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JTextField;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Guidb {

	private JFrame frame;
	private JTextField textField;
	MyDataBase dbms = new eg.edu.alexu.csd.oop.db.cs43.MyDataBase();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Guidb window = new Guidb();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Guidb() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(163, 83, 334, 48);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNew = new JLabel("Insert SQL Command");
		lblNew.setForeground(new Color(255, 255, 255));
		lblNew.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		lblNew.setBounds(164, 48, 232, 24);
		frame.getContentPane().add(lblNew);
		
		JButton btnNewButton = new JButton("INSERT");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query=textField.getText().toString();
				
				String getmassage;
				try {
					getmassage = getresult( query);
					JOptionPane.showMessageDialog(null, getmassage);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "ERROR!");
					
				}
				
				
				
			}
		});
		btnNewButton.setBounds(166, 159, 101, 39);
		frame.getContentPane().add(btnNewButton);
		frame.setBounds(100, 100, 682, 408);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public String getresult(String query) throws SQLException{
		boolean success;
		int numRows;
		String get = null;
		if(query.toLowerCase().contains("create database")){
			String path =query.substring(15,query.length());
			String choice =JOptionPane.showInputDialog(null,"Do you want to recreate if it exists ? (yes/no) ");
			if(choice.toLowerCase().equals("yes")){
				dbms.createDatabase(path, true);
				get="created";
			}else if(choice.toLowerCase().equals("no")){
				dbms.createDatabase(path, false);
				get="created";
			}else{
				 get="invalid input";
			}
		}
		else{
			if(query.toLowerCase().contains("create table")){
				success = dbms.executeStructureQuery(query);
				get="Creating table: "+ success;
			}else if(query.toLowerCase().contains("drop table")){
				success = dbms.executeStructureQuery(query);
				get="Droping table: "+ success;
			}
			else if(query.toLowerCase().contains("drop database")){
				success = dbms.executeStructureQuery(query);
				get="Droping database: "+ success;
			}else if(query.toLowerCase().contains("select")){
				get="selected Rows:"+printRows(dbms.executeQuery(query));
				
			}
			else if(query.toLowerCase().contains("insert")){
				dbms.executeUpdateQuery(query);
				get="Row is inserted";
			}else if(query.toLowerCase().contains("delete")){
				dbms.executeUpdateQuery(query);
			}else if(query.toLowerCase().contains("update")){
				numRows = dbms.executeUpdateQuery(query);
				get="Number of updated rows = "+ numRows;
			}else if(query.equals("0")){
				return get;
			}
			else{
				get="INVALID QUERY";
			}
		}
		return get;
	}


public static String printRows(Object[][] array){
	String row="";
	for(int i=0;i<array.length;i++){
		for(int j=0;j<array[i].length;i++){
			row+="Row "+(i+1);
			row+=array[i][j];
		}
	}
	return row;
}
		
		
		
	
}
