package manipulations;

public class Split {
	public final String text;
	public final int start;
	public final int stop;
	
	public Split(String text, int start, int stop) {
		this.start = start;
		this.stop = stop;
		this.text = text;
	}
}
