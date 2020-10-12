/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.routines;

import ehnetwork.database.Account;

/**
 * Insert (or update) a StatEvent into the StatEvents table.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InsertStatEvent extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 688756303;

	/**
	 * The parameter <code>Account.insertStatEvent.statAccountID</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> statAccountID = createParameter("statAccountID", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.insertStatEvent.statDate</code>.
	 */
	public static final org.jooq.Parameter<java.sql.Date> statDate = createParameter("statDate", org.jooq.impl.SQLDataType.DATE, false);

	/**
	 * The parameter <code>Account.insertStatEvent.statGamemode</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Byte> statGamemode = createParameter("statGamemode", org.jooq.impl.SQLDataType.TINYINT, false);

	/**
	 * The parameter <code>Account.insertStatEvent.statServerGroup</code>.
	 */
	public static final org.jooq.Parameter<java.lang.String> statServerGroup = createParameter("statServerGroup", org.jooq.impl.SQLDataType.VARCHAR.length(100), false);

	/**
	 * The parameter <code>Account.insertStatEvent.statType</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Byte> statType = createParameter("statType", org.jooq.impl.SQLDataType.TINYINT, false);

	/**
	 * The parameter <code>Account.insertStatEvent.statValue</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Short> statValue = createParameter("statValue", org.jooq.impl.SQLDataType.SMALLINT, false);

	/**
	 * Create a new routine call instance
	 */
	public InsertStatEvent() {
		super("insertStatEvent", Account.Account);

		addInParameter(statAccountID);
		addInParameter(statDate);
		addInParameter(statGamemode);
		addInParameter(statServerGroup);
		addInParameter(statType);
		addInParameter(statValue);
	}

	/**
	 * Set the <code>statAccountID</code> parameter IN value to the routine
	 */
	public void setStatAccountID(java.lang.Integer value) {
		setValue(InsertStatEvent.statAccountID, value);
	}

	/**
	 * Set the <code>statDate</code> parameter IN value to the routine
	 */
	public void setStatDate(java.sql.Date value) {
		setValue(InsertStatEvent.statDate, value);
	}

	/**
	 * Set the <code>statGamemode</code> parameter IN value to the routine
	 */
	public void setStatGamemode(java.lang.Byte value) {
		setValue(InsertStatEvent.statGamemode, value);
	}

	/**
	 * Set the <code>statServerGroup</code> parameter IN value to the routine
	 */
	public void setStatServerGroup(java.lang.String value) {
		setValue(InsertStatEvent.statServerGroup, value);
	}

	/**
	 * Set the <code>statType</code> parameter IN value to the routine
	 */
	public void setStatType(java.lang.Byte value) {
		setValue(InsertStatEvent.statType, value);
	}

	/**
	 * Set the <code>statValue</code> parameter IN value to the routine
	 */
	public void setStatValue(java.lang.Short value) {
		setValue(InsertStatEvent.statValue, value);
	}
}
