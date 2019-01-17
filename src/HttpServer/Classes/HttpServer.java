package HttpServer.Classes;

import HttpServer.Barebone.StandardAcceptClient;
import HttpServer.Barebone.StandardMethodHandler;
import HttpServer.Barebone.StandardStatusCodes;
import HttpServer.Interfaces.ClientHandler;
import HttpServer.Interfaces.MethodHandler;
import HttpServer.Interfaces.StatusCodes;
import DataHandler.ExceptionCollector;
import JSON.Configable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by Philipp Susen on 11.04.2017. This is the Main Class for the Project.
 * It initializes the sockets and provides a method for accepting a request in the constructing class.
 */
public class HttpServer
{
    public static final String ApplicationName = "Simple Http Server by Digger";

    private ServerSocket serverSocket;
    private MethodHandler methodHandler;
    private ClientHandler acceptClientClass;
    private HttpServerConfig httpServerConfig;
    private StatusCodes statusCodes;
    private Configable configable;

    /**
     * Constructs an uninitialized Server.
     */
    public HttpServer()
    {
        serverSocket = null;
        httpServerConfig = null;
        methodHandler = null;
        acceptClientClass = null;
        configable = null;
    }

    /**
     * Constructs and initializes an fully customized Server.
     * @param httpServerConfig The config for the Server.
     * @param methodHandler The custom methodHandler.
     * @param acceptClientClass The custom acceptClientClass.
     * @param statusCodes The custom Statuscodes.
     * @param configable A custom Configable that is used in Subsequent Classes
     */
    public HttpServer(HttpServerConfig httpServerConfig, MethodHandler methodHandler, ClientHandler acceptClientClass,StatusCodes statusCodes,Configable configable)
    {
        try
        {
            serverSocket = new ServerSocket(httpServerConfig.getPort());
            serverSocket.setSoTimeout(5000);
        }
        catch (IOException e)
        {
            ExceptionCollector.logException(e,2);
            e.printStackTrace();
            System.exit(2);
        }
        this.methodHandler = methodHandler;
        this.httpServerConfig = httpServerConfig;
        this.acceptClientClass = acceptClientClass;
        this.statusCodes = statusCodes;
        this.configable = configable;
    }

    /**
     * Constructs and initializes a Server
     * @param httpServerConfig The config for the Server.
     */
    public HttpServer(HttpServerConfig httpServerConfig)
    {
        try
        {
            serverSocket = new ServerSocket(httpServerConfig.getPort());
            serverSocket.setSoTimeout(2000);
        }
        catch (IOException e)
        {
            ExceptionCollector.logException(e,2);
            System.exit(2);
        }
        this.httpServerConfig = httpServerConfig;
        statusCodes = new StandardStatusCodes();
        methodHandler = new StandardMethodHandler(httpServerConfig,statusCodes);
        acceptClientClass = new StandardAcceptClient();
        configable = null;
    }

    /**
     * Sets the acceptClientClass to the parameter.
     */
    public void overrideAcceptClient(ClientHandler acceptClientClass)
    {
        this.acceptClientClass = acceptClientClass;
    }

    /**
     * Sets the methodHandler to the parameter.
     */
    public void overrideMethodHandler(MethodHandler methodHandler)
    {
        this.methodHandler = methodHandler;
    }

    /**
     * Sets the serverSocket to the parameter.
     */
    public void overrideSocket(ServerSocket serverSocket) throws SocketException
    {
        this.serverSocket = serverSocket;
        this.serverSocket.setSoTimeout(5000);
    }

    /**
     * Sets the httpServerConfig to the parameter.
     */
    public void overrideConfig(HttpServerConfig httpServerConfig)
    {
        this.httpServerConfig = httpServerConfig;
    }

    /**
     * Sets the statusCodes to the parameter.
     */
    public void overrideStatuscodes(StatusCodes statusCodes)
    {
        this.statusCodes = statusCodes;
    }

    /**
     * Sets the configable to the parameter.
     */
    public void overrideConfigable(Configable configable)
    {
        this.configable = configable;
    }

    /**
     * Accepts a request and handles it.
     */
    public void acceptClient()
    {
        if(serverSocket != null && methodHandler != null && httpServerConfig != null && statusCodes != null && acceptClientClass != null)
        {
            try
            {
                Socket clientsocket = serverSocket.accept();
                acceptClientClass.handleClient(clientsocket, methodHandler, httpServerConfig, statusCodes,configable);
            }
            catch (SocketTimeoutException e)
            {
                //No Action needed
            }
            catch (IOException e)
            {
                ExceptionCollector.logException(e, -1);
            }
        }
        else  //Constructs error Message
        {
            String msg = "";
            if (serverSocket == null)
                msg += "Serversocket,";
            if(methodHandler == null)
                msg += "MethodHandler,";
            if(httpServerConfig == null)
                msg += "HttpServerConfig,";
            if(statusCodes == null)
                msg += "Statuscodes,";
            if(acceptClientClass == null)
                msg += "AcceptClientClass,";
            throw new NullPointerException("The arguments " + msg.substring(0,msg.length()-1) + " of Http-Server is Null");
        }
    }


}