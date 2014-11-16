package swipe.android.nearlings.jsonResponses.register;

//TODO: make this shared between JSON and stuff
public class Users{
	public Users(String id, String name, String iconURL) {
		super();
		this.id = id;
		this.name = name;
		this.iconURL = iconURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	String id, name, iconURL;
	
}