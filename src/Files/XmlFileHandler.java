package Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/*import org.jdom2.DocType;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter; */
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlFileHandler {
       
	  public XmlFileHandler(){
		  
	  }
	  
	  public boolean createDtdFile(String tbpath , String tbname , List<String> colnames , List<String> coltype){
		  String fileName = tbpath+"\\"+tbname+".dtd" ;
		  try {
              FileWriter fileWriter = new FileWriter(fileName);
              BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
              if(colnames.isEmpty()){
            	  bufferedWriter.write("<!ELEMENT table EMPTY>");
              }
              else{
                  	 bufferedWriter.write("<!ELEMENT table (row*)>");
	                 bufferedWriter.newLine();
	                 bufferedWriter.write("<!ELEMENT row (");
	                 bufferedWriter.write(colnames.get(0));
	                 for(int i=1 ; i< colnames.size() ; i++){
	                	 bufferedWriter.write(","+colnames.get(i));
	                 }
	                 bufferedWriter.write(")>");
	                 bufferedWriter.newLine();
	                 for(int i=0;i<colnames.size();i++){
	                	 bufferedWriter.write("<!ELEMENT "+colnames.get(i)+" (#PCDATA)>");
		                 bufferedWriter.newLine();
	                 }
	                 for(int i=0;i<colnames.size();i++){
	                	 bufferedWriter.write("<!ATTLIST "+colnames.get(i)+" type NMTOKEN #FIXED \""+coltype.get(i)+"\">");
	                	 bufferedWriter.newLine();
	                 }
              }
              bufferedWriter.close();
              return true ;
          }
          catch(IOException ex) {
              System.out.println(
                  "Error writing to file '"
                  + fileName + "'");
              return false ;
          }
    	  
	  }
	  
	  
      public boolean createXmlFile(String tbpath , String tbname ){
		
    	     try{
    	    	 
    	    	 DocumentBuilderFactory dbFactory =
    	    	         DocumentBuilderFactory.newInstance();
    	    	         DocumentBuilder dBuilder = 
    	    	            dbFactory.newDocumentBuilder();
    	    	         Document doc = dBuilder.newDocument();          
    	    	         Element rootNode = doc.createElement("table");
    	    	         doc.appendChild(rootNode);
    	    	 
		         Transformer transformer = TransformerFactory.newInstance().newTransformer();
		         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		         transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		         transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM , tbname+".dtd"); 
		         DOMSource source = new DOMSource(doc);
		         StreamResult result = new StreamResult(new File(tbpath+"\\" + tbname+".xml"));
		         transformer.transform(source, result);
		         StreamResult consoleResult =  new StreamResult(System.out);
		         transformer.transform(source, consoleResult);  
		  	          return true ;
		            } catch (Exception e) {
		   	           e.printStackTrace();
		   	           System.out.println("+++++++++++++++++++++++++++++*************************");
		   	           return false ;
		  	      }
	  }
      
      private boolean flagg = true ;
      
      public boolean validateXml(String tbpath , String tbname){
    	    String xmlFile  = tbpath + "\\"+tbname+".xml";
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
            	       e3.printStackTrace();
            	       return false ;
    	    }
      }
}
