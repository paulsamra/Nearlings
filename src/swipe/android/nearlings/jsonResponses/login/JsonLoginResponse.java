package swipe.android.nearlings.jsonResponses.login;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonLoginResponse implements JsonResponseInterface{

	@Override
	public boolean isValid() {
		return error == null;
	}
	private String error, token, username, firstname, description, lastname, mobile, email, gravitar;
	int balance, holdbalance, user_rating, rating_count, taskcount, alertcount, todocount;
	String address1, address2, city, state;
	int zip;
	String timezone;
	int alerts;
	String alertchannel;
	double latitude, longitude;
	int[] memberships;
	private int userID;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGravitar() {
		return gravitar;
	}
	public void setGravitar(String gravitar) {
		this.gravitar = gravitar;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getHoldbalance() {
		return holdbalance;
	}
	public void setHoldbalance(int holdbalance) {
		this.holdbalance = holdbalance;
	}
	public int getUser_rating() {
		return user_rating;
	}
	public void setUser_rating(int user_rating) {
		this.user_rating = user_rating;
	}
	public int getRating_count() {
		return rating_count;
	}
	public void setRating_count(int rating_count) {
		this.rating_count = rating_count;
	}
	public int getTaskcount() {
		return taskcount;
	}
	public void setTaskcount(int taskcount) {
		this.taskcount = taskcount;
	}
	public int getAlertcount() {
		return alertcount;
	}
	public void setAlertcount(int alertcount) {
		this.alertcount = alertcount;
	}
	public int getTodocount() {
		return todocount;
	}
	public void setTodocount(int todocount) {
		this.todocount = todocount;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public int getAlerts() {
		return alerts;
	}
	public void setAlerts(int alerts) {
		this.alerts = alerts;
	}
	public String getAlertchannel() {
		return alertchannel;
	}
	public void setAlertchannel(String alertchannel) {
		this.alertchannel = alertchannel;
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
	public int[] getMemberships() {
		return memberships;
	}
	public void setMemberships(int[] memberships) {
		this.memberships = memberships;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}