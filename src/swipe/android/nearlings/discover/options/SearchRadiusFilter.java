package swipe.android.nearlings.discover.options;

public class SearchRadiusFilter{
	boolean isSelected = false;
	double radius = 0;
	String tag = "";
	public SearchRadiusFilter(boolean isSelected, double radius, String tag){
		this.isSelected = isSelected;
		this.radius = radius;
		this.tag = tag;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public boolean isSelected(){
		return isSelected;
	}
	public void setSelected(boolean value){
		isSelected = value;
	}
}