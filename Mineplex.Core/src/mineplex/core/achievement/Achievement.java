package mineplex.core.achievement;

import mineplex.core.common.util.C;

public enum Achievement
{
	GLOBAL_MINEPLEX_LEVEL("Mineplex Level", 20000,
			new String[]{"Global.ExpEarned"},
			new String[]{"Level up by doing well in games!"},
			getExperienceLevels(),
			AchievementCategory.GLOBAL),
	
	GLOBAL_GEM_HUNTER("Gem Hunter", 10000,
			new String[]{"Global.GemsEarned"},
			new String[]{"+1 for every Gem earned in any game."},
			new int[]{10000, 15000, 20000, 25000, 30000, 35000, 40000, 45000, 50000, 55000, 60000, 65000},
			AchievementCategory.GLOBAL),

	//Bridges
	BRIDGES_WINS("Bridge Champion", 600,
			new String[]{"The Bridges.Wins"},
			new String[]{"Win 30 games of The Bridges"},
			new int[]{30},
			AchievementCategory.BRIDGES),

	BRIDGES_FOOD("Food for the Masses", 600,
			new String[]{"The Bridges.FoodForTheMasses"},
			new String[]{"Get 20 kills with Apples"},
			new int[]{20},
			AchievementCategory.BRIDGES),

	BRIDGES_SNIPER("Sniper", 600,
			new String[]{"The Bridges.Sniper"},
			new String[]{"Kill an enemy with Archery before Bridges fall"},
			new int[]{1},
			AchievementCategory.BRIDGES),

	BRIDGES_FORTUNE_BOMBER("Fortune Bomber", 600,
			new String[]{"The Bridges.FortuneBomber"},
			new String[]{"Mine 30 Diamond Ore using TNT"},
			new int[]{30},
			AchievementCategory.BRIDGES),

	BRIDGES_RAMPAGE("Rampage", 1200,
			new String[]{"The Bridges.Rampage"},
			new String[]{"Get 4 kills in a row, with no more than", "10 seconds between each kill"},
			new int[]{1},
			AchievementCategory.BRIDGES),

	BRIDGES_DEATH_BOMBER("Death Bomber", 1000,
			new String[]{"The Bridges.DeathBomber"},
			new String[]{"Get 5 kills in a single game with TNT"},
			new int[]{1},
			AchievementCategory.BRIDGES),

	//Survival Games
	SURVIVAL_GAMES_WINS("Katniss Everdeen", 600,
			new String[]{"Survival Games.Wins"},
			new String[]{"Win 20 games of Survival Games"},
			new int[]{30},
			AchievementCategory.SURVIVAL_GAMES),

	SURVIVAL_GAMES_LIGHT_WEIGHT("Light Weight", 1000,
			new String[]{"Survival Games.NoArmor"},
			new String[]{"Win a game without wearing any armor"},
			new int[]{1},
			AchievementCategory.SURVIVAL_GAMES),

	SURVIVAL_GAMES_BLOODLUST("Bloodlust", 1200,
			new String[]{"Survival Games.Bloodlust"},
			new String[]{"Kill 3 other players in the first minute"},
			new int[]{1},
			AchievementCategory.SURVIVAL_GAMES),

	SURVIVAL_GAMES_LOOT("Loot Hoarder", 600,
			new String[]{"Survival Games.SupplyDropsOpened"},
			new String[]{"Be the first to open 50 Supply Drops"},
			new int[]{50},
			AchievementCategory.SURVIVAL_GAMES),

	SURVIVAL_GAMES_SKELETONS("Skeletal Army", 1000,
			new String[]{"Survival Games.Skeletons"},
			new String[]{"Have 5 Necromanced Skeletons alive"},
			new int[]{1},
			AchievementCategory.SURVIVAL_GAMES),
	
	//Skywars
	SKYWARS_WINS("Sky King",2000,
			new String[]{"Skywars.Wins"},
			new String[]{"Win 20 Games of Skywars"},
			new int[]{30},
			AchievementCategory.SKYWARS),
			
