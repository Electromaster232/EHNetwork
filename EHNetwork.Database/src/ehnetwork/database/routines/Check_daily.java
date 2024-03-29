/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.routines;

import ehnetwork.database.Account;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Check_daily extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1266580733;

	/**
	 * The parameter <code>Account.check_daily.accountId_in</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> accountId_in = createParameter("accountId_in", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.check_daily.coinsChange</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> coinsChange = createParameter("coinsChange", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.check_daily.gemsChange</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> gemsChange = createParameter("gemsChange", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.check_daily.pass</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Byte> pass = createParameter("pass", org.jooq.impl.SQLDataType.TINYINT, false);

	/**
	 * The parameter <code>Account.check_daily.outTime</code>.
	 */
	public static final org.jooq.Parameter<java.sql.Timestamp> outTime = createParameter("outTime", org.jooq.impl.SQLDataType.TIMESTAMP, false);

	/**
	 * Create a new routine call instance
	 */
	public Check_daily() {
		super("check_daily", Account.Account);

		addInParameter(accountId_in);
		addInParameter(coinsChange);
		addInParameter(gemsChange);
		addOutParameter(pass);
		addOutParameter(outTime);
	}

	/**
	 * Set the <code>accountId_in</code> parameter IN value to the routine
	 */
	public void setAccountId_in(java.lang.Integer value) {
		setValue(Check_daily.accountId_in, value);
	}

	/**
	 * Set the <code>coinsChange</code> parameter IN value to the routine
	 */
	public void setCoinsChange(java.lang.Integer value) {
		setValue(Check_daily.coinsChange, value);
	}

	/**
	 * Set the <code>gemsChange</code> parameter IN value to the routine
	 */
	public void setGemsChange(java.lang.Integer value) {
		setValue(Check_daily.gemsChange, value);
	}

	/**
	 * Get the <code>pass</code> parameter OUT value from the routine
	 */
	public java.lang.Byte getPass() {
		return getValue(pass);
	}

	/**
	 * Get the <code>outTime</code> parameter OUT value from the routine
	 */
	public java.sql.Timestamp getOutTime() {
		return getValue(outTime);
	}
}
