package pizzashop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pizzashop.Pizza;
import pizzashop.dao.PizzaDAO;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {
   
  @Autowired private PizzaDAO pizzaDAO;
   
  /**
   * This handler method is invoked when
   * http://localhost:8080/pizzashop is requested.
   * The method returns view name "index"
   * which will be resolved into /WEB-INF/index.jsp.
   *  See src/main/webapp/WEB-INF/servlet-context.xml
   */
  @RequestMapping(method=RequestMethod.GET)
  public String list(Model model) {
    List<Pizza> pizzas = pizzaDAO.findAll();
    model.addAttribute("pizzas", pizzas);
    return "index";
  }
  
  @RequestMapping(method=RequestMethod.GET, produces={"application/json"})
  @ResponseStatus(HttpStatus.OK)  
  @ResponseBody
  public List<Pizza> list() {
	  return pizzaDAO.findAll();	  
  }
}
