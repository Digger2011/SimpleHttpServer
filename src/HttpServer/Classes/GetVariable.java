package HttpServer.Classes;

/**
 * Created by phil- on 02.06.2017.
 */
public class GetVariable
{
    public String name;
    public Object value;

    GetVariable(String name,Object value)
    {
        this.name = name;
        this.value = value;
    }

    public static Object GetVariable(String name,GetVariable[] variables)
    {
        Object value = null;
        for(GetVariable v : variables)
        {
            if(v.name.toLowerCase().equals(name.toLowerCase()))
            {
                value = v.value;
            }
        }
        return value;
    }
}
