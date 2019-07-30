#version 300 es
precision mediump float;
in vec2 mcLongLat;//接收从顶点着色器过来的参数
out vec4 fFragColor;//输出的片元颜色
void main()                         
{                       
   vec3 bColor=vec3(0.678,0.231,0.129);//砖块的颜色
   vec3 mColor=vec3(0.763,0.657,0.614);//水泥的颜色
   vec3 color;//片元的最终颜色
   
   //计算当前位于奇数还是偶数行
   int row=int(mod((mcLongLat.y+90.0)/12.0,2.0));
   //计算当前片元是否在此行区域1中的辅助变量
   float ny=mod(mcLongLat.y+90.0,12.0);
   //每行的砖块偏移值，奇数行偏移半个砖块
   float oeoffset=0.0;
   //当前片元是否在此行区域3中的辅助变量
   float nx;
   
   if(ny>10.0)
   {//位于此行的区域1中
     color=mColor;//采用水泥色着色
   }
   else
   {//不位于此行的区域1中
     if(row==1)
     {//若为奇数行则偏移半个砖块
        oeoffset=11.0;
     }
     //计算当前片元是否在此行区域3中的辅助变量
     nx=mod(mcLongLat.x+oeoffset,22.0);
     if(nx>20.0)
     {//不位于此行的区域3中
        color=mColor;
     }
     else
     {//位于此行的区域3中
        color=bColor;//采用砖块色着色
     }
   } 
   //将片元的最终颜色传递进渲染管线
   fFragColor=vec4(color,0);
}     