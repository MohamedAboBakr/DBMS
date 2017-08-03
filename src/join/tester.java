package join;
import java.util.ArrayList;
import java.util.Arrays;

import Parser.*;
import Files.*;
import tools.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.locks.Condition;

import javax.annotation.processing.ProcessingEnvironment;
import javax.xml.stream.events.StartDocument;

public class tester{
    
    String query;
    String curr_db = null;
    List<Map<String,String>> Display = new ArrayList<Map<String,String>>();
    
    //Object of the parser
    ValidateQuery parser = new ValidateQuery();
    DbController controller = new DbController();
    Intersection and = new Intersection();
    Limit limit = new Limit();
    Sort order = new Sort();
    Union or = new Union();
    //Object of the file Manipulation
	//TODO
    //Object of printing
	//TODO
    
    private void getQuery(){
	
        System.out.print(">> ");
	//get the data from the user
	Scanner x = new Scanner(System.in);
	String query = x.nextLine();
	//assign the data to the class's var
	this.query = query;
    }
    //validate
    private boolean validate(){
	parser.setQuery(this.query);
	return parser.isValid();
    }
    //parse
    private ArrayList<String> getData(){
	
	try{
	    return parser.parseIt();
	}
	catch(RuntimeException e){
	    return null;
	}
    }
    //execute
    private void executeQuery(ArrayList<String> data){
	//TODO
	
	//switch between the query types 
	switch(parser.getQueryType()){
	    case 1: // use
	    	this.curr_db = data.get(0);
	    	break;
	    case 2:
	    	this.curr_db = data.get(0);
	    	 this.controller.CreateDB(curr_db);
	    	// creat database return string (database name)
	    	break;
	    case 3:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.createTable(data);
	    	
	    	// creat table  
	    	/*
	    	 * table name , db name , no. of columns .
	    	 *  2 list of strings --> name of columns - type of columns(String , Integer)
	    	 */
	    	break;
	    case 4:
	    	this.curr_db = data.get(0);
	    	this.controller.DropDB(curr_db);
	    	// drop database   return db name
	    	break;
	    case 5:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.controller.DropTable(curr_db, data.get(0));
	    	// drop table  return db name , table name
	    	break;
	    case 6:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.select(data);
	    	// select  
		break;
	    case 7:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.update(data);
	    	// update or modify
	    	break;
	    case 8:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.delete(data);	// delete 
	    	break;
	    case 9:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.insert(data);
	    	// insert return table name , db name , list of strings of row  , list of String of values//
	    	//AddRow
	    	break;
	    case 10:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.Display = controller.showTables(curr_db);
	    	// show tables >>> 
	    	break;
	    case 11:
	    	if(this.curr_db == null)
	    		return;
	    	else
	    		this.Display = controller.describeTable(curr_db, data.get(0));
	    	// describe table name , db name
	    	break;
	}//
	
    }   
    private void createTable ( ArrayList<String> s){
    	ArrayList<String>colNames = new ArrayList<>();
    	ArrayList<String>colTypes = new ArrayList<>();
    	for(int i = 1; i < s.size()-1; i+=2){
    		colNames.add(s.get(i));
    		if(s.get(i+1).equals("int"))
    			colTypes.add("Integer");
    		else
    			colTypes.add("String");
    	}
    	System.out.println(s);
    	this.controller.CreateTable(s.get(0), curr_db, (s.size()-1)/2, colNames, colTypes);
    }  
    private void update(ArrayList<String> s){
	   ArrayList<String> colNames = new ArrayList<>();
	   ArrayList<String> colValues = new ArrayList<>();
	   System.out.println(s);
	   for(int i = 1; i < s.size()-1; i+=2){
		   colNames.add(s.get(i));
		   colValues.add(s.get(i+1));
	   }
	   if(this.parser.isWhere())
	   {
		   List<String> old = new ArrayList<>();
		   ArrayList<String> w = this.parser.whereData();
		   System.out.println("WW" + w);
		   System.out.println("SS" + s);
   		   this.Display = this.evaluate2(w,s);
  		   if(this.Display == null)
  			   return; 
   		   w.clear();
   			for(int i = 0; i < this.Display.size(); i++){
   				for(String ss : this.Display.get(i).keySet()){
   					old.add(this.Display.get(i).get(ss));w.add("=");
   				}
   				this.controller.ModifyRow(s.get(0),curr_db, colNames,w ,old, colNames, colValues);
   				w.clear();old.clear();
   			}
   			this.Display.clear();
   		}
	   else
		   this.controller.ModifyRow(s.get(0),curr_db, null, null,null, colNames, colValues);
   }
    private void insert(ArrayList<String> s){
	   ArrayList<String> colNames = new ArrayList<>();
	   ArrayList<String> colValues = new ArrayList<>();
	   for(int i = 2; i < s.size()-1; i+=2){
		   colNames.add(s.get(i));
		   colValues.add(s.get(i+1));
	   }
	   System.out.println(colNames);
	   System.out.println(colValues);
	   this.controller.AddRow(s.get(0), curr_db, colNames, colValues);
   }
    private void delete(ArrayList<String> s){
	   //loop for conditions
    	 if(this.parser.isWhere()){
  		   List<String> old = new ArrayList<>();
  		   ArrayList<String> w = this.parser.whereData();
  		   List<String> x = new ArrayList<>();
  		   this.Display = this.evaluate2(w,s);
  		   if(this.Display == null)
  			   return;
  		   w.clear();
 			for(int i = 0; i < this.Display.size(); i++){
 				for(String ss : this.Display.get(i).keySet()){
 					old.add(this.Display.get(i).get(ss));w.add("=");
 					x.add(ss);
 				}
 				System.out.println(x);
 				System.out.println(w);
 				System.out.println(old);
 				this.controller.DeleteRow(s.get(0), curr_db, x, w, old);
 				w.clear();old.clear();s.clear();
 			}
 			this.Display.clear();
     	}
    	 else{
    		 this.controller.DeleteRow(s.get(0), curr_db, null, null, null);
    		 System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFF");
    	 }
   }
    private void select(ArrayList<String> s){
    	if(this.parser.isWhere()){
    		ArrayList<String> w = this.parser.whereData();
    		System.out.println("F" + w);
    		this.Display = this.evaluate(w,s);
    	}
    	else{
    		this.Display = this.controller.Select(s.get(1), curr_db, null, null, null);
    	}
    	
    	
    	if(!s.get(0).equals("*")){
        	List<String> cols = Arrays.asList(s.get(0).split(" "));
        	List<Map<String,String>> res = new ArrayList<>();
        	Map<String,String> m = new HashMap<>();
        	boolean flag = false;
    		for(int j = 0; j < cols.size(); j++){
    			if(this.Display == null)
    				return;
    			if(!this.Display.get(0).containsKey(cols.get(j))){
    				flag = false; this.Display = null; return;
    			}
    			else
    				continue;
    		}
    		if(flag){
    			for(int i = 0; i < this.Display.size(); i++){
    				for(int j = 0; j < cols.size(); j++)
    					m.put(cols.get(j), this.Display.get(i).get(cols.get(j)));
    				res.add(m);
    			}
    			this.Display = res;
    		}
    	}
    	if(this.parser.isOrder()){
    		List<String> o = this.parser.orderData();
    		if(o.size() == 1)
    			this.Display = this.order.sortByKey(this.Display, o.get(0), "asc");
    		else
    			this.Display = this.order.sortByKey(this.Display, o.get(0), o.get(1));
    	}
    	if(this.parser.islimit()){
    		ArrayList<String> l = parser.LimitData();
    		this.Display = limit.limit(Display, Integer.valueOf(l.get(0)), Integer.valueOf(l.get(1)));
    	}
    }
    
