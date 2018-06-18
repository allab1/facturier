package pck;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class xmltodb {



public static void main(String[] args) {
try {
		String user ="root";
		String pass = "root";
		String host= "jdbc:mysql://localhost/xmltodb";
		Connection conn = DriverManager.getConnection(host,user,pass);
		
		createTable(conn);
		File[] files = selectFiles();
//getVal(file, path+"/author/test/@type");   get attribute
//getVal(file, path+"/author/test");     get value

		for(int j=0; j<files.length; j++)
		{
			File file =  files[j];	
			String path = "/catalog/book[@id='bk102']";
			NodeList nlist = getNode(file,path);
		 
			java.sql.PreparedStatement stmt = conn.prepareStatement(
					"INSERT IGNORE  INTO books(" +
					" book_id, author, title, genre, price," +
					" publish_date, description)" +
					"VALUES(?, ?, ?, ?, ?," +
					" str_to_date(?, '%Y-%m-%d'), ?)");
			
		for (int i = 0 ; i < nlist.getLength() ; i++) {
			Node node = nlist.item(i);
			List<String> columns = Arrays.asList(
					getAttrValue(node, "id"),
					getTextContent(node, "author"),
					getTextContent(node, "title"),
					getTextContent(node, "genre"),
					getTextContent(node, "price"),
					getTextContent(node, "publish_date"),
					getTextContent(node, "description"));
			for (int n = 0 ; n < columns.size() ; n++) {
				stmt.setString(n+1, columns.get(n));
			}
			stmt.execute();
		}

		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

private static void createTable(Connection conn) throws SQLException {
	conn.createStatement().execute("CREATE TABLE IF NOT EXISTS books(\n" +
    " id integer primary key auto_increment,\n" +
    " book_id varchar(25) not null unique,\n" +
    " author varchar(50) not null,\n" +
    " title varchar(250) not null,\n" +
    " genre varchar(25) not null,\n" +
    " price float not null,\n" +
    " publish_date date not null,\n" +
    " description text not null\n" +
    ")");
}

private static NodeList getNode(File file, String path)
		throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document xmlDoc = builder.parse(file);
		XPath xpath = XPathFactory.newInstance().newXPath();
		Object res = xpath.evaluate(path, xmlDoc, XPathConstants.NODESET);
		return  (NodeList) res;
	}
	 


private static String getValPath(File file, String path) throws Exception {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document xmlDoc = builder.parse(file);
	XPathExpression xp = XPathFactory.newInstance().newXPath().compile(path);
	String href = xp.evaluate(xmlDoc);
	return href;
}
 
	private static File[] selectFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML","xml");
		chooser.setFileFilter(filter);
		chooser.showOpenDialog(chooser);
		return chooser.getSelectedFiles();
	}
	
	
	static private String getAttrValue(Node node,String attrName) {
		if ( ! node.hasAttributes() ) return "";
		NamedNodeMap nmap = node.getAttributes();
		if ( nmap == null ) return "";
		Node n = nmap.getNamedItem(attrName);
		if ( n == null ) return "";
		return n.getNodeValue();
	}


	static private String getTextContent(Node parentNode,String childName) {
		NodeList nlist = parentNode.getChildNodes();
		for (int i = 0 ; i < nlist.getLength() ; i++) {
			Node n = nlist.item(i);
			String name = n.getNodeName();
			if ( name != null && name.equals(childName) )
				return n.getTextContent();
		}
		return "";
	}

	static {
		try { Class.forName("com.mysql.jdbc.Driver"); }
		catch(ClassNotFoundException ex) {
			System.err.println("Driver not found: " + ex.getMessage());
		}
	};
	
}
