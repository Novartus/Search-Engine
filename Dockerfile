FROM alpine:3.14
LABEL maintainer="Abhee Hudani"
# Copy all files of project
COPY . /usr/search_engine
# Ant Dir Creation
RUN mkdir -p /opt/ant/
# Version: Ant 1.9.8
RUN wget http://archive.apache.org/dist/ant/binaries/apache-ant-1.9.8-bin.tar.gz -P /opt/ant
# Unpack Apache Ant
RUN tar -xvzf /opt/ant/apache-ant-1.9.8-bin.tar.gz -C /opt/ant/
# Remove installtion file
RUN rm -f /opt/ant/apache-ant-1.9.8-bin.tar.gz
# JDK 1.8
RUN apk --update add openjdk8
# Set Ant Env Home in Docker
ENV ANT_HOME=/opt/ant/apache-ant-1.9.8
# Update Path
ENV PATH="${PATH}:${HOME}/bin:${ANT_HOME}/bin"
# Update current working directory
WORKDIR /usr/search_engine
# Run the executable command
CMD ["ant","-noinput","-buildfile", "build.xml", "run"]
