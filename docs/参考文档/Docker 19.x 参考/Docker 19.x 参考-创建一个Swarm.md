# Docker 19.x 参考-创建 Swarm

## 1. 创建一个 **Swarm** 集群

完成本教程的设置步骤后，您就可以创建群组了。确保在主机上启动 Docker Engine 守护程序。

1. 打开一个终端，并在要运行管理器节点的机器上SSH。本教程使用一台名为manager1的计算机。如果使用Docker Machine，则可以使用以下命令通过SSH连接到它：

   ```shell
   $ docker-machine ssh manager1
   ```

2. 运行以下命令以创建新的群集：

   ```shell
   $ docker swarm leave --force # 移除初始化的 swarm 集群
   
   $ docker swarm init --advertise-addr <MANAGER-IP>
   ```

   > 注意：如果要使用Mac的Docker桌面或Windows的Docker桌面来测试单节点swarm，只需运行不带参数的docker swarm init。在这种情况下，无需指定--advertise-addr。

   ```shell
   $ docker swarm init --advertise-addr 192.168.1.88
   Swarm initialized: current node (qj3q1ssb64p6uj27dqg9shjip) is now a manager.
   
   To add a worker to this swarm, run the following command:
   
       docker swarm join --token SWMTKN-1-4k9ys0b3liv6101e4co44lgtiw8yitzynh37vxxd6t7fta346m-10txmkq2migv5k4971ukxstz0 192.168.1.88:2377
   
   To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.
   ```

   `--advertise-addr` 标志将管理器节点配置为将其地址发布为192.168.1.88。群中的其他节点必须能够访问该IP地址上的管理器。

   输出包括将新节点加入群集的命令。节点将根据`--token`标志的值作为管理者或工作者加入。

3. 运行 `docker info` 以查看集群的当前状态：

   ```shell
   $ docker info
   
   Containers: 2
   Running: 0
   Paused: 0
   Stopped: 2
     ...snip...
   Swarm: active
     NodeID: dxn1zf6l61qsb1josjja83ngz
     Is Manager: true
     Managers: 1
     Nodes: 1
     ...snip...
   ```

4. 运行 `docker node ls` 命令查看节点信息：

   ```shell
   $ docker node ls
   ID                            HOSTNAME                STATUS              AVAILABILITY        MANAGER STATUS      ENGINE VERSION
   qj3q1ssb64p6uj27dqg9shjip *   localhost.localdomain   Ready               Active              Leader              19.03.12
   ```

   节点ID旁边的 `* ` 表示您当前已连接到该节点。

   

## 2. 在 **swarm** 集群中添加节点

1. 打开终端，并在要运行工作程序节点的计算机中SSH。本教程使用名称worker1。

2. 运行“`docker swarm init`”教程步骤中输出产生的命令，以创建加入现有群的工作节点：

   ```shell
   chenbaihongdeMacBook-Pro:~ chenbaihong$ docker swarm join --token SWMTKN-1-4k9ys0b3liv6101e4co44lgtiw8yitzynh37vxxd6t7fta346m-10txmkq2migv5k4971ukxstz0 192.168.1.88:2377
   This node joined a swarm as a worker.
   ```

   ​		如果没有或忘记了可用的命令，则可以在 管理器 Linux 节点上 运行以下命令以检索工作程序的加入命令：

   ```shell
   $ docker swarm join-token worker
   To add a worker to this swarm, run the following command:
   
       docker swarm join --token SWMTKN-1-4k9ys0b3liv6101e4co44lgtiw8yitzynh37vxxd6t7fta346m-10txmkq2migv5k4971ukxstz0 192.168.1.88:2377
   ```

3. 打开一个终端并SSH进入管理器节点运行的机器，然后运行docker node ls命令查看工作节点：

   ```shell
   $ docker node ls
   ID                            HOSTNAME                STATUS              AVAILABILITY        MANAGER STATUS      ENGINE VERSION
   g8et7v162dprnb43xs8p8u06o     docker-desktop          Ready               Active                                  19.03.5
   qj3q1ssb64p6uj27dqg9shjip *   localhost.localdomain   Ready               Active              Leader              19.03.12
   ```

## 3. 向集群部署服务

1. 在 管理器 Linux 节点上 运行以下命令 (`overlay` 设置为全局覆盖网络 )

   ```shell
   $ docker network create --driver=overlay --attachable overlay-net
   ```

2. 在 管理器 Linux  服务器上 以创建 elasticsearch 集群为例

   ```shell
   $ vim docker-compose.yml
   ```

   ```yaml
   version: '3.5'
   services:
     es01:
       image: docker.elastic.co/elasticsearch/elasticsearch:7.8.0
       container_name: es-node-01
       environment:
         - cluster.name=baimicro-es-cluster
         - node.name=es-node-01
         - node.master=true
         - node.data=true
         - http.cors.enabled=true
         - http.cors.allow-origin=*
         - network.bind_host=0.0.0.0
         - network.publish_host=0.0.0.0
         - discovery.seed_hosts=es-node-01,es-node-02,es-node-03
         - cluster.initial_master_nodes=es-node-01,es-node-02,es-node-03
         - bootstrap.memory_lock=true
         - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
       ulimits:
         memlock:
           soft: -1
           hard: -1
       restart: always
       volumes:
         - /home/data/elasticsearch/logs/es-node-01:/usr/share/elasticsearch/logs
         - /home/data/elasticsearch/data/es-node-01:/usr/share/elasticsearch/data
         - /home/data/elasticsearch/plugins/es-node-01:/usr/share/elasticsearch/plugins
         - /home/data/elasticsearch/config/es-node-01/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml
         - /home/data/elasticsearch/config/es-node-01/elastic-certificates.p12:/usr/share/elasticsearch/config/elastic-certificates.p12
       ports:
         - 9200:9200
       networks:
         - custom_net
   # ...... 省略剩余两个节点配置，陪同与 es01 雷同
   networks:
     custom_net:
       external:
         name: overlay-net
   ```

3. 分别在 manage, worker1, worker2 机器上面执行以下使用 `busybox` 网络驱动程序插件及运行命令

   > ## overlay:
   >
   > docker 自带的跨主机网络模型

   ```shell
   $ docker run -itd --name=mybusybox --network=overlay-net busybox /bin/sh
   ```

4. 部署应用

   在 worker 机器上部署 Skywalking ，调用部署在 manage 机器上的 Elasticserach 为例

   ```shell
   $ vim docker-compose.yml
   ```

   ```yaml
   version: '3.5'
   services:
     # ------------------- skywalking-oap -------------------
     oap:
       image: apache/skywalking-oap-server:8.0.0-es7
       container_name: skywalking-oap
       restart: on-failure
       ports:
       - 11800:11800
       - 12800:12800
       environment:
         SW_STORAGE: elasticsearch7
         SW_STORAGE_ES_CLUSTER_NODES: 192.168.1.88:9200 # 远程服务器主机映射 docker 容器服务的 IP：PORT
         SW_ES_USER: elastic
         SW_ES_PASSWORD: XbneuJCsLxBf6SSz3BKa
         JAVA_OPTS: "-Xms512m -Xmx512m"
         TZ: Asia/Shanghai
       networks:
         - custom_net
     # ------------------- skywalking-ui -------------------
     ui:
       image: apache/skywalking-ui:8.0.0
       container_name: skywalking-ui
       depends_on:
         - oap
       links:
         - oap
       restart: on-failure
       ports:
         - 8080:8080
       environment:
         SW_OAP_ADDRESS: oap:12800
       networks:
         - custom_net
   networks:
     custom_net:
       external: true
       name: overlay-net
   ```

   

