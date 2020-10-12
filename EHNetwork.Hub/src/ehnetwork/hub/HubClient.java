package ehnetwork.hub;

public class HubClient
{  
	public String ScoreboardString = "         Hello, I am a big friendly cat!";
	public int ScoreboardIndex = 0;
	
	public String NewsString = "         Bridges v2.0 is coming soon! New gameplay, new kits, new maps!";
	public int NewsIndex = 0;
	
	public String PurchaseString = "         Purchase Ultra Rank at mineplex.com to unlock all game benefits!";
	public int PurchaseIndex = 0;
 
	public String UltraString = "         Thank you for your support!";
	public int UltraIndex = 0;
	
	public String StaffString = "None";
	public int StaffIndex = 0;
	
	public String BestPig = "0-Nobody";
		 
	public int DisplayLength = 16;
	
	private int _lastGemCount = 0;
	
	private int _lastCoinCount = 0;  
	
	public HubClient(String name)
	{
		ScoreboardString = "      Welcome " + name + ", to the Endless Hosting Network!";
	}

	public void SetLastGemCount(int gems) 
	{
		_lastGemCount = gems;
	}
	
	public int GetLastGemCount()
	{
		return _lastGemCount;
	}
	
	public String GetScoreboardText() 
	{
		if (ScoreboardString.length() <= DisplayLength)
			return ScoreboardString;
		
		String display = ScoreboardString.substring(ScoreboardIndex, Math.min(ScoreboardIndex+DisplayLength, ScoreboardString.length()));
		
		if (display.length() < DisplayLength && ScoreboardString.length() > DisplayLength)
		{
			int add = DisplayLength - display.length();
			display += ScoreboardString.substring(0, add);
		}
		
		ScoreboardIndex = (ScoreboardIndex + 1) % ScoreboardString.length();
		
		return display;
	}
	
	public String GetPurchaseText(boolean increment) 
	{
		if (PurchaseString.length() <= DisplayLength)
			return PurchaseString;
		
		if (increment)
			PurchaseIndex = (PurchaseIndex + 1) % PurchaseString.length();
		
		String display = PurchaseString.substring(PurchaseIndex, Math.min(PurchaseIndex+DisplayLength, PurchaseString.length()));
		
		if (display.length() < DisplayLength && PurchaseString.length() > DisplayLength)
		{
			int add = DisplayLength - display.length();
			display += PurchaseString.substring(0, add);
		}
		
		return display;
	}

	public String GetUltraText(boolean increment) 
	{
		if (UltraString.length() <= DisplayLength)
			return UltraString;
		
		if (increment)
			UltraIndex = (UltraIndex + 1) % UltraString.length();
		
		String display = UltraString.substring(UltraIndex, Math.min(UltraIndex+DisplayLength, UltraString.length()));
		
		if (display.length() < DisplayLength)
		{
			int add = DisplayLength - display.length();
			display += UltraString.substring(0, add);
		}
		
		return display;
	}
	
	public String GetStaffText(boolean increment) 
	{
		if (StaffString.length() <= DisplayLength)
			return StaffString;
		
		if (increment)
			StaffIndex = (StaffIndex + 1) % StaffString.length();
		
		String display = StaffString.substring(StaffIndex, Math.min(StaffIndex+DisplayLength, StaffString.length()));
		
		if (display.length() < DisplayLength && StaffString.length() > DisplayLength)
		{
			int add = DisplayLength - display.length();
			display += StaffString.substring(0, add);
		}
		
		return display;
	}
	
	public String GetNewsText(boolean increment) 
	{
		if (NewsString.length() <= DisplayLength)
			return NewsString;
		
		if (increment)
			NewsIndex = (NewsIndex + 1) % NewsString.length();
		
		String display = NewsString.substring(NewsIndex, Math.min(NewsIndex+DisplayLength, NewsString.length()));
		
		if (display.length() < DisplayLength && NewsString.length() > DisplayLength)
		{
			int add = DisplayLength - display.length();
			display += NewsString.substring(0, add);
		}
		
		return display;
	}

	public void SetLastCoinCount(int coins)
	{
		_lastCoinCount = coins;
	}
	
	public int GetLastCoinCount()
	{
		return _lastCoinCount;
	}
}
