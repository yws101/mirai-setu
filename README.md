# mirai-setu
一个简单制作的色图mirai插件，现在还有很多不完善，学习用

##使用说明
这是一个运行在[mirai-console](https://github.com/mamoe/mirai-console)下的插件,需要自行下载安装使用。

自行编译打包jar放入plugins文件夹

请使用"1.0-M4"或者以上版本加载使用

在机器人对应的群发送指令使用
## 指令
* 色图时间

获取一张图片
* 青少年模式

关闭R18，发送正常的瑟图
* 青壮年模式

开启R18
* 搜色图 [关键词]

搜索图片

## 配置文件
config/com.blrabbit.mirai-setu/setu-config.yml

    # 插件名称
    name: setu       
    # lolicon的APIKEY          
    APIKEY:   
    # 获取图片的指令         
    command_get: 色图时间
    # 关闭R18的指令
    command_R18off: 青少年模式
    # 开启R18的指令
    command_R18on: 青壮年模式
    # 搜图指令
    command_search: 搜色图

## TO DO
增加缓存区，提高请求速度

增加本地库

增加搜图h'j'j'g

    

