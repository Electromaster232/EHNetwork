package mineplex.enjinTranslator;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import mineplex.core.MiniPlugin;
import mineplex.core.account.CoreClient;
import mineplex.core.account.CoreClientManager;
import mineplex.core.common.Rank;
import mineplex.core.common.util.Callback;
import mineplex.core.common.util.NautHashMap;
import mineplex.core.common.util.UUIDFetcher;
import mineplex.core.donation.DonationManager;
import mineplex.core.inventory.InventoryManager;
import mineplex.core.punish.Category;
import mineplex.core.punish.Punish;
import mineplex.core.server.util.TransactionResponse;
import mineplex.core.updater.UpdateType;
import mineplex.core.updater.event.UpdateEvent;

public class Enjin extends MiniPlugin implements CommandExecutor
{
	private CoreClientManager _clientManager;
	private DonationManager _donationManager;
	private InventoryManager _inventoryManager;
	private Punish _punish;
	
	private TempRepository _repository;
	
	private NautHashMap<String, Entry<UUID, Long>> _cachedUUIDs = new NautHashMap<String, Entry<UUID, Long>>();
	private List<QueuedCommand> _commandQueue = new ArrayList<QueuedCommand>();
	private static Object _commandLock = new Object();
	
	public long _lastPoll = System.currentTimeMillis() - 120000;
	
	private SimpleDateFormat _dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	
	public Enjin(JavaPlugin plugin, CoreClientManager clientManager, DonationManager donationManager, InventoryManager inventoryManager, Punish punish)
	{
		super("Enjin", plugin);
		
		_clientManager = clientManager;
		_donationManager = donationManager;
		_inventoryManager = inventoryManager;

		_punish = punish;
		_repository = new TempRepository(plugin);
		
		plugin.getCommand("enjin_mineplex").setExecutor(this);
		plugin.getCommand("pull").setExecutor(this);
	}
	
	@EventHandler
	public void expireCachedUUIDs(UpdateEvent event)
	{
		if (event.getType() != UpdateType.MIN_01)
			return;
		
		for (Iterator<Entry<String, Entry<UUID, Long>>> iterator = _cachedUUIDs.entrySet().iterator(); iterator.hasNext();)
		{
			Entry<String, Entry<UUID, Long>> entry = iterator.next();
			
			if (System.currentTimeMillis() > entry.getValue().getValue())
				iterator.remove();
		}
	}
	
