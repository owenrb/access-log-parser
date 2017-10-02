# access-log-parser

## Environment
### MySQL :
The database connection detail is defined in: `${PROJECT_HOME}/src/main/resources/jdbc.properties`
### Query Statements
Native queries are defined, via `@Query` annotation, in: `${PROJECT_HOME}/src/main/java/com/ef/orm/repository/LogDetailRepository.java`:

	jdbc.driverClassName=com.mysql.jdbc.Driver
	jdbc.url=jdbc:mysql://localhost:3306/ef
	jdbc.username=user
	jdbc.password=password

The database tables are automatically created, or drop-then-create for an existing table, by the Hibernate/JPA libraries in the existing `ef` database.

## Input
### Access Log File
A sample access log file is provided in: `${PROJECT_HOME}/src/main/resources/sample/access.log`
This is the default input file, and it is defined in the Spring XML configuration file.

    <bean id="inputReader" class="com.ef.reader.InputReader">
    	<property name="inputType" value="CLASSPATH" />
    	<property name="path" value="/sample/access.log" />
    </bean>
It is also possible to read log file from System Input (inputType==`SYSTEMIN`) or from an absolute system path (inputType==`FILEPATH`).

### Log Format
The log format applied in the above sample data is: `"%a|%l|%u|%t|%U|\"%r\"|%>s|%b"`. 
This format is based on the Apache's LogFormat directive convention.

The format value is configurable via properties file: `${PROJECT_HOME}/src/main/resources/appconfig.properties`

## Build
Execute Maven with `package` goal to create an executable jar file, with dependencies, saved as `${PROJECT_HOME}/target/parser.jar`

## Execution
 Example Usage:
 
`java -cp parser.jar com.ef.Parser --startDate 2017-10-01.10:00:00 --duration hourly --threshold 100`

 Sample Output:
 
	IP: 172.17.0.1
	TOTAL COUNT: 240
	SUMMARY:
		REQUEST                                            | COUNT | STATUS | COMMENT                                           
		-                                                  | 24    | 408    | Timeout                                           
		/good                                              | 96    | 200    |                                                   
		/index.html                                        | 24    | 200    |                                                   
		/index.html                                        | 24    | 304    |                                                   
		/nothing/here                                      | 24    | 404    | Resource Not Found                                
		/old/session                                       | 24    | 403    | Forbidden                                         
		/secured/userinfo                                  | 24    | 401    | Unauthorized Access                               
	
	IP: 172.17.0.2
	TOTAL COUNT: 130
	SUMMARY:
		REQUEST                                            | COUNT | STATUS | COMMENT                                           
		-                                                  | 13    | 408    | Timeout                                           
		/good                                              | 52    | 200    |                                                   
		/index.html                                        | 13    | 200    |                                                   
		/index.html                                        | 13    | 304    |                                                   
		/nothing/here                                      | 13    | 404    | Resource Not Found                                
		/old/session                                       | 13    | 403    | Forbidden                                         
		/secured/userinfo                                  | 13    | 401    | Unauthorized Access 