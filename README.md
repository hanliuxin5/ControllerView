# **DEMO使用**
**本项目假设您有一定基础的原生开发经验**
提供了
```
<controller src="*.js" tag="lychee"></controller>
```
标签来将vue页面作为Android里的fragment组件灵活配置使用,
通过动态改变src的值来改变展现的页面内容

(由于才疏学浅目前一个页面最多配置一个该标签,并且还得写死一个tag)  >_<!



**编译工程之前请先确认**
```
Gradle版本：gradle-3.3-all.zip
AS Grable插件：com.android.tools.build:gradle:2.3.1
```

完成之后打开
```
/vue
```
找到 controller.vue 文件
修改 `_this.src` 指向的 `*.js` 为你需要展示的页面文件路径 
```
  case 1:
  _this.src = '编译成*.js的子页面文件路径';
  break;
```
（本demo提供了4个内容稍有不同的 `fragmentX.vue` 以供编译）

最后将编写好的`controller.vue`编译生成的`*.js`文件作为项目入口路径配置到
`SplashActivity`的`sUrl`作为初始化页面

一切配置妥当，就运行项目吧！

