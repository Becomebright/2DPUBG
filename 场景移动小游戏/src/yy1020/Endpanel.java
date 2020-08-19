package yy1020;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Endpanel extends JPanel implements gameConfig{
	JPanel buttonpanel;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Endpanel() {
		super();
		init();
	}
	
	@SuppressWarnings("serial")
	public void init(){
		this.setBounds(1,1,frameX,frameY);
		this.setLayout(null);
		this.setVisible(false);
		//this.setOpaque(false);//设置面板透明
		
		
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		ImageIcon bg=new ImageIcon("img\\endbg.jpg");
		g.drawImage(bg.getImage(), 0, 0, frameX, frameY, null);
		if(mainFrame.tag==3) {
			ImageIcon gg=new ImageIcon("img\\gameover.png");
			g.drawImage(gg.getImage(), 230,200, 500, 200, null);
		}
		else {
			ImageIcon gg=new ImageIcon("img\\win.png");
			g.drawImage(gg.getImage(), 230,200, 500, 200, null);
		}
		buttonpanel=new JPanel();
		buttonpanel.setLayout(null);
		buttonpanel.setBounds(230, 500, 500, 200);
		buttonpanel.setVisible(true);
		JButton on = new JButton();
        on.setSize(new Dimension(500, 200));
        on.setLocation(0, 0);
        on.setIcon(new ImageIcon("img\\exit.png"));
       // on.mouseUp(new Event(0, 0, 0), 280, 300);
        on.addActionListener(new AbstractAction() { 
			@Override
            public void actionPerformed(ActionEvent e) {
                
				test.mf.dispose();
				//StartFrame startFrame=new StartFrame();
				System.exit(0);
				//mainFrame.tag=1;
				//StartFrame startFrame=new StartFrame();
            }
        });
        buttonpanel.add(on);
		this.add(buttonpanel);
		/*
			g.setColor(Color.BLUE);
			Font font = new Font("黑体", 600, 25);
			g.setFont(font);
			g.drawString(npc.name+":", 30, 30);
			g.setColor(Color.GREEN);
			g.drawString(npc.talk, 60, 65);*/
		
	}

}
