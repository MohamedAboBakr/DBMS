package Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Retraindoctype {

	public static void main(String[] args) throws TransformerFactoryConfigurationError, TransformerException, IOException, ParserConfigurationException, SAXException {
		 boolean check = Validate("a7a.xml");    
		if(check){
       	 System.out.println("SUCCESSSSSSSSS    a7AAAAAAAAAA");
        }
		         String inputFile = "a7a.xml";
                 DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
		         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		         Document doc = dBuilder.parse(inputFile);
		         doc.getDocumentElement().normalize();
		         Element rootNode = doc.getDocumentElement();
		         NodeList nList = doc.getElementsByTagName("row");
		         for(int i=0;  i < nList.getLength() ; i++){
		        	 Node currnode = nList.item(i);
		        	 Node fchild = currnode.getFirstChild();
		        	 while(fchild != null){
		        		   String elementname , elementType , elementcontent ;
	    		    	   elementname = fchild.getNodeName() ;
	    		    	   elementcontent = fchild.getTextContent();
	    		    	   if(elementname.equals("name")&&elementcontent.equals("mohamed")){
	    		    		   rootNode.removeChild(currnode);
	    		    		   break ;
	    		    	   }
		        		   fchild = fchild.getNextSibling();
		        	 }
		         }
		         
		         Transformer transformer = TransformerFactory.newInstance().newTransformer();
		         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		         transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		         DOMSource source = new DOMSource(doc);
		         StreamResult result = new StreamResult(inputFile);
		         transformer.transform(source, result);
		         StreamResult consoleResult =  new StreamResult(System.out);
		         transformer.transform(source, consoleResult); 
		         System.out.println("********************************************************");
		         writeXml( "a7a");
		          check = Validate("a7a.xml");
		         if(check){
		        	 System.out.println("SUCCESSSSSSSSS    a7AAAAAAAAAA");
		         }
	} 
	
	public static void writeXml( String name) throws IOException, TransformerFactoryConfigurationError, TransformerException{
		    String xmlpath = name+".xml";
		    String dtdpath = name+".dtd";
		    StringBuilder sb = new StringBuilder();
		    BufferedReader br = new BufferedReader(new FileReader(xmlpath));
		    String line = "";
		    while ((line = br.readLine()) != null) {
		                 sb.append(line);
		      }
		    br.close();
		    String ss = sb.toString();
	        ss = ss.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>","") ;
	        Source xmlInput = new StreamSource(new StringReader("<!-- Document comment -->"+ss));
	        StreamResult xmlOutput = new StreamResult(new File(xmlpath));
	        Transformer transformer = TransformerFactory.newInstance().newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdpath);
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        transformer.transform(xmlInput, xmlOutput);
	       // System.out.println(xmlOutput.getWriter().toString());
	        System.out.println("********************************************************");
	}
	
	public static boolean flagg = true ;
	public static boolean Validate(String path){
		String xmlFile  = path;
	    try{
	    	       DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    	  	   domFactory.setValidating(true);
	    	  	   DocumentBuilder builder = domFactory.newDocumentBuilder();
	    	  	   builder.setErrorHandler(new ErrorHandler() {
	    	  	       @Override
	    	  	       public void error(SAXParseException exception) throws SAXException {
	    	  	           exception.printStackTrace();
	    	  	           flagg  = false ;
	    	  	       }
	    	  	       @Override
	    	  	       public void fatalError(SAXParseException exception) throws SAXException {
	    	  	    	   exception.printStackTrace();
	    	  	    	   flagg = false ;
	    	  	       }
	
	    	  	       @Override
	    	  	       public void warning(SAXParseException exception) throws SAXException {
	    	  	    	   System.out.println("Invalid Xml file....7");
	    	  	    	   flagg = false ;
	    	  	       }
	    	  	   });
	    	  	   if(!flagg) return false ;
	    	       org.w3c.dom.Document doc = builder.parse(xmlFile);
	    	       return true ;
	    }catch(ParserConfigurationException e1){
	    	       System.out.println("Invalid Xml file....1");
	    	       return false ;
	    }
        catch(SAXException e2){
        	       System.out.println("Invalid Xml file....2");
        	       return false ;
	    }
        catch(IOException e3){
        	       System.out.println("Invalid Xml file....3");
        	       return false ;
	    }
  }
		
	}

