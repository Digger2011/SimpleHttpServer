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
    private String charset = "ISO-8859-1";
    //Header Variablen
    private String StatusCode = null;
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
                    headerVariablesMap.put("Connection","close");
                    headerVariablesMap.put("Content_Length",String.valueOf(responseMessage.length()));
                    appendHeaders(builder);
                    builder.append("\r\n");
                    builder.append(responseMessage);
                    out.write(builder.toString().getBytes(charset));
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