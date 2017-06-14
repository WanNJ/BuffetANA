# 迭代一
## 运行说明
1.要求已安装JDK1.8

2.BUFF文件夹下是迭代一的项目，Document/迭代一  文件夹下是迭代一的文档，Data文件夹下是迭代一的股票数据，executable jars  文件夹下是迭代一的可执行jar文件

3.BuffServer-1.0-SNAPSHOT-jar-with-dependencies.jar和上级目录的Data文件夹是服务器端的内容，BuffClient-1.0-SNAPSHOT-jar-with-dependencies.jar和libs、text文件夹时客户端的内容

4.Windows下运行

    开始.bat

MacOS和Linux下运行

    开始.sh

即可开始程序


# 迭代二
## 运行说明
1.要求已安装JDK1.8

2.BUFF文件夹下是迭代二的项目，Document/迭代二  文件夹下是迭代一的文档，Data文件夹下是迭代二的股票数据，executable jars 2.0  文件夹下是迭代二的可执行jar文件

3.运行

    BuffClient-2.0.jar

即可开始程序


#  迭代三
## 浏览网站
迭代三是是WEB项目，如果你在自己的电脑上运行了服务器，在浏览器输入网址http://localhost:3000 即可访问。

## 服务器端运行说明
需要安装的依赖：

    Node.js 6.10以上  python3.6  

以及python库：

    numpy scipy sklearn tensorflow pymongo

以及mongodb数据库，并将BuffTreasureWebApp/app中连接数据库的语句

    mongoose.connect('mongodb://localhost/allInfo');

中localhost改成你的数据库的的ip地址
进入到BuffTreasureWebApp/bin下，运行：

    node www

即可运行服务器端
注意：请保证你的"python"命令运行的是python3，而不是python2
