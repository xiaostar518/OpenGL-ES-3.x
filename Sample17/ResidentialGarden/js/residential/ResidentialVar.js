//==========矩阵start=====//
var ms2D=new MatrixState();//2D对象的矩阵
ms2D.setInitStack();//初始化变换矩阵
ms.setInitStack();//初始化3D变换矩阵
//==========矩阵end=====//

//==========要绘制的3D物体模型=====start=====//
var map;//地图模型对象
var buildings=new Array();//楼房模型对象
var trees=new Array();//树的对象
var flower;//花的对象
var drawHouse;//绘制楼房的对象
var loadland;//绘制草坪
var skybox;//天空盒
var rectangle;//纹理矩形顶点数据
var fence;//栅栏模型对象
var pool;//喷泉模型对象
var grand;//草坪模型对象
var button;//按钮绘制对象
//==========要绘制的3D物体模型=====end=======//

//============标志位=======start==============//
var isGo=false;//是否播放漫游动画
var isChange=false;//改变方向
var isReturn=false;//是否返回
//============标志位=======end==============//

//============Button=======start==============//
var goOrStopX=-1.85;//暂停、播放图标在2D中的x坐标
var goOrStopY=-0.8;//暂停、播放图标在2D中的y坐标
var goOrStop_HalfSize=0.25/2.0;//暂停、播放图标的半宽或者半长
var goOrStop_minX=goOrStopX-goOrStop_HalfSize;//图标x的最左边值
var goOrStop_maxX=goOrStopX+goOrStop_HalfSize;//图标x的最右边值
var goOrStop_minY=goOrStopY-goOrStop_HalfSize;//图标y的最下边值
var goOrStop_maxY=goOrStopY+goOrStop_HalfSize;//图标y的最上边值

var directionX=-1.85;//摇杆图标在2D中的x坐标
var directionY=-0.8;//摇杆图标在2D中的y坐标
//前进键
var up_minX=-1.86;
var up_maxX=-1.78;
var up_minY=-0.75;
var up_maxY=-0.72;
//后退键
var down_minX=-1.86;
var down_maxX=-1.78;
var down_minY=-0.93;
var down_maxY=-0.89;
//左移键
var left_minX=-1.93;
var left_maxX=-1.89;
var left_minY=-0.857;
var left_maxY=-0.79;
//右移键
var right_minX=-1.756;
var right_maxX=-1.73;
var right_minY=-0.857;
var right_maxY=0.79;

var direction=-1;//摇杆方向--0-up;1-down;2-left;3-right
var vt=0;//移动速度
//返回按钮
var returnX=1.7;//返回图标在2D中的x坐标
var returnY=-0.84;//返回图标在2D中的y坐标
var return_minX=returnX-0.25/2;//图标x的最左边值
var return_maxX=returnX+0.25/2;//图标x的最右边值
var return_minY=returnY-0.25/2;//图标y的最下边值
var return_maxY=returnY+0.25/2;//图标y的最上边值

//横幅图的相关数据
var HF_X=0;
var HF_Y=0.77;
//============Button=======end==============//

//============屏幕数据=======start==============//
var SCREEN_WIDTH_STANDARD=1200;//屏幕标准宽度
var SCREEN_HEIGHT_STANDARD=600;//屏幕标准高度
var RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//屏幕标准比例--即透视投影的比例
//============屏幕数据=======end==============//
var Vtop=1;
var Vnear=1;//3D下
var Vfar=1000000;
var skyboxSize=500000;
var V2Dnear=1;//2D下

//===========移动摄像机需要的数据=========start======//
var v=0.5;//移动速度
var bs=0.5;//缩放比例
var points=[
	0,196*bs,0,-v,
	0,77*bs,-v,0,
	-78.4*bs,77*bs,0,-v,
	-78.4*bs,-77*bs,v,0,
	0,-77*bs,0,-v,
	0,-196*bs,0,v,
	0,-77*bs,v,0,
	78.4*bs,-77*bs,0,v,
	78.4*bs,77*bs,-v,0,
	0,77*bs,0,v,
	0,196*bs,0,-v];//路线数据
var vx=points[2];//x轴上的速度
var vz=points[3];//z轴上的速度
var currentAngle=0;//目标旋转角度

var DISTANCE_CAMERA_YACHT=2;//摄像机位置距离观察目标点的平面距离
var CAMERA_INI_Y=1.67;//摄像机y坐标

var index=1;//路线数据的索引值
var px=points[0];//目标点的x坐标
var pz=points[1];//目标点的z坐标

var mapminX=-196*bs;
var mapmaxX=196*bs;
var mapminZ=-196*bs;
var mapmaxZ=196*bs;
//===========移动摄像机需要的数据===========end=======//

var uriIndex="0";//从上一页面传过来的参数值
//=================楼房数据  start=================
var building2=[19.5,-30,52.5,-30,81.5,-30];//楼房2的位置数据--w19,L20,h30(x,z)
var building8=[18.5,-30,45.5,-30,72.5,-30];//楼房8的位置数据--w15,L20,h46(x,z)
var building4=[23.6,-21.6,36.4,-60.4];//楼房4的位置数据--w31.2,L31.2,h92(x,z)
var building5=[30.2,-21,29.8,-59];//楼房5的位置数据--w44.4,L30,h57.6(x,z)

var building01=[22,-30,50,-30,78,-30];//楼房01的位置数据--w16,L27,h20(x,z)
var building02=[21,-30,51,-30,81,-30];//楼房02的位置数据--w18,L23,h11(x,z)
//=================楼房数据  start=================
var land=[-95,42.3,95,42.3,-95,-36,95,-36,-95,-94.3,95,-94.3,-36,-36];//绘制草坪需移动的一定位置
var landSize=4;//草坪尺寸大小
var MapHalfSize=100;//草坪一半的大小
