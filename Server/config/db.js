var mariadb = require('mysql');
var connection = mariadb.createConnection({
        user:'dev',
        password:'1q2w3e4r',
        database:'acs'
});

connection.connect(function(err) {
	if (err) {
		console.error('error connecting : ' + err.stack);
		return;
	}
});

module.exports = connection;
