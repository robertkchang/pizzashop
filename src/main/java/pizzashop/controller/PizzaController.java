package pizzashop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pizzashop.Pizza;
import pizzashop.dao.PizzaDAO;
import pizzashop.utils.PreConditions;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
   
  @Autowired private PizzaDAO pizzaDAO;
 
  @RequestMapping(method=RequestMethod.GET)
  public String list(Model model) {
    List<Pizza> pizzas = pizzaDAO.findAll();
    model.addAttribute("pizzas", pizzas);
    return "index";
  }
  
  @RequestMapping(value = "/{id}", method=RequestMethod.GET)
  public String byID(@PathVariable( "id" ) Long id, Model model) {
    List<Pizza> pizzas = new ArrayList<Pizza>();
    pizzas.add(PreConditions.checkFound(pizzaDAO.findByID(id), id));
    model.addAttribute("pizzas", pizzas);
    return "index";
  }
  
  @RequestMapping(method=RequestMethod.GET, produces={"application/json"})
  @ResponseStatus(HttpStatus.OK)  
  @ResponseBody
  public List<Pizza> list() {
	  return pizzaDAO.findAll();	  
  }
  
  @RequestMapping( value = "/{id}", method = RequestMethod.GET, produces={"application/json"} )
  @ResponseStatus(HttpStatus.OK)  
  @ResponseBody
  public Pizza byID(@PathVariable( "id" ) Long id) {
	  return PreConditions.checkFound(pizzaDAO.findByID(id), id);	  
  }
  
  @RequestMapping( method = RequestMethod.POST, produces={"application/json"})
  @ResponseStatus( HttpStatus.CREATED )
  @ResponseBody
  public Pizza create(@ModelAttribute("pizza") Pizza pizza) {
	  return pizzaDAO.save(pizza);
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces={"application/json"}, headers = "content-type=application/x-www-form-urlencoded")
  @ResponseStatus( HttpStatus.OK )
  @ResponseBody
  public Pizza update(@PathVariable("id") Long id,  @ModelAttribute("pizza") Pizza pizza, Model model) {
	  Pizza found = PreConditions.checkFound(pizzaDAO.findByID(id), id);
	  found.setName(pizza.getName());
	  found.setPrice(pizza.getPrice());
	  return pizzaDAO.save(found);
  }
  
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus( HttpStatus.OK )
  @ResponseBody
  public void destroy(@PathVariable("id") Long id, @ModelAttribute("pizza") Pizza pizza) {
	  Pizza found = PreConditions.checkFound(pizzaDAO.findByID(id), id);	  
	  pizzaDAO.delete(found);
  }
}
