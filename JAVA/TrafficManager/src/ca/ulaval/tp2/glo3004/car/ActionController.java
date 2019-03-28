package ca.ulaval.tp2.glo3004.car;

/**
 * Classe permettant des mouvements s�curitaires et des mouvements dans
 * des directions oppos�es
 */
public class ActionController {

	// Attributs priv�s
	private Action[] actionsWhenPriority;
	private Action[] actionsWhenOppositeSideOn;
	
	// Constructeur avec parametres 
	public ActionController(Action[] actionsWhenPriority, Action[] actionsWhenOppositeSideOn) {
		this.actionsWhenPriority = actionsWhenPriority;
		this.actionsWhenOppositeSideOn = actionsWhenOppositeSideOn;
	}
	
	// M�thode permettant la lecture des actions prioritaires
	public Action[] getActionWhenPriority() {
		return this.actionsWhenPriority;
	}
	
	// M�thode permettant la lecture des actions de directions oppos�es
	public Action[] getActionWhenOppositeSideOn() {
		return this.actionsWhenOppositeSideOn;
	}
}
