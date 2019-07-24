#ifndef RenderingEngine2__h
#define RenderingEngine2__h

#import <OpenGLES/ES3/gl.h>
#import <OpenGLES/ES3/glext.h>
#include <cmath>
#include <iostream>

#include "Moon.h"
#include "Earth.h"
#include "Celestial.h"

class RenderingEngine2 : public IRenderingEngine {
public:
    //声明该类的方法
    RenderingEngine2();
    void Initialize(int width, int height);
    void Render() const;
    void OnFingerUp(float locationx,float locationy);
    void OnFingerDown(float locationx,float locationy);
    void OnFingerMove(float previousx,float previousy,float currentx,float current);
private:
    GLuint m_framebuffer;//创建一个帧缓冲区对象
    GLuint m_renderbuffer;//创建一个渲染缓冲区对象
    GLuint m_depthRenderbuffer;
    GLuint texId;
    
    GLuint textureIdEarth;//系统分配的地球纹理id
    GLuint textureIdEarthNight;//系统分配的地球夜晚纹理id
    GLuint textureIdMoon;//系统分配的月球纹理id
    
   float yAngle=0;//太阳灯光绕y轴旋转的角度
    float xAngle=0;//摄像机绕X轴旋转的角度
    
    static float eAngle;//地球自转角度
    static float cAngle;//天球自转的角度
    
    Earth *e;//地球对象的指针
    Moon *m;//月球对象的指针
    Celestial *cs;//天空球对象的指针
    Celestial *cl;
    const float PI=3.1415926f/180.0f;
};
float RenderingEngine2::eAngle = 0;
float RenderingEngine2::cAngle = 0;
struct IRenderingEngine* CreateRenderer2()
{
    return new RenderingEngine2();
}


#endif


