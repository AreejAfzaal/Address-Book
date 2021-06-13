
create database AddressBook
go
use AddressBook
go

create table Person(
	CNIC varchar(15) NOT NULL PRIMARY KEY,
	Name varchar(20) NOT NULL,
	[Address] varchar(50) not null,
	PhoneNumber varchar(12)
)


