package swipe.android.nearlings.jsonResponses.explore;

public class Tasks{
	String user, user_thumbnail, title, description, status, reward; 
	String id;
	DateNearlings created_at, due_date;
	public DateNearlings getCreated_at() {
		return created_at;
	}
	public void setCreated_at(DateNearlings created_at) {
		this.created_at = created_at;
	}
	public DateNearlings getDue_date() {
		return due_date;
	}
	public void setDue_date(DateNearlings due_date) {
		this.due_date = due_date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUser_thumbnail() {
		return user_thumbnail;
	}
	public void setUser_thumbnail(String user_thumbnail) {
		this.user_thumbnail = user_thumbnail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	double latitude, longitude;
}