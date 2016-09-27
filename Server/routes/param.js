var express = require('express');
var db = require('../config/db');

// POST - /param
var param = {
	checkin: function(req, res) {
		var body = req.body;
		db.query('INSERT INTO ACS_ACCESS_LOGS_TB ( USERID, LOC_CODE, COU_ID ) VALUE (?, ?, ?)', [body.ID, body.LOC_CODE, body.COU_ID], function(e) {
			if(e) {
				console.log(e);
				res.write("-1");
			} else {
				console.log('Access');
				res.write("1");
			}
			res.end();
		});
	},

	login: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('SELECT COUNT(*) AS COUNT FROM ACS_USERS_TB WHERE ID = ?', [body.ID], function(e,r,f) {
			if(e) throw e;
			console.log(r[0].COUNT);
			if(r[0].COUNT == 0) { console.log('ID FAIL'); res.write("-1"); res.end(); }
			else {
				db.query('SELECT STATUS FROM ACS_USERS_TB WHERE ID = ?', [body.ID], function(e,r,f) {
					if(e) throw e;
					console.log(r[0].STATUS);
					if(r[0].STATUS != 0) { console.log('STATUS FAIL'); res.write("-2"); res.end(); }
					else {
						db.query('SELECT COUNT(*) AS COUNT FROM ACS_USERS_TB WHERE ID = ? AND PASSWD = ?', [body.ID, body.PASSWD], function(e,r,f) {
							if(e) throw e;
							console.log(r[0].COUNT);
							if(r[0].COUNT == 0) { console.log('PASSWD FAIL'); res.write("-1"); res.end(); }
							else{ res.write("1"); res.end(); }
						});
					}
				});
			}
		});
	},
        
	plogin: function(req, res) {
                var body = req.body;
                console.log(body);
                db.query('SELECT COUNT(*) AS COUNT FROM ACS_PROFESSORS_TB WHERE ID = ?', [body.ID], function(e,r,f) {
                        if(e) throw e;
                        console.log(r[0].COUNT);
                        if(r[0].COUNT == 0) { console.log('ID FAIL'); res.write("-1"); res.end(); }
                        else {
                                db.query('SELECT STATUS FROM ACS_PROFESSORS_TB WHERE ID = ?', [body.ID], function(e,r,f) {
                                        if(e) throw e;
                                        console.log(r[0].STATUS);
                                        if(r[0].STATUS != 0) { console.log('STATUS FAIL'); res.write("-2"); res.end(); }
                                        else {
                                                db.query('SELECT COUNT(*) AS COUNT FROM ACS_PROFESSORS_TB WHERE ID = ? AND PASSWD = ?', [body.ID, body.PASSWD], function(e,r,f) {
                                                        if(e) throw e;
                                                        console.log(r[0].COUNT);
                                                        if(r[0].COUNT == 0) { console.log('PASSWD FAIL'); res.write("-1"); res.end(); }
                                                        else{ res.write("1"); res.end(); }
                                                });
                                        }
                                });
                        }
                });
        },

	beaconchk: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('SELECT VERSION FROM ACS_VERSION_TB', function(e, r) {
			if(e) throw e;
			console.log(r[0].VERSION);
			if(r[0].VERSION == body.VERSION) {
				console.log('SUCCESS'); res.write("-1"); res.end();
			} else { 
				var i = r[0].VERSION
				console.log('MISSMATCH'); res.write(i.toString()); res.end();
			}
		});
	},

	beaconupdate: function(req, res) {
		db.query('SELECT * FROM ACS_BEACONS_TB', function(e, r) {
			if(e) throw e;
			console.log('UPDATE');
			res.send(r);
		});
	},

	timeschedule: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('SELECT VERSIOND FROM ACS_USERS_TB WHERE ID = ?', [body.ID], function(e,r) {
			if(e) throw e;
			console.log(r);
			if(r[0].VERSIOND != body.VERSION) {
				db.query('SELECT * FROM ACS_USERS_COURSE_VW WHERE USER_ID = ?', [body.ID], function(e,ra) {
					if(e) throw e;
					console.log(ra);
					console.log('SUCCESS'); res.send({object1: r, object2: ra});
				});
			} else {
				res.write("1"); res.end();
			}				
		});
	},

	SuppleTimeSchedule: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('SELECT b.NAME, a.COURSE_ID, a.LOC_ID, a.DAY, a.START, a.END FROM (SELECT a.COURSE_ID, a.LOC_ID, a.DAY, a.START, a.END FROM ACS_SUPPLE_COURSES_TB a INNER JOIN ACS_USERS_SCHEDULE_TB b ON a.COURSE_ID = b.COURSE_ID AND b.USER_ID = ?) a, ACS_COURSES_TB b WHERE a.COURSE_ID = b.CODE', [body.ID], function(e,r) {
			res.send(r);	
		});
	},
	
	//*/
        ptimeschedule: function(req, res) {
                var body = req.body;
                console.log(body);
                db.query('SELECT VERSIOND FROM ACS_PROFESSORS_TB WHERE ID = ?', [body.ID], function(e,r) {
                        if(e) throw e;
                        console.log(r);
                        if(r[0].VERSIOND != body.VERSION) {
                                db.query('SELECT * FROM ACS_PROFESSORS_COURSE_VW WHERE PROF_ID = ?', [body.ID], function(e,ra) {
                                        if(e) throw e;
                                        console.log(ra);
                                        console.log('SUCCESS'); res.send({object1: r, object2: ra});
                                });
                        } else {
                                res.write("1"); res.end();
                        }
                });
        },

	ProfessorSuppleTimeSchedule: function(req, res) {
                var body = req.body;
                console.log(body);
		db.query('SELECT b.NAME, a.COURSE_ID, a.LOC_ID, a.DAY, a.START, a.END FROM ACS_SUPPLE_COURSES_TB a, ACS_COURSES_TB b WHERE a.PROFESSOR_ID = ? AND a.COURSE_ID = b.CODE', [body.ID], function(e, r) {
                        res.send(r);
                });
        },
		
//*/
	getsupplelesson: function(req, res) {
		var body = req.body;
		console.log(body);
		db.query('SELECT MAX(ID) AS MAX FROM ACS_SUPPLE_COURSES_TB', function(e, r) {
			if(e) throw e;
			console.log(r);
			var i = r[0].MAX + 1;
			console.log(i);
			db.query('INSERT INTO ACS_SUPPLE_COURSES_TB (ID, PROFESSOR_ID, COURSE_ID, LOC_ID, DAY, START, END, CONTENT ) VALUE (?, ?, ?, ?, ?, ?, ?, ?)', [i, body.PROFESSOR_ID, body.COURSE_ID, body.LOC_ID, body.DAY, body.START, body.END, body.CONTENT], function (e) {
				if(e) {
					console.error(e);
				} else {
					res.write("1"); res.end();
				}
			});
		})
	}
}

module.exports = param;
