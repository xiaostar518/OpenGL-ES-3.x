package com.bn.lll;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;

//地图元素设计面板

public class ItemDesignPanel extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7443661768642367771L;
	
	MainFrame father;	
	ItemViewPanel ivp=new ItemViewPanel(this);
	JFileChooser jfc=new JFileChooser(".\\res");	
	Image tempImage;
	String imagePath;
	int pCol;//元素的占位列
	int pRow;//元素的占位行

	JButton jbIn=new JButton("导入图片");
	JButton jbSave=new JButton("保存元素");
	JButton jbSetZW=new JButton("设置占位行列");
	JButton jbSaveList=new JButton("保存元素列表");
	JButton jbLoadList=new JButton("加载元素列表");
	JButton jbDelete=new JButton("删除元素");
	
	JTextField jlLeiMing = new JTextField("");
	
	JList jl=new JList();
	JScrollPane jspL=new JScrollPane(jl);
	
	int status=0;//工作状态 0：什么都不干 1：导入图片  2：保存元素  3：设置占位行列 4:保存元素列表  5:加载元素列表 6：删除元素 
	
	ArrayList<Item> alItem=new ArrayList<Item>();
	
	public ItemDesignPanel(MainFrame father)
	{
		this.father=father;
		
		this.setLayout(null);
		ivp.setBounds(10,10,512,384);
		this.add(ivp);
		
		jbIn.setBounds(540,10,140,30);
		this.add(jbIn);
		jbIn.addActionListener(this);
		
		jbSave.setBounds(540,60,140,30);
		this.add(jbSave);
		jbSave.addActionListener(this);
		
		jbSetZW.setBounds(540,110,140,30);
		this.add(jbSetZW);
		jbSetZW.addActionListener(this);
		
		jbSaveList.setBounds(540,160,140,30);
		this.add(jbSaveList);
		jbSaveList.addActionListener(this);
		
		jbLoadList.setBounds(540,210,140,30);
		this.add(jbLoadList);
		jbLoadList.addActionListener(this);
		
		jbDelete.setBounds(540,260,140,30);
		this.add(jbDelete);
		jbDelete.addActionListener(this);
		
		jlLeiMing.setBounds(10,420,120,30);
		this.add(jlLeiMing);
		
		jspL.setBounds(700,10,140,360);
		this.add(jspL);		
		jl.setCellRenderer(new MyCellRenderer());
		jl.addListSelectionListener(
		   new ListSelectionListener()
		   {
		   	  public void valueChanged(ListSelectionEvent e)
		   	  {
		   	  	  Item item=(Item)jl.getSelectedValue();
		   	  	  if(item==null){return;}
		   	  	  tempImage=item.img;
				  imagePath=item.imgPath;
				  pCol=item.pCol;//元素的占位列
				  pRow=item.pRow;//元素的占位行
				  jlLeiMing.setText(item.leiMing);
					
				  ivp.repaint();		   	  	  
		   	  }
		   }
		);
			
	}
	
	public void flush()
	{
		jl.setListData(alItem.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jbIn)
		{//"导入图片"
		   status=1;
		   int jg=jfc.showOpenDialog(this);
		   if(jg==JFileChooser.APPROVE_OPTION)
		   {
		   	  tempImage=(new ImageIcon(jfc.getSelectedFile().getAbsolutePath())).getImage();
		   	  imagePath="res\\"+jfc.getSelectedFile().getName();
		   	  ivp.repaint();
		   }
		}
		else if(e.getSource()==jbSetZW)
		{//设置占位行列
			status=3;
		}
		else if(e.getSource()==jbSave)
		{//保存元素
			status=2;
			if(tempImage!=null)
			{
				Item item=new Item
				(
					tempImage,imagePath,
					tempImage.getWidth(this),
					tempImage.getHeight(this),
					pCol,
					pRow,
					jlLeiMing.getText()
				);
				alItem.add(item);
				this.flush();	
			}
		}
		else if(e.getSource()==jbSaveList)
		{//保存元素列表
			status=4;
			try
			{
				FileOutputStream fout=new FileOutputStream("data/ItemList.wyf");
				ObjectOutputStream oout=new ObjectOutputStream(fout);
				oout.writeObject(alItem);
				oout.close();
				fout.close();
				System.out.println("保存元素列表成功"+fout.getChannel());
			}
			catch(Exception ea)
			{
				ea.printStackTrace();
			}
		}
		else if(e.getSource()==jbLoadList)
		{//加载元素列表
			status=5;
			try
			{
				FileInputStream fin=new FileInputStream("data/ItemList.wyf");
				ObjectInputStream oin=new ObjectInputStream(fin);
				alItem =(ArrayList<Item>)oin.readObject();
				oin.close();
				fin.close();
				this.flush();
			}
			catch(Exception ea)
			{
				ea.printStackTrace();
			}
		}
		else if(e.getSource()==jbDelete)
		{//删除元素
			status=6;

			if(jl.getSelectedIndex() != -1){
				alItem.remove(jl.getSelectedIndex());
				tempImage = null;
				imagePath = null;
				this.flush();
				ivp.repaint();	
			}
		}
	}
	
	public static void main(String args[])
	{
		new MainFrame();
	}
}