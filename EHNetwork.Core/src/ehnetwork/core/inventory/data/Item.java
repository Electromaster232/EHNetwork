package ehnetwork.core.inventory.data;

public class Item
{
	public int Id;
	public String Name;
	public String Category;
	
	public Item(String name, String category)
	{
		this(-1, name, category);
	}
	
	public Item(int id, String name, String category)
	{
		Id = id;
		Name = name;
		Category = category;
	}
}
