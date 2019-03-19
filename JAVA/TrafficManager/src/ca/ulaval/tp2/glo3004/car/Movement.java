package ca.ulaval.tp2.glo3004.car;

public class Movement {

	private Action[] actionsWhenPriority;
	private Action[] actionsWhenOppositeSideOn;
	
	public Movement(Action[] actionsWhenPriority, Action[] actionsWhenOppositeSideOn) {
		this.actionsWhenPriority = actionsWhenPriority;
		this.actionsWhenOppositeSideOn = actionsWhenOppositeSideOn;
	}
	
	public Action[] getActionWhenPriority() {
		return this.actionsWhenPriority;
	}
	
	public Action[] getActionWhenOppositeSideOn() {
		return this.actionsWhenOppositeSideOn;
	}
}
