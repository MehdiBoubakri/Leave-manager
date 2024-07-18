DROP TABLE IF EXISTS employee;
CREATE  TABLE  employee (
    email varchar(255) ,
    leave_balance int,
    PRIMARY KEY (email)
);
INSERT INTO employee (email, leave_balance) VALUES ('tomas@gmail.com', 20);

