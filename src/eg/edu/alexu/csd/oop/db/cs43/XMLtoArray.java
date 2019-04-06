package eg.edu.alexu.csd.oop.db.cs43;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLtoArray {
	public ArrayList<String> columnLabels = new ArrayList<String>();
	public ArrayList<String> dataTypes = new ArrayList<String>();
	int colsNum = 0;
	public Object[][] selectAll(String xmlFileName, ArrayList<Object> columns,
			ArrayList<Object> condition) {
		setAllToLowerCase(columns);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		ArrayList<Object[]> table = new ArrayList<Object[]>();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFileName);
			NodeList classList = doc.getChildNodes();
			NodeList columnList = classList.item(0).getChildNodes();
			for (int i = 0; i < columnList.getLength(); i++) {
				Node p = columnList.item(i);
				if (getAttributes(p, columns, condition) != null) {
					table.add(getAttributes(p, columns, condition));
				}

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object[][] arrayTable = new Object[table.size()][];
		for (int i = 0; i < table.size(); i++) {
			Object[] row = table.get(i);
			arrayTable[i] = row;
		}
		return arrayTable;
	}

	public Object[] getAttributes(Node p, ArrayList<Object> columns,
			ArrayList<Object> condition) {
		if (p.getNodeType() == Node.ELEMENT_NODE) {
			ArrayList<Object> attributes = new ArrayList<Object>();
			//dataTypes = new ArrayList<String>();
			//columnLabels = new ArrayList<String>();
			Element student = (Element) p;
			NodeList nameList = student.getChildNodes();
			colsNum= 0;
			for (int j = 0; j < nameList.getLength(); j++) {
				Node n = nameList.item(j);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					colsNum++;
					boolean conditionMet = false;
					Element name = (Element) n;
					Object currentAttrib = name.getTagName();
					if(columnLabels.size() < colsNum)
					columnLabels.add((String) currentAttrib) ;
					if (condition.size() == 3
							&& condition.get(0).equals(
									currentAttrib.toString().toLowerCase())) {
						Object type = condition.get(0);
						condition.set(0, n.getTextContent());
						conditionMet = isConditionTrue(condition);
						condition.set(0, type);
						if (!conditionMet) {
							return null;
						}
					}
					if (!columns.contains(currentAttrib.toString()
							.toLowerCase()) && !columns.isEmpty()) {
						continue;
					}
					String datatype = n.getAttributes()
							.getNamedItem("datatype").getNodeValue();
					Object o = name.getTextContent();
					if(dataTypes.size() < colsNum)
					dataTypes.add((String) datatype);
					switch (datatype) {
					case "int":
						try {
							Integer icurrentElement = Integer
									.parseInt((String) o);
							attributes.add(icurrentElement);
						} catch (Exception e) {
							// TODO: handle exception
							//throw new RuntimeException();
						}
						break;
					case "varchar":
						String scurrentElement = name.getTextContent();
						attributes.add(scurrentElement);
						break;
					default:
						attributes.add(o);
						break;

					}
				}
			}
			return attributes.toArray();
		}
		return null;
	}

	public Object[] getAttributesNames(NodeList columnList) {
		ArrayList<Object> attributesNames = new ArrayList<Object>();
		boolean flag = true;
		for (int i = 0; i < columnList.getLength() && flag; i++) {
			Node p = columnList.item(i);
			if (p.getNodeType() == Node.ELEMENT_NODE) {
				Element student = (Element) p;
				NodeList nameList = student.getChildNodes();
				for (int j = 0; j < nameList.getLength(); j++) {
					Node n = nameList.item(j);
					if (n.getNodeType() == Node.ELEMENT_NODE) {
						flag = false;
						Element name = (Element) n;
						attributesNames.add(name.getTagName());
					}
				}
				return attributesNames.toArray();
			}
		}
		return null;
	}

	public boolean isConditionTrue(ArrayList<Object> conditions) {
		if (conditions.isEmpty()) {
			return true;
		}
		Object operation = conditions.get(1);
		if (operation.equals("=")) {
			if ((conditions.get(0).toString()).equals(conditions
					.get(2).toString())) {
				return true;
			}
		} else if (operation.equals(">")) {
			String left = (String) conditions.get(0);
			String right = (String) conditions.get(2);

			try {
				if (Integer.parseInt(left) > Integer.parseInt(right)) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		} else if (operation.equals("<")) {
			String left = (String) conditions.get(0);
			String right = (String) conditions.get(2);

			try {
				if (Integer.parseInt(left) < Integer.parseInt(right)) {
					return true;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}

	public void setAllToLowerCase(ArrayList<Object> elements) {
		for (int i = 0; i < elements.size(); i++) {
			elements.set(i, elements.get(i).toString().toLowerCase());
		}
	}
	public ArrayList<String> getColumnsLabels(){
		return columnLabels;
	}
	public ArrayList<String> getDataTypes(){
		return dataTypes;
	}

}