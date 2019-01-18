package HttpServer.Barebone;


import HttpServer.Classes.HttpRequest;
import HttpServer.Classes.HttpResponse;
import HttpServer.Classes.HttpServer;
import HttpServer.Classes.HttpServerConfig;
import HttpServer.Interfaces.MethodHandler;
import HttpServer.Interfaces.StatusCodes;
import JSON.Configable;
import DataHandler.ExceptionCollector;

import java.io.*;

/**
 * Created by phil- on 11.04.2017.
 */
public class StandardMethodHandler implements MethodHandler
{
    private HttpServerConfig httpServerConfig;
    private StatusCodes statusCodes;
    private String[] allowedOrigins;

    public StandardMethodHandler(HttpServerConfig httpServerConfig,StatusCodes statusCodes) {
        this.httpServerConfig = httpServerConfig;
        this.statusCodes = statusCodes;
    }

    private boolean handleCORSb(HttpResponse httpResponse, HttpRequest httpRequest)
    {
        if (httpRequest.getHeader("Access_Control_Request_Method")!= null && allowedOrigins != null && httpRequest.getMethod().equals("OPTIONS"))
        {
            for(String s: allowedOrigins)
            {
                if (httpRequest.getHeader("Origin").equals(s))
                {
                    httpResponse.setResponseHeader("Access_Control_Allow_Origin",s);
                    break;
                }
            }
            httpResponse.setResponseHeader("Access_Control_Allow_Methods","GET ,POST");
            if(httpRequest.getHeader("Access_Control_Request_Headers") != null)
                httpResponse.setResponseHeader("Access_Control_Allow_Methods",httpRequest.getHeader("Access_Control_Request_Headers"));
            return true;
        }
        else if (httpRequest.getHeader("Origin")!=null && allowedOrigins != null)
        {
            for(String s: allowedOrigins)
            {
                if (httpRequest.getHeader("Origin").equals(s))
                {
                    httpResponse.setResponseHeader("Access_Control_Allow_Origin",s);
                    break;
                }
            }
            return false;
        }
        return false;
    }

    protected void handleCORS(HttpResponse httpResponse,HttpRequest httpRequest)
    {
        handleCORSb(httpResponse,httpRequest);
    }

    public void getRequest(HttpResponse httpResponse,HttpRequest httpRequest,Configable configable)
    {
        handleCORS(httpResponse, httpRequest);

        String path = httpRequest.getFilePath();
        String[] split = path.split("/");

        if (path.endsWith("/"))
            path += "index.html";
        else if(!split[split.length-1].contains("."))
            path += ".html";
        path = path.replace('/', httpServerConfig.getPathReplacementChar().charAt(0));

        File requested = new File(httpServerConfig.getServerpath() + path);
        try
        {
            FileInputStream fis = new FileInputStream(requested);

            StringBuilder stringBuilder = new StringBuilder();
            while (fis.available() != 0)
            {
                stringBuilder.append((char) fis.read());
            }
            fis.close();


            statusCodes.makeStandardResponse(200,httpResponse);
            httpResponse.setResponseMessage(stringBuilder.toString());
            if(path.endsWith(".jpg"))
            {
                httpResponse.setResponseHeader("Content-Type", "image/jpeg");
                httpResponse.setResponseHeader("Accept-Ranges","bytes");
            }
        } catch (IOException e)
        {
            ExceptionCollector.logException(e,-2);
            statusCodes.makeStandardResponse(404,httpResponse);
        }
    }


    public void headerRequest(HttpResponse httpResponse,HttpRequest httpRequest,Configable configable)
    {
        handleCORS(httpResponse,httpRequest);
        String path = httpRequest.getFilePath();
        path = path.replace('/','\\');
        File requested=new File(httpServerConfig.getServerpath()+path);
        if (requested.exists())
        {
            statusCodes.makeStandardResponse(200,httpResponse);
        }
        else
            statusCodes.makeStandardResponse(404,httpResponse);
    }

    public void postRequest(HttpResponse httpResponse, HttpRequest httpRequest, Configable configable)
    {
        handleCORS(httpResponse,httpRequest);
        statusCodes.makeStandardResponse(404,httpResponse);
    }

    public void optionsRequest(HttpResponse httpResponse,HttpRequest httpRequest,Configable configable)
    {
        boolean preflight = handleCORSb(httpResponse,httpRequest);
        if(preflight)
        {
            statusCodes.makeStandardResponse(200,httpResponse);
        }
        else
        {
            httpResponse.setStatusCode("405 Not Allowed");
            httpResponse.setResponseHeader("Server",HttpServer.ApplicationName);
        }
    }

    public void setHttpServerConfig(HttpServerConfig httpServerConfig) {
        this.httpServerConfig = httpServerConfig;
    }

    public void setAllowedOrigins(String[] allowedOrigins)
    {
        this.allowedOrigins = allowedOrigins;
    }
}