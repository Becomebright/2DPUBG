package MySocket;

import java.awt.Point;
import java.util.Random;

import com.alibaba.fastjson.JSON;

import yy1020.Poi;

public class testPoi {
	static Poi[] point;
	private static void randCircle() {
		
    	final int N = 3500, N1 = 1700, N2 = 1200, N3 = 800, N4 = 200, N5 = 50,
    			RADIUS[] = new int[]{2200, N1, N2, N3, N4, N5};
    	final int CNT = 6;
    	
		point = new Poi[CNT];
		int Radius, Left, Right, Up, Down;
		Left = Up = 0;
		Right = Down = N;
		point[0] = new Poi(N / 2, N / 2);
		for(int i = 1; i < CNT; ++i) {
			Radius = RADIUS[i];
			Left = Left + Radius;
			Right = Right - Radius;
			
			Up += Radius;
			Down -= Radius;
			if(Left > Right || Up > Down) while(true) {
				System.out.printf("%d %d %d %d\n", Left, Right, Up, Down);
			}
			Random rd = new Random();
			point[i] = new Poi(rd.nextInt(Right - Left + 1) + Left, rd.nextInt(Down - Up + 1) + Up);
			Left = (int) (point[i].x - Radius);
			Right = (int) (point[i].x + Radius);
			Up = (int) (point[i].y - Radius);
			Down = (int) (point[i].y + Radius);
			//System.out.printf("%d %d %d %d\n\n", Left, Right, Up, Down);
		}
//		for(int i = 0; i < CNT; ++i) {
//			System.out.printf("%f %f %d\n", point[i].x, point[i].y, RADIUS[i]);
//			try {
//				sleep(2000);
//			} catch (Exception e) {
//				//TESTING@@@@@@@@@@@@@@@@@@@@@@@
//			}
//		}
	}
	public static void main(String[] args){
		point = new Poi[6];
		randCircle();
		String string = JSON.toJSONString(point);
		System.out.println(string);
		System.out.println(string.substring(9, 10).equals("3"));
	}
}
