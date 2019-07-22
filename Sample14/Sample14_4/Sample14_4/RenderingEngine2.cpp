#include "IRenderingEngine.hpp"
#include "RenderingEngine2.hpp"
#include "MatrixState.hpp"

#include "LoadResourceUtil.h"

RenderingEngine2::RenderingEngine2()
{
}

void RenderingEngine2::Initialize(int width, int height)
{//初始化函数
	glGenRenderbuffers(1, &m_renderbuffer);					//创建一个渲染缓冲
	glBindRenderbuffer(GL_RENDERBUFFER, m_renderbuffer);	//绑定上述渲染缓冲
	glGenRenderbuffers(1, &m_depthRenderbuffer);			//创建一个作为深度缓冲的渲染缓冲
	//绑定上述作为深度缓冲的渲染缓冲
	glBindRenderbuffer(GL_RENDERBUFFER, m_depthRenderbuffer);
	glRenderbufferStorage(GL_RENDERBUFFER,GL_DEPTH_COMPONENT16,width, height);//设置缓冲类型及尺寸
	glGenFramebuffers(1, &m_framebuffer);					//创建一个帧缓冲
	glBindFramebuffer(GL_FRAMEBUFFER, m_framebuffer);		//绑定上述帧缓冲
	//将第一个创建的渲染缓冲绑定到帧缓冲上，作为其颜色附件
	glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_RENDERBUFFER,m_renderbuffer);
	//将第二个创建的深度缓冲绑定到帧缓冲上，作为其深度附件
	glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER, m_depthRenderbuffer);
	glBindRenderbuffer(GL_RENDERBUFFER, m_renderbuffer);	//绑定作为颜色附件的渲染缓冲

    
    glViewport(0, 0, width, height);//设置视口
    glEnable(GL_CULL_FACE);//启用背面剪裁
    glEnable(GL_DEPTH_TEST);//启用深度测试
    float ratio = (float) width/height;//计算宽高比
    //设置投影矩阵
    MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 4, 100);
    //设置社摄像机
   MatrixState::setCamera(0, 0, 11.2f, 0, 0, 0,0,1, 0);
    //设置太阳灯光的初始位置
    MatrixState::setLightLocationSun(100,5,0);
    MatrixState::setInitStack();   //初始化矩阵
    textureIdEarth = LoadResourceUtil::initTexture("earth");  //加载地球白天纹理
    textureIdEarthNight = LoadResourceUtil::initTexture("earthn");//加载地球夜晚纹理
    textureIdMoon = LoadResourceUtil::initTexture("moon");//加载月球纹理

    e = new Earth(2.0);//新建地球对象
    m = new Moon(1.0);//新建月球对象
    cs = new Celestial(1,0);//新建天空球对象
   
}



void RenderingEngine2::Render() const
{
    glClearColor(0.0f, 0.0f, 0.1f, 1);//设置背景颜色
    //清除深度缓冲与颜色缓冲
    glClear(GL_DEPTH_BUFFER_BIT|GL_COLOR_BUFFER_BIT);
    
    MatrixState::pushMatrix();//保护现场
    MatrixState::rotate(eAngle, 0, 1, 0);//地球自转
    e->drawSelf(textureIdEarth, textureIdEarthNight);//绘制地球
    MatrixState::translate(2, 0, 0);
    MatrixState::rotate(eAngle, 0, 1, 0); //月球自转
    m->drawSelf(textureIdMoon);//绘制月球
    MatrixState::popMatrix();//恢复现场
    
    
    MatrixState::pushMatrix();//保护现场
    MatrixState::rotate(cAngle, 0, 1, 0);//天空球自转
    cs->drawSelf();//绘制天空球
    MatrixState::popMatrix(); //恢复现场
    
    eAngle = eAngle+0.2f;//地球、月球继续旋转一定的角度
    cAngle = cAngle+0.05f;//天空球继续移动一定角度
}
void RenderingEngine2::OnFingerDown(float locationx,float locationy){}
void RenderingEngine2::OnFingerUp(float locationx,float locationy){}
void RenderingEngine2::OnFingerMove(float previousx,float previousy,float currentx,float currenty)
{
	//触控横向位移太阳光源绕y轴旋转
	float dx=currentx-previousx;							//计算触控点X位移
	yAngle+=(float)dx*180.0f/320.0f;						//将X位移折算成角度
	float sunx=(float)std::cos((float)yAngle*PI)*100;				//计算光源位置X分量
	float sunz=-(float)(std::sin((float)yAngle*PI)*100);			//计算光源位置Z分量
	MatrixState::setLightLocationSun(sunx,0,sunz);				//设置光源位置
	//触控纵向位移摄像机绕x轴旋转 -90~+90
	float dy=currenty-previousy;							//计算触控点Y位移
	xAngle+=(float)dy*90.0f/320.0f;						//将Y位移折算成绕X轴旋转的角度
	if(xAngle>90) {									//若xAngle大于90，则设置为90
		xAngle=90;
	} else if(xAngle<-90){								//若小于-90，则设置为-90
		xAngle=-90;
	}
	float cy=(float) (11.2f*std::sin((float)xAngle*PI));				//计算摄像机的Y分量
	float cz=(float) (11.2f*std::cos((float)xAngle*PI));			//计算摄像机的Z分量
	float upy=(float) std::cos((float)xAngle*PI);					//计算UP向量的Y分量
	float upz=-(float) std::sin((float)xAngle*PI);					//计算UP向量的Z分量
	MatrixState::setCamera(0, cy, cz, 0, 0, 0, 0, upy,upz);		//设置摄像机参数
}

