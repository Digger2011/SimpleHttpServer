package HttpServer;

import DataHandler.ExceptionCollector;
import HttpServer.Classes.HttpServer;
import HttpServer.Classes.HttpServerConfig;

public class TestStart
{
    public static void main(String[] args)
    {
        ExceptionCollector.setLogPath("errorlog.txt");//Set the Log-Path for Errors
        HttpServerConfig httpServerConfig = new HttpServerConfig();
        httpServerConfig.setPathReplacementChar("\\");//What does your OS use for the Folder Structure
        httpServerConfig.setServerpath("Web");//The Path where the Server looks for your Website-Files
        HttpServer httpServer = new HttpServer(httpServerConfig);

        while (true)
        {
            httpServer.acceptClient();
        }
    }
}
