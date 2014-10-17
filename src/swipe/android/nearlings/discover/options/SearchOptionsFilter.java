package swipe.android.nearlings.discover.options;

public class SearchOptionsFilter{
	boolean isSelected = false;
	int unselectedIcon, selectedIcon;
	public SearchOptionsFilter(boolean isSelected, int unselectedIcon, int selectedIcon){
		this.isSelected = isSelected;
		this.unselectedIcon = unselectedIcon;
		this.selectedIcon = selectedIcon;
	}
	public boolean isSelected(){
		return isSelected;
	}
	public int getUnselectedIcon(){
		return unselectedIcon;
	}
	public int getSelectedIcon(){
		return selectedIcon;
	}
	public void setSelected(boolean value){
		isSelected = value;
	}
}