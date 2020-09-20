# Docker 部署 Elasticsearch 7.8 单机说明

参考地址

  > [Docker —— 从入门到实践](https://yeasy.gitbook.io/docker_practice/)
  >
  > [官方运行 docker 安装 Elastic Stack](https://www.elastic.co/guide/en/elastic-stack-get-started/current/get-started-docker.html)
  >
  > [官方运行 docker 安装 Kibana](https://www.elastic.co/guide/en/kibana/7.3/docker.html#docker)

## 1 Docker 安装 Elasticserach 前提配置

> 注意：在 取决于您的平台下， `vm.max_map_count` 内核设置至少需要设置为 262144 才能用于生产。

### Linux 虚拟机内核设置

>`vm.max_map_count` 设置应在 `/etc/sysctl.conf` 中永久设置

```bash
$ grep vm.max_map_count /etc/sysctl.conf
vm.max_map_count=262144
```

> 要在实时系统上应用设置，请键入

```shell
$ sysctl-w vm.max_map_count=262144
```

### MacOS  虚拟机内核设置

>必须在 xhyve虚拟机中设置 ` vm.max_map_count` 设置

```bash
$ screen ~/Library/Containers/com.docker.docker/Data/vms/0/tty
```

> 只需按 Enter键，然后像对 Linux 那样配置 sysctl 设置

```bash
$ grep vm.max_map_count /etc/sysctl.conf
vm.max_map_count=262144
```

### Windows 虚拟机内核设置

> 注意： 你的安装 `docker-box`，才有 ` docker-machine` 命令
>
> 必须通过 `docker-machine` 设置 `vm.max_map_count` 设置：

```bash
docker-machine ssh
sudo sysctl -w vm.max_map_count=262144
```

* 命令

  > 从命令行以开发者模式运行 ElasticSearch，以开发者模式 ，使用以下命令可以快速启动 ElasticSearch 进行开发 或 测试

  ```bash
  docker run -d --name elasticSearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.3.1
  ```

* 实例

  ```bash
  PS C:\Program Files\Docker Toolbox> docker run -d --name elasticSearch -p 9200:9200 -p 9300:9300 -e "discovery.type=sing
  le-node" elasticsearch:7.3.1
  5fe05ddcbe9e3554196a3f89eeda65100eee940dff2c085bd0e1d74aa56a9e02
  ```

  * > elasticSearch 已经安装完成,我们进入容器配置配置跨域。由于要进行配置，因此需要进入容器当中修改相应的配置信息

      ```bash
      docker exec -it elasticSearch /bin/bash 
      ```

      > 实例:
      >
      > ```bash
      > PS C:\Program Files\Docker Toolbox> docker exec -it elasticSearch /bin/bash
      > [root@5fe05ddcbe9e elasticsearch]#
      > [root@5fe05ddcbe9e elasticsearch]#
      > ```
      >
      > 加入以下配置
      >
      > ```bash
      > [root@5fe05ddcbe9e elasticsearch]# vi config/elasticsearch.yml
      > ```
      >
      > ```yaml
      > http.cors.enabled: true 
      > http.cors.allow-origin: "*"
      > ```



## 2 Docker-compose 安装 Elasticserach

### 2.1 编写 docker-compose.yml 文件

```yaml
version: '3.5'
services:
  # ------------------- elasticsearch -------------------
  elasticsearch:
    image: elasticsearch:7.5.0
    container_name: elasticsearch
    environment:
      ES_JAVA_OPTS: "-Xms512m -Xmx512m"
      #集群名称
      cluster.name: "baihealth-es-cluster"
      #本节点名称
      node.name: "elasticsearch"
      #是否master节点
      node.master: "true"
      #是否存储数据
      node.data: "true"
      #跨域设置
      http.cors.enabled: "true"
      http.cors.allow-origin: "*"
      # 对客户端 http 访问端口
      http.port: 9200
      # 对客户端 tcp 转接发布端口
      transport.tcp.port: 9300
      #可以访问es集群的ip  0.0.0.0表示不绑定
      network.bind_host: 0.0.0.0
      #es集群相互通信的ip  0.0.0.0默认本地网络搜索
      network.publish_host: 0.0.0.0
      #7.x 配置集群节点地址，多个地址逗号隔开
      discovery.seed_hosts: "elasticsearch"
      #7.x 配置集群节点，多个地址逗号隔开
      cluster.initial_master_nodes: "elasticsearch"
    ulimits:
      nofile:
        soft: 65536
        hard: 65536
    restart: always
    # volumes 配置由（:）分隔的三个字段组成，<卷名>:<容器路径>:<选项列表>
    # <卷名> ： 第一个字段，主机上的文件或目录
    # <容器路径> ： 第二个字段，容器中的文件或目录
    # <选项列表> ： 第三个字段，可选，且用逗号分隔，如：ro，consistent，delegated，cached，z 和 Z
    volumes:
    - /home/data/elasticsearch/logs:/usr/share/elasticsearch/logs
    - /home/data/elasticsearch/data:/usr/share/elasticsearch/data
    ports:
    - 9200:9200
    - 9300:9300
    networks:
    - elastic
  # ------------------- kibana -------------------
  kibana:
    image: kibana:7.5.0
    container_name: kibana
    ports:
    - 5601:5601
    links:
    - elasticsearch:elasticsearch
    depends_on:
    - elasticsearch
    environment:
      ELASTICSEARCH_URL: http://elasticsearch:9200
networks:
  elastic:
    driver: bridge

```

### 2.2 宿主机与容器数据卷目录授权

> 注意：主要 Linux 系统下 docker 安装 elasticsearch 需要对主机数据卷映射目录授权，否则 docker中启动elasticsearch 会报错 （Error opening log file 'logs/gc.log': Permission denied）

```shell
$ chmod 777 /home/data/elasticsearch/logs
$ chmod 777 /home/data/elasticsearch/data
$ chmod 777 /home/data/elasticsearch/plugins
```

### 2.3 在 docker-compose.yml 文件所在目录运行以下命令

```shell
$ docker-compose up -d # 后台启动
```

### 2.4 查看 elasticSearch 是否启动成功

```shell
$ curl http://127.0.0.1:9200/_cat/health
```

### 2.5 进入 elasticSearch 容器内 bash 页面

```shell
 $ docker exec -it elasticSearch /bin/bash 
```



