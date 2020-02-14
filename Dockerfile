FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.6_10_openj9-0.18.1-alpine-slim

RUN apk add --no-cache \
      chromium \
      nss \
      freetype \
      freetype-dev \
      harfbuzz \
      ca-certificates \
      ttf-freefont \
      nodejs \
      npm

ENV PUPPETEER_SKIP_CHROMIUM_DOWNLOAD=true \
    PUPPETEER_EXECUTABLE_PATH=/usr/bin/chromium-browser

RUN npm install -g bpmn-to-image

COPY build/libs/bpmn-image-server-*-all.jar bpmn-image-server.jar

EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar bpmn-image-server.jar