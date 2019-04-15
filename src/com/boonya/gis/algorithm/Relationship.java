package com.boonya.gis.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author PJL
 *
 * @note     功能描述:TODO点、线、面 之间的关系
 * @package  com.boonya.gis.algorithm
 * @filename Relationship.java
 * @date     2019年4月15日 下午4:42:25
 * @see      点线面拓扑关系 https://blog.csdn.net/u013240519/article/details/82191317
 */
public class Relationship {
	
	/**
	 * 点在线上
	 * @param point
	 * @param line
	 * @return
	 * @throws Exception 
	 */
	public  boolean isPointOnLine(Point point,Polyline line) throws Exception{
		double distance=minDistanceToLine(point, line);
		if((distance<0.0000000001&&distance>0)||distance==0){
			return true;
		}
		return false;
	}
	
	/**
	 * 点在面上
	 * @param point
	 * @param polygon
	 * @return
	 */
	public  boolean isPointInPolygon(Point point,Polygon polygon){
		return false;
	}
	
	/**
	 * 线包含于面
	 * @param point
	 * @param line
	 * @return
	 */
	public  boolean isLineInPolygon(Polyline line,Polygon polygon){
		return false;
	}
	
	/**
	 * 线与面相交
	 * @param point
	 * @param line
	 * @return
	 */
	public  boolean isLineCrossPolygon(Polyline line,Polygon polygon){
		return false;
	}
	
	/**
	 * 点到线的最小距离
	 * @return
	 * @throws Exception 
	 */
	public static double minDistanceToLine(Point point,Polyline line) throws Exception{
		double min=999999999999d,distance;
		if(line==null||line.points==null||line.points.size()<2){
			throw new Exception("Polyline is initial failed");
		}
		for (int i = 0,j=line.points.size(); i < j; i++) {
			distance=getDistance(point, line.points.get(i));
			if(distance<min){
				min=distance;
			}
		}
		return min;
	}
	
	/**
	 * 点到线的最大距离
	 * @return
	 * @throws Exception 
	 */
	public static double maxDistanceToLine(Point point,Polyline line) throws Exception{
		double max=0,distance;
		if(line==null||line.points==null||line.points.size()<2){
			throw new Exception("Polyline is initial failed");
		}
		for (int i = 0,j=line.points.size(); i < j; i++) {
			distance=getDistance(point, line.points.get(i));
			if(distance>max){
				max=distance;
			}
		}
		return max;
	}
	
	/**
	 * 得到两点间的距离 (米)
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static double getDistance(Point point1,Point point2) {
		double radLat1 = rad(point1.getX());
		double radLat2 = rad(point2.getX());
		double a = radLat1 - radLat2;
		double b = rad(point1.getY()) - rad(point2.getY());
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10;
		return s;
	}
	
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
	
	/**
	 * 地球半径：6378.137KM
	 */
	private static double EARTH_RADIUS = 6378.137;
	
	
	/**
	 * 线--多点
	 * @author PJL
	 *
	 * @company  北京地林伟业科技股份有限公司
	 * @note     功能描述:TODO线条轨迹
	 * @package  com.boonya.gis.algorithm
	 * @filename Relationship.java
	 * @date     2019年4月15日 下午4:45:28
	 */
	public class Polyline{
		private String uuid;
		private String name;
		/**
		 * 线是由点组成的
		 */
		private List<Point> points=new ArrayList<Point>();
		/**
		 * @return the uuid
		 */
		public String getUuid() {
			return uuid;
		}
		/**
		 * @param uuid the uuid to set
		 */
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the points
		 */
		public List<Point> getPoints() {
			return points;
		}
		/**
		 * @param points the points to set
		 */
		public void setPoints(List<Point> points) {
			this.points = points;
		}
		
	}
	
	/**
	 * 
	 * @author PJL
	 *
	 * @note     功能描述:TODO多边形-面
	 * @package  com.boonya.gis.algorithm
	 * @filename Relationship.java
	 * @date     2019年4月15日 下午4:45:15
	 */
    public class Polygon{
		private String uuid;
		private String name;
		/**
		 * 面是由线组成的
		 */
		private List<Polyline> lines=new ArrayList<Polyline>();
		/**
		 * @return the uuid
		 */
		public String getUuid() {
			return uuid;
		}
		/**
		 * @param uuid the uuid to set
		 */
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the lines
		 */
		public List<Polyline> getLines() {
			return lines;
		}
		/**
		 * @param lines the lines to set
		 */
		public void setLines(List<Polyline> lines) {
			this.lines = lines;
		}
		
		
	}

}
