# Дипломный проект профессии «Тестировщик ПО»

## Документация

### [План автоматизации тестирования](../main/docs/Plan.md)

## Инструкция по запуску SUT и авто-тестов

### Предусловия

1. Для запуска проекта и тестов на локальной машине потребуются установленные 
Git, JDK 11, IntelliJ IDEA, Docker, Docker Compose.
3. Запустить Docker Desktop
2. Склонировать репозиторий на локальную машину командой в Git:

   `git clone https://github.com/BrainLucker/qa-diploma.git`

4. Запустить IntelliJ IDEA и открыть проект.

### 1. Запуск контейнеров

Для старта контейнеров с БД (MySQL, PostgreSQL), а также симулятором банковских сервисов,
выполнить в терминале из корня проекта команду: `docker-compose up`.
Дождаться пока поднимутся БД (вывод логов запуска прекратится).

### 2. Запуск SUT

В новой вкладке терминала из корня проекта выполнить одну из команд:

* С подключением к MySQL:

  `java -jar artifacts\aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app`

* С подключением к PostgreSQL:

  `java -jar artifacts\aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`


### 3. Запуск авто-тестов

#### Все тестовые классы

В консоли «Run Anything» (двойное нажатие Ctrl) выполнить одну из команд в зависимости от выбранной БД в п. 2.

* С подключением к MySQL:

  `./gradlew clean test -Ddb.url=jdbc:mysql://localhost:3306/app`

* С подключением к PostgreSQL:

  `./gradlew clean test -Ddb.url=jdbc:postgresql://localhost:5432/app`

#### Отдельный тестовый класс

Запустить необходимый тестовый класс можно командой в консоли, указав его имя, например:

`./gradlew clean test --tests BuyTourCreditPageTest`...

Либо запустить его с помощью кнопки «Run» в IDEA.

#### Настройка БД по-умолчанию

Выставить по-умолчанию необходимую БД для выполнения тестов можно, раскомментировав соответствующую строку
в файле [build.gradle](../main/build.gradle).

### 4. Формирование отчета AllureReport

В консоли «Run Anything» (двойное нажатие Ctrl) выполнить команду: `./gradlew allureServe`.
Отчет сгенерируется и автоматически откроется в браузере. По окончании работы с отчетом необходимо 
завершить выполнение процесса allureServe, нажав Ctrl + F2 во вкладке «Run» терминала.