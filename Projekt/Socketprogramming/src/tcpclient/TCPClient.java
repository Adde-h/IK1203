package tcpclient;

import java.net.*;
import java.io.*;

public class TCPClient 
{
    private static int BUFFERSIZE = 1024;
    
    public static String askServer(String hostname, int port, String ToServer) throws  IOException 
    {
        if (ToServer == null)
        {
            return askServer(hostname, port);
        }

        Socket clientSocket = new Socket(hostname, port);
        InputStream input = clientSocket.getInputStream();      //Input from server
        OutputStream output = clientSocket.getOutputStream();   //Output to server

        byte[] toServerBuffer = encode(ToServer);
        output.write(toServerBuffer);
        clientSocket.setSoTimeout(12000);

        byte[] fromServerBuffer = new byte[BUFFERSIZE];
        int fromServerLength = 0;

        StringBuilder fromServer = new StringBuilder();
        while(fromServerLength != -1)
        {
            fromServerLength = input.read(fromServerBuffer);
            if(fromServerLength != -1)
            {
                fromServer.append(decode(fromServerBuffer, fromServerLength));
            }
        }
        
        clientSocket.close();
        String serverMessage = fromServer.toString();

        return serverMessage;
    }

    public static String askServer(String hostname, int port) throws  IOException 
    {
        Socket clientSocket = new Socket(hostname, port);
        InputStream input = clientSocket.getInputStream();      //Input from server

        clientSocket.setSoTimeout(12000);
        byte[] fromServerBuffer = new byte[BUFFERSIZE];

        int fromServerLength = 0;
      
        StringBuilder fromServer = new StringBuilder();
        while(fromServerLength != -1)
        {
            fromServerLength = input.read(fromServerBuffer);
            if(fromServerLength != -1)
            {
                fromServer.append(decode(fromServerBuffer, fromServerLength));
            }

        }

        clientSocket.close();
        String serverMessage = fromServer.toString();

        return serverMessage;

    }

    private static String decode(byte[] bytes, int length) throws UnsupportedEncodingException
    {
        String string = new String(bytes, 0, length, "UTF-8");
        return string;
    }

    private static byte[] encode(String string) throws UnsupportedEncodingException
    {
        byte[] bytes = string.getBytes("UTF-8");
        return bytes;
    }

}

