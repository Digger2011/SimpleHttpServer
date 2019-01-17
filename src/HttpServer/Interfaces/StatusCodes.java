package HttpServer.Interfaces;

import HttpServer.Classes.HttpResponse;

/**
 * Created by phil on 13.10.2017.
 */
public interface StatusCodes
{
    boolean makeStandardResponse(int statuscode,HttpResponse httpResponse);
}
