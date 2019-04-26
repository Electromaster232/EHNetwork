package mineplex.hub.queue;

import java.util.ArrayList;
import java.util.List;

public class PlayerMatchStatus
{
	public int Id = -1;
	public String State = "Awaiting Match";
	public int AssignedMatch = -1;
	public List<String> OtherStatuses = new ArrayList<String>();
	public boolean Prompted = false;
}
