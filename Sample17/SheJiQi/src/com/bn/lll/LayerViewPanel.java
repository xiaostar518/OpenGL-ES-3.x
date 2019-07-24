package com.bn.lll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//地图层绘制面板
public class LayerViewPanel extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5919783310156740753L;
	
	LayerDesignPanel father;
	public boolean flag=true;
	
	public LayerViewPanel(LayerDesignPanel father)//构造器
	{
		this.father=father;
		this.setPreferredSize(new Dimension(ConstantUtil.Rows+10,ConstantUtil.Cols+10));//1930,1450
		this.addMouseListener(this);
	}
	
	public void paint(Graphics g)
	{
		//画背景
		g.setColor(Color.black);
		g.fillRect(0,0,ConstantUtil.Rows+10,ConstantUtil.Cols+10);
		int spanX=ConstantUtil.itemSpanX;
		int spanY=ConstantUtil.itemSpanY;
		
		if(flag)//绘制地图元素
		{
			for(int i=0;i<ConstantUtil.Row;i++)
			{
				for(int j=0;j<ConstantUtil.Col;j++)
				{
					Item item=father.itemArray[i][j];//层元素
					if(item!=null)
					{
						item.draw(g,this);
					}
				}
			}
		}
		
		//画定位线-横线
		for(int i=0;i<ConstantUtil.Row;i++)
		{
			g.setColor(Color.green);
			g.drawLine(0,i*spanY,ConstantUtil.Rows,i*spanY);
		}
		
	    //画定位线-竖线线
		for(int i=0;i<ConstantUtil.Col;i++)
		{
			g.setColor(Color.green);
			g.drawLine(i*spanX,0,i*spanX,ConstantUtil.Cols);
		}	
	}
	
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
	public void mouseClicked(MouseEvent e)
	{
		int mx=e.getX();
		int my=e.getY();
		int spanX=ConstantUtil.itemSpanX;
		int spanY=ConstantUtil.itemSpanY;
		
		int col=(mx/spanX-1)+((mx%spanX==0)?0:1);
		int row=(my/spanY-1)+((my%spanY==0)?0:1);
		
		Item item=((Item)(father.jl.getSelectedValue())).clone();
		
		father.itemArray[row][col]=item;//设计层地图
		if(item!=null)
		{
			item.setPosition(col,row);
		}	
		this.repaint();
	}	
	
	public static void main(String args[])
	{
		new MainFrame();
	}
}