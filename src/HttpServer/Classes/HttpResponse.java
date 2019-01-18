package HttpServer.Classes;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPOutputStream;

/**
 * Created by phil- on 09.04.2017.
 */
public class HttpResponse
{
    //Sonstige
    private String charset = "ASCII";
    //Header Variablen
    private String StatusCode = null;
    private String Access_Control_Allow_Origin = null;
    private String Access_Control_Allow_Credentials = null;
    private String Access_Control_Expose_Headers = null;
    private String Access_Control_Allow_Methods = null;
    private String Access_Control_Allow_Headers = null;
    private String Accept_Patch = null;
    private String Accept_Ranges = null;
    private String Age = null;
    private String Allow = null;
    private String Alt_Svc =null;
    private String Cache_Control =null;
    private String Connection = null;
    private String Content_Disposition = null;
    private String Content_Encoding = null;
    private String Content_Language = null;
    private String Content_Location = null;
    private String Content_Length = null;
    private String Content_Range = null;
    private String Content_Type = null;
    private String Date = null;
    private String ETag = null;
    private String Expires = null;
    private String Last_Modified = null;
    private String Link = null;
    private String Location = null;
    private String P3P = null;
    private String Pragma = null;
    private String Proxy_Authenticate = null;
    private String Public_Key_Pins = null;
    private String Retry_After = null;
    private String Server = null;
    private String Set_Cookie = null;
    private String Strict_Transport_Security = null;
    private String Trailer = null;
    private String Transfer_Encoding = null;
    private String TSV = null;
    private String Upgrade = null;
    private String Vary = null;
    private String Via = null;
    private String Warning = null;
    private String WWW_Authenticate = null;
    private String responseMessage = null;

    private Map<String,String> HeaderMap = new HashMap<>(0);


