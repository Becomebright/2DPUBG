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
    private static final String Server_IP = "123.206.27.121"; // �����IP
    private static final int Server_Port = 610;

    public static Socket client;
    private Writer writer;
    private BufferedReader in;

    //���캯�������������������
    public Client() throws IOException {
        super(Server_IP, Server_Port);
        this.client = this;
        mainFrame.client = this;
        System.out.println("Client[" + client.getLocalPort() + "] ����");
        UpdateThread.dsz0.setPort(client.getLocalPort());
    }

    /**
     * ����������ȡ��Ϣ��ѭ�����Բ�ͣ��������Ϣ������Ϣ���ͳ�ȥ
     * msgΪJSON�ַ���
     * @throws Exception
     */
    public void load() throws IOException, InterruptedException {
        this.writer = new OutputStreamWriter(this.getOutputStream(), "UTF-8");
        new Thread(new ReceiveMsgTask()).start(); //// ��������
        
        ClientCharacterThread clientThread = new ClientCharacterThread(this);
        Thread write = new Thread(clientThread);
        write.start();
    }
    
    //����MsgCharacter��Server
    public void writeToServer() throws IOException{
    	if(UpdateThread.dsz0 == null){
            System.out.println("dsz0 Fuck!!");
            return;
        }
    	if(UpdateThread.dsz0 == null) System.out.println("dsz0 Ϊ�� Fuck");
        String msg = JSON.toJSONString(UpdateThread.dsz0.toMsg());
        if(msg == null) System.out.println("msg Fuck!!!");
//        System.out.println("send: " + msg); //
        writer.write(msg);
        writer.write("\n");
        writer.flush();

    }
    
    //����MsgBullet��Server
    public void writeBulletToServer(MsgBullet msgBullet) throws IOException{
    	String msg = JSON.toJSONString(msgBullet);
    	writer.write(msg);
    	writer.write("\n");
    	writer.flush();
    }
    

    /**
     * ������������������Ϣ�߳���
     */
    class ReceiveMsgTask implements Runnable{
        private BufferedReader reader;

        @Override
        public void run() {
            try {
                this.reader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                while(true){
                	//���յ��������Ϣ reply
                    String reply = reader.readLine();
                    
                    //Bullet��Ϣ, kind = 2
                    if(reply.substring(30, 31).equals("2")){
//                    	System.out.println("Bullet");
                    	MsgBullet tmp = JSON.parseObject(reply, MsgBullet.class);
                    	// ֻ���������ͻ��˷�������Ϣ
                    	if(tmp.getPort() == UpdateThread.dsz0.getPort()) continue;
                    	mainFrame.dsz1.bullet[tmp.getId()] = new Bullet().fromMsg(tmp);
                    	Thread thb=new Thread(mainFrame.dsz1.bullet[tmp.getId()]);
                    	thb.start();
                    	System.out.println("Bu " + "��" + tmp.getX() + "," + tmp.getY() + "��");
<<<<<<< HEAD
                    }
                    
                    //Poi��Ϣ, kind = 3
                    else if(reply.substring(9, 10).equals("3")){
                    	System.out.println("##��ʼ��С��Ȧ����##");
                    	
//                    	List<Poi> pointList = JSON.parseArray(reply, Poi.class);
//                    	Poi[] pointArray = new Poi[6];
//                    	pointList.toArray(pointArray);
//                    	
//                    	Circle circle = new Circle();
//                    	circle.setPoint(pointArray);
//        				Thread thcir = new Thread(circle);
//        				thcir.start();
=======
>>>>>>> parent of e76671c... 18：17
                    }
                    
                    //Character��Ϣ, kind = 1
                    else{
//                    	System.out.println("Character");
                    	MsgCharacter tmp = JSON.parseObject(reply, MsgCharacter.class);
                    	if(tmp == null) System.out.println("MsgCharacter Fuck");
                    	else{
                    		// ֻ���������ͻ��˷�������Ϣ
                        	if(tmp.getPort() == UpdateThread.dsz0.getPort()) continue;
<<<<<<< HEAD
<<<<<<< HEAD
                        	
                        	
                        	/**��Ҫ�޸�**/
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
>>>>>>> parent of e76671c... 18：17
=======
                        	mainFrame.dsz1.fromMsg(tmp);
>>>>>>> parent of e76671c... 18：17
//                            System.out.println( "Ch" + " ��" + mainFrame.dsz1.x + ", " + mainFrame.dsz1.y + "��");
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
