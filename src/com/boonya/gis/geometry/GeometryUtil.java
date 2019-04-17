package com.boonya.gis.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

public class GeometryUtil {
	private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null ); 
	private static CRSFactory crsFactory = ReferencingFactoryFinder.getCRSFactory(null); 
	//北京2000经纬度坐标系
	private static String BEIJING_2000_WKT = "GEOGCS[\"GCS_China_Geodetic_Coordinate_System_2000\",DATUM[\"D_China_2000\",SPHEROID[\"CGCS2000\",6378137.0,298.257222101]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]";
    //墨卡托平面坐标系，用距离计算、面积计算
	private static String Mercator_XY_WKT = "PROJCS[\"WGS_1984_Web_Mercator\",GEOGCS[\"GCS_WGS_1984_Major_Auxiliary_Sphere\",DATUM[\"D_WGS_1984_Major_Auxiliary_Sphere\",SPHEROID[\"WGS_1984_Major_Auxiliary_Sphere\",6378137.0,0.0]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mercator\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],UNIT[\"Meter\",1.0]]";
	//坐标转换对象
	
	/** 
     * create a Point 
     * @param x 
     * @param y 
     * @return Coordinate
     */  
    public static Coordinate createPoint(double x,double y){ 
        return new Coordinate(x,y);  
    } 
    /**
	 * create a point
	 * @return Geometry
	 */
	public static Point createGeoPoint(double x,double y){
		Coordinate coord = new Coordinate(x, y);
		Point point = geometryFactory.createPoint( coord );
		return point;
	}
    /**
     * 通过读取WKT字符串创建空间对象 
     * @param geomWKT
     * @return
     * @throws ParseException
     */
    public static Geometry createPointByWKT(String geomWKT) throws ParseException{  
        WKTReader reader = new WKTReader( geometryFactory );  
        Geometry geom =reader.read(geomWKT);  
        return geom;  
    }  
      
  
    /** 
     * create a line 
     * @return 
     */  
    public static LineString createLine(List<Coordinate> points){  
        Coordinate[] coords  = (Coordinate[]) points.toArray(new Coordinate[points.size()]);  
        LineString line = geometryFactory.createLineString(coords);  
        return line;  
    }
    
    /**
     * 根据点串创建多边形
     * @param points
     * @return
     */
    public static Polygon createPolygon(List<Coordinate> points){
    	Coordinate[] coords  = (Coordinate[]) points.toArray(new Coordinate[points.size()]);  
    	Polygon polygon=geometryFactory.createPolygon(coords);
    	return polygon;
    }
    /**
     * 判断一个点是否在多边形上
     * @param geomWKT
     * @return true为在面上，false则不是
     * @throws ParseException
     */
    public static boolean isContains(String geomWKT,String pointWKT) throws ParseException{  
        WKTReader reader = new WKTReader( geometryFactory );  
        Geometry geom =reader.read(geomWKT);  
        Geometry point = reader.read(pointWKT);  
        return geom.contains(point);  
    }  
    /** 
     * 返回(A)与(B)中距离最近的两个点的距离 
     * @param a 
     * @param b 
     * @return 
     */  
    public static double distanceGeo(Geometry a,Geometry b){  
        return a.distance(b);  
    }  
      
    /** 
     * 两个几何对象的交集 
     * @param a 
     * @param b 
     * @return 
     */  
    public static Geometry intersectionGeo(Geometry a,Geometry b){  
        return a.intersection(b);  
    }  
      
    /** 
     * 几何对象合并 
     * @param a 
     * @param b 
     * @return 
     */  
    public static Geometry unionGeo(Geometry a,Geometry b){  
        return a.union(b);  
    }  
      
    /** 
     * 在A几何对象中有的，但是B几何对象中没有 
     * @param a 
     * @param b 
     * @return 
     */  
    public static Geometry differenceGeo(Geometry a,Geometry b){  
        return a.difference(b);  
    } 
    
    /**
     * 获取线的长度，默认坐标为：坐标值为北京2000坐标系
     * @param line
     * @return
     * @throws TransformException 
     * @throws FactoryException 
     * @throws MismatchedDimensionException 
     * @throws org.opengis.util.FactoryException 
     */
    public static double getLengthLonLat(LineString line) throws MismatchedDimensionException, FactoryException, TransformException, org.opengis.util.FactoryException{
    	Coordinate []points=line.getCoordinates();
    	List<Coordinate> pointsList=new ArrayList<Coordinate>();
    	for(int i=0;i<points.length;i++){
    		Coordinate point=lonLatToXY(points[i]);
    		pointsList.add(point);
    	}
    	LineString lineXY=createLine(pointsList);
    	return lineXY.getLength();
    }
    
    /**
     * 获取线的长度，默认坐标为平面坐标系
     * @param line
     * @return
     */
    public static double getLengthXY(LineString line){
    	return line.getLength();
    }
    
    /**
     * 获取多边形面积，输入坐标系为北京2000经纬度坐标系
     * @param polygon
     * @return
     * @throws MismatchedDimensionException
     * @throws FactoryException
     * @throws TransformException 
     * @throws org.opengis.util.FactoryException 
     */
    public static double getAreaLonLat(Polygon polygon) throws MismatchedDimensionException, FactoryException, TransformException, org.opengis.util.FactoryException{
    	Coordinate []points=polygon.getCoordinates();
    	List<Coordinate> pointsList=new ArrayList<Coordinate>();
    	for(int i=0;i<points.length;i++){
    		Coordinate point=lonLatToXY(points[i]);
    		pointsList.add(point);
    	}
    	Polygon lineXY=createPolygon(pointsList);
    	return lineXY.getArea();
    }
    
    /**
	 * create a Circle  创建一个圆，圆心(x,y) 半径RADIUS
	 * @param x
	 * @param y
	 * @param RADIUS 单位（米）
	 * @return
	 */
	public static Polygon createCircle(double x, double y, final double RADIUS){
		final int SIDES = 32;//圆上面的点个数
		//距离转度
		double degree = RADIUS / (2 * Math.PI * 6378137.0) * 360;
//		System.out.println("度："+degree);
	    Coordinate coords[] = new Coordinate[SIDES+1];
	    for( int i = 0; i < SIDES; i++){
	        double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
	        double dx = Math.cos( angle ) * degree;
	        double dy = Math.sin( angle ) * degree;
	        coords[i] = new Coordinate( (double) x + dx, (double) y + dy );
	    }
	    coords[SIDES] = coords[0];
	    LinearRing ring = geometryFactory.createLinearRing( coords );
	    Polygon polygon = geometryFactory.createPolygon( ring, null );
	    return polygon;
	}
    
    
    /**
     * 将经纬度的点坐标转换为平面坐标
     * @param point
     * @return
     * @throws FactoryException 
     * @throws TransformException 
     * @throws MismatchedDimensionException 
     * @throws org.opengis.util.FactoryException 
     */
    public static Coordinate lonLatToXY(Coordinate coordinate) throws FactoryException, MismatchedDimensionException, TransformException, org.opengis.util.FactoryException{
    	Hints hint = new Hints(Hints.LENIENT_DATUM_SHIFT, true);
    	CoordinateReferenceSystem sourceCRS = crsFactory.createFromWKT(BEIJING_2000_WKT);
        CoordinateReferenceSystem targetCRS = crsFactory.createFromWKT(Mercator_XY_WKT);
    	CoordinateOperationFactory coFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(hint);
        CoordinateOperation co = coFactory.createOperation(sourceCRS,targetCRS);
        MathTransform transform = co.getMathTransform();
        double x = coordinate.x;
    	double y = coordinate.y;
    	DirectPosition point = new GeneralDirectPosition(x, y);
    	point = transform.transform(point, point);
    	double lat = point.getOrdinate(0);
    	double lon = point.getOrdinate(1);
    	return createPoint(lat,lon);
    }
    
    /**
     * 获取空间信息点
     * 
     * @param geometry
     * @return
     */
    public static List<Coordinate> getCoordinates(Geometry geometry){
    	return Arrays.asList(geometry.getCoordinates());
    }
    
    /**
     * 判断几何图形是否包含其本身(false标识几何图形存在非节点交叉点)
     * 
     * @param geometry
     * @return
     */
    public static boolean isContainSelf(Geometry geometry){
    	return geometry.contains(geometry);
    }
    
    /**
     * 解析交集的线条集合
     * 
     * @param multiLineString
     * @return
     */
    public static List<LineString> parseMultiLineString(Geometry multiLineString){
    	int count=multiLineString.getNumGeometries();
    	List<LineString> list=new ArrayList<LineString>();
    	Geometry geometry_=null;
    	for (int i = 0; i < count; i++) {
    		geometry_=multiLineString.getGeometryN(i);
    		List<Coordinate> points=Arrays.asList(geometry_.getCoordinates());
    		LineString lineString=createLine(points);
    		list.add(lineString);
		}
    	return list;
    }
    
    /**
     * 计算线条与几何图像的交集内外里程长度-千米(效率低下-每次都要判定点是否在围栏内部)
     * @param lineString
     * @param polygon
     * @return
     * @throws ParseException 
     */
	public static Map<String,Double> getDistancesByTraditional(LineString lineString,Polygon polygon) throws ParseException {
		double sum=0.0,inner=0.0,distance=0.0;
		boolean isInner=false;
		List<Coordinate> linePoints=getCoordinates(lineString);
		Geometry currentPoint=null, lastGeoPoint=null;
		String geoWKT=polygon.toString();
		for (Coordinate coor : linePoints) {
			currentPoint=createGeoPoint(coor.x,coor.y);
			// 点是否在几何图形内部
			isInner=isContains(geoWKT, currentPoint.toString());
			// 总里程计算
			if(lastGeoPoint!=null){
				// 计算当前与上一个点之间的距离
				distance=lastGeoPoint.distance(currentPoint);
				sum+=distance;
			}
			// 内部里程计算
			if(isInner&&lastGeoPoint!=null){
				inner+=distance;
			}
			lastGeoPoint=currentPoint;
		}
		double outer = sum - inner;
		Map<String,Double> map=new HashMap<String, Double>();
		map.put("SUM", sum);
		map.put("INN", inner);
		map.put("OUT", outer);
		return map;
	}
    
    /**
     * 计算线条与几何图像的交集内外里程长度-米
     * @param lineString
     * @param polygon
     * @return
     * @throws ParseException 
     */
	public static Map<String,Double> getDistances(LineString lineString,Polygon polygon) throws ParseException {
		Geometry multiLineString = GeometryUtil.intersectionGeo(lineString,polygon);
		return getDistances((Geometry)lineString,multiLineString);
	}
    
    /**
     * 计算线条与几何图像的交集内外里程长度-米
     * @param lineString
     * @param multiLineString
     * @return
     * @throws ParseException 
     */
	public static Map<String,Double> getDistances(Geometry lineString,Geometry multiLineString) throws ParseException {
		List<LineString> lines = GeometryUtil.parseMultiLineString(multiLineString);
		double sum = GeometryUtil.getDistance(lineString);
		double inner = GeometryUtil.getDistance(lines);
		double outer = sum - inner;
		Map<String,Double> map=new HashMap<String, Double>();
		map.put("SUM", sum);
		map.put("INN", inner);
		map.put("OUT", outer);
		return map;
	}
    
    /**
     * 计算轨迹里程长度-米
     * @param point1
     * @param point2
     * @return
     * @throws ParseException 
     */
	public static double getDistance(List<LineString> lines) throws ParseException {
		double sum=0.0;
		for (int i = 0,j=lines.size(); i < j; i++) {
			sum+=getDistance(lines.get(i));
		}
		return sum;
	}
	
	 /**
     * 计算轨迹里程长度-米
     * @param point1
     * @param point2
     * @return
     * @throws ParseException 
     */
	public static double getDistance(Geometry lineString) throws ParseException {
		double sum=0.0;
		if(lineString==null)	return sum;
		Geometry geometry=lineString;
		Coordinate [] coordinates=geometry.getCoordinates();
		Coordinate current=null,last=null;
		for (Coordinate coordinate : coordinates) {
			current=coordinate;
			if(last!=null){
				sum+=GeometryUtil.getDistance(last, current);
			}
			last=current;
		}
		return sum;
	}
    
    /**
     * 计算轨迹里程长度-米
     * @param point1
     * @param point2
     * @return
     * @throws ParseException 
     */
	public static double getDistance(LineString line) throws ParseException {
		double sum=0.0;
		if(line==null)	return sum;
		Geometry geometry=GeometryUtil.createPointByWKT(line.toString());
		Coordinate [] coordinates=geometry.getCoordinates();
		Coordinate current=null,last=null;
		for (Coordinate coordinate : coordinates) {
			current=coordinate;
			if(last!=null){
				sum+=GeometryUtil.getDistance(last, current);
			}
			last=current;
		}
		return sum;
	}
	
	public static WKTReader getWKTReader(){
		 WKTReader reader = new WKTReader( geometryFactory );  
		 return reader;
	}
    
    /**
     * 计算两点间的距离 -米
     * @param point1
     * @param point2
     * @return
     */
	public static double getDistance(Coordinate point1,Coordinate point2) {
		double radLat1 = rad(point1.x);
		double radLat2 = rad(point2.x);
		double a = radLat1 - radLat2;
		double b = rad(point1.y) - rad(point2.y);
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
	 * @param args
	 * @throws FactoryException 
	 * @throws TransformException 
	 * @throws MismatchedDimensionException 
	 */
/*	public static void main(String[] args) throws FactoryException, MismatchedDimensionException, TransformException {
		// TODO Auto-generated method stub
		Coordinate point1=GeometryUtil.createPoint(116.252189, 39.9065632);
		Coordinate point2=GeometryUtil.createPoint(116.251977, 39.9068492);
		Coordinate point3=GeometryUtil.createPoint(116.252168, 39.9065392);
		Coordinate point4=GeometryUtil.createPoint(116.255095, 39.9066112);
		List<Coordinate> points=new ArrayList<Coordinate>();
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		points.add(point1);
		Polygon polygon=GeometryUtil.createPolygon(points);
		LineString line =GeometryUtil.createLine(points);
		System.out.println("line-changdu-lonlat:"+line.getLength());
		System.out.println("line-changdu-xy:"+GeometryUtil.getLengthLonLat(line));
		System.out.println("polygon-mianji-lonlat:"+polygon.getArea());
		System.out.println("line-changdu-xy:"+GeometryUtil.getAreaLonLat(polygon));
	}*/
    
    

}
