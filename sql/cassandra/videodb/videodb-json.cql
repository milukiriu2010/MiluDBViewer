/* 
 * This file demonstrates usage of JSON support in Cassandra 2.2 or higher
 * You should first execute the statements in videodb-udts-tuples.cql as some of these
 * statements will depend on types created in that script.  
 */

USE videodb;

INSERT INTO users JSON '{
	"username" : "dchamberlain",
	"firstname" : "Donald",
	"lastname" : "Chamberlain",
	"email" : ["donald.chamberlain@gmail.com", "dchamberlain@gmail.com"],
	"password" : "5f4dcc3b5aa765d61d8327deb882cf99",
	"created_date" : "2014-02-28 08:00:00",
	"address" : {
		"street" : "SQL lane",
		"city" : "Palo Alto",
		"zip" : 94305,
		"location" : [122.0, 123.0],
		"phones" : [
			{ "number" : "408-719-1234", "tags" : ["preferred", "direct line"]}, 
			{ "number" : "408-227-5038", "tags" : ["cell"]}
		]
	}
}';

INSERT INTO users JSON '{
	"username" : "rboyce",
	"firstname" : "Raymond",
	"lastname" : "Boyce",
	"email" : ["ronald.boyce@gmail.com", "rboyce@gmail.com"],
	"password" : "7f4dcc3b5aa765d61d8327deb882cf66",
	"created_date" : "2014-02-28 08:00:00",
	"address" : {
		"street" : "SQL ave",
		"city" : "Palo Alto",
		"zip" : 94305,
		"location" : [40.4240, 86.9290],
		"phones" : [
			{ "number" : "510-831-1234", "tags" : ["preferred", "direct line"]}, 
			{ "number" : "510-408-4321", "tags" : ["cell1"]},
			{ "number" : "703-521-4321", "tags" : ["cell2"]},
			{ "number" : "719-428-2744", "tags" : ["cell3"]}
		]
	}
}';

/*
 * This is an example of using fromJson to convert a 
 * JSON literal into a type that can be compared against
 * a column
 */
UPDATE users 
SET address = fromJson('{
    "street": "3975 Freedom Circle",
    "city": "Santa Clara",
    "zip": 95054,
    "phones": [
        { "number": "650 389 6000", "tags": [ "switchboard" ] }
    ],
    "location" : [37.3872135,-121.9739083]
}')
WHERE username = 'dchamberlain';

/*
 * New JSON style select that returns rows as JSON
 */
SELECT JSON *
FROM users
WHERE username = 'dchamberlain';

/*
 * DevCenter will always put the primary key column
 * on the first line of results if the primary key is part of the
 * selected columns.  If you want other columns to appear on the first 
 * line of each collapsed result, ensure they are specified in the select clause
 * before the primary key.
 * 
 * In this example firstname and lastname precede the primary key(username) so 
 * are displayed before the folding of the rest of each rows results.
 */
SELECT JSON firstname, lastname, username, address
FROM users
WHERE username = 'dchamberlain';

/*
 * toJson results can be selected in the result view and displayed as JSON in the 
 * detailed result view on the right
 */
SELECT toJson(address)
FROM users
WHERE username = 'rboyce';
