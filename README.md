<div align="center">
   <img width="160" src="doc/mirai.png" alt="logo"></br>

   <img width="95" src="doc/mirai.svg" alt="title">

----

mirai 是一个在全平台下运行，提供 QQ Android 协议支持的高效率机器人库

这个项目的名字来源于
<p><a href = "http://www.kyotoanimation.co.jp/">京都动画</a>作品<a href = "https://zh.moegirl.org.cn/zh-hans/%E5%A2%83%E7%95%8C%E7%9A%84%E5%BD%BC%E6%96%B9">《境界的彼方》</a>的<a href = "https://zh.moegirl.org.cn/zh-hans/%E6%A0%97%E5%B1%B1%E6%9C%AA%E6%9D%A5">栗山未来(Kuriyama <b>mirai</b>)</a></p>
<p><a href = "https://www.crypton.co.jp/">CRYPTON</a>以<a href = "https://www.crypton.co.jp/miku_eng">初音未来</a>为代表的创作与活动<a href = "https://magicalmirai.com/2019/index_en.html">(Magical <b>mirai</b>)</a></p>
图标以及形象由画师<a href = "https://github.com/DazeCake">DazeCake</a>绘制
</div>

# mirai-setu

一个使用[loliconAPI](https://api.lolicon.app/#/setu) 的[mirai-console](https://github.com/mamoe/mirai-console) 的色图插件

# 声明
本插件为开发学习用，请勿用于非法用途。部分搜索的图片可能会引发不适，甚至导致账号封禁以及群封禁，请谨慎使用。作者本人不承担任何责任。

本插件遵循mirai软件AGPLv3协议开源，不参与一切商业活动。

由于作者学业压力较大，不能保证插件的及时更新以及bug修复。

## 使用说明
1. 环境配置
   - 插件兼容mirai-console2.0及以上版本。
   - mirai-console需要java环境，建议使用java11以及以上的版本运行。
2. 插件下载
   - 前往本项目的[releases](https://github.com/meaningtree/mirai-setu/releases)下载插件
3. 插件运行
   - 将本放入mirai-console的plugins的文件夹然后运行mirai-console。
4. 关于lollicon的注意事项
   -
   lolicon是一个公开的setu库的APi，此API不属于本人，如果此API出现问题，恕我无力解决。图片获取是从i.pixiv.cat反向代理得到的，可以不使用科学上网获取，但是国内网络获取比较慢，获取卡顿和失败是正常情况。如果有条件的话可以使用科学上网进行加速，或者部署到没有限制的服务器上。
   - lolicon的APIKEy申请需要到Telegram上申请，需要使用科学上网，请自备工具。

## 指令

指令可以参考指令配置文件，指令可以在配置文件进行修改

## 配置文件

配置文件可以配置代理一些配置，参考注释配置

```
# 色图的一些指令
setucommand: 
  command_get: 
    - 色图时间
    - 涩图时间
    - 涩图来
    - 色图来
  command_search: 
    - 搜色图
    - 搜涩图
  command_off: 
    - 关闭插件
    - 封印
  command_setumode0: 
    - 普通模式
    - 青少年模式
  command_setumode1: 
    - 'R-18模式'
    - 青壮年模式
  command_setumode2: 
    - 混合模式
# bilibili等一些其他功能的指令
bilibilicommand: 
  command_bangumitoday: 
    - 今日番剧
  command_bangumiyesterday: 
    - 今日番剧
  command_bangumitomorrow: 
    - 明日番剧
  command_searchbangumibyimage: 
    - 以图搜番
    - 以图识番
```

message.yml

```
# 发送信息的格式，pid(图片pid) p(图片号) uid(作者uid) title(题目) author(作者) originalurl(原始url) r18(是否为r18) width(宽度) height(高度) tags(标签) largeurl(大号缩略图ps.无法保证100%有效)
SetuReply: "pid: %pid%\ntitle: %title%\nauthor: %author%\nurl: %originalurl%\ntags: %tags%"
# 来自lolicon的报错，code(错误代码)，msg(错误信息)
Lolicode401: lolicon错误代码：%code% 错误信息：%msg%
lolicode404: lolicon错误代码：%code% 错误信息：%msg%
lolicode429: lolicon错误代码：%code% 错误信息：%msg%
lolicodeelse: lolicon错误代码：%code%,发生此错误请截取详细信息到github提交issue 错误信息：%msg%
# 下载图片出现404自动回复
image404: 图片获取失败，可能图片已经被原作者删除
# 群友没有权限的时候的自动回复
setunopermission: 您没有权限操作
# 色图插件未启用自动回复
setumodeoff: 本群尚未开启插件,请联系管理员
# 搜色图提醒关键词自动回复
setusearchkey: 请输入搜索的关键词
# 关闭色图插件的自动回复
setumode_1: 已经关闭了本群的色图插件
setumode_1to_1: 本群尚未启用色图插件,无需再次禁用
# 设置普通模式的自动回复
setumode0: 切换为普通模式
setumode0to0: 本群色图已经为普通模式，无需切换
# 设置r-18模式的自动回复
setumode1: '切换为R-18模式'
setumode1to1: '本群色图已经为R-18模式，无需切换'
# 设置混合模式的自动回复
setumode2: 切换为混合模式
setumode2to2: 本群色图已经为混合模式，无需切换
```

setuconfig.yml

```
# 设置此插件主人的id。
masterid: 
  - 123456
# 设置lolicon的APIKEY，可以不设置，但是每天调用次数会比较少。
APIKEY: 365007185fc06c84ac62e6
# 代理设置,0为不使用代理，1为使用http代理，2为使用socks代理
proxyconfig: 2
httpproxy: 
  proxy: 'http://127.0.0.1:80'
socksproxy: 
  host: 127.0.0.1
  port: 4001
```
