package ehnetwork.core.database;

import ehnetwork.core.common.util.NautHashMap;
import ehnetwork.core.database.column.Column;

public class Row
{
	public NautHashMap<String, Column<?>> Columns = new NautHashMap<String, Column<?>>();
}
