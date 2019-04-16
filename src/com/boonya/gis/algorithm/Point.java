package com.boonya.gis.algorithm;

import java.util.List;

public class Point {

	/*
	 * X坐标
	 */	 
	private double _x;
	public double getX(){
		return _x;
	}
//	public void setX(double x){
//		this._x = x;
//	}
	/*
	 * Y坐标
	 */
	private double _y;
	public double getY(){
		return _y;
	}
//	public void setY(double y){
//		this._y = y;
//	}
	
	/*
	 * 构造函数
	 */
	public Point(double x,double y){
		this._x = x;
		this._y = y;
	}
	
	public GeoPoint convertToGeoPoint(Point point){
		return new GeoPoint(point.getY(), point.getX());
	}
	
	public List<GeoPoint> convertToGeoPoints(List<Point> points){
		return GeoUtil.convertToGeoPoints(points);
	}
}
