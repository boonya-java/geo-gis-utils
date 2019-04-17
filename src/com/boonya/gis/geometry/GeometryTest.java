package com.boonya.gis.geometry;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;
/**
 * 
 * @author PJL
 *
 * @note     功能描述:轨迹在围栏内外的里程
 * @package  test
 * @filename GeometryTest.java
 * @date     2019年4月17日 下午3:16:33
 */
public class GeometryTest {
	
	/**
	 * 轨迹点个数
	 */
	private static int count = 10000;
	
	/**
	 * 围栏边数
	 */
	private static int polygonNum=3;
	
	/**
	 * 经度
	 */
	private static double lng = 116.252189;
	
	/**
	 * 纬度
	 */
	private static double lat = 39.9065632;
	
	/**
	 * 线
	 */
	private static LineString lineString;
	
	/**
	 * 面
	 */
	private static Polygon polygon;
	
	/**
	 * 结果集
	 */
	private static List<Long> resultList=new ArrayList<Long>();

	/**
	 * 生成轨迹线条
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @param count
	 *            点的个数
	 * @return
	 * @throws Exception
	 */
	public static LineString getLineString(double lng, double lat, int count)
			throws Exception {
		if (count < 2) {
			throw new Exception("至少需要2个点才能生成一条轨迹");
		}
		List<Coordinate> points = new ArrayList<Coordinate>();
		for (int i = 0; i < count; i++) {
			lng = lng + (Math.random() * 1000) / 1000000;
			lat = lat + (Math.random() * 1000) / 1000000;
			Coordinate point = GeometryUtil.createPoint(lng, lat);
			points.add(point);
		}
		LineString lineString = GeometryUtil.createLine(points);
		System.out.println(lineString);
		return lineString;
	}

	/**
	 * 生成几何面(不规则)
	 * 
	 * @param lng
	 *            经度
	 * @param lat
	 *            纬度
	 * @param count
	 *            点的个数
	 * @return
	 * @throws Exception
	 */
	public static Polygon getPolygon(double lng, double lat, int count)
			throws Exception {
		if (count < 3) {
			throw new Exception("至少需要3个点才能生成一个几何面");
		}
		List<Coordinate> points = new ArrayList<Coordinate>();
		for (int i = 0; i < count; i++) {
			lng = lng + (Math.random() * 1000) / 100000;
			lat = lat + (Math.random() * 1000) / 100000;
			Coordinate point = GeometryUtil.createPoint(lng, lat);
			points.add(point);
		}
		points.add(points.get(0));
		Polygon polygon = GeometryUtil.createPolygon(points);
		System.out.println(polygon);
		return polygon;
	}
	
	/**
	 * 单次运行
	 * 
	 * @throws Exception
	 */
	public static void runGeometry(boolean useNewData) throws Exception{
		if(useNewData){
			lineString = GeometryTest.getLineString(lng, lat, count);
			polygon = GeometryTest.getPolygon(lng, lat, polygonNum);
		}
		System.out.println("========runGeometry========>开始....");
		long start = new Date().getTime();
		Geometry multiLineString = GeometryUtil.intersectionGeo(lineString,polygon);
		Map<String, Double> map=GeometryUtil.getDistances(lineString, polygon);
		
		System.out.println("线的长度:" + lineString.getLength());
		System.out.println("线轨迹的实际里程(米):" + map.get("SUM"));
		System.out.println("面的面积(平方米):" + GeometryUtil.getAreaLonLat(polygon));
		System.out.println("两个空间对象最近的距离:" + GeometryUtil.distanceGeo(lineString, polygon));
		System.out.println("两个几何对象的交集:" + multiLineString);
		System.out.println("围栏内里程(米):" + map.get("INN"));
		System.out.println("围栏外里程(米):" + map.get("OUT"));
		long end = new Date().getTime();

		long cost=end-start;
		resultList.add(cost);

		System.out.println("========runGeometry========>耗时" + cost+ "ms,points=" + count);
	}
	
	/**
	 * 单次运行
	 * 
	 * @throws Exception
	 */
	public static void runCommon(boolean useNewData) throws Exception{
		long start = new Date().getTime();
		if(useNewData){
			lineString = GeometryTest.getLineString(lng, lat, count);
			polygon = GeometryTest.getPolygon(lng, lat, polygonNum);
		}
		System.out.println("========runCommon========>开始....");
		Map<String, Double> map=GeometryUtil.getDistancesByTraditional(lineString, polygon);
		System.out.println("线轨迹的实际里程(米):" + map.get("SUM"));
		System.out.println("围栏内里程(米):" + map.get("INN"));
		System.out.println("围栏外里程(米):" + map.get("OUT"));
		long end = new Date().getTime();
		
		long cost=end-start;
		resultList.add(cost);

		System.out.println("========runCommon========>耗时" + cost+ "ms,points=" + count);
	}
	
	/**
	 * 多次运行验证是否存在数据造成的异常问题
	 * 
	 * @param times
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void runGeometryLoop(int times) throws Exception{
		if(times<1)return ;
		for (int i = 0; i < times; i++) {
			try {
				runGeometry(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("耗时统计结果:");
		long sum=0;
		for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
			Long long1 = (Long) iterator.next();
			System.out.print(long1+",");
			sum+=long1;
		}
		System.out.println("");
		System.out.println("平均耗时:"+sum/resultList.size());
	}
	
	/**
	 * 多次运行验证是否存在数据造成的异常问题
	 * 
	 * @param times
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void runCommonLoop(int times) throws Exception{
		if(times<1)return ;
		for (int i = 0; i < times; i++) {
			try {
				runCommon(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("耗时统计结果:");
		long sum=0;
		for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
			Long long1 = (Long) iterator.next();
			System.out.print(long1+",");
			sum+=long1;
		}
		System.out.println("");
		System.out.println("平均耗时:"+sum/resultList.size());
	}
	
	/**
	 * 线是否包含其本身
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static boolean lineStringContainsLineString() throws Exception{
		boolean result=GeometryUtil.isContainSelf(lineString);
		System.out.println("lineStringContainsLineString="+result);
		return result;
	}
	
	/**
	 * 面是否包含其本身（交叉情况返回false）
	 * @return
	 * @throws Exception
	 */
    public static boolean polygonContainsPolygon() throws Exception{
		boolean result=GeometryUtil.isContainSelf(polygon);
		System.out.println("polygonContainsPolygon="+result);
		return result;
	}

	public static void main(String[] args) throws Exception {
		//GeometryTest.runGeometry();
		GeometryTest.runGeometryLoop(10);
		//GeometryTest.runCommonLoop(10);
		GeometryTest.lineStringContainsLineString();
		GeometryTest.polygonContainsPolygon();
	}

}
