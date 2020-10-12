//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.bukkit.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.FormattedCommandAlias;
import org.bukkit.command.defaults.AchievementCommand;
import org.bukkit.command.defaults.BanCommand;
import org.bukkit.command.defaults.BanIpCommand;
import org.bukkit.command.defaults.BanListCommand;
import org.bukkit.command.defaults.ClearCommand;
import org.bukkit.command.defaults.DefaultGameModeCommand;
import org.bukkit.command.defaults.DeopCommand;
import org.bukkit.command.defaults.DifficultyCommand;
import org.bukkit.command.defaults.EffectCommand;
import org.bukkit.command.defaults.EnchantCommand;
import org.bukkit.command.defaults.ExpCommand;
import org.bukkit.command.defaults.GameModeCommand;
import org.bukkit.command.defaults.GameRuleCommand;
import org.bukkit.command.defaults.GiveCommand;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.KickCommand;
import org.bukkit.command.defaults.KillCommand;
import org.bukkit.command.defaults.ListCommand;
import org.bukkit.command.defaults.MeCommand;
import org.bukkit.command.defaults.OpCommand;
import org.bukkit.command.defaults.PardonCommand;
import org.bukkit.command.defaults.PardonIpCommand;
import org.bukkit.command.defaults.PlaySoundCommand;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.command.defaults.SaveCommand;
import org.bukkit.command.defaults.SaveOffCommand;
import org.bukkit.command.defaults.SaveOnCommand;
import org.bukkit.command.defaults.SayCommand;
import org.bukkit.command.defaults.ScoreboardCommand;
import org.bukkit.command.defaults.SeedCommand;
import org.bukkit.command.defaults.SetIdleTimeoutCommand;
import org.bukkit.command.defaults.SetWorldSpawnCommand;
import org.bukkit.command.defaults.SpawnpointCommand;
import org.bukkit.command.defaults.SpreadPlayersCommand;
import org.bukkit.command.defaults.StopCommand;
import org.bukkit.command.defaults.TeleportCommand;
import org.bukkit.command.defaults.TellCommand;
import org.bukkit.command.defaults.TestForCommand;
import org.bukkit.command.defaults.TimeCommand;
import org.bukkit.command.defaults.TimingsCommand;
import org.bukkit.command.defaults.ToggleDownfallCommand;
import org.bukkit.command.defaults.VanillaCommand;
import org.bukkit.command.defaults.VersionCommand;
import org.bukkit.command.defaults.WeatherCommand;
import org.bukkit.command.defaults.WhitelistCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.TabCompleteEvent;
import org.bukkit.util.Java15Compat;
import org.bukkit.util.StringUtil;

