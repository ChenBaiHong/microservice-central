# Docker 部署 Redis 集群说明

## 1. 拉取 Redis 镜像

```bash
$ docker pull redis:6.0.5
```

## 2. 查看拉取镜像

```bash
$ docker images
REPOSITORY                                      TAG                             IMAGE ID            CREATED             SIZE
redis                                           6.0.5                           235592615444        5 weeks ago         98.3MB
```

## 3. 集群搭建

### 3.1 创建 redis-cluster 目录

```shell
mkdir /home/data/redis-cluster -p
```

### 3.2. 在`redis-cluster`目录下创建 `redis-cluster.tmpl` 配置文件

```temple
port ${PORT}                                       ##节点端口
protected-mode no                                  ##开启集群模式
cluster-enabled yes                                ##cluster集群模式
cluster-config-file nodes.conf                     ##集群配置名
cluster-node-timeout 5000                          ##超时时间
cluster-announce-ip 192.168.xx.xx                  ##实际为各节点网卡分配ip  先用上网关ip代替
cluster-announce-port ${PORT}                      ##节点映射端口
cluster-announce-bus-port 1${PORT}                 ##节点总线端口
appendonly yes                                     ##持久化模式
```

```shell
cd /home/data/redis-cluster
vim redis-cluster.tmpl
port ${PORT}
protected-mode no
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip 192.168.1.88
cluster-announce-port ${PORT}
cluster-announce-bus-port 1${PORT}
appendonly yes
```

> 备注：此模版文件为集群节点通用文件  其中 ${PORT} 将读取命令行变量  ip则根据网卡分配 ip 进行替换  以保证节点配置文件除端口以及 ip 全部一致。

### 3.3. 创建自定义的 `network`

```shell
$ docker network create redis-net
```

> 备注：创建 redis-net虚拟网卡 目的是让 docker 容器能与宿主（centos7）桥接网络，并间接与外界连接

### 3.4. 查看 redis-net 虚拟网卡网关 IP

```shell
$ docker network inspect redis-net | grep "Gateway" | grep --color=auto -P '(\d{1,3}.){3}\d{1,3}' -o 192.168.1.88
```

























