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
public class TestPro extends org.jooq.impl.AbstractRoutine<java.lang.Void> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -418397480;

	/**
	 * The parameter <code>Account.testPro.var</code>.
	 */
	public static final org.jooq.Parameter<java.sql.Date> var = createParameter("var", org.jooq.impl.SQLDataType.DATE, false);

	/**
	 * Create a new routine call instance
	 */
	public TestPro() {
		super("testPro", Account.Account);

		addOutParameter(var);
	}

	/**
	 * Get the <code>var</code> parameter OUT value from the routine
	 */
	public java.sql.Date getVar() {
		return getValue(var);
	}
}