	SKYWARS_BOMBER("Master Bomber",500,
			new String[]{"Skywars.DeathBomber"},
			new String[]{"Get 3 kills with \"Super Throwing TNT\"", " in a single game."},
			new int[]{1},
			AchievementCategory.SKYWARS),
					
	SKYWARS_TNT("TNT Hoarder",250,
			new String[]{"Skywars.BombPickups"},
			new String[]{"Pickup 100 \"Super Throwing TNT\"s"},
			new int[]{100},
			AchievementCategory.SKYWARS),	
			
	SKYWARS_ZOMBIE_KILLS("Left For Dead",750,
			new String[]{"Skywars.ZombieKills"},
			new String[]{"Kill 120 Zombies"},
			new int[]{120},
			AchievementCategory.SKYWARS),
			
	SKYWARS_PLAYER_KILLS("Cold Blooded Killer",500,
			new String[]{"Skywars.Kills"},
			new String[]{"Kill 80 Players"},
			new int[]{80},
			AchievementCategory.SKYWARS),
			
	SKYWARS_NOCHEST("Survivalist",1000,
			new String[]{"Skywars.NoChest"},
			new String[]{"Win a Game Without Opening a Chest"},
			new int[]{1},
			AchievementCategory.SKYWARS),
			
	SKYWARS_NOARMOR("Bare Minimum",1000,
			new String[]{"Skywars.NoArmor"},
			new String[]{"Win a Game With No Armor"},
			new int[]{1},
			AchievementCategory.SKYWARS),
			
			

	//UHC
	UHC_WINS("Ultimate Winner", 600,
			new String[]{"Ultra Hardcore.Wins"},
			new String[]{"Win 10 games of Ultra Hardcore"},
			new int[]{10},
			AchievementCategory.UHC),
			
	//UHC
	WIZARDS_WINS("Supreme Wizard", 600,
			new String[]{"Wizards.Wins"},
			new String[]{"Win 50 games of Wizards"},
			new int[]{50},
			AchievementCategory.WIZARDS),

	//Smash Mobs
	SMASH_MOBS_WINS("SO SUPER!", 600,
			new String[]{"Super Smash Mobs.Wins"},
			new String[]{"Win 100 games of Super Smash Mobs"},
			new int[]{100},
			AchievementCategory.SMASH_MOBS),

	SMASH_MOBS_MLG_PRO("MLG Pro", 1200,
			new String[]{"Super Smash Mobs.MLGPro"},
			new String[]{"Win a game without dying"},
			new int[]{1},
			AchievementCategory.SMASH_MOBS),

	SMASH_MOBS_FREE_KITS("Free Kits Forever", 800,
			new String[]{"Super Smash Mobs.FreeKitsForever"},
			new String[]{"Win 100 games using only Free Kits"},
			new int[]{100},
			AchievementCategory.SMASH_MOBS),

	SMASH_MOBS_1V3("1v3", 2000,
			new String[]{"Super Smash Mobs.1v3"},
			new String[]{"Get 10 kills in a game with 4 players"},
			new int[]{1},
			AchievementCategory.SMASH_MOBS),

	SMASH_MOBS_TRIPLE_KILL("Triple Kill", 1200,
			new String[]{"Super Smash Mobs.TripleKill"},
			new String[]{"Get 3 kills in a row, with no more than", "10 seconds between each kill"},
			new int[]{1},
			AchievementCategory.SMASH_MOBS),

	SMASH_MOBS_RECOVERY_MASTER("Recovery Master", 800,
			new String[]{"Super Smash Mobs.RecoveryMaster"},
			new String[]{"Take 200 damage in a single life"},
			new int[]{1},
			AchievementCategory.SMASH_MOBS),

	//Block Hunt
	BLOCK_HUNT_WINS("The Blockiest Block", 600,
			new String[]{"Block Hunt.Wins"},
			new String[]{"Win 50 games of Block Hunt"},
			new int[]{50},
			AchievementCategory.BLOCK_HUNT),

