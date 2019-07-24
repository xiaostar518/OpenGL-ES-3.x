function loadHouseObjFile(url,object,id)
{
	var req = new XMLHttpRequest();
	req.onreadystatechange = function () { processHouseLoadObj(req,object,id) };
	req.open("GET", url, true);
	req.responseType = "text";
	req.send(null);
}
function createHouse(objDataIn,object,id)
{
	if(shaderProgArray[id])
	{
		//创建绘制用的物体
		switch(object)
		{
			case 0://楼房1====2
				buildings[0]=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]); 
			break;
		  
			case 1://楼房2=====8
				buildings[1]=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]);
			break;
		  
			case 2://楼房3====4
				buildings[2]=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]); 
			break;
		  
			case 3://楼房4====5
				buildings[3]=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]); 
			break;
		  
			case 4://楼房5====01
				buildings[4]=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]); 
				//alert("====buildings[4]=====");
			break;
		  
			case 5://楼房6====02
				buildings[5]=new ObjObject(gl,objDataIn.vertices,objDataIn.normals,objDataIn.texcoords,shaderProgArray[id]); 
			break;
		  
		}
	}
	else
	{
		setTimeout(function(){createHouse(objDataIn,object,id);},10); //休眠10毫秒
	}
}

function processHouseLoadObj(req,object,id)
{
	if (req.readyState == 4) 
	{
		var objStr = req.responseText;			
		this.dataTemp=fromObjStrToObjectData(objStr);
		createHouse(dataTemp,object,id); 	
	}
} 