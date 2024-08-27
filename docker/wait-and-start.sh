#!/bin/sh
set -e

# Задержка перед запуском приложения
sleep 30

# Запуск приложение
exec java -jar /app/task-mgt-system-0.0.1-SNAPSHOT.jar
