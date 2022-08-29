create sequence hibernate_sequence start 2 increment 1;

create table appointment_status (
    appointment_id int8 not null,
    status varchar(255)
);

create table appointments (
    id int8 not null,
    date_appointment timestamp,
    description varchar(255),
    price float8,
    doctor_id int8,
    patient_id int8,
    primary key (id)
);

create table doctor_position (
    doctor_id int8 not null,
    position varchar(255)
);

create table doctor_roles (
    user_id int8 not null,
    role varchar(255)
);

create table doctors (
    id int8 not null,
    full_name varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
);

create table files (
    id int8 not null,
    creation_date timestamp,
    name varchar(255),
    text varchar(255),
    update_date timestamp,
    folder_id int8,
    patient_id int8,
    primary key (id)
);

create table folders (
    id int8 not null,
    name varchar(255),
    path varchar(255),
    folder_id int8,
    primary key (id)
);

create table patient_roles (
    user_id int8 not null,
    role varchar(255)
);

create table patients (
    id int8 not null,
    date_registration timestamp,
    full_name varchar(255),
    password varchar(255),
    username varchar(255),
    primary key (id)
);

alter table if exists appointment_status
    add constraint appointment_status_appointment_fk
    foreign key (appointment_id)
    references appointments on delete cascade;

alter table if exists appointments
    add constraint appointments_doctor_fk
    foreign key (doctor_id)
    references doctors;

alter table if exists appointments
    add constraint appointments_patient_fk
    foreign key (patient_id)
    references patients;

alter table if exists doctor_position
    add constraint doctor_position_doctor_fk
    foreign key (doctor_id)
    references doctors on delete cascade;

alter table if exists doctor_roles
    add constraint doctor_roles_user_fk
    foreign key (user_id)
    references doctors on delete cascade;

alter table if exists files
    add constraint files_folder_fk
    foreign key (folder_id)
    references folders;

alter table if exists files
    add constraint files_patient_fk
    foreign key (patient_id)
    references patients;

alter table if exists folders
    add constraint folders_folder_fk
    foreign key (folder_id)
    references folders;

alter table if exists patient_roles
    add constraint patient_roles_user_fk
    foreign key (user_id)
    references patients on delete cascade;