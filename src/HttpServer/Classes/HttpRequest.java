package HttpServer.Classes;



import DataHandler.ExceptionCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Philipp Susen
 * this Object Checks the incoming Request for Headers and reads out the parameters if they Exist.
 */
public class HttpRequest
{

    private String Method = "";
    private String FilePath = "";
    private String HTTPver = "";
    private String Postdata = "";
    private InetAddress connectedIP;
    private String Accept = null;
    private String Accept_Charset = null;
    private String Accept_Datetime = null;
    private String Accept_Encoding = null;
    private String Accept_Language = null;
    private String Authorization = null;
    private String Cache_Control = null;
    private String Connection = null;
    private String Content_Length = null;
    private String Content_MD5 = null;
    private String Content_Type = null;
    private String Cookie = null;
    private String Date = null;
    private String Expect = null;
    private String Forwarded = null;
    private String From = null;
    private String Host = null;
    private String If_Match = null;
    private String If_Modified_Since = null;
    private String If_None_Match = null;
    private String If_Range = null;
    private String If_Unmodified_Since = null;
    private String Max_Forwards = null;
    private String Origin = null;
    private String Pragma = null;
    private String Proxy_Authorization = null;
    private String Range = null;
    private String Referer_sic = null;
    private String TE = null;
    private String Upgrade = null;
    private String User_Agent = null;
    private String Via = null;
    private String Warning = null;
    private String Access_Control_Request_Method = null;
    private String Access_Control_Request_Headers = null;
    private GetVariable[] getVariables = null;

    public HttpRequest(BufferedReader in, Socket socket)throws IOException
    {
        socket.setSoTimeout(2000);
        this.connectedIP = socket.getInetAddress();
        String input;
        String[] request = new String[0];
        int i = 0;
        while ((input = in.readLine())!=null)
        {
            if (!input.equals(""))
            {
                String[] temp = request;
                request = new String[temp.length+1];
                int o = 0;
                for (String s:temp)
                {
                    request[o]=s;
                    o++;
                }
                request[i] = input;
            }
            else
                break;
            i++;
        }

        i = 0;
        for (String s : request)
        {
            if (i == 0)
                processFirstLine(s);
            else
                validateHeaders(s);
            i++;
        }
        try
        {
            int length;
            if(Content_Length != null)
                length = Integer.valueOf(Content_Length.trim());
            else
                length = 0;
            if (Method.equals("POST") && length != 0)
            {
                StringBuilder builder = new StringBuilder();
                for (i =0;i<length;i++)
                {
                    builder.append((char) in.read());
                }
                Postdata = builder.toString();
            }
        }
        catch (NumberFormatException|NullPointerException e)
        {
            ExceptionCollector.logException(e,-1);
            Postdata = "";
        }
    }

