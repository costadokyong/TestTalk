import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.*;



//////////////////////////////////////////////////////////LOGIN CLASS///////////////////////////////////////////////////////////////

public class Login extends JFrame {
	
	Socket client = null;
	BufferedReader reader = null;
	PrintWriter writer = null;
	String serverAns;
	
	JLabel logo = new JLabel("Test Talk");
	JTextField ID = new JTextField();
	JLabel text1 = new JLabel("Type Id");
	JLabel text2 = new JLabel("Type Password");
	
	JTextField Password = new JTextField();
	JPanel mid = new JPanel();
	JPanel up = new JPanel();
	
	JButton login = new JButton("Login");
	JButton Join = new JButton("Sing Up");
	
	//chat display
	JTextArea display = new JTextArea();
	
	String ClientName;
	
	Login(){
		try {
			client = new Socket("127.0.0.1",7000);
			writer = new PrintWriter(client.getOutputStream(), true);
			reader = new BufferedReader (new InputStreamReader( client.getInputStream()));
			
			
		} catch (UnknownHostException e) {
			System.out.println("Servidor nao existe");
			
		} catch (IOException e) {
			System.out.println("Servidor nao disponivel");
		}
		
		new Thread() {
			public void run() {
				try {
					
					while(true) {
						serverAns = reader.readLine();
						
						
						if(serverAns.startsWith(">>>")) {
							display.append(serverAns.substring(3,serverAns.length()) +"\n");
						}
						
						else if(serverAns.startsWith("Client_Name")){
							ClientName = serverAns;
							display.append("Welcome "+serverAns.substring(11,serverAns.length()) +"\n");
							
							
						}
						
					}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
			
		}.start();
		
		
		
		
		
		LoginAction Al = new LoginAction();
		setTitle("Test Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Container
		Container c = getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.LEFT, 65, 60));
		//c.setBackground(Color.GREEN);
		
		// First Panel
		up.setPreferredSize(new Dimension(150,50));
		//up.setBackground(Color.GREEN);
		c.add(up);
		Font f = new Font("Verdana",Font.BOLD,30);
		logo.setFont(f);
		up.add(logo);
		
		// Second Panel 
		mid.setPreferredSize(new Dimension(150,160));
		mid.setLayout(new GridLayout(6,1));
		c.add(mid);
		
		mid.add(text1);
		ID.setPreferredSize(new Dimension(75,100));
		mid.add(ID);
		mid.add(text2);
		mid.add(Password);
		mid.add(login);
		mid.add(Join);
		//mid.setBackground(Color.GREEN);
		//Actions
		Join.addActionListener(Al);
		login.addActionListener(Al);
	
		
		
		setLocationRelativeTo(null);
		setSize(300,500);
		setVisible(true);
	}
	
	
	
	
	
	public class LoginAction implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()== login) {
				String UserLoginInformation;
				if((ID.getText().equals("")) || (Password.getText().equals("")) ) {
					ID.setText("Required field");
					Password.setText("Required field");
				}		
				else {
					UserLoginInformation = "UserLoginInformation/"+ID.getText()+"/"+Password.getText();
					writer.println(UserLoginInformation);
					
					
					try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
							//reader = new BufferedReader (new InputStreamReader( client.getInputStream()));
							
							//serverAns = reader.readLine();
							
							if(serverAns.equals("false")) {
								ID.setText("ID Incorrect");
								Password.setText("Password Incorrect");
								
							}
							
							
							else {
								
							
								setVisible(false);
								new ChatInterface();
								
							
							}
						
				}
				
				
			}
			
			else if(e.getSource()==Join) {
				
				new Joins();
				
			}
			
			
		}
		
	}

