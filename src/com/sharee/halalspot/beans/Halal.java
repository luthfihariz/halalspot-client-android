package com.sharee.halalspot.beans;

public class Halal {

	private int type;
	private String displayValue;
	private String description;
	private HalalInstitution institution;

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

	public HalalInstitution getInstitution() {
		return institution;
	}

	public void setInstitution(HalalInstitution institution) {
		this.institution = institution;
	}

}
