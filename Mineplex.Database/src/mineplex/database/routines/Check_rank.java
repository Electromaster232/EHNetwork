/**
 * This class is generated by jOOQ
 */
package mineplex.database.routines;

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
public class Check_rank extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -1491579372;

	/**
	 * The parameter <code>Account.check_rank.accountId_in</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> accountId_in = createParameter("accountId_in", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.check_rank.coinsChange</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> coinsChange = createParameter("coinsChange", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.check_rank.gemsChange</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Integer> gemsChange = createParameter("gemsChange", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>Account.check_rank.pass</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Byte> pass = createParameter("pass", org.jooq.impl.SQLDataType.TINYINT, false);

	/**
	 * The parameter <code>Account.check_rank.outTime</code>.
	 */
	public static final org.jooq.Parameter<java.sql.Date> outTime = createParameter("outTime", org.jooq.impl.SQLDataType.DATE, false);

	/**
	 * Create a new routine call instance
	 */
	public Check_rank() {
		super("check_rank", mineplex.database.Account.Account);

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
		setValue(mineplex.database.routines.Check_rank.accountId_in, value);
	}

	/**
	 * Set the <code>coinsChange</code> parameter IN value to the routine
	 */
	public void setCoinsChange(java.lang.Integer value) {
		setValue(mineplex.database.routines.Check_rank.coinsChange, value);
	}

	/**
	 * Set the <code>gemsChange</code> parameter IN value to the routine
	 */
	public void setGemsChange(java.lang.Integer value) {
		setValue(mineplex.database.routines.Check_rank.gemsChange, value);
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
	public java.sql.Date getOutTime() {
		return getValue(outTime);
	}
}
