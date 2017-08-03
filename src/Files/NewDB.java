package Files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

public class NewDB {
	
	
	  private String dbname ;
	  private List<Table> tables ;
	  private Map<String , Integer> tablenames ;
	  private boolean firstCall ;
	  private String DBpath ;
	  
	  
      public NewDB(String name , boolean load){
    	       DBpath = "database\\"+name ;
	    	   this.dbname = name ;
	    	   tables = new ArrayList<>();
	    	   tablenames = new HashMap<>();
	    	   if(load) LoadDB();
      }

      public String getName(){
    	       return dbname ;
      }
      
      public boolean TableExist(String tablename){
	    	   if(tablenames.containsKey(tablename)) return true ;
	    	   return false ;
      }
     
      public void addTable(String tbname , int colnum, List<String> colnames, List<String> coltypes){
	    	 try{
			    	  tablenames.put(tbname, 1);
			    	  String path_ = DBpath+"\\"+tbname ;
			    	  Path path = Paths.get(path_);
			   	      Files.createDirectories(path);
			    	  Table newtable = new Table( tbname , colnum, colnames, coltypes ,DBpath);
			    	  tables.add(newtable);
	    	 }catch(IOException e){
	    		 System.out.println(e.getMessage());
	    	 }
      }
      
      public void deleteRow(String tbname , List<String> col , List<String> relation , List<String> data){
	    	   for(Table tb : tables){
	    		     if(tb.getName().equals(tbname)){
	    		    	  tb.DeleteRow(col, relation, data);
	    		    	  break;
	    		     }
	    	   }
      }
      
      public void Addrow(String tbname, List<String> Row , List <String> values){
    	  for(Table tb : tables){
 		     if(tb.getName().equals(tbname)){
 		    	  tb.Addrow(Row , values);
 		    	  break;
 		     }
 	      }
      }
      
      public void modifyrow(String tbname  , List<String> col , List<String> relation , List<String> data ,  List<String> cols , List<String> newValues){
    	  for(Table tb : tables){
  		     if(tb.getName().equals(tbname)){
  		    	  tb.ModifyRow(col, relation, data, cols, newValues);
  		    	  break;
  		     }
  	      }
      }
      
      public List<Map<String, String>> Select(String tbname, List<String> col, List<String> relation, List<String> data){
    	  for(Table tb : tables){
   		     if(tb.getName().equals(tbname)){
   		    	  return tb.SelectRows(col,relation,data);
   		     }
   	      }    
    	  return null ;
      }
      
      public void LoadDB(){
    	File file = new File(DBpath);
  	    String[] names = file.list();
  	    for(String name : names)
  	    {  
 
  	        if (new File(DBpath+"\\"+name).isDirectory())
  	        {
  	              Table tb = new Table(name,dbname);
  	              tables.add(tb);
  	              tablenames.put(name,1);
  	        }
  	    }
  		
      }
      
      public void DeleteTable(String tbname){
    	       Table deleted =null;
    	       for(Table tb : tables){
    	    	    if(tb.getName().equals(tbname)){
    	    	    	 deleted = tb ;
    	    	    	 break ;
    	    	    }
    	       }
    	       tablenames.remove(tbname);
    	       tables.remove(deleted);
    	       deleted.DeleteTable(DBpath+"\\"+tbname);
      }
      
      public void DeleteDB(String path){
    	  File tbfolder = new File(path);
    	  if(tbfolder.isDirectory()){
    		    for(File innerFile : tbfolder.listFiles() ){
    		    	DeleteDB(path+"\\"+innerFile.getName());
    		    }
    	  }
    	  tbfolder.delete();  
      }
      
     
      public boolean validTable(String tbname){
	    	   Table validated  =null;
	    	   if(tables.isEmpty()) return false ;
		       for(Table tb : tables){
		    	    if(tb.getName().equals(tbname)){
		    	    	validated = tb ;
		    	    	 break ;
		    	    }
		       }
	    	   return validated.validTable() ;
      }  
     
     
      
      public List<Map<String , String>> showTables(){
    	      if(tablenames.size()==0){
    	    	   System.out.println(dbname + " is empty");
    	    	   return null;
    	      }
    	      List<Map<String , String>> lstmp  = new ArrayList<>();
    	      Map<String , String> mp = new HashMap<>();
    	      for(String s : tablenames.keySet()){
    	    	  mp.put("1" , s);
    	      }
    	       lstmp.add(mp) ;
    	       return lstmp ;
   }
      
      public List<Map<String , String>> describeTable(String tbname){
    	      if(!TableExist(tbname)) return null ;
    	      Table tb = null ;
    	      for(Table tt : tables){
    	    	  if(tt.getName().equals(tbname)) {
    	    		  tb = tt ;
    	    		  break ;
    	    	  }
    	      }
    	      
    	      return tb.describeTable();
      }
}