	BLOCK_HUNT_HUNTER_KILLER("Hunter Killer", 1200,
			new String[]{"Block Hunt.HunterKiller"},
			new String[]{"Kill 10 Hunters in a single game"},
			new int[]{1},
			AchievementCategory.BLOCK_HUNT),

	BLOCK_HUNT_MEOW("Meow Meow Meow Meow", 800,
			new String[]{"Block Hunt.Meow"},
			new String[]{"Meow 50 times in a single game"},
			new int[]{1},
			AchievementCategory.BLOCK_HUNT),

	BLOCK_HUNT_HUNTER_OF_THE_YEAR("Hunter of the Year", 1200,
			new String[]{"Block Hunt.HunterOfTheYear"},
			new String[]{"Kill 7 Hiders in a single game"},
			new int[]{1},
			AchievementCategory.BLOCK_HUNT),

	BLOCK_HUNT_BAD_HIDER("Bad Hider", 1000,
			new String[]{"Block Hunt.BadHider"},
			new String[]{"Win as Hider without disguising"},
			new int[]{1},
			AchievementCategory.BLOCK_HUNT),

	//Draw My Thing
	DRAW_MY_THING_WINS("Art Hipster", 600,
			new String[]{"Draw My Thing.Wins"},
			new String[]{"Win 50 games of Draw My Thing"},
			new int[]{50},
			AchievementCategory.DRAW_MY_THING),

	DRAW_MY_THING_MR_SQUIGGLE("Mr. Squiggle", 800,
			new String[]{"Draw My Thing.MrSquiggle"},
			new String[]{"Both your drawings are guessed", "within the first 15 seconds."},
			new int[]{1},
			AchievementCategory.DRAW_MY_THING),

	DRAW_MY_THING_KEEN_EYE("Keen Eye", 1200,
			new String[]{"Draw My Thing.KeenEye"},
			new String[]{"Guess every single drawing in a game"},
			new int[]{1},
			AchievementCategory.DRAW_MY_THING),

	DRAW_MY_THING_PURE_LUCK("Pure Luck", 800,
			new String[]{"Draw My Thing.PureLuck"},
			new String[]{"Guess a word in the first 8 seconds"},
			new int[]{1},
			AchievementCategory.DRAW_MY_THING),

	// Master Builders
	MASTER_BUILDER_WINS("Master Builder", 1000,
			new String[]{"Master Builders.Wins"},
			new String[]{"Win 30 games of Master Builders"},
			new int[]{30},
			AchievementCategory.MASTER_BUILDERS),

	//Castle Siege
	CASTLE_SIEGE_WINS("FOR THE KING!", 600,
			new String[]{"Castle Siege.ForTheKing"},
			new String[]{"Win as Defenders 50 times"},
			new int[]{50},
			AchievementCategory.CASTLE_SIEGE),

	CASTLE_SIEGE_KINGSLAYER("Kingslayer", 800,
			new String[]{"Castle Siege.KingSlayer"},
			new String[]{"Get the killing blow on the King"},
			new int[]{1},
			AchievementCategory.CASTLE_SIEGE),

	CASTLE_SIEGE_BLOOD_THIRSTY("Blood Thirsty", 1200,
			new String[]{"Castle Siege.BloodThirsty"},
			new String[]{"Kill 50 Undead in a single game"},
			new int[]{1},
			AchievementCategory.CASTLE_SIEGE),

	CASTLE_SIEGE_ASSASSIN("Assassin", 1000,
			new String[]{"Castle Siege.Assassin"},
			new String[]{"Do 50% or more of the damage to the king"},
			new int[]{1},
			AchievementCategory.CASTLE_SIEGE),

	//Champions
	CHAMPIONS_WINS("Champion", 600,
			new String[]{"Champions Domination.Wins", "Champions TDM.Wins"},
			new String[]{"Win 80 games of Dominate or TDM"},
			new int[]{80},
			AchievementCategory.CHAMPIONS),

