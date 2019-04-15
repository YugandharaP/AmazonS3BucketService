 
var express = require('express'), 
aws = require('aws-sdk'),
bodyParser = require('body-parser'),
multer = require('multer'),
multerS3 = require('multer-s3');

aws.config.update({
    secretAccessKey: '',
    accessKeyId: '',
    region: 'us-east-1'
});

var app = express(),
s3 = new aws.S3();

app.use(bodyParser.json());

var upload = multer({
        storage: multerS3({
        s3: s3,
        bucket: 's3-task',
        key: function (req, file, cb) {
        console.log(file);
        cb(null, file.originalname); //use Date.now() for unique file keys
    }
})
});

//open in browser to see upload form
app.get('/', function (req, res) {
res.sendFile(__dirname + '/index.html');
});

//use by upload form
app.post('/upload', upload.array('index.html',1), function (req, res, next) {
    console.log("inside post api");
    res.send("Uploaded!");
});

app.listen(3000, function () {
console.log('Example app listening on port 3000!');
}); 
 
