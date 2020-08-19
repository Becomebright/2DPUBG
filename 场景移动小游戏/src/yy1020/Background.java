package yy1020;

public class Background implements Runnable{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//System.out.println("123123123123");
			if(ReadMapFile.map1[Player.getI()+1][Player.getJ()+1]==3){
				Player.health.de(1.0);
			}
			if(Player.health.h<0) {
				mainFrame.tag=3;
			}
			else if(mainFrame.dsz1[0].islive==false) {
				mainFrame.tag=4;
			}
		}
	}
}
