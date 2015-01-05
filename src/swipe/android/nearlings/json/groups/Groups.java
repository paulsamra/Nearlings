package swipe.android.nearlings.json.groups;

import swipe.android.nearlings.jsonResponses.explore.NearlingsTime;

public class Groups{
	String id, name, description, visibility, category, created_by, membercount, eventcount, needcount, address1, address2, city, state, zip;
	NearlingsTime created_at;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getMembercount() {
		return membercount;
	}
	public void setMembercount(String membercount) {
		this.membercount = membercount;
	}
	public String getEventcount() {
		return eventcount;
	}
	public void setEventcount(String eventcount) {
		this.eventcount = eventcount;
	}
	public String getNeedcount() {
		return needcount;
	}
	public void setNeedcount(String needcount) {
		this.needcount = needcount;
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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public NearlingsTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(NearlingsTime created_at) {
		this.created_at = created_at;
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