	CHAMPIONS_FLAWLESS_VICTORY("Flawless Victory", 800,
			new String[]{"Champions TDM.FlawlessVictory"},
			new String[]{"Win TDM without losing a player"},
			new int[]{1},
			AchievementCategory.CHAMPIONS),

	CHAMPIONS_ACE("Ace", 2000,
			new String[]{"Champions TDM.Ace"},
			new String[]{"Kill all enemies in a game of TDM"},
			new int[]{1},
			AchievementCategory.CHAMPIONS),

	CHAMPIONS_ASSASSINATION("Assassination", 1000,
			new String[]{"Champions Domination.Assassination", "Champions TDM.Assassination"},
			new String[]{"Kill 40 players with Backstab without", "taking any damage from them"},
			new int[]{40},
			AchievementCategory.CHAMPIONS),

	CHAMPIONS_MASS_ELECTROCUTION("Mass Electrocution", 1200,
			new String[]{"Champions Domination.MassElectrocution", "Champions TDM.MassElectrocution"},
			new String[]{"Hit 4 enemies with a Lightning Orb"},
			new int[]{1},
			AchievementCategory.CHAMPIONS),

	CHAMPIONS_THE_LONGEST_SHOT("The Longest Shot", 1200,
			new String[]{"Champions Domination.TheLongestShot", "Champions TDM.TheLongestShot"},
			new String[]{"Kill someone using Longshot who", "is over 64 Blocks away from you"},
			new int[]{1},
			AchievementCategory.CHAMPIONS),

	CHAMPIONS_EARTHQUAKE("Earthquake", 1200,
			new String[]{"Champions Domination.Earthquake", "Champions TDM.Earthquake"},
			new String[]{"Launch 5 enemies using Seismic Slam"},
			new int[]{1},
			AchievementCategory.CHAMPIONS),

	//Paintball
	SUPER_PAINTBALL_WINS("Paintball King", 600,
			new String[]{"Super Paintball.Wins"},
			new String[]{"Win 50 games of Paintball"},
			new int[]{50},
			AchievementCategory.SUPER_PAINTBALL),

	SUPER_PAINTBALL_KILLING_SPREE("Killing Spree", 1200,
			new String[]{"Super Paintball.KillingSpree"},
			new String[]{"Get 4 kills in a row, with no more than", "5 seconds between each kill"},
			new int[]{1},
			AchievementCategory.SUPER_PAINTBALL),

	SUPER_PAINTBALL_FLAWLESS_VICTORY("Flawless Victory", 1000,
			new String[]{"Super Paintball.Wins"},
			new String[]{"Win a game with your entire team alive"},
			new int[]{1},
			AchievementCategory.SUPER_PAINTBALL),

	SUPER_PAINTBALL_MEDIC("Medic!", 800,
			new String[]{"Super Paintball.Medic"},
			new String[]{"Revive 200 team members"},
			new int[]{200},
			AchievementCategory.SUPER_PAINTBALL),

	SUPER_PAINTBALL_SPEEDRUNNER("Speedrunner", 1000,
			new String[]{"Super Paintball.Speedrunner"},
			new String[]{"Win a game in 30 seconds"},
			new int[]{1},
			AchievementCategory.SUPER_PAINTBALL),

	SUPER_PAINTBALL_LAST_STAND("Last Stand", 1200,
			new String[]{"Super Paintball.LastStand"},
			new String[]{"Be the last alive on your team", "and kill 3 enemy players"},
			new int[]{1},
			AchievementCategory.SUPER_PAINTBALL),
			
	//Sheep Quest
	SHEEP_QUEST_WINS("Hungry Hungry Hippo", 600,
			new String[]{"Sheep Quest.Wins"},
			new String[]{"Win 50 games of Sheep Quest"},
			new int[]{50},
			AchievementCategory.SHEEP_QUEST),

