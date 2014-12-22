package swipe.android.nearlings;

import swipe.android.nearlings.MessagesSync.Needs;

import com.edbert.library.network.sync.JsonResponseInterface;

public class JsonChangeStateResponse implements JsonResponseInterface{

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return true;
	}
	//dummy
	public static String getStatus(String state){
		if (state.equals(Needs.NOT_ACCEPTED_YET)) {
		return Needs.PENDING;
		} else if (state.equals(Needs.PENDING)) {
			return Needs.DONE_WAITING_FOR_REVIEW;
		} else if (state.equals(Needs.DONE_WAITING_FOR_REVIEW)) {
			return Needs.FINISHED;
		}else{
			return "UKNOWN STATE";
		}
		
	}
	
}