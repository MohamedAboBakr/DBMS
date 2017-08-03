package Files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;


public class Table {
	 private String tablename;
	 private int colnum ;
	 private List<String> colnames ;
	 private List<String> coltypes ;
	// private String dbname ;
	 private File XmlFile ;
	 private File dtdFile ;
	 private boolean update;
	 private boolean delete ;
	 private boolean select ;
	 private boolean allrows;
	 private XmlFileHandler xmlHandler  ;
	 private String Tbpath ;
	 
     public Table(String tablename , int num , List<String> names , List<String> types , String path){
	    	     this.tablename = tablename ;
	    	     this.colnum = num ;
	    	     this.colnames = names;
	    	     this.coltypes = types;
	    	     Tbpath = path + "\\" + tablename ;
	    	     xmlHandler = new XmlFileHandler();
	    	     xmlHandler.createDtdFile(Tbpath, tablename, names, types) ;
	    	     xmlHandler.createXmlFile(Tbpath, tablename);
	    	     XmlFile = new File(Tbpath + "\\" + tablename+".xml");
	    	     dtdFile = new File(Tbpath + "\\" + tablename+".dtd");
	             update = false ; delete  = false ; select = false ; allrows = false ;
     }
     
     public Table(String tnname , String currdb){

		    	 this.tablename = tnname ;
		    	 update = false ; delete  = false ; select = false ; allrows = false ;
		    	 colnum = 0 ;
		    	 Tbpath = "database\\"+currdb+"\\"+tablename ;
		    	 colnames = new ArrayList<>();
		    	 coltypes = new ArrayList<>();
		    	 XmlFile = new File(Tbpath+"\\" + tnname+".xml");
		    	 dtdFile = new File(Tbpath+"\\"+  tnname+".dtd");
		    	 xmlHandler = new XmlFileHandler();
		    	 LoadTable();
      }
     
