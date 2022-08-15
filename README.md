# Ветклиника #
Программный продукт предназначен для хранения информации о пациентах, докторах, заметках и приемах к врачу,
а также для операций CRUD со всеми перечисленным сущностями.
## Возможности пациента ##
* Регистрация(Поля full name, username, password, date(дата регистрации))
* Авторизация(Поля username, password)
* Просмотр и редактирование аккаунта
* Просмотр своих записей на прием к врачу
* Возможность просматривать и фильтровать врачей по должностям

## Возможности доктора ##
* Авторизация(Поля username, password)
* Просмотр аккаунта
* Просмотр, создание(без пароля), редактирование(без пароля) и удаление аккаунтов пациентов
* Просмотр, создание, редактирование и удаление записей на прием к врачу. У приема могут быть статусы: новый, в процессе, отменен, ожидает оплаты, завершен
* Просмотр, создание, редактирование и удаление папок
* Переходы между папками
* Просмотр(сортировка по по имени пациента и дате последнего обновления(по убыванию)), создание, редактирование и удаление заметок о пациентах в папках
* Фильтрация заметок по пациенту
* Просмотр всех заметок конкретного пациента

## Возможности администратора ##
* Все возможности которые имеет доктор
* Создание докторов(Поля full name, username, password, назначение ролей(doctor, admin), назначение должностей)
* Просмотр, редактирование и удаление докторов
