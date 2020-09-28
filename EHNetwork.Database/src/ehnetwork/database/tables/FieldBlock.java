/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.FieldBlockRecord;

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
public class FieldBlock extends org.jooq.impl.TableImpl<FieldBlockRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 584309427;

	/**
	 * The reference instance of <code>Account.fieldBlock</code>
	 */
	public static final FieldBlock fieldBlock = new FieldBlock();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<FieldBlockRecord> getRecordType() {
		return FieldBlockRecord.class;
	}

	/**
	 * The column <code>Account.fieldBlock.id</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.fieldBlock.server</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.String> server = createField("server", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.fieldBlock.location</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.String> location = createField("location", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * The column <code>Account.fieldBlock.blockId</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Integer> blockId = createField("blockId", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>Account.fieldBlock.blockData</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Byte> blockData = createField("blockData", org.jooq.impl.SQLDataType.TINYINT, this, "");

	/**
	 * The column <code>Account.fieldBlock.emptyId</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Integer> emptyId = createField("emptyId", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>Account.fieldBlock.emptyData</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Byte> emptyData = createField("emptyData", org.jooq.impl.SQLDataType.TINYINT, this, "");

	/**
	 * The column <code>Account.fieldBlock.stockMax</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Integer> stockMax = createField("stockMax", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>Account.fieldBlock.stockRegenTime</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.Double> stockRegenTime = createField("stockRegenTime", org.jooq.impl.SQLDataType.DOUBLE, this, "");

	/**
	 * The column <code>Account.fieldBlock.loot</code>.
	 */
	public final org.jooq.TableField<FieldBlockRecord, java.lang.String> loot = createField("loot", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * Create a <code>Account.fieldBlock</code> table reference
	 */
	public FieldBlock() {
		this("fieldBlock", null);
	}

	/**
	 * Create an aliased <code>Account.fieldBlock</code> table reference
	 */
	public FieldBlock(java.lang.String alias) {
		this(alias, FieldBlock.fieldBlock);
	}

	private FieldBlock(java.lang.String alias, org.jooq.Table<FieldBlockRecord> aliased) {
		this(alias, aliased, null);
	}

	private FieldBlock(java.lang.String alias, org.jooq.Table<FieldBlockRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<FieldBlockRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_fieldBlock;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<FieldBlockRecord> getPrimaryKey() {
		return Keys.KEY_fieldBlock_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<FieldBlockRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<FieldBlockRecord>>asList(Keys.KEY_fieldBlock_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FieldBlock as(java.lang.String alias) {
		return new FieldBlock(alias, this);
	}

	/**
	 * Rename this table
	 */
	public FieldBlock rename(java.lang.String name) {
		return new FieldBlock(name, null);
	}
}