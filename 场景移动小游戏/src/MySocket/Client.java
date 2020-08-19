package MySocket;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import com.alibaba.fastjson.JSON;

import yy1020.Bullet;
import yy1020.Circle;
import yy1020.Dsz;
import yy1020.Poi;
import yy1020.UpdateThread;
import yy1020.mainFrame;

public class Client extends Socket {
    private static final String Server_IP = "123.206.27.121"; // ·şÎñ¶ËIP
    private static final int Server_Port = 610;

    public static Socket client;
    private Writer writer;
    private BufferedReader in;

    //¹¹Ôìº¯Êı£¬Óë·şÎñÆ÷½¨Á¢Á¬½Ó
    public Client() throws IOException {
        super(Server_IP, Server_Port);
        this.client = this;
        mainFrame.client = this;
        System.out.println("Client[" + client.getLocalPort() + "] ½øÈë");
        UpdateThread.dsz0.setPort(client.getLocalPort());
    }

    /**
     * Æô¶¯¼àÌıÊÕÈ¡ÏûÏ¢£¬Ñ­»·¿ÉÒÔ²»Í£µÄÊäÈëÏûÏ¢£¬½«ÏûÏ¢·¢ËÍ³öÈ¥
     * msgÎªJSON×Ö·û´®
     * @throws Exception
     */
    public void load() throws IOException, InterruptedException {
        this.writer = new OutputStreamWriter(this.getOutputStream(), "UTF-8");
        new Thread(new ReceiveMsgTask()).start(); //// Æô¶¯¼àÌı
        
        ClientCharacterThread clientThread = new ClientCharacterThread(this);
        Thread write = new Thread(clientThread);
        write.start();
    }
    
    //·¢ËÍMsgCharacter¸øServer
    public void writeToServer() throws IOException{
    	if(UpdateThread.dsz0 == null){
            System.out.println("dsz0 Fuck!!");
            return;
        }
    	if(UpdateThread.dsz0 == null) System.out.println("dsz0 Îª¿Õ Fuck");
        String msg = JSON.toJSONString(UpdateThread.dsz0.toMsg());
        if(msg == null) System.out.println("msg Fuck!!!");
//        System.out.println("send: " + msg); //
        writer.write(msg);
        writer.write("\n");
        writer.flush();

    }
    
    //·¢ËÍMsgBullet¸øServer
    public void writeBulletToServer(MsgBullet msgBullet) throws IOException{
    	String msg = JSON.toJSONString(msgBullet);
    	writer.write(msg);
    	writer.write("\n");
    	writer.flush();
    }
    

    /**
     * ¼àÌı·şÎñÆ÷·¢À´µÄÏûÏ¢Ïß³ÌÀà
     */
    class ReceiveMsgTask implements Runnable{
        private BufferedReader reader;

        @Override
        public void run() {
            try {
                this.reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                while(true){
                	//½ÓÊÕµ½·şÎñ¶ËÏûÏ¢ reply
                    String reply = reader.readLine();
                    
                    //BulletÏûÏ¢, kind = 2
                    if(reply.substring(30, 31).equals("2")){
//                    	System.out.println("Bullet");
                    	MsgBullet tmp = JSON.parseObject(reply, MsgBullet.class);
                    	// Ö»½ÓÊÕÆäËü¿Í»§¶Ë·¢À´µÄÏûÏ¢
                    	if(tmp.getPort() == UpdateThread.dsz0.getPort()) continue;
                    	mainFrame.dsz1.bullet[tmp.getId()] = new Bullet().fromMsg(tmp);
                    	Thread thb=new Thread(mainFrame.dsz1.bullet[tmp.getId()]);
                    	thb.start();
                    	System.out.println("Bu " + "¡¾" + tmp.getX() + "," + tmp.getY() + "¡¿");
<<<<<<< HEAD
                    }
                    
                    //PoiÏûÏ¢, kind = 3
                    else if(reply.substring(9, 10).equals("3")){
                    	System.out.println("##¿ªÊ¼ËõĞ¡¶¾È¦£¡£¡##");
                    	
//                    	List<Poi> pointList = JSON.parseArray(reply, Poi.class);
//                    	Poi[] pointArray = new Poi[6];
//                    	pointList.toArray(pointArray);
//                    	
//                    	Circle circle = new Circle();
//                    	circle.setPoint(pointArray);
//        				Thread thcir = new Thread(circle);
//        				thcir.start();
=======
>>>>>>> parent of e76671c... 18ï¼š17
                    }
                    
                    //CharacterÏûÏ¢, kind = 1
                    else{
//                    	System.out.println("Character");
                    	MsgCharacter tmp = JSON.parseObject(reply, MsgCharacter.class);
                    	if(tmp == null) System.out.println("MsgCharacter Fuck");
                    	else{
                    		// Ö»½ÓÊÕÆäËü¿Í»§¶Ë·¢À´µÄÏûÏ¢
                        	if(tmp.getPort() == UpdateThread.dsz0.getPort()) continue;
<<<<<<< HEAD
<<<<<<< HEAD
                        	
                        	
                        	/**ĞèÒªĞŞ¸Ä**/
                        	for(Dsz dsz : mainFrame.dsz1){
                        		if(dsz.getPort() == 0){
                        			dsz.setPort(tmp.getPort());
                        			dsz.fromMsg(tmp);
                        			break;
                        		}
                        		if(dsz.getPort() == tmp.getPort()){
                        			dsz.fromMsg(tmp);
                        			break;
                        		}
                        	}	
=======
                        	mainFrame.dsz1.fromMsg(tmp);
>>>>>>> parent of e76671c... 18ï¼š17
=======
                        	mainFrame.dsz1.fromMsg(tmp);
>>>>>>> parent of e76671c... 18ï¼š17
//                            System.out.println( "Ch" + " ¡¾" + mainFrame.dsz1.x + ", " + mainFrame.dsz1.y + "¡¿");
                    	}
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                    client.close();
                    in.close();
                } catch (Exception e) {

                }
            }
        }
    }
}
