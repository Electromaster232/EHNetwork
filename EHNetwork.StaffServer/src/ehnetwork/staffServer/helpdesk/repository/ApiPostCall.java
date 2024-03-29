package ehnetwork.staffServer.helpdesk.repository;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

public class ApiPostCall extends HelpDeskApiCallBase
{
	private String _action;
	
	public ApiPostCall(String apiUrl, int domainId, String category, String action)
	{
		super(apiUrl, domainId, category);
		
		_action = action;
	}

	public void Execute(Object argument)
    {
		Gson gson = new Gson();
		HttpPost request = new HttpPost(ApiUrl + DomainId + Category + _action);
		
		System.out.println(request.getURI().toString());
		
		try
		{
	        StringEntity params = new StringEntity(gson.toJson(argument));
	        params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        request.setEntity(params);
		}
		catch (Exception exception)
		{
            System.out.println("Error executing ApiPostCall(Object): \n" + exception.getMessage());
            
            for (StackTraceElement trace : exception.getStackTrace())
            {
            	System.out.println(trace);
            }
		}
        
		System.out.println(execute(request));
    }
	
    public <T> T Execute(Class<T> returnClass)
    {
        return Execute(returnClass, (Object)null);
    }
    
    public <T> T Execute(Type returnType, Object argument)
    {
		Gson gson = new Gson();
		HttpPost request = new HttpPost(ApiUrl + DomainId + Category + _action);
		System.out.println(request.getURI().toString());
		try
		{
	        StringEntity params = new StringEntity(gson.toJson(argument));
	        params.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        request.setEntity(params);
		}
		catch (Exception exception)
		{
            System.out.println("Error executing ApiPostCall(Type, Object): \n" + exception.getMessage());
            
            for (StackTraceElement trace : exception.getStackTrace())
            {
            	System.out.println(trace);
            }
		}
		
		String response = execute(request);
		System.out.println(response);
		return new Gson().fromJson(response, returnType);
    }
}
