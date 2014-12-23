package pizzashop.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pizzashop.Pizza;

@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class PizzaDAO {
  @Autowired private SessionFactory sessionFactory;
   
  /**
   * @Transactional annotation below will trigger Spring Hibernate transaction manager to automatically create
   * a hibernate session. See src/main/webapp/WEB-INF/servlet-context.xml
   */
  @Transactional
  public List<Pizza> findAll() {
    Session session = sessionFactory.getCurrentSession();
    List pizzas = session.createQuery("from Pizza").list();
    return pizzas;
  }
  
  @Transactional
  public Pizza findByID(long id) {
    Session session = sessionFactory.getCurrentSession();
    Pizza pizza = (Pizza) session.get(Pizza.class, id);
    return pizza;
  }
  
  @Transactional
  public Pizza save(Pizza pizza) {
	  Session session = sessionFactory.getCurrentSession();
	  session.saveOrUpdate(pizza);
	  return pizza;
  }
  
  @Transactional
  public void delete(Pizza pizza) {
	  Session session = sessionFactory.getCurrentSession();
	  session.delete(pizza);	  
  }
}
