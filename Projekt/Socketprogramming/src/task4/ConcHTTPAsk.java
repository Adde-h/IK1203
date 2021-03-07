import tcpclient.TCPClient;
import java.net.*;
import java.io.*;

public class ConcHTTPAsk 
{
    public static void main( String[] args) throws IOException
    {
        int port = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(port);
        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();
            //Runnable r = new MyRunnable(connectionSocket);
            new Thread(new MyRunnable(connectionSocket)).start();
        }
    }
}