/**
 * 
 */
package facebook;

/**
 * @author alexandre
 *
 */
public class UserElement extends FacebookElement{
	
	private String name;

	public UserElement(String id, String name) {
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