	SHEEP_QUEST_THIEF("Thief", 800,
			new String[]{"Sheep Quest.Thief"},
			new String[]{"Steal 300 Sheep from enemy pens"},
			new int[]{300},
			AchievementCategory.SHEEP_QUEST),

	SHEEP_QUEST_ANIMAL_RESCUE("Animal Rescue", 800,
			new String[]{"Sheep Quest.AnimalRescue"},
			new String[]{"Make 300 enemies drop their Sheep"},
			new int[]{300},
			AchievementCategory.SHEEP_QUEST),

	SHEEP_QUEST_SELFISH("Selfish", 1200,
			new String[]{"Sheep Quest.Selfish"},
			new String[]{"Win with more than 12 Sheep"},
			new int[]{1},
			AchievementCategory.SHEEP_QUEST),

	//Snake
	SNAKE_WINS("Nokia 3310", 600,
			new String[]{"Snake.Wins"},
			new String[]{"Win 50 games of Snake"},
			new int[]{50},
			AchievementCategory.SNAKE),

	SNAKE_CANNIBAL("Cannibal", 1600,
			new String[]{"Snake.Cannibal"},
			new String[]{"Kill 6 players in a single game"},
			new int[]{1},
			AchievementCategory.SNAKE),

	SNAKE_CHOO_CHOO("Choo Choo", 1000,
			new String[]{"Snake.ChooChoo"},
			new String[]{"Grow to be 60 Sheep or longer"},
			new int[]{1},
			AchievementCategory.SNAKE),

	SNAKE_SLIMY_SHEEP("Slimy Sheep", 800,
			new String[]{"Snake.SlimySheep"},
			new String[]{"Eat 20 slimes in a single game"},
			new int[]{1},
			AchievementCategory.SNAKE),

	//Dragons
	DRAGONS_WINS("Dragon Tamer", 600,
			new String[]{"Dragons.Wins"},
			new String[]{"Win 50 games of Dragons"},
			new int[]{50},
			AchievementCategory.DRAGONS),

	DRAGONS_SPARKLEZ("Sparklez", 400,
			new String[]{"Dragons.Sparklez"},
			new String[]{"Throw 100 Sparklers"},
			new int[]{100},
			AchievementCategory.DRAGONS),

	//Turf Wars
	TURF_WARS_WINS("Turf Master 3000", 600,
			new String[]{"Turf Wars.Wins"},
			new String[]{"Win 50 games of Turf Wars"},
			new int[]{50},
			AchievementCategory.TURF_WARS),

	TURF_WARS_SHREDDINATOR("The Shreddinator", 800,
			new String[]{"Turf Wars.TheShreddinator"},
			new String[]{"Destroy 2000 blocks as Shredder"},
			new int[]{2000},
			AchievementCategory.TURF_WARS),

	TURF_WARS_BEHIND_ENEMY_LINES("Behind Enemy Lines", 1000,
			new String[]{"Turf Wars.BehindEnemyLines"},
			new String[]{"Stay on enemy turf for 15 seconds"},
			new int[]{1},
			AchievementCategory.TURF_WARS),

	TURF_WARS_COMEBACK("The Comeback", 2000,
			new String[]{"Turf Wars.TheComeback"},
			new String[]{"Win a game after having 5 or less turf"},
			new int[]{1},
			AchievementCategory.TURF_WARS),

	//Death Tag
	DEATH_TAG_WINS("Death Proof", 600,
			new String[]{"Death Tag.Wins"},
			new String[]{"Win 50 games of Death Tag"},
			new int[]{50},
			AchievementCategory.DEATH_TAG),

	DEATH_TAG_COME_AT_ME_BRO("Come At Me Bro!", 1200,
			new String[]{"Death Tag.ComeAtMeBro"},
			new String[]{"Kill 2 Undead Chasers in a single game"},
			new int[]{1},
			AchievementCategory.DEATH_TAG),

	//Runner
	RUNNER_WINS("Hot Feet", 600,
			new String[]{"Runner.Wins"},
			new String[]{"Win 50 games of Runner"},
			new int[]{50},
			AchievementCategory.RUNNER),

