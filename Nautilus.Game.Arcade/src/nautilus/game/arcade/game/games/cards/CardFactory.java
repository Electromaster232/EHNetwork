package nautilus.game.arcade.game.games.cards;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import mineplex.core.common.util.C;
import mineplex.core.common.util.UtilMath;
import mineplex.core.map.MapText;

public class CardFactory
{
	private MapText _mapText;
	
	public CardFactory()
	{
		_mapText = new MapText();
	}
	
	private String[] _questions = new String[]
			{
			"When I get home from school, I crave _____.", "I was playing One in the Quiver when I ran into ____.", "Ultra+ now get free _____ every month.", "Everything was fine until _____ joined.", "Help me! My mom is _______.", "_________ crashed the server again…", "_______ ate his pet rabbit when he saw __________.", "My mouse batteries died because I was using it for ______.", "______ is the #1 reason of deaths on the server.", "Ugh, I'm lagging thanks to _____!", "I need to go to the doctor, I'm sick with _________!", "My best friend thinks he is _____.", "The cure for all diseases is partially made out of _____.", "When i was little i wanted to become a ______ and use _____ to save the world.", "I painted a beautiful painting of ____!", "When you grow up you will realize that ______ is all you need.", "_______ knew he had made the wrong choice when he realised he was _______.", "While falling asleep, I dreamt of ______ kissing ______.", "I went to a fancy restaurant, but the chef gave me _____!", "What am I supposed to do when I see _____ ______?", "There is absolutely nothing worse in this world than ___________.", "I get all excited and tingly when I see ________!", "I would rather be _________ than _________.", "I think my brother is a big fan of _______.", "Why can't I sleep at night?", "What's that smell?", "I got 99 problems but ________ ain't one.", "What's the next game coming out on Mineplex?", "What is the next trend on YouTube?", "It's a pity that kids these days are getting involved with ________.", "What's that sound?", "What should I look for in a girlfriend?", "I wish I could forget about _______.", "What don't you want to find in your treasure chest?", "I can't take you seriously when you are always _________.", "I'm sorry, but I couldn't finish my homework because of _________.", "What does Batman do in his spare time?", "What's a girl's best friend?", "___________. That's how I want to die.", "For my next trick, I will pull _______ out of ________.", "________ is a slippery slope that leads to ________.", "Instead of coal, Santa now gives the bad children ________.", "An exciting game of The Bridges would be incomplete without _________.", "War! What is it good for?", "While mining diamonds, I like to think about ________.", "What are my parents hiding from me?", "What will always make girls like you?", "What did I bring back from the Nether?", "What don't you want to find in your Mushroom Soup?", "Step 1: __________, Step 2: __________, Step 3: Profit!" ,"What makes you need to fart?", "What do old people smell like?", "What's your secret power?", "The class field trip was completely ruined by _______.", "I never understood _______, until I found ________.", "What does Justin Beiber enjoy in his spare time?", "Why is the Villager sticky?", "Scientists think that Creepers explode because of _________.", "Why do you hurt all over?", "When you are a billionaire, you will spend all of your money on ________.", "How do you cheer up a sad friend?", "What is the new diet everyone is trying?", "What is the secret ingredient in Mountain Dew?", "________ is one of the few things that makes me happy.", "The most important thing you need to build a house is ________.", "What is the best way to get unbanned from Mineplex?", "_________ is the secret to winning at Super Smash Mobs.",

			};
	
