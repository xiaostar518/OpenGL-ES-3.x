package com.bn.lll;

import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

//地图层设计面板
public class LayerDesignPanel extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -250254695630556258L;
	
	MainFrame father;	
	Item[][] itemArray=new Item[ConstantUtil.Row][ConstantUtil.Col];
	
	LayerViewPanel lvp=new LayerViewPanel(this);
	JScrollPane jsp=new JScrollPane(lvp);

	JList jl=new JList();
	JScrollPane jspL=new JScrollPane(jl);
	
	JButton jcb=new JButton("加载层");
	
	JButton jbSaveLayer=new JButton("保存层");
	JButton jbclear=new JButton("清除");
	
	JButton jbLoadLayer1=new JButton("设计层");
	
	JButton jbCreate=new JButton("生成地图文件");
	JButton jbLoadAll=new JButton("全部铺上当前选中");
	
	JTextField jtfCengMing=new JTextField("Layer");
	
	final int FileNum=10;//文件的最大数量
	
	public LayerDesignPanel(MainFrame father)
	{
		this.father=father;
		this.setLayout(null);
		
		jsp.setBounds(8,8,1200,620);
		this.add(jsp);
		
		jspL.setBounds(1220,10,120,200);
		this.add(jspL);		
		jl.setCellRenderer(new MyCellRenderer());
		
		jcb.setBounds(1220,240,120,20);//加载层
		this.add(jcb);
		jcb.addActionListener(this);
		
		jbLoadLayer1.setBounds(1220,280,120,20);//设计层
		this.add(jbLoadLayer1);
		jbLoadLayer1.addActionListener(this);
		
		jbSaveLayer.setBounds(1220,340,120,20);//保存层
		this.add(jbSaveLayer);
		jbSaveLayer.addActionListener(this);
		
		jbLoadAll.setBounds(1220,370,120,20);//平铺
		this.add(jbLoadAll);
		jbLoadAll.addActionListener(this);
		
		jbCreate.setBounds(1220,400,120,20);//创建地图
		this.add(jbCreate);
		jbCreate.addActionListener(this);		
		
		jtfCengMing.setBounds(1220,430,120,20);//文件名
		this.add(jtfCengMing);
		
		jbclear.setBounds(1220,460,120,20);//清除
		this.add(jbclear);
		jbclear.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==jbclear)//清除
		{
			lvp.flag=false;//不绘制图片
			lvp.repaint();
		}
		else if(e.getSource()==jbSaveLayer)//保存
		{
			try
			{
				//保存层图
				FileOutputStream fout=new FileOutputStream("data/"+jtfCengMing.getText()+".wyf");
				ObjectOutputStream oout=new ObjectOutputStream(fout);
				oout.writeObject(itemArray);
				oout.close();
				fout.close();
				System.out.println("保存层图成功");
			}
			catch(Exception ea)
			{
				ea.printStackTrace();
			}			
		}else if(e.getSource()==jbLoadLayer1)//设计层
		{
			try
			{
				FileInputStream fin=new FileInputStream("data/"+jtfCengMing.getText()+".wyf");
				ObjectInputStream oin=new ObjectInputStream(fin);
				itemArray=(Item[][])oin.readObject();		
				oin.close();
				fin.close();
				this.flush();
				System.out.println("设计层");
			}
			catch(Exception ea)
			{
				ea.printStackTrace();
			}
			lvp.flag=true;	
			lvp.repaint();
		}else if(e.getSource()==jcb)//加载层图
		{
			try
			{
				FileInputStream fin=new FileInputStream("data/"+jtfCengMing.getText()+".wyf");
				ObjectInputStream oin=new ObjectInputStream(fin);
				itemArray =(Item[][])oin.readObject();		
				oin.close();
				fin.close();
				this.flush();
				System.out.println("加载层图成功");
			}
			catch(Exception ea)
			{
				ea.printStackTrace();
			}
			lvp.flag=true;//绘制图片
			lvp.repaint();		
		}else if(e.getSource()==jbLoadAll)//全部平铺
		{//全部铺上当前选中
			for(int row=0; row<ConstantUtil.Row; row++)
			{
				for(int col=0; col<ConstantUtil.Col; col++)
				{
					Item item=((Item)(jl.getSelectedValue())).clone();
					itemArray[row][col]=item;
					if(item!=null)
					{
						item.setPosition(col,row);
					}	
					
				}
			}
			lvp.flag=true;
			lvp.repaint();
		}else if(e.getSource()==jbCreate)
		{//单击生成地图文件按钮
			int index=0;//确定文件数量
			try
			{	
				for(;index<FileNum;index++)
				{
					File f=new File("data/maps"+index+".js");
					if(!f.exists())
					{
						f.createNewFile();//创建文件夹
						break;
					}
				}
					
				
				//输出到JS文件============================================
			    StringBuilder sb=new StringBuilder();
			    //输出顶点坐标信息
			    sb.append("var mapsData"+index+"=new Array(\n");
			    
				int totalBlocks=0;//物体个数
				for(int i=0; i<ConstantUtil.Row; i++)
				{
					for(int j=0; j<ConstantUtil.Col; j++)
					{
						Item item=itemArray[i][j];
						if(item != null)
						{
							totalBlocks++;
						}
					}
				}
				System.out.println("物体个数totalBlocks=="+totalBlocks);
				for(int i=0; i<ConstantUtil.Row; i++)
				{
					for(int j=0; j<ConstantUtil.Col; j++)
					{
						Item item=itemArray[i][j];
						if(item != null)
						{
							int col = item.col;//元素的地图列
							int row = item.row;//元素的地图行
							String leiMing = "\""+item.leiMing+"\"";//类名
					
							sb.append(col);	//该元素的列
							sb.append(",");//分割符
		
							sb.append(row);	//该元素的行
							sb.append(",");//分割符
							
							sb.append(leiMing.trim());//存放图片名
							if(totalBlocks>1)//物体个数大于1
					    	{
					    		sb.append(",");//分割符
					    	}
					    	totalBlocks--;//物体数减一
						}							
				    }
				}
				sb.append(");\n");
				
				FileWriter fw=new FileWriter("data/maps"+index+".js");	
			    fw.write(sb.toString());
			    fw.close();
			    System.out.println("OK.........");
			}
			catch(Exception ea)
			{
				ea.printStackTrace();
			}
		}
	}
	
	public void flush()
	{
		ArrayList<Item> alItem=father.idp.alItem;
		jl.setListData(alItem.toArray());
	}

	public static void main(String args[])
	{
		new MainFrame();
	}
}