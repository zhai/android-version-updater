Android-Version-Updater 
=====

### 程序特点
                1. 开源的项目
                
                2. 自动检查版本是否有更新，集成代码简单
                
                3. 服务器端可以直接控制是否客户端强制更新
                


### 集成步骤：
                1. 下载项目，导入eclispe,添加项目Android version updater引用
                
                2. 配置 Manifest.xml 添加如下节点
                <!-- Android version updater -->
                <activity
                android:name="com.zhai.updater.Activity_Verison_Update"
                android:label="@string/app_name"
                android:theme="@style/DialogTheme" >
                </activity>
                <service android:name="com.zhai.updater.CheckUpdateService" />
                <service android:name="com.zhai.updater.UpdateService" >
                 </service>
                 
                3. 添加服务器的配置文件， 建立/src/update.config 文件
                文件格式如下：Update_Url=http://code.taobao.org/svn/zhaisoft/branches/zhaihost/zhaihost_update.txt 

                4.配置服务器的update.txt文件
                文件格式说明： 
                //要空一格

                version_name=1.0.20.1210   //服务器保存的最新app版本号, 当客户端版本低于此版本，会提示升级
                force_update=0   //1：强制升级，不升级将无法使用  0：用户可以跳过升级，一般置0
                apk=http://code.taobao.org/svn/zhaisoft/branches/zhaihost/onekeyhost.apk
                //升级文件apk的路径
                info=http://code.taobao.org/svn/zhaisoft/branches/zhaihost/aboutonekeyhost.html
                //升级信息说明,Html格式，美观大方


                
### update.txt配置说明

                version_name=1.0.20.1210 \#版本号
                force_update=0
                apk=http://code.taobao.org/svn/zhaisoft/branches/ants-home/onekeyhost.apk
                info=http://code.taobao.org/svn/zhaisoft/branches/ants-home/update.html




 