public class SimpleCommandMap implements CommandMap {
	private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);
	protected final Map<String, Command> knownCommands = new HashMap<String, Command>();
	private final Server server;

	public SimpleCommandMap(Server server) {
		this.server = server;
		this.setDefaultCommands();
	}

	private void setDefaultCommands() {
		this.register("bukkit", new SaveCommand());
		this.register("bukkit", new SaveOnCommand());
		this.register("bukkit", new SaveOffCommand());
		this.register("bukkit", new StopCommand());
		this.register("bukkit", new VersionCommand("version"));
		this.register("bukkit", new ReloadCommand("reload"));
		this.register("bukkit", new PluginsCommand("plugins"));
		this.register("bukkit", new TimingsCommand("timings"));
	}

	public void setFallbackCommands() {
		this.register("bukkit", new ListCommand());
		this.register("bukkit", new OpCommand());
		this.register("bukkit", new DeopCommand());
		this.register("bukkit", new BanIpCommand());
		this.register("bukkit", new PardonIpCommand());
		this.register("bukkit", new BanCommand());
		this.register("bukkit", new PardonCommand());
		this.register("bukkit", new KickCommand());
		this.register("bukkit", new TeleportCommand());
		this.register("bukkit", new GiveCommand());
		this.register("bukkit", new TimeCommand());
		this.register("bukkit", new SayCommand());
		this.register("bukkit", new WhitelistCommand());
		this.register("bukkit", new TellCommand());
		this.register("bukkit", new MeCommand());
		this.register("bukkit", new KillCommand());
		this.register("bukkit", new GameModeCommand());
		this.register("bukkit", new HelpCommand());
		this.register("bukkit", new ExpCommand());
		this.register("bukkit", new ToggleDownfallCommand());
		this.register("bukkit", new BanListCommand());
		this.register("bukkit", new DefaultGameModeCommand());
		this.register("bukkit", new SeedCommand());
		this.register("bukkit", new DifficultyCommand());
		this.register("bukkit", new WeatherCommand());
		this.register("bukkit", new SpawnpointCommand());
		this.register("bukkit", new ClearCommand());
		this.register("bukkit", new GameRuleCommand());
		this.register("bukkit", new EnchantCommand());
		this.register("bukkit", new TestForCommand());
		this.register("bukkit", new EffectCommand());
		this.register("bukkit", new ScoreboardCommand());
		this.register("bukkit", new PlaySoundCommand());
		this.register("bukkit", new SpreadPlayersCommand());
		this.register("bukkit", new SetWorldSpawnCommand());
		this.register("bukkit", new SetIdleTimeoutCommand());
		this.register("bukkit", new AchievementCommand());
	}

	public void registerAll(String fallbackPrefix, List<Command> commands) {
		if(commands != null) {
			Iterator var3 = commands.iterator();

			while(var3.hasNext()) {
				Command c = (Command)var3.next();
				this.register(fallbackPrefix, c);
			}
		}

	}

	public boolean register(String fallbackPrefix, Command command) {
		return this.register(command.getName(), fallbackPrefix, command);
	}

	public boolean register(String label, String fallbackPrefix, Command command) {
		label = label.toLowerCase().trim();
		fallbackPrefix = fallbackPrefix.toLowerCase().trim();
		boolean registered = this.register(label, command, false, fallbackPrefix);
		Iterator iterator = command.getAliases().iterator();

		while(iterator.hasNext()) {
			if(!this.register((String)iterator.next(), command, true, fallbackPrefix)) {
				iterator.remove();
			}
		}

		if(!registered) {
			command.setLabel(fallbackPrefix + ":" + label);
		}

		command.register(this);
		return registered;
	}

	private synchronized boolean register(String label, Command command, boolean isAlias, String fallbackPrefix) {
		this.knownCommands.put(fallbackPrefix + ":" + label, command);
		if((command instanceof VanillaCommand || isAlias) && this.knownCommands.containsKey(label)) {
			return false;
		} else {
			boolean registered = true;
			Command conflict = (Command)this.knownCommands.get(label);
			if(conflict != null && conflict.getLabel().equals(label)) {
				return false;
			} else {
				if(!isAlias) {
					command.setLabel(label);
				}

				this.knownCommands.put(label, command);
				return registered;
			}
		}
	}

	public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
		String[] args = PATTERN_ON_SPACE.split(commandLine);
		if(args.length == 0) {
			return false;
		} else {
			String sentCommandLabel = args[0].toLowerCase();
			Command target = this.getCommand(sentCommandLabel);
			if(target == null) {
				return false;
			} else {
				try {
					target.timings.startTiming();
					target.execute(sender, sentCommandLabel, (String[])Java15Compat.Arrays_copyOfRange(args, 1, args.length));
					target.timings.stopTiming();
					return true;
				} catch (CommandException var7) {
					target.timings.stopTiming();
					throw var7;
				} catch (Throwable var8) {
					target.timings.stopTiming();
					throw new CommandException("Unhandled exception executing \'" + commandLine + "\' in " + target, var8);
				}
			}
		}
	}

	public synchronized void clearCommands() {
		Iterator var1 = this.knownCommands.entrySet().iterator();

		while(var1.hasNext()) {
			Entry entry = (Entry)var1.next();
			((Command)entry.getValue()).unregister(this);
		}

		this.knownCommands.clear();
		this.setDefaultCommands();
	}

	public Command getCommand(String name) {
		Command target = (Command)this.knownCommands.get(name.toLowerCase());
		return target;
	}

	public List<String> tabComplete(CommandSender sender, String cmdLine) {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(cmdLine, "Command line cannot null");
		int spaceIndex = cmdLine.indexOf(32);
		String argLine;
		if(spaceIndex == -1) {
			ArrayList commandName1 = new ArrayList();
			Map target1 = this.knownCommands;
			argLine = sender instanceof Player?"/":"";
			Iterator args1 = target1.entrySet().iterator();

			while(args1.hasNext()) {
				Entry ex = (Entry)args1.next();
				Command command = (Command)ex.getValue();
				if(command.testPermissionSilent(sender)) {
					String name = (String)ex.getKey();
					if(StringUtil.startsWithIgnoreCase(name, cmdLine)) {
						commandName1.add(argLine + name);
					}
				}
			}

			Collections.sort(commandName1, String.CASE_INSENSITIVE_ORDER);
			return commandName1;
		} else {
			String commandName = cmdLine.substring(0, spaceIndex);
			Command target = this.getCommand(commandName);

			argLine = cmdLine.substring(spaceIndex + 1, cmdLine.length());
			String[] args = PATTERN_ON_SPACE.split(argLine, -1);
			TabCompleteEvent event = new TabCompleteEvent(sender, commandName, args);

			if (target != null) {
				try {
					event.getSuggestions().addAll(target.tabComplete(sender, commandName, args));
				} catch (CommandException var11) {
					throw var11;
				} catch (Throwable var12) {
					throw new CommandException("Unhandled exception executing tab-completer for \'" + cmdLine + "\' in " + target, var12);
				}
			}

			server.getPluginManager().callEvent(event);

			return event.getSuggestions();
		}
	}

	public Collection<Command> getCommands() {
		return Collections.unmodifiableCollection(this.knownCommands.values());
	}

	public void registerServerAliases() {
		Map values = this.server.getCommandAliases();
		Iterator var2 = values.keySet().iterator();

		while(var2.hasNext()) {
			String alias = (String)var2.next();
			if(!alias.contains(":") && !alias.contains(" ")) {
				String[] commandStrings = (String[])values.get(alias);
				ArrayList targets = new ArrayList();
				StringBuilder bad = new StringBuilder();
				String[] var7 = commandStrings;
				int var8 = commandStrings.length;

				for(int var9 = 0; var9 < var8; ++var9) {
					String commandString = var7[var9];
					String[] commandArgs = commandString.split(" ");
					Command command = this.getCommand(commandArgs[0]);
					if(command == null) {
						if(bad.length() > 0) {
							bad.append(", ");
						}

						bad.append(commandString);
					} else {
						targets.add(commandString);
					}
				}

				if(bad.length() > 0) {
					this.server.getLogger().warning("Could not register alias " + alias + " because it contains commands that do not exist: " + bad);
				} else if(targets.size() > 0) {
					this.knownCommands.put(alias.toLowerCase(), new FormattedCommandAlias(alias.toLowerCase(), (String[])targets.toArray(new String[targets.size()])));
				} else {
					this.knownCommands.remove(alias.toLowerCase());
				}
			} else {
				this.server.getLogger().warning("Could not register alias " + alias + " because it contains illegal characters");
			}
		}

	}
}
