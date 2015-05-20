package swipe.android.nearlings.json.UserHistory;

import android.os.Parcel;
import android.os.Parcelable;

public class TxnDetails implements Parcelable {
	String recipient_username, recipient_name;
	int task_id;
	String task_title;
	float task_cost;
	float fee, total_charge;

	float earnings;

	public float getEarnings() {
		return earnings;
	}

	public void setEarnings(float earnings) {
		this.earnings = earnings;
	}

	public String getRecipient_username() {
		return recipient_username;
	}

	public void setRecipient_username(String recipient_username) {
		this.recipient_username = recipient_username;
	}

	public String getRecipient_name() {
		return recipient_name;
	}

	public void setRecipient_name(String recipient_name) {
		this.recipient_name = recipient_name;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getTask_title() {
		return task_title;
	}

	public void setTask_title(String task_title) {
		this.task_title = task_title;
	}

	public float getTask_cost() {
		return task_cost;
	}

	public void setTask_cost(float task_cost) {
		this.task_cost = task_cost;
	}

	public float getFee() {
		return fee;
	}

	public void setFee(float fee) {
		this.fee = fee;
	}

	public float getTotal_charge() {
		return total_charge;
	}

	public void setTotal_charge(float total_charge) {
		this.total_charge = total_charge;
	}

	private TxnDetails(Parcel in) {
		readFromParcel(in);

	}

	private void readFromParcel(Parcel in) {
		// This order must match the order in writeToParcel()

		recipient_username = in.readString();
		recipient_name = in.readString();
		task_id = in.readInt();
		task_title = in.readString();
		task_cost = in.readFloat();
		fee = in.readFloat();
		total_charge = in.readFloat();

		earnings = in.readFloat();
	}

	public void writeToParcel(Parcel out, int flags) {

		out.writeString(recipient_username);
		out.writeString(recipient_name);
		out.writeInt(task_id);
		out.writeString(task_title);
		out.writeFloat(task_cost);
		out.writeFloat(fee);
		out.writeFloat(total_charge);
	}

	// Just cut and paste this for now
	public int describeContents() {
		return 0;
	}

	// Just cut and paste this for now
	public static final Parcelable.Creator<TxnDetails> CREATOR = new Parcelable.Creator<TxnDetails>() {
		public TxnDetails createFromParcel(Parcel in) {
			return new TxnDetails(in);
		}

		public TxnDetails[] newArray(int size) {
			return new TxnDetails[size];
		}
	};
}