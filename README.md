Android-Version-Updater 
=====





### 程序特点
                1. 开源
                
                2. android端配置非常简单，两句代码就搞定程序的配置
                
                3. 服务器端配置非常灵活简单，不需要动态服务器，只需要放一个txt文本文件即可







### 集成步骤：
                1. 客服端集成
                A, Gradle配置 加一行，   


```groovy
compile 'com.zhaisoft.lib.updater:android-version-updater:0.1.15'
```


                B.调用方法

                AndroidUpdateSDK.getInstance().init(Activity_Tab.this,true,"http://hsl.yanzhen100.com/apk/android-version-updater/update.txt");


                init参数说明， 
                参数1: 当前activity的context 
                参数2：如果没有新版本，是否提示当前已经是最新版本，主要用于app的设置菜单手动检测更新用
                true 提示
                false 不提示
                参数3: updata.txt的路径， 服务器的配置文件
                
                
                 
                3. update.txt文件
                文件格式说明： 
                //要空一格

                version_name=1.0.20.1210   //服务器保存的最新app版本号, 当客户端版本低于此版本，会提示升级
                force_update=0   //1：强制升级，不升级将无法使用  0：用户可以跳过升级，一般置0
                apk=http://code.taobao.org/svn/zhaisoft/branches/zhaihost/onekeyhost.apk
                //升级文件apk的路径
                info=http://code.taobao.org/svn/zhaisoft/branches/zhaihost/aboutonekeyhost.html
                //升级信息说明,Html格式，美观大方


                
##版本迭代

###0.1.18 beta
* 去掉不必要的权限，只保留两个权限，网络和下载权限
* 修改多进程下载的Bug
* 优化代码

###0.1.15 beta
* 更新UI
* 更新多个启动方法
* 添加了LogUtil工具类，可以配置在Debug模式中打印日志，release模式中不打印日志

###0.0.3 beta
* 第一个测试版本
* 修复强制升级情况下点击退出，程序无限重启的问题





 
