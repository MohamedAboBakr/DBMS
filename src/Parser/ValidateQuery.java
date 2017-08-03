package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

public class ValidateQuery implements Query{
    
    private String query;   //the query String
    private int stat;   //the stat of the query //zero for not prepared //one for prepared
    private String arr[];
    private ArrayList<String> parameters;
    private int queryType;
    private String dbname;
    private int wherelocation;
    private int limitlocation;
    private int orderlocation;
    private int error;
    private ArrayList<String> whereList;
    private ArrayList<String> orderList;
    private ArrayList<String> limitList;
    
    public ValidateQuery(){
	whereList = new ArrayList<>();
	orderList = new ArrayList<>();
	limitList = new ArrayList<>();
	orderlocation = -1;
	wherelocation = -1;
	limitlocation = -1;
	dbname = null;
	queryType = -1;
	stat = 0;
	error = 0;
    }
    
    //set the query string
    public void setQuery(String query){
	
	//initialize the variables
	initialize();	
	
	//remove multiple spaces or tabs
	this.query = trim(query.trim());
	
	try{
	    //check for existing of semicolon
	    if(this.query.charAt(this.query.length()-1) == ';'){
		this.query = this.query.substring(0,this.query.length()-1);
		this.query = this.query.trim();
	    }else{
		error("Your query must end with semicolon");
		stat = 0;
		return; 
	    }
	}catch(StringIndexOutOfBoundsException e){
	    error("empty String");
	    return;
	}

	arr = this.query.split(" ");
	stat = 1;
    }
    
    //get the query string
    public String getQuery(){
	return query;
    }
    
    public boolean isValid(){
	
	if(stat == 0)
	    return false;
	
	//initialize the variables
	initialize();
	
	//start the process steps
	try{
	    stage1(0);
	}
	catch(ArrayIndexOutOfBoundsException e){
	    error("Inclompete query");
	    stat = 2;
	    return false;
	}
	catch(StringIndexOutOfBoundsException e){
	    error("Inclompete query");
	    stat = 2;
	    return false;
	}
	//check if it's valid or not
	if(error == 1){
	    stat = 2;
	    return false;
	}else{
	    stat = 3;
	    return true;
	}
    }
    
    public ArrayList<String> parseIt() throws RuntimeException{
	if(stat == 3)
	    return this.parameters;
	else
	    throw new RuntimeException("can't parse invalid query");
    }
    
    public int getQueryType(){
	return this.queryType;
    }
    
    public boolean isWhere(){
	    if(isValid())
		if(wherelocation != -1)
		    return true;
	return false;
    }
    
    public boolean islimit(){
	    if(isValid())
		if(limitlocation != -1)
		    return true;
	return false;
    }
    
    public boolean isOrder(){
	    if(isValid())
		if(orderlocation != -1)
		    return true;
	return false;
    }
    
    public ArrayList<String> whereData(){
	if(isWhere())
	    return whereList;
	return null;
    }
   
    public ArrayList<String> LimitData(){
	if(islimit())
	    return limitList;
	return null;
    }
    
    public ArrayList<String> orderData(){
	if(isOrder())
	    return orderList;
	return null;
    }
    
    private void initialize(){
	this.parameters = new ArrayList<String>();
	this.queryType = -1;
	this.wherelocation = -1;
	this.limitlocation = -1;
	this.orderlocation = -1;
	this.error = 0;
	this.stat = this.stat >1 ?1:0;
	this.limitList = new ArrayList<String>();
	this.orderList = new ArrayList<String>();
	this.whereList = new ArrayList<>();
    }
    
