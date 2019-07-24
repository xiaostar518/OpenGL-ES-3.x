//==================绘制对象start================//
var building;//楼房模型
var rectangle;//绘制2D物体的绘制对象
var button;//绘制按钮对象
var grand;//草坪绘制对象
var trees=new Array();//树的对象
var imageName;//模型图片名
var objName;//模型名
//==================绘制对象end================//

//==================树的绘制位置数据 start==========
var point0=[-16,14,15,14,-12,-12,16,-14];//绘制树位置的数据--高8
var point1=[-12,12,12,12,-13,-15,12,-16];//绘制树位置的数据---高10
var point2=[-16,9,16,15,-16,-10,10,-12];//绘制树位置的数据---高6
var bs=1;//树的大小倍数
//==================树的绘制位置数据 end==========

var ms2D=new MatrixState();//2D对象的变化矩阵
var uriIndex="1";//要绘制房子的索引值

//============屏幕数据=======start==============//
var SCREEN_WIDTH_STANDARD=1200;//屏幕标准宽度
var SCREEN_HEIGHT_STANDARD=600;//屏幕标准高度
var RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//屏幕标准比例--即透视投影的比例
//============屏幕数据=======end==============//

//==================按钮绘制位置等数据start================//
var HUXING_X=-1.5;
var HUXING_Y=0.014;
//一室一厅的相关数据
var ONEROOM_X=-1.56;
var ONEROOM_Y=0.24;
var ONEROOM_MINX=-1.82;
var ONEROOM_MAXX=-1.25;
var ONEROOM_MINY=0.14;
var ONEROOM_MAXY=0.32;
//二室一厅的相关数据
var TOWROOM_X=-1.56;
var TOWROOM_Y=-0.08;
var TOWROOM_MINX=-1.82;
var TOWROOM_MAXX=-1.25;
var TOWROOM_MINY=-0.18;
var TOWROOM_MAXY=0.007;
//三室一厅的相关数据
var THREEROOM_X=-1.56;
var THREEROOM_Y=-0.4;
var THREEROOM_MINX=-1.82;
var THREEROOM_MAXX=-1.25;
var THREEROOM_MINY=-0.51;
var THREEROOM_MAXY=-0.32;
//返回按钮
var returnX=1.7;//返回图标在2D中的x坐标
var returnY=-0.84;//返回图标在2D中的y坐标
var return_minX=returnX-0.25/2;//图标x的最左边值
var return_maxX=returnX+0.25/2;//图标x的最右边值
var return_minY=returnY-0.25/2;//图标y的最下边值
var return_maxY=returnY+0.25/2;//图标y的最上边值

var ONE=0;//ONE图片索引值
var TWO=2;//TWO图片索引值
var THREE=4;//THREE图片索引值
var PIC_NAME=["label9_1","label9_2","label8_1","label8_2","label7_1","label7_2",];
//==================按钮绘制位置等数据end================//

//==================摄像机相关数据start================//
var cameraX=0;//摄像机x位置
var cameraY=120;//摄像机y位置
var cameraZ=1050;//摄像机z位置	
	
var targetX=0;//目标点x位置
var targetY=26;//目标点y位置
var targetZ=0;//目标点z位置

var upX=0;
var upY=1;
var upZ=0;

var tempx=upX+cameraX;//中间值x
var tempz=upZ+cameraZ;//中间值z
var tempLimit=tempz;

var currSightDis=1000;//摄像机和目标的距离
var angdegElevation=5;//仰角
var angdegAzimuth=0;//方位角
var TOUCH_SCALE_FACTOR = 180.0/320;//角度缩放比例

var Vtop=1;
var Vnear=30;
var V2Dnear=1;//2D下
var Vfar=50000;

//==================摄像机相关数据end================//	

var isReturn=false;//是否返回主界面
var isRoomType=false;//转到浏览户型的页面的标志位
var roomTypeIndex=0;//户型id
var count=0;//停顿时间
var displaySize=16;//每一块的尺寸