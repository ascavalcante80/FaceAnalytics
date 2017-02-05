/**
 * 
 */
package facebook;

/**
 * @author alexandre
 *
 */
public class ProfileElement extends FacebookElement {

	private String name;

	public ProfileElement(String id, String name) {
		super(id);
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
