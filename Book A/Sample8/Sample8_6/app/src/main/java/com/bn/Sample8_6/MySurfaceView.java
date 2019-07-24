package com.bn.Sample8_6;

import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.zqt.util.ZQTEdgeUtil;

import android.content.Context;

class MySurfaceView extends GLSurfaceView {

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例
	private float mPreviousY;// 上次的触控位置Y坐标
	private float mPreviousX;// 上次的触控位置X坐标

	private SceneRenderer mRenderer;// 场景渲染器
	boolean lightFlag = true; // 光照旋转的标志位

	float yAngle = 0;// 绕y轴旋转的角度
	float xAngle = 0;// 绕x轴旋转的角度
	float zAngle = 0;// 绕z轴旋转的角度
	// 工具类UtilTools对象的引用
	UtilTools utilTools;
	// 球对象引用
	Ball ball;
	// 棍对象引用
	Stick stick;
	// 球和棍位置信息对象
	ResultData rusultData;

	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(3); // 设置使用OPENGL ES3.0
		mRenderer = new SceneRenderer(); // 创建场景渲染器
		setRenderer(mRenderer); // 设置渲染器
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 设置渲染模式为主动渲染
	}

	// 触摸事件回调方法
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreviousY;// 计算触控笔Y位移
			float dx = x - mPreviousX;// 计算触控笔X位移
			yAngle += dx * TOUCH_SCALE_FACTOR;// 设置绕y轴旋转角度
			zAngle += dy * TOUCH_SCALE_FACTOR;// 设置绕z轴旋转角度
		}
		mPreviousY = y;// 记录触控笔位置
		mPreviousX = x;// 记录触控笔位置
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {
		public void onDrawFrame(GL10 gl) {
			// 清除深度缓冲与颜色缓冲
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT
					| GLES30.GL_COLOR_BUFFER_BIT);

			MatrixState.pushMatrix();
			MatrixState.translate(0, 0, -10f);
			MatrixState.rotate(yAngle, 0, 1, 0);
			MatrixState.rotate(zAngle, 0, 0, 1);
			// 根据顶点的个数绘制球体 
			for (int i = 0; i < rusultData.CAtomicPosition.length; i++) {
				MatrixState.pushMatrix();
				MatrixState.translate(rusultData.CAtomicPosition[i][0],
						rusultData.CAtomicPosition[i][1], rusultData.CAtomicPosition[i][2]);
				ball.drawSelf();
				MatrixState.popMatrix();
			}

			for (float[] ab : rusultData.ChemicalBondPoints) {
				float[] result = ZQTEdgeUtil.calTranslateRotateScale(ab);

				MatrixState.pushMatrix();
				MatrixState.translate(result[0], result[1], result[2]);
				MatrixState.rotate(result[3], result[4], result[5],result[6]);
				MatrixState.scale(result[7], result[8], result[9]);
				stick.drawSelf();
				MatrixState.popMatrix();
			}
			MatrixState.popMatrix();

		}
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// 设置视窗大小及位置
			GLES30.glViewport(0, 0, width, height);
			// 计算GLSurfaceView的宽高比
			float ratio = (float) width / height;
			// 调用此方法计算产生透视投影矩阵
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 4f, 100);
			// 调用此方法产生摄像机9参数位置矩阵
			MatrixState.setCamera(0, 0, 8.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
			// 初始化光源
			MatrixState.setLightLocation(10, 0, -10);
			// 启动一个线程定时修改灯光的位置
			new Thread() {
				public void run() {
					float redAngle = 0;
					while (lightFlag) {
						// 根据角度计算灯光的位置
						redAngle = (redAngle + 5) % 360;
						float rx = (float) (15 * Math.sin(Math
								.toRadians(redAngle)));
						float rz = (float) (15 * Math.cos(Math
								.toRadians(redAngle)));
						MatrixState.setLightLocation(rx, 0, rz);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// 设置屏幕背景色RGBA
			GLES30.glClearColor(1f, 1f, 1f, 1f);
			// 启用深度测试
			GLES30.glEnable(GLES30.GL_DEPTH_TEST);
			// 设置为打开背面剪裁
			GLES30.glEnable(GLES30.GL_CULL_FACE);
			utilTools = new UtilTools();
			// 初始化资源对象！
			rusultData = utilTools.initVertexData(Constant.TRIANGLE_SCALE,
					Constant.TRIANGLE_AHALF, Constant.SPLIT_COUNT);
			// 初始化变换矩阵
			MatrixState.setInitStack();
            float[] colorValue = {1,0,0,1};	//创建颜色数组
			ball = new Ball(MySurfaceView.this, Constant.BALL_R, colorValue);// 创建球对象
			colorValue = new float[]{1,1,0,1};
			// 棍对象的引用
			stick = new Stick(MySurfaceView.this, Constant.LENGTH, Constant.R,
					Constant.ANGLE_SPAN, colorValue);// 创建圆管对象
		}
	}
}
