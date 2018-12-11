const { exec } = require('child_process');
const express = require('express');
const app = express();

app.use(require('cors')());

app.get('/',function(req,res){
    if(req.query.letters != undefined && req.query.search != undefined){
        execute(req.query.letters,req.query.search,res)
    }else{
        res.sendStatus(400)
    }
});

const server = app.listen(3001, () =>{
    console.log("API is running at port 3001...");
});

server.timeout = 1000000000;
async function execute(a,b,res){
    console.log('> java Generate '+a+' '+b)
    exec('java Generate '+a+' '+b,(err, stdout, stderr) => {
        if(err){
            res.sendStatus(400);
        }
        res.send([stdout]);
    })
}
