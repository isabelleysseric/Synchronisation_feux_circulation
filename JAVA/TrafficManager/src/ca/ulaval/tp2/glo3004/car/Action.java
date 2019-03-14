package ca.ulaval.tp2.glo3004.car;

public enum Action {

	CONTINUE(0), TURN_LEFT(1), TURN_RIGHT(2);
	
	private final int value;

    private Action(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
