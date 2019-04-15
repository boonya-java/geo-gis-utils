package com.boonya.gis.algorithm;
/**
 * 
 * @author PJL
 *
 * @note     功能描述:GEO单位
 * @package  com.boonya.gis.algorithm
 * @filename GeoUnit.java
 * @date     2019年4月15日 上午10:37:49
 */
public enum GeoUnit {
	KM(1000),
	M(10);
	
	private int distance=10;
	
	private GeoUnit(int distace){
		this.distance=distace;
	}
	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
}
