// 引入express
const express = require("express");
const config = require("./config")
const swig=require('swig');

//文件操作
const multiparty = require('multiparty')
const fs = require('fs')


// 引入mysql模块
const mysql = require('mysql');
const con = mysql.createConnection({ // 配置mysql数据库
    host: config.db_host,　　// 主机名
    port: config.db_port,　　// 默认端口
    user: config.db_user,　　// 连接的名字
    password: config.db_pwd,　　// 连接的密码
    database: config.db_database　　// 连接的数据库
});
con.connect(); // 与数据库建立连接

// 创建服务器的实例对象
const app = express();
//静态文件托管设置
app.use('/public',express.static(__dirname+'/public'));
app.use(express.json());

//模板引擎设置
app.set('views', './views')
app.set('view engine', 'html')
//使用模板引擎
app.engine('html', swig.renderFile);
swig.setDefaults({cache:false})

// 处理post请求
app.use(express.urlencoded({
    extended: true  // 可以解析多层json,false会解析成一层
}));

// 启动服务器，3003为端口号，选择一个空闲的端口号
app.listen(config.app_port, () => {
    console.log("Server running at http://127.0.0.1:"+config.app_port);
});

// 引入express-session
var session = require("express-session");
// 配置session中间件
app.use(session({
    secret: 'keyboard cat', // 建议使用 128 个字符的随机字符串
    cookie: { maxAge: 20 * 60 * 1000 }, //cookie生存周期20*60秒
    resave: true,  //cookie之间的请求规则,假设每次登陆，就算会话存在也重新保存一次
    saveUninitialized: true //强制保存未初始化的会话到存储器
}));