    private String trim(String x){
	int word  = 0;
	boolean working = true;
	String toreturn = "";
	
	for(int i=0;i<x.length();i++){
	    switch(x.charAt(i)){
		case '\t':
		case ' ':
		    if(working){
			if(word == 0){
			    toreturn += " ";
			    word = 1;
			}
		    }else
			toreturn += " ";
		    break;
		case '\"':
		    toreturn += x.charAt(i);
		    working = working==true?false:true;
		    word = 0;
		    break;
		default:
			toreturn += x.charAt(i);
			word = 0;
		    break;
	    }
	}
	return toreturn;
    }
    
    
    //read the first word
    private void stage1(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{

	switch(arr[pos].toLowerCase()){
	    case "use":
		queryType = 1;
		stage2(pos+1);
		break;
	    case "create":
		//table or database
		switch(arr[pos+1].toLowerCase()){
		    case "database":
			queryType = 2;
			stage4(pos+2);
			break;
		    case "table":
			queryType = 3;
			stage3(pos+2);
			break;
		    default :
			error("Syntax error in "+arr[pos+1]);
			return;
		}
		break;
	    case "drop":
		//table or database
		switch(arr[pos+1].toLowerCase()){
		    case "database":
			queryType = 4;
			stage4(pos+2);
			break;
		    case "table":
			queryType = 5;
			stage6(pos+2);
			break;
		    default:
			error("Syntax error in "+arr[pos+1]);
			return;
		}
		break;
	    case "select":
		queryType = 6;
		stage13(pos+1);
		break;
	    case "update":
		queryType = 7;
		    stage11(pos+1);
		break;
	    case "delete":
		queryType = 8;
		    if(arr[pos+1].toLowerCase().equals("from"))
			stage7(pos+2);
		    else{
			error("Syntax error in "+arr[pos+1]);
			return;
		    }
		break;
	    case "insert":
		queryType = 9;
		    if(arr[pos+1].toLowerCase().equals("into"))
			stage9(pos+2);
		    else{
			error("Syntax error in "+arr[pos+1]);
			return;
		    }
		break;
	    case "show":
		queryType = 10;
		if(arr.length != 2){
		    error("show query must be SHOW TABLES");
		    return;
		}
		if(arr[pos+1].toLowerCase().equals("tables"))
		    return;
		else
		{
		    error("Only Works as SHOW TABLES");
		    return;
		}
	    case "describe":
		queryType = 11;
		if(arr.length != 2){
		    error("DESCRIBE query must be DESCRIBE TABLES");
		    return;
		}
		stage6(pos+1);
		break;
	    default :
		error("Syntax error in "+arr[pos]);
		return;
	}
    }
    
    //USE DATABASE
    private void stage2(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	if(pos != arr.length-1)
	{
	    error("USE works only with one database");
	    return;
	}
	parameters.add(arr[pos]);
    }
    
    //CREATE TABLE
    private void stage3(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	parameters.add(arr[pos]);
        stage5(pos+1);
    }
    
    //Drop TABLE
    private void stage6(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{

	if(pos != arr.length-1)
	{
	    error("DROP/CREATE/DESCRIBE TABLE works only with one table");
	    return;
	}
	
	parameters.add(arr[pos]);
    }
    
    //CREATE DATABASE
    private void stage4(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	if(pos != arr.length-1)
	{
	    error("CREATE/DROP DATABASE works only with one database");
	    return;
	}
	
	parameters.add(arr[pos]);
    }
    
    //CREATE TABLE (name type,,)
    private void stage5(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	
	//construct String with (x y,z m,n v)
	String table = new String();
	table = arr[pos];
	for(int i=pos+1;i<arr.length;i++)
	    table += " " + arr[i];
	table = table.trim();
	
	if(table.charAt(0) != '(' || table.charAt(table.length()-1) != ')'){
	    error("missing bracket");
	    return;
	}
	table = table.substring(1, table.length()-1).trim();

	HashMap<String,String> x = new HashMap<String,String>();
	
	//replace "dadad" with the "inti"
	table = hashQuot(x, table);

	//validate col name contain only one text
	if(!validateSingleValue(x))
	    return;
	
	//construct "x" y
	//	    "z" m
	//	    "n" v
	String terms[] = table.split(",");
	for(int i=0;i<terms.length;i++)
	    terms[i] = terms[i].trim();
	
	//check the string and the datatype
	if(!checkForColNamesAndDataType(terms))
	    return;
	
	//construct string as
	//	x
	//	int
	//	yy
	//	varchar
	ArrayList<String> colsName = new ArrayList<String>();
	
	for(int i=0;i<terms.length;i++){
	    if(x.get(terms[i].split(" ")[0]) == null){
		error("error in your syntax");
		return;
	    }
	    colsName.add(x.get(terms[i].split(" ")[0]));
	    parameters.add(x.get(terms[i].split(" ")[0]));
	    parameters.add(clean(terms[i].split(" ")[1]));
	}
	if(checkEmptyRepition(colsName)){
	    error("repeated col name");
	    return;
	}
    }
    
    private boolean checkForColNamesAndDataType(String terms[]){
	for(int i=0;i<terms.length;i++){
	    
	    //check for name and type
	    String temp[] = terms[i].split(" ");
	    if(temp.length != 2){
		error(" Syntax error in \"" + terms[i] + "\"");
		return false;
	    }
	    
	    //check the string following the convention or not
	    if(!checkString(temp[0]))
		return false;
	    
	    //check for datatype
	    if(!temp[1].toLowerCase().equals("int") && !temp[1].toLowerCase().equals("varchar")){
		error("Not supported datatype in \"" + temp[1] + "\"");
		return false;
	    }
	    
	}
	return true;
    }
    
    private boolean validateSingleValue(HashMap<String,String> x){
	Set<String> y = x.keySet();
	for(String key : y){
	    if(x.get(key).trim().split(" ").length > 1 || x.get(key).trim().isEmpty()){
		error("column name must be one word");
		return false;
	    }
	}
	return true;
    }
    
    private boolean checkString(String x){
	if(clean(x).equals(x)){
	    error("strings must be inside duble coutation and following the convention");
	    return false;
	}else
	    return true;
    }
	
    private boolean checkStringWithNoErrorMsg(String x){
	if(clean(x).equals(x))
	    return false;
	else
	    return true;
    }
    
    //DELETE FROM dbname
    private void stage7(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	    parameters.add(arr[pos]);
	    stage8(pos+1);
    }
    
    //handle WHERE LIMIT ORDERBY
    private void stage8(int pos)  throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{	
	//check if the pos inside the array
	if(pos >= arr.length)
	    return;
	
	//collect the rest of the sentence to one string
	String str = collectorToEnd(pos, arr);
	
	//Hash the quotation
	HashMap<String,String> x = new HashMap<String,String>();
	str = hashQuot(x, str);
	replaceHashQuot(str, x);
	str = str.replaceAll("\\("," ( ");
	str = str.replaceAll("\\)"," ) ");
	str = str.replaceAll("="," = ");
	str = str.replaceAll(">"," > ");
	str = str.replaceAll("<"," < ");
	str = trim(str);
	
	String warr[] = str.split(" ");
	
	//check if there is only one where and know it's location
	if(contianOnce(str, "where")){
	    wherelocation = findIndex(0, warr, "where");
	}else
	    return;

	
	//check if there is only one limit and know it's location
	if(contianOnce(str, "limit")){
	    limitlocation = findIndex(0, warr, "limit");
	}else
	    return;
	
	//check if there is only one order and know it's location
	if(contianOnce(str, "order by")){
	   orderlocation = findIndex(0, warr, "order" , "by");
	}else
	    return;
	
	if(!checkOrder()){
	    return;
	}
	
	for(int i=0;i<warr.length;i++)
	    if(x.get(warr[i]) != null)
		warr[i] = "\"" + x.get(warr[i]) + "\"";

	//build the string of WHERE
	String conditions = buildWhere(warr);
	
	//build the string of LIMIT
	String limits = buildLimit(warr);
	
	//build the string of ORDERBY
	String orders = buildOrder(warr);
		
	//process the limit sting as x y
	if(!processLimit(limits))
	    return;
	
	//process the order string as XXXX
	if(!processOrder(orders))
	    return;
	
	//process where string
	if(!processWhere(conditions))
	    return;
	
    }
    
    private boolean processWhere(String conditions){
	if(wherelocation != -1){
	    HashMap<String,String> quotWhere = new HashMap<String,String>();
	    conditions = hashQuot(quotWhere, conditions);
	    conditions = wherePrepare(conditions);
	    System.out.println(conditions);
	    if(validateWhereQuery(conditions)){
		conditions = replaceHashQuot(conditions,quotWhere);
		getConditions(conditions);
	    }else{
		error("during parsing of where queries");
		return false;
	    }
	}
	return true;
    }
    
    private boolean processLimit(String limits){
	if(limitlocation != -1){
	    if(limits.split(",").length == 2){
		if(isNumeric(limits.split(",")[0]) && isNumeric(limits.split(",")[1])){
		    limitList.add(limits.split(",")[0].trim());
		    limitList.add(limits.split(",")[1].trim());
		}else{
		    error("Syntax error in limits , Intergers only");
		    return false;    
		}
	    }else{
		error("Syntax error in limits");
		return false;
	    }
	}
	return true;
    }
    
    private boolean processOrder(String orders){
	if(orderlocation != -1){
	    if(orders.trim().split(" ").length > 2){
		error("Syntax error in orders");
		return false;
	    }else if(orders.trim().split(" ").length == 2){
		if(!orders.split(" ")[1].trim().toLowerCase().equals("asc") && !orders.split(" ")[1].trim().toLowerCase().equals("desc")){
		    error("order must be ASC or DESC Only");
		    return false;
		}
		orderList.add(orders.trim().split(" ")[0].trim());
		orderList.add(orders.trim().split(" ")[1].trim().toLowerCase());
	    }else if(orders.trim().split(" ").length == 1 && !orders.trim().split(" ")[0].isEmpty()){
		orderList.add(orders.trim().split(" ")[0].trim());
	    }else{
		error("No parameters given to ORDER BY");
		return false;
	    }
	}
	return true;
    }
    
    private String buildLimit(String warr[]){
	String limits = "";
	if(limitlocation != -1){
	    for(int i=limitlocation;i<warr.length;i++)
		limits += " "+warr[i];
	    limits = limits.trim();
	}
	return limits;
    }
    
    private String buildOrder(String warr[]){
	String orders = "";
	if(orderlocation != -1){
	    for(int i=orderlocation+1;i<warr.length;i++){
		if(i == limitlocation-1){
		    break;
		}
		orders += " "+warr[i];
	    }
	    orders = orders.trim();
	}
	return orders;
    }
    
    private String buildWhere(String warr[]){
	String conditions = "";
	if(wherelocation != -1){
	    for(int i=wherelocation;i<warr.length;i++){
		if(i == orderlocation-2 || i == limitlocation-1){
		    break;
		}
		if(warr[i].toLowerCase().equals("and") || warr[i].toLowerCase().equals("or"))
		    conditions += " "+warr[i].toLowerCase();
		else
		    conditions += " "+warr[i];
	    }
	    conditions = conditions.trim();
	}
	
	return conditions;
    }
    
    private String collectorToEnd(int pos,String arr[]){
	
	String str = "";
	for(int i=pos;i<arr.length;i++){
	    if(arr[i].toLowerCase().equals("where") || arr[i].toLowerCase().equals("limit"))
		str += " " + arr[i].toLowerCase();
	    else if(arr[i].toLowerCase().equals("order")){
		if(i+1 < arr.length){
		    if(arr[i+1].toLowerCase().equals("by"))
			str += " " + "order by";
		}
	    }
	    else
		str += " " + arr[i];
	}
	return str;
    }
    
    private int findIndex(int pos, String warr[],String str1 , String str2){
	    for(int i=pos;i<warr.length;i++){
		if(warr[i].toLowerCase().equals(str1)){
		    if(warr[i+1].toLowerCase().equals(str2)){
			return i+2;
		    }
		}
	    }
	    return -1;
    }
    
    private boolean contianOnce(String str , String pattern){
	if(str.contains(pattern) && str.replace(pattern, " ").contains(pattern)){
	    error("contians "+pattern+" two times");
	    return false;
	}
	return true;
    }
    
    private int findIndex(int pos, String warr[],String pattern){
	    for(int i=pos;i<warr.length;i++){
		if(warr[i].toLowerCase().equals(pattern)){
		    return i+1;
		}
	    }
	    return -1;
    }
    
    private void stage9(int pos)  throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
        parameters.add(arr[pos]);
        stage10(pos+1);
    }
    
    //(,) VALUES ('','')
    private void stage10(int pos)  throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{

	String param;	//(,,,)
	String values;	//(,,,)
	int j;

	param = arr[pos];
	for(j=pos+1;j<arr.length;j++){
	    if(arr[j].toLowerCase().equals("values"))
		break;
	    param += " " + arr[j];
	}
	
	values = arr[j+1];
	for(j = j+2;j<arr.length;j++)
	    values += " " + arr[j];
		
	HashMap<String,String> x = new HashMap<String,String>();
	    
	int start = 0;
	String curr = "";
	for(int i=0;i<values.length();i++){
	    if(values.charAt(i) == '\"'){
		start = start==0?1:0;
	    }else{
		if(start == 1){
		    curr += values.charAt(i);
		}
	    }
	    if(start == 0){
		if(curr.length() > 0){
		    values = values.replace(curr, i+"i");
		    x.put("\"" + i + "i\"", curr);
		}
	    }
	    if(start == 0)
	        curr = "";
	}
	ArrayList<String> paramFinal = convertListToString(param);
	ArrayList<String> valuesFinal = convertListToString(values);

	if(paramFinal == null || valuesFinal == null){
	    error("error in your syntax");
	    return;
	}
	
	if(checkEmptyRepition(paramFinal)){
	    error("your columns name contains repeated or empty names");
	    return;
	}
	
	if(valuesFinal.size() != paramFinal.size()){
	    error("Number of Cols doesn't equal number of data");
	    return;
	}
	
	parameters.add(valuesFinal.size()+"");
	for(int i=0;i<valuesFinal.size();i++){
	    if(paramFinal.get(i).split(" ").length > 1){
		error("param values can't have spaces");
		return;
	    }
	    if(!valuesFinal.get(i).contains("\"")){
		if(valuesFinal.get(i).split(" ").length > 1){
		    error("integers values can't have spaces");
		    return;  
		}
		if(valuesFinal.get(i).length() < 1){
		    error("Database doesn't support null data");
		    return;
		}
		if(!isNumeric(valuesFinal.get(i).trim())){
		    error("integers values must be between 0-9");
		    return; 
		}
	    }else{
		if(x.get(valuesFinal.get(i)) == null){
		    error("Syntax error " + valuesFinal.get(i));
		    return;
		}
		valuesFinal.set(i, x.get(valuesFinal.get(i)));
	    }
	    parameters.add(paramFinal.get(i));
	    parameters.add(valuesFinal.get(i));
	}
    }
    
    //UPDATE tablename SET
    private void stage11(int pos){
	parameters.add(arr[pos]);
	if(arr[pos+1].toLowerCase().equals("set")){
	    stage12(pos+2);
	}else{
	    error("\"SET\" wasn't found in the query");
	    return;
	}
    }
    // x = 1 , y = 2 , z = 3
    private void stage12(int pos)  throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	String setters;
	setters = arr[pos];
	int k;
	for(k=pos+1; k<arr.length;k++){
	    if(!arr[k].toLowerCase().equals("where")){
		setters += " " + arr[k];
	    }
	    else
		break;
	}
	
	HashMap<String,String> x = new HashMap<String,String>();
	int start = 0;
	String curr = "";
	for(int i=0;i<setters.length();i++){
	    if(setters.charAt(i) == '\"'){
		start = start==0?1:0;
	    }else{
		if(start == 1){
		    curr += setters.charAt(i);
		}
	    }
	    if(start == 0){
		if(curr.length() > 0){
		    setters = setters.replace(curr, i+"");
		    x.put("\"" + i + "\"", curr);
		}
	    }
	    if(start == 0)
	        curr = "";
	}
	
	String sets[] = setters.trim().split(",");
	String param = "";
	String values = "";
	ArrayList<String> cols = new ArrayList<String>();
	
	for(int i=0;i<sets.length;i++){
	    String halfs[] = sets[i].split("=");
	    if(halfs.length == 2){
		if(halfs[0].trim().split(" ").length == 1 && halfs[1].trim().split(" ").length == 1){
		    param += " " + clean(halfs[0].trim());
		    if(checkStringWithNoErrorMsg(halfs[1].trim()) || isNumeric(halfs[1].trim()))
		    {}
		    else{
			error("set parameter should only be string or integer");
			return;
		    }
		}else{	
		    error("error in set parameters");
		    return;
		}
	    }else{
	        error("error in set parameters , should always be x = y");
	        return;
	    }
	    halfs[1] = halfs[1].trim();
	    cols.add(halfs[0].trim());
	    parameters.add(halfs[0].trim());
	    if(halfs[1].trim().charAt(0) == '"' && halfs[1].trim().charAt(halfs[1].trim().length()-1) == '"')
		halfs[1] = x.get(halfs[1].trim());
	    parameters.add(halfs[1]);
	}
	if(checkEmptyRepition(cols)){
	    error("names of your columns contains repeated or empty ones");
	    return;
	}
	stage8(k);
    }
    
