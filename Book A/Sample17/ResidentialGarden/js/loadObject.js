function loadObjFile(url,object,id)
{//加载模型文件方法
	var req=new XMLHttpRequest();//创建XMLHttpRequest对象
	//重写onreadystatechange事件
	req.onreadystatechange=function (){processObjectLoadObj(req,object,id)};
	req.open("GET",url,true);//调用open方法
	req.responseType="text";//设置responseType
	req.send(null);//调用send方法
}
function createObject(objDataIn,object,id)
{//创建物体方法
	if(shaderProgArray[id])
	{//拿到的着色器是否为空
		switch(object)
		{//物体编号
			case 0://创建地图对象
				map=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
			break;
	  
			case 1://创建天空盒对象
				skybox=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
			break;
		  
			case 2://创建矩形绘制对象
				rectangle=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]); 
			break;
		  
			case 3://创建水池对象
				pool=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
			break;
		  
			case 4://创建草坪对象
				grand=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
			break;
			
			case 5://创建主界面的2D对象
				obj=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);//
				//创建背景图对象
				mainView_rectangle=new BasicObject(obj,48,12,600,300,texMap["bg"]);
				
				//创建普通状态的按钮
				lable[0]=new BasicObject(obj,7,2,1050,290,texMap["label1_1"]);
				lable[1]=new BasicObject(obj,7,2,1050,440,texMap["label0_1"]);
				
				lable[2]=new BasicObject(obj,6.4,1.4,160,236,texMap["label2_1"]);
				lable[3]=new BasicObject(obj,6.4,1.4,160,316,texMap["label3_1"]);
				lable[4]=new BasicObject(obj,6.4,1.4,160,396,texMap["label4_1"]);
				lable[5]=new BasicObject(obj,6.4,1.4,160,476,texMap["label5_1"]);
				
				//创建按下的按钮
				lable_down[0]=new BasicObject(obj,7,2,1050,290,texMap["label1_2"]);
				lable_down[1]=new BasicObject(obj,7,2,1050,440,texMap["label0_2"]);
				lable_down[2]=new BasicObject(obj,6.4,1.4,160,236,texMap["label2_2"]);
				lable_down[3]=new BasicObject(obj,6.4,1.4,160,316,texMap["label3_2"]);
				lable_down[4]=new BasicObject(obj,6.4,1.4,160,396,texMap["label4_2"]);
				lable_down[5]=new BasicObject(obj,6.4,1.4,160,476,texMap["label5_2"]);
				//创建标题栏
				lable[6]=new BasicObject(obj,12.8,9,160,326,texMap["rightView"]);
				lable[7]=new BasicObject(obj,12.8,9,1040,326,texMap["leftView"]);
				
			break;
		}
	}
	else
	{
		setTimeout(function(){createObject(objDataIn,object,id);},10); //休眠10毫秒
	}
}

function processObjectLoadObj(req,object,id)
{//加载模型文件的过程方法
	if (req.readyState == 4) 
	{//数据解析完毕
		var objStr = req.responseText;//获取网页信息			
		this.dataTemp=fromObjStrToObjectData(objStr);//获取模型相关数据
		createObject(dataTemp,object,id);//调用创建物体方法	
	}
} 