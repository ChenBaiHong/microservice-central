FROM node:8.5-alpine
MAINTAINER ChenBaiHong <chen.baihoo@gmail.com>

RUN npm install gitbook-cli -g

ARG GITBOOK_VERSION=3.2.3
RUN gitbook fetch $GITBOOK_VERSION

ENV BOOKDIR /gitbook

VOLUME $BOOKDIR

EXPOSE 4000

WORKDIR $BOOKDIR

CMD ["gitbook", "--help"]
