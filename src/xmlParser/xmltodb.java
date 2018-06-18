package xmlParser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.jws.WebService;
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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class xmltodb {
	
	static String user ="root";
	static String pass = "root";
	static String host= "jdbc:mysql://localhost/xmltodb";
	
	static {
		try { Class.forName("com.mysql.jdbc.Driver"); }
		catch(ClassNotFoundException ex) {
			System.err.println("Driver not found: " + ex.getMessage());
		}
};


public static List<String> saveUser(String input) {
	List<String> columns =null;
try {
		Connection conn = DriverManager.getConnection(host,user,pass);
		Element root = getRoot(input);
		String path = "/output_node/csvImport/informationUser";
		Node node = nodeFXpath(root,path);
		showNode(node);
		java.sql.PreparedStatement stmt = conn.prepareStatement(
				"INSERT IGNORE  INTO user(" +
				" societe, nom, prenom, telephone, fax, client, contract, lot)" +
				"VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
		
		  columns = Arrays.asList(
				//valFNode(node, "@id"),
				valFNode(node, "Societe"),
				valFNode(node, "nom"),
				valFNode(node, "prenom"),
				valFNode(node, "Telephone"),
				valFNode(node, "Fax"),
				valFNode(node, "Client"),
				valFNode(node, "Contrat"),
				valFNode(node, "Lot"));
		for (int n = 0 ; n < columns.size() ; n++) {
			stmt.setString(n+1, columns.get(n));
			}
		stmt.execute();		
 
	} catch (Exception e) { 	e.printStackTrace(); }
return columns;
}


private static Element getRoot(String input) throws Exception{
	
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	InputSource is = new InputSource();
	is.setCharacterStream(new StringReader(input));
	Document xml = builder.parse(is);
	Element root = xml.getDocumentElement();
	return root;
}


public static void createTable(Connection conn) throws SQLException {
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


 
public static File[] selectFiles() {
	JFileChooser chooser = new JFileChooser();
	chooser.setMultiSelectionEnabled(true);
	FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichiers XML","xml");
	chooser.setFileFilter(filter);
	chooser.showOpenDialog(chooser);
	return chooser.getSelectedFiles();
}





@SuppressWarnings("unused")
static public void showNode(Node node){
	System.out.println(node.getNodeName() + " : " + node.getTextContent());
}

@SuppressWarnings("unused")
static public String valFXpath(Element root, String expression)throws Exception {
	XPathFactory xpf = XPathFactory.newInstance();
    XPath path = xpf.newXPath();
    return (String)path.evaluate(expression, root, XPathConstants.STRING);
} 

@SuppressWarnings("unused")
static public String valFNode(Node myNode, String expression)throws Exception {
	Element root = (Element) myNode;
	XPathFactory xpf = XPathFactory.newInstance();
    XPath path = xpf.newXPath();
    return (String)path.evaluate(expression, root, XPathConstants.STRING);
} 	

@SuppressWarnings("unused")
static public Node nodeFXpath(Element root, String expression)throws Exception {
	XPathFactory xpf = XPathFactory.newInstance();
    XPath path = xpf.newXPath();
    return (Node)path.evaluate(expression, root, XPathConstants.NODE);
} 

@SuppressWarnings("unused")
static public Node nodeFNode(Node myNode, String expression)throws Exception {
	Element root = (Element) myNode;
	XPathFactory xpf = XPathFactory.newInstance();
    XPath path = xpf.newXPath();
    return (Node)path.evaluate(expression, root, XPathConstants.NODE);
} 	

@SuppressWarnings("unused")
static public NodeList nodesFXpath(Element root, String expression)throws Exception {
	XPathFactory xpf = XPathFactory.newInstance();
    XPath path = xpf.newXPath();
    return (NodeList)path.evaluate(expression, root, XPathConstants.NODESET);
} 	

static public NodeList nodesFNode(Node myNode, String expression)throws Exception {
	Element root = (Element) myNode;
	XPathFactory xpf = XPathFactory.newInstance();
    XPath path = xpf.newXPath();
    return (NodeList)path.evaluate(expression, root, XPathConstants.NODESET);
} 	




//
//public static void main(String[] args) {
//try {
//		String user ="root";
//		String pass = "root";
//		String host= "jdbc:mysql://localhost/xmltodb";
//		Connection conn = DriverManager.getConnection(host,user,pass);
//		createTable(conn);
//		File[] files = selectFiles();
//		
//		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		 DocumentBuilder builder = factory.newDocumentBuilder();
//	    
//		
//		for(int j=0; j<files.length; j++)
//		{
//			File file =  files[j];	
//			Document xml = builder.parse(file);
//		    Element root = xml.getDocumentElement();
//		    
//			String path = "/catalog/book";
//			NodeList nlist = nodesFXpath(root,path);
//
//			java.sql.PreparedStatement stmt = conn.prepareStatement(
//					"INSERT IGNORE  INTO books(" +
//					" book_id, author, title, genre, price," +
//					" publish_date, description)" +
//					"VALUES(?, ?, ?, ?, ?," +
//					" str_to_date(?, '%Y-%m-%d'), ?)");
//			
//		for (int i = 0 ; i < nlist.getLength() ; i++) {
//			Node node = nlist.item(i);
//			showNode(node);
//			List<String> columns = Arrays.asList(
//					valFNode(node, "@id"),
//					valFNode(node, "author"),
//					valFNode(node, "title"),
//					valFNode(node, "genre"),
//					valFNode(node, "price"),
//					valFNode(node, "publish_date"),
//					valFNode(node, "description"));
//			for (int n = 0 ; n < columns.size() ; n++) {
//				stmt.setString(n+1, columns.get(n));
//			}
//			stmt.execute();
//		}
//
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//}


//get attribute from node
//static private String getAttrValue(Node node,String attrName) {
//		if ( ! node.hasAttributes() ) return "";
//		NamedNodeMap nmap = node.getAttributes();
//		if ( nmap == null ) return "";
//		Node n = nmap.getNamedItem(attrName);
//		if ( n == null ) return "";
//		return n.getNodeValue();
//}
	
	
//get content from node
//static private String getTextContent(Node parentNode,String childName) {
//		NodeList nlist = parentNode.getChildNodes();
//		for (int i = 0 ; i < nlist.getLength() ; i++) {
//			Node n = nlist.item(i);
//			String name = n.getNodeName();
//			if ( name != null && name.equals(childName) )
//				return n.getTextContent();
//		}
//		return "";
//}
//	
 
//private static NodeList getNode(File file, String path) throws  Exception  {
//DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//DocumentBuilder builder = factory.newDocumentBuilder();
//Document xmlDoc = builder.parse(file);
//XPath xpath = XPathFactory.newInstance().newXPath();
//Object res = xpath.evaluate(path, xmlDoc, XPathConstants.NODESET);
//return  (NodeList) res;
//}
//
//node by direct xpath

//value by direct xpath 
//private static String getValPath(Document xmlDoc, String path) throws Exception {
//XPathExpression xp = XPathFactory.newInstance().newXPath().compile(path);
//String href = xp.evaluate(xmlDoc);
//return href;
//}
	
}