    private void stage13(int pos) throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	int k;
	String stables = arr[pos];
	for(k =pos+1;k<arr.length;k++){
	    if(!arr[k].toLowerCase().equals("from"))
		stables += " " + arr[k];
	    else
		break;
	}
	
	String tables[] = stables.split(",");
	String result = "";
	ArrayList<String> cols = new ArrayList<String>();
	
	for(int i=0;i<tables.length;i++){
	     
	    tables[i] = tables[i].trim();
	    if(tables[i].split(" ").length != 1){
		error("Syntax error "+ tables[i]);
		return;
	    }
	    cols.add(tables[i]);
	    result += " " + tables[i];
	}
	
	if(checkEmptyRepition(cols)){
	    error("collumns contains repeated names or empty ones");
	    return;
	}
	
	result = result.trim();
	parameters.add(result);	
	stage7(k+1);
    }
    
    private boolean checkOrder(){
	//check order WHERE then ORDER then limit and thier existing
	if(orderlocation != -1 && wherelocation != -1 && limitlocation != -1 ){
	    if(wherelocation > orderlocation || orderlocation > limitlocation ||
		    wherelocation > orderlocation){
		error("Syntax error, WHEN ORDER LIMIT order wrong");
		return false;
	    }
	}else if(wherelocation != -1 && orderlocation != -1){
	    if(wherelocation > orderlocation){
		error("Syntax error, WHEN ORDER order wrong");
		return false;
	    }
	}else if(wherelocation != -1 && limitlocation != -1){
	    if(wherelocation > limitlocation){
		error("Syntax error, WHEN LIMIT order wrong");
		return false;
	    }
	}else if(orderlocation != -1 && limitlocation != -1){
	    if(orderlocation > limitlocation){
		error("Syntax error, ORDER LIMIT order wrong");
		return false;
	    }
	}else if(orderlocation == -1 && limitlocation == -1 && wherelocation == -1){
	    error("Syntax error, NO WHERE, ORDER, LIMIT after the query");
	    return false;
	}
	return true;
    }
    
    private String replaceHashQuot (String con , HashMap<String,String> x){
	Set<String> y = x.keySet();
	for(String s : y){
	    con = con.replace(s, x.get(s));
	}
	return con;
    }
    
    private void getConditions(String sentString) {

		Stack<String> evaluatorStack = new Stack<String>();

		StringBuilder getcondition = new StringBuilder();

		ArrayList<String> conditions = new ArrayList<String>();

		sentString = sentString.replace("(", " ( ");
		sentString = sentString.replace(")", " ) ");

		for (int i = 0; i < sentString.length(); i++) {
			if (sentString.charAt(i) == '(') {
				evaluatorStack.push("(");
			} else if (sentString.charAt(i) == ')') {

				while (evaluatorStack.size() != 0) {
					String temp = evaluatorStack.pop().toString();
					if (temp != "(")
						conditions.add(temp);
					else
						break;
				}
			} else if (sentString.charAt(i) == ' ')
				continue;
			else if (sentString.substring(i, i + 1).equals("'")) {
				i++;
				while ((i) < sentString.length() && !sentString.substring(i, i + 1).equals("'")) {
					getcondition.append(sentString.charAt(i));
					i++;
				}
				conditions.add(getcondition.toString());
				getcondition.delete(0, getcondition.length());

			} else if (sentString.charAt(i) == 'a' || sentString.charAt(i) == 'A') {
				getcondition.append(sentString.charAt(i));
				getcondition.append(sentString.charAt(i + 1));
				getcondition.append(sentString.charAt(i + 2));

				if (getcondition.toString().toLowerCase().equals("and")) {
					evaluatorStack.push(getcondition.toString().toLowerCase());
					getcondition.delete(0, getcondition.length());
				}
			} else if (sentString.charAt(i) == 'o' || sentString.charAt(i) == 'O') {
				getcondition.append(sentString.charAt(i));
				getcondition.append(sentString.charAt(i + 1));

				if (getcondition.toString().toLowerCase().equals("or")) {
					while (evaluatorStack.size() != 0 && evaluatorStack.peek().toString() == "and")
						conditions.add(evaluatorStack.pop().toString());
					evaluatorStack.push(getcondition.toString().toLowerCase());
					getcondition.delete(0, getcondition.length());
				}
			}
		}
		while (evaluatorStack.size() != 0)
			conditions.add(evaluatorStack.pop().toString());
		
		whereList = conditions;

    }
    
    private String hashQuot (HashMap<String,String> x , String setters){
	    int start = 0;
	    String curr = "";
	    for(int i=0;i<setters.length();i++){
		if(setters.charAt(i) == '\"'){
		    start = start==0?1:0;
		}else{
		    if(start == 1){
			curr += setters.charAt(i);
		    }
		}
		if(start == 0){
		    if(curr.length() > 0){
			setters = setters.replace("\"" + curr + "\"","\"" +  i+"i" + "\"");
			x.put("\"" + i + "i\"", curr);
		    }
		}
		if(start == 0)
		    curr = "";
	    }
	    
	    return setters;
    }
    
    private String wherePrepare (String setters){
	
	    //make sure it's surrounding with spaces
	    setters = setters.replaceAll("="," = ");
	    setters = setters.replaceAll(">"," > ");
	    setters = setters.replaceAll("<"," < ");
	    setters = setters.replaceAll("\\("," ( ");
	    setters = setters.replaceAll("\\)"," ) ");

	    //remove unnesessary spaces
	    setters = setters.replaceAll("\\s+"," ");
	    setters = setters.replaceAll("\\t+"," ");
	    
	    //cut most of the spaces
	    setters = setters.replaceAll(" = ","=");
	    setters = setters.replaceAll(" > ",">");
	    setters = setters.replaceAll(" < ","<");
	    setters = setters.trim();
	    
	    String toget;
	    toget = setters.replaceAll("\\(","");
	    toget = toget.replaceAll("\\)","");
	    toget = toget.trim();
	    
	    String arr[] = toget.split("and|anD|aNd|aND|And|AnD|ANd|AND|or|oR|Or|OR");
	    for(int i=0;i<arr.length;i++)
		System.out.println(arr[i]);
	    //change the condition
	    for(int i=0;i<arr.length;i++)
		setters = setters.replaceFirst(arr[i].trim(), "'" + arr[i].trim() + "'");
	    
	    return setters;
    }
    
    private boolean validateWhereQuery(String x){
	    x = x.replaceAll("\\(","");
	    x = x.replaceAll("\\)","");
	    String arr[] = x.split("and|anD|aNd|aND|And|AnD|ANd|AND|or|oR|Or|OR");
	    for(int i=0;i<arr.length;i++){
		//validate the condition
		String cond = arr[i].trim();
		cond = cond.trim().substring(1,cond.length()-1).trim();
		if(cond.split("=").length + cond.split(">").length + cond.split("<").length != 4){
		    return false;
		}
		
		String p[] = new String[2];
		if(cond.split("=").length == 2)
		    p = cond.split("=");
		if(cond.split(">").length == 2)
		    p = cond.split(">");
		if(cond.split("<").length == 2)
		    p = cond.split("<");

		if(p[0].split(" ").length != 1 || p[1].split(" ").length != 1)
		{
		    return false;
		}

		if(!isNumeric(p[1].trim()) && !checkStringWithNoErrorMsg(p[1].trim())){
		    return false;
		}
	    }
	return true;
    }
    
    private boolean checkEmptyRepition(ArrayList<String> x){
	
	//check for being empty
	for(int i=0;i<x.size();i++){
	    if(x.get(i).trim().isEmpty())
		return true;
	}
	
	//check for repitions
	for(int i=0;i<x.size();i++){
	    for(int j=i+1;j<x.size();j++){
		if(x.get(i).equals(x.get(j)))
		    return true;
	    }
	}
	
	return false;
    }
    
    private boolean isNumeric(String x){
	x = x.trim();
	
	for(int i=0;i<x.length();i++){
	    switch(x.charAt(i)){
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
		    break;
		default:
		    return false;
	    }
	}
	
	return true;
    }
    
    //from (x,y,z,w) to x y z w
    private ArrayList<String> convertListToString(String x)  throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
    
	String paramterms[] = x.split(",");
	for(int i=0;i<paramterms.length;i++){
	    paramterms[i] = paramterms[i].trim();
	}
	//check for ( AND )
	if(paramterms[0].charAt(0) != '(' || paramterms[paramterms.length-1].charAt(paramterms[paramterms.length-1].length()-1) != ')' ){
	    error("Syntax error , bracket not found");
	    return null;
	}
	
	//edit the first and last one
	paramterms[0] = paramterms[0].substring(1).trim();
	paramterms[paramterms.length-1] = paramterms[paramterms.length-1].substring(0, paramterms[paramterms.length-1].length()-1).trim();
	
	//Trim the term
	for(int i=0;i<paramterms.length;i++){
	    paramterms[i] = paramterms[i].trim();
	}
	
	ArrayList<String> toreturn = new ArrayList<String>();
	
	for(int i=0;i<paramterms.length;i++)
	    toreturn.add(paramterms[i].trim());
	
	return toreturn;
    }
    
    private String clean(String x)  throws ArrayIndexOutOfBoundsException , StringIndexOutOfBoundsException{
	boolean flag = true;
	while(flag){
	    if(x.charAt(x.length()-1) == x.charAt(0)){
		switch(x.charAt(0)){
		    case '\"':
		    case '\'':
		    case '`':
			x = x.substring(1, x.length()-1);
			break;
		    default:
			flag = false;
			break;
		}
	    }
	    flag = false;
	}
	
	return x;
    }
    
    private void error(String error){
	System.out.println("ERROR " + error);
	this.error = 1;
    }
}
