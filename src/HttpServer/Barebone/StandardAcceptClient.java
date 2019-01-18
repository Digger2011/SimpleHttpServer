package HttpServer.Barebone;

import DataHandler.ExceptionCollector;
import HttpServer.Classes.HttpServerConfig;
import HttpServer.Classes.HttpRequest;
import HttpServer.Classes.HttpResponse;
import HttpServer.Interfaces.ClientHandler;
import HttpServer.Interfaces.MethodHandler;
import HttpServer.Interfaces.StatusCodes;
import JSON.Configable;

import java.io.*;
import java.net.Socket;

/**
 * Created by phil- on 04.06.2017.
 */
public class StandardAcceptClient implements ClientHandler
{

    @Override
    public void handleClient(Socket clientsocket, MethodHandler methodHandler, HttpServerConfig httpServerConfig, StatusCodes statusCodes,Configable configable)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(clientsocket.getOutputStream());
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bufferedOutputStream);

                    HttpRequest httpRequest = new HttpRequest(bufferedReader,clientsocket);

                    handleHttpRequest(httpRequest, bufferedOutputStream);
                    bufferedReader.close();
                    outputStreamWriter.close();
                    bufferedOutputStream.close();
                    clientsocket.close();
                } catch (IOException e)
                {
                    ExceptionCollector.logException(e, -1);
                }
            }

            private void handleHttpRequest(HttpRequest httpRequest, BufferedOutputStream bufferedOutputStream)
            {
                HttpResponse httpResponse = new HttpResponse();
                switch (httpRequest.getMethod())
                {
                    case "GET":
                        methodHandler.getRequest(httpResponse, httpRequest,configable);
                        break;
                    case "HEAD":
                        methodHandler.headerRequest(httpResponse, httpRequest,configable);
                        break;
                    case "POST":
                        methodHandler.postRequest(httpResponse, httpRequest,configable);
                        break;
                    case "OPTIONS":
                        methodHandler.optionsRequest(httpResponse, httpRequest,configable);
                        break;
                    default:
                        statusCodes.makeStandardResponse(403,httpResponse);
                }

                boolean allowencoding = false;
                if (httpRequest.getHeader("Accept_Encoding") != null)
                    allowencoding = httpRequest.getHeader("Accept_Encoding").contains("gzip");
                try
                {
                    httpResponse.SendResponse(bufferedOutputStream, allowencoding, httpServerConfig);
                } catch (IOException e)
                {
                    ExceptionCollector.logException(e, -1);
                }
            }
        }).start();
    }
}
