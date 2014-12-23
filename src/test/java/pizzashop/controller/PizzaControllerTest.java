package pizzashop.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pizzashop.Pizza;
import pizzashop.dao.PizzaDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test-context.xml") // Create test context using H2
@WebAppConfiguration
public class PizzaControllerTest {

	public static final MediaType APPLICATION_JSON = MediaType.APPLICATION_JSON;
		
	private MockMvc mockMvc;

	@Mock // @Mock here is same as mock(PizzaDAO.class)
	private PizzaDAO pizzaMock;

	@Autowired  // Need to autowire controller or @InjectMocks does not inject pizzaMock
	@InjectMocks
	private PizzaController pizzaController;
	    
    @Autowired 
    private WebApplicationContext ctx;
    
    @Before
    public void doSetup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build(); // don't use standalone
    }
    
	@Test
	public void find_all_pizzas_ShouldReturnFoundPizzaEntries() throws Exception {
		Pizza first = new Pizza();
		first.setId(1L);
		first.setName("Yummy");
		first.setPrice(10);
		
		Pizza second = new Pizza();
		second.setId(2L);
		second.setName("Yuck");
		second.setPrice(20);

		when(pizzaMock.findAll()).thenReturn(Arrays.asList(first, second));
		
		mockMvc.perform(get("/pizzas.json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].name", is("Yummy")))
        .andExpect(jsonPath("$[0].price", is(10.0)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].name", is("Yuck")))
        .andExpect(jsonPath("$[1].price", is(20.0)));

		verify(pizzaMock, times(1)).findAll();
		verifyNoMoreInteractions(pizzaMock);
	}
	
	@Test
	public void find_by_id_pizza_ShouldReturnFoundPizza() throws Exception {
		Pizza first = new Pizza();
		first.setId(1L);
		first.setName("Yummy");
		first.setPrice(10);
		
		Pizza second = new Pizza();
		second.setId(2L);
		second.setName("Yuck");
		second.setPrice(20);

		when(pizzaMock.findByID(1L)).thenReturn(first);
		
		mockMvc.perform(get("/pizzas/1.json"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("Yummy")))
        .andExpect(jsonPath("$.price", is(10.0)));

        verify(pizzaMock, times(1)).findByID(1L);
		verifyNoMoreInteractions(pizzaMock);
	}
	
	@Test
	public void createPizza() throws Exception {
		
		Pizza createMe = new Pizza();
		createMe.setId(1);
		createMe.setName("New Pizza");
		createMe.setPrice(15D);
		
		when(pizzaMock.save(any(Pizza.class))).thenReturn(createMe);

		mockMvc.perform(post("/pizzas.json")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", createMe.getName())
                .param("price", Double.toString(createMe.getPrice())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(createMe.getId())))))
                .andExpect(jsonPath("$.name", is(createMe.getName())))
                .andExpect(jsonPath("$.price", is(createMe.getPrice())));		
		
		ArgumentCaptor<Pizza> captor = ArgumentCaptor.forClass(Pizza.class);
        verify(pizzaMock, times(1)).save(captor.capture());
		verifyNoMoreInteractions(pizzaMock);
		
		// validate argument into PizzaController.create
		Pizza pizzaArg = (Pizza) captor.getValue();
		assertThat(pizzaArg.getId(), is(0L));
		assertThat(pizzaArg.getName(), is(createMe.getName()));
		assertThat(pizzaArg.getPrice(), is(createMe.getPrice()));
	}
	
	@Test
	public void updatePizza() throws Exception {
		
		Pizza first = new Pizza();
		first.setId(1L);
		first.setName("New Pizza");
		first.setPrice(15D);
		
		Pizza updated = new Pizza();
		updated.setId(1);
		updated.setName("New Pizzax");
		updated.setPrice(15D);
		
		when(pizzaMock.findByID(1L)).thenReturn(first);
		when(pizzaMock.save(any(Pizza.class))).thenReturn(updated);

		mockMvc.perform(put("/pizzas/1.json")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Pizzax"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Integer.parseInt(String.valueOf(updated.getId())))))
                .andExpect(jsonPath("$.name", is(updated.getName())))
                .andExpect(jsonPath("$.price", is(updated.getPrice())));		
		
		ArgumentCaptor<Pizza> captor = ArgumentCaptor.forClass(Pizza.class);
        verify(pizzaMock, times(1)).save(captor.capture());
		
		// validate argument into PizzaController.update
		Pizza pizzaArg = (Pizza) captor.getValue();
		assertThat(pizzaArg.getId(), is(1L));
		assertThat(pizzaArg.getName(), is(updated.getName()));
		assertThat(pizzaArg.getPrice(), is(0D)); // not a param value
	}
	
	@Test
	public void destroyPizza() throws Exception {
		
		Pizza first = new Pizza();
		first.setId(1L);
		first.setName("New Pizza");
		first.setPrice(15D);
		
		when(pizzaMock.findByID(1L)).thenReturn(first);
		Mockito.doNothing().when(pizzaMock).delete(any(Pizza.class));
		
		mockMvc.perform(delete("/pizzas/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());                
	
		ArgumentCaptor<Pizza> captor = ArgumentCaptor.forClass(Pizza.class);
        verify(pizzaMock, times(1)).delete(captor.capture());
		
		// validate argument into PizzaController.destroy
		Pizza pizzaArg = (Pizza) captor.getValue();
		assertThat(pizzaArg.getId(), is(1L));
	}
}
