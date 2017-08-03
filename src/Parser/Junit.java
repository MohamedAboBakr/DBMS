package Parser;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Junit {

    	ArrayList<String> y = new ArrayList<String>();
    
        @Test
        public void useDatabase() {
            ValidateQuery x = new ValidateQuery();
	    
	    x.setQuery("USE ;");
            assertEquals(false, x.isValid());
            assertEquals(1, x.getQueryType());
	    
	    x.setQuery("USE");
            assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Use xxx");
            assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Use ii ii;");
            assertEquals(false, x.isValid());
            assertEquals(1, x.getQueryType());
	    
	    x.setQuery("UsE uuu;");
            assertEquals(true, x.isValid());
            assertEquals(1, x.getQueryType());
	    
	    x.setQuery("   UsE    uXu   ;    ");
            assertEquals(true, x.isValid());
            assertEquals(1, x.getQueryType());
	    y.clear();
	    y.add("uXu");
	    assertEquals(y, x.parseIt());

        }
	
	@Test
	public void create(){
	    ValidateQuery x = new ValidateQuery();
	    x.setQuery("Create");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Create ;");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("Create ;");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("Create table");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("Create table ;");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    x.setQuery("Create database");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("Create database ;");
	    assertEquals(false, x.isValid());
            assertEquals(2, x.getQueryType());
	}
	
	@Test
	public void createTable(){
	    ValidateQuery x = new ValidateQuery();
	    
	    
	    x.setQuery("Create table xxx;");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    
	    x.setQuery("Create table xxx (xxx int);");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    x.setQuery("Create table xXx (\"yYy\" string);");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    x.setQuery("Create table xXx yyy (\"yYy\" int);");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    x.setQuery("Create table yyy (\"yYy\" int int);");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    x.setQuery("Create table xXx (\"yYy\" int , \"yYy\" int);");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	    
	    x.setQuery("Create table xXx (\"yYy\" int);");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xXx");
	    y.add("yYy");
	    y.add("int");
            assertEquals(3, x.getQueryType());
            assertEquals(y, x.parseIt());
	    
	    x.setQuery("Create table xXx (\"yYy\" int , \"name\" varchar);");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xXx");
	    y.add("yYy");
	    y.add("int");
	    y.add("name");
	    y.add("varchar");
            assertEquals(3, x.getQueryType());
            assertEquals(y, x.parseIt());
	    
	    x.setQuery("Create table xXx (\"yYy\" int , \"name\" boolean);");
	    assertEquals(false, x.isValid());
            assertEquals(3, x.getQueryType());
	}
	
	@Test
	public void creatDatabase(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Create database xxx");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Create database xxx yyy;");
	    assertEquals(false, x.isValid());
            assertEquals(2, x.getQueryType());   
	    
	    x.setQuery("Create database xxx;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xxx");
            assertEquals(2, x.getQueryType()); 
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void drop(){
	    ValidateQuery x = new ValidateQuery();
	    x.setQuery("drop");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("drop ;");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("drop ;");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("drop table");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("drop table ;");
	    assertEquals(false, x.isValid());
            assertEquals(5, x.getQueryType());
	    
	    x.setQuery("drop database");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType()); 
	    
	    x.setQuery("drop database ;");
	    assertEquals(false, x.isValid());
            assertEquals(4, x.getQueryType());
	}
	
	@Test
	public void dropTable(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("drop table xxx");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("drop table xxx yyy;");
	    assertEquals(false, x.isValid());
            assertEquals(5, x.getQueryType());   
	    
	    x.setQuery("drop table xYx;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xYx");
            assertEquals(5, x.getQueryType()); 
	    assertEquals(y, x.parseIt());  
	}
	
	@Test
	public void dropDatabase(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("drop database xxx");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("drop database xxx yyy;");
	    assertEquals(false, x.isValid());
            assertEquals(4, x.getQueryType());   
	    
	    x.setQuery("drop database xYx;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xYx");
            assertEquals(4, x.getQueryType()); 
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void showTables(){
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("show");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("show;");
	    assertEquals(false, x.isValid());
            assertEquals(10, x.getQueryType());
	    
	    x.setQuery("show xxx");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("show xxx;");
	    assertEquals(false, x.isValid());
            assertEquals(10, x.getQueryType());
	    
	    x.setQuery("show tables");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("show tables x ;");
	    assertEquals(false, x.isValid());
            assertEquals(10, x.getQueryType());
	    
	    x.setQuery("show TabLeS;");
	    assertEquals(true, x.isValid());
	    y.clear();
            assertEquals(10, x.getQueryType());
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void describe(){
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("describe");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("describe ;");
	    assertEquals(false, x.isValid());
            assertEquals(11, x.getQueryType());
	 
	    x.setQuery("describe xx yy;");
	    assertEquals(false, x.isValid());
            assertEquals(11, x.getQueryType());
	    
	    x.setQuery("describe xx ;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xx");
            assertEquals(11, x.getQueryType());
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void insert(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Insert");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Insert ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into  xxx ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into xxx (x) ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into xxx (x) VALUES ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into xxx (x) VALUES (ff) ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());

	    x.setQuery("Insert into xxx (x,x) VALUES (1,2) ;");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into xxx yyy (x) VALUES (\"FF\");");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into xxx (x,y) VALUES (\"FF\",mmm);");
	    assertEquals(false, x.isValid());
            assertEquals(9, x.getQueryType());
	    
	    x.setQuery("Insert into xxx (x,y,,z) VALUES (\"FF\",2345,\" f ,,F,, f \");");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Insert into xxx (x,y,z) VALUES (2345,\" f ,,F,, f \");");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Insert into xxx (x,y,z VALUES (\"FF\",2345,\" f ,,F,, f \");");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Insert into xxx (x,y,z) VALUES \"FF\",2345,\" f ,,F,, f \";");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Insert into xxx (x) VALUES (\"FF\");");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xxx");
	    y.add(1+"");
	    y.add("x");
	    y.add("FF");
            assertEquals(9, x.getQueryType());
	    assertEquals(y, x.parseIt());
	    
	    x.setQuery("Insert into xxx (x,y) VALUES (\"FF\",2345);");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xxx");
	    y.add(2+"");
	    y.add("x");
	    y.add("FF");
	    y.add("y");
	    y.add("2345");
            assertEquals(9, x.getQueryType());
	    assertEquals(y, x.parseIt());
	    
	    x.setQuery("Insert into xxx (x,y,z) VALUES (\"FF\",2345,\" f F f \");");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xxx");
	    y.add(3+"");
	    y.add("x");
	    y.add("FF");
	    y.add("y");
	    y.add("2345");
	    y.add("z");
	    y.add(" f F f ");
            assertEquals(9, x.getQueryType());
	    assertEquals(y, x.parseIt());
	    
	    x.setQuery("Insert into xxx (x,y,z) VALUES (\"FF\",2345,\" f ,,F,, f \");");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("xxx");
	    y.add(3+"");
	    y.add("x");
	    y.add("FF");
	    y.add("y");
	    y.add("2345");
	    y.add("z");
	    y.add(" f ,,F,, f ");
            assertEquals(9, x.getQueryType());
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void update(){

	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("UPDATE");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("UPDATE ;");
	    assertEquals(false, x.isValid());
            assertEquals(7, x.getQueryType());
	    
	    x.setQuery("UPDATE xxx ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx yyy;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx SET ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx SET x;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx SET x < y;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx SET x = ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx Set x = f;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx yyy Set x = 3 ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx Set x = 3, x = 5 ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx Set x = 4 5;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx Set x y = 5;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx Set x , y = 4;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx Set  y < 4;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("UPDATE xxx SeT x = 3;");
	    y.clear();
	    y.add("xxx");
	    y.add("x");
	    y.add("3");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());
	    
	    x.setQuery("UPDATE xxx SEt x = 2 , y = 1;");
	    y.clear();
	    y.add("xxx");
	    y.add("x");
	    y.add("2");
	    y.add("y");
	    y.add("1");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());
	    
	    
	    x.setQuery("UPDATE xxx Set x = 1 , y = \"YuYu\";");
	    y.clear();
	    y.add("xxx");
	    y.add("x");
	    y.add("1");
	    y.add("y");
	    y.add("YuYu");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());
	    
	    x.setQuery("UPDATE xxx Set x = \"F\" , l = \"oo\" ;");
	    y.clear();
	    y.add("xxx");
	    y.add("x");
	    y.add("F");
	    y.add("l");
	    y.add("oo");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());
	    
	    x.setQuery("UPDATE xxx Set x = \" UPDATE xxx SET x = 3 \";");
	    y.clear();
	    y.add("xxx");
	    y.add("x");
	    y.add(" UPDATE xxx SET x = 3 ");

	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void delete(){
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Delete");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Delete ;");
	    assertEquals(false, x.isValid());
            assertEquals(8, x.getQueryType());
	    
	    x.setQuery("Delete FrOm ;");
	    assertEquals(false, x.isValid());
            assertEquals(8, x.getQueryType());
	    
	    x.setQuery("Delete From xxx yyy;");
	    assertEquals(false, x.isValid());
            assertEquals(8, x.getQueryType());
	    
	    x.setQuery("Delete From TyUiK;");
	    y.clear();
	    y.add("TyUiK");
	    assertEquals(true, x.isValid());
            assertEquals(8, x.getQueryType());
	    assertEquals(y, x.parseIt());
	}
	
	@Test
	public void select(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Select");
	    assertEquals(false, x.isValid());
            assertEquals(-1, x.getQueryType());
	    
	    x.setQuery("Select ;");
	    assertEquals(false, x.isValid());
            assertEquals(6, x.getQueryType());
	    
	    x.setQuery("Select * ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx yyy;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select id man FROM xxx;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select id,man ff FROM xxx;");
	    assertEquals(false, x.isValid());
	 
	    x.setQuery("Select id,id FROM xxx;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select id ,,man FROM xxx;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select id name ,man FROM xxx;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select id,man FROM xXx;");
	    y.clear();
	    y.add("id man");
	    y.add("xXx");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());

	    
	    x.setQuery("Select * FROM xXx;");
	    y.clear();
	    y.add("*");
	    y.add("xXx");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.parseIt());

	}
	
	@Test
	public void where(){

	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Select * FROM xxx WHERE;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id =;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id man = 3;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id = ff;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id = 33;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=33");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE id = \"JJkkLL\";");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=JJkkLL");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE id = 33 AND n = 5;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=33");
	    y.add("n=5");
	    y.add("and");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE id = 33 AND n = 5 AND l = 7;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=33");
	    y.add("n=5");
	    y.add("l=7");
	    y.add("and");
	    y.add("and");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE id = 33 AND n = 5 Or l = 7;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=33");
	    y.add("n=5");
	    y.add("l=7");
	    y.add("or");
	    y.add("and");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE (id = 33 AND n = 5) Or l = 7;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=33");
	    y.add("n=5");
	    y.add("and");
	    y.add("l=7");
	    y.add("or");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE (id = \" X \" AND n = \"  I  O  L  \") Or l = 7;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id= X ");
	    y.add("n=  I  O  L  ");
	    y.add("and");
	    y.add("l=7");
	    y.add("or");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE (id=\" X \" AND n=\"  I  O  L  \") Or l=7;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id= X ");
	    y.add("n=  I  O  L  ");
	    y.add("and");
	    y.add("l=7");
	    y.add("or");
	    assertEquals(y, x.whereData());
	    
	    x.setQuery("Select * FROM xxx WHERE (id = 33 AN n = 5) Or l = 7;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id jj = 33 AND n = 5;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id =< 33 AND n = 5;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx WHERE id =< 33 AND n = 5 4;");
	    assertEquals(false, x.isValid());
	}
	
	@Test
	public void limit(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Select * FROM xxx limit;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit x;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit 1;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit x,y;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit 1, 2 , 3;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit 1.5 , 1.2;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit 1,2 fares;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx limit 1,2;");
	    y.clear();
	    y.add(1+"");
	    y.add(2+"");
	    assertEquals(true, x.isValid());
	    assertEquals(y, x.LimitData());
	}
	
	@Test 
	public void order(){
	    
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Select * FROM xxx order;");
	    assertEquals(false, x.isValid());
	 
	    x.setQuery("Select * FROM xxx orderby ;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx order by;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx order by id nam;");
	    assertEquals(false, x.isValid());
	    
	    x.setQuery("Select * FROM xxx order by id;");
	    y.clear();
	    y.add("id");
	    assertEquals(true, x.isValid());
	    assertEquals(y,x.orderData());
	    
	    x.setQuery("Select * FROM xxx order by id AsC;");
	    y.clear();
	    y.add("id");
	    y.add("asc");
	    assertEquals(true, x.isValid());
	    assertEquals(y,x.orderData());
	    
	    x.setQuery("Select * FROM xxx order by id DeSc;");
	    y.clear();
	    y.add("id");
	    y.add("desc");
	    assertEquals(true, x.isValid());
	    assertEquals(y,x.orderData());
	}
	
	@Test
	public void whereOrderbyLimit(){
	    ValidateQuery x = new ValidateQuery();
	    	    
	    x.setQuery("Select * FROM xxx WHERE id = 5 Order by name Limit 4,5;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=5");
	    assertEquals(y, x.whereData());
	    y.clear();
	    y.add("name");
	    assertEquals(y, x.orderData());
	    y.clear();
	    y.add("4");
	    y.add("5");
	    assertEquals(y, x.LimitData());
	    
	    x.setQuery("Select * FROM xxx WHERE id = 5 AND name = \"FARES\" Order by name asc Limit 4,5;");
	    assertEquals(true, x.isValid());
	    y.clear();
	    y.add("id=5");
	    y.add("name=FARES");
	    y.add("and");
	    assertEquals(y, x.whereData());
	    y.clear();
	    y.add("name");
	    y.add("asc");
	    assertEquals(y, x.orderData());
	    y.clear();
	    y.add("4");
	    y.add("5");
	    assertEquals(y, x.LimitData());
	}
}