package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.io.IOException;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author omid
 *
 */
public class Update {
	Parser p = Parser.getInstance();
	MyDataBase dba = new MyDataBase();
	public LinkedList<String> whereVal = new LinkedList<String>();
	public LinkedList<String> conditionval = new LinkedList<String>();
	public LinkedList<String> column = new LinkedList<String>();

	public String getTable;

	public int UpdateData() throws SQLException {
		int counter = 0;
		column = p.getColumns();
		whereVal = p.getwhere();
		conditionval = p.getcondition();
		getTable = p.getTableName();
	if (column.isEmpty()) {
			counter=0;
			return counter;
		}
		String path;
		if (dba.isValidPath(p.getDataBaseName())) {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + p.getDataBaseName()
					+ System.getProperty("file.separator") + getTable + ".xml";
		} else {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + "Databases"
					+ System.getProperty("file.separator") + p.getDataBaseName() + System.getProperty("file.separator")
					+ getTable + ".xml";
		}

		File table = new File(path);
		if(!table.exists()){
			
			throw new SQLException();
			//return 0;
		}
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document d = db.parse(table);
			Element root = d.getDocumentElement();
			NodeList rootlist = root.getChildNodes();
			Element data = null;
			Node datatext = null;
			if (whereVal.isEmpty()) {
				for (int i = 0; i < rootlist.getLength() - 1; i++) {
					for (int j = 0; j < conditionval.size();) {
						Element row = (Element) rootlist.item(i);

						NodeList rowlist = row.getChildNodes();
						for (int k = 0; k < rowlist.getLength(); k++) {
							if (rowlist.item(k).getNodeName().equals(conditionval.get(j))) {
								data = (Element) rowlist.item(k);

							}

						}
						NodeList datalist = data.getChildNodes();
						datatext = (Node) datalist;
						// old data
						datatext.getTextContent();
						datatext.setTextContent(conditionval.get(j + 2));
						j += 3;
					}
				}
				counter = rootlist.getLength() - 1;
			} else {
				for (int i = 0; i < rootlist.getLength() - 1; i++) {
					Element row = (Element) rootlist.item(i);

					NodeList rowlist = row.getChildNodes();
					for (int j = 0; j < whereVal.size();) {
						for (int k = 0; k < rowlist.getLength(); k++) {
							boolean found = false;
							boolean name = rowlist.item(k).getNodeName().equals(whereVal.get(j));
							// could be bigger than or lesser than
							boolean valu = false;
							if (name) {
								if (whereVal.get(1).equals("=")) {
									valu = rowlist.item(k).getTextContent().equals(whereVal.get(j + 2));
								}
								if (whereVal.get(1).equals(">")) {
									valu = Integer.parseInt(rowlist.item(k).getTextContent()) > Integer
											.parseInt(whereVal.get(j + 2));
								}
								if (whereVal.get(1).equals("<")) {
									valu = Integer.parseInt(rowlist.item(k).getTextContent()) < Integer
											.parseInt(whereVal.get(j + 2));
								}
							}
							if (name && valu) {
								for (int t = 0; t < conditionval.size();) {
									for (int r = 0; r < rowlist.getLength(); r++) {
										if (rowlist.item(r).getNodeName().equals(conditionval.get(t))) {
											data = (Element) rowlist.item(r);

										}

									}
									NodeList datalist = data.getChildNodes();
									datatext = (Node) datalist;
									// old data
									datatext.getTextContent();
									datatext.setTextContent(conditionval.get(t + 2));
									t += 3;
									found = true;
								}

							}
							if (found) {
								counter++;
							}
						}
						j += 3;
					}
				}
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource ds = new DOMSource(d);
			StreamResult sr = new StreamResult(table);
			t.transform(ds, sr);
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}
}