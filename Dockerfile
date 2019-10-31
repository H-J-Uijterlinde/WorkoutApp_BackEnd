FROM hypriot/rpi-java
ADD build/libs/semaforce-workout-app-0.1.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app.jar"]