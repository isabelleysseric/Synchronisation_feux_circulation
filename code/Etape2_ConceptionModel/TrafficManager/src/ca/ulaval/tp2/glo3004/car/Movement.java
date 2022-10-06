package ca.ulaval.tp2.glo3004.car;

/*
 *  Classe permettant des mouvements securitaires et des mouvements dans
 * des directions opposees
 */
public class Movement {

	private Action[] actionsWhenPriority;
	private Action[] actionsWhenOppositeSideOn;
	
	public Movement(Action[] actionsWhenPriority, Action[] actionsWhenOppositeSideOn) {
		this.actionsWhenPriority = actionsWhenPriority;
		this.actionsWhenOppositeSideOn = actionsWhenOppositeSideOn;
	}
	
	// Methode permettant la lecture des actions prioritaires
	public Action[] getActionWhenPriority() {
		return this.actionsWhenPriority;
	}
	

	// Methode permettant la lecture des actions de directions oppos�es
	public Action[] getActionWhenOppositeSideOn() {
		return this.actionsWhenOppositeSideOn;
	}
}
