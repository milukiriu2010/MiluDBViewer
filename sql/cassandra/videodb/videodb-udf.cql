/*
 * This file contains examples of creating and dropping User Defined Functions(UDFs) and Aggregate Functions,
 * both of which are supported in Cassandra 2.2 and higher.  
 * 
 * The statements in this script leverage the videodb schema.  It also depends on several types
 * and data created in scripts that should be run prior to this one:
 * 1.  videodb-udts-tuples.cql, then 
 * 2.  videodb-json.cql
 */
 
use videodb;

/*
 * A simple example of creating a function that adds two numbers
 */
CREATE OR REPLACE FUNCTION adder(first double, second double) 
RETURNS NULL ON NULL INPUT 
RETURNS double   
LANGUAGE JAVA 
AS 'return first + second;';


/*
 * Simple example of a function showing how you can work with User Types in your 
 * UDFs. 
 */
CREATE OR REPLACE FUNCTION num_phones(address address) 
CALLED ON NULL INPUT
RETURNS int 
LANGUAGE JAVA 
AS 
'
//If "called on null input", handle nulls
if(address == null) return 0;


//User types are converted to com.datastax.driver.core.UDTValue types
java.util.Set phones =  address.getSet("phones", com.datastax.driver.core.UDTValue.class);

return phones.size();
';

/*
 * Leverage the function just created to get the number of phone
 * numbers per user.
 */
select num_phones(address) from users;

/*
 * This is similar example of how you can work with User Types in your 
 * UDFs.  This will also be used as an accumulator state function later.
 */
CREATE OR REPLACE FUNCTION num_phones_accumulator(previous_total int, address address) 
CALLED ON NULL INPUT
RETURNS int 
LANGUAGE JAVA 
AS 
'
//If "called on null input", handle nulls
if(address == null) return previous_total + 0;


//User types are converted to com.datastax.driver.core.UDTValue types
java.util.Set phones =  address.getSet("phones", com.datastax.driver.core.UDTValue.class);

return previous_total + phones.size();
';


/*
 * Leverage the accumulator function in an aggregate function.
 * This aggregate function takes in a set of addresses, uses
 * the num_phones_accumulator state function to accumulate the total
 * count of phone numbers, then returns the total count.
 */
CREATE OR REPLACE AGGREGATE phone_number_count(address) 
SFUNC num_phones_accumulator
STYPE int
INITCOND 0
;

/*
 * Now get the total count of phone numbers from the users table
 */
select phone_number_count(address) as phone_number_total from users;


/*
 * Now lets create a UDF that will help us calculate the average number
 * of phone numbers users have
 *
 */
 
/*
 * NOTE: This version works with Cassandra 2.2 only
 */
CREATE OR REPLACE FUNCTION num_phones_and_user_count_accumulator(
    total_and_count tuple<int, int>, 
    address address
) 
RETURNS NULL ON NULL INPUT 
RETURNS tuple<int, int>
LANGUAGE JAVA 
AS 
'
if(total_and_count == null){
    total_and_count = com.datastax.driver.core.TupleType.of(
                        com.datastax.driver.core.DataType.cint(),
                        com.datastax.driver.core.DataType.cint()).newValue();
    total_and_count.setInt(0, 0);
    total_and_count.setInt(1, 0);
}

//User types are converted to com.datastax.driver.core.UDTValue types
java.util.Set phones =  address.getSet("phones", com.datastax.driver.core.UDTValue.class);

total_and_count.setInt(0, total_and_count.getInt(0) + phones.size());
total_and_count.setInt(1, total_and_count.getInt(1) + 1);

return total_and_count;
';

/*
 * NOTE: This version works with Cassandra 3.0 and higher only
 */
CREATE OR REPLACE FUNCTION num_phones_and_user_count_accumulator(
    total_and_count tuple<int, int>, 
    address address
) 
RETURNS NULL ON NULL INPUT 
RETURNS tuple<int, int>
LANGUAGE JAVA 
AS 
'
if(total_and_count == null){
    total_and_count = com.datastax.driver.core.TupleType.of(
                        com.datastax.driver.core.ProtocolVersion.NEWEST_SUPPORTED,
                        com.datastax.driver.core.CodecRegistry.DEFAULT_INSTANCE,
                        com.datastax.driver.core.DataType.cint(),
                        com.datastax.driver.core.DataType.cint()).newValue();
    total_and_count.setInt(0, 0);
    total_and_count.setInt(1, 0);
}

//User types are converted to com.datastax.driver.core.UDTValue types
java.util.Set phones =  address.getSet("phones", com.datastax.driver.core.UDTValue.class);

total_and_count.setInt(0, total_and_count.getInt(0) + phones.size());
total_and_count.setInt(1, total_and_count.getInt(1) + 1);

return total_and_count;
';

/*
 * Next is a function to produce an average that will take a tuple of <int, int>
 * that will assume the first int is a sum, and the 
 * second is assumed to be the total count assumed to be this sum.
 */
CREATE OR REPLACE FUNCTION avg_with_tuples(total_and_count tuple<int, int>) 
CALLED ON NULL INPUT
RETURNS double 
LANGUAGE JAVA 
AS 'return total_and_count.getInt(0) / (double)total_and_count.getInt(1);';

/*
 * Now create an aggregate that will accumulate the the total number of phone
 * numbers and the count of users the phone numbers came from.  Then using a 
 * final function(FINALFUNC) produce an average number of phone numbers. 
 */
CREATE OR REPLACE AGGREGATE avg_num_phone_numbers(address) 
SFUNC num_phones_and_user_count_accumulator
STYPE tuple<int, int> 
FINALFUNC avg_with_tuples
INITCOND (0, 0)
;

/*
 * And finally use the aggregate function.
 */
select avg_num_phone_numbers(address) from users;

/*
 * Drop aggregate statement examples.
 */
DROP AGGREGATE phone_number_count;

/*
 * Create a function that by name alone is ambiguous
 * since there is another function with the signature adder(double, double)
 */
CREATE OR REPLACE FUNCTION adder(first int, second int)
RETURNS NULL ON NULL INPUT 
RETURNS int
LANGUAGE JAVA
AS 'return first + second;';

/*
 * This statement should be flagged with a validation error
 * stating that 'adder' alone is ambiguous
 */
DROP FUNCTION adder;

/*
 * This drop statement on the other hand is unambiguous
 */
DROP FUNCTION adder(int, int);
 
