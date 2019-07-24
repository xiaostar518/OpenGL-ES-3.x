//=============绘制对象start====================//
var obj;//矩形对象
var trees=new Array();//树的对象
var mainView_2D;//绘制主界面的2D物体
var mainView_rectangle;//绘制主界面的2D物体
var grand;//草坪绘制对象
var building;//楼房模型	
var lable=new Array(8);//创建按钮对象
var lable_down=new Array(6);//创建按钮对象
//=============绘制对象end====================//

//=============屏幕相关数据start====================//
var SCREEN_WIDTH_STANDARD=1200;//屏幕标准宽度
var SCREEN_HEIGHT_STANDARD=600;//屏幕标准高度
var RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//屏幕标准比例--即透视投影的比例
//=============屏幕相关数据end====================//
//=============屏幕相关数据end====================//

//=============2D矩阵start====================//
var ms2D=new MatrixState();//2D对象的矩阵
//=============2D矩阵end====================//

//=================按钮位置数据 start==============
//点击漫游坐标
var CRUISEGO_LEFT=975;
var CRUISEGO_RIGHT=1140;
var CRUISEGo_TOP=251;
var CRUISEGo_BUTTOM=328;
//点击自由行走坐标
var FREETOWALK_LEFT=975;
var FREETOWALK_RIGHT=1140;
var FREETOWALK_TOP=401;
var FREETOWALK_BUTTOM=479;
//浏览模型1
var MODEL1_LEFT=93;
var MODEL1_RIGHT=240;
var MODEL1_TOP=212;
var MODEL1_BUTTOM=267;
//浏览模型2
var MODEL2_LEFT=93;
var MODEL2_RIGHT=240;
var MODEL2_TOP=294;
var MODEL2_BUTTOM=347;
//浏览模型3
var MODEL3_LEFT=93;
var MODEL3_RIGHT=240;
var MODEL3_TOP=372;
var MODEL3_BUTTOM=449;
//浏览模型4
var MODEL4_LEFT=93;
var MODEL4_RIGHT=240;
var MODEL4_TOP=453;
var MODEL4_BUTTOM=507;
//=================按钮位置数据 end==============

//==================设置摄像机环视 start==========
var Vtop=1;
var Vnear=30;//3D下--近平面
var Vfar=30000;//远平面
var V2Dnear=1;//2D下--近平面

var cameraX=0;//摄像机位置x值
var cameraY=120;//摄像机y位置
var cameraZ=1050;//摄像机z位置
var cameraLimit=cameraZ;
var targetX=0;//目标点x位置
var targetY=26;//目标点y位置
var targetZ=0;//摄像机目标点z位置
var upX=0;//摄像机up向量x值
var upY=1;//摄像机up向量y值
var upZ=0;//摄像机up向量z值
var tempx=upX+cameraX;//中间值x
var tempz=upZ+cameraZ;//中间值z
var tempLimit=tempz;
var degree=0;//旋转角度
//==================设置摄像机环视 end==========

var isDown=new Array(0,0,0,0,0,0);//判断是否被按下
var displaySize=16;//每一块的尺寸
var openIndex=-1;//模型id

//==================树的绘制位置数据 start==========
var point0=[-16,14,15,14,-12,-12,16,-14];//绘制树位置的数据--高8
var point1=[-12,12,12,12,-13,-15,12,-16];//绘制树位置的数据---高10
var point2=[-16,9,16,15,-16,-10,10,-12];//绘制树位置的数据---高6
//==================树的绘制位置数据 end==========

//===================页面跳转的标志位start================
var isHouseModel=false;//切换到浏览楼房模型界面
var isRoomType=false;//切换到查看户型界面
var isCruiseGo=false;//切换到漫游模式界面
var isOpen=false;//切换到浏览小区全景的页面
//===================页面跳转的标志位end================