package com.bn.lll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//地图元素设计面板
public class ItemViewPanel extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6361511310276988768L;
	
	ItemDesignPanel father;
	Image iZw=(new ImageIcon("img/zw.png")).getImage();
	
	public ItemViewPanel(ItemDesignPanel father)
	{
		this.father=father;
		this.addMouseListener(this);
	}
	
	public void paint(Graphics g)
	{
		//画背景
		g.setColor(Color.black);
		g.fillRect(0,0,512,384);
		
		//画元素图
		if(father.tempImage!=null)
		{
			g.drawImage
			(
				father.tempImage,
				0,
				384-father.tempImage.getHeight(this),
				null,
				this
			);			
		}
		
		//画定位线
		g.setColor(Color.green);
		int spanX=ConstantUtil.itemSpanX;
		int spanY=ConstantUtil.itemSpanY;
		
		//画横线span
		for(int i=0;i<8;i++)
		{
			g.drawLine(0,spanY*i,512,spanY*i);
		}
		
		//画竖线
		for(int i=0;i<8;i++)
		{
			g.drawLine(spanX*i,0,spanX*i,384);
		}
		
		//画占位
		g.drawImage(iZw,father.pCol*spanX+22,384-(father.pRow*spanY+spanY-15),this);
	
	}
	
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
	public void mouseClicked(MouseEvent e)
	{
		if(father.status==3)
		{//设置占位点
			int mx=e.getX();
			int my=384-e.getY();
			
			int spanX=ConstantUtil.itemSpanX;
			int spanY=ConstantUtil.itemSpanY;
			
			father.pCol=(mx/spanX-1)+((mx%spanX==0)?0:1);
			father.pRow=(my/spanY-1)+((my%spanY==0)?0:1);
		}
		
		this.repaint();
	}
	
	public static void main(String args[])
	{
		new MainFrame();
	}	
}