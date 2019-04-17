# geo-gis-utils
geo gis utils.


# Exceptions

## found non-noded intersection between LINESTRING

```
com.vividsolutions.jts.geom.TopologyException: found non-noded intersection between LINESTRING ( 116.26451237493255 39.91605493967855, 116.271980875453 39.925889370081514 ) and LINESTRING ( 116.28210414799756 39.933904772275405, 116.25531773218691 39.90853459945099 ) [ (116.26772659308502, 39.92028738294261, NaN) ]
	at com.vividsolutions.jts.noding.FastNodingValidator.checkValid(FastNodingValidator.java:130)
	at com.vividsolutions.jts.geomgraph.EdgeNodingValidator.checkValid(EdgeNodingValidator.java:94)
	at com.vividsolutions.jts.geomgraph.EdgeNodingValidator.checkValid(EdgeNodingValidator.java:59)
	at com.vividsolutions.jts.operation.overlay.OverlayOp.computeOverlay(OverlayOp.java:170)
	at com.vividsolutions.jts.operation.overlay.OverlayOp.getResultGeometry(OverlayOp.java:127)
	at com.vividsolutions.jts.operation.overlay.OverlayOp.overlayOp(OverlayOp.java:66)
	at com.vividsolutions.jts.operation.overlay.snap.SnapIfNeededOverlayOp.getResultGeometry(SnapIfNeededOverlayOp.java:96)
	at com.vividsolutions.jts.operation.overlay.snap.SnapIfNeededOverlayOp.overlayOp(SnapIfNeededOverlayOp.java:58)
	at com.vividsolutions.jts.geom.Geometry.intersection(Geometry.java:1342)
	at com.forestar.geometry.utils.GeometryUtil.intersectionGeo(GeometryUtil.java:122)
	at test.GeometryTest.main(GeometryTest.java:84)

```
Solution：

```
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
	
```
Please do not use two Geometrys.
```
 /** 
     * 两个几何对象的交集 
     * @param a 
     * @param b 
     * @return 
     */  
    public static Geometry intersectionGeo(Geometry a,Geometry b){  
        return a.intersection(b);  
    }  
```
