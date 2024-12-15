###  Итоговый проект модуля 3 JavaRush (_Опросник по Java_) ###

Чернильцев Илья
### Функционал приложения ###
Приложение представляет собой проверочную викторину (квиз) по Java Core. Пользователю предлагается последовательно отвечать на вопросы, выбирая тот или иной правильный (с точки зрения пользователя) ответ. 
При этом, при каждом ответе, суммируется общий счет правильных ответов. 
В конце викторины выводится суммарный счет и предлагается начать викторину заново.

### Основные задачи: ###

**Реализация опросника:**

- Maven проект с использованием сервлетов, jsp, jstl.
- Для запуска проекта использовался Tomcat 9.
- В сессии хранится счет пользователя.
- Вопросы хранятся в файле в формате JSON, загружаются непосредственно перед началом теста. Возможно увеличение кол-ва вопросов путем простой записи их в данный файл.