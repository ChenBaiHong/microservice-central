# Docker 19.x 参考-[Swarm 入门教程](https://docs.docker.com/engine/swarm/swarm-tutorial/deploy-service/)

## Swarm 入门

本教程向您介绍Docker Engine Swarm模式的功能。在开始之前，您可能需要熟悉[关键概念](https://docs.docker.com/engine/swarm/key-concepts/)。

本教程将指导您完成以下活动：

* 以群体模式初始化Docker引擎集群
* 向集群添加节点
* 将应用程序服务部署到集群
* 运行起来就管理集群

## 设置

要运行本教程，您需要以下内容：

* 安装了 Docker 的 三台 Linux主机 可以通过网络进行通信
* 安装了Docker Engine 1.12或更高版本
* 主机同网络段 IP地址
* 主机之间打开端口

### 三台联网主机

本教程需要安装 Linux 的三台 Linux 主机，它们可以通过网络进行通信。其中一台机器是管理员（称为manager1），其中两台机器是 worker（worker1和worker2）。

### Docker Engine 1.12或更高版本

本教程在每个主机上都需要Docker Engine 1.12或更高版本。安装Docker Engine，并验证Docker Engine守护程序是否在每台机器上运行

### 管理机器 IP地址

必须将IP地址分配给主机操作系统可用的网络接口。群中的所有节点都需要通过IP地址连接到管理器。

因为其他节点通过其IP地址与管理器节点联系，所以您应该使用固定的IP地址

您可以在 `Linux` 或 `macOS`上运行` ifconfig`来查看可用网络接口的列表。

如果您使用的是 `Docker Machine`，则可以使用 `docker-machine ls`. 或 `docker-machine ip <MACHINE-NAME>`（例如docker-machine ip manager1）获取管理器IP。 本教程使用 manager1：192.168.1.88。

### 主机之间的开放协议和端口

以下端口必须可用。在某些系统上，这些端口默认情况下处于打开状态。

* 用于群集管理通信的TCP端口 `2377`
* TCP 和 UDP 端口 `7946`，用于节点之间的通信
* UDP 端口 `4789`，用于覆盖网络流量
* 如果计划使用加密（--opt encrypted）创建覆盖网络，则还需要确保允许 ip 协议50（ESP）流量。


```shell
$ firewall-cmd --permanent --add-port=2377/tcp
success
$ firewall-cmd --permanent --add-port=7946/tcp
success
$ firewall-cmd --permanent --add-port=4789/tcp
success
$ firewall-cmd --reload
```









