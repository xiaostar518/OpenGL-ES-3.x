function DrawLandMaps(){//绘制地面构造方法
	this.mapsData0=mapsData0;//获取地图0块地面物体位置与名称数据
	this.mapsData1=mapsData1;
	this.mapsData2=mapsData2;
	this.mapsData3=mapsData3;
	this.mapsData4=mapsData4;
	this.mapsData5=mapsData5;
	this.mapsData6=mapsData6;
	
	this.treeSize=new Array(10);//树的随机尺寸数组
	for(var i=0;i<10;i++)
	{//循环遍历数组
		var numF=Math.random()*2;//随机获取尺寸
		this.treeSize[i]=numF;//设置树的尺寸
	}
	
	this.treeNum=new Array(6);//树的编码
	for(var i=0;i<this.treeNum.length;i++)
	{
		var numF=Math.round(Math.random()*5);//随机获取树的编号
		this.treeNum[i]=numF;//设置树的编码
	}
	
	this.FlowerSize=new Array(360);//花的随机尺寸
	for(var i=0;i<360;i++)
	{
		this.FlowerSize[i]=Math.random()*1.5;
	}
	this.R=6;//中央水池的大小
	this.r=8;//边上种花的范围
	
	this.Radin =0.017453;//种花的范围
}
DrawLandMaps.prototype.drawLandMaps=function(ms)
{//绘制整体地面方法
	//开启混合
    gl.enable(gl.BLEND);  
    //设置混合因子
	gl.blendFunc(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA);
	
	ms.pushMatrix();//保护现场
	this.drawLeftBelowLand(this.mapsData0,ms);//绘制左下方地面
	ms.popMatrix();//恢复现场
	
	ms.pushMatrix();
	this.drawRightBelowLand(this.mapsData1,ms);
	ms.popMatrix();
	
	ms.pushMatrix();
	this.drawMiddleLeftLand(this.mapsData2,ms);
	ms.popMatrix();
	
	ms.pushMatrix();
	this.drawMiddleRightLand(this.mapsData3,ms);
	ms.popMatrix();
	
	ms.pushMatrix();
	this.drawWestToNorthLand(this.mapsData4,ms);
	ms.popMatrix();
	
	ms.pushMatrix();
	this.drawEastToNorthLand(this.mapsData5,ms);
	ms.popMatrix();
	
	ms.pushMatrix();
	this.drawCenterLand(this.mapsData6,ms);
	ms.popMatrix();
	
	gl.disable(gl.BLEND);//关闭混合
	
};
DrawLandMaps.prototype.drawLeftBelowLand=function(plantData,ms)
{//给左下方铺路方法
	ms.pushMatrix();//保护现场
	for(var i=0;i<plantData.length/3;i++)
	{//循环遍历地图中的数据
		ms.pushMatrix();//保护现场
		//平移到指定位置
		ms.translate(land[0]+plantData[i*3]*landSize,0,plantData[(i*3+1)]*landSize+land[1]);
		
		ms.pushMatrix();//保护现场
		ms.scale(landSize,landSize,landSize);//缩放比例
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//绘制地面物体
		ms.popMatrix();//恢复现场
		
		if(plantData[(i*3+2)]=="grass1_1")
		{//物体名为"grass1_1"
			ms.pushMatrix();//保护现场
			this.num=this.treeNum[plantData[i*3]%6];//获取树的编号
			switch(this.num)
			{//树的编号
				case 4://绘制棕榈树
					ms.pushMatrix();//保护现场
					//缩放比例
					ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+2,this.treeSize[plantData[i*3]%10]+1);
					trees[4].drawSelf(ms,texMap["tree"]);//绘制棕榈树
					ms.popMatrix();//恢复现场
					
					ms.pushMatrix();//保护现场
					ms.translate(0,0,-0.2);//平移到指定位置
					//缩放比例
					ms.scale(this.treeSize[10-plantData[i*3]%10]+1.2,this.treeSize[10-plantData[i*3]%10]+1.8,this.treeSize[10-plantData[i*3]%10]+1);
					trees[4].drawSelf(ms,texMap["tree"]);//绘制棕榈树
					ms.popMatrix();//恢复现场
				
				break;
				case 5://绘制垂柳
					ms.pushMatrix();
					ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+6,this.treeSize[plantData[i*3]%10]+0.8);
					trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
					ms.popMatrix();
					
					ms.pushMatrix();
					ms.translate(0,0,-0.2);
					ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+4,this.treeSize[plantData[i*3]%10]+0.8);
					trees[5].drawSelf(ms,texMap["chuiliu"]);//绘制垂柳
					ms.popMatrix();
				break;
				default :
					trees[this.num].drawSelf(ms,texMap["tree1"]);//绘制棕榈树
				break;
			}
			ms.popMatrix();//恢复现场
		}
		ms.popMatrix();//恢复现场
	}
	ms.popMatrix();//恢复现场
};
DrawLandMaps.prototype.drawRightBelowLand=function(plantData,ms)
{//给右下方铺路
	for(var i=0;i<plantData.length/3;i++)
	{
		ms.pushMatrix();
		ms.translate(land[2]-plantData[i*3]*landSize,0,plantData[(i*3+1)]*landSize+land[3]);
		
		ms.pushMatrix();
		ms.scale(landSize,landSize,landSize);
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//草坪
		ms.popMatrix();
		
		if(plantData[(i*3+2)]=="grass1_1")
		{
			ms.pushMatrix();
			this.num=this.treeNum[plantData[i*3]%6];
			switch(this.num)
			{
				case 4:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+2,this.treeSize[plantData[i*3]%10]+0.8);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.2);
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+1.5,this.treeSize[plantData[i*3]%10]+0.8);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				break;
				case 5:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+4,this.treeSize[plantData[i*3]%10]+0.8);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.2);
				ms.scale(this.treeSize[10-plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+3,this.treeSize[10-plantData[i*3]%10]+0.8);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				break;
				default :
				trees[3-this.num].drawSelf(ms,texMap["tree1"]);//棕榈树
				break;
			}
			
			ms.popMatrix();
		}
		
		ms.popMatrix();
	}
};
DrawLandMaps.prototype.drawMiddleRightLand=function(plantData,ms)
{//给中右方铺路
	ms.pushMatrix();
	for(var i=0;i<plantData.length/3;i++)
	{
		ms.pushMatrix();
		ms.translate(land[4]+plantData[i*3]*landSize,0,land[5]+plantData[(i*3+1)]*landSize);
		
		ms.pushMatrix();
		ms.scale(landSize,landSize,landSize);
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//草坪
		ms.popMatrix();
		
		if(plantData[(i*3+2)]=="grass4_1")
		{
			ms.pushMatrix();
			this.num=this.treeNum[plantData[i*3]%6];
			switch(this.num)
			{
				case 4:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+5,this.treeSize[plantData[i*3]%10]+0.8);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.2);
				ms.scale(this.treeSize[10-plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+4,this.treeSize[plantData[i*3]%10]+0.8);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				break;
				
				case 5:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+2,this.treeSize[plantData[i*3]%10]+0.8);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.2);
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+2.8,this.treeSize[10-plantData[i*3]%10]+0.8);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				break;
				
				default :
				trees[this.num].drawSelf(ms,texMap["tree1"]);//棕榈树
				break;
			}
			ms.popMatrix();
		}
		ms.popMatrix();
	}
	ms.popMatrix();
};
DrawLandMaps.prototype.drawMiddleLeftLand=function(plantData,ms)
{//给中左方铺路
	for(var i=0;i<plantData.length/3;i++)
	{
		ms.pushMatrix();
		ms.translate(land[6]-plantData[i*3]*landSize,0,land[7]+plantData[(i*3+1)]*landSize);
		
		ms.pushMatrix();
		ms.scale(landSize,landSize,landSize);
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//草坪
		ms.popMatrix();
		
		if(plantData[(i*3+2)]=="grass4_1")
		{
			ms.pushMatrix();
			this.num=this.treeNum[plantData[i*3]%6];
			switch(this.num)
			{
				case 4:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+0.5,this.treeSize[plantData[i*3]%10]+2,this.treeSize[plantData[i*3]%10]+0.3);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,0.3);
				ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+2.4,this.treeSize[plantData[i*3]%10]+0.5);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				break;
				case 5:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+4,this.treeSize[plantData[i*3]%10]+1);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.2);
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+6,this.treeSize[plantData[i*3]%10]+0.8);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				break;
				default :
				trees[3-this.num].drawSelf(ms,texMap["tree1"]);//棕榈树
				break;
			}
			ms.popMatrix();
		}
		ms.popMatrix();
	}
};
DrawLandMaps.prototype.drawWestToNorthLand=function(plantData,ms)
{//给西北方铺路
	ms.pushMatrix();
	for(var i=0;i<plantData.length/3;i++)
	{
		ms.pushMatrix();
		ms.translate(land[8]+plantData[i*3]*landSize,0,land[9]+plantData[(i*3+1)]*landSize);
		
		ms.pushMatrix();
		ms.scale(landSize,landSize,landSize);
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//草坪
		ms.popMatrix();
		
		if(plantData[(i*3+2)]=="grass1_1")
		{
			ms.pushMatrix();
			ms.scale(this.treeSize[plantData[i*3]%10]+1.2,this.treeSize[plantData[i*3]%10]+7,this.treeSize[plantData[i*3]%10]+0.8);
			trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
			ms.popMatrix();
			
			ms.pushMatrix();
			ms.translate(0,0,-0.3);
			ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[(plantData[i*3]%10)]+8,this.treeSize[plantData[i*3]%10]+0.8);
			trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
			ms.popMatrix();
		}
		ms.popMatrix();
	}
	ms.popMatrix();
};
DrawLandMaps.prototype.drawEastToNorthLand=function(plantData,ms)
{//给东北方铺路
	ms.pushMatrix();
	for(var i=0;i<plantData.length/3;i++)
	{
		ms.pushMatrix();
		ms.translate(land[10]-plantData[i*3]*landSize,0,land[11]+plantData[(i*3+1)]*landSize);
		
		ms.pushMatrix();
		ms.scale(landSize,landSize,landSize);
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//草坪
		ms.popMatrix();
		
		if(plantData[(i*3+2)]=="grass1_1")
		{
			ms.pushMatrix();
			ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+7,this.treeSize[plantData[i*3]%10]+0.8);
			trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
			ms.popMatrix();
				
			ms.pushMatrix();
			ms.translate(0,0,-0.3);
			ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+5,this.treeSize[plantData[i*3]%10]+0.8);
			trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
			ms.popMatrix();
		}
		ms.popMatrix();
	}
	ms.popMatrix();
};
DrawLandMaps.prototype.drawCenterLand=function(plantData,ms)
{//给中心方铺路
	ms.pushMatrix();
	for(var i=0;i<plantData.length/3;i++)
	{
		ms.pushMatrix();
		ms.translate(land[12]+plantData[i*3]*landSize,0,land[13]+plantData[(i*3+1)]*landSize);
		
		ms.pushMatrix();
		ms.scale(landSize,landSize,landSize);
		grand.drawSelf(ms,texMap[plantData[(i*3+2)]]);//草坪
		ms.popMatrix();
		
		if(plantData[(i*3+2)]=="grass1_1")
		{
			ms.pushMatrix();
			this.num=this.treeNum[plantData[i*3]%6];
			if(this.num<2)
			{
				this.num=5;
			}else
			{
				this.num=4;
			}
				
			switch(this.num)
			{
				case 4:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+2,this.treeSize[9-plantData[i*3]%10]+0.4);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.25);
				ms.scale(this.treeSize[plantData[i*3]%10]+0.8,this.treeSize[plantData[i*3]%10]+1.5,this.treeSize[9-plantData[i*3]%10]+0.4);
				trees[4].drawSelf(ms,texMap["tree"]);//棕榈树
				ms.popMatrix();
				break;
				case 5:
				ms.pushMatrix();
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+5,this.treeSize[plantData[i*3]%10]+0.8);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				
				ms.pushMatrix();
				ms.translate(0,0,-0.25);
				ms.scale(this.treeSize[plantData[i*3]%10]+1,this.treeSize[plantData[i*3]%10]+6,this.treeSize[9-plantData[i*3]%10]+0.5);
				trees[5].drawSelf(ms,texMap["chuiliu"]);//垂柳
				ms.popMatrix();
				break;
			}
			ms.popMatrix();
		}
		ms.popMatrix();
	}
	ms.popMatrix();
	//绘制中央水池
	ms.pushMatrix();
	ms.scale(6,1,6);
	pool.drawSelf(ms,texMap["shuichi"]);
	ms.popMatrix();
	
	ms.pushMatrix();
	//绘制中央水池边的花
	for(var angle=0;angle<360;angle=angle+5)
	{
		ms.pushMatrix();
		var angradElevation=this.Radin*angle;
		ms.translate(this.r*Math.sin(angradElevation),0,this.r*Math.cos(angradElevation));
		ms.scale(1+this.FlowerSize[angle],1.2+this.FlowerSize[angle],1+this.FlowerSize[angle]);
		flower.drawSelf(ms,texMap["suanFlower"]);//花
		ms.popMatrix();
	}
	ms.popMatrix();
};