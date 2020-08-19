package yy1020;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import MySocket.Client;
import MySocket.MsgBullet;

/**
 * 游戏主窗体
 * @author yy
 *
 */
public class mainFrame extends JFrame implements gameConfig{
	
	public static Client client;
	//游戏状态   1为行走 2为对话
	static int tag = 1;
	//游戏面板
	JPanel panel;
	//对话面板
	JPanel tpanel;
	
	Endpanel epanel;
	//public static Dsz dsz1;
	public static Dsz dsz1[]=new Dsz[4];
	public static int bnum = 0;
	public mainFrame() {
		init();
		
	}
	/**
	 * 设置窗体
	 */
	public void init(){
		this.setTitle(title);
		this.setSize(frameX, frameY);
		this.setLayout(null);
		this.setDefaultCloseOperation(3);
//		//设置窗体无边框
//		this.setUndecorated(true);
		//创建对话框面板
		tpanel = settpanel();
		//创建游戏面板
		panel = setpanel();
		
		epanel=new Endpanel();
		
		this.add(tpanel);
		this.add(panel);
		this.add(epanel);
		
		this.setVisible(true);
		//安装键盘监听器
		PanelListenner plis = new PanelListenner();
		this.addKeyListener(plis);
		
		//启动人物移动线程
		Player player = new Player();
		player.start();
		
		//启动刷新面板线程
		UpdateThread ut = new UpdateThread(panel,tpanel,epanel);
		ut.start();
		
		//启动其他玩家类
		for(int i=0;i<=3;i++) {
			dsz1[i]=new Dsz(375,375,0);
		}
		//dsz1=new Dsz(375,375,0);
		//Thread thdsz1=new Thread(dsz1);
		//thdsz1.start();
	}
	
	/**
	 * 设置游戏面板
	 */

	public JPanel setpanel(){
		JPanel panel = new MyPanel();
		panel.setBounds(18, 5, panelX, panelY);
//		panel.setPreferredSize(new Dimension(panelX, panelY));
		panel.setLayout(null);
		panel.setBackground(Color.black);
		
		return panel;
	}
	/**
	 * 设置对话面板
	 * @return
	 */
	public JPanel settpanel(){
		JPanel tpanel = new TalkPanel();
		return tpanel;
	}
	
