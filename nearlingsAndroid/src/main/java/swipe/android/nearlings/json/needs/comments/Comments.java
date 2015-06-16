package swipe.android.nearlings.json.needs.comments;

public class Comments implements Comparable<Comments>{
	String id, sender, sender_id, sender_thumbnail, message;
	long time;
	public Comments(){
		setId("ID");
		setSender("Sender");
		setSender_id("SenderID");
		setSender_thumbnail("http://www.parse.com/images/nuget/package_icon.png");
		setMessage("Message");
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSender_id() {
		return sender_id;
	}
	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}
	public String getSender_thumbnail() {
		return sender_thumbnail;
	}
	public void setSender_thumbnail(String sender_thumbnail) {
		this.sender_thumbnail = sender_thumbnail;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
    public int hashCode() {
		return  id.hashCode()*message.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Comments))
            return false;
        if (obj == this)
            return true;
        Comments rhs = (Comments) obj;
        return id.equals(rhs.id) && message.equals(rhs.message);
    }

	@Override
	public int compareTo(Comments another) {
		Integer thisID = Integer.valueOf(this.id);
		Integer otherID = Integer.valueOf(another.id);
		return thisID - otherID;
	
	}
}