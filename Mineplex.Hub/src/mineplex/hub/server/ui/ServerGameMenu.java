package mineplex.hub.server.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import mineplex.core.account.CoreClientManager;
import mineplex.core.common.util.C;
import mineplex.core.donation.DonationManager;
import mineplex.core.itemstack.ItemStackFactory;
import mineplex.core.shop.page.ShopPageBase;
import mineplex.hub.server.ServerManager;
import mineplex.hub.server.ui.button.SelectBETAButton;
import mineplex.hub.server.ui.button.SelectBHButton;
import mineplex.hub.server.ui.button.SelectBLDButton;
import mineplex.hub.server.ui.button.SelectBRButton;
import mineplex.hub.server.ui.button.SelectCSButton;
import mineplex.hub.server.ui.button.SelectDMTButton;
import mineplex.hub.server.ui.button.SelectDOMButton;
import mineplex.hub.server.ui.button.SelectMINButton;
import mineplex.hub.server.ui.button.SelectMSButton;
import mineplex.hub.server.ui.button.SelectMicroButton;
import mineplex.hub.server.ui.button.SelectPLAYERButton;
import mineplex.hub.server.ui.button.SelectSGButton;
import mineplex.hub.server.ui.button.SelectSKYButton;
import mineplex.hub.server.ui.button.SelectSSMButton;
import mineplex.hub.server.ui.button.SelectTDMButton;
import mineplex.hub.server.ui.button.SelectUHCButton;
import mineplex.hub.server.ui.button.SelectWIZButton;

public class ServerGameMenu extends ShopPageBase<ServerManager, QuickShop>
{
	private List<ItemStack> _superSmashCycle = new ArrayList<ItemStack>();
	private List<ItemStack> _minigameCycle = new ArrayList<ItemStack>();
	
	private int _ssmIndex;
	private int _minigameIndex;
	
	public ServerGameMenu(ServerManager plugin, QuickShop quickShop, CoreClientManager clientManager, DonationManager donationManager, String name, Player player)
	{
		super(plugin, quickShop, clientManager, donationManager, name, player, 47);
		
		createSuperSmashCycle();
		createMinigameCycle();
		
		buildPage();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void buildPage()
	{
		setItem(0, ItemStackFactory.Instance.CreateStack(Material.IRON_PICKAXE.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "The Bridges " + C.cGray + "4 Team Survival", new String[] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "4 Teams get 10 minutes to prepare.",
					ChatColor.RESET + "Then the bridges drop, and all hell",
					ChatColor.RESET + "breaks loose as you battle to the",
					ChatColor.RESET + "death with the other teams.",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("BR") + ChatColor.RESET + " other players!",
				}));
		
		setItem(2, ItemStackFactory.Instance.CreateStack(Material.IRON_SWORD.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Survival Games " + C.cGray + "Last Man Standing", new String[] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Search for chests to find loot and ",
					ChatColor.RESET + "fight others to be the last man standing. ",
					ChatColor.RESET + "Stay away from the borders!",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("HG") + ChatColor.RESET + " other players!",
				}));