	/**
	 * 内部游戏按键监听类
	 * @author yy
	 *
	 */
	class PanelListenner extends KeyAdapter{
		//当按键按下
		public void keyPressed(KeyEvent e){
			int code = e.getKeyCode();
			if(tag==1){
				switch (code) {
				case KeyEvent.VK_SPACE:
					Player.shot();
					if(client != null){
						MsgBullet msgBullet = new MsgBullet();
						msgBullet.setPort(UpdateThread.dsz0.getPort());
						msgBullet.setX(UpdateThread.dsz0.getX());
						msgBullet.setY(UpdateThread.dsz0.getY());
						msgBullet.setDirection(Player.towards);
						
						Bullet[] bullet1 = Player.bullet1;
						
						for(int i=0;i<=4;i++) {
							if(bullet1[i]!=null&&bullet1[i].isLive==true) {
								if(i==4) {
									bnum=(bnum+1)%5;
									break;
								}
								continue;
							}
							bnum = i;
						}
						msgBullet.setId(bnum);
						try {
							client.writeBulletToServer(msgBullet);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							client.writeBulletToServer(msgBullet);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					break;
				case KeyEvent.VK_UP:
					Player.up = true;
					Player.towards = 1;
					break;
				case KeyEvent.VK_DOWN:
					Player.down = true;
					Player.towards = 2;
					break;
				case KeyEvent.VK_LEFT:
					Player.left = true;
					Player.towards = 3;
					break;
				case KeyEvent.VK_RIGHT:
					Player.right = true;
					Player.towards = 4;
					break;
				case KeyEvent.VK_G://按下了对话键
					if(Player.towards==1){//角色朝着上
						int num = ReadMapFile.map3[Player.y/elesize-1][Player.x/elesize];
						if(num!=0){//上方有npc
							if(GetNPC.map.get(num)==null){
								GetNPC.getnpc(num);
								TalkPanel.gettalknpc(num);
							}
							tag = 2;
							tpanel.setBounds(28, 500, 630, 150);
							
							tpanel.repaint();
							System.out.println(1);
						}
					}else if(Player.towards==2){//角色朝着下
						if(ReadMapFile.map3[Player.y/elesize+1][Player.x/elesize]!=0){//下方有npc
							tpanel.setBounds(28, 500, 630, 150);
							tag = 2;//进入对话模式
							tpanel.repaint();
						}
					}else if(Player.towards==3){//角色朝着左
						if(ReadMapFile.map3[Player.y/elesize][Player.x/elesize-1]!=0){//左方有npc
							tpanel.setBounds(28, 500, 630, 150);
							tag = 2;
							tpanel.repaint();
						}
					}else if(Player.towards==4){//角色朝着下
						if(ReadMapFile.map3[Player.y/elesize][Player.x/elesize+1]!=0){//右方有npc
							tpanel.setBounds(28, 500, 630, 150);
							tag = 2;
							tpanel.repaint();
						}
					}
					
					break;

				default:
					break;
				}
			}else if(tag==2){
				if(code==KeyEvent.VK_G){
					tag=1;
					tpanel.setBounds(28, 500, 0, 0);
				}
			}
			
		}
		//当按键释放
		public void keyReleased(KeyEvent e){
			if(tag==1){
				int code = e.getKeyCode();
				switch (code) {
				case KeyEvent.VK_UP:
					Player.up = false;
					Player.up1 = 0;
					break;
				case KeyEvent.VK_DOWN:
					Player.down = false;
					Player.down1 = 0;
					break;
				case KeyEvent.VK_LEFT:
					Player.left = false;
					Player.left1 = 0;
					break;
				case KeyEvent.VK_RIGHT:
					Player.right = false;
					Player.right1 = 0;
					break;

				default:
					break;
				}
			}
		}
	}
	/**
	 * 自定义内部游戏面板类
	 * @author yy
	 *
	 */
	class MyPanel extends JPanel{
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			//找到角色旁边的素材，上下左右各5格
			for(int i=Player.getI()-12;i<=Player.getI()+12;i++){
				for(int j=Player.getJ()-12;j<=Player.getJ()+12;j++){
					//如果这一格没有超界
					if(i>=0&&j>=0&&i<ReadMapFile.map1.length&&j<ReadMapFile.map1[0].length){
						//画第一层元素
						ImageIcon icon = GetMap.int2icon(ReadMapFile.map1[i][j]);
						g.drawImage(icon.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
						//第二层
						if(ReadMapFile.map2[i][j]!=0){
							ImageIcon icon2 = GetMap.int2icon(ReadMapFile.map2[i][j]);
							g.drawImage(icon2.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
						}
						//第三层
						if(ReadMapFile.map3[i][j]!=0){
							ImageIcon icon3 = GetMap.int2npc(ReadMapFile.map3[i][j]);
							g.drawImage(icon3.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
						}
						//第四层，毒圈
//						if(ReadMapFile.map4[i][j]!=0){
//							ImageIcon icon4 = GetMap.int2npc(ReadMapFile.map4[i][j]);
//							g.drawImage(icon4.getImage(), (Player.px-elesize/2)+((j-Player.getJ())*elesize)-(Player.mx%elesize), (Player.py-elesize/2)+((i-Player.getI())*elesize)-(Player.my%elesize), elesize, elesize, null);
//						}
					}
				}
			}
//			g.setColor(Color.black);
//			g.fillRect(0, 0, 50, 650);
//			g.fillRect(0, 0, 650, 50);
//			g.fillRect(600, 0, 50, 650);
//			g.fillRect(0, 600, 650, 50);
			
			Player.draw(g);
			//System.out.format("%d",ReadMapFile.map1[0][0]);  
			for(int i=0;i<=4;i++) {
				if(Player.bullet1[i]!=null&&Player.bullet1[i].isLive==true){
		          //System.out.format("%d   ",i);            
					Player.bullet1[i].paint(g);
					// g.setColor(Color.red);
		            //g.draw3DRect(Player.bullet1[i].x-Player.mx+Player.px,Player.bullet1[i].y-Player.my+Player.py, 20, 20, false);
		        }
			}
			
			Player.health.paint(g);
			for(int i=0;i<=3;i++) {
				if(dsz1[i]!=null&&dsz1[i].islive==true) {
					dsz1[i].paint(g);
				}
			}
			
			/*if(UpdateThread.dsz0!=null&&(UpdateThread.dsz0.islive==true)) {
				UpdateThread.dsz0.paint(g);
			}*/
			SmallMap smallmap = new SmallMap();
			smallmap.paint(g);
			/*if(tag==3) {
				epanel.paint(g);
			}*/
			//ImageIcon bg=new ImageIcon("img\\endbg.png");
			//g.drawImage(bg.getImage(), 0, 0, frameX, frameY, null);
			//npc
//			g.drawImage(npc1.getImage(), 400, 400, 50, 50, null);
			
			
			//个人的一个小想法，做一个黑色的图片，然后中间挖空一个圆，加上模糊效果，来模拟人的视野
			//g.drawImage(shadow2.getImage(), 0, 0, 650, 650, null);
		}
	}
}
