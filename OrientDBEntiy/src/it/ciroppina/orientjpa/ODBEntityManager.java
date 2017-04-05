package it.ciroppina.orientjpa;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public final class ODBEntityManager {
	
	public OObjectDatabaseTx db;
	private String jdbcUrl = "";
	private String dbUser = "";
	private String dbPasswd = "";
	
	public OObjectDatabaseTx getDb() {
		return this.db;
	}

	public OObjectDatabaseTx factory() {
		try {
			db = new OObjectDatabaseTx (jdbcUrl).open(dbUser, dbPasswd);
			db.getEntityManager().registerEntityClasses("it.ciroppina.orientjpa");
//			db.getEntityManager().registerEntityClass(it.ciroppina.orientjpa.Company.class);
//			db.getEntityManager().registerEntityClass(it.ciroppina.orientjpa.Department.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this.db;
	}


	public <T> T newInstance(Class<T> t) {
		return this.db.newInstance(t);
	}
	
	public ODBEntityManager() {
		super();
		jdbcUrl = "remote:localhost/BetterDemo";
		dbUser = "root";
		dbPasswd = "isigroup";
		this.factory();
	}

	public ODBEntityManager(String url, String username, String passwd) {
		super();
		this.jdbcUrl = url;
		this.dbUser = username;
		this.dbPasswd = passwd;
		this.factory();
	}

	@SuppressWarnings("unchecked")
	public <T> T save (T entity) {
		T r = null;
		this.db.getEntityManager().registerEntityClass(entity.getClass());
		try {
			r = (T) this.db.save(entity);
		} catch (Throwable t) {
			System.out.println("ODBEntityManager - save error: " 
				+ t.getLocalizedMessage());
			t.printStackTrace(); 
		}
		return r == null ? entity : r;
	}

//	public Object osave (Object entity) {
//		Object  r = this.db.save(entity);
//		return r;
//	}

	public void destroy() {
		this.db.close();
	}

	public void commit() {
		this.db.commit();
	}
}
