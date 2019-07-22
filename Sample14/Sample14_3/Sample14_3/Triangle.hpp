#ifndef __OpenGL3_0Demo__Triangle__
#define __OpenGL3_0Demo__Triangle__

#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>

class Triangle {
    GLuint mProgram;
    GLuint muMVPMatrixHandle;//总变换矩阵引用
    GLuint maPositionHandle; //顶点位置属性引用
    GLuint maColorHandle; //顶点颜色属性引用
    const GLvoid* pCoords;//顶点坐标数据缓冲
    const GLvoid* pColors;//顶点着色数据缓冲
    int vCount;
    int aa;
public:
    Triangle();//构造函数
    void initVertexData();//初始化顶点数据的函数
    void initShader();//初始化着色器的函数
    void drawSelf();//绘制函数
};

const float vertices[]=
{//三角形三个顶点坐标
    -0.8    ,0      ,0,
    0       ,0.8    ,0,
    0.8     ,0      ,0
};


const float colors[] = 
{//三角形三个顶点的颜色值
    1,0,0,0,
    0,0,1,0,
    0,1,0,0
};

#endif
