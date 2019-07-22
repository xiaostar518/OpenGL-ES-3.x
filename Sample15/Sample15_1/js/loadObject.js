function loadObjFile(url,id)//从服务器获取Obj文本内容的函数
{
	var req = new XMLHttpRequest();//异步请求对象
	//设置响应回调函数，调用processObjectLoadObj处理响应
	req.onreadystatechange = function () { processObjectLoadObj(req,id) };
	req.open("GET", url, true);//用GET方式打开指定URL
	req.responseType = "text";//设置响应类型
	req.send(null);//发送HTTP请求
}
function createObject(objDataIn,id)//创建物体绘制对象的方法
{
	if(shaderProgArray[0])//如果着色器已加载完毕
	{
		ooTri=new ObjObject(gl,objDataIn.vertices,shaderProgArray[0]);//创建茶壶绘制对象
	}
	else
	{
		setTimeout(function(){createObject(objDataIn,id);},10); //休息10ms后再执行
	}
}

function processObjectLoadObj(req,id)//处理obj文本内容的回调函数
{
	if (req.readyState == 4) //若状态为4
	{
		var objStr = req.responseText;//获取响应文本			
		this.dataTemp=fromObjStrToObjectData(objStr);//将obj文本解析为数据对象
		createObject(dataTemp,id);//创建绘制用物体对象 	
	}
} 