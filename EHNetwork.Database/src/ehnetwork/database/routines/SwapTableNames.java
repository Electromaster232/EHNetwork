/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.routines;

import ehnetwork.database.Account;

/**
 * Swap the names of two tables in one atomic operation.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class SwapTableNames extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1203459895;

	/**
	 * The parameter <code>Account.swapTableNames.tableName1</code>.
	 */
	public static final org.jooq.Parameter<java.lang.String> tableName1 = createParameter("tableName1", org.jooq.impl.SQLDataType.VARCHAR.length(100), false);

	/**
	 * The parameter <code>Account.swapTableNames.tableName2</code>.
	 */
	public static final org.jooq.Parameter<java.lang.String> tableName2 = createParameter("tableName2", org.jooq.impl.SQLDataType.VARCHAR.length(100), false);

	/**
	 * Create a new routine call instance
	 */
	public SwapTableNames() {
		super("swapTableNames", Account.Account);

		addInParameter(tableName1);
		addInParameter(tableName2);
	}

	/**
	 * Set the <code>tableName1</code> parameter IN value to the routine
	 */
	public void setTableName1(java.lang.String value) {
		setValue(SwapTableNames.tableName1, value);
	}

	/**
	 * Set the <code>tableName2</code> parameter IN value to the routine
	 */
	public void setTableName2(java.lang.String value) {
		setValue(SwapTableNames.tableName2, value);
	}
}
