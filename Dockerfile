# Используем официальный образ JDK 17
#FROM openjdk:17-jdk-slim
FROM maven:3.8.5-openjdk-17
# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл JAR в контейнер
COPY target/ShowImages-0.0.1-SNAPSHOT.jar app.jar

# Устанавливаем порт, на котором работает приложение
EXPOSE 8080

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]