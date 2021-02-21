import java.net.*;


import java.io.*;

public class HTTPEcho 
{
    static int BUFFERSIZE = 1024;

    public static void main(String[] args) throws Exception 
    {
        int port = Integer.parseInt(args[0]);                           //Converting string to int
        ServerSocket welcomeSocket = new ServerSocket(port);            //Creating a socket to communicate with the server

       try
       {
            while(true)
            {
                Socket connectionSocket = welcomeSocket.accept();           //Creating a new socket for communication with the client
                InputStream input = connectionSocket.getInputStream();      //Input from client
                
                byte[] fromClientBuffer = new byte[BUFFERSIZE];
                int fromClientLength = 0;//input.read(fromClientBuffer);                           
                connectionSocket.setSoTimeout(2000);
                
                StringBuilder sb = new StringBuilder();
                
                String checkMsg = "HTTP/1.1 200 OK \r\n\r\n";
                //sb.append("HTTP/1.1 200 OK \r\n\r\n");
                //sb.append("Hello \r\n\r\n");
                int i = 0;
                while(checkMsg != "\n")
                {
                    System.out.println("start");
                    sb.append(checkMsg + "\r\n");
                    fromClientLength = input.read(fromClientBuffer);        //Reads from client and stores length
                    checkMsg = decode(fromClientBuffer, fromClientLength);
                    System.out.println(checkMsg);
                }
                
                OutputStream output = connectionSocket.getOutputStream();   //Output from server
                byte[] toClientBuffer = encode(checkMsg);              //Store message from server
                output.write(toClientBuffer);                               //Output from server to client
                
                connectionSocket.close();           
            }
       }
        catch (Exception e) 
        {
            System.out.println("Exception");
        }
        
    }
    
    private static byte[] encode(String string) throws UnsupportedEncodingException
    {
        byte[] bytes = string.getBytes("UTF-8");                //Encodes the string to UTF-8 bytearray
        return bytes;
    }

    private static String decode(byte[] bytes, int length) throws UnsupportedEncodingException
    {
        String string = new String(bytes, 0, length, "UTF-8");  //Creating a string that takes bytearray and decodes it using UTF-8 from 0 array to lenght of array
        return string;
    }

}



