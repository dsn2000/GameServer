package server.services.mechanic;

public enum PointState {
	BLUE, RED, EMPTY;
	
	public String toString() {
		if (PointState.BLUE == this) {
			return "blue";
		}
		if (PointState.RED == this) {
			return "red";
		}
		if (PointState.EMPTY == this) {
			return "empty";
		}
		return null;
	}
}
