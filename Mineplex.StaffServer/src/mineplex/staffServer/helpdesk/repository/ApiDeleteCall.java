package mineplex.staffServer.helpdesk.repository;

import org.apache.http.client.methods.HttpDelete;

public class ApiDeleteCall extends HelpDeskApiCallBase
{
	public ApiDeleteCall(String apiUrl, int domainId, String category)
	{
		super(apiUrl, domainId, category);
	}
	
    public void Execute()
    {
		HttpDelete request = new HttpDelete(ApiUrl + DomainId + Category);
		System.out.println(request.getURI().toString());
		
		System.out.println(execute(request));
    }
}
