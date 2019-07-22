//加载的用于绘制的3D物体
	    function ObjObject
	    (
	       gl,						  //GL上下文
	       vertexDataIn,    //顶点坐标数组
	       vertexNormalIn,   //顶点法向量数组
		   programIn				//着色器程序对象
	    )
	    {
	    	//接收顶点数据
	    	this.vertexData=vertexDataIn;   	  
	    	//得到顶点数量
	    	this.vcount=this.vertexData.length/3;
	    	//创建顶点数据缓冲
	    	this.vertexBuffer=gl.createBuffer();
	    	//绑定顶点坐标数据缓冲
			gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexBuffer);
			//将顶点数据送入缓冲
			gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexData),gl.STATIC_DRAW); 
           
			//接收顶点法向量数据
			this.vertexNormal=vertexNormalIn;  
			//创建顶点法向量数据缓冲
	    	this.vertexNormalBuffer=gl.createBuffer();
	    	//绑定顶点法向量数据缓冲
			gl.bindBuffer(gl.ARRAY_BUFFER,this.vertexNormalBuffer);
			//将顶点法向量数据送入缓冲
			gl.bufferData(gl.ARRAY_BUFFER,new Float32Array(this.vertexNormal),gl.STATIC_DRAW); 
          
			//初始化着色器程序id
			this.program=programIn;
   
			this.drawSelf=function(ms)
			{		
				gl.useProgram(this.program);//指定使用某套着色器程序
				
				//获取总变换矩阵引用id
				var uMVPMatrixHandle=gl.getUniformLocation(this.program, "uMVPMatrix");   
				//将总变换矩阵送入渲染管线
				gl.uniformMatrix4fv(uMVPMatrixHandle,false,new Float32Array(ms.getFinalMatrix())); 

				//获取变换矩阵引用id
				var uMMatrixHandle=gl.getUniformLocation(this.program, "uMMatrix");
				//将变换矩阵送入渲染管线
				gl.uniformMatrix4fv(uMMatrixHandle,false,new Float32Array(ms.currMatrix)); 
				
				//将光源位置送入渲染管线
				var uLightLocationHandle=gl.getUniformLocation(this.program, "uLightLocation");
				gl.uniform3fv(uLightLocationHandle,new Float32Array([-20,25,15]));
              
				//获取光源位置引用id
				var uCameraHandle=gl.getUniformLocation(this.program, "uCamera");
				//将摄像机位置送入渲染管线
				gl.uniform3fv(uCameraHandle,new Float32Array([0,0,15]));
				
				//启用顶点坐标数据数组
				gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aPosition"));        
				//绑定顶点坐标数据缓冲
				gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexBuffer);
				//给管线指定顶点坐标数据
				gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aPosition"), 3, gl.FLOAT, false, 0, 0);   
	        
				//启用法向量数据数组
				gl.enableVertexAttribArray(gl.getAttribLocation(this.program, "aNormal")); 
				//绑定顶点法向量数据缓冲
				gl.bindBuffer(gl.ARRAY_BUFFER, this.vertexNormalBuffer);
				//给管线指定顶点法向量数据
				gl.vertexAttribPointer(gl.getAttribLocation(this.program, "aNormal"), 3, gl.FLOAT, false, 0, 0);
              
				//用顶点法绘制物体
				gl.drawArrays(gl.TRIANGLES, 0, this.vcount); 
	        }      
	    }