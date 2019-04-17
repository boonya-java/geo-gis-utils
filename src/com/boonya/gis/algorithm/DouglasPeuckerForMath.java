package com.boonya.gis.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author PJL
 *
 * @note     功能描述:TODO 数学曲线抽稀算法
 * @description  Douglas-Peucker算法(道格拉斯-普克算法）是将曲线近似表示为一系列点，并减少点的数量的一种算法。 
 *  <li> 它的优点是具有平移和旋转不变性，给定曲线与阈值后，抽样结果一定。</li> 
 *  <li> Douglas—Peucker算法通常用于线状矢量数据压缩、轨迹数据压缩等。</li>
 * @package  com.boonya.gis.algorithm
 * @filename DouglasPeuckerForMath.java
 * @date     2019年4月17日 上午9:48:34
 * @see https://www.jianshu.com/p/bf595477a124
 */
public class DouglasPeuckerForMath {

	public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        List<Point> result = new ArrayList<>();

        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 4));
        points.add(new Point(4, 1));
        points.add(new Point(5, 0));
        points.add(new Point(6, 3));
        points.add(new Point(7, 5));
        points.add(new Point(8, 2));
        points.add(new Point(9, 1));
        points.add(new Point(10, 6));
        
        System.out.print("原始坐标：");
	    for(Point p :points){
	    	 System.out.print("(" + p.getX() + "," + p.getY() + ") ");
	    }
	    System.out.println("");
        System.out.println("=====================================================================");
        System.out.print("抽稀坐标：");

        result = douglasPeucker(points, 1);

        for (Point p : result) {
            System.out.print("(" + p.getX() + "," + p.getY() + ") ");
        }
    }

    public static List<Point> douglasPeucker(List<Point> points, int epsilon) {
        // 找到最大阈值点，即操作（1）
        double maxH = 0;
        int index = 0;
        int end = points.size();
        for (int i = 1; i < end - 1; i++) {
            double h = H(points.get(i), points.get(0), points.get(end - 1));
            if (h > maxH) {
                maxH = h;
                index = i;
            }
        }

        // 如果存在最大阈值点，就进行递归遍历出所有最大阈值点
        List<Point> result = new ArrayList<>();
        if (maxH > epsilon) {
            List<Point> leftPoints = new ArrayList<>();// 左曲线
            List<Point> rightPoints = new ArrayList<>();// 右曲线
            // 分别提取出左曲线和右曲线的坐标点
            for (int i = 0; i < end; i++) {
                if (i <= index) {
                    leftPoints.add(points.get(i));
                    if (i == index)
                        rightPoints.add(points.get(i));
                } else {
                    rightPoints.add(points.get(i));
                }
            }

            // 分别保存两边遍历的结果
            List<Point> leftResult = new ArrayList<>();
            List<Point> rightResult = new ArrayList<>();
            leftResult = douglasPeucker(leftPoints, epsilon);
            rightResult = douglasPeucker(rightPoints, epsilon);

            // 将两边的结果整合
            rightResult.remove(0);//移除重复点
            leftResult.addAll(rightResult);
            result = leftResult;
        } else {// 如果不存在最大阈值点则返回当前遍历的子曲线的起始点
            result.add(points.get(0));
            result.add(points.get(end - 1));
        }
        return result;
    }

    /**
     * 计算点到直线的距离
     * 
     * @param p
     * @param s
     * @param e
     * @return
     */
    public static double H(Point p, Point s, Point e) {
        double AB = distance(s, e);
        double CB = distance(p, s);
        double CA = distance(p, e);

        double S = helen(CB, CA, AB);
        double H = 2 * S / AB;

        return H;
    }

    /**
     * 计算两点之间的距离
     * 
     * @param p1
     * @param p2
     * @return
     */
    public static double distance(Point p1, Point p2) {
        double x1 = p1.getX();
        double y1 = p1.getY();

        double x2 = p2.getX();
        double y2 = p2.getY();

        double xy = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return xy;
    }

    /**
     * 海伦公式，已知三边求三角形面积
     * 
     * @param cB
     * @param cA
     * @param aB
     * @return 面积
     */
    public static double helen(double CB, double CA, double AB) {
        double p = (CB + CA + AB) / 2;
        double S = Math.sqrt(p * (p - CB) * (p - CA) * (p - AB));
        return S;
    }
}
