package HttpServer.Classes;



import DataHandler.ExceptionCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

    private Map<String,String> headerVariablesMap = new HashMap<>(0);
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
            if(headerVariablesMap.get("Content_Length") != null)
                length = Integer.valueOf(headerVariablesMap.get("Content_Length").trim());
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
        String[] headersplit = header.split(":",2);
        headerVariablesMap.put(headersplit[0].trim(),headersplit[1].trim());
    }

    /**
     * @return
     * Returns the IP from the Client of this Request
     */
    public InetAddress getConnectedIP()
    {
        return connectedIP;
    }

    /**
     * @return
     * Returns the GetVariables supplied by the Request
     */
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
     * @param headerName
     * The Header to request.
     * @return
     * The value of the requested Header.
     */
    public String getHeader(String headerName)
    {
        return headerVariablesMap.get(headerName);
    }
}