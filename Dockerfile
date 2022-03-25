FROM openjdk:8-jdk-alpine:latest
LABEL maintainer="hudanaibhee@gmail.com"
COPY . /usr/search_engine
WORKDIR /usr/search_engine
RUN javac Hello.java
CMD ["java","Hello"]