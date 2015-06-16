package swipe.android.nearlings.json.needs.needsdetailsoffersresponse;

public class NeedsOffers{
	String id, message;
	int created_by, changed_by;
	String status;
	double offerprice; 
	long created_at;
	String username, thumbnail;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public int getChanged_by() {
		return changed_by;
	}
	public void setChanged_by(int changed_by) {
		this.changed_by = changed_by;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getOfferprice() {
		return offerprice;
	}
	public void setOfferprice(float offerprice) {
		this.offerprice = offerprice;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
 
}