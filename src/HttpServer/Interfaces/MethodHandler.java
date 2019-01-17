package HttpServer.Interfaces;


import HttpServer.Classes.HttpResponse;
import HttpServer.Classes.HttpRequest;
import JSON.Configable;

/**
 * Created by phil- on 07.10.2017.
 */
public interface MethodHandler
{
    void getRequest(HttpResponse httpResponse, HttpRequest httpRequest,Configable configable);
    void headerRequest(HttpResponse httpResponse, HttpRequest httpRequest,Configable configable);
    void postRequest(HttpResponse httpResponse, HttpRequest httpRequest, Configable configable);
    void optionsRequest(HttpResponse httpResponse, HttpRequest httpRequest,Configable configable);
}
