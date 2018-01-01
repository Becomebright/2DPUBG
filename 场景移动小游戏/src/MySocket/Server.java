package MySocket;

import com.alibaba.fastjson.JSON;
import yy1020.Dsz;
import yy1020.Poi;
import yy1020.mainFrame;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Server extends ServerSocket {
    private static final int Server_Port = 610;

    //����һ������Ϊ50���̳߳�
    private static ExecutorService service = Executors.newFixedThreadPool(50);

    private static CopyOnWriteArrayList<String> userList = new CopyOnWriteArrayList<String>();
    private static CopyOnWriteArrayList<Task> threadList =  new CopyOnWriteArrayList<Task>();
    private static BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(50);
    
    private Poi[] point; //��Ȧ����
    private int clientNumber = 4; //��Ϸ����
    

    public Server() throws IOException {
        super(Server_Port);
        point = new Poi[6];
        randCircle();
    }
    
    /**
     * ������ɶ�Ȧ
     */
    private void randCircle() {
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

    /**
     * ������ͻ��˷�����Ϣ���̣߳�ʹ���̴߳���ÿ���ͻ��˷�������Ϣ
     *
     * @throws Exception
     */
    public void load() throws Exception {
        new Thread(new PushMsgTask()).start(); // ������ͻ��˷�����Ϣ���߳�

        while (true) {
            // server���Խ�������Socket����������server��accept����������ʽ��
            Socket socket = this.accept();
            /**
             * ���ǵķ���˴���ͻ��˵�����������ͬ�����еģ� ÿ�ν��յ����Կͻ��˵����������
             * ��Ҫ�ȸ���ǰ�Ŀͻ���ͨ����֮������ٴ�����һ���������� ���ڲ����Ƚ϶������»�����Ӱ���������ܣ�
             * Ϊ�ˣ����ǿ��԰�����Ϊ���������첽������ͻ���ͨ�ŵķ�ʽ
             */
            // ÿ���յ�һ��Socket�ͽ���һ���µ��߳���������
            service.execute(new Task(socket, socket.getPort()));
            Dsz dsz1 = new Dsz(375,375,1);
            dsz1.setPort(socket.getPort());
            MsgCharacter msgCharacter = dsz1.toMsg();
            msgQueue.put(JSON.toJSONString(msgCharacter));
        }
    }

    /**
     * ����Ϣ������ȡ��Ϣ���ٷ��͸����пͻ���
     */
    class PushMsgTask implements Runnable{
        @Override
        public void run() {
            while(true){
                String msg = null;
                try {
                    msg = msgQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(msg != null){
                    for(Task thread : threadList){
                        thread.sendMsg(msg);
                    }
                }
            }
        }
    }

    /**
     * ����ͻ��˷�������Ϣ�߳���
     */
    class Task implements Runnable{
        private Socket socket;
        private BufferedReader reader;
        private Writer writer;
        private String userName;
        private int port;
        
        private boolean hasSentCircle = false; //�Ƿ��Ѿ����͹���Ȧ��Ϣ
        

        /**
         * ���캯��<br>
         * ����ͻ��˵���Ϣ
         * @throws Exception
         */
        public Task(Socket socket, int port) {
            this.socket = socket;
            this.port = port;
            this.userName = String.valueOf(socket.getPort());
            try {
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                this.writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            userList.add(this.userName);
            threadList.add(this);
            System.out.println("Form Client[port:" + socket.getPort() + "] "
                    + this.userName + "��������Ϸ");
            
            // �������˶�������Ϸ�󣬷��Ͷ�Ȧ��Ϣ
            if(!hasSentCircle && userList.size() == clientNumber){
            	String circleMsg = JSON.toJSONString(point);
            	pushMsg(circleMsg);
            	hasSentCircle = true;
            }
        }

        @Override
        public void run() {
            try {
            	
                while(true){
                    String msg = reader.readLine();
                    if(msg==null) System.out.println("Server.Task.run.msg Fuck");
                    else {
                    	Date day = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                  
                        System.out.println(dateFormat.format(day) + msg);
                        pushMsg(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                    reader.close();
                    socket.close();
                } catch (Exception e) {

                }
                userList.remove(userName);
                threadList.remove(this);
//                pushMsg("��" + userName + "�˳�����Ϸ��");
                System.out.println("Form Client[port:" + socket.getPort() + "] "
                        + userName + "�˳�����Ϸ");
            }
        }

        /**
         * ׼�����͵���Ϣ�������
         *
         * @param msg
         */
        private void pushMsg(String msg){
            try {
                msgQueue.put(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sendMsg(String msg){
            try {
                // ����Ϣ���͸��ͻ���
                writer.write(msg);
                writer.write("\n");
                writer.flush();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
