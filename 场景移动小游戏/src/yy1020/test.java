package yy1020;

import MySocket.Client;

import static java.lang.Thread.sleep;

import java.io.IOException;

/**
 * 开始游戏
 * @author yy
 *
 */
public class test {
	static mainFrame mf;
	/*public static void main(String[] args) {
		//首先从地图文件中读入地图数组
		ReadMapFile.readfile("map1.map");
		//用读到的地图数组创建游戏窗体，开始游戏
		mf = new mainFrame();
		Circle circle = new Circle();
		circle.randCircle();
//		try {
//			sleep(200000);
//		} catch (Exception e) {
//			//TESTING@@@@@@@@@@@@@@@@@@@@@@@
//		}
		Thread thcir = new Thread(circle);
		thcir.start();
		Background bg = new Background();
		Thread thbg = new  Thread(bg);
		thbg.start();

        try {
            Client client = new Client();
            client.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }*/
	public static void start() {
		//首先从地图文件中读入地图数组
				ReadMapFile.readfile("map1.map");
				//用读到的地图数组创建游戏窗体，开始游戏
				mf = new mainFrame();
				
				Background bg = new Background();
				Thread thbg = new  Thread(bg);
				thbg.start();

		        try {
		            Client client = new Client();
		            client.load();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
	}

}