    public void SendResponse(OutputStream out, boolean allowencoding,HttpServerConfig httpServerConfig) throws IOException
    {
        if (StatusCode != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            java.util.Date date = new Date();
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date = dateFormat.format(date);

            StringBuilder builder = new StringBuilder();
            builder.append("HTTP/1.1");
            builder.append(' ');
            builder.append(StatusCode);
            builder.append("\r\n");
            if (responseMessage != null)
            {
                if (httpServerConfig.isAllowencoding() && responseMessage.length() >= httpServerConfig.getMinsizeencode() && allowencoding)
                {
                    Content_Encoding = "gzip";
                    Vary = "Accept-Encoding";

                    byte[] tocompresseddata = responseMessage.getBytes(charset);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(tocompresseddata.length);
                    try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream))
                    {
                        gzipOutputStream.write(tocompresseddata);
                        gzipOutputStream.flush();
                    }
                    byteArrayOutputStream.close();
                    byte[] compresseddata = byteArrayOutputStream.toByteArray();

                    Content_Length = String.valueOf(compresseddata.length);
                    appendHeaders(builder);
                    builder.append("\r\n");

                    out.write(builder.toString().getBytes(charset));
                    out.write(compresseddata);
                    out.flush();

                }
                else
                {
                    appendHeaders(builder);
                    builder.append("\r\n");
                    builder.append(responseMessage);
                    out.write(builder.toString().getBytes(charset));
                    out.write(responseMessage.getBytes(charset));
                    out.flush();
                }
            }
            else
            {
                appendHeaders(builder);
                builder.append("\r\n");
                out.write(builder.toString().getBytes(charset));
                out.flush();
            }
        }
    }

    private void appendHeaders(StringBuilder builder)
    {
        if (Cache_Control != null)
        {
            builder.append("Cache-Control: ");
            builder.append(Cache_Control);
            builder.append("\r\n");
        }
        if (Connection != null)
        {
            builder.append("Connection: ");
            builder.append(Connection);
            builder.append("\r\n");
        }
        if (Date != null)
        {
            builder.append("Date: ");
            builder.append(Date);
            builder.append("\r\n");
        }
        if (Pragma != null)
        {
            builder.append("Pragma: ");
            builder.append(Pragma);
            builder.append("\r\n");
        }
        if (Trailer != null)
        {
            builder.append("Trailer: ");
            builder.append(Trailer);
            builder.append("\r\n");
        }
        if (Transfer_Encoding != null)
        {
            builder.append("Transfer-Encoding: ");
            builder.append(Transfer_Encoding);
            builder.append("\r\n");
        }
        if (Upgrade != null)
        {
            builder.append("Upgrade: ");
            builder.append(Upgrade);
            builder.append("\r\n");
        }
        if (Via != null)
        {
            builder.append("Via: ");
            builder.append(Via);
            builder.append("\r\n");
        }
        if (Warning != null)
        {
            builder.append("Warning: ");
            builder.append(Warning);
            builder.append("\r\n");
        }
        if (Access_Control_Allow_Methods != null)
        {
            builder.append("Access-Control-Allow-Methods: ");
            builder.append(Access_Control_Allow_Methods);
            builder.append("\r\n");
        }
        if (Access_Control_Expose_Headers != null)
        {
            builder.append("Access-Control-Expose-Headers: ");
            builder.append(Access_Control_Expose_Headers);
            builder.append("\r\n");
        }
        if (Access_Control_Allow_Headers != null)
        {
            builder.append("Access-Control-Allow-Headers: ");
            builder.append(Access_Control_Allow_Headers);
            builder.append("\r\n");
        }
        if (Access_Control_Allow_Origin != null)
        {
            builder.append("Access-Control-Allow-Origin: ");
            builder.append(Access_Control_Allow_Origin);
            builder.append("\r\n");
        }
        if (Access_Control_Allow_Credentials != null)
        {
            builder.append("Access_Control_Allow_Credentials: ");
            builder.append(Access_Control_Allow_Credentials);
            builder.append("\r\n");
        }
        if (Accept_Ranges != null)
        {
            builder.append("Accept-Ranges: ");
            builder.append(Accept_Ranges);
            builder.append("\r\n");
        }
        if (Age != null)
        {
            builder.append("Age: ");
            builder.append(Age);
            builder.append("\r\n");
        }
        if (ETag != null)
        {
            builder.append("ETag: ");
            builder.append(ETag);
            builder.append("\r\n");
        }
        if (Location != null)
        {
            builder.append("Location: ");
            builder.append(Location);
            builder.append("\r\n");
        }
        if (Proxy_Authenticate != null)
        {
            builder.append("Proxy-Authenticate: ");
            builder.append(Proxy_Authenticate);
            builder.append("\r\n");
        }
        if (Retry_After != null)
        {
            builder.append("Retry-After: ");
            builder.append(Retry_After);
            builder.append("\r\n");
        }
        if (Server != null)
        {
            builder.append("Server: ");
            builder.append(Server);
            builder.append("\r\n");
        }
        if (Vary != null)
        {
            builder.append("Vary: ");
            builder.append(Vary);
            builder.append("\r\n");
        }
        if (WWW_Authenticate != null)
        {
            builder.append("WWW-Authenticate: ");
            builder.append(WWW_Authenticate);
            builder.append("\r\n");
        }
        if (Allow != null)
        {
            builder.append("Allow: ");
            builder.append(Allow);
            builder.append("\r\n");
        }
        if (Content_Encoding != null)
        {
            builder.append("Content-Encoding: ");
            builder.append(Content_Encoding);
            builder.append("\r\n");
        }
        if (Content_Language != null)
        {
            builder.append("Content-Language: ");
            builder.append(Content_Language);
            builder.append("\r\n");
        }
        if (Content_Length != null)
        {
            builder.append("Content-Length: ");
            builder.append(Content_Length);
            builder.append("\r\n");
        }
        else if (responseMessage != null)
        {
            builder.append("Content-Length: ");
            builder.append(String.valueOf(responseMessage.length()));
            builder.append("\r\n");
        }
        if (Content_Location != null)
        {
            builder.append("Content-Location: ");
            builder.append(Content_Location);
            builder.append("\r\n");
        }
        if (Content_Range != null)
        {
            builder.append("Content-Range: ");
            builder.append(Content_Range);
            builder.append("\r\n");
        }
        if (Content_Type != null)
        {
            builder.append("Content-Type: ");
            builder.append(Content_Type);
            builder.append("\r\n");
        }
        if (Expires != null)
        {
            builder.append("Expires: ");
            builder.append(Expires);
            builder.append("\r\n");
        }
        if (Last_Modified != null)
        {
            builder.append("Last-Modified: ");
            builder.append(Last_Modified);
            builder.append("\r\n");
        }
        if (Accept_Patch != null)
        {
            builder.append("Accept-Patch: ");
            builder.append(Accept_Patch);
            builder.append("\r\n");
        }
        if (Alt_Svc != null)
        {
            builder.append("Alt-Svc: ");
            builder.append(Alt_Svc);
            builder.append("\r\n");
        }
        if (Content_Disposition != null)
        {
            builder.append("Content-Disposition: ");
            builder.append(Content_Disposition);
            builder.append("\r\n");
        }
        if (Link != null)
        {
            builder.append("Link: ");
            builder.append(Link);
            builder.append("\r\n");
        }
        if (P3P != null)
        {
            builder.append("P3P: ");
            builder.append(P3P);
            builder.append("\r\n");
        }
        if (Public_Key_Pins != null)
        {
            builder.append("Public-Key-Pins: ");
            builder.append(Public_Key_Pins);
            builder.append("\r\n");
        }
        if (Set_Cookie != null)
        {
            builder.append("Set-Cookie: ");
            builder.append(Set_Cookie);
            builder.append("\r\n");
        }
        if (Strict_Transport_Security != null)
        {
            builder.append("Strict-Transport-Security: ");
            builder.append(Strict_Transport_Security);
            builder.append("\r\n");
        }
        if (TSV != null)
        {
            builder.append("TSV: ");
            builder.append(TSV);
            builder.append("\r\n");
        }
    }

    public void setStatusCode(String statusCode)
    {
        StatusCode = statusCode;
    }

    public void setAccess_Control_Allow_Origin(String access_Control_Allow_Origin)
    {
        Access_Control_Allow_Origin = access_Control_Allow_Origin;
    }

    public void setAccess_Control_Allow_Credentials(String access_Control_Allow_Credentials)
    {
        Access_Control_Allow_Credentials = access_Control_Allow_Credentials;
    }

    public void setAccess_Control_Expose_Headers(String access_Control_Expose_Headers)
    {
        Access_Control_Expose_Headers = access_Control_Expose_Headers;
    }

    public void setAccess_Control_Allow_Methods(String access_Control_Allow_Methods)
    {
        Access_Control_Allow_Methods = access_Control_Allow_Methods;
    }

    public void setAccess_Control_Allow_Headers(String access_Control_Allow_Headers)
    {
        Access_Control_Allow_Headers = access_Control_Allow_Headers;
    }

    public void setAccept_Patch(String accept_Patch)
    {
        Accept_Patch = accept_Patch;
    }

    public void setAccept_Ranges(String accept_Ranges)
    {
        Accept_Ranges = accept_Ranges;
    }

    public void setAge(String age)
    {
        Age = age;
    }

    public void setAllow(String allow)
    {
        Allow = allow;
    }

    public void setAlt_Svc(String alt_Svc)
    {
        Alt_Svc = alt_Svc;
    }

    public void setCache_Control(String cache_Control)
    {
        Cache_Control = cache_Control;
    }

    public void setConnection(String connection)
    {
        Connection = connection;
    }

    public void setContent_Disposition(String content_Disposition)
    {
        Content_Disposition = content_Disposition;
    }

    public void setContent_Encoding(String content_Encoding)
    {
        Content_Encoding = content_Encoding;
    }

    public void setContent_Language(String content_Language)
    {
        Content_Language = content_Language;
    }

    public void setContent_Location(String content_Location)
    {
        Content_Location = content_Location;
    }

    public void setContent_Range(String content_Range)
    {
        Content_Range = content_Range;
    }

    public void setContent_Type(String content_Type)
    {
        Content_Type = content_Type;
    }

    public void setDate(String date)
    {
        Date = date;
    }

    public void setETag(String ETag)
    {
        this.ETag = ETag;
    }

    public void setExpires(String expires)
    {
        Expires = expires;
    }

    public void setLast_Modified(String last_Modified)
    {
        Last_Modified = last_Modified;
    }

    public void setLink(String link)
    {
        Link = link;
    }

    public void setLocation(String location)
    {
        Location = location;
    }

    public void setP3P(String p3P)
    {
        P3P = p3P;
    }

    public void setPragma(String pragma)
    {
        Pragma = pragma;
    }

    public void setProxy_Authenticate(String proxy_Authenticate)
    {
        Proxy_Authenticate = proxy_Authenticate;
    }

    public void setPublic_Key_Pins(String public_Key_Pins)
    {
        Public_Key_Pins = public_Key_Pins;
    }

    public void setRetry_After(String retry_After)
    {
        Retry_After = retry_After;
    }

    public void setServer(String server)
    {
        Server = server;
    }

    public void setSet_Cookie(String set_Cookie)
    {
        Set_Cookie = set_Cookie;
    }

    public void setStrict_Transport_Security(String strict_Transport_Security)
    {
        Strict_Transport_Security = strict_Transport_Security;
    }

    public void setTrailer(String trailer)
    {
        Trailer = trailer;
    }

    public void setTSV(String TSV)
    {
        this.TSV = TSV;
    }

    public void setUpgrade(String upgrade)
    {
        Upgrade = upgrade;
    }

    public void setVary(String vary)
    {
        Vary = vary;
    }

    public void setVia(String via)
    {
        Via = via;
    }

    public void setWarning(String warning)
    {
        Warning = warning;
    }

    public void setWWW_Authenticate(String WWW_Authenticate)
    {
        this.WWW_Authenticate = WWW_Authenticate;
    }

    public void setResponseMessage(String responseMessage)
    {
        this.responseMessage = responseMessage;
    }

    public String getStatusCode()
    {
        return StatusCode;
    }
}