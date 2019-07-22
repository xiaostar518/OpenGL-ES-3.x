#ifndef __OpenGL3_0Demo__Celestial__
#define __OpenGL3_0Demo__Celestial__

#include <iostream>
#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>

class Celestial {
    const float UNIT_SIZE=10.0f;//天球半径
	const GLvoid* mVertexBuffer;//顶点坐标数据缓冲
    const int vCount=2000;//星星数量
    float yAngle;//天球绕Y轴旋转的角度
    float scale;//星星尺寸
    GLuint mProgram;//自定义渲染管线程序id
    GLuint muMVPMatrixHandle;//总变换矩阵引用
    GLuint maPositionHandle; //顶点位置属性引用
    GLuint uPointSizeHandle;//顶点尺寸参数引用
public:
    Celestial(float scale,float yAngle);
    void initVertexData();
    void initShader();
    void drawSelf();
};


#endif
