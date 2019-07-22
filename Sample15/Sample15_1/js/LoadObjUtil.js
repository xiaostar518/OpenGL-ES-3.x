 	function ObjectData(verticesIn)//声明数据类ObjectData
  	{  	 
  	  this.vertices=verticesIn;	//初始化顶点坐标数据成员  	  
  	}	
  	
    function fromObjStrToObjectData(objStr)//解析obj文本内容的函数
	{
		//原始顶点坐标列表--直接从obj文件中加载
    	var alv=new Array();
    	//结果顶点坐标列表--按面组织好
    	var alvResult=[];       	
    	
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
              
              alvResult.push(alv[vIndex*3+0]);//将对应索引顶点的X坐标存入结果顶点坐标列表
              alvResult.push(alv[vIndex*3+1]);//将对应索引顶点的Y坐标存入结果顶点坐标列表
              alvResult.push(alv[vIndex*3+2]);//将对应索引顶点的Z坐标存入结果顶点坐标列表
           }
        }
      }
      return new ObjectData(alvResult);//数据类ObjectData的对象
	}