var aws = require('aws-sdk');
var express = require('express');
var multer = require('multer');
var multerS3= require('multer-s3');
var bodyparser = require('body-parser');

aws.config.update({
    //secretAccessKey : '',
    //accessKeyId : '',
    region: 'us-east-1'
})

var app = express();
app.use(bodyparser.json());

var s3 = new aws.S3();
var upload = multer({
    storage : multerS3({
        s3 : s3,
        bucket : "s3-task",
        acl: 'public-read',
        key : function(req,file,callback){
            console.log(file + " & " +req.bucketName);
            callback(null,file.originalname); //use Date.now() for unique file keys
        }
    })
});

//use by upload form
//'image' is the key name
app.post('/upload', upload.array('image',2), function (req, res, next) {
    res.send("Uploaded!");
});

app.listen(3000, function () {
    console.log('Example app listening on port 3000!');
});