    private void processFirstLine(String line)
    {
        int phase = 0;
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<line.length();i++)
        {
            if(line.charAt(i)== ' ')
            {
                switch (phase)
                {
                    case 0: Method = builder.toString();
                        break;
                    case 1:
                        FilePath = builder.toString();
                        if(FilePath.contains("?"))
                        {
                            String[] temp = FilePath.split("\\?");
                            FilePath = temp[0];
                            getVariables = filterVariables(temp);
                        }
                        break;
                }
                builder.delete(0,builder.length());
                phase++;
                continue;
            }
            builder.append(line.charAt(i));
        }
        HTTPver = builder.toString();
    }

    private GetVariable[] filterVariables(String[] variablestrings)
    {
        variablestrings = variablestrings[1].split("&");
        GetVariable[] variables = new GetVariable[variablestrings.length];
        for(int i = 0;i<variables.length;i++)
        {
            String[] temp = variablestrings[i].split("=");
            variables[i] = new GetVariable(temp[0],temp[1]);
        }
        return variables;
    }

    private void validateHeaders(String header)
    {
        if (header.contains("Accept:"))
        {
            Accept = header.split(" ", 2)[1];
        }
        else if (header.contains("Accept-Charset:"))
        {
            Accept_Charset = header.split(" ", 2)[1];
        }
        else if (header.contains("Accept-Datetime:"))
        {
            Accept_Datetime = header.split(" ", 2)[1];
        }
        else if (header.contains("Accept-Encoding:"))
        {
            Accept_Encoding = header.split(" ", 2)[1];
        }
        else if (header.contains("Accept-Language:"))
        {
            Accept_Language = header.split(" ", 2)[1];
        }
        else if (header.contains("Authorization:"))
        {
            Authorization = header.split(" ", 2)[1];
        }
        else if (header.contains("Cache-Control:"))
        {
            Cache_Control = header.split(" ", 2)[1];
        }
        else if (header.contains("Connection:"))
        {
            Cache_Control = header.split(" ", 2)[1];
        }
        else if (header.contains("Content-Length:"))
        {
            Content_Length = header.split(" ", 2)[1];
        }
        else if (header.contains("Content-MD5:"))
        {
            Content_MD5 = header.split(" ", 2)[1];
        }
        else if (header.contains("Content-Type:"))
        {
            Content_Type = header.split(" ", 2)[1];
        }
        else if (header.contains("Cookie:"))
        {
            Cookie = header.split(" ", 2)[1];
        }
        else if (header.contains("Date:"))
        {
            Date = header.split(" ", 2)[1];
        }
        else if (header.contains("Expect:"))
        {
            Expect = header.split(" ", 2)[1];
        }
        else if (header.contains("Forwarded:"))
        {
            Forwarded = header.split(" ", 2)[1];
        }
        else if (header.contains("From:"))
        {
            From = header.split(" ", 2)[1];
        }
        else if (header.contains("Host:"))
        {
            Host = header.split(" ", 2)[1];
        }
        else if (header.contains("If-Match:"))
        {
            If_Match = header.split(" ", 2)[1];
        }
        else if (header.contains("If-Modified-Since:"))
        {
            If_Modified_Since = header.split(" ", 2)[1];
        }
        else if (header.contains("If-None-Match:"))
        {
            If_None_Match = header.split(" ", 2)[1];
        }
        else if (header.contains("If-Range:"))
        {
            If_Range = header.split(" ", 2)[1];
        }
        else if (header.contains("If-Unmodified-Since:"))
        {
            If_Unmodified_Since = header.split(" ", 2)[1];
        }
        else if (header.contains("Max-Forwards:"))
        {
            Max_Forwards = header.split(" ", 2)[1];
        }
        else if (header.contains("Origin:"))
        {
            Origin = header.split(" ", 2)[1];
        }
        else if (header.contains("Pragma:"))
        {
            Pragma = header.split(" ", 2)[1];
        }
        else if (header.contains("Proxy-Authorization:"))
        {
            Proxy_Authorization = header.split(" ", 2)[1];
        }
        else if (header.contains("Range:"))
        {
            Range = header.split(" ", 2)[1];
        }
        else if (header.contains("Referer:"))
        {
            Referer_sic = header.split(" ", 2)[1];
        }
        else if (header.contains("TE:"))
        {
            TE = header.split(" ", 2)[1];
        }
        else if (header.contains("Upgrade:"))
        {
            Upgrade = header.split(" ", 2)[1];
        }
        else if (header.contains("User-Agent:"))
        {
            User_Agent = header.split(" ", 2)[1];
        }
        else if (header.contains("Via:"))
        {
            Via = header.split(" ", 2)[1];
        }
        else if (header.contains("Warning:"))
        {
            Warning = header.split(" ", 2)[1];
        }
        else if (header.contains("Access-Control-Request-Headers:"))
        {
            Access_Control_Request_Headers = header.split(" ", 2)[1];
        }
        else if (header.contains("Access_Control_Request_Method:"))
        {
            Access_Control_Request_Method = header.split(" ", 2)[1];
        }
    }

    public InetAddress getConnectedIP()
    {
        return connectedIP;
    }

    public GetVariable[] getGetVariables()
    {
        return getVariables;
    }
    /**
     * @return
     * Returns the used HTTP Method
     */
    public String getMethod()
    {
        return Method;
    }

    /**
     * @return
     * Returns the requested FilePath
     */
    public String getFilePath()
    {
        return FilePath;
    }

    /**
     * @return
     * Returns the used HTTP protocol
     */
    public String getHTTPver()
    {
        return HTTPver;
    }

    /**
     * @return
     * Returns the Postdata Null if no postdata
     */
    public String getPostdata()
    {
        return Postdata;
    }

    /**
     * Content-Types that are acceptable for the response.
     * @return
     * Returns Header Accept.
     */
    public String getAccept()
    {
        return Accept;
    }

    /**
     * Character sets that are acceptable.
     * @return
     * Returns Header Accept_Charset
     */
    public String getAccept_Charset()
    {
        return Accept_Charset;
    }

    /**
     * Acceptable version in time.
     * @return
     * Returns Header Accept_Datetime
     */
    public String getAccept_Datetime()
    {
        return Accept_Datetime;
    }

    /**
     * List of acceptable encodings.
     * @return
     * Returns Header Accept-Encoding
     */
    public String getAccept_Encoding()
    {
        return Accept_Encoding;
    }

    /**
     * List of acceptable human languages for response.
     * @return
     * Returns Header Accept-Language
     */
    public String getAccept_Language()
    {
        return Accept_Language;
    }

    /**
     * Authentication credentials for HTTP authentication.
     * @return
     * Returns Header Authorization
     */
    public String getAuthorization()
    {
        return Authorization;
    }

    /**
     * Used to specify directives that must be obeyed by all caching mechanisms along the request-response chain.
     * @return
     * Returns Header Cache-Control
     */
    public String getCache_Control()
    {
        return Cache_Control;
    }

    /**
     * Control options for the current connection and list of hop-by-hop request fields.
     * @return
     * Returns Header Connection
     */
    public String getConnection()
    {
        return Connection;
    }

    /**
     * The length of the request body in octets (8-bit bytes).
     * @return
     * Returns Header Content-Length
     */
    public String getContent_Length()
    {
        return Content_Length;
    }

    /**
     * A Base64-encoded binary MD5 sum of the content of the request body.
     * @return
     * Returns Header Content-MD5
     */
    public String getContent_MD5()
    {
        return Content_MD5;
    }

    /**
     * The MIME type of the body of the request (used with POST and PUT requests).
     * @return
     * Returns Header Content-Type
     */
    public String getContent_Type()
    {
        return Content_Type;
    }

    /**
     * An HTTP cookie previously sent by the server with Set-Cookie (below).
     * @return
     * Returns Header Cookie
     */
    public String getCookie()
    {
        return Cookie;
    }

    /**
     * The date and time that the message was originated
     * @return
     * Returns Header Date
     */
    public String getDate()
    {
        return Date;
    }

    /**
     * Indicates that particular server behaviors are required by the client.
     * @return
     * Returns Header Expect
     */
    public String getExpect()
    {
        return Expect;
    }

    /**
     * Disclose original information of a client connecting to a web server through an HTTP proxy.
     * @return
     * Returns Header Forwarded
     */
    public String getForwarded()
    {
        return Forwarded;
    }

    /**
     * The email address of the user making the request.
     * @return
     * Returns Header From
     */
    public String getFrom()
    {
        return From;
    }

    /**
     * The domain name of the server (for virtual hosting), and the TCP port number on which the server is listening. The port number may be omitted if the port is the standard port for the service requested.
     * @return
     * Returns Header Host
     */
    public String getHost()
    {
        return Host;
    }

    /**
     * Only perform the action if the client supplied entity matches the same entity on the server. This is mainly for methods like PUT to only update a resource if it has not been modified since the user last updated it.
     * @return
     * Returns Header If-Match
     */
    public String getIf_Match()
    {
        return If_Match;
    }

    /**
     * Allows a 304 Not Modified to be returned if content is unchanged.
     * @return
     * Returns Header If-Modified-Since
     */
    public String getIf_Modified_Since()
    {
        return If_Modified_Since;
    }

    /**
     * Allows a 304 Not Modified to be returned if content is unchanged.
     * @return
     * Returns Header If-None-Match
     */
    public String getIf_None_Match()
    {
        return If_None_Match;
    }

    /**
     * If the entity is unchanged, send me the part(s) that I am missing; otherwise, send me the entire new entity.
     * @return
     * Returns Header If-Range
     */
    public String getIf_Range()
    {
        return If_Range;
    }

    /**
     * Only send the response if the entity has not been modified since a specific time.
     * @return
     * Returns Header Unmodified-Since
     */
    public String getIf_Unmodified_Since()
    {
        return If_Unmodified_Since;
    }

    /**
     * Limit the number of times the message can be forwarded through proxies or gateways.
     * @return
     * Returns Header Max-Forwards
     */
    public String getMax_Forwards()
    {
        return Max_Forwards;
    }

    /**
     * Initiates a request for cross-origin resource sharing (asks server for an 'Access-Control-Allow-Origin' response field).
     * @return
     * Returns Header Origin
     */
    public String getOrigin()
    {
        return Origin;
    }

    /**
     * Implementation-specific fields that may have various effects anywhere along the request-response chain.
     * @return
     * Returns Header Pragma
     */
    public String getPragma()
    {
        return Pragma;
    }

    /**
     * Authorization credentials for connecting to a proxy.
     * @return
     * Returns Header Proxy-Authorization
     */
    public String getProxy_Authorization()
    {
        return Proxy_Authorization;
    }

    /**
     * Request only part of an entity. Bytes are numbered from 0.
     * @return
     * Returns Header Range
     */
    public String getRange()
    {
        return Range;
    }

    /**
     * This is the address of the previous web page from which a link to the currently requested page was followed. (The word “referrer” has been misspelled in the RFC as well as in most implementations to the point that it has become standard usage and is considered correct terminology)
     * @return
     * Returns Header Referer_sic
     */
    public String getReferer_sic()
    {
        return Referer_sic;
    }

    /**
     * The transfer encodings the user agent is willing to accept: the same values as for the response header field Transfer-Encoding can be used,
     * plus the "trailers" value (related to the "chunked" transfer method) to notify the server it expects to receive additional fields in the trailer after the last, zero-sized, chunk.
     * @return
     * Returns Header TE
     */
    public String getTE()
    {
        return TE;
    }

    /**
     * Ask the server to upgrade to another protocol.
     * @return
     * Returns Header Upgrade
     */
    public String getUpgrade()
    {
        return Upgrade;
    }

    /**
     * The user agent string of the user agent.
     * @return
     * Returns Header User-Agent
     */
    public String getUser_Agent()
    {
        return User_Agent;
    }

    /**
     * 	Informs the server of proxies through which the request was sent.
     * @return
     * Returns Header Via
     */
    public String getVia()
    {
        return Via;
    }

    /**
     * A general warning about possible problems with the entity body.
     * @return
     * Returns Header Warning
     */
    public String getWarning()
    {
        return Warning;
    }

    /**
     * Used by CORS Needs an Access-Control-Allow-Headers Header in Response
     * @return
     * Returns Header Access-Control-Request-Headers
     */
    public String getAccess_Control_Request_Headers()
    {
        return Access_Control_Request_Headers;
    }

    /**
     * Used by CORS Needs an Access-Control-Allow-Methods Header in Response
     * @return
     * Returns Header Access_Control_Request_Method
     */
    public String getAccess_Control_Request_Method()
    {
        return Access_Control_Request_Method;
    }
}