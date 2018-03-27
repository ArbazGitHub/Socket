package com.androidsrc.client;

import com.google.gson.annotations.SerializedName;

public class GeoLocation{

	@SerializedName("geo_lat")
	private String geoLat;

	@SerializedName("geo_long")
	private String geoLong;

	@SerializedName("geo_heading")
	private String geoHeading;

	@SerializedName("device_speed")
	private String deviceSpeed;

	@SerializedName("horizontalAccuracy")
	private String horizontalAccuracy;

	@SerializedName("geo_alt")
	private String geoAlt;

	public void setGeoHeading(String geoHeading){
		this.geoHeading = geoHeading;
	}

	public String getGeoHeading(){
		return geoHeading;
	}

	public void setGeoLat(String geoLat){
		this.geoLat = geoLat;
	}

	public String getGeoLat(){
		return geoLat;
	}

	public void setDeviceSpeed(String deviceSpeed){
		this.deviceSpeed = deviceSpeed;
	}

	public String getDeviceSpeed(){
		return deviceSpeed;
	}

	public void setGeoLon(String geoLon){
		this.geoLong = geoLon;
	}

	public String getGeoLon(){
		return geoLong;
	}

	public void setHorizontalAccuracy(String horizontalAccuracy){
		this.horizontalAccuracy = horizontalAccuracy;
	}

	public String getHorizontalAccuracy(){
		return horizontalAccuracy;
	}

	public void setGeoAlt(String geoAlt){
		this.geoAlt = geoAlt;
	}

	public String getGeoAlt(){
		return geoAlt;
	}

	@Override
 	public String toString(){
		return 
			"GeoLocation{" + 
			"geo_heading = '" + geoHeading + '\'' + 
			",geo_lat = '" + geoLat + '\'' + 
			",device_speed = '" + deviceSpeed + '\'' + 
			",geo_lon = '" + geoLong + '\'' +
			",horizontalAccuracy = '" + horizontalAccuracy + '\'' + 
			",geo_alt = '" + geoAlt + '\'' + 
			"}";
		}
}