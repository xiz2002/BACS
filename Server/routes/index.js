var express = require('express');
var main = require('./main.js');
var param = require('./param.js');
var management_get = require('./management_get.js');
var management_post = require('./management_post.js');

var router = express.Router();

/* ======================= Home page ======================*/
/* GET home page. */
router.get('/', main.start);
/*
router.get('/', function(req, res) {
	res.render('index');
});
*/
/* ====================== Management ======================*/
/* GET Management Page. */
router.get('/management/users', management_get.Users); 

router.get('/management/users/insert', management_get.UserInsert);

router.get('/management/users/delete/:id', management_get.UserDelete);

router.get('/management/users/accept/:id', management_get.UserAccept);

router.get('/management/users/edit/:id', management_get.UserEdit);

router.get('/management/users/course/:id', management_get.UserCourse);

router.get('/management/users/course/:id/delete/:code', management_get.UserCourseDelete);

router.get('/management/beacons', management_get.Beacons);

router.get('/management/beacon/delete/:id', management_get.BeaconDelete);

router.get('/management/department', management_get.Department);

router.get('/management/department/delete/:id', management_get.DepartmentDelete);

router.get('/management/course', management_get.Course);

router.get('/management/course/delete/:id', management_get.CourseDelete);

router.get('/management/location/delete/:id', management_get.LocationDelete);

router.get('/logs', management_get.Logs);

/* POST Management Page. */
router.post('/management/users/insert', management_post.UserInsert);

router.post('/management/users/edit/:id', management_post.UserEdit);

router.post('/management/users/course/:id/add', management_post.UserCourseAdd);

router.post('/management/beacon/insert', management_post.BeaconInsert);

router.post('/management/department/insert', management_post.DepartmentInsert);

router.post('/management/course/insert', management_post.CourseInsert);

router.post('/management/location/insert', management_post.LocationInsert);

/* ====================== Application =====================*/
/* POST Application Check in */
router.post('/param/checkin', param.checkin);

/* POST Application Login */
router.post('/param/login', param.login);

/* POST Application PROFESSOR Login */
router.post('/param/plogin', param.plogin);

/* POST Application BaeconList Check */
router.post('/param/beaconchk', param.beaconchk);

/* GET Application BeaconList */
router.get('/param/beaconupdate', param.beaconupdate);

/* POST Application USER TimeSchedule */
router.post('/param/gettimeschedule', param.timeschedule);

/* POST Application USER SuppleTimeSchedule */
router.post('/param/gSTS', param.SuppleTimeSchedule);

/* POST Application PROFESSOR TimeSchedule */
router.post('/param/getptimeschedule', param.ptimeschedule);

/* POST Application PROFESSOR SuppleTimeSchedule */
router.post('/param/gPSTS', param.ProfessorSuppleTimeSchedule);

/* POST Application SUPPLEMENT LESSON GET */
router.post('/param/getsupplelesson', param.getsupplelesson);

module.exports = router;
