package scoreBoard;

public class BallEvent {
	
	int runs;
    boolean wicket;

    public BallEvent(int runs, boolean wicket) {
        this.runs = runs;
        this.wicket = wicket;
    }
}