	private String[] _answers = new String[]
			{
			"A Stinky Pig Butt", "Mouldy Raw Fish", "A sawed off 10-foot pole", "billions of diamonds", "A whiny forum post", "Sparkling Diamonds", "Sheep that don’t know when to stop", "Villager Legs", "Curdled cow milk", "That one popcorn piece stuck in your teeth.", "THE DIAMOND DANCE!", "an unstoppable addiction to fly hacks", "the biggest pile of poop you've ever seen!", "Bed time", "A bag of Doritos and a Mountain Dew.", "zombies chewing on my butt", "Jazz hands", "Brain surgery", "A disappointing birthday party.", "Overly friendly Iron Golems", "An Endermans favourite block", "Puppies!", "your friend who deleted you from Skype", "being on fire", "two cows stealing your wallet", "a lifetime of sadness", "a golden notch apple", "dragon eggs", "holding hands", "kissing", "wearing parents on your head", "soup that is too hot", "edible underpants", "people who are terrible at PvP", "obesity", "sheep eating potatoes", "my collection of spider eyes", "oversized fishing rods", "working as a team", "horse meat", "zombies screaming while on fire", "a ghast calling her dad to say happy birthday", "Microsoft releasing a good game", "bad childhood memories", "a bad haircut", "a steamy locker room", "armed robbery", "bankruptcy", "stacks of TNT", "a block of dirt", "cold pizza", "a pack of angry wolves", "eternal sadness", "the Nether", "a blaze having a barbeque", "Endermen robbing your house", "dental surgery", "religious villagers", "poor villagers", "flesh-eating bacteria", "the drive to win", "friends who take all your loot", "Morgan Freeman", "Darth Vader", "Villager #17", "Barrack Obama", "Lady Gaga", "That one hobo that lives down the street", "A Sad Creeper", "Parker Games", "A shaved sheep", "CaptainSparklez", "DanTDM", "SkyDoesMinecraft", "Mineplex", "A Mineplex Administrator", "Miley Cyrus", "Notch", "Mojang", "Pikachu", "BATMAN!!!", "The Hulk", "Justin Beiber", "Herobrine", "Pirates", "Vikings", "Ninjas", "Uncontrollably pooping", "Dancing until a helper warned me", "Making passive aggressive tweets", "complaining about balance on the forums", "Chopping down the mineplex lobby tree", "crashing servers", "falling off a cliff", "Realizing that you arent actually a ninja.", "Eating a hot pepper", "Digging straight down", "using xray hacks", "picking your nose", "Playing against a team in SSM", "Mining diamond with a stone pickaxe", "stepping on Legos", "Shaking it off", "Watching Parker games", "Accidentally hiding an egg as Easter bunny morph", "doing a poop and realizing there is no toilet paper", "Eating rotten flesh", "Understanding how magnets work", "watching videos on YouTube", "finding Legendary loot in a Treasure Chest", "Doing homework", "dividing by 0", "Finding out the meaning of life", "banning players for no reason", "dying", "so angry that he pooped himself.", "spending hours planting carrots", "doing the right thing", "sleeping in a cave", "getting drunk on potions", "trolling on the internet", "punching a tree", "leaving a stinky surprise", "doing math", "being devoured by zombie pigmen", "farting and walking away", "sneakily doing a poop", "blowing everything up with TNT", "peeing a little bit", "Tweeting about cats", "reading the instructions carefully",
			"leaving some poop in the corner of the room", "eating a balanced breakfast", "puking", "shooting arrows at bats", "typing /kill", "watching Frozen repeatedly", "building a house out of dirt", "winking at chickens", "yelling at a toaster", "putting in 110% effort", "paying a lot of money", "bribing with candy", "throwing rocks at people", "crying until everything is better"
			};
	
	public String getQuestionCard()
	{
		return _questions[UtilMath.r(_questions.length)];
	}
	
	public String getAnswerCard()
	{
		return _answers[UtilMath.r(_answers.length)];
	}

	public ItemStack getAnswerStack() 
	{
		String text = getAnswerCard();
		
		ItemStack stack = _mapText.getMap(true, text);
		
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(C.cGreen + text);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
	public ItemStack getQuestionStack()
	{
		String text = getQuestionCard();
		
		ItemStack stack = _mapText.getMap(true, text);
		
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(C.cYellow + text);
		stack.setItemMeta(meta);
		
		return stack;
	}
}
