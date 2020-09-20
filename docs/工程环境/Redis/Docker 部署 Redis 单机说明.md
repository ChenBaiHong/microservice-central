## 1. Doceker 安装 Redis

### 1.1 拉取 Redis 镜像

```bash
$ docker pull redis:6.0.5
```

### 1.2 查看拉取是否成功

```bash
$ docker images
REPOSITORY                                      TAG                             IMAGE ID            CREATED             SIZE
redis                                           6.0.5                           235592615444        5 weeks ago         98.3MB
```

### 1.3 启动镜像做映射

#### 1.3.1 [从官网下载](http://download.redis.io/redis-stable/redis.conf) <font style='color:red'>redis.conf</font> 文件

> redis.conf 配置文件放在 `/home/data/redis/`目录下

#### 1.3.2 修改默认配置

```json
# daemonize no               	# 默认是 no ， 改为 yes；开启后台进程守护
protected-mode no 						# 默认 yes 表示开启保护模式，用来限制 redis 受保护只能本地访问
bind 127.0.0.1                # 注释掉本地绑定
dir "/data/"                  # 设置 rdb 数据存放的目录
logfile "/data/redis.log"     # 设置日志路径
appendonly yes                # 开启AOF，持久化数据
requirepass "baihoo!@#321"    # 设置密码
```

> 注意：daemonize yes 千万要注释掉，不然起不来 docker 容器

### 1.3  编写 docker-compose.yml 文件启动 redis 实例

#### docker-compose.yml

```yaml
version: '3.5'
services:
  redis:
    image: redis:6.0.5
    container_name: docker-redis
    restart: always
    ports:
      - 6379:6379
    volumes:
      - /home/data/redis/data:/data
      - /home/data/redis/redis.conf:/etc/redis/redis.conf
    command: redis-server /etc/redis/redis.conf
    privileged: true # 给予授权访问主机读写权限
```

> privileged: true 给予授权访问主机读写权限并没有成功

```shell
chmod 0777 /home/data/redis/*
```

### 1.4 docker 启动 redis 实例命令

````shell
$ docker run --restart=always --log-driver json-file --log-opt max-size=100m --log-opt max-file=2 -p 6379:6379 --name docker-redis -v /home/data/redis/redis.conf:/etc/redis/redis.conf -v /home/data/redis/data:/data -d redis:6.0.5 redis-server /etc/redis/redis.conf
````

#### docker 启动 redis 内容解释说明

```yaml
# 端口映射，前表示主机端口:后表示容器端口
-p 6379:6379 

# 指定容器名称，查看和进行操作都比较方便
--name myredis  

#将主机中/opt/data/redis目录下的redis挂载到容器的/data
-v /home/data/redis/data:/data 

##将主机中redis.conf配置文件挂载到容器的/etc/redis/redis.conf文件中
-v /home/data/redis/redis.conf:/etc/redis/redis.conf 

 # 表示后台启动redis
-d redis

# 以配置文件启动 redis，加载容器内的 conf 文件，最终找到的是挂载的目录 /home/data/redis/redis.conf
redis-server /etc/redis/redis.conf

# 开启redis 持久化
--appendonly yes
```

## 2 Docker 查看运行的进程

```bash
$ docker ps
```

### 2.1 Docker 进入容器内命令

```shell
$ docker exec -it docker-redis sh
```

### 2.2 Docker 关闭当前运行的 redis 进程实例

```bash
$ docker stop docker-redis
```

### 2.3 Docker  重启当前 redis 实例

 ```bash
$ docker restart docker-redis
 ```

> 注意：myredis 是 redis 的实例名称。启动redis实例时，我们使用 --name 指定的

### 2.4 Docker 杀死 redis 进程

```bash
$ docker kill -s KILL ef9d654914f9
```

> 注意：ef9d654914f9 表示当前 redis 实例的容器ID

### 2.5 Docker 移除 redis 容器实例

```bash
$ docker rm -f -v ef9d654914f9
```

### 2.6 Docker 连接redis客户端实例

```shell
$ docker exec -it docker-redis redis-cli
$ docker exec -ti 5f4c4cf5a5f5 redis-cli
$ docker exec -ti 5f4c4cf5a5f5 redis-cli a "redis passwd"
$ docker exec -ti 5f4c4cf5a5f5 redis-cli -h localhost -p 6379
$ docker exec -ti 5f4c4cf5a5f5 redis-cli -h 127.0.0.1 -p 6379
$ docker exec -ti 5f4c4cf5a5f5 redis-cli -h 172.17.0.3 -p 6379
```