// 一个数据库查询函数
function aQuery(res,con,sql){
    con.query(sql, (err, result) => {
        if (err) {
            res.json({
                code: 500,
                msg: "SQL执行错误",
                err:err
            })
        } else {
            res.json({
                code: 0,
                data: result // 查询结果
            })
        }
    })
}
// 传入count但返回count的查询函数
function countQuery(res,con,sql,count){
    con.query(sql, (err, result) => {
        if (err) {
            res.json({
                code: 500,
                msg: "SQL执行错误",
                err:err
            })
        } else {
            res.json({
                code: 0,
                count:count,
                data: result // 查询结果
            })
        }
    })
}
// 不传入count但返回count的查询函数
function queryLength(res,con,sql){
    con.query(sql, (err, result) => {
        if (err) {
            res.json({
                code: 500,
                msg: "SQL执行错误",
                err:err
            })
        } else {
            res.json({
                code: 0,
                count:result.length,
                data: result // 查询结果
            })
        }
    })
}
// 时间格式化的查询函数
function timeQuery(res,con,sql,count){
    con.query(sql, (err, result) => {
        if (err) {
            res.json({
                code: 500,
                msg: "SQL执行错误",
                err:err
            })
        } else {
            result.forEach((i)=>{
                i.create_time = i.create_time.toLocaleString('zh', { hour12: false }).replace(/\//g,"-");
            })
            // console.log(result);
            res.json({
                code: 0,
                count:count,
                data: result // 查询结果
            })
        }
    })
}


// 处理用户表
let user_num = 20;
// 返回用户表
app.get('/table/user/', (req, res) => {
    const page = req.query.page-1;
    const num = req.query.limit;
    if (page === 0){
        let sql1 = "select count(*) as num from user;"
        con.query(sql1, (err, result) => {
            if (err) {
            } else {
                user_num = result[0].num;
            }
        })
    }
    let sql = "select * from user limit "+page*num+","+num+";"
    countQuery(res,con,sql,user_num);
})
// 增加用户
app.get('/table/user/add', (req, res) => {
    let sql = `insert into user (username,password) values ('${req.query.username}','${req.query.password}');`
    aQuery(res,con,sql);
})
// 删除单个用户
app.get('/table/user/del', (req, res) => {
    let sql = `DELETE FROM user WHERE id = ${req.query.id};`
    console.log(sql);
    aQuery(res,con,sql);
})
// 删除多个用户
app.post('/table/user/somedel', (req, res) => {
    let datalist = req.body.data;
    let idList = []
    datalist.forEach((i)=>{
        idList.push(i.id)
    })
    let idStr = "(" + idList.join(",") + ")";
    let sql = `DELETE FROM user WHERE id in ${idStr};`
    console.log(sql);
    aQuery(res,con,sql);
})
// 通过id更新用户表
app.get('/table/user/update', (req, res) => {
    let userdata = req.query
    let sql = `update user set username='${userdata.username}',password='${userdata.password}' where id =${userdata.id};`
    // console.log(sql);
    aQuery(res,con,sql);
})
//添加用户
app.get('/user/add', (req, res) => {
    res.render('./usertable/add')
})
//修改用户
app.get('/user/update', (req, res) => {
    let user = {
        id:req.query.id,
        name:req.query.name,
        pwd:req.query.pwd
    }
    res.render('./usertable/edit',{user:user})
})
// 搜索用户
app.post('/user/search', (req, res) => {
    let resdata = req.body.searchParams
    let sql = `SELECT * FROM user WHERE username LIKE '%${resdata}%';`
    queryLength(res,con,sql);
})




// 返回vr外景表
let pano_num = 20;
app.get('/table/pano/', (req, res) => {
    let page = req.query.page-1;
    let num = req.query.limit;
    let sql = "select * from pano_table limit "+page*num+","+num+";"
    // aQuery(res,con,sql);
    if (page === 0){
        let sql1 = "select count(*) as num from pano_table;"
        con.query(sql1, (err, result) => {
            if (err) {
            } else {
                pano_num = result[0].num;
            }
        })
    }
    timeQuery(res,con,sql,pano_num);
})
//修改vr外景
app.get('/pano/update', (req, res) => {
    let pano = {
        uid:req.query.uid,
        pano_name:req.query.pano_name,
        pano_addr:req.query.pano_addr,
        pano_x:req.query.pano_x,
        pano_y:req.query.pano_y,
        pano_uid:req.query.pano_uid,
        create_time:req.query.create_time
    }
    res.render('./panotable/edit',{pano:pano})
})
// 通过id更新vr外景表
app.get('/table/pano/update', (req, res) => {
    let panodata = req.query
    let sql = `update pano_table set pano_name='${panodata.pano_name}',pano_addr='${panodata.pano_addr}',pano_x='${panodata.pano_x}',pano_y='${panodata.pano_y}',pano_uid='${panodata.pano_uid}',create_time='${panodata.create_time}' where uid =${panodata.uid};`
    aQuery(res,con,sql);
})
// 增加vr外景表数据
app.get('/table/pano/add', (req, res) => {
    let sql = `insert into pano_table (pano_name,pano_addr,pano_x,pano_y,pano_uid) values ('${req.query.pano_name}','${req.query.pano_addr}','${req.query.pano_x}','${req.query.pano_y}','${req.query.pano_uid}');`
    // console.log(sql);
    aQuery(res,con,sql);
})
//添加vr外景
app.get('/pano/add', (req, res) => {
    res.render('./panotable/add')
})
// 删除单个vr外景
app.get('/table/pano/del', (req, res) => {
    let sql = `DELETE FROM pano_table WHERE uid = ${req.query.uid};`
    aQuery(res,con,sql);
})
// 删除多个vr外景
app.post('/table/pano/somedel', (req, res) => {
    let datalist = req.body.data;
    let idList = []
    datalist.forEach((i)=>{
        idList.push(i.uid)
    })
    let idStr = "(" + idList.join(",") + ")";
    let sql = `DELETE FROM pano_table WHERE uid in ${idStr};`
    aQuery(res,con,sql);
})
// 搜索vr外景风景区
app.post('/pano/search', (req, res) => {
    let resdata = req.body.searchParams
    let sql = `SELECT * FROM pano_table WHERE pano_name LIKE '%${resdata}%';`
    // console.log(sql);
    queryLength(res,con,sql);
})



// 返回vr内景表
let indoor_pano_num = 20;
app.get('/table/indoorpano/', (req, res) => {
    let page = req.query.page-1;
    let num = req.query.limit;
    let sql = "select * from indoorpano_table limit "+page*num+","+num+";"
    if (page === 0){
        let sql1 = "select count(*) as num from indoorpano_table;"
        con.query(sql1, (err, result) => {
            if (err) {
            } else {
                indoor_pano_num = result[0].num;
            }
        })
    }
    countQuery(res,con,sql,indoor_pano_num)
})
//修改vr内景
app.get('/indoorpano/update', (req, res) => {
    let indoorpano = {
        id:req.query.id,
        addrname:req.query.addrname,
        pid:req.query.pid,
    }
    res.render('./indoorpanotable/edit',{indoorpano:indoorpano})
})
// 通过id更新vr内景表
app.get('/table/indoorpano/update', (req, res) => {
    let indoorpanodata = req.query
    let sql = `update indoorpano_table set addrname='${indoorpanodata.addrname}',pid='${indoorpanodata.pid}' where id =${indoorpanodata.id};`
    aQuery(res,con,sql);
})
//添加vr内景
app.get('/indoorpano/add', (req, res) => {
    res.render('./indoorpanotable/add')
})
// 增加vr内景表数据
app.get('/table/indoorpano/add', (req, res) => {
    let sql = `insert into indoorpano_table (addrname,pid) values ('${req.query.addrname}','${req.query.pid}');`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 删除单个vr内景
app.get('/table/indoorpano/del', (req, res) => {
    let sql = `DELETE FROM indoorpano_table WHERE id = ${req.query.id};`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 删除多个vr内景
app.post('/table/indoorpano/somedel', (req, res) => {
    let datalist = req.body.data;
    let idList = []
    datalist.forEach((i)=>{
        idList.push(i.id)
    })
    let idStr = "(" + idList.join(",") + ")";
    let sql = `DELETE FROM indoorpano_table WHERE id in ${idStr};`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 搜索vr内景风景区
app.post('/indoorpano/search', (req, res) => {
    let resdata = req.body.searchParams
    let sql = `SELECT * FROM indoorpano_table WHERE addrname LIKE '%${resdata}%';`
    queryLength(res,con,sql);
})



// 返回vr酒店
let hotel_pano_num = 20;
app.get('/table/hotelpano/', (req, res) => {
    let page = req.query.page-1;
    let num = req.query.limit;
    let sql = "select * from hotelpano_table limit "+page*num+","+num+";"
    if (page === 0){
        let sql1 = "select count(*) as num from hotelpano_table;"
        con.query(sql1, (err, result) => {
            if (err) {
            } else {
                hotel_pano_num = result[0].num;
            }
        })
    }
    countQuery(res,con,sql,hotel_pano_num)
})
//修改vr酒店
app.get('/hotelpano/update', (req, res) => {
    let indoorpano = {
        id:req.query.id,
        addrname:req.query.addrname,
        pid:req.query.pid,
    }
    res.render('./hotelpanotable/edit',{indoorpano:indoorpano})
})
// 通过id更新vr酒店表
app.get('/table/hotelpano/update', (req, res) => {
    let indoorpanodata = req.query
    let sql = `update hotelpano_table set addrname='${indoorpanodata.addrname}',pid='${indoorpanodata.pid}' where id =${indoorpanodata.id};`
    aQuery(res,con,sql);
})
//添加vr酒店
app.get('/hotelpano/add', (req, res) => {
    res.render('./hotelpanotable/add')
})
// 增加vr酒店表数据
app.get('/table/hotelpano/add', (req, res) => {
    let sql = `insert into hotelpano_table (addrname,pid) values ('${req.query.addrname}','${req.query.pid}');`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 删除单个vr酒店
app.get('/table/hotelpano/del', (req, res) => {
    let sql = `DELETE FROM hotelpano_table WHERE id = '${req.query.id}';`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 删除多个vr酒店
app.post('/table/hotelpano/somedel', (req, res) => {
    let datalist = req.body.data;
    let idList = []
    datalist.forEach((i)=>{
        idList.push(i.id)
    })
    let idStr = "(" + idList.join(",") + ")";
    let sql = `DELETE FROM hotelpano_table WHERE id in ${idStr};`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 搜索vr酒店
app.post('/hotelpano/search', (req, res) => {
    let resdata = req.body.searchParams
    let sql = `SELECT * FROM hotelpano_table WHERE addrname LIKE '%${resdata}%';`
    queryLength(res,con,sql);
})



// 返回新闻
let news_num = 20;
app.get('/table/news/', (req, res) => {
    let page = req.query.page-1;
    let num = req.query.limit;
    let sql = "select * from news_table limit "+page*num+","+num+";"
    // console.log(sql);
    if (page === 0){
        let sql1 = "select count(*) as num from news_table;"
        con.query(sql1, (err, result) => {
            if (err) {
            } else {
                news_num = result[0].num;
            }
        })
    }
    timeQuery(res,con,sql,news_num);
})
//修改新闻
app.get('/news/update', (req, res) => {
    let uid = req.query.uid
    let sql = `select * from news_table where uid =${uid};`
    con.query(sql, (err, result) => {
        if (err) {
            res.json({
                code: 500,
                msg: "SQL执行错误",
                err:err
            })
        } else {
            result[0].create_time = result[0].create_time.toLocaleString('zh', { hour12: false }).replace(/\//g,"-");
            // console.log(result);
            res.render('./newstable/edit',{news:result[0]})
        }
    })
})
// 通过id更新新闻表
app.get('/table/news/update', (req, res) => {
    let newsdata = req.query
    let sql = `update news_table set news_title='${newsdata.news_title}',news_source='${newsdata.news_source}',create_time='${newsdata.create_time}',news_content='${newsdata.news_content}' where uid =${newsdata.uid};`
    aQuery(res,con,sql);
})
// 增加新闻表数据
app.post('/table/news/add', (req, res) => {
    let sql = `insert into news_table (news_title,news_source,news_content) values ('${req.body.news_title}','${req.body.news_source}','${req.body.news_content}');`
    // console.log(sql);
    aQuery(res,con,sql);
})
//添加新闻
app.get('/news/add', (req, res) => {
    res.render('./newstable/add')
})
// 删除单个新闻
app.get('/table/news/del', (req, res) => {
    let sql = `DELETE FROM news_table WHERE uid = '${req.query.uid}';`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 删除多个新闻
app.post('/table/news/somedel', (req, res) => {
    let datalist = req.body.data;
    let idList = []
    datalist.forEach((i)=>{
        idList.push(i.uid)
    })
    let idStr = "(" + idList.join(",") + ")";
    let sql = `DELETE FROM news_table WHERE uid in ${idStr};`
    // console.log(sql);
    aQuery(res,con,sql);
})
// 搜索新闻
app.post('/news/search', (req, res) => {
    let resdata = req.body.searchParams
    let sql = `SELECT * FROM news_table WHERE news_title LIKE '%${resdata}%';`
    queryLength(res,con,sql);
})
// 请求统计
app.get('/data/count',(req,res) =>{
    let sql = `select count(*) as a from pano_table union all
               select count(*) from indoorpano_table union all
               select count(*)  from hotelpano_table union all
               select count(*)  from news_table union all
               select count(*) from user;`;
    con.query(sql, (err, result) => {
        if (err) {
            res.json({
                code: 500,
                msg: "SQL执行错误",
                err:err
            })
        } else {
            // console.log(result);
            let countdata = {
                pano:result[0].a,
                indoor:result[1].a,
                hotel:result[2].a,
                news:result[3].a,
                user:result[4].a
            }
            res.json(countdata)
            pano_num = countdata.pano;
            indoor_pano_num = countdata.indoor;
            hotel_pano_num = countdata.hotel;
            news_num = countdata.news;
            user_num =countdata.user;
        }
    })
})

// 文件上传
app.get('/upload', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./upload/upload')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
// 文件处理
app.post('/server/picture/upload',(req,res)=>{
    /* 生成multiparty对象，并配置上传目标路径 */
    let form = new multiparty.Form();
    // 设置编码
    form.encoding = 'utf-8';
    // 设置文件存储路径，以当前编辑的文件为相对路径
    form.uploadDir = './public/uploadimg';
    // parse，表单解析器
    // fields :普通的表单数据
    // files:上传的文件的信息
    form.parse(req, function (err, fields, files) {
        try {
            // 文件为files.file[0]
            let upfile = files.file[0]
            // console.log(upfile);
            // 为文件进行命名,修改upfile文件中的path,否则会随机生成文件名
            let newpath = form.uploadDir + '/' + upfile.originalFilename  //文件名
            // 重命名
            fs.renameSync(upfile.path, newpath);
            // 返回信息,((upfile.size)/1048576).toFixed(2)将文件由B转换为M的单位并进行取小数点后两位进行四舍五入向上取操作
            res.send({
                code:200,
                msg:'File Success',
                file_name:upfile.originalFilename,
                file_size:((upfile.size)/1048576).toFixed(2)+'M'
            })
        } catch {
            //    异常情况下的消息
            console.log(err)
            res.send({
                code:401,
                msg:'File error',
                more_msg:err
            })
        }
    })
});


//退出
app.get('/exit', (req, res) => {
    req.session.cookie.maxAge = 0;
    res.redirect('/login')
})
//我的
app.get('/me', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./other/me')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})


//根
app.get('/', (req, res) => {
    res.redirect('/login')
})
//首页
app.get('/home', (req, res) => {
    // res.render('./usertable/table')
    // res.redirect('/table/user/')
    // console.log(req.session.userinfo);
    if (req.session.userinfo=="adminlogin"){
        let sql = `select count(password) as a from user union all
               select count(pano_uid) from pano_table union all
               select count(pid)  from indoorpano_table union all
               select count(news_title) from news_table;`;
        con.query(sql, (err, result) => {
            if (err) {
                res.json({
                    code: 500,
                    msg: "SQL执行错误",
                    err:err
                })
            } else {
                // console.log(result);
                res.render('./home',{countdata:result})
                user_num = result[0].a;
                pano_num = result[1].a;
                indoor_pano_num = result[2].a;
                news_num = result[3].a;
            }
        })
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
    // res.render('./home');
})
//用户
app.get('/vr/user', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./usertable/table')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
//vr外景
app.get('/vr/pano', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./panotable/table')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
//vr内景
app.get('/vr/indoor', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./indoorpanotable/table')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
//vr酒店
app.get('/vr/hotel', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./hotelpanotable/table')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
//新闻
app.get('/vr/news', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./newstable/table')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
// 可视化表格
app.get('/vr/data', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./datashow/datashow')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
//  其他
app.get('/other', (req, res) => {
    if (req.session.userinfo=="adminlogin"){
        res.render('./other/tab')
    }else{
        res.status(404).render("./login/index",{msg:"登录过期，请登录！！"});
    }
})
//login
app.get('/login', function(req,res){
    res.render("./login/index")
})
//post login
app.post('/login', function(req,res){
    let sql = `select * from user where username = '${req.body.name}';`
    con.query(sql, (err, result) => {
        if (err) {
            res.status(404).render("./login/index",{msg:"sql执行错误！！"})
        } else {
            if (result == ""){
                res.status(404).render("./login/index",{msg:"没有该账户！！"})
            }else if(result[0].password == req.body.pwd & req.body.pwd == "admin"){
                // console.log(result[0].password);
                req.session.userinfo=result[0].username+"login";
                res.redirect("/home");
            }else {
                res.status(404).render("./login/index",{msg:"密码错误！！"});
            }
        }
    })
})
//注册
app.post('/register', function(req,res){
    res.status(404).render("./login/index",{msg:"你没有权限！！"});
})


//404,必须最最后面
app.use(function(req,res){
    res.status(404).render("./404")
})

