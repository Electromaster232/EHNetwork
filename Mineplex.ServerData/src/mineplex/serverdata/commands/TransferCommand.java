package mineplex.serverdata.commands;


/**
 * The TransferCommand is sent across the server network to notify
 * servers to transfer players to other destinations.
 * @author Ty
 *
 */
public class TransferCommand extends ServerCommand
{

	// The ServerTransfer to be sent to another server for enactment
	private ServerTransfer _transfer;
	public ServerTransfer getTransfer() { return _transfer; }
	
	/**
	 * Class constructor
	 * @param transfer - the {@link ServerTransfer} to notify another server of
	 */
	public TransferCommand(ServerTransfer transfer)
	{
		_transfer = transfer;
	}
	
	@Override
	public void run() 
	{
		// Utilitizes a callback functionality to seperate dependencies
	}
}
