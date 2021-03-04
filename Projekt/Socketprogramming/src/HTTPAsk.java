import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk 
{
    static int BUFFERSIZE = 1024;

    public static void main(String[] args) throws IOException 
    {
        int port = Integer.parseInt(args[0]);                           //Converting string to int
        ServerSocket welcomeSocket = new ServerSocket(port);            //Creating a socket to communicate with the server
        try
        {
            while(true)
            {
                String TCPClientMsg = new String();
                Socket connectionSocket = welcomeSocket.accept();           //Creating a new socket for communication with the client
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
                String[] serverParameters = serverMessage.split("[?=& ]", 12);
                String hostname = null;
                String toServer = null;
                
                int hostport = 0;
                if((serverParameters[1].equals("/ask")) && (serverParameters[2].equals("hostname")) && (serverParameters[4].equals("port")))
                {
                    hostname = serverParameters[3];
                    hostport = Integer.parseInt(serverParameters[5]);

                    if (serverParameters[6].equals("string"))
                    {
                        toServer = serverParameters[6];
                    }
                    else if (!(serverParameters[6].equals("HTTP/1.1")))
                    {
                        TCPClientMsg = "HTTP/1.1 400 Bad Request";
                        byte[] toClientBuffer = encode(TCPClientMsg);               //Store message from server
                        output.write(toClientBuffer);                               //Output from server to client
                    }

                
                    try 
                    {
                        if((hostname != null) || (hostport != 0))
                        {
                            TCPClientMsg = "HTTP/1.1 200 OK \r\n\r\n" + TCPClient.askServer(hostname,hostport,toServer);
                        }
                        
                    } 
                    catch (IOException e) 
                    {
                        TCPClientMsg = "HTTP/1.1 404 Not Found";
                        byte[] toClientBuffer = encode(TCPClientMsg);               //Store message from server
                        output.write(toClientBuffer);                              //Output from server to client
                    }
                }
                else
                {
                    TCPClientMsg = "HTTP/1.1 400 Bad Request";
                    byte[] toClientBuffer = encode(TCPClientMsg);               //Store message from server
                    output.write(toClientBuffer);                               //Output from server to client
                }
                   
                byte[] toClientBuffer = encode(TCPClientMsg);               //Store message from server
                output.write(toClientBuffer);                               //Output from server to client
                
                connectionSocket.close();                        
            }
        }
        catch (SocketTimeoutException socketTimeoutException) 
        {
            System.out.println("Socket Timeout Exception");
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

