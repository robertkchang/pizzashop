package pizzashop.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
}