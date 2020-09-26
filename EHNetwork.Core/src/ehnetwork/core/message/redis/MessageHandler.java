package ehnetwork.core.message.redis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ehnetwork.core.message.MessageManager;
import ehnetwork.serverdata.commands.CommandCallback;
import ehnetwork.serverdata.commands.ServerCommand;

public class MessageHandler implements CommandCallback
{
    private MessageManager _messageManager;

    public MessageHandler(MessageManager messageManager)
    {
        _messageManager = messageManager;
    }

    public void run(ServerCommand command)
    {
        if (command instanceof RedisMessage)
        {
            RedisMessage message = (RedisMessage) command;

            Player target = Bukkit.getPlayerExact(message.getTarget());

            if (target != null)
            {
                _messageManager.receiveMessage(target, message);
            }
        }
        else if (command instanceof RedisMessageCallback)
        {

            _messageManager.receiveMessageCallback((RedisMessageCallback) command);
        }
    }
}
