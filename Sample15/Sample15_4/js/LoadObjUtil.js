function ObjectData//声明数据类ObjectData
(
  vertexCountIn,
  verticesIn,
  texcoordsIn,
  normalsIn
)
{  	 
	this.vertexCount=vertexCountIn;//初始化顶点数量成员
	this.vertices=verticesIn;//初始化顶点坐标数据成员
	this.texcoords=texcoordsIn;//初始化顶点纹理坐标数据成员
	this.normals=normalsIn;  	//初始化顶点法向量成员  
}	

function fromObjStrToObjectData(objStr)//解析obj文本内容的函数
{
	//原始顶点坐标列表--直接从obj文件中加载
	var alv=new Array();
	//结果顶点坐标列表--按面组织好
	var alvResult=[];  
	//原始顶点纹理坐标列表--直接从obj文件中加载
	var alt=new Array();
	//结果顶点纹理坐标列表--按面组织好
	var altResult=[];  
	//原始法向量列表--直接从obj文件中加载
	var aln=new Array();    
	//结果法向量列表--按面组织好
	var alnResult=[];     	
	
	var lines = objStr.split("\n");//将obj中的数据以换行符为分隔符分开
	
	for (var lineIndex in lines) //遍历每一行文本
	{
		var line = lines[lineIndex].replace(/[ \t]+/g, " ").replace(/\s\s*$/, "");//去掉空白符
		if (line[0] == "#")//如果是以#开头行
		{
			continue;//舍弃此行
		}

		var array = line.split(" ");//将一行数据以空格符为分隔符分开
		if (array[0] == "v") //判断是否为顶点坐标信息行
		{
			alv.push(parseFloat(array[1]));//获取顶点X坐标并存入原始顶点坐标列表
			alv.push(parseFloat(array[2]));//获取顶点Y坐标并存入原始顶点坐标列表
			alv.push(parseFloat(array[3]));//获取顶点Z坐标并存入原始顶点坐标列表
		}
		else if (array[0] == "vt") //判断是否为顶点纹理坐标信息行
		{
			alt.push(parseFloat(array[1]));//获取顶点纹理S坐标并存入原始顶点纹理坐标列表
			alt.push(1.0-parseFloat(array[2]));//获取顶点纹理T坐标并存入原始顶点纹理坐标列表
		}
		else if (array[0] == "vn") //判断是否为法向量信息行
		{
			aln.push(parseFloat(array[1]));//获取法向量X分量并存入原始法向量列表
			aln.push(parseFloat(array[2]));//获取法向量y分量并存入原始法向量列表
			aln.push(parseFloat(array[3]));//获取法向量z分量并存入原始法向量列表
		}
		else if (array[0] == "f") //判断是否为组装三角形面的信息行
		{
			if (array.length != 4)//若数据数量不对
			{
				alert("array.length != 4");//出错提示信息
				continue;//舍弃此行
			}
			for (var i = 1; i < 4; ++i) //遍历此行的三个顶点元素
			{
				var tempArray=array[i].split("/");//将每个顶点元素用“/”切分
				var vIndex=tempArray[0]-1;//获取顶点索引值
				var tIndex=tempArray[1]-1;//获取顶点纹理索引值
				var nIndex=tempArray[2]-1;//获取法向量索引值
		  
				alvResult.push(alv[vIndex*3+0]);//将对应索引顶点的X坐标存入结果顶点坐标列表
				alvResult.push(alv[vIndex*3+1]);//将对应索引顶点的y坐标存入结果顶点坐标列表
				alvResult.push(alv[vIndex*3+2]);//将对应索引顶点的z坐标存入结果顶点坐标列表
		  
				altResult.push(alt[tIndex*2+0]);//将对应索引顶点纹理的s坐标存入结果顶点纹理坐标列表
				altResult.push(alt[tIndex*2+1]);//将对应索引顶点纹理的T坐标存入结果顶点纹理坐标列表
	
				alnResult.push(aln[nIndex*3+0]);//将对应索引法向量的X分量存入结果法向量列表
				alnResult.push(aln[nIndex*3+1]);//将对应索引法向量的y分量存入结果法向量列表
				alnResult.push(aln[nIndex*3+2]);//将对应索引法向量的z分量存入结果法向量列表
			}
		}
	}
	return new ObjectData(alvResult.length/3,alvResult,altResult,alnResult);//返回数据类ObjectData的对象
}