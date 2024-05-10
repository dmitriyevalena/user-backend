INSERT into address (id, country, city, zip_code, street)
VALUES (1, 'Ukraine', 'Dnipro', '12345', 'Darvina')
     , (2, 'Ireland', 'Donegol', '67890', 'Killaghtee');

	
INSERT into local_user (email, first_name, last_name, birth_date, address_id, phone_number)
VALUES ('UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', '2004-09-01', 1, '123456')
     , ('UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', '2004-10-01', 2, null)
     , ('UserC@junit.com', 'UserC-FirstName', 'UserC-LastName', '2004-11-01', null, null);



