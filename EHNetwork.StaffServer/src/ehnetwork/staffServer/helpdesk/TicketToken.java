package ehnetwork.staffServer.helpdesk;

public class TicketToken
{
	public int TicketID;
	public int UserID;
	public int AssignedToUserID;
	public String IssueDate;
	public String Subject;
	public String Body;
	public int Priority;
	public int StatusID;
	public int CategoryID;
	public String DueDate;
	public String ResolvedDate;
	public String StartDate;
	public int TimeSpentInSeconds;
	public boolean IsCurrentUserTechInThisCategory;
	public boolean IsCurrentCategoryForTechsOnly;
	public boolean SubmittedByCurrentUser;
	public boolean IsInKb;
}