    private List<Map<String,String>> evaluate(List<String> w , List<String> s){
    	Stack<List<Map<String,String>>> stak = new Stack<>();
    	List<String> condtion = new ArrayList();
    	List<String> col= new ArrayList<String>();
    	List<String> rel= new ArrayList<String>();
    	List<String> val= new ArrayList<String>();   
    	List<Map<String,String>> listMap1= new ArrayList();
    	List<Map<String,String>> listMap2= new ArrayList();
    	List<Map<String,String>> ret;
    	System.out.println(w);
    	System.out.println(s);
    	for(int i = 0; i < w.size(); i++){
    		col.clear();
    		rel.clear();
    		val.clear();
    		condtion.clear();
    		if(!w.get(i).equals("and") && !w.get(i).equals("or")){
    			String warr[] = new String[1];
    			
    			if(w.get(i).split("=").length > 1){
    				warr = w.get(i).split("=");
    				condtion.add(warr[0]);
    				condtion.add("=");
    				condtion.add(warr[1]);
    			}
    			else if(w.get(i).split("<").length > 1){
    				warr = w.get(i).split("<");

    				condtion.add(warr[0]);
    				condtion.add("<");
    				condtion.add(warr[1]);
    			}
    			else if(w.get(i).split(">").length > 1){
    				warr = w.get(i).split(">");
    				condtion.add(warr[0]);
    				condtion.add(">");
    				condtion.add(warr[1]);
    			}
    			
    			col.add(condtion.get(0));
    			rel.add(condtion.get(1));
    			val.add(condtion.get(2));
    			ret = this.controller.Select(s.get(1), curr_db, col, rel, val);
    			stak.push(ret); System.out.println(stak.size());
    		}
    		else if(!stak.isEmpty() && stak.size() > 1){
    			System.out.println(stak.peek());
    			listMap1 = stak.pop();listMap2 = stak.pop();
    			if(w.get(i).equals("and")){
    				stak.push(and.intersect(listMap1, listMap2));
    			}
    			else if(w.get(i).equals("or")){
    				stak.push(or.union(listMap1, listMap2));
    				System.out.println(listMap1);
    				System.out.println(listMap2);
    			}else
    				 return null;
    		}
    	}
    	return stak.pop();
    }
    

