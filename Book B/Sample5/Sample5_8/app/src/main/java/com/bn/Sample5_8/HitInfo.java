package com.bn.Sample5_8;//声明包
//一个交点的信息
public class HitInfo{

	double hitTime;//相交时间
	HitObject hitObject;//相交的物体
	boolean isEntering;//光线是进入还是射出
	int surface;//相交于哪个表面
	Point3 hitPoint;//交点的坐标，变换后的
	Vector3 hitNormal;//交点处的法向量，变换前的
	
	public HitInfo(){
		hitPoint = new Point3();//创建碰撞点对象
		hitNormal = new Vector3();//创建法向量对象
	}
	/* 
	 * 此方法可能会不对，复制问题可能出现
	 * 如果有解决不了的问题可以回来看
	 */
	public void set(HitInfo hit){//根据传入的HitInfo对象，复制各项信息进入自身的成员变量
		this.hitTime=hit.hitTime;//设置相交时间值
		this.hitObject=hit.hitObject;//设置相交的物体
		this.isEntering=hit.isEntering;//设置光线是进入还是射出
		this.surface=hit.surface;//设置相交于哪个表面
		this.hitPoint.set(hit.hitPoint);//设置交点的坐标，变换后的
		this.hitNormal.set(hit.hitNormal);//设置交点处的法向量，变换前的
	}
	@Override
	public String toString() {
		return "hitTime"+hitTime+",hitPoint"+hitPoint;
	}
}