///////////////////////////////////////////////////////////// JOINS CLASS///////////////////////////////////////////////////////////
	
	public class Joins extends JFrame {
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		
		JLabel logo = new JLabel("Test Talk");
		JLabel l1 = new JLabel("Create a ID");
		JLabel l2 = new JLabel("Create a Password");
		JLabel l3 = new JLabel("Type your Name");
		JLabel l4 = new JLabel("Type your Phone number");
		
		
		JTextField t1 = new JTextField();
		JTextField t2 = new JTextField();
		JTextField t3 = new JTextField();
		JTextField t4 = new JTextField();
		
		JButton ca = new JButton("Confirm");
		JButton cancel = new JButton("Cancel");
		
		Joins(){
			setTitle("Make a new Account");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			JoinAction ja = new JoinAction();
			
			Container c = getContentPane();
			c.setLayout(new FlowLayout(FlowLayout.LEFT, 65, 60));
			
			panel2.setPreferredSize(new Dimension(150,50));
			c.add(panel2);
			
			Font f = new Font("Verdana",Font.BOLD,30);
			logo.setFont(f);
			panel2.add(logo);
			
			panel.setPreferredSize(new Dimension(150,250));
			//panel.setBackground(Color.BLACK);
			panel.setLayout(new GridLayout(10,1));
			c.add(panel);
			
			panel.add(l1);
			panel.add(t1);
			panel.add(l2);
			panel.add(t2);
			panel.add(l3);
			panel.add(t3);
			panel.add(l4);
			panel.add(t4);
			panel.add(ca);
			panel.add(cancel);
			
			ca.addActionListener(ja);
			cancel.addActionListener(ja);
			
			setLocationRelativeTo(null);
			setSize(300,500);
			setVisible(true);
		}
		
		public class JoinAction implements ActionListener{

			
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()== ca) {
					if((t1.getText().equals("")) || (t2.getText().equals("")) || (t3.getText().equals("")) || (t4.getText().equals(""))) {
						t1.setText("Required field");
						t2.setText("Required field");
						t3.setText("Required field");
						t4.setText("Required field");
					}
					
					else {
						String UserInformation = "UserInformation/"+t1.getText()+"/"+t2.getText()+"/"+t3.getText()+"/"+t4.getText();
						writer.println(UserInformation);
						
						
							//reader = new BufferedReader (new InputStreamReader( client.getInputStream()));
							System.out.println("ta chegando ate aqui");
							//serverAns = reader.readLine();
							try {
								Thread.sleep(800);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							if(serverAns.equals("invalid")) {
								t1.setText("this ID alredy exist!!");
							}
							
							else {
								setVisible(false);
							}
							
							
						
					
						
					
						
					}
					
				}
				
				else if(e.getSource()== cancel) {
					setVisible(false);
					
					
				}
				
				
			}
			
		}

	}
	
	
