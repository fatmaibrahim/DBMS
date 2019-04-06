package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.io.IOException;
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
public class Insert {

	public void InsertinTable() {
		Parser p = Parser.getInstance();
		MyDataBase dba = new MyDataBase();
		int columnNumber = 0;
		String tableName = p.getTableName();
		LinkedList<String> selectedColumns = p.getselectColumns();
		LinkedList<String> Values = p.getValues();
		String path;
		if(dba.isValidPath(p.getDataBaseName())){
			path  =	System.getProperty("user.dir")+System.getProperty("file.separator")+p.getDataBaseName() + System.getProperty("file.separator") +tableName+".xml";
		}else{
		path  =	System.getProperty("user.dir")+System.getProperty("file.separator")+"Databases"+ System.getProperty("file.separator") +p.getDataBaseName() + System.getProperty("file.separator") +tableName+".xml";
		}

		File table = new File(path);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document d = db.parse(table);
			// function////////
			// get every row node
			NodeList rows = d.getElementsByTagName("row");
			// last row is the one with the columns names
			Node lastRow = rows.item(rows.getLength() - 1);
			if (lastRow.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) lastRow;
				NodeList colNames = e.getChildNodes();
				columnNumber = colNames.getLength();
				if (selectedColumns.isEmpty()) {
					Element appendedrow = d.createElement("row");
					for (int i = 0; i < columnNumber; i++) {
						Node n = colNames.item(i);
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							Element e1 = (Element) n;
							String f = e1.getTagName();
							String att = e1.getAttribute("datatype");
							Element newcol = d.createElement(f);
							newcol.setTextContent(Values.get(i));
							newcol.setAttribute("datatype", att);
							appendedrow.appendChild(newcol);
						}
					}
					// insert the appended row in the root
					lastRow.getParentNode().insertBefore(appendedrow, rows.item(rows.getLength() - 1));
				} else {
					Element appendedrow = d.createElement("row");
					for (int i = 0; i < columnNumber; i++) {
						Node n = colNames.item(i);
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							Element e1 = (Element) n;
							String f = e1.getTagName();
							String att = e1.getAttribute("datatype");
							boolean found = false;
							for (int j = 0; j < selectedColumns.size(); j++) {
								if (selectedColumns.get(j).equals(f)) {
									Element newcol = d.createElement(f);
									newcol.setTextContent(Values.get(j));
									newcol.setAttribute("datatype", att);
									appendedrow.appendChild(newcol);
									found = true;
								}
							}
							if (!found) {
								Element newcol = d.createElement(f);
								newcol.setTextContent(null);
								newcol.setAttribute("datatype", att);
								appendedrow.appendChild(newcol);
							}
						}
					}
					lastRow.getParentNode().insertBefore(appendedrow, rows.item(rows.getLength() - 1));
				}
			}
			///////////////////////////
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource ds = new DOMSource(d);
			StreamResult sr = new StreamResult(table);
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
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}