    private List<Map<String,String>> evaluate2(List<String> w , List<String> s){
    	Stack<List<Map<String,String>>> stak = new Stack<>();
    	List<String> condtion = new ArrayList();
    	List<String> col= new ArrayList<String>();
    	List<String> rel= new ArrayList<String>();
    	List<String> val= new ArrayList<String>();   
    	List<Map<String,String>> listMap1= new ArrayList();
    	List<Map<String,String>> listMap2= new ArrayList();
    	List<Map<String,String>> ret;
    	System.out.println(w);
    	System.out.println(s);
    	for(int i = 0; i < w.size(); i++){
    		col.clear();
    		rel.clear();
    		val.clear();
    		condtion.clear();
    		if(!w.get(i).equals("and") && !w.get(i).equals("or")){
    			String warr[] = new String[1];
    			
    			if(w.get(i).split("=").length > 1){
    				warr = w.get(i).split("=");
    				condtion.add(warr[0]);
    				condtion.add("=");
    				condtion.add(warr[1]);
    			}
    			else if(w.get(i).split("<").length > 1){
    				warr = w.get(i).split("<");

    				condtion.add(warr[0]);
    				condtion.add("<");
    				condtion.add(warr[1]);
    			}
    			else if(w.get(i).split(">").length > 1){
    				warr = w.get(i).split(">");
    				condtion.add(warr[0]);
    				condtion.add(">");
    				condtion.add(warr[1]);
    			}
    			
    			col.add(condtion.get(0));
    			rel.add(condtion.get(1));
    			val.add(condtion.get(2));
    			ret = this.controller.Select(s.get(0), curr_db, col, rel, val);
    			stak.push(ret); System.out.println(stak.size());
    		}
    		else if(!stak.isEmpty() && stak.size() > 1){
    			System.out.println(stak.peek());
    			listMap1 = stak.pop();listMap2 = stak.pop();
    			if(w.get(i).equals("and")){
    				stak.push(and.intersect(listMap1, listMap2));
    			}
    			else if(w.get(i).equals("or")){
    				stak.push(or.union(listMap1, listMap2));
    				System.out.println(listMap1);
    				System.out.println(listMap2);
    			}else
    				 return null;
    		}
    	}
    	return stak.pop();
    }
    
    
    private void debugList (ArrayList<String> x){
	System.out.println();
	for(int i=0;i<x.size();i++){
	    System.out.println(x.get(i));
	}
	System.out.println("---------------------------------");
    }
    
    public static void main(String args[]){
	
	tester start = new tester();
	Print printer = new Print();
	FillMap f = new FillMap();
	printer.getOutput(f.g(), f.fill());
	while(true){
	    start.getQuery();
	    if(start.validate()){
			System.out.println("It's valid");
			ArrayList<String> data = start.getData();   //get the data

			start.executeQuery(data);   //execute the query
			
			if(start.curr_db == null) {
				System.out.println("No database Selected yet !");
			}
			else if((start.parser.getQueryType() == 6) || (start.parser.getQueryType() == 10) || (start.parser.getQueryType() == 11)){
				if(start.Display == null)
					System.out.println("Error");
				else if(start.Display.size() == 0)
						System.out.println("Invalid operation");
				else{
					List<String> colNames = new ArrayList<>();
					for(String s : start.Display.get(0).keySet()){
						colNames.add(s);
					}
					printer.getOutput(colNames, start.Display);
				}
			}
			
		}
	    else{
		System.out.println("it's not valid");
	    }
	}
   }   
}