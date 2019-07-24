//变换矩阵管理类对象
var ms1=new MatrixState();
var bs=1;//缩放比例值
var housemodel;//室内模型

var ms2D=new MatrixState();//2D对象的变化矩阵

var TOUCH_SCALE_FACTOR = 180.0/320;//角度缩放比例
//关于摄像机的变量
//================摄像机 start================
var cameraX=5.3;//摄像机x位置
var cameraY=0;//摄像机y位置
var cameraZ=19;//摄像机z位置	
	
var targetX=0;//目标点x位置
var targetY=1.6;//目标点y位置
var targetZ=0;//目标点z位置

var upX=0;
var upY=1;
var upZ=0;

var tempx=upX+cameraX;//中间值x
var tempz=upZ+cameraZ;//中间值z
var tempLimit=tempz;

var currSightDis=200;//摄像机和目标的距离
var angdegElevation=30;//仰角
var angdegAzimuth=180;//方位角
//=====================end===================
var Vtop=1;
var Vnear=30;
var Vfar=500000000;
var uriIndex="2";//地址变量
var uriIndex1="2";//地址变量

var imageName;//模型图片名
var objName;//模型名
//============屏幕数据=======start==============//
var SCREEN_WIDTH_STANDARD=1200;//屏幕标准宽度
var SCREEN_HEIGHT_STANDARD=600;//屏幕标准高度
var RATIO=SCREEN_WIDTH_STANDARD/SCREEN_HEIGHT_STANDARD;//屏幕标准比例--即透视投影的比例
//============屏幕数据=======end==============//

var isReturn=false;//是否返回主界面
//返回按钮
var returnX=1.7;//返回图标在2D中的x坐标
var returnY=-0.84;//返回图标在2D中的y坐标
var return_minX=returnX-0.25/2;//图标x的最左边值
var return_maxX=returnX+0.25/2;//图标x的最右边值
var return_minY=returnY-0.25/2;//图标y的最下边值
var return_maxY=returnY+0.25/2;//图标y的最上边值