package HttpServer.Classes;

import DataHandler.ExceptionCollector;
import HttpServer.Barebone.StandardAcceptClient;
import HttpServer.Barebone.StandardMethodHandler;
import HttpServer.Barebone.StandardStatusCodes;
import HttpServer.Interfaces.ClientHandler;
import HttpServer.Interfaces.MethodHandler;
import HttpServer.Interfaces.StatusCodes;
import JSON.Configable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by phil- on 27.05.2017. Implements SSL Connection
 */
public class HttpsServer extends HttpServer
{
    public HttpsServer(HttpServerConfig httpServerConfig)
    {
        SSLServerSocketFactory sslServerSocketFactory = sslServerSocketFactory(httpServerConfig.getSslkeystore());
        if(sslServerSocketFactory != null)
            try
            {
                overrideSocket(sslServerSocketFactory.createServerSocket(httpServerConfig.getSslport()));

                overrideConfig(httpServerConfig);

                overrideAcceptClient(new StandardAcceptClient());

                StatusCodes statusCodes = new StandardStatusCodes();
                overrideStatuscodes(statusCodes);

                overrideMethodHandler(new StandardMethodHandler(httpServerConfig,statusCodes));
            }
            catch (IOException e)
            {
                ExceptionCollector.logException(e,2);
                System.exit(2);
            }
        else
        {
            System.exit(1);
        }
    }

    public HttpsServer(HttpServerConfig httpServerConfig, MethodHandler methodHandler, ClientHandler acceptClientClass, StatusCodes statusCodes, Configable configable)
    {
        SSLServerSocketFactory sslServerSocketFactory = sslServerSocketFactory(httpServerConfig.getSslkeystore());
        if(sslServerSocketFactory != null)
        {
            try
            {
                overrideSocket(sslServerSocketFactory.createServerSocket(httpServerConfig.getSslport()));
                overrideConfig(httpServerConfig);
                overrideStatuscodes(statusCodes);
                overrideMethodHandler(methodHandler);
                overrideAcceptClient(acceptClientClass);
                overrideConfigable(configable);
            }
            catch (IOException e)
            {
                ExceptionCollector.logException(e,2);
                System.exit(2);
            }
        }
        else
        {
            System.exit(1);
        }
    }

    private SSLServerSocketFactory sslServerSocketFactory(String keystorepath)
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
        catch (KeyStoreException|NoSuchAlgorithmException|CertificateException|UnrecoverableKeyException|KeyManagementException|IOException e)
        {
            ExceptionCollector.logException(e,1);
            return null;
        }
    }
}
