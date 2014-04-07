package com.sharee.halalspot.beans;

public class Photo {
	private String url;
	private String source;

	public Photo(){
		
	}
	
	public Photo(String url){
		this.url = url;
	}
	
	public Photo(String url, String source){
		this.url = url;
		this.source = source;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
