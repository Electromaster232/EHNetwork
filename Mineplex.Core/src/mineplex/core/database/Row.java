package mineplex.core.database;

import mineplex.core.common.util.NautHashMap;
import mineplex.core.database.column.Column;

public class Row
{
	public NautHashMap<String, Column<?>> Columns = new NautHashMap<String, Column<?>>();
}
