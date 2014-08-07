package pizzashop.utils;


public class PreConditions {

	public static <T> T checkFound(final T resource, final Long id) {
		if (resource == null) {
		    throw new ResourceNotFoundException(id);
		}
		return resource;
	}
}
