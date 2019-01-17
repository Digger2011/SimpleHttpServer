package HttpServer.Barebone;

import HttpServer.Classes.HttpResponse;
import HttpServer.Classes.HttpServer;
import HttpServer.Interfaces.StatusCodes;

/**
 * Created by phil on 13.10.2017.
 */
public class StandardStatusCodes implements StatusCodes
{
    @Override
    public boolean makeStandardResponse(int statuscode, HttpResponse httpResponse) {
        switch (statuscode)
        {
            case 200:success200ok(httpResponse);break;
            case 204:success204nocontent(httpResponse);break;
            case 400:error400badrequest(httpResponse);break;
            case 403:error403forbidden(httpResponse);break;
            case 404:error404notfound(httpResponse);break;
            case 500:error500internalservererror(httpResponse);break;
            default:return false;
        }
        return true;
    }

    private void success204nocontent(HttpResponse httpResponse)
    {
        httpResponse.setStatusCode("204 No Content");
        httpResponse.setServer(HttpServer.ApplicationName);
    }

    private void error403forbidden(HttpResponse httpResponse)
    {
        httpResponse.setStatusCode("403 Forbidden");
        httpResponse.setServer(HttpServer.ApplicationName);
    }

    private void error404notfound(HttpResponse httpResponse)
    {
        httpResponse.setStatusCode("404 Not Found");
        httpResponse.setServer(HttpServer.ApplicationName);
    }

    private void error500internalservererror(HttpResponse httpResponse)
    {
        httpResponse.setStatusCode("500 Internal Server Error");
        httpResponse.setServer(HttpServer.ApplicationName);
    }

    private void error400badrequest(HttpResponse httpResponse)
    {
        httpResponse.setStatusCode("400 Bad Request");
        httpResponse.setServer(HttpServer.ApplicationName);
    }

    private void success200ok(HttpResponse httpResponse)
    {
        httpResponse.setStatusCode("200 OK");
        httpResponse.setServer(HttpServer.ApplicationName);
    }
}
