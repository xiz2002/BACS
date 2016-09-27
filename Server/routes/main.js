var express = require('express');
var _ = require('underscore');
var db = require('../config/db');

// GET - /
var main = {
	start: function(req, res) {
		db.query('SELECT MAC, LOC_CODE FROM ACS_BEACONS_TB LIMIT 10', function(e, r) {
			if (e) { console.error('Error Start Query : ' + e.stack); res.end();}
			else { db.query('SELECT USERID, USERNAME, LOCATIONNAME, DATE FROM ACS_LOGS_VW ORDER BY DATE DESC LIMIT 10', function(e, r1) {
					if (e) { console.error('Error STart Query : ' + e.stack); res.end(); }
					else { res.render('index', { data: r, data1: r1}) };
				});
			}
		});
	}
}

module.exports = main;
