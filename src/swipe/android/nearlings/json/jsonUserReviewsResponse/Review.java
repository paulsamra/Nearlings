package swipe.android.nearlings.json.jsonUserReviewsResponse;

public class Review{
	int id, created_by, need_id, effort_rating, quality_rating, timeliness_rating;
	String message;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public int getNeed_id() {
		return need_id;
	}
	public void setNeed_id(int need_id) {
		this.need_id = need_id;
	}
	public int getEffort_rating() {
		return effort_rating;
	}
	public void setEffort_rating(int effort_rating) {
		this.effort_rating = effort_rating;
	}
	public int getQuality_rating() {
		return quality_rating;
	}
	public void setQuality_rating(int quality_rating) {
		this.quality_rating = quality_rating;
	}
	public int getTimeliness_rating() {
		return timeliness_rating;
	}
	public void setTimeliness_rating(int timeliness_rating) {
		this.timeliness_rating = timeliness_rating;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}