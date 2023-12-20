
import java.io.IOException;
import java.net.*;

public class Server {

	public static void main(String[] args) {
		ServerSocket server = null;
		
		try {
			server = new ServerSocket(7000);
			System.out.println("servidor iniciado");
			while(true) {
				Socket client = server.accept();
				new ClientManager(client);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(server != null) {
			server.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
