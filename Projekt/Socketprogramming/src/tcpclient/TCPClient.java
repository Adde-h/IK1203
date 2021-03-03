package tcpclient;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class TCPClient 
{
    private static int BUFFERSIZE = 1024;
    
    public static String askServer(String hostname, int port, String ToServer) throws  IOException 
    {
        if (ToServer == null)                                   //If there is not a message to send to server
        {
            return askServer(hostname, port);                   //Run second function
        }

        Socket clientSocket = new Socket(hostname, port);       //Creating a socket
        clientSocket.setSoTimeout(2000);                        //Timeout 2 sec

        InputStream input = clientSocket.getInputStream();      //Input from server
        OutputStream output = clientSocket.getOutputStream();   //Output to server

        byte[] toServerBuffer = encode(ToServer + "\r\n");      //Encoding message to UTF-8 and saving in bytearray
        byte[] fromServerBuffer = new byte[BUFFERSIZE];         //Creating bytearray with BUFFERSIZE size
        output.write(toServerBuffer);                           //Sending the TCP Message to the server

        StringBuilder sb = new StringBuilder();
        int fromServerLength = 0;
        try 
        {
            while(fromServerLength != -1)
            {
                sb.append(decode(fromServerBuffer,fromServerLength));
                fromServerLength = input.read(fromServerBuffer);    //Reading the TCP Message from the server and determening the size
            }
        } 
        catch (SocketTimeoutException setSoTimeout) 
        {
        }

        clientSocket.close();                                   //Closing the TCP Connection
        return sb.toString();    

    }

    public static String askServer(String hostname, int port) throws  IOException 
    { 
        Socket clientSocket = new Socket(hostname, port);       //Creating a socket
        clientSocket.setSoTimeout(2000);                        //Timeout 2 sec

        InputStream input = clientSocket.getInputStream();      //Input from server

        byte[] fromServerBuffer = new byte[BUFFERSIZE];         //Creating bytearray with BUFFERSIZE size
        StringBuilder sb = new StringBuilder();
        int fromServerLength = 0;

        try 
        {
            while(fromServerLength != -1 && fromServerLength < 256)
            {
                sb.append(decode(fromServerBuffer,fromServerLength));
                fromServerLength = input.read(fromServerBuffer);    //Reading the TCP Message from the server and determening the size

            }
        }
        catch (SocketTimeoutException setSoTimeout) 
        {
            System.out.println("Socket Timeout Exception");
        }

        clientSocket.close();                                   //Closing the TCP Connection
        return sb.toString();    

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

