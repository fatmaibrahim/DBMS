package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author omid
 *
 */
public class Delete {
	@SuppressWarnings("static-access")
	public int deleteFromtable() throws SQLException {
		Parser p = Parser.getInstance();
		MyDataBase dba=new MyDataBase();
		int counter = 0;
		String tableName = p.getTableName();
		LinkedList<String> condition = p.getwhere();
		String path ;
		if(dba.isValidPath(p.getDataBaseName())){
			path  =	System.getProperty("user.dir")+System.getProperty("file.separator")+p.getDataBaseName() + System.getProperty("file.separator") +tableName+".xml";
		}else{
		path  =	System.getProperty("user.dir")+System.getProperty("file.separator")+"Databases"+ System.getProperty("file.separator") +p.getDataBaseName() + System.getProperty("file.separator") +tableName+".xml";
		}
		File table = new File(path);
		if(!table.exists()){
			throw new SQLException();
			
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document d;
		try {
			d = dbf.newDocumentBuilder().parse(path);
			//// function
			Element root = d.getDocumentElement();
			NodeList rows = d.getElementsByTagName("row");
			if (condition.isEmpty()) {
				for (int i = 0; i < rows.getLength() - 1; i++) {
					// the row
					Node row = rows.item(i);
					// all columns inside the row
					NodeList col = row.getChildNodes();
					// loop on cols
					for (int j = 0; j < col.getLength(); j++) {
						// column node
						Node n = col.item(j);
						row.removeChild(n);
					}

					root.removeChild(row);
					i--;
					counter++;
				}
			} else {
				String column = condition.get(0);
				String op = condition.get(1);
				String val = condition.get(2);

				for (int i = 0; i < rows.getLength() - 1; i++) {
					boolean found = false;
					// the row
					Node row = rows.item(i);
					// all columns inside the row
					NodeList col = row.getChildNodes();
					// loop on cols
					for (int j = 0; j < col.getLength(); j++) {
						// column node
						Node n = col.item(j);
						if (op.equals("=")) {
							if (n.getNodeName().equals(column) && n.getTextContent().equals(val)) {
								// remove or set the content to null
								row.removeChild(n);
								found = true;
							}
						} else if (op.equals(">")) {
							if (n.getNodeName().equals(column)
									&& Integer.parseInt(n.getTextContent()) > Integer.parseInt(val)) {
								// remove or set the content to null
								row.removeChild(n);
								found = true;
							}
						} else {
							if (n.getNodeName().equals(column)
									&& Integer.parseInt(n.getTextContent()) < Integer.parseInt(val)) {
								// remove or set the content to null
								row.removeChild(n);
								found = true;
							}
						}
					}
					if (found) {
						for (int k = 0; k < col.getLength(); k++) {
							Node n = col.item(k);
							row.removeChild(n);
						}
						root.removeChild(row);
						 i--;
						counter++;
					}
				}
			}
			///// ends
			TransformerFactory tf = TransformerFactory.newInstance();

			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			DOMSource ds = new DOMSource(d);
			StreamResult sr = new StreamResult(table);
			t.transform(ds, sr);
		} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}
}