# Pull OpenJDK, it uses Oracle Linux
FROM openjdk:11

# Update the environment and perform apt installations
RUN apt-get update -y \
    && apt-get install -y maven \
    && apt-get clean

# create a directory for app
WORKDIR /swim-nlng

# Copy project directory into the container at /dropwizard-template
COPY  . /swim-nlng

# Generate fat jar (uber jar)
RUN mvn package

# Metric and healthchecks
EXPOSE 5031 
# Application
EXPOSE 5030

# Production FAT jar
CMD java -jar target/swim-nlng-2.0.0.jar server settings_prod.yml