	RUNNER_MARATHON_RUNNER("Marathon Runner", 1000,
			new String[]{"Runner.MarathonRunner"},
			new String[]{"Run over 20,000 blocks"},
			new int[]{20000},
			AchievementCategory.RUNNER),

	//Dragon Escape
	DRAGON_ESCAPE_WINS("Douglas Defeater", 600,
			new String[]{"Dragon Escape.Wins"},
			new String[]{"Win 50 games of Dragon Escape"},
			new int[]{50},
			AchievementCategory.DRAGON_ESCAPE),

	DRAGON_ESCAPE_PARALYMPICS("Paralympics", 1200,
			new String[]{"Dragon Escape.Wins"},
			new String[]{"Win a game without using Leap"},
			new int[]{1},
			AchievementCategory.DRAGON_ESCAPE),

	DRAGON_ESCAPE_SKYLANDS("Skylands Master", 1000,
			new String[]{"Dragon Escape.Win.Skylands"},
			new String[]{"Win by finishing Skylands 5 times"},
			new int[]{5},
			AchievementCategory.DRAGON_ESCAPE),

	DRAGON_ESCAPE_THROUGH_HELL("To Hell and Back", 1000,
			new String[]{"Dragon Escape.Win.Through Hell"},
			new String[]{"Win by finishing Through Hell 5 times"},
			new int[]{5},
			AchievementCategory.DRAGON_ESCAPE),

	DRAGON_ESCAPE_PIRATE_BAY("Plundered", 1000,
			new String[]{"Dragon Escape.Win.Pirate Bay"},
			new String[]{"Win by finishing Pirate Bay 5 times"},
			new int[]{5},
			AchievementCategory.DRAGON_ESCAPE),

	//OITQ
	OITQ_WINS("One of a Kind", 600,
			new String[]{"One in the Quiver.Wins"},
			new String[]{"Win 50 games of One in the Quiver"},
			new int[]{50},
			AchievementCategory.ONE_IN_THE_QUIVER),

	OITQ_PERFECTIONIST("The Perfect Game", 3000,
			new String[]{"One in the Quiver.Perfectionist"},
			new String[]{"Win without dying"},
			new int[]{1},
			AchievementCategory.ONE_IN_THE_QUIVER),

	OITQ_SHARPSHOOTER("SharpShooter", 1200,
			new String[]{"One in the Quiver.Sharpshooter"},
			new String[]{"Hit with 8 Arrows in a row"},
			new int[]{1},
			AchievementCategory.ONE_IN_THE_QUIVER),

	OITQ_WHATS_A_BOW("What's A Bow?", 1200,
			new String[]{"One in the Quiver.WhatsABow"},
			new String[]{"Win a game without using a bow"},
			new int[]{1},
			AchievementCategory.ONE_IN_THE_QUIVER),

	//Super Spleef
	SPLEEF_WINS("Spleef King (or Queen)", 600,
			new String[]{"Super Spleef.Wins"},
			new String[]{"Win 50 games of Super Spleef"},
			new int[]{50},
			AchievementCategory.SPLEEF),

	SPLEEF_DEMOLITIONIST("Demolitionist", 1000,
			new String[]{"Super Spleef.SpleefBlocks"},
			new String[]{"Destroy 20,000 blocks."},
			new int[]{20000},
			AchievementCategory.SPLEEF),

	//Bacon Brawl
	BACON_BRAWL_WINS("King of Bacon", 600,
			new String[]{"Bacon Brawl.Wins"},
			new String[]{"Win 50 games of Bacon Brawl"},
			new int[]{50},
			AchievementCategory.BACON_BRAWL),

	//Sneaky Assassins
	SNEAKY_ASSASSINS_WINS("So So Sneaky", 600,
			new String[]{"Sneaky Assassins.Wins"},
			new String[]{"Win 50 games of Sneaky Assassins"},
			new int[]{50},
			AchievementCategory.SNEAKY_ASSASSINS),

