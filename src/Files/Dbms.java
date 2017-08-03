package Files;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

public interface Dbms {
     public void CreateDB(String dbname);
     public void CreateTable(String tbname , String dbname , int colnum , List<String> colnames , List<String> coltypes);
     public void AddRow(String tbname , String dbname ,  List<String> Row ,List <String> values);  // update 
     public void ModifyRow(String tbname, String dbname, List<String> col , List<String> relation , List<String> data , List<String> cols , List<String> newValues);
     public void DeleteRow(String tbname , String dbname ,List<String> col , List<String> relation , List<String> data) ;
     public List<Map<String , String>> Select(String tbname , String dbname ,List<String> col , List<String> relation , List<String> data) ;
     public void DropDB(String dbname);
     public void DropTable(String dbname , String tbname);
     public boolean validateTable(String dbname , String tbname);
     public List<Map<String , String>> showTables(String dbname);
     public List<Map<String , String>> describeTable(String dbname , String tbname);
 }
