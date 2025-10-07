FROM gradle:jdk21 AS build
RUN mkdir /workplace
WORKDIR /workplace
COPY build.gradle /workplace
COPY settings.gradle /workplace
COPY src /workplace/src
RUN gradle bootJar


FROM openjdk:21-jdk
COPY --from=build /workplace/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]