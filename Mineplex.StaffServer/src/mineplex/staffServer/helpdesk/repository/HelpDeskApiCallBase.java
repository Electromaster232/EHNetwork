package mineplex.staffServer.helpdesk.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public abstract class HelpDeskApiCallBase
{
	protected String ApiUrl = "https://mineplex.jitbit.com/helpdesk/api";
	protected int DomainId = 962728;
	protected String Category = "";
	
	public HelpDeskApiCallBase(String apiUrl, int domainId, String category)
	{
		ApiUrl = apiUrl;
		DomainId = domainId;
		Category = category;		
	}
	
	protected String execute(HttpRequestBase request)
	{
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("https", 80, PlainSocketFactory.getSocketFactory()));
	
		PoolingClientConnectionManager connectionManager = new PoolingClientConnectionManager(schemeRegistry);
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		
	    HttpClient httpClient = new DefaultHttpClient(connectionManager);
	    InputStream in = null;
	    String response = "";
	    
	    try
	    {
	        HttpResponse httpResponse = httpClient.execute(request);            
	
	        if (httpResponse != null)
	        {
	            in = httpResponse.getEntity().getContent();
	            response = convertStreamToString(in);
	        }
	    }
	    catch (Exception ex) 
	    {
	        System.out.println("HelpDeskApiCall Error:\n" + ex.getMessage());
	        
	        for (StackTraceElement trace : ex.getStackTrace())
	        {
	        	System.out.println(trace);
	        }
	    } 
	    finally 
	    {
	        httpClient.getConnectionManager().shutdown();
	        
	        if (in != null)
	        {
	            try 
	            {
	                in.close();
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return response;
	}

	protected String getServerTime()
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(calendar.getTime());
	}

	protected String convertStreamToString(InputStream is)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
