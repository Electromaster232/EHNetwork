package mineplex.core.report;

import org.bukkit.ChatColor;

public enum ReportResult
{
	UNDETERMINED(ChatColor.WHITE, "Could not determine"),
	MUTED(ChatColor.YELLOW, "Muted"),
	BANNED(ChatColor.RED, "Banned"),
	ABUSE(ChatColor.DARK_RED, "Abuse of report system");
	
	private ChatColor color;
	private String displayMessage;
	
	private ReportResult(ChatColor color, String displayMessage)
	{
		this.color = color;
		this.displayMessage = displayMessage;
	}
	
	public String toDisplayMessage()
	{
		return color + displayMessage;
	}
}