	SNEAK_ASSASSINS_MASTER_ASSASSIN("Master Assassin", 600,
			new String[]{"Sneaky Assassins.MasterAssassin"},
			new String[]{"Get Master Assassin 10 times"},
			new int[]{10},
			AchievementCategory.SNEAKY_ASSASSINS),

	SNEAK_ASSASSINS_THE_MASTERS_MASTER("The Master's Master", 700,
			new String[]{"Sneaky Assassins.TheMastersMaster"},
			new String[]{"Kill a Master Assassin without", "having a single power-up."},
			new int[]{1},
			AchievementCategory.SNEAKY_ASSASSINS),

	SNEAK_ASSASSINS_INCOMPETENCE("Incompetence", 600,
			new String[]{"Sneaky Assassins.Incompetence"},
			new String[]{"Kill 200 NPCs."},
			new int[]{200},
			AchievementCategory.SNEAKY_ASSASSINS),

	SNEAK_ASSASSINS_I_SEE_YOU("I See You", 800,
			new String[]{"Sneaky Assassins.ISeeYou"},
			new String[]{"Reveal 50 players."},
			new int[]{50},
			AchievementCategory.SNEAKY_ASSASSINS),

	//Micro Battle
	MICRO_BATTLE_WINS("Micro Champion", 600,
			new String[]{"Micro Battle.Wins"},
			new String[]{"Win 100 games of Micro Battle"},
			new int[]{100},
			AchievementCategory.MICRO_BATTLE),

	MICRO_BATTLE_ANNIHILATION("Annihilation", 1200,
			new String[]{"Micro Battle.Annihilation"},
			new String[]{"Kill 8 players in one game"},
			new int[]{1},
			AchievementCategory.MICRO_BATTLE),

