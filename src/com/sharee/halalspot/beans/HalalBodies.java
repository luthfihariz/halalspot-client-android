package com.sharee.halalspot.beans;

public class HalalBodies {

	private int id;
	private String name;
	private String shortName;
	private String overview;
	private String logoUrl;
	private String country;	
	private String address;
	private String phone;
	private String fax;
	private String email;
	private String website;
	private String pic;

	public static final String KEY_BODY_NAME = "bodyName";
	public static final String KEY_BODY_SHORTNAME = "bodyShortName";
	public static final String KEY_BODY_OVERVIEW = "bodyOverview";
	public static final String KEY_BODY_LOGOURL = "bodyLogoUrl";
	public static final String KEY_BODY_COUNTRY = "bodyCountry";
	public static final String KEY_BODY_ADDRESS = "bodyAddr";
	public static final String KEY_BODY_PHONE = "bodyPhone";
	public static final String KEY_BODY_EMAIL = "bodyEmail";
	public static final String KEY_BODY_WEBSITE = "bodyWeb";
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}
