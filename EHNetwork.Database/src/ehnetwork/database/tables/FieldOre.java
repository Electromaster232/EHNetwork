/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.FieldOreRecord;

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
public class FieldOre extends org.jooq.impl.TableImpl<FieldOreRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = -20930501;

	/**
	 * The reference instance of <code>Account.fieldOre</code>
	 */
	public static final FieldOre fieldOre = new FieldOre();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<FieldOreRecord> getRecordType() {
		return FieldOreRecord.class;
	}

	/**
	 * The column <code>Account.fieldOre.id</code>.
	 */
	public final org.jooq.TableField<FieldOreRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.fieldOre.server</code>.
	 */
	public final org.jooq.TableField<FieldOreRecord, java.lang.String> server = createField("server", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.fieldOre.location</code>.
	 */
	public final org.jooq.TableField<FieldOreRecord, java.lang.String> location = createField("location", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * Create a <code>Account.fieldOre</code> table reference
	 */
	public FieldOre() {
		this("fieldOre", null);
	}

	/**
	 * Create an aliased <code>Account.fieldOre</code> table reference
	 */
	public FieldOre(java.lang.String alias) {
		this(alias, FieldOre.fieldOre);
	}

	private FieldOre(java.lang.String alias, org.jooq.Table<FieldOreRecord> aliased) {
		this(alias, aliased, null);
	}

	private FieldOre(java.lang.String alias, org.jooq.Table<FieldOreRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<FieldOreRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_fieldOre;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<FieldOreRecord> getPrimaryKey() {
		return Keys.KEY_fieldOre_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<FieldOreRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<FieldOreRecord>>asList(Keys.KEY_fieldOre_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldOre as(java.lang.String alias) {
		return new FieldOre(alias, this);
	}

	/**
	 * Rename this table
	 */
	public FieldOre rename(java.lang.String name) {
		return new FieldOre(name, null);
	}
}