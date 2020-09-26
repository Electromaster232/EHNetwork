package ehnetwork.staffServer.salespackage;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.MiniPlugin;
import ehnetwork.core.account.CoreClientManager;
import ehnetwork.core.common.jsonchat.JsonMessage;
import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.donation.DonationManager;
import ehnetwork.core.inventory.InventoryManager;
import ehnetwork.core.stats.StatsManager;
import ehnetwork.staffServer.salespackage.command.DisplayPackageCommand;
import ehnetwork.staffServer.salespackage.command.Sales;
import ehnetwork.staffServer.salespackage.salespackages.AncientChest;
import ehnetwork.staffServer.salespackage.salespackages.ApplyKits;
import ehnetwork.staffServer.salespackage.salespackages.Coins;
import ehnetwork.staffServer.salespackage.salespackages.DefaultRank;
import ehnetwork.staffServer.salespackage.salespackages.EasterBunny;
import ehnetwork.staffServer.salespackage.salespackages.FrostLord;
import ehnetwork.staffServer.salespackage.salespackages.GemHunter;
import ehnetwork.staffServer.salespackage.salespackages.LifetimeHero;
import ehnetwork.staffServer.salespackage.salespackages.LifetimeLegend;
import ehnetwork.staffServer.salespackage.salespackages.LifetimeUltra;
import ehnetwork.staffServer.salespackage.salespackages.MonthlyHero;
import ehnetwork.staffServer.salespackage.salespackages.MonthlyUltra;
import ehnetwork.staffServer.salespackage.salespackages.MythicalChest;
import ehnetwork.staffServer.salespackage.salespackages.OldChest;
import ehnetwork.staffServer.salespackage.salespackages.SalesPackageBase;

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
