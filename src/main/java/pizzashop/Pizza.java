package pizzashop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pizza")
public class Pizza {
  @Id 
  @GeneratedValue(strategy=GenerationType.IDENTITY) 
  private long id;
  
  private String name;
  private double price;
  
  /* getters & setters */
  public long getId() {
	return id;
  }
  public void setId(long id) {
	this.id = id;
  }
  public String getName() {
	return name;
  }
  public void setName(String name) {
	this.name = name;
  }
  public double getPrice() {
	return price;
  }
  public void setPrice(double price) {
	this.price = price;
  }
}