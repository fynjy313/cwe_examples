--wallets table
INSERT INTO Wallets (username, email, password, cash) VALUES
('admin', 'admin@cwe.exp', '21232f297a57a5a743894a0e4a801fc3', 99999),
('user', 'user@cwe.exp', 'ee11cbb19052e40b07aac0ca060c23ee', 50000),
('vasya', 'vasya@cwe.exp', 'a127c4fdad3080541ec6acc0d8acd704', 20000),
('petya', 'petya@cwe.exp', 'a173e8ab419cb6d9b3f3400b9552766f', 10000);

--products table
INSERT INTO Products (name, quantity, price) VALUES
('juice', 14, 10.50),
('potato', 23, 2.30),
('beer', 7, 5.99);