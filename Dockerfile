FROM gradle:jdk21 AS build
WORKDIR /workplace
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon
COPY src ./src
RUN gradle bootJar --no-daemon


FROM openjdk:21-jdk
COPY --from=build /workplace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]