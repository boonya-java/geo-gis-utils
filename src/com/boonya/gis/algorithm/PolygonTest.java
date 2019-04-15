package com.boonya.gis.algorithm;
import java.util.ArrayList;
import java.util.List;

public class PolygonTest {
	public static void main(String[] args){
		check();
	}
	
	private static void check(){
		Point p = new Point(117.18137,39.13884);
		List<Point> points = new ArrayList<Point>();
		//117.18137,39.13884,117.18568,39.13874,117.18560,39.13709,117.18139,39.13724
		points.add(new Point(117.18137,39.13884));
		points.add(new Point(117.18568,39.13874));
		points.add(new Point(117.18560,39.13709));
		points.add(new Point(117.18139,39.13724));
		//points.add(new AyPoint(117.1171,39.392));
		boolean b = PolygonUtil.isInPolygon(p, points);
		String isIn = b?"内":"外";
		System.out.print("点在围栏  " + isIn);
	}
}
