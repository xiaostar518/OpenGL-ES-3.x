function loadHouseObjFile(url,id)
{
	//alert("======"+url);
	var req = new XMLHttpRequest();
	req.onreadystatechange = function () { processHouseObjectLoadObj(req,id) };
	req.open("GET", url, true);
	req.responseType = "text";
	req.send(null);
}
function createHouseObject(objDataIn,id)
{
	if(shaderProgArray[id])
	{
		//创建绘制用的物体
		building=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
		//if(building)
		//alert("=====building===");
	}
	else
	{
		setTimeout(function(){createHouseObject(objDataIn,object,id);},10); //休眠10毫秒
	}
}

function processHouseObjectLoadObj(req,id)
{
	if (req.readyState == 4) 
	{
		var objStr = req.responseText;			
		this.dataTemp=fromObjStrToObjectData(objStr);
		createHouseObject(dataTemp,id); 	
	}
} 