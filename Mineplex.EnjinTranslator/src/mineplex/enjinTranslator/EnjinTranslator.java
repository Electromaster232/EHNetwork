package mineplex.enjinTranslator;

import mineplex.core.account.CoreClientManager;
import mineplex.core.command.CommandCenter;
import mineplex.core.donation.DonationManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.punish.Punish;
import mineplex.core.updater.Updater;

import org.bukkit.plugin.java.JavaPlugin;

public class EnjinTranslator extends JavaPlugin
{
	private String WEB_CONFIG = "webServer";

	@Override
	public void onEnable()
	{
		getConfig().addDefault(WEB_CONFIG, "http://mplex.endlcdn.site/accounts/");
		getConfig().set(WEB_CONFIG, getConfig().getString(WEB_CONFIG));
		saveConfig();

		//Static Modules
		CommandCenter.Initialize(this);
		
		//Core Modules
		CoreClientManager clientManager = new CoreClientManager(this, GetWebServerAddress());
		CommandCenter.Instance.setClientManager(clientManager);
		
		DonationManager donationManager = new DonationManager(this, clientManager, GetWebServerAddress());
		//Other Modules
		Punish punish = new Punish(this, GetWebServerAddress(), clientManager);
		
		//Main Modules
		new Enjin(this, clientManager, donationManager, new InventoryManager(this, clientManager), punish);
		
		new Updater(this);
	}
	
	public String GetWebServerAddress()
	{
		String webServerAddress = getConfig().getString(WEB_CONFIG);

		return webServerAddress;
	}
}
