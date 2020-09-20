# Docker 部署 sonerqube 代码审查工具

## 1. 创建主机映射目录

```shell
mkdir /home/data/sonar/conf -p
mkdir /home/data/sonar/logs -p
mkdir /home/data/sonar/extensions -p
```

## 2. Docker 拉取 sonerqube 镜像

```shell
docker pull sonarqube:6.7.5
```

## 3.  创建并运行 sonerqube 服务

```shell
docker run -d --name sonarqube -p 9001:9000 -p 9002:9092  -v /home/data/sonar/conf:/opt/sonarqube/conf  -v /home/data/sonar/data:/opt/sonarqube/data -v /home/data/sonar/logs:/opt/sonarqube/logs -v /home/data/sonar/extensions:/opt/sonarqube/extensions -e 'SONARQUBE_JDBC_USERNAME=root'  -e 'SONARQUBE_JDBC_PASSWORD=baihoo!@#321' -e 'SONARQUBE_JDBC_URL=jdbc:mysql://192.168.1.88:33306/sonarqube?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance&useSSL=false' sonarqube:6.7.5
```

> 数据库 服务器地址，需要改成自己的

## 4 查看启动日志

```shell
docker logs -f sonarqube
```

