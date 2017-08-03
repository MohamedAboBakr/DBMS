package Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

public class DbController implements Dbms{
   
	private Map<String,Integer> dbnames ;
	private List<NewDB> databases ;
	
	public DbController() {
				dbnames = new HashMap<>();
				databases = new ArrayList<>();
				 LoadAllDB();
	}
	
	
	public void LoadAllDB() {
				File file = new File("database");
			    String[] names = file.list();
			    for(String name : names)
			    {    
			    
			        if (new File("database\\" + name).isDirectory())
			        {
			            NewDB db = new NewDB(name , true);
			            databases.add(db);
			            dbnames.put(name,1);
			        }
			    }
	}
	
	@Override
	public void CreateDB(String dbname) {
				 try { 
							 if(dbnames.containsKey(dbname)){
								    System.out.println("this name already Exist..change it");
								    return ;
							  }
							  Path path = Paths.get("database\\"+dbname);
					          Files.createDirectories(path);		
							  NewDB newdb = new NewDB(dbname , false);
							  databases.add(newdb);
							  dbnames.put(dbname, 1);
		   	      } catch (IOException e) {
						 System.out.println(e.getMessage());
				 }
	}

	@Override
	public void CreateTable(String tbname, String dbname, int colnum, List<String> colnames, List<String> coltypes){
				NewDB db = getDb(dbname);
				if(db == null ) return ;
			    if(db.TableExist(tbname)){
			    	   System.out.println("this table doesn't Exist in " + dbname);
				       return ;
			    } 
		        db.addTable(tbname, colnum, colnames, coltypes);
	}

	
	@Override
	public void AddRow(String tbname, String dbname, List<String> Row , List <String> values)  {
				NewDB db = getDb(dbname);
				if(db == null ) return ;
			    if(!db.TableExist(tbname)){
			    	   System.out.println("this table doesn't Exist in " + dbname);
				       return ;
			    }     
				db.Addrow(tbname, Row , values);
	}

	
	@Override
	public void ModifyRow(String tbname, String dbname, List<String> col , List<String> relation , List<String> data , List<String> cols , List<String> newValues) {
				NewDB db = getDb(dbname);
				if(db == null ) return ;
			    if(!db.TableExist(tbname)){
			    	   System.out.println("this table doesn't Exist in " + dbname);
				       return ;	    
				} 
				db.modifyrow(tbname, col, relation, data, cols , newValues);
	}

	@Override
	public void DeleteRow(String tbname, String dbname,List<String> col , List<String> relation , List<String> data){
			   NewDB db = getDb(dbname);
			   if(db == null ) return ;
		       if(!db.TableExist(tbname)){
		    	   System.out.println("this table doesn't Exist in " + dbname);
			       return ;
		       } 
		       db.deleteRow(tbname, col, relation, data);
	}
	
	@Override
	public List<Map<String, String>> Select(String tbname, String dbname, List<String> col, List<String> relation,List<String> data) {
				NewDB db = getDb(dbname);
				if(db == null) return null;
			     if(!db.TableExist(tbname)){
			    	 System.out.println("this table doesn't Exist in " + dbname);
			    	 return null;
			      } 
				return db.Select(tbname , col,relation,  data);
	}
	
	@Override
	public void DropDB(String dbname) {
				NewDB db = getDb(dbname);
				if(db==null) return ;
				dbnames.remove(dbname);
				databases.remove(db);
				db.DeleteDB("database\\"+dbname);
	}


	@Override
	public void DropTable(String dbname, String tbname) {
				NewDB db = getDb(dbname);
				if(db==null) return ;
			    if(!db.TableExist(tbname)){
			    	   System.out.println("this table doesn't Exist in " + dbname);
			    	   return ;
			     } 
				db.DeleteTable(tbname);
	}
	
	 @Override
	public boolean validateTable(String dbname , String tbname){
				NewDB db = getDb(dbname);
				if(db==null) return false;
			    if(!db.TableExist(tbname)){
			    	   System.out.println("this table doesn't Exist in " + dbname);
			    	   return false;
			     } 
			    return db.validTable(tbname) ;
	}  
	
	private NewDB getDb(String dbname){
			    for(NewDB DB : databases){
			    	  if(DB.getName().equals(dbname)){
			    		   return DB ;
			    	  }
			    }
			    System.out.println("this database doesn't Exist");
			    return null ;
	}


	@Override
	public List<Map<String, String>> showTables(String dbname) {
		NewDB db = getDb(dbname);
		if(db==null) return null ;
		return db.showTables();
	}


	@Override
	public List<Map<String, String>> describeTable(String dbname, String tbname) {
		NewDB db = getDb(dbname);
		if(db==null) return null ;
		return db.describeTable(tbname);
	}


	


}
