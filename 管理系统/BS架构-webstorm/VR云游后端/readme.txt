1.安装好node.js和mysql
2.下载依赖
  cd 项目
  npm install / cnpm i
3. 配置数据库
   数据库可在config.js文件中配置 默认root 123456 login
  3.1创建数据库
  create database login;
  3.2 导入sql文件夹下的所有数据
  必须创建所有表（用户表，内景表，外景表，酒店表，新闻表），并且创建(admin,admin)用户
4.启动项目
 npm run start/nodemon app.js/ node app
 
    
