package miliardi.di.records;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.orientechnologies.orient.core.db.OPartitionedDatabasePool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.iterator.ORecordIteratorCluster;
import com.orientechnologies.orient.core.record.impl.ODocument;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InsertMiliardiDiRecords extends TestCase {

	private static ODatabaseDocumentTx _db = null;
//	private String dbUrl = "plocal:c:/Programmi/orientdb-community-2.1.9/databases/mortimer";
//  private String dbUrl = "remote:localhost/mortimer"; //2424 ?
	private static String dbUrl = "memory:miliardi";
	
	static {
		/* in memory create DB */
		_db = new ODatabaseDocumentTx(dbUrl).create();
	}
	
	@Test
	public void test0_OpenCloseDocumentDB() {
		/* simple connection */
		//open _db = new ODatabaseDocumentTx("remote:localhost/miliardi").open("admin", "admin");
		/* pooled connection */
		_db= new OPartitionedDatabasePool(dbUrl, "admin", "admin").acquire();
		assertTrue("DB miliardi should be opened", !_db.isClosed() );
		_db.close();
		assertTrue("DB miliardi should be closed", _db.isClosed());
	}
	
	@Test
	public void test1_Transaction() {
		/* pooled connection */
		_db= new OPartitionedDatabasePool(dbUrl, "admin", "admin").acquire();
		try {
			_db.begin();
			_db.commit();
			_db.close();
			assertTrue("DB miliardi should be closed", _db.isClosed());
		} catch (Throwable e) {
			_db.rollback();
			assertTrue("DB miliardi should rollback", !_db.isClosed());
		}
	}
	
	@Test
	public void test6_Delete() {
		/* pooled connection */
		_db= new OPartitionedDatabasePool(dbUrl, "admin", "admin").acquire();
		_db.declareIntent( new OIntentMassiveInsert() );
		ORecordIteratorCluster<ODocument> pictures =null, persons =null;
		try {
			double start = System.currentTimeMillis() / 1000.0d;
			_db.begin();
			pictures = _db.browseCluster("picture");
			persons  = _db.browseCluster("person");
			int count = 0;
			for (ODocument picture : pictures) {
				picture.delete();
				count ++;
				if (count % 1000 == 0) {
					_db.commit();
					_db.begin();
				}
			}
			assertTrue("Transaction should be wellgone!!!", !_db.browseCluster("picture").hasNext());
			for (ODocument person : persons) {
				person.delete();
				count ++;
				if (count % 1000 == 0) {
					_db.commit();
					_db.begin();
				}
			}
			assertTrue("Transaction should be wellgone!!!", !_db.browseCluster("person").hasNext());
			_db.commit();
			double stop = System.currentTimeMillis() / 1000.0d;
			
			System.out.println("Ci ha messo: "+(stop - start)+" secondi, a cancellare "
					+ count + " records");
			_db.declareIntent(null);
		} catch (Throwable e) {
			_db.rollback();
			assertFalse("Transaction should not be wellgone...", (0>0));
			e.printStackTrace();
		} finally {
			_db.close();
		}
	}

	static int howmany = 10000;
	@Test
	public void test2_Insert() {
		/* pooled connection */
		_db= new OPartitionedDatabasePool(dbUrl, "admin", "admin").acquire();
		_db.declareIntent( new OIntentMassiveInsert() );
		byte[] image = readImage();
		try {
			double start = System.currentTimeMillis() / 1000;
			for (int i=0; i < howmany; i++) {
				//not-in-memory: _db.begin(); 
				ODocument person = createDocumentGraph(i, image);
				person.save();
				person = null;
				//not-in-memory: _db.commit();
			}
			double stop = System.currentTimeMillis() / 1000;
			
			System.out.println("Ci ha messo: "+(stop - start)+" secondi, a inserire " 
					+ howmany + " records");
			_db.declareIntent(null);
			assertTrue("Transaction should be wellgone!!!", (0<1)); //here, all things went well
		} catch (Throwable e) {
			_db.rollback();
			assertFalse("Transaction should not be wellgone...", (0>0));
			e.printStackTrace();
		} finally {
			_db.close();
		}
	}

	@Test
	public void test4_Read_And_WriteOnceToDisk() {
		/**
		 * forcing delete output directory
		 */
		try {
			FileUtils.forceDelete(new File("C:/Users/Administrator/Pictures/chicche/oparte/"));
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		/* pooled connection */
		_db= new OPartitionedDatabasePool(dbUrl, "admin", "admin").acquire();
		ORecordIteratorCluster<ODocument> iter = _db.browseCluster("person");
		ODocument person =null; int count =0;
		
		double start = System.currentTimeMillis() / 1000;
		if (iter.hasNext())  {
			person = iter.next();
			ODocument picture = (ODocument) person.field("photo");
			//String buffer = picture.field("picture");
			byte[] buffer = picture.field("picture", byte[].class);
			writeToDisk(0, buffer);
			count++;
		}
		double stop = System.currentTimeMillis() / 1000;
		System.out.println("Ci ha messo: "+(stop - start)+" secondi, a leggere e scrivere "
				+ count + " records su disco");
	}
	
	@Test
	public void test5_MassiveRead_Amd_WriteToDisk() {
		/* pooled connection */
		_db= new OPartitionedDatabasePool(dbUrl, "admin", "admin").acquire();
		ORecordIteratorCluster<ODocument> iter = _db.browseCluster("person");
		ODocument photo =null; int i=0;
		
		double start = System.currentTimeMillis() / 1000;
		while (iter.hasNext())  {
			photo = iter.next();
			ODocument picture = (ODocument) photo.field("photo");
			byte[] buffer = picture.field("picture", byte[].class);
			i = writeToDisk(i, buffer);
		}
		double stop = System.currentTimeMillis() / 1000;
		System.out.println("Ci ha messo: "+(stop - start)+" secondi, a leggere e scrivere " 
				+ i + " records su altrettanti files del disco");
	}

	private int writeToDisk(int i, byte[] buffer) {
		try {
			if (! new File("C:/Users/Administrator/Pictures/chicche/oparte/").isDirectory() ) {
				new File("C:/Users/Administrator/Pictures/chicche/oparte/").mkdirs();
			}
			BufferedOutputStream out = 
				new BufferedOutputStream(
				new FileOutputStream(
				new File("C:/Users/Administrator/Pictures/chicche/oparte/Stefout"+(i++)+".png")));
			out.write(buffer);
			out.flush();
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return i;
	}

	private ODocument createDocumentGraph(int i, byte[] b) {
		ODocument person = _db.newInstance("Person"); //new ODocument("Person");
		
		//person.reset(); person.setClassName("Person");
		person.field("name", "Name"+i );
		person.field("surname", "Skywalker"+i );
		person.field("bornAt","SpaceCity"+i);
		person.field("country", "Planet"+i);
		person.field("photo", _db.newInstance("Picture").field("picture", b) ); // new ODocument("Picture")
		person.field("meta", "{\"metadata\": {\"hasPicture\": \"true\", \"insertDate\" : " 
				+ new Date().toString() + "}}"+i);
		return person;
	}

	private byte[] readImage() {
		BufferedInputStream in;
		byte[] b = null;
		try {
			in = new BufferedInputStream(
				new FileInputStream(new File("C:/Users/Administrator/Pictures/chicche/oparte.jpg"))); //kia
			int space = in.available();
			b = new byte[space];
			in.read(b);
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return b;
	}
}
