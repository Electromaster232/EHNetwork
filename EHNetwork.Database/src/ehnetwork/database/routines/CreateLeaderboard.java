/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.routines;

import ehnetwork.database.Account;

/**
 * Creates a functional leaderboard table.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CreateLeaderboard extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1695430202;

	/**
	 * The parameter <code>Account.createLeaderboard.tableName</code>.
	 */
	public static final org.jooq.Parameter<java.lang.String> tableName = createParameter("tableName", org.jooq.impl.SQLDataType.VARCHAR.length(100), false);

	/**
	 * Create a new routine call instance
	 */
	public CreateLeaderboard() {
		super("createLeaderboard", Account.Account);

		addInParameter(tableName);
	}

	/**
	 * Set the <code>tableName</code> parameter IN value to the routine
	 */
	public void setTableName(java.lang.String value) {
		setValue(CreateLeaderboard.tableName, value);
	}
}