	@EventHandler
	public void processCommandQueue(UpdateEvent event)
	{
		if (event.getType() != UpdateType.MIN_01 || _commandQueue.size() == 0)
			return;
		
		List<QueuedCommand> commandCopyQueue = new ArrayList<QueuedCommand>();
		
		synchronized (_commandLock)
		{
			for (QueuedCommand command : _commandQueue)
				commandCopyQueue.add(command);
			
			 _commandQueue.clear();
		}
		
		System.out.println("=====] Processing queued commands [=====");
		for (QueuedCommand command : commandCopyQueue)
		{
			try
			{
				onCommand(command.Sender, command.Command, command.Label, command.Args);
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		}
		System.out.println("========================================");
	}
	
	@EventHandler
	public void pollLastPurchases(UpdateEvent event)
	{
		if (event.getType() != UpdateType.MIN_01)
			return;
		
		/*
		@SuppressWarnings("serial")
		List<EnjinPurchase> purchases = new JsonWebCall("http://theendlessweb.com/api/m-shopping-purchases/m/14702725").Execute(new TypeToken<List<EnjinPurchase>>(){}.getType(), null);
		_lastPoll = System.currentTimeMillis();
		
		int i = 0;
		for (EnjinPurchase purchase : purchases)
		{
			if (i > 10)
				break;
			
			purchase.logInfoToConsole();
			i++;
		}
		*/
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		synchronized (_commandLock)
		{
			try
			{
				if (sender instanceof Player)
					((Player)sender).kickPlayer("Like bananas? I don't.  Here take these and go have fun.");
		
				if (label.equalsIgnoreCase("enjin_mineplex"))
				{
					final String name = args[1];
					
					_clientManager.loadClientByName(name, new Runnable()
					{
						public void run()
						{
							final CoreClient client = _clientManager.Get(name);
							
							if (client == null)
							{
								System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + ", isn't in our database.");
							}
							else
							{
								UUID uuid = null;
								
								if (_cachedUUIDs.containsKey(name))
									uuid = _cachedUUIDs.get(name).getKey();
								else
								{
									// Fails if not in DB and if duplicate.
									uuid = _clientManager.loadUUIDFromDB(name);
									
									if (uuid == null)
										uuid = UUIDFetcher.getUUIDOf(name);
								}
								
								if (uuid == null)
								{
									System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + ", no UUID.");
									return;
								}
								
								final UUID playerUUID = uuid;
								
								_cachedUUIDs.put(name, new AbstractMap.SimpleEntry<UUID, Long>(playerUUID, System.currentTimeMillis() + 240000));
								
								if (args.length == 3 && args[0].equalsIgnoreCase("gem"))
								{			
									final int amount = Integer.parseInt(args[2]);
									
									_donationManager.RewardGems(new Callback<Boolean>()
									{
										public void run (Boolean response)
										{
											if (response)
											{
												System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " gems.");
											}
											else
											{
												_commandQueue.add(new QueuedCommand(sender, command, label, args));
												System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " gems.  Queuing for run later.");
											}
										}
									}, "purchase", name, playerUUID, amount);
								}		
								else if (args.length == 3 && args[0].equalsIgnoreCase("coin"))
								{			
									final int amount = Integer.parseInt(args[2]);

									_donationManager.RewardCoins(new Callback<Boolean>()
									{
										public void run (Boolean response)
										{
											if (response)
											{
												System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " coins.");
											}
											else
											{
												_commandQueue.add(new QueuedCommand(sender, command, label, args));
												System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " coins.  Queuing for run later.");
											}
										}
									}, "purchase", name, client.getAccountId(), amount);
								}
								else if (args.length == 3 && args[0].equalsIgnoreCase("booster"))
								{			
									int amount = Integer.parseInt(args[2]);
									
									_donationManager.PurchaseUnknownSalesPackage(null, name, client.getAccountId(), "Gem Booster " + amount, false, 0, false);
									_repository.addGemBooster(name, amount);
									System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " Gem Boosters" + ".");
								}
								else if (args.length >= 3 && args[0].equalsIgnoreCase("key"))
								{
									final int amount = Integer.parseInt(args[2]);
									
									if (args.length == 4)
									{
										_inventoryManager.addItemToInventoryForOffline(new Callback<Boolean>()
										{
											public void run(Boolean success)
											{
												if (success)
													System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " Treasure Keys" + ".");
												else
												{
													// Add arg so we don't add back to windows api call
													_commandQueue.add(new QueuedCommand(sender, command, label, new String[] { args[0], args[1], args[2], "noaccountchange" }));
													System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " Treasure Keys.  Queuing for run later.");
												}
											}
										}, playerUUID, "Treasure", "Treasure Key", amount);
									}
									else
									{
										_donationManager.PurchaseUnknownSalesPackage(new Callback<TransactionResponse>()
										{
											public void run(TransactionResponse data)
											{
												if (data == TransactionResponse.Success)
												{
													_inventoryManager.addItemToInventoryForOffline(new Callback<Boolean>()
													{
														public void run(Boolean success)
														{
															if (success)
																System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " Treasure Keys" + ".");
															else
															{
																// Add arg so we don't add back to windows api call
																_commandQueue.add(new QueuedCommand(sender, command, label, new String[] { args[0], args[1], args[2], "noaccountchange" }));
																System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " Treasure Keys.  Queuing for run later.");
															}
														}
													}, playerUUID, "Treasure", "Treasure Key", amount);
												}
												else
												{
													_commandQueue.add(new QueuedCommand(sender, command, label, args));
													System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " Treasure Keys.  Queuing for run later.");
												}
											}
										}, name, client.getAccountId(), "Treasure Key " + amount, false, 0, false);
									}
								}
								else if (args.length >= 3 && args[0].equalsIgnoreCase("chest"))
								{			
									final int amount = Integer.parseInt(args[2]);
									
									if (args.length == 4)
									{
										_inventoryManager.addItemToInventoryForOffline(new Callback<Boolean>()
										{
											public void run(Boolean success)
											{
												if (success)
													System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " Treasure Chests" + ".");
												else
												{
													// Add arg so we don't add back to windows api call
													_commandQueue.add(new QueuedCommand(sender, command, label, new String[] { args[0], args[1], args[2], "noaccountchange" }));
													System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " Treasure Chests.  Queuing for run later.");
												}
											}
										}, playerUUID, "Utility", "Treasure Chest", amount);
									}
									else
									{
										_donationManager.PurchaseUnknownSalesPackage(new Callback<TransactionResponse>()
										{
											public void run(TransactionResponse data)
											{
												_inventoryManager.addItemToInventoryForOffline(new Callback<Boolean>()
												{
													public void run(Boolean success)
													{
														if (success)
															System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " Treasure Chests" + ".");
														else
														{
															// Add arg so we don't add back to windows api call
															_commandQueue.add(new QueuedCommand(sender, command, label, new String[] { args[0], args[1], args[2], "noaccountchange" }));
															System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " Treasure Chests.  Queuing for run later.");
														}
													}
												}, playerUUID, "Utility", "Treasure Chest", amount);
											}
										}, name, client.getAccountId(), "Treasure Chest " + amount, false, 0, false);
									}
								}
								else if (args.length == 4 && args[0].equalsIgnoreCase("rank"))
								{
									final Rank rank = mineplex.core.common.Rank.valueOf(args[2]);
									final boolean perm = Boolean.parseBoolean(args[3]);
									
									_clientManager.loadClientByName(name, new Runnable()
									{
										public void run()
										{
											if (_clientManager.Get(name).GetRank() == Rank.ALL || !_clientManager.Get(name).GetRank().Has(rank) || _clientManager.Get(name).GetRank() == rank)
											{
												_clientManager.SaveRank(name, playerUUID, rank, perm);
												System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + rank + " " + (perm ? "permanently." : "for 1 month."));									
											}
											else
											{
												System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " DENIED INFERIOR " + rank + " " + (perm ? "permanently." : "for 1 month."));
											}
										}
									});
								}
								else if (args.length >= 3 && args[0].equalsIgnoreCase("purchase"))
								{
									final int amount = Integer.parseInt(args[2]);
									final String category = args[3];
									String tempName = args[4];
									
									for (int i = 5; i < args.length; i++)
									{
										tempName += " " + args[i];
									}
									
									final String packageName = tempName;
									
									_donationManager.PurchaseUnknownSalesPackage(new Callback<TransactionResponse>()
									{
										public void run(TransactionResponse data)
										{
											if (data == TransactionResponse.Success)
											{
												_inventoryManager.addItemToInventoryForOffline(new Callback<Boolean>()
												{
													public void run(Boolean success)
													{
														if (success)
															System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " received " + amount + " " + packageName + ".");
														else
														{
															// Add arg so we don't add back to windows api call
															_commandQueue.add(new QueuedCommand(sender, command, label, new String[] { args[0], args[1], args[2], "noaccountchange" }));
															System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + packageName + ".  Queuing for run later.");
														}
													}
												}, playerUUID, category, packageName, amount);
											}
											else
											{
												_commandQueue.add(new QueuedCommand(sender, command, label, args));
												System.out.println("[" + _dateFormat.format(new Date()) + "] ERROR processing " + name + " " + amount + " Treasure Keys.  Queuing for run later.");
											}
										}
									}, name, client.getAccountId(), packageName, false, 0, false);
								}
								else if (args.length >= 3 && args[0].equalsIgnoreCase("unban"))
								{			
									String reason = args[2];
									
									for (int i = 3; i < args.length; i++)
									{
										reason += " " + args[i];
									}
									
									_punish.RemoveBan(name, reason);
									System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " unbanned for " + reason);
								}
								else if (args.length >= 3 && args[0].equalsIgnoreCase("ban"))
								{				
									String reason = args[2];
									
									for (int i = 3; i < args.length; i++)
									{
										reason += " " + args[i];
									}
									
									_punish.AddPunishment(name, Category.Other, reason, null, 3, true, -1);
									System.out.println("[" + _dateFormat.format(new Date()) + "] " + name + " banned for " + reason);
								}
							}
						}
					});
				}
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
			
			try
			{
				Thread.sleep(20);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		return true;
	}
}
