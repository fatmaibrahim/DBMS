package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author omid
 *
 */
public class Tables {
	MyDataBase dba = new MyDataBase();

	@SuppressWarnings("static-access")
	public boolean createTable(String path) throws SQLException {
		LinkedList<String> columns = new LinkedList<String>();
		LinkedList<String> datatypes = new LinkedList<String>();
		String tableName;
		Parser p = Parser.getInstance();
		String dirPath;
		tableName = p.getTableName();
		columns = p.getColumns();
		datatypes = p.getDatatypes();
		if (p.getDataBaseName() == null) {
			return false;
		}
		
			if ((columns.isEmpty()) && (datatypes.isEmpty())) {
				throw new SQLException();
			}
		
		if (dba.isValidPath(p.getDataBaseName())) {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + p.getDataBaseName()
					+ System.getProperty("file.separator") + tableName + ".xml";
		} else {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + "Databases"
					+ System.getProperty("file.separator") + p.getDataBaseName() + System.getProperty("file.separator")
					+ tableName + ".xml";
		}
		File table = new File(path);
		// create table without DB and create an already existing table
		if (table.exists()) {
			return false;
		}
		// path =
		// System.getProperty("user.dir")+System.getProperty("file.separator")+"Databases"+System.getProperty("file.separator")+p.getDataBaseName()+tableName+".xml";
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document d = db.newDocument();
			Element root = d.createElement(tableName);
			d.appendChild(root);
			Element row = d.createElement("row");
			for (int i = 0; i < columns.size(); i++) {
				Element e = d.createElement(columns.get(i));
				Attr attribute = d.createAttribute("datatype");
				if (datatypes.size() != columns.size()) {
					return false;
				}

				attribute.setValue(datatypes.get(i));
				e.setAttributeNode(attribute);
				row.appendChild(e);

			}
			root.appendChild(row);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource ds = new DOMSource(d);
			StreamResult sr = new StreamResult(new File(path));
			t.transform(ds, sr);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@SuppressWarnings("static-access")
	public boolean droptable() {
		Parser p = Parser.getInstance();
		String tableName = p.getTableName();
		String path;
		if (dba.isValidPath(p.getDataBaseName())) {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + p.getDataBaseName()
					+ System.getProperty("file.separator") + tableName + ".xml";
			System.out.println(path);
			System.out.println(p.getDataBaseName());

		} else {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + "Databases"
					+ System.getProperty("file.separator") + p.getDataBaseName() + System.getProperty("file.separator")
					+ tableName + ".xml";
			System.out.println(path);
			System.out.println(p.getDataBaseName());

		}
		// path =
		// System.getProperty("user.dir")+System.getProperty("file.separator")+"Databases"+System.getProperty("file.separator")+p.getDataBaseName()+tableName+".xml";
		Path tobeDeleted = Paths.get(path);
		if (!Files.exists(tobeDeleted)) {
			return true;
		} else {
			try {
				Files.delete(tobeDeleted);
			} catch (IOException e) {
				return true;
			}
			return true;
		}
	}

}