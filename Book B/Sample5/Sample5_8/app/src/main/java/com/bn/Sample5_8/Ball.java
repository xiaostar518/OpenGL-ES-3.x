package com.bn.Sample5_8;//声明包
/*
 * 球心在原点，半径为1的标准球
 * 注意：其变换成椭球后法向量也是对的
 */
public class Ball extends HitObject {//球心在原点，半径为1的标准球
	public Ball(Camera cam, Color3f color){//初始化颜色与摄像机
		this.cam=cam;
		this.color=color;
	}
	   
	@Override
	public boolean hit(Ray r,Intersection inter) {//重写的计算光线与物体碰撞点的方法
		/*
		 * 求解光线S+ct与变换后物体的交点需要以下步骤：
		 * 1、求出逆变换光线S'+c't
		 * 2、求出逆变换光线与通用物体的碰撞时间t
		 * 3、把碰撞时间t代入等式S+ct得到实际的交点坐标
		 * 
		 * 
		 * 因此，genRay只是变换后的光线，
		 * 只用于求解碰撞时间t，
		 * 用t求交点时用变换前的光线r
		 */
		Ray genRay=new Ray();//变换后的光线		
		xfrmRay(genRay, getInvertMatrix(), r);//计算出变换后的光线

		double A,B,C;//计算交点的方程的3个系数
		A = Vector3.dot(genRay.dir,genRay.dir);	//求出第一个系数A
		B = Vector3.dot(genRay.start, genRay.dir);//求出第一个系数B
		C = Vector3.dot(genRay.start, genRay.start)-1.0f;//求出第一个系数C
		double discrim = B*B-A*C;                        //求判别式的值
		if(discrim<0.0){//若判别式值小于0则没有交点
			return false;
		}
		int num=0;//目前的交点个数
		double discRoot = (float) Math.sqrt(discrim);//求判别式的平方根
		double t1 = (-B-discRoot)/A;		//第一个交点的对应相交时间
		if(t1>0.00001){
			inter.hit[0].hitTime=t1;//记录交点的时间
			inter.hit[0].hitObject=this;//记录交点所属的物体
			inter.hit[0].isEntering=true;
			inter.hit[0].surface=0;			
			Point3 P = rayPos(r,t1);//交点坐标(使用变换前的光线)
			inter.hit[0].hitPoint.set(P);//变换后的交点位置		
			Point3 preP = xfrmPtoPreP(P);//通过变换后的点求变换前的点
			inter.hit[0].hitNormal.set(preP);//变换前的点就是变换前的法向量
			
			num=1;//交点数量
		}
		double t2 = (-B+discRoot)/A;//第二个交点的对应相交时间
		if(t2>0.00001){
			inter.hit[num].hitTime=t2;//记录交点的时间
			inter.hit[num].hitObject=this;//记录交点所属的物体
			inter.hit[num].isEntering=true;
			inter.hit[num].surface=0;			
			Point3 P = rayPos(r,t2);//交点坐标(使用变换前的光线)
			inter.hit[num].hitPoint.set(P);
			Point3 preP = xfrmPtoPreP(P);//通过变换后的点求变换前的点
			inter.hit[num].hitNormal.set(preP);//变换前的点就是变换前的法向量
			
			num++;//交点数量
		}
		inter.numHits=num;
		return (num>0);//若有一个或一个以上的有效交点则返回true
	}

	@Override
	public boolean hit(Ray r) {//服务于阴影探测器判断是否在阴影中的方法
		Ray genRay=new Ray();//变换后的光线		
		xfrmRay(genRay, getInvertMatrix(), r);//获取变换后的光线

		double A,B,C;//计算交点的方程的3个系数
		A = Vector3.dot(genRay.dir,genRay.dir);	//求出第一个系数A
		B = Vector3.dot(genRay.start, genRay.dir);//求出第一个系数B
		C = Vector3.dot(genRay.start, genRay.start)-1.0f;//求出第一个系数C
		double discrim = B*B-A*C;                        //求判别式值
		if(discrim<0.0){//若判别式小于0则没有交点
			return false;
		}
		double discRoot = (float) Math.sqrt(discrim);//求判别式的平方根
		double t1 = (-B-discRoot)/A;		//第一次相交的时间
		//只接受从0到1之间的碰撞，因为在光源另外一侧不会产生阴影
		if(t1<0 || t1>1){//若相交时间不在0～1内则不在阴影中
			return false;
		}
		return true;//否则在阴影中
	}
}
