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
    /*private String Access_Control_Allow_Origin = null;
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
    private String WWW_Authenticate = null;*/
    private String responseMessage = null;

    private Map<String,String> headerVariablesMap = new HashMap<>(0);


    public void SendResponse(OutputStream out, boolean allowencoding,HttpServerConfig httpServerConfig) throws IOException
    {
        if (StatusCode != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            java.util.Date date = new Date();
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            headerVariablesMap.put("Date",dateFormat.format(date));

            StringBuilder builder = new StringBuilder();
            builder.append("HTTP/1.1");
            builder.append(' ');
            builder.append(StatusCode);
            builder.append("\r\n");
            if (responseMessage != null)
            {
                if (httpServerConfig.isAllowencoding() && responseMessage.length() >= httpServerConfig.getMinsizeencode() && allowencoding)
                {
                    headerVariablesMap.put("Content_Encoding","gzip");
                    headerVariablesMap.put("Vary","Accept-Encoding");

                    byte[] tocompresseddata = responseMessage.getBytes(charset);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(tocompresseddata.length);
                    try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream))
                    {
                        gzipOutputStream.write(tocompresseddata);
                        gzipOutputStream.flush();
                    }
                    byteArrayOutputStream.close();
                    byte[] compresseddata = byteArrayOutputStream.toByteArray();

                    headerVariablesMap.put("Content_Length",String.valueOf(compresseddata.length));
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
        for (Map.Entry entry:headerVariablesMap.entrySet())
        {
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue());
            builder.append("\r\n");
        }
    }

    public void setStatusCode(String statusCode)
    {
        StatusCode = statusCode;
    }

    public void setResponseHeader(String headerName,String headerValue)
    {
        headerVariablesMap.put(headerName,headerValue);
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