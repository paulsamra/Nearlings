package swipe.android.nearlings.json.UserHistory;

import android.os.Parcel;
import android.os.Parcelable;

public class PaymentHistory implements Parcelable {
	public int getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(int buyer_id) {
		this.buyer_id = buyer_id;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTxn_type() {
		return txn_type;
	}

	public void setTxn_type(String txn_type) {
		this.txn_type = txn_type;
	}

	public TxnDetails getTxn_details() {
		return txn_details;
	}

	public void setTxn_details(TxnDetails txn_details) {
		this.txn_details = txn_details;
	}

	int payment_id, user_id, buyer_id, seller_id;
	String title;
	long created_at;
	String status;
	String txn_type;
	TxnDetails txn_details;

	// The following methods that are required for using Parcelable
	private PaymentHistory(Parcel in) {
		readFromParcel(in);

	}

	private void readFromParcel(Parcel in) {
		// This order must match the order in writeToParcel()
		payment_id = in.readInt();
		user_id = in.readInt();
		buyer_id = in.readInt();
		seller_id = in.readInt();
		title = in.readString();
		created_at = in.readLong();
		status = in.readString();
		txn_type = in.readString();
		txn_details = in.readParcelable(TxnDetails.class.getClassLoader());
	}

	public void writeToParcel(Parcel out, int flags) {
		// Again this order must match the Question(Parcel) constructor

		out.writeInt(payment_id);
		out.writeInt(user_id);
		out.writeInt(buyer_id);
		out.writeInt(seller_id);
		out.writeString(title);
		out.writeLong(created_at);
		out.writeString(status);
		out.writeString(txn_type);
		out.writeParcelable(txn_details, flags);
	}

	// Just cut and paste this for now
	public int describeContents() {
		return 0;
	}

	// Just cut and paste this for now
	public static final Parcelable.Creator<PaymentHistory> CREATOR = new Parcelable.Creator<PaymentHistory>() {
		public PaymentHistory createFromParcel(Parcel in) {
			return new PaymentHistory(in);
		}

		public PaymentHistory[] newArray(int size) {
			return new PaymentHistory[size];
		}
	};

}