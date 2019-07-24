function loadRoomTypeObjFile(url,id)
{
//alert("====lllllll===="+url);
	var req = new XMLHttpRequest();
	req.onreadystatechange = function () { processRoomTypeObjectLoadObj(req,id) };
	req.open("GET", url, true);
	req.responseType = "text";
	req.send(null);
}
function createRoomTypeObject(objDataIn,id)
{
	if(shaderProgArray[id])
	{
		//创建绘制用的物体
		housemodel=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
	}
	else
	{
		setTimeout(function(){createRoomTypeObject(objDataIn,object,id);},10); //休眠10毫秒
	}
}

function processRoomTypeObjectLoadObj(req,id)
{
	if (req.readyState == 4) 
	{
		var objStr = req.responseText;			
		this.dataTemp=fromObjStrToObjectData(objStr);
		createRoomTypeObject(dataTemp,id); 	
	}
} 