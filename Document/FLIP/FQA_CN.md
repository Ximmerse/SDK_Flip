<h1>Flip Controller 常见问题解答</h1>


&emsp;

**1. 运行Gear VR版本的Demo时提示"thread priority secority escption makesure the apk is signed"**

>  在Gear VR运行测试demo，需要在oculus的官网上申请三星手机签名证书，将证书导入APK的assets目录才能运行。具体请点击[这里](https://docs.unity3d.com/Manual/VRDevices-Oculus.html),查看Gear VR开发环境的搭建说明。

&emsp;

**2. VR一体机BTConfig无法获取焦点**

>  可通过投屏软件(vysor、total Control)远程控制一体机,进行手柄绑定

&emsp;

**3. 打开Playground或PickCubes等vr应用，手柄光标的初始朝向不是正对**
>   开发自己应用，进入应用的时候，须对手柄进行recenter操作，就像daydream应用一样

&emsp;

**3. PC端同时配对两个以上手柄，后续配对手柄无效**
>   打开系统注册表项HKEY_CURRENT_USER\SOFTWARE\Ximmerse\Runtime\X-Cobra，将X-Cobra下的项目删除，重新配对手柄
