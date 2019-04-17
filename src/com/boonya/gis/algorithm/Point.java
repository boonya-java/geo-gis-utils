package com.boonya.gis.algorithm;

import java.util.List;

public class Point {

	private double x;
	
	private double y;
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public GeoPoint convertToGeoPoint(Point point){
		return new GeoPoint(point.getY(), point.getX());
	}
	
	public List<GeoPoint> convertToGeoPoints(List<Point> points){
		return GeoUtil.convertToGeoPoints(points);
	}
}