	//MineStrike
	MINE_STRIKE_WINS("Striker", 800,
			new String[]{"MineStrike.Wins"},
			new String[]{"Win 50 games of MineStrike"},
			new int[]{50},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_BOOM_HEADSHOT("BOOM! HEADSHOT!", 800,
			new String[]{"MineStrike.BoomHeadshot"},
			new String[]{"Kill 500 people with headshots"},
			new int[]{500},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_ACE("Ace", 2000,
			new String[]{"MineStrike.Ace"},
			new String[]{"Get the kill on all enemies in a single round"},
			new int[]{1},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_KABOOM("Kaboom!", 1000,
			new String[]{"MineStrike.Kaboom"},
			new String[]{"Kill two people with a single", "High Explosive Grenade"},
			new int[]{1},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_ASSASSINATION("Assassination", 800,
			new String[]{"MineStrike.Assassination"},
			new String[]{"Get 20 backstab kills with the knife"},
			new int[]{20},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_CLUTCH_OR_KICK("Clutch or Kick", 1000,
			new String[]{"MineStrike.ClutchOrKick"},
			new String[]{"Be the last one alive, and kill", "3 or more enemies to achieve victory"},
			new int[]{1},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_KILLING_SPREE("Killing Spree", 1000,
			new String[]{"MineStrike.KillingSpree"},
			new String[]{"Kill 4 enemies in a row with no more", "than 5 seconds between each kill"},
			new int[]{1},
			AchievementCategory.MINE_STRIKE),

	MINE_STRIKE_BLINDFOLDED("Blindfolded", 800,
			new String[]{"MineStrike.Blindfolded"},
			new String[]{"Kill 2 enemies while blinded from", "a single flashbang"},
			new int[]{1},
			AchievementCategory.MINE_STRIKE),
			
	//Bomb Lobbers
	BOMB_LOBBERS_WINS("Master Bomber", 1200,
			new String[]{"Bomb Lobbers.Wins"},
			new String[]{"Win 100 games of Bomb Lobbers"},
			new int[] {100},
			AchievementCategory.BOMB_LOBBERS),
			
	BOMB_LOBBERS_PROFESSIONAL_LOBBER("Professional Lobber", 1000,
			new String[]{"Bomb Lobbers.Thrown"},
			new String[]{"Throw 2000 TNT"},
			new int[]{2000},
			AchievementCategory.BOMB_LOBBERS),

	BOMB_LOBBERS_ULTIMATE_KILLER("Ultimate Killer", 800,
			new String[]{"Bomb Lobbers.Killer"},
			new String[]{"Kill 6 players in a single game"},
			new int[]{1},
			AchievementCategory.BOMB_LOBBERS),
			
	BOMB_LOBBERS_EXPLOSION_PROOF("Jelly Skin", 1200, 
			new String[]{"Bomb Lobbers.JellySkin"},
			new String[]{"Win a game without taking any damage."},
			new int[]{1},
			AchievementCategory.BOMB_LOBBERS),
			
	BOMB_LOBBERS_BLAST_PROOF("Blast Proof", 800,
			new String[]{"Bomb Lobbers.BlastProof"},
			new String[]{"Win 20 games using Armorer"},
			new int[]{20},
			AchievementCategory.BOMB_LOBBERS),
			
	BOMB_LOBBERS_SNIPER("Sniper", 10000,
			new String[]{"Bomb Lobbers.Direct Hit"},
			new String[]{"Get 50 direct hits"},
			new int[]{50},
			AchievementCategory.BOMB_LOBBERS)
			
	;

	private String _name;
	private String[] _desc;
	private String[] _stats;
	private int[] _levels;
	private int _gems;
	private AchievementCategory _category;

	Achievement(String name, int gems, String[] stats, String[] desc, int[] levels, AchievementCategory category)
	{
		_name = name;
		_gems = gems;
		_desc = desc;
		_stats = stats;
		_levels = levels;
		_category = category;
	}

	private static int[] getExperienceLevels()
	{
		int[] levels = new int[100];
		
		int expReq = 0;
		
		for (int i=0 ; i<10 ; i++)
		{
			expReq += 500;
			levels[i] = expReq;
		}
		
		for (int i=10 ; i<20 ; i++)
		{
			expReq += 1000;
			levels[i] = expReq;
		}
			
		for (int i=20 ; i<40 ; i++)
		{
			expReq += 2000;
			levels[i] = expReq;
		}
		
		for (int i=40 ; i<60 ; i++)
		{
			expReq += 3000;
			levels[i] = expReq;
		}
		
		for (int i=60 ; i<80 ; i++)
		{
			expReq += 4000;
			levels[i] = expReq;
		}
		
		for (int i=80 ; i<levels.length ; i++)
		{
			expReq += 5000;
			levels[i] = expReq;
		}
		
		return levels;
	}
	
	public static String getExperienceString(int level)
	{
		if (level < 0)
			return C.cPurple + level;

		if (level < 20)
			return C.cGray + level;

		if (level < 40)
			return C.cBlue + level;
		
		if (level < 60)
			return C.cDGreen + level;
		
		if (level < 80)
			return C.cGold + level;
		
		return C.cRed + level;
	}

	public String getName()
	{
		return _name;
	}

	public String[] getDesc()
	{
		return _desc;
	}

	public String[] getStats()
	{
		return _stats;
	}

	public int[] getLevels()
	{
		return _levels;
	}

	public int getMaxLevel()
	{
		return _levels.length;
	}

	public boolean isOngoing()
	{
		return _levels[0] > 1;
	}

	public boolean isSingleLevel()
	{
		return _levels.length == 1;
	}

	public AchievementCategory getCategory()
	{
		return _category;
	}

	public AchievementData getLevelData(long exp)
	{
		for (int i = 0; i < _levels.length; i++)
		{
			int req = _levels[i];

			//Has Experience, Level Up!
			if (exp >= req)
			{
				exp -= req;
				continue;
			}

			return new AchievementData(i, exp, req);
		}

		return new AchievementData(getMaxLevel(), -1, -1);
	}

	public int getGemReward()
	{
		return _gems;
	}
}
