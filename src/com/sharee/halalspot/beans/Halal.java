package com.sharee.halalspot.beans;

public class Halal {

	private int type;
	private String displayValue;
	private String description;
	private HalalBodies bodies;
	
	public static final int UNVERIFIED = 1;
	public static final int VERBAL_ASSURANCE = 2;
	public static final int LABELED_MENU = 3;
	public static final int MUSLIM_OWNERS = 4;
	public static final int HALAL_WINDOW_SIGN = 5;
	public static final int HALAL_CERTIFICATED = 6;
	public static final int GOVERNMENT = 7;
	public static final int ISLAMIC = 8;
	
	public static final String KEY_HALAL_DISPLAY = "halalDisplay";	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public HalalBodies getBodies() {
		return bodies;
	}

	public void setBodies(HalalBodies bodies) {
		this.bodies = bodies;
	}

}