		setItem(4, ItemStackFactory.Instance.CreateStack(Material.BEDROCK.getId(), (byte) 0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Skywars " + C.cGray + "Solo Survival", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "16 contenders fight for the right to rule the skies!",
						ChatColor.RESET + "Spawn on a sky island and build your path!",
						ChatColor.RESET + "Find weapons to take your enemies down!",
						ChatColor.RESET + "Way up there, death ever looming if you fall..",
						ChatColor.RESET + "Can you fight? Can you live? Can you win Skywars?",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("SKY") + ChatColor.RESET + " other players!",
				}));

		setItem(6, ItemStackFactory.Instance.CreateStack(Material.GOLDEN_APPLE.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "UHC " + C.cGray + "Ultra Hardcore Mode", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "Extremely hard team-based survival ",
						ChatColor.RESET + "Gather materials and fight your way",
						ChatColor.RESET + "to become the last team standing!",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("UHC") + ChatColor.RESET + " other players!",
				}));

		setItem(8, ItemStackFactory.Instance.CreateStack(Material.BLAZE_ROD.getId(), (byte) 0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Wizards " + C.cGray + "Last Man Standing", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "Wield powerful spells to fight",
						ChatColor.RESET + "against other players in this",
						ChatColor.RESET + "exciting free-for-all brawl!",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("WIZ") + ChatColor.RESET + " other players!",
				}));

		setItem(18, ItemStackFactory.Instance.CreateStack(Material.DIAMOND_CHESTPLATE, (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Castle Siege " + C.cGray + "Team Game", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "Defenders must protect King Sparklez",
						ChatColor.RESET + "from the endless waves of Undead",
						ChatColor.RESET + "until the sun rises!",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("CS") + ChatColor.RESET + " other players!",
				}));

		setItem(20, ItemStackFactory.Instance.CreateStack(Material.GRASS.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Block Hunt " + C.cGray + "Cat and Mouse", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "Hide as blocks/animals, upgrade your ",
						ChatColor.RESET + "weapon and fight to survive against",
						ChatColor.RESET + "the Hunters!",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("BH") + ChatColor.RESET + " other players!",
				}));

		setItem(22, _superSmashCycle.get(_ssmIndex));

		setItem(24, ItemStackFactory.Instance.CreateStack(Material.TNT.getId(), (byte) 0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Mine-Strike " + C.cGray + "Team Survival", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "One team must defend two bomb sites from",
						ChatColor.RESET + "the other team, who are trying to plant a bomb",
						ChatColor.RESET + "and blow them up!",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("MS") + ChatColor.RESET + " other players!",
				}));
		
		setItem(26, ItemStackFactory.Instance.CreateStack(Material.BOOK_AND_QUILL.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Draw My Thing " + C.cGray + "Pictionary!", new String[]
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Players take turns at drawing a random",
					ChatColor.RESET + "word. Whoever guesses it within the time",
					ChatColor.RESET + "limit gets some points!",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("DMT") + ChatColor.RESET + " other players!",
				}));

		setItem(31, ItemStackFactory.Instance.CreateStack(Material.FEATHER.getId(), (byte)0, getPlugin().getGroupTagPlayerCount("MICRO") + 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Micro Games " + C.cGray + "Quick Action", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "Fast-paced gameplay with 23 other players.",
						ChatColor.RESET + "Each game lasts less than 5 minutes each,",
						ChatColor.RESET + "and there is no waiting lobby in between games!",
						ChatColor.RESET + "",
						ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("MICRO") + ChatColor.RESET + " other players!",
				}));
		
		setItem(36, ItemStackFactory.Instance.CreateStack(Material.BEACON.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Dominate " + C.cGray + "Team Game", new String[]
				{
					ChatColor.RESET + "",
					ChatColor.RESET + "Customize one of five exciting champions",
					ChatColor.RESET + "and battle with the opposing team for the",
					ChatColor.RESET + "control points on the map.",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("DOM") + ChatColor.RESET + " other players!",
				}));
		
		setItem(38, ItemStackFactory.Instance.CreateStack(Material.GOLD_SWORD.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Team Deathmatch " + C.cGray + "Team Game", new String[]
				{
					ChatColor.RESET + "",
					ChatColor.RESET + "Customize one of five exciting champions",
					ChatColor.RESET + "and battle with the opposing team to the",
					ChatColor.RESET + "last man standing.",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("TDM") + ChatColor.RESET + " other players!",
				}));
		
		setItem(40, ItemStackFactory.Instance.CreateStack(Material.WOOD.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Master Builders " + C.cGray + "Creative Build", new String[]
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Players are given a Build Theme and ",
					ChatColor.RESET + "must use blocks, monsters and more",
					ChatColor.RESET + "to create a masterpiece!",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("BLD") + ChatColor.RESET + " other players!",
				}));
		
		setItem(42, _minigameCycle.get(_minigameIndex));

		setItem(44, ItemStackFactory.Instance.CreateStack(Material.SKULL_ITEM.getId(), (byte) 3, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Player Servers " + C.cGray + "Player Hosted Games", new String[]
				{
						ChatColor.RESET + "",
						ChatColor.RESET + "Join your friends in their own ",
						ChatColor.RESET + "Mineplex Player Server. You can play",
						ChatColor.RESET + "the games you want, when you want.",
						ChatColor.RESET + "",
				}));

		getButtonMap().put(0, new SelectBRButton(this));
		getButtonMap().put(2, new SelectSGButton(this));
		getButtonMap().put(4, new SelectSKYButton(this));
		getButtonMap().put(6, new SelectUHCButton(this));
		getButtonMap().put(8, new SelectWIZButton(this));
				
		getButtonMap().put(18, new SelectCSButton(this));
		getButtonMap().put(20, new SelectBHButton(this));
		getButtonMap().put(22, new SelectSSMButton(this));
		getButtonMap().put(24, new SelectMSButton(this));
		getButtonMap().put(26, new SelectDMTButton(this));
		getButtonMap().put(31, new SelectMicroButton(this));
		getButtonMap().put(36, new SelectDOMButton(this));
		getButtonMap().put(38, new SelectTDMButton(this));
		getButtonMap().put(40, new SelectBLDButton(this));
		getButtonMap().put(42, new SelectMINButton(this));
		getButtonMap().put(44, new SelectPLAYERButton(this));
