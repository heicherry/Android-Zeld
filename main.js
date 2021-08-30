var mysql  = require('mysql');  
 
var connection = mysql.createConnection({     
  host     : 'localhost',       
  user     : 'zeld',              
  password : 'dose587gab223',       
  port: '3306',                   
  database: 'zeld' 
}); 
 
connection.connect();
 
function recordAppLaunch(uuid,model){
    var launchSql = 'INSERT INTO launch(uuid,os_model,time) VALUES(?,?,now())';
    var launchSqlParams = [uuid,model];

    connection.query(launchSql,launchSqlParams,function (err, result) {
        if(err){
         console.log('[INSERT ERROR] -recordAppLaunch- ',err.message);
         return;
        }

       console.log('--------------------------INSERT-recordAppLaunch----------------------------');
       console.log('uuid:',uuid)
       console.log('INSERT ID:',result);
       console.log('-----------------------------------------------------------------\n\n');
    });
}

function recordSceneEnter(uuid,scene,os_model){
    var sceneEnterSql = 'INSERT INTO scene(uuid,scene,os_model,time) VALUES(?,?,?,now())';
    var sceneEnterParams = [uuid,scene,os_model];

    connection.query(sceneEnterSql,sceneEnterParams,function (err, result) {
        if(err){
         console.log('[INSERT ERROR] -recordSceneEnter- ',err.message);
         return;
        }

       console.log('--------------------------INSERT-recordSceneEnter---------------------------');
       console.log('uuid:',uuid)
       console.log('scene:',scene)
       console.log('INSERT ID:',result);
       console.log('-----------------------------------------------------------------\n\n');
    });
}

function recordGameResult(uuid,scene,result,os_model){
    var gameResultSql = 'INSERT INTO gameresult(uuid,scene,result,os_model,time) VALUES(?,?,?,?,now())';
    var gameResultParams = [uuid,scene,result,os_model];

    connection.query(gameResultSql,gameResultParams,function (err, result) {
        if(err){
         console.log('[INSERT ERROR] -recordGameResult- ',err.message);
         return;
        }

       console.log('--------------------------INSERT-recordGameResult---------------------------');
       console.log('uuid:',uuid)
       console.log('scene:',scene)
       console.log('INSERT ID:',result);
       console.log('-----------------------------------------------------------------\n\n');
    });
}


var express = require('express');
var app = express();

app.get('/', function (req, res) {
   res.send('Hello World');
   req.params
})

app.get('/launch', function (req, res) {
    recordAppLaunch(req.query.uuid,req.query.os_model)
    res.send('success,uuid: ' + req.query.uuid);
 })

app.get('/sceneenter', function (req, res) {
    recordSceneEnter(req.query.uuid,req.query.scene,req.query.os_model)
    res.send('success,uuid: ' + req.query.uuid);
})

app.get('/gameresult', function (req, res) {
    recordGameResult(req.query.uuid,req.query.scene,req.query.result,req.query.os_model)
    res.send('success,uuid: ' + req.query.uuid);
})

 
var server = app.listen(8090, function () {
 
  var host = server.address().address
  var port = server.address().port
 
  console.log("应用实例，访问地址为 http://%s:%s", host, port)
  
  console.log('Server start! enjoy!')
})