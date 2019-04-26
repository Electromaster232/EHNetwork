package mineplex.core.punish;

public enum Category
{
	ChatOffense,
	Advertisement, // No longer used
	Exploiting, // General Offense
	Hacking, // Illegal Mods
	Warning,
	PermMute,
	Other; // Represents perm ban - (or old perm mutes)
	
    public static boolean contains(String s) 
    {
        try 
        {
        	Category.valueOf(s);
            return true;
        } 
        catch (Exception e) 
        {
            return false;
        }
     }
}
