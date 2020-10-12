package ehnetwork.core.elo;

public class EloPlayer
{
	public String UniqueId;
	public int Rating;
	
	public void printInfo()
	{
		System.out.println(UniqueId + "'s elo is " + Rating);
	}
}
