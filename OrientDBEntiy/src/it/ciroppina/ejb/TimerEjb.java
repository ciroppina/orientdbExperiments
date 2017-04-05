package it.ciroppina.ejb;

import it.ciroppina.ejb.entity.Documento;
import it.ciroppina.ejb.entity.Entita;
import it.ciroppina.ejb.entity.Offset;
import it.ciroppina.orientjpa.Company;
import it.ciroppina.orientjpa.Department;
import it.ciroppina.orientjpa.ODBEntityManager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

@Stateless
public class TimerEjb {

	Company company;
    /**
     * Default constructor. 
     */
    public TimerEjb() {
    	company = new Company();
    	company.setName("Luke SkyWalker Ltd.");
    }
	
	@Schedule(second="*/30", minute="*", hour="8-23", dayOfWeek="Mon-Fri",
      dayOfMonth="*", month="*", year="*", info="OrientDBTimer")
    private void scheduledTimeout(final Timer t) {
        System.out.println("@Schedule called at: " + new java.util.Date());
        ODBEntityManager em = new ODBEntityManager("remote:localhost/BetterDemo", "root", "isigroup");
        
        //Company company = new Company();
		company.setName("Luke SkyWalker Ltd.");
		Department dep = new Department();
		dep.setName("Dep-" + System.currentTimeMillis());
		company.getListaDept().add(dep);
		//the following returned Type<T> object is only alive before destroy of em
		Company ret = em.save(company);
		em.commit();
		for (Department d : ret.getListaDept()) {
			System.out.println(" --- SAVED ENTITY ---> Department: " + d.getName()
			  		  +"; belogning to Company: " + company.getName());
		}
		
		em.destroy();
    }

	public static void main(String[] a) {
		ODBEntityManager em = new ODBEntityManager();
		Company company = em.newInstance(Company.class);
		//new Company();
		company.setName("Luke SkyWalker Ltd.");
		
		Department dep = new Department();
		dep.setName("Department-" + System.currentTimeMillis());
		company.getListaDept().add(dep);
		dep.setName("Department-" + System.currentTimeMillis());
		company.getListaDept().add(dep);
		
		Company ret = em.save(company);
		String companyName = ret.getName();
		List<Department> depts = ret.getListaDept();
		em.commit();
		System.out.println("Ho inserito la company?: " + company.getName() .equals(companyName));
		for (Department department : depts) {
			System.out.println("La company " + company.getName()
				+" ha il Department: " + department.getName());
		}
		em.destroy();

		//anotherMain();

	}

	private static void anotherMain() {
        // OPEN THE DATABASE
        OObjectDatabaseTx db = new OObjectDatabaseTx("remote:localhost/BetterDemo").open("root", "isigroup");
 
 
        // REGISTER THE CLASS ONLY ONCE AFTER THE DB IS OPEN/CREATED
        db.getEntityManager().registerEntityClass(Entita.class);
        db.getEntityManager().registerEntityClass(Documento.class);
        db.getEntityManager().registerEntityClass(Offset.class);
 
 
        Offset offset = db.newInstance(Offset.class);
        offset.setStart("1098");
        offset.setEnd("2098");
 
 
        Entita entita = new Entita();
        entita.setDescrizione("La prima");
        entita.setDescrizione("TESTANDOLA");
        
        ArrayList<Offset> listaOffset = new ArrayList<Offset>();
        listaOffset.add(offset);
        entita.setListaOffset(listaOffset);
        
        
        
        Documento doc = new Documento();
        doc.setFascicolo("pippo");
        doc.setFase("fase");
        doc.setNomeDocumento("nomeDocumento");
        ArrayList<Entita> list = new ArrayList<Entita>();
        list.add(entita);
        doc.setListaEntita(list);
 
        Documento retD = db.save(doc);
        String retE0StartOffset = retD.getListaEntita().get(0).getListaOffset().get(0).getStart();
        System.out.println("0 Entity first start offset = " + retE0StartOffset);
        String actual = retD.getNomeDocumento();
        System.out.println("appena salvato il doc.to: " + actual .equals(doc.getNomeDocumento()));
		
	}
}