package mineplex.hub.modules;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import mineplex.core.MiniPlugin;
import mineplex.hub.HubManager;

public class ForumManager extends MiniPlugin
{
	private HubManager Manager;
	private String _baseURL;
	public ForumManager(HubManager manager)
	{
		super("Forum Manager", manager.getPlugin());

		Manager = manager;
		_baseURL = "https://mplex.endlcdn.site/forum.php";
	}

	public String[] getPosts(String category){
		String response = sendGET(_baseURL + "?action=getPosts&category=" + category);
		return response.split("%");
	}

	public String[] getPost(String category, String postID){
		String response = sendGET(_baseURL + "?action=getPost&category=" + category + "&postid=" + postID);
		return response.split("%");
	}




	private static String sendGET(String GET_URL) {
		try
		{
			URL obj = new URL(GET_URL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "EHNetwork/1.0");
			int responseCode = con.getResponseCode();
			System.out.println("GET Response Code :: " + responseCode);
			if (responseCode == HttpsURLConnection.HTTP_OK)
			{ // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null)
				{
					response.append(inputLine);
				}
				in.close();

				// print result
				return response.toString();
			}
			else
			{
				return null;
			}
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}

	}

	private static String sendPOST(String POST_URL, String POST_PARAMS) throws IOException
	{
		URL obj = new URL(POST_URL);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "EHNetwork/1.0");

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpsURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			return response.toString();
		} else {
			return null;
		}
	}


}
