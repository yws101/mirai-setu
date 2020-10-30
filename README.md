<div align="center">
   <img width="160" src="http://img.mamoe.net/2020/02/16/a759783b42f72.png" alt="logo"></br>


   <img width="95" src="http://img.mamoe.net/2020/02/16/c4aece361224d.png" alt="title">

----
Mirai 是一个在全平台下运行，提供 QQ 协议支持的高效率机器人库

这个项目的名字来源于
     <p><a href = "http://www.kyotoanimation.co.jp/">京都动画</a>作品<a href = "https://zh.moegirl.org/zh-hans/%E5%A2%83%E7%95%8C%E7%9A%84%E5%BD%BC%E6%96%B9">《境界的彼方》</a>的<a href = "https://zh.moegirl.org/zh-hans/%E6%A0%97%E5%B1%B1%E6%9C%AA%E6%9D%A5">栗山未来(Kuriyama <b>Mirai</b>)</a></p>
     <p><a href = "https://www.crypton.co.jp/">CRYPTON</a>以<a href = "https://www.crypton.co.jp/miku_eng">初音未来</a>为代表的创作与活动<a href = "https://magicalmirai.com/2019/index_en.html">(Magical <b>Mirai</b>)</a></p>
图标以及形象由画师<a href = "">DazeCake</a>绘制
</div>

# mirai-setu
一个使用[loliconAPI](https://api.lolicon.app/#/setu) 的[mirai-console](https://github.com/mamoe/mirai-console) 的色图插件

# 声明
本插件为开发学习用，请勿用于非法用途。部分搜索的图片可能会引发不适，甚至导致账号封禁以及群封禁，请谨慎使用。作者本人不承担任何责任。

本插件遵循mirai软件AGPLv3协议开源，不参与一切商业活动。

由于作者学业压力较大，不能保证插件的及时更新以及bug修复。

##使用说明
这是一个运行在[mirai-console](https://github.com/mamoe/mirai-console) 下的插件,需要自行下载安装。

如果您对mirai-console不熟悉可以使用我自己打包的mirai-console，[下载链接](https://pan.baidu.com/s/15HLgK2jjHTd6y3UBcAdozg) 提取码 setu

自行编译打包jar放入plugins文件夹，或者前往release下载最新版本

请使用"1.0-RC"或者以上版本加载使用，建议使用JDK11版本运行

在机器人加入的群发送指令使用

如果有问题和建议欢迎提出
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
