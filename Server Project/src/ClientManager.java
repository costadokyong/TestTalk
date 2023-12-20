import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ClientManager extends Thread {
	
	private Socket client;
	private String clientName;
	private static final Map<String,ClientManager> clients = new HashMap<>();
	private BufferedReader reader;
	private PrintWriter writer;
	
	//User data 
	private String ID;
	private String Password;
	private String Name;
	private String PhoneNumber;
	private String NULL;
	StringTokenizer st;
	
	String resultStrName = null;
	String resultStrPhoneNumber = null;
	
	//DB Connection 
	Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://127.0.0.1:3306/db_user?useTimezone=true&serverTimezone=UTC";
	String user = "root";
	String passwd = "1234";
	
	String insertStringUserInfo = "INSERT INTO userinfo_data VALUES ";
	String insertStringUserLoginInfo = "INSERT INTO login_data VALUES ";
	String recordStringUserInfo;
	String recordStringUserLoginInfo;
	

	ClientManager(Socket c){
		this.client = c;
		start();
	}
	
	
	
	public void run() {
///////////////////////////////////////////////////////DB Connection/////////////////////////////////////////////////////////////
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: "); 
			System.err.println(e.getMessage());
			return;
		}
		
////////////////////////////////////////////////// client and server connecttion////////////////////////////////////////////////
		 try {
			 reader = new BufferedReader (new InputStreamReader( client.getInputStream()));
			 writer = new PrintWriter(client.getOutputStream(), true);
			//writer.println("por favor escreva seu nome");
			String msg;
			//this.clientName = msg;
			while(true) {
				msg = reader.readLine();
				if(msg.startsWith("UserLoginInformation")) {
/////////////////////////////////////////////////////////////getting user`s login information////////////////////////////////////// 
					st = new StringTokenizer(msg,"/");
					NULL = st.nextToken();
					ID = st.nextToken();
					Password = st.nextToken();
					
////////////////////////////////////////////comparing information//////////////////////////////////////////////////////////////////				
					
					try {
						con = DriverManager.getConnection(url, user, passwd);
						stmt = con.createStatement();
			                         	
						ResultSet result = stmt.executeQuery("SELECT * FROM login_data"); 
				
						
						int count = 0;
						while (result.next()) { 
							
							String resultStrID = result.getString("ID");
							String resultStrPass = result.getString("Password");
							
							if((ID.equals(resultStrID)) && (Password.equals(resultStrPass))) {
								
								ResultSet result2 = stmt.executeQuery("SELECT * FROM userinfo_data WHERE ID = '"+ID+"'"); 
								
								while(result2.next()) {
									
									resultStrName = result2.getString("User_name").replaceAll(",", "");
									 
									resultStrPhoneNumber = result2.getString("User_phoneNumber");
									
								}
								clients.put(this.resultStrName, this);
								count++;
								writer.println("Client_Name"+this.resultStrName);
								writer.println("true");
								
								
								
								
								break;
							}
						
							
							
						}
						if(count == 0) {
							
							writer.println("false");
						}
						
						stmt.close();
						con.close();	

					} catch(SQLException ex) {
						System.err.println("Select error: " + ex.getMessage());
					}
		
					
					
					
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					
					
				}
				
				else if(msg.startsWith("UserInformation")) {
/////////////////////////////////////////////////////////////getting user`s data///////////////////////////////////////////////////	
					
					st = new StringTokenizer(msg,"/");
					NULL = st.nextToken();
					ID = st.nextToken();
					Password = st.nextToken();
					Name = st.nextToken();
					PhoneNumber = st.nextToken();
					
////////////////////////////////////insert data int db /////////////////////////////////////////////////////////////////////////////
					
					
					recordStringUserInfo = insertStringUserInfo + "('"+ID+"' , '"+Name+"' , '"+PhoneNumber+"')";
					recordStringUserLoginInfo = insertStringUserLoginInfo + "('"+ID+"' , '"+Password+"')";
					try {
						con = DriverManager.getConnection(url, user, passwd);
						stmt = con.createStatement();
						stmt.execute(recordStringUserInfo);
						stmt.execute(recordStringUserLoginInfo);
						writer.println("valid");
					} catch (SQLException e) {
						System.err.println("SQLException: " + e.getMessage());
						writer.println("invalid");
						
					}
					finally {
		        		try {
		        			
						if (stmt != null) stmt.close();
		                		if (con != null) con.close();
		               		} catch (Exception e) {}
		      	 	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////					
					
					
				}
				else if(msg.startsWith("perfil_Information_Solicited")) {
					
						writer.println(resultStrName+"/"+resultStrPhoneNumber);

					
				}
				else if(msg.startsWith("::msg")){
					System.out.println("ate aqui ok ");
					
					String addresseeName = msg.substring(5,msg.length());
					System.out.println(addresseeName);
					ClientManager addressee = clients.get(addresseeName);
					
					System.out.println("ok2");
					if(addressee == null) {
						writer.println(">>>client not found");
						
					}
					else {
						String Sentmeessage;
						writer.println(">>>sending a message for "+addressee.getResultStrName());
						Sentmeessage = reader.readLine();
						addressee.getWriter().println(">>>"+this.resultStrName+" said: " +Sentmeessage);
						writer.println(">>>You said: "+Sentmeessage);
						
					}
				}
				else if(msg.equals("::ListClients")) {
					StringBuffer str = new StringBuffer();
					for(String c : clients.keySet()) {
						str.append(c);
						str.append(",");
						
					}
					str.delete(str.length()-1, str.length());
					writer.println(str.toString());
				}
				else {
					writer.println(">>> "+ msg);
				
			
				}
				
			}
		
		 } catch (IOException e) {
			 
			System.out.println("the client close the connection");
			clients.remove(this.resultStrName);
			
			
		}
		
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public BufferedReader getReader() {
		return reader;
	}
	
	public String getResultStrName() {
		return resultStrName;
	}

}
