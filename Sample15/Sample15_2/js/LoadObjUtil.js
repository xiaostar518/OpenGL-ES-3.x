 	  function ObjectData
  	(
  	  vertexCountIn,verticesIn,normalsIn
  	)
  	{  	 
  	  this.vertexCount=vertexCountIn;
  	  this.vertices=verticesIn;
  	  this.normals=normalsIn;  	  
  	}	
  	
    function fromObjStrToObjectData(objStr)
		{//解析obj文本内容的函数
		  //原始顶点坐标列表--直接从obj文件中加载
    	var alv=new Array();
    	//结果顶点坐标列表--按面组织好
    	var alvResult=[];
    	//原始法向量列表
    	var aln=new Array();    
    	//法向结果量列表
    	var alnResult=[];     	
    	
    	var lines = objStr.split("\n");//将obj中的数据以换行符为分隔符分开
    	
    	for (var lineIndex in lines) 
    	{//遍历每一行文本
        var line = lines[lineIndex].replace(/[ \t]+/g, " ").replace(/\s\s*$/, "");//去掉空白符
        if (line[0] == "#")
        {//如果是以#开头行
           continue;//舍弃此行
        }
        var array = line.split(" ");//将一行数据以空格符为分隔符分开
        if (array[0] == "v") //判断是否为顶点坐标信息行
        {//获取顶点坐标并存入原始顶点坐标列表
            alv.push(parseFloat(array[1]));
            alv.push(parseFloat(array[2]));
            alv.push(parseFloat(array[3]));
        }
        else if (array[0] == "vn") //判断是否为顶点法向量信息行
        {//获取顶点法向量并存入原始顶点法向量列表
            aln.push(parseFloat(array[1]));
            aln.push(parseFloat(array[2]));
            aln.push(parseFloat(array[3]));
        }
        else if (array[0] == "f") //判断是否为组装三角形面的信息行
        {
           if (array.length != 4)
           {//若数据数量不对
                alert("array.length != 4");//出错提示信息
                continue;//舍弃此行
           }
           for (var i = 1; i < 4; ++i) //遍历此行的三个顶点元素
           {
              var tempArray=array[i].split("/");//将每个顶点元素用“/”切分
              var vIndex=tempArray[0]-1;//获取顶点坐标索引值
              var nIndex=tempArray[2]-1;//获取顶点法向量索引值
              
			  //将对应索引顶点的坐标存入结果顶点坐标列表
              alvResult.push(alv[vIndex*3+0]);
              alvResult.push(alv[vIndex*3+1]);
              alvResult.push(alv[vIndex*3+2]);
        
			  //将对应索引法向量的坐标存入结果法向量列表
              alnResult.push(aln[nIndex*3+0]);
              alnResult.push(aln[nIndex*3+1]);
              alnResult.push(aln[nIndex*3+2]);
           }
        }
      }
      return new ObjectData(alvResult.length/3,alvResult,alnResult);	//数据类ObjectData的对象
	}