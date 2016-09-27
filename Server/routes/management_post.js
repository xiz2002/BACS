var express = require('express');
var crypto = require('crypto');
var shasum = crypto.createHash('sha1');
var db = require('../config/db');

// POST - /management/:page
var management_post = {
	UserInsert: function(req, res, next) {
		var body = req.body;
		console.log(body);
		shasum.update(body.PASSWD);
		var passwd = shasum.digest('base64');
		console.log(passwd);
		db.query('INSERT INTO ACS_USERS_TB (ID, NAME, PASSWD, ACTSTATUS ) VALUE (?, ?, ?, ?)', [body.USERID, body.USERNAME, passwd, 0], function (e) {
			if(e) {
				console.error('Error User Insert Query : ' + e.stack);
				res.redirect('/management/users/');
			} else {
				res.redirect('/management/users/');
			}
		});
	},
	
        UserEdit: function(req, res, next) {
                var body = req.body;
		shasum.update(body.PASSWD);
		var passwd = shasum.digest('base64');
                db.query('UPDATE ACS_USERS_TB SET NAME=?, PASSWD=? WHERE USERID=?', [body.USERNAME, passwd, body.USERID], function (e) {
                        if(e) {
                                console.error('Error User Edit Query : ' + e.stack);
				res.redirect('/management/users/');
                        } else {
                                res.redirect('/management/users/');
                        }
                });
        },
	
	UserCourseAdd: function(req, res, next) {
		var body = req.body;
		var t = req.param('id');
		db.query('INSERT INTO ACS_USERS_SCHEDULE_TB (USER_ID, COURSE_ID) VALUE (?, ?)', [t, body.CODE], function(e) {
			if(e) {
				console.error('Error UCourse Add Query : ' + e.stack);
				res.redirect('/management/users/course/' + t);
			} else {
				res.redirect('/management/users/course/' + t);
			}
		});
	},

	BeaconInsert: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('INSERT INTO ACS_BEACONS_TB (MAC, LOC_CODE) VALUE (?, ?)', [body.MAC, body.LOCATION], function(e) {
			if(e) {
				console.error('Error Beacon Insert Query : ' + e.stack);
				res.redirect('/management/beacons/');
			} else {
				res.redirect('/management/beacons/');
			}
		});
	},

	DepartmentInsert: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('INSERT INTO ACS_DEPARTMENTS_TB (CODE, NAME) VALUE (?, ?)', [body.CODE, body.NAME], function(e) {
			if(e) {
				console.error('Error Department Insert Query : ' +  e.stcak);
				res.redirect('/management/department');
			} else {
				res.redirect('/management/department');
			}
		});
	},

	CourseInsert: function(req, res) {
		var body = req.body;
		var tName = 'AT_'+body.CODE+'_TB'; 
		tName = tName.replace(/\W/,'');
		console.log(body);
		console.log(tName);
		db.query('INSERT INTO ACS_COURSES_TB VALUE (?, ?, (SELECT CODE FROM ACS_DEPARTMENTS_TB WHERE NAME = ?), (SELECT CODE FROM ACS_LOCATIONS_TB WHERE NAME = ?), ?, ?, ?, ?)', [body.CODE, body.NAME, body.DEP, body.LOC, body.DAY, body.ST, body.ET, tName], function(e) {
			if(e) {	console.error('Error Course Insert Query : ' + e.stack); res.redirect('/management/course'); }
			else { db.query('CREATE TABLE `AT_?_TB` ( `USERNAME` VARCHAR(20) )', [body.CODE.replace(/\W/,'')], function(e) {
				if(e) { console.error('Error Course Create Table Query : ' + e.stack); res.redirect('/management/course'); }
				else { res.redirect('/management/course'); }
				});
			}
		});
	},

	LocationInsert: function(req, res) {
		var body = req.body; console.log(body); db.query('INSERT INTO ACS_LOCATIONS_TB (CODE, NAME) VALUE (?, ?)', [body.CODE, body.NAME], function(e) {
			if(e) {
				console.error('Error Location Insert Query : ' + e.stack);
				res.redirect('/management/beacons/');
			} else {
				res.redirect('/management/beacons/');
			}
		});
	}
}

module.exports = management_post;
