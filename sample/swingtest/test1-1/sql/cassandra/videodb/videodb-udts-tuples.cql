// This file demonstrates usage of user-defined types (UDT's) and tuples.
// These features require Cassandra 2.1 or higher.
// For more information about user-defined types, see
// https://www.datastax.com/documentation/cql/3.1/cql/cql_using/cqlUseUDT.html
// WARNING: The data model presented below is designed for demonstration purposes only
// and should not be considered as a best practice in real-world situations.

USE videodb;

// Create a new user-defined type named 'phone'

CREATE TYPE phone (
    number text,
    tags set<text>
);
  
// Create a new user-defined type named 'address', if it does not exist yet
// (note the 'IF NOT EXISTS' syntax).
// Note that UDT's can reference other UDT's, as in the 'phones' field.
// Also note that the field 'location' is a tuple of two floating-point coordinates.
// Finally, note the 'frozen' keyword that qualifies references to UDT's and tuples.
// Currently, Cassandra stores values having multiple fields in a serialized form, much like blobs. 
// This means that, for now, these values are "frozen" and it is only possible to update them as a whole. 
// However, future versions of Cassandra will introduce other forms of storage for UDT's and tuples, 
// and partial updates of specific fields will become possible.
// To ensure compatibility with these future versions, a column definition of a 
// user-defined or tuple type currently requires the 'frozen' keyword. 

CREATE TYPE IF NOT EXISTS address (
    street text,
    city text,
    zip int,
    phones set<frozen<phone>>,
    location frozen<tuple<float,float>>
);

// Alter table 'users', add new column 'address' of type 'address'.
// Note the 'frozen' keyword again.

ALTER TABLE users ADD address frozen<address>;

// Insert new user with an address.

INSERT INTO users (username, firstname, lastname, email, password, created_date, address) 
VALUES (
    'tsmith', 
    'Tom', 
    'Smith', 
    ['tsmith@gmail.com', 'tom.smith@gmail.com'],
    '5f4dcc3b5aa765d61d8327deb882cf99',
    '2014-02-28 08:00:00',
    {
        street: '202 4th St',
        city: 'San Francisco',
        zip: 94103,
        phones: {
            { number: '212 221 9165', tags: { 'preferred', 'direct line' } },
            { number: '500 310 2342', tags: { 'fax' } }
        },
        location: (37.783205,-122.4026532)
    }
);

// Update the user, modify the address. 
// Note that it is currently not possible to
// update only specific fields of an UDT or a tuple.

UPDATE users 
SET address = {
    street: '3975 Freedom Circle',
    city: 'Santa Clara',
    zip: 95054,
    phones: {
        { number: '650 389 6000', tags: { 'switchboard' } }
    },
    location: (37.3872135,-121.9739083)
}
WHERE username = 'tsmith';

// Select on a specific user. 
// Result set will contain the entire address in JSON-like format.

SELECT address
FROM users
WHERE username = 'tsmith';

// Select on a specific user. 
// Note that, while it's currently not possible to update only
// portions of an UDT value, it is possible to retrieve only some of its fields.
// Result set will contain only the desired fields of the 'address' UDT.

SELECT 
    address.city,    // text
    address.phones,  // set<frozen<phone>>
    address.location // frozen<tuple<float,float>>
FROM users
WHERE username = 'tsmith';
