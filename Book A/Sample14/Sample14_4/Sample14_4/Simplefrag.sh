#version 300 es
precision mediump float;
uniform sampler2D sTextureDay;
uniform sampler2D sTextureNight;
in vec2 vTextureCoord;
in vec4 vAmbient;
in vec4 vDiffuse;
in vec4 vSpecular;
out vec4 fragColor;

void main()                         
{  
  vec4 finalColorDay;   
  vec4 finalColorNight;   
  
  finalColorDay= texture(sTextureDay, vTextureCoord);
  finalColorDay = finalColorDay*vAmbient+finalColorDay*vSpecular+finalColorDay*vDiffuse;
  finalColorNight = texture(sTextureNight, vTextureCoord);
  finalColorNight = finalColorNight*vec4(0.5,0.5,0.5,1.0);
  
  if(vDiffuse.x>0.21)
  {
    fragColor=finalColorDay;
  } 
  else if(vDiffuse.x<0.05)
  {     
     fragColor=finalColorNight;
  }
  else
  {
     float t=(vDiffuse.x-0.05)/0.16;
     fragColor=t*finalColorDay+(1.0-t)*finalColorNight;
  }  
}              
