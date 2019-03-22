package ca.ulaval.tp2.glo3004.car;

/**
 * Classe permettant des mouvements sécuritaires et des mouvements dans
 * des directions opposées
 */
public class Movement {

	// Attributs privés
	private Action[] actionsWhenPriority;
	private Action[] actionsWhenOppositeSideOn;
	
	// Constructeur avec parametres 
	public Movement(Action[] actionsWhenPriority, Action[] actionsWhenOppositeSideOn) {
		this.actionsWhenPriority = actionsWhenPriority;
		this.actionsWhenOppositeSideOn = actionsWhenOppositeSideOn;
	}
	
	// Méthode permettant la lecture des actions prioritaires
	public Action[] getActionWhenPriority() {
		return this.actionsWhenPriority;
	}
	
	// Méthode permettant la lecture des actions de directions opposées
	public Action[] getActionWhenOppositeSideOn() {
		return this.actionsWhenOppositeSideOn;
	}
}
