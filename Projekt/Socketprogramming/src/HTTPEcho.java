import java.net.*;


import java.io.*;

public class HTTPEcho 
{
    static int BUFFERSIZE = 1024;

    public static void main(String[] args) throws IOException 
    {
        int port = Integer.parseInt(args[0]);                           //Converting string to int
        ServerSocket welcomeSocket = new ServerSocket(port);            //Creating a socket to communicate with the server

        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();           //Creating a new socket for communication with the client
            InputStream input = connectionSocket.getInputStream();      //Input from client
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
                                             
            connectionSocket.setSoTimeout(2000);

            StringBuilder sb = new StringBuilder();

            sb.append("HTTP/1.1 200 OK \r\n");
            int fromClientLength = 0;  

           while(fromClientLength != -1)
            {
                sb.append(decode(fromClientBuffer, fromClientLength) + "\r\n");
                fromClientLength = input.read(fromClientBuffer);        //Reads from client and stores length

            }

            OutputStream output = connectionSocket.getOutputStream();   //Output from server
            byte[] toClientBuffer = encode(sb.toString());              //Store message from server
            output.write(toClientBuffer);                               //Output from server to client
            
            connectionSocket.close();           
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



