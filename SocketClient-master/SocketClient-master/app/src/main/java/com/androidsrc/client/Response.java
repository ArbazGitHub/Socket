package com.androidsrc.client;

import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("geo_location")
	private GeoLocation geoLocation;

	@SerializedName("user_id")
	private int userId;

	public void setGeoLocation(GeoLocation geoLocation){
		this.geoLocation = geoLocation;
	}

	public GeoLocation getGeoLocation(){
		return geoLocation;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"geo_location = '" + geoLocation + '\'' + 
			",user_id = '" + userId + '\'' + 
			"}";
		}
}