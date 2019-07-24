function shaderObject(typeIn,textIn)
{
	this.type=typeIn;
	this.text=textIn;
}

function processLoadShader(req,index)
{
	if (req.readyState == 4) 
	{
		var shaderStr = req.responseText;	
		var shaderStrA=shaderStr.split("<#BREAK_BN#>");
		var vertexShader=new shaderObject("vertex",shaderStrA[0]);
		var fragmentShader=new shaderObject("fragment",shaderStrA[1]);						
		shaderProgArray[index]=loadShaderSerial(gl,vertexShader, fragmentShader);
	}
}

//加载着色器的方法
//第一个参数为着色器脚本路径，第二个参数为着色器管理用编号
function loadShaderFile(url,index)
{
	var req = new XMLHttpRequest();
	req.onreadystatechange = function () { processLoadShader(req,index) };
	req.open("GET", url, true);
	req.responseType = "text";
	req.send(null);
}

