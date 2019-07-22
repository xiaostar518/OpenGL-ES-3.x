#ifndef __OpenGL3_0Demo__Moon__
#define __OpenGL3_0Demo__Moon__

#include <iostream>
#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>

class Moon {
	GLuint mProgram;//自定义渲染管线程序id
    GLuint muMVPMatrixHandle;//总变换矩阵引用
    GLuint muMMatrixHandle;//位置、旋转变换矩阵
    GLuint maCameraHandle; //摄像机位置属性引用
    GLuint maPositionHandle; //顶点位置属性引用
    GLuint maNormalHandle; //顶点法向量属性引用
    GLuint maTexCoorHandle; //顶点纹理坐标属性引用
    GLuint maSunLightLocationHandle;//光源位置属性引用
    GLuint mTexHandle;
    const GLvoid* mVertexBuffer;//顶点坐标数据缓冲
    const GLvoid* mTexCoorBuffer;//顶点纹理坐标数据缓冲
    
    int vCount;
    
public:
    Moon(float r);
    void initVertexData(float r);
    void initShader();
    void drawSelf(int texId);
    float toRadians(float angle);
    void generateTexCoor(int bw,int bh,float* tex);
};


#endif
