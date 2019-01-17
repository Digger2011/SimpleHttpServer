package HttpServer.Classes;

import DataHandler.ExceptionCollector;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by phil- on 02.12.2017.
 */
public class Visualizer
{
    public static void main(String[] args) throws IOException
    {
        httpRequest("de.wikipedia.org","/wiki/Portable_Network_Graphics#/media/File:PNG_transparency_demonstration_1.png",80);

    }

    private static void httpRequest(String url,String path,int port)throws IOException
    {
        Socket socket = new Socket(url,port);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write("GET "+path+" http/1.1\n");
        bufferedWriter.write("Host: "+url+"\n\n");
        bufferedWriter.flush();
        String s ;
        s = bufferedReader.readLine();
        while (!s.equals(""))
        {
            System.out.println(s);
            s =  bufferedReader.readLine();
            if(s == null)
                break;
        }
        socket.close();
    }

    private static void http() throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(85);
        while (true)
        {
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = bufferedReader.readLine();
            String first = s;
            while (!s.equals(""))
            {
                System.out.println(s);
                s =  bufferedReader.readLine();
                if(s == null)
                    break;
            }
            if(first.contains("POST"))
            {
                System.out.println(s);
                s = bufferedReader.readLine();
                while (!s.equals(""))
                {
                    System.out.println(s);
                    s =  bufferedReader.readLine();
                    if(s == null)
                        break;
                }
            }
            socket.close();
        }
    }

    private static void https() throws IOException
    {
        SSLServerSocketFactory sslServerSocketFactory = sslServerSocketFactory("E:\\Java Projects\\PasswordManagerClient\\src\\resources\\diggerkeystore.jks");
        ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(443);

        while (true)
        {
            Socket socket = serverSocket.accept();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s = bufferedReader.readLine();
            String first = s;
            while (!s.equals(""))
            {
                System.out.println(s);
                s =  bufferedReader.readLine();
                if(s == null)
                    break;
            }
            if(first.contains("POST"))
            {
                System.out.println(s);
                s = bufferedReader.readLine();
                while (!s.equals(""))
                {
                    System.out.println(s);
                    s =  bufferedReader.readLine();
                    if(s == null)
                        break;
                }
            }
            socket.close();
        }
    }


    private static SSLServerSocketFactory sslServerSocketFactory(String keystorepath)
    {
        try
        {
            KeyStore ks = KeyStore.getInstance("JKS");
            char[] password = "Worldwide1".toCharArray();

            InputStream inputStream = new FileInputStream(keystorepath);
            ks.load(inputStream, password);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(ks, password);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ks);

            SSLContext cs = SSLContext.getInstance("TLS");
            cs.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            return cs.getServerSocketFactory();
        }
        catch (KeyStoreException |NoSuchAlgorithmException |CertificateException |UnrecoverableKeyException |KeyManagementException|IOException e)
        {
            ExceptionCollector.logException(e,1);
            return null;
        }
    }
}
