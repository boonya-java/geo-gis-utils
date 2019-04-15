package com.boonya.gis.algorithm;
import java.util.List;

/**
 * @author PJL
 *
 * @note     功能描述:TODO围栏监测算法_围栏点进、出判断<射线法计算>
 * @package  com.boonya.gis.regon
 * @filename AnalysisPointAndRegion.java
 * @date     2019年4月15日 上午11:08:06
 */
public class PolygonUtil {
	
	private static double _dis =  0.0000000001;
	
	/**
	 * 是否进入围栏区域
	 * 
	 * @param point  当前位置
	 * @param points 围栏点集合
	 * @return
	 */
	public static Boolean isInPolygon(Point point,List<Point> points){
		//检测点为空或围栏为空，返回false
		if(point == null || points == null) return false;
		//检测围栏数组
		int len = points.size();
		if(len <3) return false;
		//交点数
		int meetPointCount = 0;
		//围栏判断
		for(int k = 1 ; k < len; k++){
			Point p1 = points.get(k);
			Point p2 = points.get(k-1);
			//初步判断 大范围过滤
			if(    (point.getX() > p1.getX() || point.getX() < p2.getX() || (point.getX() > p1.getX() || point.getX() < p2.getX()))//x坐标在顶点线段范围外
					|| (point.getY() < p1.getY() && point.getY() < p2.getY()))
					continue;
			/*
			 * 检测点在线段范围内，进行交点计算
			 */
			//点在线上，认为在围栏内，则可返回true了
			if(judgePointOnLine(p1,p2,point))
				return true;
			//处理特殊情况，交点是端点的情况 
			double temp;
			//temp相当于被除数(斜率的分母)
			temp = p1.getX() - p2.getX();
			if (temp >= -_dis && temp <= _dis){
				//处理交点情况 
                double dx = point.getX() - p1.getX();                                
                if(dx < -_dis || dx > _dis) continue;
                
                int[] indexs = new int[2];
                indexs[0] = 0;
                indexs[1] = 0;
                
                indexs = getNotSame(points,k);
                
                Point lineP1 = points.get(indexs[0]);
                Point lineP2 = points.get(indexs[1]);
                
                if( k> indexs[0])
                	break;
                else
                	k = indexs[0] + 1;
                
                if( (point.getY() > p1.getY())
                	&& ( (point.getX() >= lineP1.getX() && point.getX() <= lineP2.getX())
                		 || (point.getX() >= lineP2.getX() && point.getX() <= lineP2.getX())
                	   )
                   )
                	meetPointCount++;
            }else{
            	double kk;
            	double bb;
            	double meetPtX;
            	double meetPtY;
            	
            	kk = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
            	bb = p1.getY() - kk * p1.getX();
            	
            	meetPtY = kk * point.getX() + bb;
            	meetPtX = point.getX();
            	
            	//处理特殊情况，交点是端点的情况             	
                double dx = meetPtX - p1.getX();
                double dy = meetPtY - p1.getY();
                double dx2 = meetPtX - p2.getX();
                double dy2 = meetPtY - p2.getX();
                
                if(dx >= -_dis && dx <= _dis && dy >= -_dis && dy <= _dis){
                	Point p3;
                	if(k == 1){
                		p3 = points.get(len - 2);
                	}else{
                		p3 = points.get(k - 2);
                	}
                	//提取交点的上下两点分别在垂线的两侧
                	if( (point.getY() > meetPtY)
                		&& ((meetPtX >= p3.getY() && meetPtX <= p2.getX())
                			|| (meetPtX >= p2.getX() && meetPtX <= p3.getX())
                		   )
                	   ){
                		meetPointCount++;
                	}
                }else if(!(dx2 >= -_dis && dx2 <= _dis && dy2 >= -_dis && dy2 <= _dis)){
                	if(point.getY() > meetPtY)
                		meetPointCount++;
                }
            }
		}
		if(meetPointCount % 2 == 1){
			return true;
		}
		return false;
	}
	
	/*
	 * 检测点是否在线上
	 * point1和point2是顶点
	 * point是待检测点
	 */
	private static Boolean judgePointOnLine(Point point1,Point point2,Point point){
		double dx1 = getDistance(point1, point2);
        double dx2 = getDistance(point, point1);
        double dx3 = getDistance(point, point2);
        double dx = dx3 + dx2 - dx1;

        if (dx >= -0.0000000001 && dx <= 0.0000000001)
        {
            return true;
        }        
		return false;
	}
	
	/*
	 * 求取两点之间的距离 
	 */
	private static double getDistance(Point point1,Point point2){
		double x = point1.getX() - point2.getX();
        if (x <= 0)
        {
            x = -x;
        }
        double y = point1.getY() - point2.getY();
        if (y <= 0)
        {
            y = -y;
        }

        return Math.sqrt(x * x + y * y);
	}
	
	/*
	 * 在链表中获取x轴不相同的点 
	 */
	private static int[] getNotSame(List<Point> pts,int index){
		int[] indexs = new int[2];
		indexs[0] = -1;
		indexs[1] = -1;
		
		int size = pts.size();
		
		Point buftpt;
		Point tpt;
		
		tpt = pts.get(index);
		
		for(int i = index ; i < size ; i++){
			buftpt = pts.get(i);
			if(buftpt.getX() == tpt.getX()) continue;
			indexs[0] = i;
			break;
		}
		
		if(indexs[0] == -1){
			for(int i = 0 ; i < size ; i++){
				buftpt = pts.get(i);
				if(buftpt.getX() == tpt.getX()) continue;
				indexs[0] = i;
				break;
			}
		}
		
		for(int i = index ; i >= 0 ; i--){
			buftpt = pts.get(i);
			if(buftpt.getX() == tpt.getX()) continue;
			indexs[1] = i;
			break;
		}
		
		if(indexs[1] == -1){
			for(int i = size - 1  ; i >= 0 ; i--){
				buftpt = pts.get(i);
				if(buftpt.getX() == tpt.getX()) continue;
				indexs[0] = i;
				break;
			}
		}
		
		return indexs;
	}
}
