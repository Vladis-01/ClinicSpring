insert into doctors(id, username, password, full_name)
    values (1, 'admin', '$2a$12$XeBGoQyVcuY/cSlNtStG6.GxBIW5haD.G9dr8IYvQic7JFQ8fTrRm', 'admin'); /* password: admin */

insert into doctor_roles(user_id, role)
    values (1, 'ADMIN');