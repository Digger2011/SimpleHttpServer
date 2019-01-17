package HttpServer.Classes;

import JSON.ConfigWriter;
import JSON.Configable;
import JSON.JsonReader;


import javax.swing.filechooser.FileSystemView;

/**
 * Created by phil- on 12.10.2017.
 */
public class HttpServerConfig implements Configable
{
    private boolean allowencoding;
    private int minsizeencode;
    private int port;
    private int sslport;
    private String serverpath;
    private String sslkeystore;
    private String pathReplacementChar;

    public HttpServerConfig(boolean allowencoding, int minsizeencode, int port, int sslport, String serverpath, String sslkeystore, String pathReplacementChar)
    {
        this.allowencoding = allowencoding;
        this.minsizeencode = minsizeencode;
        this.port = port;
        this.sslport = sslport;
        this.serverpath = serverpath;
        this.sslkeystore = sslkeystore;
        this.pathReplacementChar = pathReplacementChar;
    }

    public HttpServerConfig()
    {
        allowencoding = true;
        minsizeencode = 1000;
        port = 80;
        sslport = 443;
        serverpath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        sslkeystore = "";
        pathReplacementChar = "\\";
    }

    @Override
    public void addVariables(ConfigWriter configWriter)
    {
        configWriter.addComment("HTTP-SERVER");
        configWriter.addElse("AllowEncoding",String.valueOf(allowencoding),"Bestimmt ob das kompriemieren von der Response erlaubt ist.");
        configWriter.addElse("MinSizeEncoding",String.valueOf(minsizeencode),"Bestimmt ab welcher größe Antworten komprimiert werden.(Bytes)");
        configWriter.addElse("Port",String.valueOf(port),"Setzt den Port auf welchem der Server nach HTTP-Anfragen wartet.");
        configWriter.addElse("SSLPort",String.valueOf(sslport),"Setzt den Port auf welchem der Server nach HTTPS-Anfragen wartet.");
        configWriter.addString("ServerPath",serverpath,"Setzt den Pfad der Webseite.");
        configWriter.addString("SSLKeyStore",sslkeystore,"Setzt den Pfad des KeyStores.");
        configWriter.addString("PathReplacementChar",pathReplacementChar,"bestimmt durch was / im Pfad einer HTTP-Anfrage ersetzt werden soll");
        configWriter.addComment("/HTTP-SERVER");
    }

    @Override
    public Configable getVariables(JsonReader jsonReader)
    {
        return new HttpServerConfig((boolean)jsonReader.getVariable("AllowEncoding"),
                (int) jsonReader.getVariable("MinSizeEncoding"),
                (int) jsonReader.getVariable("Port"),
                (int) jsonReader.getVariable("SSLPort"),
                (String) jsonReader.getVariable("ServerPath"),
                (String) jsonReader.getVariable("SSLKeyStore"),
                (String) jsonReader.getVariable("PathReplacementChar"));
    }

    public String getPathReplacementChar()
    {
        return pathReplacementChar;
    }

    public void setPathReplacementChar(String pathReplacementChar)
    {
        this.pathReplacementChar = pathReplacementChar;
    }

    public boolean isAllowencoding()
    {
        return allowencoding;
    }

    public void setAllowencoding(boolean allowencoding)
    {
        this.allowencoding = allowencoding;
    }

    public int getMinsizeencode()
    {
        return minsizeencode;
    }

    public void setMinsizeencode(int minsizeencode)
    {
        this.minsizeencode = minsizeencode;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getServerpath() {
        return serverpath;
    }

    public void setServerpath(String serverpath) {
        this.serverpath = serverpath;
    }

    public int getSslport()
    {
        return sslport;
    }

    public void setSslport(int sslport)
    {
        this.sslport = sslport;
    }

    public String getSslkeystore()
    {
        return sslkeystore;
    }

    public void setSslkeystore(String sslkeystore)
    {
        this.sslkeystore = sslkeystore;
    }
}
