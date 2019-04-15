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

var singleUpload = upload.single('image');
var multipleUpload = upload.array('image', 2);

app.post('/single-upload', function(req, res) {
    singleUpload(req, res, function(err, file) {
      if (err) {
        return res.status(422).send({errors: [{title: 'Image Upload Error', detail: err.message}] });
      }
      return res.json({'imageUrl': req.file.location});
    });
  })

//use by upload form
//'image' is the key name
app.post('/multiple-upload', function (req, res) {
    multipleUpload(req,res,function(err,file){
        if(err){
            return res.status(422).send({errors: [{title: 'Image Upload Error', detail: err.message}] });
        }
        return res.json({'message': 'uploaded'});
    });
});

app.listen(8080, function () {
    console.log('Example app listening on port 8080!');
});