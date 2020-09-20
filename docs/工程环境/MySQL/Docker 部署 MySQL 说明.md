# Docker 安装 MySQL

## 1. Docker 安装 MySQL

### 1.1 参考地址

[www.runoob.com](https://www.runoob.com/docker/docker-install-mysql.html)

### 1.2 拉取 MySQL 镜像

```shell
$ docker pull mysql:5.7.22
```

### 1.3  Docker 运行 MySQL 实例
```bash
$ docker run -itd --name docker-mysql -p 33306:3306  --network=host --restart=always -v '/home/data/mysql:/var/lib/mysql' -e MYSQL_ROOT_PASSWORD='baihoo!@#321' mysql:5.7.22 --lower_case_table_names=1
```

* 参数说明

```
--restart=always 跟随docker启动
--privileged=true 容器root用户享有主机root用户权限
-v 映射主机路径到容器
-e MYSQL_ROOT_PASSWORD=root 设置root用户密码
-d 后台启动
--lower_case_table_names=1 设置表名参数名等忽略大小写
–network=host 将本机ip映射到外网
```



### 1.4 MySQL 慢查询监控

#### 1.4.1 相关参数

* slow_query_log：0表示禁用日志，1表示启用日志
* slow_query_log_file：指定日志文件的保存路径和名称
* long_query_time：指定多少秒返回查询为慢查询，可精确到微秒
* log_slow_admin_statements：表示记录administrative语句，如alter table,analyze table等
* log_queries_not_using_indexes：表示记录所有未使用索引的查询
* log_throttle_queries_not_using_indexes：指定每分钟记录到日志的未使用索引查询的数量，超过此数量的只记录语句数量和花费总时间
* log_slow_slave_statements：表示记录从库上的慢查询
* min_examined_row_limit：表示查询的行数少于此数的查询不会记录到日志
* log_timestamps：表示慢日志的时间戳的时区
* log_output：表示日志的输出格式，可以是TABLE, FILE, and NONE，默认为 FILE

#### 1.4.2 慢日志判定

服务器按以下顺序使用控制参数来确定是否将查询写入慢查询日志

1. 检查查询是否为管理语句，如果是则必须启用 log_slow_admin_statements
2. 查询时常需要超过 long_query_time的配置，或者启用了 log_queries_not_using_indexes ，并且查询未使用索引
3. 如设定了min_examined_row_limit，则查询必须至少检查过指定数量的行数
4. 如设置了log_throttle_queries_not_using_indexes，则未使用索引查询数量必须小于其设定值

> 提示：默认情况下，复制从属服务器不会将复制查询写入慢速查询日志。要更改此设置，需要启用 log_slow_slave_statements 系统变量。

#### 1.4.3 慢查询日志启用

进入 docker mysql 容器内 并登陆进去 MySQL

```bash
docker exec -it docker-mysql /bin/bash

mysql -uroot -p
```

查看原始状态，默认是关闭状态

```mysql
show variables like 'slow_query%';
```

查看慢查询 long_query_time 设定时长

```mysql
show variables like 'long_query_time';
```

使用全局变量配置

```mysql
set global slow_query_log='ON'; # 设置开启慢查询

set global long_query_time=1; 	# 设置超过1秒就记录日志
```

#### 1.4.4 使用配置文件设置 

1. 修改 `conf.d/mysql.cnf` 文件，加入相关配置

```bash
vim /etc/mysql/mysql.conf.d/mysql.cnf
```

```properties
# For explanations see
# http://dev.mysql.com/doc/mysql/en/server-system-variables.html

[mysqld]
pid-file        = /var/run/mysqld/mysqld.pid
socket          = /var/run/mysqld/mysqld.sock
datadir         = /var/lib/mysql
#log-error      = /var/log/mysql/error.log
# By default we only accept connections from localhost
#bind-address   = 127.0.0.1
# Disabling symbolic-links is recommended to prevent assorted security risks
symbolic-links=0
#是否开启慢查询日志
slow_query_log=ON
#日志存放地址
slow_query_log_file=/var/log/mysql/mysql-slow.log
#慢查询时间(s) 
long_query_time=0.1
```

2. 退出 docker mysql 

```bash
exit
```

### 1.4 在宿主机中拷贝出 Docker MySQL 的配置目录、日志目录

* 配置目录 

```bash
 mkdir etc && docker cp docker-mysql:/etc/mysql ./etc && mv ./etc/mysql/* ./etc && rm -rf ./etc/mysql
```

* 日志目录

```bash
 mkdir logs && docker cp docker-mysql:/var/log/mysql ./logs && mv ./logs/mysql/* ./logs && rm -rf ./logs/mysql
```

* 创建 mysql-slow.log 文件 并授予读写权限

```bash
touch mysql-slow.log && chmod 0777 mysql-slow.log
```

### 1.5 停止并移除 docker mysql 容器

```bash
docker stop docker-mysql 

docker rm docker-mysql
```

## 2. Docker-compose 安装 MySQL 并结合综上所述配置

### 2.1 创建 docker-compose.yml

```shell
# docker-compose.yml
version: '3.5'
services:
  mysql-server:
    container_name: docker-mysql
    image: mysql:5.7.22
    restart: always
    ports:
      - 33306:3306
    volumes:
      - /home/data/mysql/etc:/etc/mysql 			# mysql 配置目录
      - /home/data/mysql/lib:/var/lib/mysql		# mysql 存储目录
      - /home/data/mysql/logs:/var/log/mysql	# mysql 日志目录
    command: mysqld --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --init-connect='SET NAMES utf8mb4;' --innodb-flush-log-at-trx-commit=0 --lower_case_table_names=1
    environment:
      MYSQL_ROOT_PASSWORD: "baihoo!@#321"
```

### 2.2 执行以下命令进入 MySQL 容器

```shell
 $ docker container exec -it docker-mysql bash
```

#### 2.2.1 进入容器内部 mysql 服务

```shell
$ mysql -uroot -p
```

#### 2.2.2 设置 mysql 远程连接

```shell
$ ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'baihoo!@#321';
```

#### 2.2.3 刷新权限

```shell
$ flush privileges;
```

### 2.2 运行以下命令启动

```shell
 $ docker-compose up -d # 后台启动
```

2.3 Mysql创建用户并授权

```mysql
create user ArtLangdon identified by 'Art.Langdon123!';
```

2.4 授权用户指定数据库操作权限

```mysql
grant all privileges on `micro-platform`.* to ArtLangdon@'%' identified by 'Art.Langdon123!';

grant all privileges on `logger-center`.* to ArtLangdon@'%' identified by 'Art.Langdon123!';

grant all privileges on `file-center`.* to ArtLangdon@'%' identified by 'Art.Langdon123!';

grant all privileges on `oauth-center`.* to ArtLangdon@'%' identified by 'Art.Langdon123!';

grant all privileges on `tx-logger`.* to ArtLangdon@'%' identified by 'Art.Langdon123!';

grant all privileges on `tx-manager`.* to ArtLangdon@'%' identified by 'Art.Langdon123!';

flush privileges;
```

## 3 安装 代码审查工具 sonerqube

```shell
docker run -d --name sonarqube -p 9001:9000 -p 9002:9092  -v /home/data/sonar/conf:/opt/sonarqube/conf  -v /home/data/sonar/data:/opt/sonarqube/data -v /home/data/sonar/logs:/opt/sonarqube/logs -v /home/data/sonar/extensions:/opt/sonarqube/extensions -e 'SONARQUBE_JDBC_USERNAME=root'  -e 'SONARQUBE_JDBC_PASSWORD=baihoo!@#321' -e 'SONARQUBE_JDBC_URL=jdbc:mysql://114.116.34.169:33306/sonarqube?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&useConfigs=maxPerformance&useSSL=false' sonarqube:6.7.5
```

