package com.bn.lll;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.swing.ImageIcon;

public class Item implements Externalizable
{
	
	Image img;//元素的图片
	String imgPath;
	int w;//元素的图片宽度
	int h;//元素的图片高度
	
	int col;//元素的地图列
	int row;//元素的地图行
	
	int pCol;//元素的占位列
	int pRow;//元素的占位行
	
	String leiMing;//类名
	
	public Item()
	{
	}
	
	public Item(Image img,String imgPath,int w,int h,int pCol,int pRow,String leiMing)
	{
		this.img=img;
		this.imgPath=imgPath;
		this.w=w;
		this.h=h;
		this.pCol=pCol;
		this.pRow=pRow;
		this.leiMing = leiMing;
		System.out.println("============="+leiMing+"==============");
	}
	
	public void setPosition(int col,int row)
	{
		this.col=col;
		this.row=row;
	}
	
	public void draw(Graphics g,ImageObserver io)
	{		
		int spanX=ConstantUtil.itemSpanX;
		int spanY=ConstantUtil.itemSpanY;
		
		int x=(col-pCol)*spanX;
		int y=row*spanY+(pRow+1)*spanY-h;
		
		g.drawImage(img,x,y,io);
	}
	
	public Item clone()
	{
		return new Item(img,imgPath,w,h,pCol,pRow,leiMing);
	}
	
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(imgPath);
		out.writeInt(w);
		out.writeInt(h);
		out.writeInt(pCol);
		out.writeInt(pRow);
		out.writeInt(col);
		out.writeInt(row);
		out.writeUTF(leiMing);
	}
	
	public void readExternal(ObjectInput in)throws IOException,ClassNotFoundException
	{
		imgPath=(String)(in.readObject());
		img=new ImageIcon(imgPath).getImage();
		
		w=in.readInt();
		h=in.readInt();
		pCol=in.readInt();
		pRow=in.readInt();
		
		col=in.readInt();
		row=in.readInt();
		
		leiMing=in.readUTF();
	}

	public static void main(String args[])
	{
		new MainFrame();
	}
}