/////////////////////////////////////////////////////////////////ChatInterfaceClass/////////////////////////////////////////////////
	public class ChatInterface extends JFrame {
		
		//Font f = new Font(15);
		JPanel basePanel = new JPanel( new BorderLayout());
		JPanel westPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		
		//labels
		JLabel Name = new JLabel();
		JLabel PhoneNumber = new JLabel();
		
		//Chat textArea
		JTextArea taEditor = new JTextArea();
		
		JButton send = new JButton("Send");
		
		
		CardLayout cl =new CardLayout();
		
		JButton msg = new JButton("Chat");
		JButton friends = new JButton("Friends");
		JButton Perfil = new JButton("Profile");
		JButton Logout = new JButton("Logout");
		
		
		
		JLabel friend1 = new JLabel("Friend 1");
		JLabel friend2 = new JLabel("Friend 2");
		JLabel friend3 = new JLabel("Friend 3");
		
		//User`s list
		ArrayList User ;
		DefaultListModel model = new DefaultListModel();
		JList userList = new JList();
		
		
		//userList.getModel();
		
		
		ChatInterface(){
		
			setTitle("Test Talk");
			
			ChatIterfaceAction cfa = new ChatIterfaceAction();
				
			
			westPanel.setBackground(Color.WHITE);
			centerPanel.setBackground(Color.LIGHT_GRAY);
			westPanel.setPreferredSize(new Dimension(50, 500));
			setContentPane(basePanel);
			basePanel.add(westPanel,BorderLayout.WEST);
			basePanel.add(centerPanel,BorderLayout.CENTER);
			westPanel.setLayout(new FlowLayout());
			
			
			
			//perfil panel
			centerPanel.setLayout(cl);
			JPanel perfilBasePanel = new JPanel();
			JPanel perfilPanel = new JPanel();
			
			perfilBasePanel.setLayout(new FlowLayout());
			perfilPanel.setLayout(new GridLayout(2,1));
			perfilPanel.setPreferredSize(new Dimension(200,200));
			centerPanel.add(perfilBasePanel,"perfilBasePanel");
			perfilBasePanel.add(perfilPanel);
			perfilPanel.add(Name);
			perfilPanel.add(PhoneNumber);
			
			
			//friend panel
			JPanel FriendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
			JLabel Friendson = new JLabel("Online Friends:");
			centerPanel.add(FriendPanel,"FriendPanel");
			userList.setBackground(Color.GRAY);
			userList.setModel(model);
			FriendPanel.add(Friendson);
			FriendPanel.add(userList);
			
			
			
			//Chat Panel
			JPanel ChatBasePanel = new JPanel();
			JPanel ChatPanel = new JPanel();
			BorderLayout ChatLayout = new BorderLayout();
			
			ChatLayout.setVgap(1);
			ChatBasePanel.setLayout(ChatLayout);
			ChatPanel.setLayout(new BorderLayout());
			ChatBasePanel.setBackground(Color.BLACK);
			
			display.setEditable(false);
			ChatBasePanel.add(new JScrollPane(display),BorderLayout.CENTER);
			ChatPanel.setPreferredSize(new Dimension(200,50));
		
			ChatBasePanel.add(ChatPanel,BorderLayout.SOUTH);
			taEditor.setBackground(Color.LIGHT_GRAY);
			send.setPreferredSize(new Dimension(80,50));
			ChatPanel.add(taEditor,BorderLayout.CENTER);
			ChatPanel.add(send,BorderLayout.EAST);
			
			centerPanel.add(ChatBasePanel,"ChatBasePanel");
			cl.show(centerPanel, "ChatBasePanel");
			
			//msg.setPreferredSize(new Dimension(50,55));
			//msg.setFont(f);
			westPanel.add(msg);
			westPanel.add(friends);
			westPanel.add(Perfil);
			westPanel.add(Logout);
			
			
			
			msg.addActionListener(cfa);
			Logout.addActionListener(cfa);
			Perfil.addActionListener(cfa);
			friends.addActionListener(cfa);
			send.addActionListener(cfa);
			userList.addMouseListener(cfa);
			
			
			setLocationRelativeTo(null);
			setSize(350,500);
			setVisible(true);
		}
		
		public class ChatIterfaceAction implements ActionListener, MouseListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==Logout) {
					setVisible(false);
					System.exit(0);
					new Login();
				}
				else if(e.getSource()==Perfil) {
					cl.show(centerPanel, "perfilBasePanel");
					writer.println("perfil_Information_Solicited");
					
					try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
						
						String perfilInformation = serverAns;
						StringTokenizer  st = new StringTokenizer(perfilInformation,"/");
						String profileName = st.nextToken();
						String ProfilePN = st.nextToken();
						
						
						Name.setText("Name: "+profileName);
						PhoneNumber.setText("Phone Number: "+ProfilePN);
						
					
					
				}
				
				else if(e.getSource() == friends) {
					
					cl.show(centerPanel, "FriendPanel");
					model = new DefaultListModel<>();
					userList.setModel(model);
					writer.println("::ListClients");
					try {
						Thread.sleep(800);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					String user = serverAns;
					
					User = new ArrayList<>();
					StringTokenizer  st = new StringTokenizer(user,",");
					int i = 0;
					String Token;
					for(i=0;st.hasMoreTokens();i++) {
						Token = st.nextToken();
						if(Token.equals(ClientName.substring(11,ClientName.length())) == false) {
							User.add(Token);
							
							i++;
						}
					}
					
					for(Object users: User) {
						model.addElement(users);
						}
					
					userList.setModel(model);
					User = null;
				}
				else if(e.getSource() == msg){
					cl.show(centerPanel, "ChatBasePanel");
					
					
				}
				else if(e.getSource() == send) {
					writer.println(taEditor.getText());
					taEditor.setText("");
					
				}
				
				
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				Object Users = userList.getSelectedValue();
				writer.println("::msg"+Users);
				
				try {
					Thread.sleep(800);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				cl.show(centerPanel, "ChatBasePanel");
				model = new DefaultListModel<>();
				userList.setModel(model);
					
				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}

	}

}
