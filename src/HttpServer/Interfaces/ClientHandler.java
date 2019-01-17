package HttpServer.Interfaces;

import HttpServer.Classes.HttpServerConfig;
import JSON.Configable;

import java.net.Socket;

/**
 * Created by phil- on 07.10.2017.
 */
public interface ClientHandler
{
    void handleClient(Socket clientsocket, MethodHandler methodHandler, HttpServerConfig httpServerConfig, StatusCodes statusCodes, Configable configable);
}
