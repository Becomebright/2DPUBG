package yy1020;

import java.awt.Checkbox;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import MySocket.MsgBullet;

public class Bullet implements Runnable{
    int x;
    int y;
    int Direct; 
    int speed = 20;
    int tag;
    boolean isLive = true;
    public Bullet(int x,int y,int Direct,int tag){
        this.x=x;
        this.y=y;
        this.Direct = Direct;
        this.tag=tag;
    }
    public Bullet fromMsg(MsgBullet msg){
    	x = msg.getX();
    	y = msg.getY();
    	Direct = msg.getDirection();
    	tag = 2;
    	isLive = true;
    	return this;
    }
    public Bullet() {
    	
    }
    public int getI(){
		return (y-(20/2))/50;
	}
	//得到角色在数组中的位置J
	public int getJ(){
		return (x-(20/2))/50;
	}
	public void paint(Graphics g) {
    	ImageIcon fireball=new ImageIcon("img\\fireball"+Direct+".png");
    	switch(Direct) {
    	case 1:
    		g.drawImage(fireball.getImage(), x-Player.x+Player.px-40, y-Player.y+Player.py-75, 36, 75, null);
    		break;
    	case 2:	
    		g.drawImage(fireball.getImage(), x-Player.x+Player.px-40, y-Player.y+Player.py, 36, 75, null);
    		break;
    	case 3:	
    		g.drawImage(fireball.getImage(), x-Player.x+Player.px-75, y-Player.y+Player.py-25, 75, 36, null);
    		break;
    	case 4:	
    		g.drawImage(fireball.getImage(), x-Player.x+Player.px-25, y-Player.y+Player.py-25, 75, 36, null);
    		break;
    	}
    	//g.drawImage(fireball.getImage(), x-Player.x+Player.px-25, y-Player.y+Player.py-25, 75, 36, null);
    }
	public void check(Dsz dsz1) {
		if(dsz1.islive==true&&dsz1.getI()==getI()&&dsz1.getJ()==getJ()) {
        	dsz1.health-=10;
        	isLive = false;
        }
	}
	public void check() {
		if(Player.getI()+1==getI()&&Player.getJ()+1==getJ()) {
        	Player.health.de(10.0);
        	isLive = false;
        }
	}
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // TODO: handle exception
            }
            switch(Direct){
            case 1: 
                y-=speed;
                break;
            case 2:
                y+=speed;
                break;
            case 3:
                x-=speed;
                break;
            case 4:
                x+=speed;
                break;
            default:
                break;
            }
            /*if(tag==1&&mainFrame.dsz1.islive==true&&mainFrame.dsz1.getI()+1==getI()&&mainFrame.dsz1.getJ()==getJ()) {
            	mainFrame.dsz1.health-=10;
            	isLive = false;
                break;
            }*/
            for(int i=0;i<=3;i++) {
            	 if(tag==1&&mainFrame.dsz1[i].islive==true&&mainFrame.dsz1[i].x<=x+15&&mainFrame.dsz1[i].y<=y+25&&mainFrame.dsz1[i].x>=x-45&&mainFrame.dsz1[i].y>=y-25) {
            		 mainFrame.dsz1[i].health-=1;
            		 isLive = false;
            		 break;
            	 }
            }
           
            if(tag==2) {
            		if(isLive==true) {
            			if(x+25>=Player.x+25&&y+25>=Player.y&&x-25<=Player.x&&y-25<=Player.y) {
            				Player.health.de(2.0);
            				isLive=false;
            				break;
            			}
            			/*if(getI()==Player.getI()&&getJ()==Player.getJ()) {
            				Player.health.de(1.0);
            				isLive=false;
            				break;
            			}*/
            		}
            }
//          System.out.println(""+x+" "+y);
            //子弹何时死亡
            if(x<0||x>7000||y<0||y>7000){
                isLive = false;
                break;
            }
        }
    }
}