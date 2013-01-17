package server.services.mechanic;

public class Field {
	private int rowCount;
	private int columnCount;
	private PointState[][] points;
	
	public Field(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		initializeField(rowCount, columnCount);		
	}
	
	private void initializeField(int rowCount, int columnCount) {
		points = new PointState[rowCount][columnCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				points[i][j] = PointState.EMPTY;
			}
		}
	}
	
	public void setPointState(int i, int j, PointState state) {
		points[i][j] = state;		
	}
	
	public PointState getPointState(int i, int j) {
		return points[i][j];
	}
	
	public int getRowCount() {
		return rowCount;
	}
	
	public int getColumnCount() {
		return columnCount;
	}
	
	public boolean isEmpty(int i, int j) {
		if (points[i][j] == PointState.EMPTY) {
			return true;
		}
		return false;
	}
	
	public String toJSON() {
		StringBuilder buff = new StringBuilder();
		int count = 0;
		buff.append("'[");
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				if (!isEmpty(i, j)) {
					String colorStr = getPointState(i,j) == PointState.BLUE ? "\"blue\"" : "\"red\"";
					if (count > 0)
						buff.append(",");
					buff.append("[" + i + "," + j + "," + colorStr + "]");
					count++;
				}
			}
		}
		buff.append("]'");
		return buff.toString();
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof Field)) {
			return false;
		}
		
		Field field = (Field) obj;
		if (field.getRowCount() != this.getRowCount() || 
			field.getColumnCount() != this.getColumnCount()) {
			return false;
		}
		
		for (int i = 0; i < this.rowCount; i++) {
			for (int j = 0; j < this.columnCount; j++) {
				if (field.getPointState(i, j) != this.getPointState(i, j)) {
					return false;
				}
			}
		}	
		
		return true;		
	}
}
