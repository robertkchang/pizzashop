package pizzashop.utils;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3332292346834265371L;
	
	public ResourceNotFoundException (long id) {
		super("ResourceNotFoundException, id=" +id);
	}
}
