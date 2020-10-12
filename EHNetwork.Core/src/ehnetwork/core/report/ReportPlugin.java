package ehnetwork.core.report;

import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.report.command.ReportCloseCommand;
import ehnetwork.core.report.command.ReportCommand;
import ehnetwork.core.report.command.ReportHandleCommand;

public class ReportPlugin extends MiniPlugin 
{

	private static JavaPlugin instance;
	public static JavaPlugin getPluginInstance() { return instance; }
	
	public ReportPlugin(JavaPlugin plugin, String serverName)
	{
		super("ReportPlugin", plugin);
		
		instance = plugin;
	}
	
	@Override
	public void addCommands()
	{
		addCommand(new ReportCommand(this));
		addCommand(new ReportHandleCommand(this));
		addCommand(new ReportCloseCommand(this));
		//AddCommand(new ReportDebugCommand(this));
	}
}
