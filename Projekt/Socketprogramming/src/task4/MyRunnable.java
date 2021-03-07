import tcpclient.TCPClient;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MyRunnable implements Runnable 
{
    static int BUFFERSIZE = 1024;

    public Socket connectionSocket;
    public MyRunnable(Socket connectionSocket) 
    {
        this.connectionSocket = connectionSocket;
    }
 
    public void run() 
    {
        try
        {
            String TCPClientMsg = new String();
            InputStream input = connectionSocket.getInputStream();      //Input from client
            OutputStream output = connectionSocket.getOutputStream();   //Output from server
            
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
            int fromClientLength = 0;                           
            connectionSocket.setSoTimeout(9000);
            
            StringBuilder sb = new StringBuilder();
            String checkMsg = new String();

            while(!checkMsg.contains(""));
            {
                fromClientLength = input.read(fromClientBuffer);        //Reads from client and stores length
                checkMsg = decode(fromClientBuffer, fromClientLength);
                sb.append(checkMsg + "\r\n");
            }
            
            String serverMessage = sb.toString();
            String[] serverURL = serverMessage.split("\\r\\n");
            String firstlineURL = serverURL[0];
            String[] serverParameters = firstlineURL.split("[?=& ]", 12);
            String hostname = null;
            String toServer = null;
            String http400 = "HTTP/1.1 400 Bad Request";
            String http404 = "HTTP/1.1 404 Not Found";
            String http200 = "HTTP/1.1 200 OK \r\n\r\n";
            int hostport = 0;
            
            if((serverParameters[0].equals("GET")) && (serverParameters[1].equals("/ask")) && (serverParameters[2].equals("hostname")) && (serverParameters[4].equals("port")))
            {
                /*  Used for writing out serverParameter array during testing
                for (int i = 0; i < serverParameters.length; i++) 
                {
                    System.out.println(serverParameters[i]);
                }
                */

                hostname = serverParameters[3];
                hostport = Integer.parseInt(serverParameters[5]);

                if (serverParameters[6].equals("string"))
                {
                    toServer = serverParameters[7];
                }
                else if (!(serverParameters[serverParameters.length-1].equals("HTTP/1.1")))
                {
                    TCPClientMsg = http400;
                    byte[] toClientBuffer = encode(TCPClientMsg);           //Store message from server
                    output.write(toClientBuffer);                           //Output from server to client
                }
            
                try 
                {
                    if((hostname != null) || (hostport != 0))
                    {
                        TCPClientMsg = http200 + TCPClient.askServer(hostname,hostport,toServer);
                    }
                    
                } 
                catch (IOException e) 
                {
                    TCPClientMsg = http404;
                    byte[] toClientBuffer = encode(TCPClientMsg);           //Store message from server
                    output.write(toClientBuffer);                           //Output from server to client
                }
            }
            else
            {
                TCPClientMsg = http400;
                byte[] toClientBuffer = encode(TCPClientMsg);               //Store message from server
                output.write(toClientBuffer);                               //Output from server to client
            }
                
            byte[] toClientBuffer = encode(TCPClientMsg);                   //Store message from server
            output.write(toClientBuffer);                                   //Output from server to client
            
            connectionSocket.close();                        
        
        }
        catch (IOException socketTimeoutException) 
        {
        }
    }

    private static byte[] encode(String string)
    {
        return string.getBytes(StandardCharsets.UTF_8);                //Encodes the string to UTF-8 bytearray
    }

    private static String decode(byte[] bytes, int length)
    {
        return new String(bytes, 0, length, StandardCharsets.UTF_8);  //Creating a string that takes bytearray and decodes it using UTF-8 from 0 array to lenght of array
    }
}