//		getButtonMap().put(44, new SelectBETAButton(this));
	}

	@SuppressWarnings("deprecation")
	private void createMinigameCycle()
	{
		int playerCount = getPlugin().getGroupTagPlayerCount("MIN") +
						  getPlugin().getGroupTagPlayerCount("DR") +
						  getPlugin().getGroupTagPlayerCount("DE") + 
						  getPlugin().getGroupTagPlayerCount("PB") + 
						  getPlugin().getGroupTagPlayerCount("TF") + 
						  getPlugin().getGroupTagPlayerCount("RUN") + 
						  getPlugin().getGroupTagPlayerCount("SN") + 
						  getPlugin().getGroupTagPlayerCount("DT") + 
						  getPlugin().getGroupTagPlayerCount("SQ") +
						  getPlugin().getGroupTagPlayerCount("SA") +
						  getPlugin().getGroupTagPlayerCount("SS") + 
						  getPlugin().getGroupTagPlayerCount("OITQ");
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(98, (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(Material.GOLD_BOOTS.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(122, (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(Material.BOW, (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(Material.LEATHER_BOOTS.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(Material.MILK_BUCKET.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(Material.MILK_BUCKET.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(Material.DIAMOND_BARDING.getId(), (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(159, (byte)14, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(309, (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Death Tag",
					ChatColor.RESET + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
		
		_minigameCycle.add(ItemStackFactory.Instance.CreateStack(319, (byte)0, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Arcade " + C.cGray + "Mixed Games", new String [] 
				{ 
					ChatColor.RESET + "",
					ChatColor.RESET + "Play all of these fun minigames:",
					ChatColor.RESET + "",
					ChatColor.RESET + "Super Spleef",
					ChatColor.RESET + "Runner",
					ChatColor.RESET + "Dragons",
					ChatColor.RESET + "One in the Quiver",
					ChatColor.RESET + "Dragon Escape",
					ChatColor.RESET + "Sneaky Assassins",
					ChatColor.RESET + "Micro Battle",
					ChatColor.RESET + "Super Paintball",
					ChatColor.RESET + "Turf Wars",
					ChatColor.RESET + "Death Tag",
					ChatColor.RESET + C.Bold + ChatColor.GREEN + "Bacon Brawl",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + playerCount + ChatColor.RESET + " other players!",
					}));
	}

	private void createSuperSmashCycle()
	{
		String[] desc = new String[] 
				{
					ChatColor.RESET + "",
					ChatColor.RESET + "Pick from a selection of monsters,",
					ChatColor.RESET + "then battle other players to the ",
					ChatColor.RESET + "death with your monsters skills!",
					ChatColor.RESET + "",
					ChatColor.RESET + "Join " + ChatColor.GREEN + getPlugin().getGroupTagPlayerCount("SSM") + ChatColor.RESET + " other players!",
				};
		
		_superSmashCycle.add(ItemStackFactory.Instance.CreateStack(397, (byte)4, 1, ChatColor.RESET + C.Bold + ChatColor.YELLOW + "Super Smash Mobs", desc));
	}

	public void Update()
	{
		_ssmIndex++;
		_minigameIndex++;
		
		if (_ssmIndex >= _superSmashCycle.size())
			_ssmIndex = 0;
		
		if (_minigameIndex >= _minigameCycle.size())
			_minigameIndex = 0;
		
		buildPage();
	}

	public void OpenMIN(Player player)
	{
		getPlugin().getMixedArcadeShop().attemptShopOpen(player);
	}

	public void OpenSSM(Player player)
	{
		getPlugin().getSuperSmashMobsShop().attemptShopOpen(player);
	}

	public void OpenDOM(Player player)
	{
		getPlugin().getDominateShop().attemptShopOpen(player);
	}

	public void openCS(Player player)
	{
		getPlugin().getCastleSiegeShop().attemptShopOpen(player);
	}
	
	public void OpenBR(Player player)
	{
		getPlugin().getBridgesShop().attemptShopOpen(player);
	}
	
	public void OpenBH(Player player)
	{
		getPlugin().getBlockHuntShop().attemptShopOpen(player);
	}

	public void OpenSG(Player player)
	{
		getPlugin().getSurvivalGamesShop().attemptShopOpen(player);
	}

	public void openDMT(Player player)
	{
		getPlugin().getDrawMyThingShop().attemptShopOpen(player);
	}

	public void OpenTDM(Player player)
	{
		getPlugin().getTeamDeathmatchShop().attemptShopOpen(player);
	}

	public void OpenMicro(Player player){getPlugin().getMicroShop().attemptShopOpen(player);}

	public void openMS(Player player)
	{
		getPlugin().getMinestrikeShop().attemptShopOpen(player);
	}

	public void OpenWIZ(Player player)
	{
		getPlugin().getWizardShop().attemptShopOpen(player);
	}

	public void OpenBLD(Player player)
	{
		getPlugin().getBuildShop().attemptShopOpen(player);
	}

	public void openBeta(Player player)
	{
		getPlugin().getBetaShop().attemptShopOpen(player);
	}

	public void openUHC(Player player)
	{
		getPlugin().getUHCShop().attemptShopOpen(player);
	}

	public void openSKY(Player player)
	{
		getPlugin().getSKYShop().attemptShopOpen(player);
	}

	public void openPlayerGames(Player player)
	{
		getPlugin().getPlayerGamesShop().attemptShopOpen(player);
	}
}
