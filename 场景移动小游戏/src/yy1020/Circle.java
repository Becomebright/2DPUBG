package yy1020;

import java.util.Random;

import static java.lang.Thread.sleep;

import java.security.UnresolvedPermission;

//import static yy1020.ReadMapFile.map1;

public class Circle implements Runnable{
	private final int N = 3500, N1 = 1700, N2 = 1200, N3 = 800, N4 = 200, N5 = 50,
			RADIUS[] = new int[]{2200, N1, N2, N3, N4, N5};
	private final int CNT = 6;
	private final double MINUTE = 60000.0;
	private final int MIN = 60000;
	public int cnt = 0, Time = 0;
	private Poi point[];
	/*
	 * 6500 * 2 = 13000
	 * 4400 * 2 = 8800
	 * 2200 * 2 = 4400
	 * 300 * 2 = 600
	 */

	public void setPoint(Poi[] point) {
		this.point = point;
	}
	
	public void solve() {
//		ReadMapFile.map1[0][0] = 4;
//		ReadMapFile.map1[0][0] = 4;
		cnt = Time / MIN;
		if(cnt>=5) {
			return;
		}
		int Left = Time % MIN;
		//System.out.printf("Time %d Left : %d\n", Time, Left);
		//if(Left <= HALFMIN) return;
		double v = ((double)(RADIUS[cnt] - RADIUS[cnt + 1])) / MINUTE;
		double r = (double)RADIUS[cnt] - v * ((double)Left);
//		double r = RADIUS[cnt + 1] + v * ((double)Left - HALF);
		//if(Time % 1000 == 0) System.out.printf("Radius %f   speed %f \n", r, v);
		for(int i = 0; i < 70; ++i) {
			for(int j = 0; j < 70; ++j){
				Poi mPoint = new Poi(i * 50 + 25, j * 50 + 25);
				double dist = mPoint.dis(point[cnt + 1]);
				if(ReadMapFile.map1[i][j] != 3 && dist >= r) ReadMapFile.map1[i][j] = 3;
//				else ReadMapFile.map1[i][j] = 3;
				//System.out.printf("%d ", ReadMapFile.map1[i][j]);
			}
			//System.out.println();
		}
//		try {
//			sleep(20000);
//		} catch (Exception e) {
//			//TESTING@@@@@@@@@@@@@@@@@@@@@@@
//		}
	}

	@Override
	public void run() {
		while(true){
			try {
				sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Time += 20;
			//System.out.printf("time %d\n", Time);
			solve();
		}

	}
}