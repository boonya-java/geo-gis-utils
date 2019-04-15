package com.boonya.gis.algorithm;

import java.util.List;
/**
 * 
 * @author PJL
 *
 * @note     功能描述:GEO工具
 * @package  com.boonya.gis.algorithm
 * @filename GeoUtil.java
 * @date     2019年4月15日 上午10:39:16
 */
public class GeoUtil {
	/**
	 * 计算是否在圆形区域（单位/千米）
	 * 
	 * @param radius
	 *            半径
	 * @param lat1
	 *            纬度
	 * @param lng1
	 *            经度
	 * @return
	 * @return double
	 * @throws
	 */
	public static boolean isInCircle(double radius, double lat1, double lng1,double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		if (s > radius) {// 不在圆上
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 是否在矩形区域内
	 * 
	 * @param lat
	 *            测试点经度
	 * @param lng
	 *            测试点纬度
	 * @param minLat
	 *            纬度范围限制1
	 * @param maxLat
	 *            纬度范围限制2
	 * @param minLng
	 *            经度限制范围1
	 * @param maxLng
	 *            经度范围限制2
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInRectangleArea(double lat, double lng,	double minLat, double maxLat, double minLng, double maxLng) {
		if (isInRange(lat, minLat, maxLat)) {// 如果在纬度的范围内
			if (minLng * maxLng > 0) {
				if (isInRange(lng, minLng, maxLng)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (Math.abs(minLng) + Math.abs(maxLng) < 180) {
					if (isInRange(lng, minLng, maxLng)) {
						return true;
					} else {
						return false;
					}
				} else {
					double left = Math.max(minLng, maxLng);
					double right = Math.min(minLng, maxLng);
					if (isInRange(lng, left, 180)
							|| isInRange(lng, right, -180)) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
	}

	/**
	 * 判断是否在经纬度范围内
	 * 
	 * @param point
	 * @param left
	 * @param right
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInRange(double point, double left, double right) {
		if (point >= Math.min(left, right) && point <= Math.max(left, right)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断点是否在多边形内
	 * 
	 * @param point
	 *            测试点
	 * @param pts
	 *            多边形的点
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isInPolygon(GeoPoint point, List<GeoPoint> points) {
		int N = points.size();
		boolean boundOrVertex = true;
		int intersectCount = 0;// 交叉点数量
		double precision = 2e-10; // 浮点类型计算时候与0比较时候的容差
		GeoPoint p1, p2;// 临近顶点
		GeoPoint p = point; // 当前点

		p1 = points.get(0);
		for (int i = 1; i <= N; ++i) {
			if (p.equals(p1)) {
				return boundOrVertex;
			}

			p2 = points.get(i % N);
			if (p.getLatitude() < Math.min(p1.getLatitude(), p2.getLatitude())
					|| p.getLatitude() > Math.max(p1.getLatitude(),
							p2.getLatitude())) {
				p1 = p2;
				continue;
			}

			// 射线穿过算法
			if (p.getLatitude() > Math.min(p1.getLatitude(), p2.getLatitude())
					&& p.getLatitude() < Math.max(p1.getLatitude(),
							p2.getLatitude())) {
				if (p.getLongitude() <= Math.max(p1.getLongitude(),
						p2.getLongitude())) {
					if (p1.getLatitude() == p2.getLatitude()
							&& p.getLongitude() >= Math.min(p1.getLongitude(),
									p2.getLongitude())) {
						return boundOrVertex;
					}

					if (p1.getLongitude() == p2.getLongitude()) {
						if (p1.getLongitude() == p.getLongitude()) {
							return boundOrVertex;
						} else {
							++intersectCount;
						}
					} else {
						double xinters = (p.getLatitude() - p1.getLatitude())
								* (p2.getLongitude() - p1.getLongitude())
								/ (p2.getLatitude() - p1.getLatitude())
								+ p1.getLongitude();
						if (Math.abs(p.getLongitude() - xinters) < precision) {
							return boundOrVertex;
						}

						if (p.getLongitude() < xinters) {
							++intersectCount;
						}
					}
				}
			} else {
				if (p.getLatitude() == p2.getLatitude()
						&& p.getLongitude() <= p2.getLongitude()) {
					GeoPoint p3 = points.get((i + 1) % N);
					if (p.getLatitude() >= Math.min(p1.getLatitude(),
							p3.getLatitude())
							&& p.getLatitude() <= Math.max(p1.getLatitude(),
									p3.getLatitude())) {
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2;
		}
		if (intersectCount % 2 == 0) {// 偶数在多边形外
			return false;
		} else { // 奇数在多边形内
			return true;
		}
	}

	/**
	 * 判断一个点是否在一个多边形内部
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @param points
	 * @return boolean 是否在多边形内部
	 */
	public boolean isInPolygon(double lng, double lat,List<GeoPoint> points) {
		if (points == null || points.size() < 3)
			return false;
		GeoPoint point1 = new GeoPoint();
		GeoPoint point2 = new GeoPoint();
		int sum = 0;
		for (int i = 0, j = points.size(); i < j; i++) {
			point1.setLatitude(points.get(i).getLatitude());
			point1.setLongitude(points.get(i).getLatitude());
			if (i == j - 1) {
				point2.setLatitude(points.get(i).getLatitude());
				point2.setLongitude(points.get(i).getLatitude());
			} else {
				point2.setLatitude(points.get(i + 1).getLatitude());
				point2.setLongitude(points.get(i + 1).getLatitude());
			}

			if ((lat >= point1.getLatitude() && lat < point2.getLatitude())
					|| ((lat >= point2.getLatitude() && lat < point1
							.getLatitude()))) {
				if (Math.abs(point1.getLatitude() - point2.getLatitude()) > 0) {
					double lonTmp = point1.getLongitude()
							- ((point1.getLongitude() - point2.getLongitude()) * (point1
									.getLatitude() - lat))
							/ (point1.getLatitude() - point2.getLatitude());
					if (lonTmp < lng)
						sum += 1;
				}

			}

		}
		if (sum % 2 != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据经纬度和距离返回一个矩形范围
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @param distance
	 *            距离(单位为米)
	 * @return [lng1,lat1, lng2,lat2] 矩形的左下角(lng1,lat1)和右上角(lng2,lat2)
	 */
	public static double[] getRectangle(double lng, double lat, long distance) {
		float delta = 111000;
		if (lng != 0 && lat != 0) {
			double lng1 = lng - distance
					/ Math.abs(Math.cos(Math.toRadians(lat)) * delta);
			double lng2 = lng + distance
					/ Math.abs(Math.cos(Math.toRadians(lat)) * delta);
			double lat1 = lat - (distance / delta);
			double lat2 = lat + (distance / delta);
			return new double[] { lng1, lat1, lng2, lat2 };
		} else {
			// ZHCH 等于0时的计算公式
			double lng1 = lng - distance / delta;
			double lng2 = lng + distance / delta;
			double lat1 = lat - (distance / delta);
			double lat2 = lat + (distance / delta);
			return new double[] { lng1, lat1, lng2, lat2 };
		}
	}
	
	/**
	 *  得到两点间的距离 
	 *  
	 * @param point1
	 * @param point2
	 * @param unit
	 * @return
	 */
	public static double getDistance(GeoPoint point1,GeoPoint point2,GeoUnit unit) {
		double radLat1 = rad(point1.getLatitude());
		double radLat2 = rad(point2.getLatitude());
		double a = radLat1 - radLat2;
		double b = rad(point1.getLongitude()) - rad(point2.getLongitude());
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / unit.getDistance();
		return s;
	}
	
	/**
	 * 得到两点间的距离 米
	 * 
	 * @param lat1
	 *            第一点纬度
	 * @param lng1
	 *            第一点经度
	 * @param lat2
	 *            第二点纬度
	 * @param lng2
	 *            第二点经度
	 * @return
	 */
	public static double getDistance(GeoPoint point1,GeoPoint point2) {
		double radLat1 = rad(point1.getLatitude());
		double radLat2 = rad(point2.getLatitude());
		double a = radLat1 - radLat2;
		double b = rad(point1.getLongitude()) - rad(point2.getLongitude());
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10;
		return s;
	}

	/**
	 * 得到两点间的距离
	 * 
	 * @param lat1
	 *            第一点纬度
	 * @param lng1
	 *            第一点经度
	 * @param lat2
	 *            第二点纬度
	 * @param lng2
	 *            第二点经度
	 * @return
	 */
	public static double getDistance(double lat1, double lng1,	double lat2, double lng2,GeoUnit unit) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / unit.getDistance();
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 地球半径：6378.137KM
	 */
	private static double EARTH_RADIUS = 6378.137;

}
