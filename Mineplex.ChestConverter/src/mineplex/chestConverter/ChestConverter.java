package mineplex.chestConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ChestConverter
{
	private static ChestConverterRepository _repository = null;
	private static SimpleDateFormat _dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	private static Logger _logger = Logger.getLogger("Converter");
	
	public static void main (String args[])
	{
		try
		{
			FileHandler fileHandler = new FileHandler("converter.log", true);
			fileHandler.setFormatter(new Formatter()
			{
				@Override
				public String format(LogRecord record)
				{
					return record.getMessage() + "\n";
				}
			});
			_logger.addHandler(fileHandler);
			_logger.setUseParentHandlers(false);
		}
		catch (SecurityException | IOException e1)
		{
			e1.printStackTrace();
		}

		_repository = new ChestConverterRepository();
		int lastId = 18279475;
		int count = 50000;
		int numOfRowsProcessed = lastId;
		
		HashMap<String, Integer> tasks = _repository.getTaskList();
		
		try
		{
			while (true)
			{
				long time = System.currentTimeMillis();
				HashMap<String, List<Integer>> playerMap = new HashMap<String, List<Integer>>();
				
				
				List<AccountTask> taskList = _repository.getTasks(lastId, count);
				
				if (taskList != null && taskList.size() > 0)
				{
					for (AccountTask task : taskList)
					{
						if (!playerMap.containsKey(task.UUID))
							playerMap.put(task.UUID, new ArrayList<Integer>());
						
						playerMap.get(task.UUID).add(tasks.get(task.Task));
						
						if (task.Id > lastId)
							lastId = task.Id;
					}
					
					_repository.incrementClients(playerMap);
					try
					{
						numOfRowsProcessed += count;
						log("Natural sleep. " + count + " took " + (System.currentTimeMillis() - time) / 1000 + " seconds. Count = " + + numOfRowsProcessed);
						Thread.sleep(100);
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
				else if (numOfRowsProcessed > 17000000)
				{
					System.out.println("Count : " + numOfRowsProcessed);
					_logger.info("Count : " + numOfRowsProcessed);
					break;
				}
				else
				{
					System.out.println("No greater than 17 mil");
					_logger.info("No greater than 17 mil");
					System.out.println("Count : " + numOfRowsProcessed);
					_logger.info("Count : " + numOfRowsProcessed);
				}
			}
		}
		catch (Exception e)
		{
			_logger.info(e.getMessage());			
		}
		finally
		{
			System.out.println("Count : " + numOfRowsProcessed);
			_logger.info("Count : " + numOfRowsProcessed);
		}
	}
	
	private static void log(String message)
	{
		System.out.println("[" + _dateFormat.format(new Date()) + "] " + message);
	}
}
