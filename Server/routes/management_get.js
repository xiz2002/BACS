var express = require('express');
var _ = require('underscore');
var db = require('../config/db');

// GET - /management/:page
var management_get = {
	Users: function(req, res) {
		db.query('SELECT ID, NAME, STATUS FROM ACS_USERS_TB', function(e, r) {
			if (e) { console.error('Error User Query : ' + e.stack); res.end();}
			else { res.render('management/users/', { data: r}) };
		});
	},

	UserInsert: function(req, res) {
		res.render('management/users/insert');
	},
	
	UserEdit: function(req, res) {
		db.query('SELECT * FROM ACS_USERS_TB WHERE ID = ?', [req.param('id')], function(e, r) {
			if (e) { console.error('Error Users Edit Query : ' + e.stack); res.end(); }
			else { res.render('management/users/edit', { data:r[0] }); }
		});
	},
	
	UserDelete: function(req, res) {
		db.query('UPDATE ACS_USERS_TB SET STATUS=? WHERE ID = ?', [1, req.param('id')], function(e, r) {
			if (e) { console.error('Error Users Delete Query : ' + e.stack); res.end(); }
			else { res.redirect('/management/users/'); }
		});
	},

        UserAccept: function(req, res) {
                db.query('UPDATE ACS_USERS_TB SET STATUS=? WHERE ID = ?', [0, req.param('id')], function(e, r) {
                        if (e) { console.error('Error Users Delete Query : ' + e.stack); res.end(); }
                        else { res.redirect('/management/users/'); }
                });
        },

        UserCourse: function(req, res) {
		var t = req.param('id');
                db.query('SELECT a.CODE as COU_CODE, a.NAME as COU_NAME, b.NAME as DEP_NAME, c.NAME as LOC_NAME, a.DAYS as COU_DAY, a.DS as COU_ST, a.DE as COU_EN FROM ( SELECT a.CODE, a.NAME, a.DEP_ID, a.LOC_CODE, a.DAY as DAYS, a.START as DS, a.END as DE FROM ACS_COURSES_TB a inner join ACS_USERS_SCHEDULE_TB d ON d.COURSE_ID = a.CODE AND d.USER_ID = ? WHERE d.COURSE_ID is NOT NULL ) a, ACS_DEPARTMENTS_TB b, ACS_LOCATIONS_TB c WHERE a.DEP_ID = b.CODE AND a.LOC_CODE = c.CODE ORDER BY a.CODE ASC', [t], function(e, r) {
                        if (e) { console.error('Error User Query : ' + e.stack); res.end();}
                        else { db.query('SELECT a.CODE as CODE, a.NAME as NAME FROM ACS_COURSES_TB a LEFT JOIN ACS_USERS_SCHEDULE_TB b ON b.COURSE_ID = a.CODE AND b.USER_ID = ? WHERE b.COURSE_ID is NULL ORDER BY a.CODE ASC', [t], function (e, r1) {
				if(e) { console.error('Error User Query : ' + e.stack); res.end(); }
				else { res.render('management/users/course', { object: r, id: t, object1: r1}) };
		}	)}
                });
        },

        UserCourseDelete: function(req, res) {
                db.query('DELETE FROM ACS_USERS_SCHEDULE_TB WHERE COURSE_ID = ? AND USER_ID = ?', [req.param('code'),req.param('id')], function(e, r) {
                        if (e) { console.error('Error UCourse Delete Query : ' + e.stack); res.end(); }
                        else { res.redirect('/management/users/course/' + req.param('id')); }
                });
        },
	
	Beacons: function(req, res) {
		db.query('SELECT * FROM ACS_BEACONS_TB', function(e, r) {
			if (e) { console.error('Error Beacon Query : ' + e.stack); res.end();}
			else { 
				db.query('SELECT * FROM ACS_LOCATIONS_TB', function(e, r1) {
					if (e) { console.error('Error Location Query : ' + e.stack); res.end();}
					else { res.render('management/beacons',{object1: r, object2: r1}) };
				});
			}
		});
	},
	
	BeaconDelete: function(req, res) {
		db.query('DELETE FROM ACS_BEACONS_TB WHERE MAC = ?', [req.param('id')], function(e, r) {
			if (e) { console.error(e); res.redirect('/management/beacons'); }
			else { res.redirect('/management/beacons'); }
		});
	},

 	Department: function (req, res) {
		db.query('SELECT * FROM ACS_DEPARTMENTS_TB', function(e,r) {
			if(e) { console.error(e); }
			else { console.log(r); res.render('management/department', {object :r}) }
		});
	},
	
	DepartmentDelete: function (req, res) {
		db.query('DELETE FROM ACS_DEPARTMENTS_TB WHERE CODE = ?', [req.param('id')], function(e, r) {
			if(e) { console.error(e); res.redirect('/management/department'); }
			else { res.redirect('/management/department'); }
		});
	},

	Course: function(req, res) {
		db.query('SELECT * FROM ACS_COURSES_VW', function(e,r) {
			if (e) { console.error(e); }
			else { db.query('SELECT NAME FROM ACS_DEPARTMENTS_TB', function (e, r1) {
				if (e) { console.error(e); }
				else { db.query('SELECT NAME FROM ACS_LOCATIONS_TB', function (e, r2) {
					if(e) { console.error(e); }
					else { res.render('management/courseList', {object0: r, object1: r1, object2: r2}) }
				});
				}
			});
			}
		});
	},

	CourseDelete: function(req, res) {
		db.query('DELETE FROM ACS_COURSES_TB WHERE CODE = ?', [req.param('id')], function(e,r) {
			if(e) { console.error(e); res.redirect('/management/course'); }
			else { res.redirect('/management/course'); } 
		});
	},
//*/
	LocationDelete: function(req, res) {
		db.query('DELETE FROM ACS_LOCATIONS_TB WHERE CODE = ?', [req.param('id')], function(e,r) {
			if(e) { console.error(e); res.redirect('/management/beacons'); }
			else { res.redirect('/management/beacons');}
		});
	},

        Logs: function(req, res) {
                db.query('SELECT * FROM ACS_LOGS_VW ORDER BY DATE DESC', function(e, r) {
        		if (e) { console.error(e);
			} else { res.render('management/logs',{ data: r});
			}
        	});
	}
}

module.exports = management_get;
