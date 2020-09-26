package ehnetwork.core.benefit.benefits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ehnetwork.core.benefit.BenefitManager;
import ehnetwork.core.benefit.BenefitManagerRepository;
import ehnetwork.core.common.util.Callback;

public abstract class BenefitBase
{
	private BenefitManager _plugin;
	private String _name;
	private BenefitManagerRepository _repository;
	
	protected BenefitBase(BenefitManager plugin, String name, BenefitManagerRepository repository)
	{
		_plugin = plugin;
		_name = name;
		_repository = repository;
	}
	
	public JavaPlugin getPlugin()
	{
		return _plugin.getPlugin();
	}
	
	public BenefitManagerRepository getRepository()
	{
		return _repository;
	}
	
	public abstract void rewardPlayer(Player player);
	
	public void recordBenefit(final Player player, final Callback<Boolean> callback)
	{
		Bukkit.getServer().getScheduler().runTaskAsynchronously(_plugin.getPlugin(), new Runnable() 
		{
			public void run()
			{
				boolean success = _repository.addBenefit(_plugin.getClientManager().Get(player).getAccountId(), _name);
				
				callback.run(success);
			}
		});
	}

	public String getName()
	{
		return _name;
	}
}
