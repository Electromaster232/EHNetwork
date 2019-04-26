package mineplex.staffServer.salespackage;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.jsonchat.JsonMessage;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.donation.DonationManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.stats.StatsManager;
import mineplex.staffServer.salespackage.command.DisplayPackageCommand;
import mineplex.staffServer.salespackage.command.Sales;
import mineplex.staffServer.salespackage.salespackages.AncientChest;
import mineplex.staffServer.salespackage.salespackages.ApplyKits;
import mineplex.staffServer.salespackage.salespackages.Coins;
import mineplex.staffServer.salespackage.salespackages.DefaultRank;
import mineplex.staffServer.salespackage.salespackages.EasterBunny;
import mineplex.staffServer.salespackage.salespackages.FrostLord;
import mineplex.staffServer.salespackage.salespackages.GemHunter;
import mineplex.staffServer.salespackage.salespackages.LifetimeHero;
import mineplex.staffServer.salespackage.salespackages.LifetimeLegend;
import mineplex.staffServer.salespackage.salespackages.LifetimeUltra;
import mineplex.staffServer.salespackage.salespackages.MonthlyHero;
import mineplex.staffServer.salespackage.salespackages.MonthlyUltra;
import mineplex.staffServer.salespackage.salespackages.MythicalChest;
import mineplex.staffServer.salespackage.salespackages.SalesPackageBase;
import mineplex.staffServer.salespackage.salespackages.OldChest;

public class SalesPackageManager extends MiniPlugin
{
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private InventoryManager _inventoryManager;
	private StatsManager _statsManager;
	
	private NautHashMap<String, SalesPackageBase> _salesPackages = new NautHashMap<String, SalesPackageBase>();
	
	public SalesPackageManager(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, InventoryManager inventoryManager, StatsManager statsManager)
	{
		super("SalesPackageManager", plugin);
		
		_clientManager = clientManager;
		_donationManager = donationManager;
		_inventoryManager = inventoryManager;
		_statsManager = statsManager;
		
		AddSalesPackage(new Coins(this, 5000));
		AddSalesPackage(new Coins(this, 25000));
		AddSalesPackage(new Coins(this, 75000));
		AddSalesPackage(new MonthlyUltra(this));
		AddSalesPackage(new MonthlyHero(this));
		AddSalesPackage(new DefaultRank(this));
		AddSalesPackage(new LifetimeUltra(this));
		AddSalesPackage(new LifetimeHero(this));
		AddSalesPackage(new LifetimeLegend(this));
		AddSalesPackage(new GemHunter(this, 4));
		AddSalesPackage(new GemHunter(this, 8));
		AddSalesPackage(new ApplyKits(this));
		AddSalesPackage(new OldChest(this));
		AddSalesPackage(new AncientChest(this));
		AddSalesPackage(new MythicalChest(this));
		AddSalesPackage(new FrostLord(this));
		AddSalesPackage(new EasterBunny(this));
	}
	
	private void AddSalesPackage(SalesPackageBase salesPackage)
	{
		_salesPackages.put(salesPackage.getName(), salesPackage);
	}

	@Override
	public void addCommands()
	{
		addCommand(new DisplayPackageCommand(this));
		addCommand(new Sales(this));
	}

	public DonationManager getDonationManager()
	{
		return _donationManager;
	}

	public CoreClientManager getClientManager()
	{
		return _clientManager;
	}

	public InventoryManager getInventoryManager()
	{
		return _inventoryManager;
	}

	public void help(Player player)
	{
	}

	public void displayPackage(Player caller, String playerName, String packageName)
	{
		_salesPackages.get(packageName).displayToAgent(caller, playerName);
	}

	public void displaySalesPackages(Player caller, String playerName)
	{
		JsonMessage coinBuilder = new JsonMessage("Coins : ").color("blue");
		JsonMessage packageBuilder = new JsonMessage("Rank Packages : ").color("blue");
		JsonMessage chestBuilder = new JsonMessage("Chest Packages : ").color("blue");
		
		for (SalesPackageBase salesPackage : _salesPackages.values())
		{
			if (salesPackage instanceof Coins)
			{
				coinBuilder = coinBuilder.extra("[").color("gray").extra(salesPackage.getName()).color("green").click("run_command", "/display " + playerName + " " + salesPackage.getName()).extra("] ").color("gray");
			}
			else if (salesPackage instanceof MythicalChest || salesPackage instanceof AncientChest || salesPackage instanceof OldChest)
			{
				chestBuilder = chestBuilder.extra("[").color("gray").extra(salesPackage.getName()).color("green").click("run_command", "/display " + playerName + " " + salesPackage.getName()).extra("] ").color("gray");
			}
			else
			{
				packageBuilder = packageBuilder.extra("[").color("gray").extra(salesPackage.getName()).color("green").click("run_command", "/display " + playerName + " " + salesPackage.getName()).extra("] ").color("gray");
			}
		}

		coinBuilder.sendToPlayer(caller);
		chestBuilder.sendToPlayer(caller);
		packageBuilder.sendToPlayer(caller);
	}

	public StatsManager getStatsManager()
	{
		return _statsManager;
	}
}
