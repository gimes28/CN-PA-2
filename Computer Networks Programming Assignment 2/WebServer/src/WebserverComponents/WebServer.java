package WebserverComponents;

import java.io.* ;
import java.net.* ;
import java.util.* ;

public class WebServer {

    private int port = 6789;
    private List<Socket> ClientSockets = new ArrayList<Socket>();
    private Dictionary<String, Socket> Users = new Hashtable<String, Socket>();

    WebServer() throws Exception {
        System.out.println("Attempting to start the server\n");
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            // Listen for a TCP connection request
            System.out.println("Waiting for a connection...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection found!");

            // Construct an object to process the HTTP request message.
            ServerResponse request = new ServerResponse(clientSocket);
            // Create a new thread to process the request.
            Thread thread = new Thread(request);
            // Start the thread.
            thread.start();
        }
    }
    public static void main(String argv[])
    {
        try {
            WebServer server = new WebServer();
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    final class ServerResponse implements Runnable {
        Socket socket;

        public ServerResponse(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                AcceptConnection();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private void AcceptConnection() throws Exception {
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os.writeBytes("Welcome to the message board! Please enter a username\n"); // ask socket for a username
            String userName = null;
            userName = br.readLine();
            if(userName != null) {
                System.out.println("User name is: " + userName);
                Users.put(userName, socket); // assigns username/socket pair to dictionary for access
            }
            String echoTest;
            while(true)
            {
                System.out.println("Now listening for client");
                echoTest = br.readLine();
                System.out.println(echoTest);
                os.writeBytes(echoTest + "\n");
            }

        }
    }
}