     public String getName(){
    	         return tablename;
     }
     
     
     public  List<Map<String, String>> SelectRows(List<String> col, List<String> relation, List<String> data){
		    	 try{  
		    		         if(!validTable()) return null ;
					    	 if(col==null&&relation==null&&data==null) allrows=true ;
					    	   List<Map<String, String>> selected = new ArrayList<>();
					    	   if(!allrows){
					    		   if(!CheckRelations(col , relation , data)){
					    			     return null ;              																			//////// ALERT
					    		   }
					    	   }
					 
					    	   select = true ; update = false ; delete  = false ;
					    	   File inputFile = new File(Tbpath + "\\" + tablename+".xml");
					      	   DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
					           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					           Document doc = dBuilder.parse(inputFile);
					           doc.getDocumentElement().normalize();
					           Element rootNode = doc.getDocumentElement();
					           Selectt(col , relation , data , doc , selected);
					           allrows = false ;
					    	   return selected ;
		    	 }catch(ParserConfigurationException e){
		    		   System.out.println(e.getMessage());
		    		   return null ;
		    	 }catch(SAXException e){
		    		   System.out.println(e.getMessage());
		    		   return null ;
		    	 }catch(IOException e){
		    		  System.out.println(e.getMessage());
		    		   return null ;
		    	 }
     }
     
     
     public void ModifyRow(List<String> col , List<String> relation , List<String> data ,  List<String> cols , List<String> newValues ){
    	 
			      try{	
			    	        
			    	        if(!validTable()) return ;
					    	 if(col==null&&relation==null&&data==null) allrows=true ;
					    	 if(!allrows){
					    		 if(!CheckRelations(col , relation , data)){
					    			 
					    			  return  ;
					    		 }
					    	 }
					    	 int check = checkNewRow(cols , newValues);
					    	 if(check == 0){
					    		 return ;
					    	 }
					    	 update = true ; delete  = false ; select = false ;
					    	 File inputFile = new File(Tbpath + "\\" + tablename+".xml");
					    	 Map<String , List<String>> todelete  = Tomap(col , relation , data);
					    	 DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
					         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					         Document doc = dBuilder.parse(inputFile);
					         doc.getDocumentElement().normalize();
					         Element rootNode = doc.getDocumentElement();
					         SearchXml(doc , rootNode , todelete , cols , newValues , null);
					 
					         Transformer transformer = TransformerFactory.newInstance().newTransformer();
					         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					         transformer.setOutputProperty(OutputKeys.METHOD, "xml");
					         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
					         transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM , tablename+".dtd"); 
					         DOMSource source = new DOMSource(doc);
					         StreamResult result = new StreamResult(inputFile);
					         transformer.transform(source, result);
					  
					         allrows = false;
					         
			      }catch(ParserConfigurationException e){
					   System.out.println(e.getMessage());
				 }catch(SAXException e){
					   System.out.println(e.getMessage());
				 }catch(IOException e){
					   System.out.println(e.getMessage());
				  }catch(TransformerFactoryConfigurationError e){
					   System.out.println(e.getMessage());
				 }catch(TransformerException e){
					   System.out.println(e.getMessage());
				  }
     }
     
     public void Addrow(List<String> row ,  List <String> values){
		    	 try{ 
		    		        if(!validTable()) {
		    		        	  return;
		    		          }   
					    	  if((row.size() != colnum && colnum != 0) ||  row.size()!=values.size()){
					    		  System.out.println("number of Columns must be :- " + colnum);
					    		  return ;
					    	  }
					    	   File inputFile = new File(Tbpath + "\\" + tablename+".xml");
					           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					           Document doc = dBuilder.parse(inputFile);
					           doc.getDocumentElement().normalize();  
					           Element newRow = getRow(row,values, doc);
					           if(newRow == null ) return ;
					           Element root  = doc.getDocumentElement() ;
					           root.appendChild(newRow);
					           Transformer transformer = TransformerFactory.newInstance().newTransformer();
						       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						       transformer.setOutputProperty(OutputKeys.METHOD, "xml");
						       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
						       transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM ,tablename+".dtd"); 
					           DOMSource source = new DOMSource(doc);
					           StreamResult result = new StreamResult(inputFile);
					           transformer.transform(source, result);
					           
					           
					           
		    	 }catch(ParserConfigurationException e){
					   System.out.println(e.getMessage());
				 }catch(SAXException e){
					   System.out.println(e.getMessage());
				 }catch(IOException e){
					   System.out.println(e.getMessage());
				 }catch(TransformerFactoryConfigurationError e){
					   System.out.println(e.getMessage());
				 }catch(TransformerException e){
					   System.out.println(e.getMessage());
				 }
     }
     
     private int checkNewRow(List<String> cols , List<String> newValues){
    	    
		    	 if(cols == null && newValues == null){
		    		  return 0;
		    	 }
		    	 if(cols.size()!=newValues.size()){
		    		  return 0;
		    	 }
		    	 for(int i=0 ; i<cols.size(); i++){
		    	   String colname  = cols.get(i);
		    	   if(!colnames.contains(colname)){
		    		   System.out.println("Error ... " + tablename + " doesn't contain " + colname);
		    		   return 0;
		    	   }
		    	   String coltype = coltypes.get(colnames.indexOf(colname)) ;
		    	   if(coltype.equals("String")) return 1; 
		  		   String type2 = getType(newValues.get(i));
		  		   if(coltype.equals(type2)) return 1 ;
		  		   else return 0 ;
		    	 }
		    	   return 0 ;
     }
     
     private String getType(String data){
			 	    try{
			 	    	 int num = Integer.valueOf(data);
			 	    	 System.out.println("test ok ");
			 	    	 return "Integer" ;
			 	    }catch(NumberFormatException e){
			 	    	 return "String" ;
			 	    }
  }
     

     
   
     public void DeleteRow(List<String> col , List<String> relation , List<String> data){
		    	 try{
		    		 			 if(!validTable()) return ;
						    	 if(col==null&&relation==null&&data==null) allrows=true ;
						    	 if(!allrows){
						    		 if(!CheckRelations(col , relation , data)){
						    			  return  ;
						    		 }
						    	 }
						    	 update = false ; delete  = true ; select = false ;
						    	 Map<String , List<String>> todelete  = Tomap(col , relation , data);
						         DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
						         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						         Document doc = dBuilder.parse(new File(Tbpath + "\\" + tablename+".xml"));
						         doc.getDocumentElement().normalize();
						         Element rootNode = doc.getDocumentElement();
						         SearchXml(doc , rootNode , todelete ,null  , null , null);
						         Transformer transformer = TransformerFactory.newInstance().newTransformer();
						         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						         transformer.setOutputProperty(OutputKeys.METHOD, "xml");
						         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
						         transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM ,tablename+".dtd");  
						         DOMSource source = new DOMSource(doc);
						         StreamResult result = new StreamResult(XmlFile);
						         transformer.transform(source, result);
		
	
						         allrows = false ;
						         
		    	 }catch(ParserConfigurationException e){
					   System.out.println(e.getMessage());
				 }catch(SAXException e){
					   System.out.println(e.getMessage());
				 }catch(IOException e){
					   System.out.println(e.getMessage());
				 }catch(TransformerFactoryConfigurationError e){
					   System.out.println(e.getMessage());
				 }catch(TransformerException e){
					   System.out.println(e.getMessage());
				 }
     }
     
     
     private void  Selectt(List<String> col, List<String> relation, List<String> data , Document doc , List<Map<String, String>> selected){
    	 
    	 NodeList nList = doc.getElementsByTagName("row");
    	 for(int i=0;  i < nList.getLength() ; i++){
    		       Node currnode = nList.item(i);
    		       if(select&&allrows){
    		    	   Map<String, String> rowmp = RowToMap(currnode);
		    		   selected.add(rowmp) ;
		    		   continue ;
    		       }
  
    		       Element currElement = (Element) currnode ;
    		       boolean flag = true ;
    		       for(int j=0 ; j<colnames.size() ; j++){
  		        	     String name = colnames.get(j);
  		        	     String namevalue = currElement.getElementsByTagName(name).item(0).getTextContent().toString();
  		        	     String elementType = coltypes.get(j);
  		        	    if(col.contains(name)){
    		    		   String cmp = relation.get(col.indexOf(name));
    		    		   String s2 =  data.get(col.indexOf(name));
    		    		   boolean flag2 = true;
    		    		   if(elementType.equals("String")){
    		    			   flag2 = compareString(namevalue , s2, cmp);
    		    		   }
    		    		   else{
    		    			   flag2 = compareInteger(namevalue , s2, cmp);
    		    		   }
    		    		   flag &= flag2 ;
    		    	   }	 
  		         }
    		       
    		   
    		       if(flag==true){
    		    	   Map<String, String> rowmp = RowToMap(currnode);
		    		   selected.add(rowmp) ;
    		       }
    		    }

    	 
     }
     private void SearchXml(Document doc , Element rootNode , Map<String , List<String>> todelete , List<String> cols , List<String> newValues , List<Map<String, String>> selected){
		
    	 NodeList nList = doc.getElementsByTagName("row");
    	 List<Node> fordelete = new ArrayList<>();
		    	 for(int i=0;  i < nList.getLength() ; i++){
		    		       Node currnode = nList.item(i);
		    		       if(select&&allrows){
		    		    	   Map<String, String> rowmp = RowToMap(currnode);
				    		   selected.add(rowmp) ;
				    		   continue ;
		    		       }if(update && allrows){
		    		    	   Element newRow = updateRow(currnode , cols , newValues ,  doc);
				    		   rootNode.replaceChild(newRow, currnode);
				    		   continue ;
		    		       }
		    		       if(delete && allrows){
		    		    	   System.out.println("Delete done");
		    		    	  // rootNode.removeChild(currnode) ;
		    		    	   fordelete.add(currnode);
		    		    	   continue;
		    		        }
		  
		    		       Element currElement = (Element) currnode ;
		    		       boolean flag = true ;
		    		       for(int j=0 ; j<colnames.size() ; j++){
		  		        	     String name = colnames.get(j);
		  		        	     String namevalue = currElement.getElementsByTagName(name).item(0).getTextContent().toString();
		  		        	     String elementType = coltypes.get(j);
		  		        	    if(todelete!= null && todelete.containsKey(name)){
		    		    		   String cmp = todelete.get(name).get(0);
		    		    		   String s2 =  todelete.get(name).get(1);
		    		    		   boolean flag2 = true;
		    		    		   if(elementType.equals("String")){
		    		    			   flag2 = compareString(namevalue , s2, cmp);
		    		    		   }
		    		    		   else{
		    		    			   flag2 = compareInteger(namevalue , s2, cmp);
		    		    		   }
		    		    		   flag &= flag2 ;
		    		    	   }	 
		  		         }
		    		       
		    		   
		    		       if(flag==true){
		    		    	   if(delete == true)fordelete.add(currnode);
		    		    	   else if(update == true){
		    		    		   Element newRow = updateRow(currnode , cols , newValues ,  doc);
		    		    		   rootNode.replaceChild(newRow, currnode);
		    		    	   }else{
		    		    		   Map<String, String> rowmp = RowToMap(currnode);
		    		    		   selected.add(rowmp) ;
		    		    	   }
		    		       }
		    	 }
		    	 for(Node node : fordelete){
		    		  rootNode.removeChild(node);
		    	 }
     }
     
     
     private Element updateRow( Node currnode ,  List<String> cols , List<String> newValues , Document doc){
    	         for(String s : cols){
    	        	   System.out.println(s);
    	         }for(String s : newValues){
    	        	   System.out.println(s);
    	         }
		    	 Element newRow = doc.createElement("row"); 
		    	 Element currElement = (Element) currnode ;
  		         for(int i=0 ; i<colnames.size() ; i++){
  		        	     String name = colnames.get(i);
  		        	     String namevalue = currElement.getElementsByTagName(name).item(0).getTextContent();
  		        	     String newvalue = "";
  		        	     if(cols.contains(name)){
  		        	    	   newvalue = newValues.get(cols.indexOf(name));
  		        	     }else {
  		        	    	   newvalue = namevalue ;
  		        	     }
  			    	   Element colname = doc.createElement(name);
			    	   Attr attrType = doc.createAttribute("type");
			  	       attrType.setValue(coltypes.get(i));
			  	       colname.setAttributeNode(attrType);
			  	       colname.appendChild(doc.createTextNode(newvalue));
			  	       newRow.appendChild(colname);
  		         }
  		             
		        return newRow ;
     }
     
     private Element getRow(List<String> row , List<String> values ,  Document doc){
    	           if(row.size() != colnum){
    	        	     System.out.println("Error invalid insert data");
    	        	     return null ;
    	           }
			  	   Element newRow = doc.createElement("row");
			  	   for(int i=0 ; i< row.size() ; i++){
			  		     if(!colnames.contains(row.get(i))){
			  		    	 System.out.println("Error invalid col type");
	    	        	     return null ;
			  		     }
			  		     String type = coltypes.get(colnames.indexOf(row.get(i)));
			  		     Element colname = doc.createElement(row.get(i));
			  		     Attr attrType = doc.createAttribute("type");
			  		     attrType.setValue(type);
			  		     colname.setAttributeNode(attrType);
			  	         colname.appendChild(doc.createTextNode(values.get(i)));
			  	         newRow.appendChild(colname);
			  		     if(!type.equals("String")){
			  		    	   try{
			  		    		     int num = Integer.valueOf(values.get(i)) ;
			  		    		     
			  		    	   }catch(NumberFormatException e){
			  		    		     System.out.println("Error .. invalid data");
			  		    		     return null ;
			  		    	   }
			  		     } 
			  	   }
			  	   return newRow ;
   }
     
     private Map<String, String> RowToMap(Node currnode){
		    	 Map<String, String> mp = new HashMap<>();
		    	 Element currElement = (Element) currnode ;
		    	 for(int j=0 ; j<colnames.size() ; j++){
		        	     String name = colnames.get(j);
		        	     String namevalue = currElement.getElementsByTagName(name).item(0).getTextContent().toString();
		        	     String elementType = coltypes.get(j);
		        	     mp.put(name, namevalue); 
		         }
		    	 
		    	 return mp ;
     }
     
     private Map<String , List<String>> Tomap(List<String> col , List<String> relation , List<String> data){
		    	 if(col==null&&relation==null&&data==null) return null ;
		    	 Map<String , List<String>> todelete  = new HashMap<String , List<String>>();
		    	 int len = col.size();
		    	 for(int i=0;i<len;i++){
		    		 List<String> lst = new ArrayList<>();
		    		 lst.add(relation.get(i));
		    		 lst.add((String)data.get(i));
		    		 todelete.put(col.get(i), lst);
		    	 }
		    	 return todelete ;
     }
     
     
     private boolean compareString(String s1 , String s2 , String cmp){
	    	     int cmpp = s1.compareTo(s2) ;
	    	     if(cmp.equals("=")){
	    	    	   if(cmpp == 0) return true ;
	    	    	   return  false ;	 
	    	     }
	    	     if(cmp.equals("!=")){
	    	    	 if(cmpp == 0) return false ;
	  	    	     return  true ;	
	    	     }
	    	     if(cmp.equals(">")){
	    	    	  if(cmpp > 0) return true ;
	  	    	      return  false ;
	    	     }
	    	     if(cmp.equals("<")){
	    	    	  if(cmpp < 0) return true ;
	 	    	      return  false ;
	    	     }
	    	     if(cmp.equals("<=")){
	    	    	 if(cmpp <= 0) return true ;
	    	    	 return false ;
	    	     }
	    	     if(cmpp >= 0) return true ;
		    	 return false ;
     }
     
     private boolean compareInteger(String s1 , String s2 , String cmp){
		    	 int n1 = Integer.valueOf(s1);
		    	 int n2 = Integer.valueOf(s2);
			     if(cmp.equals("=")) return n1==n2 ;
			     if(cmp.equals("!=")) return n1!=n2;
			     if(cmp.equals(">")) return n1>n2 ;
			     if(cmp.equals("<")) return n1<n2 ;
			     if(cmp.equals(">=")) return n1>=n2 ;
			     return n1<=n2 ;
    }
     
     
     private boolean CheckRelations(List<String> col , List<String> relation , List<String> data){
    	         if(col==null && relation==null && data==null) return true ;
		    	 int len = col.size();
		    	 for(int i=0; i<len ; i++){
		    		 if(!colnames.contains(col.get(i))){
		    			    System.out.println("Error ... " + tablename + " doesn't contain " + col.get(i) + " column");
					    	return false ;
		    		 }
		    		 int index = colnames.indexOf(col.get(i));
		    		 String type = coltypes.get(index);
		    		 
		    		 if(type.equals("Integer")){
		    			    try{
		    			    	int checkInt = Integer.valueOf(data.get(i));
		    			    }catch(NumberFormatException e){
		    			    	System.out.println("Error ... Invalid data type ");
		    			    	return false ;
		    			    }
		    		 }
		    	 }
		    	 return true ;
     }
        
    
																							     
     private void LoadTable(){
		    	try{
					        boolean checkValid = xmlHandler.validateXml(Tbpath , tablename);
					    	 if(!checkValid){
					    		  System.out.println("Error... "+ tablename +" doesn't validate with its Dtd File ");
					    		  return ;
					    	 }  
					         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					         Document doc = dBuilder.parse(new File(Tbpath + "\\" + tablename+".xml"));
					         doc.getDocumentElement().normalize();
					         NodeList nList = doc.getElementsByTagName("row");
					         for (int temp = 0; temp < nList.getLength() && temp < 1; temp++) {
					        	 Node nNode = nList.item(temp);
					        	 NodeList subnodes = nNode.getChildNodes() ;
				    		     int childsnum = subnodes.getLength();
				    		     for(int i=1 ; i<childsnum ; i+=2){
				    		    	  Node currnode =  subnodes.item(i);
				    		    	  Element eElement = (Element) currnode;
				    		    	  String name = eElement.getNodeName();
						        	  String type = eElement.getAttribute("type");
						        	  colnames.add(name);
						        	  coltypes.add(type);
						        	  
						        	  colnum ++ ;
				    		     }
					         }
				        	
					         
		    	}catch(ParserConfigurationException e){
		 		   System.out.println(e.getMessage());
		 	     }catch(SAXException e){
		 		   System.out.println(e.getMessage());
		 	    }catch(IOException e){
		 		  System.out.println(e.getMessage());
		 	    }
     }
     
     
     public void DeleteTable(String path){
    	 boolean checkValid = xmlHandler.validateXml(Tbpath , tablename);
    	 if(!checkValid){
    		  System.out.println("Error... "+ tablename +" doesn't validate with its Dtd File ");
    		  return ;
    	 }  
    	  File file1 = new File(path + "\\" + tablename + ".xml");
    	  File file2 = new File(path + "\\" + tablename + ".dtd");
    	  File file3 = new File(path);
    	  file1.delete();
    	  file2.delete();
    	  file3.delete();
}



     
    
      public  boolean validTable(){
    	         
    	         return xmlHandler.validateXml(Tbpath  , tablename);
     } 
      
      public List<Map<String , String>>  describeTable(){
    	  List<Map<String , String>>  lstmp = new ArrayList<>();
    	  Map<String , String> mp = new HashMap<>();
    	  for(int i=0; i<colnames.size() ; i++){
    		   mp.put(colnames.get(i), coltypes.get(i));
    	  }
    	  lstmp.add(mp);
    	  return lstmp ;
      }
    
}
