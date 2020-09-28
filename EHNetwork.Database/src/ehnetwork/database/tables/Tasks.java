/**
 * This class is generated by jOOQ
 */
package ehnetwork.database.tables;

import ehnetwork.database.Account;
import ehnetwork.database.Keys;
import ehnetwork.database.tables.records.TasksRecord;

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
public class Tasks extends org.jooq.impl.TableImpl<TasksRecord> implements java.io.Serializable, java.lang.Cloneable {

	private static final long serialVersionUID = 787559873;

	/**
	 * The reference instance of <code>Account.tasks</code>
	 */
	public static final Tasks tasks = new Tasks();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<TasksRecord> getRecordType() {
		return TasksRecord.class;
	}

	/**
	 * The column <code>Account.tasks.id</code>.
	 */
	public final org.jooq.TableField<TasksRecord, java.lang.Integer> id = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>Account.tasks.name</code>.
	 */
	public final org.jooq.TableField<TasksRecord, java.lang.String> name = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(100), this, "");

	/**
	 * Create a <code>Account.tasks</code> table reference
	 */
	public Tasks() {
		this("tasks", null);
	}

	/**
	 * Create an aliased <code>Account.tasks</code> table reference
	 */
	public Tasks(java.lang.String alias) {
		this(alias, Tasks.tasks);
	}

	private Tasks(java.lang.String alias, org.jooq.Table<TasksRecord> aliased) {
		this(alias, aliased, null);
	}

	private Tasks(java.lang.String alias, org.jooq.Table<TasksRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, Account.Account, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<TasksRecord, java.lang.Integer> getIdentity() {
		return Keys.IDENTITY_tasks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<TasksRecord> getPrimaryKey() {
		return Keys.KEY_tasks_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<TasksRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<TasksRecord>>asList(Keys.KEY_tasks_PRIMARY, Keys.KEY_tasks_name_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tasks as(java.lang.String alias) {
		return new Tasks(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Tasks rename(java.lang.String name) {
		return new Tasks(name, null);
	}
}