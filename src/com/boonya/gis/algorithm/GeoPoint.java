package com.boonya.gis.algorithm;

import java.util.List;

public class GeoPoint {
	
	
	public GeoPoint() {
		super();
	}
	public GeoPoint(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	/***
	 * 经度
	 */
	private double longitude=-1;
	/**
	 * 纬度
	 */
	private double latitude=-1;
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public Point convertToPoint(GeoPoint point){
		return new Point(point.getLatitude(), point.getLongitude());
	}
	
	public List<Point> convertToPoints(List<GeoPoint> points){
		return GeoUtil.convertToPoints(points);
	}
	

}
