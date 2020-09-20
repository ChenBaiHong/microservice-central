# Docker - 入门到实践

参考地址

  > https://yeasy.gitbooks.io/docker_practice/

## 1 Docker 镜像容器增删查

### 1.1 Docker 删除镜像注意

```text
注意点：

1. 删除镜像前需要确保容器是停止的  stop

2. 删除镜像和容器的命令是的不一样的，需要注意。 docker rmi ID  ,其中 容器(rm)  和 镜像(rmi)

3. 注意顺序需要 先删除容器，再是镜像
```
### 1.2 Docker 查看容器
```bash
$ docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
ddb0f4cefa2c        redis               "docker-entrypoint.sh"   8 minutes ago       Up 8 minutes        6379/tcp            myredis
```

### 1.3 Docker 删除容器通过 CONTAINER ID
```bash
docker rm ddb0f4cefa2c
```

### 1.4 Docker 查看镜像
```bash
$ docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
redis               latest              f7302e4ab3a8        2 weeks ago         98.18 MB
```

### 1.5 Docker 删除镜像通过IMAGE ID
 ```bash
 $ docker rmi f7302e4ab3a8
 ```

## 2 docker-compose 网络配置

### 2.1 查看创建的网络

```shell
# 创建统一网络
$ docker network ls
```

### 2.2 创建自定义同一网络

```shell
$ docker network create app_net
```

### 2.3 yaml 文件配置如下

#### 2.3.1 示例 mysql 如下

```yaml
version: '3'
services:
 	# 其他容器服务端调用的同一网络（union network）下的数据库地址，例如：mysql-server:3306 即 数据库容器IP:端口
  mysql-server:
    container_name: docker-mysql1
    image: mysql:5.7.22
    # expose 33306 to client (navicat)
    ports:
      - 33306:3306
    volumes:
      # change './docker/mysql/volume' to your own path
      - "/data/mysql/datasource:/var/lib/mysql"
    command: mysqld --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --init-connect='SET NAMES utf8mb4;' --innodb-flush-log-at-trx-commit=0 --lower_case_table_names=1
    environment:
      MYSQL_ROOT_PASSWORD: "root123456"
    networks:
    # 调用自定义网络
    - custom_net
networks:
  # 容器内声明自定义网络
  custom_net: 
   # docker 外部创建的网络，本文【2.2】处所创建声明的
   external:
      name: app_net
```

#### 2.3.2 示例 tomcat 如下

```yaml
version: '3'
services:
  app-service:
    image: tomcat:9.0
    environment:
      TZ: Asia/Shanghai
    restart: always
    ports:
      - 8080:8080
    volumes:
    # volumes 配置由（:）分隔的三个字段组成，<卷名>:<容器路径>:<选项列表>
    # <卷名> ： 第一个字段，主机上的文件或目录
    # <容器路径> ： 第二个字段，容器中的文件或目录
    # <选项列表> ： 第三个字段，可选，且用逗号分隔，如：ro，consistent，delegated，cached，z 和 Z
    - /data/app-tomcat-9.0/webapps:/usr/local/tomcat/webapps
    - /data/app-tomcat-9.0/logs:/usr/local/tomcat/logs
    networks:
    # 调用自定义网络
    - custom_net
networks:
	# 容器内声明自定义网络
  custom_net:
  # docker 外部创建的网络，本文【2.2】处所创建声明的
    external:
      name: app_net
```

#### 2.3.3 示例 nginx 如下

```shell
$ docker run -p 80:80 -p 443:443 --name nginx -v '/data/nginx/html:/usr/share/nginx/html' -v '/data/nginx/logs:/var/log/nginx' -d nginx:stable
```

```shell
# 将容器内的配置文件拷贝到宿主机指定目录
$ docker container cp nginx:/etc/nginx /data/docker/nginx/
$ ls /data/docker/nginx/
nginx

$ cd /data/docker/nginx/
$ mv nginx conf

$ docker stop nginx
$ docker rm nginx
```

使用 docker-compose 部署启动

```yaml
version: '3'
services:
  nginx:
    image: nginx:stable
    container_name: nginx
    restart: always
    ports:
      - 10080:80
      - 10443:443
    volumes:
      - /data/docker/nginx/conf:/etc/nginx
      - /data/docker/nginx/logs:/var/log/nginx
      - /data/docker/nginx/html:/usr/share/nginx/html
    networks:
    # 调用自定义网络
    - custom_net
networks:
  # 容器内声明自定义网络
  custom_net:
  # docker 外部创建的网络，本文【2.2】处所创建声明的
    external:
      name: app_net
```

* 注意

  nginx 挂载后的配置文件,原容器中有一个 default.conf 文件 ,作用于 监听 nginx 的启动页，这里映射后 把 default.conf 覆盖了，故需 /data/nginx-1.14/conf 下创建 custom.conf ，然后重启容器

```json
#api接口转发custom.conf
server {
    server_name xx.xxxx.com
    underscores_in_headers on;
    index index.html;
    location / {
        add_header 'Access-Control-Allow-Origin' '*';
        proxy_pass http://app-service:8080/;
        #try_files $uri $uri/ /index.html;
    }
}
```

* 提示：所以上述docker-compose.yml中声明服务，nginx 中proxy.conf配置的app-service:8080,相当于通过tomcat的容器ip，
  调用tomcat的8080端口，简单的将yml中的服务名称，理解为，加入同一网络后的"host"即可，这样理解起来更方便直观一点，
  如tomcat中调用mysql服务，spring配置可以用

  ```xml
  <property name="url" value="jdbc:mysql://mysql-service:3306/finance?useUnicode=true&amp;characterEncoding=UTF-8"/>
  ```

