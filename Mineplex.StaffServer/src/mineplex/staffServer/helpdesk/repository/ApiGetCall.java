package mineplex.staffServer.helpdesk.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class ApiGetCall
{
	protected String ApiUrl = "https://mineplex.jitbit.com/helpdesk/api";
	protected String SecretKey = "?sharedSecret={WzG49S3L6spqt4}";
	private String _action;

	public ApiGetCall(String apiUrl, String action)
	{
		ApiUrl = apiUrl;
		_action = action;
	}

	protected String execute()
	{
		HttpGet request = new HttpGet(ApiUrl + SecretKey + "&" + _action);

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

	public void Execute()
	{
	}

	/*
	public <T> T Execute(Type returnType)
	{
		HttpGet request = new HttpGet(ApiUrl + DomainId + Category + _action);

		String response = execute(request);
		System.out.println(response);
		return new Gson().fromJson(response, returnType);
	}
	*/
}
