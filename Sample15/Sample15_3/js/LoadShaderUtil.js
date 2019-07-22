function shaderObject(typeIn,textIn)//声明shaderObject类
{
   this.type=typeIn;//初始化type成员变量
   this.text=textIn;//初始化text成员变量
}

function processLoadShader(req,index)//处理着色器脚本内容的回调函数
{
	if (req.readyState == 4) //若状态为4
	{
		var shaderStr = req.responseText;//获取响应文本	
			var shaderStrA=shaderStr.split("<#BREAK_BN#>");	//用分隔符<#BREAK_BN#>切分
			var vertexShader=new shaderObject("vertex",shaderStrA[0]);//顶点着色器脚本内容
			var fragmentShader=new shaderObject("fragment",shaderStrA[1]);//片元着色器脚本内容					
			shaderProgArray[index]=loadShaderSerial(gl,vertexShader, fragmentShader);//加载着色器
	}
}

//加载着色器的方法
//第一个参数为着色器脚本路径，第二个参数为着色器管理用编号
function loadShaderFile(url,index)//从服务器加载着色器脚本的函数
{
	var req = new XMLHttpRequest();//创建XMLHttpRequest对象
	req.onreadystatechange = function () //设置响应回调函数
	{ processLoadShader(req,index) };//调用processLoadShader处理响应
	req.open("GET", url, true);//用GET方式打开指定URL
	req.responseType = "text";//设置响应类型
	req.send(null);//发送HTTP请求
}

