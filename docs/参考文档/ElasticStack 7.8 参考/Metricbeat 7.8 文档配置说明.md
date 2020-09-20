# [安装 Metricbeat](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-installation.html)

## 在 Docker上运行 Metricbeat

### 拉取 Metricbeat 镜像

```bash
docker pull docker.elastic.co/beats/metricbeat:7.8.0
```

### 运行启动 Metricbeat

使用 setup 命令运行 Metricbeat 将创建索引模式并加载可视化、仪表板和机器学习作业。运行此命令：

```bash
docker run \
docker.elastic.co/beats/metricbeat:7.8.0 \
setup -E setup.kibana.host=kibana:5601 \
-E output.elasticsearch.hosts=["elasticsearch:9200"] # 1⃣️ 2⃣️ 
```

​		1⃣️ 替换Kibana和Elasticsearch主机和端口。

​		2⃣️ 如果您在ElasticCloud中使用托管的 Elasticsearch 服务，请替换`-E output.elasticsearch.hosts`使用以下语法使用云ID和弹性密码行：

```bash
-E cloud.id=<Cloud ID from Elasticsearch Service> \
-E cloud.auth=elastic:<elastic password>
```

### 在 Docker上 配置 Metricbeat

Docker映像提供了几种配置 Metricbeat 的方法。传统的方法是通过 卷 装载来提供配置文件，但也可以创建包含配置的自定义映像。

### 示例配置文件

下载此示例配置文件作为起点：

```bash
curl -L -O https://raw.githubusercontent.com/elastic/beats/7.8/deploy/docker/metricbeat.docker.yml
```

#### Volume-mounted 配置

在Docker上配置Metricbeat的一种方法是提供 `metricbeat.docker.yml` 通过卷安装。使用 docker run，可以这样指定卷挂载。

```bash
docker run -d \
  --name=metricbeat \
  --user=root \
  --volume="$(pwd)/metricbeat.docker.yml:/usr/share/metricbeat/metricbeat.yml:ro" \
  --volume="/var/run/docker.sock:/var/run/docker.sock:ro" \
  --volume="/sys/fs/cgroup:/hostfs/sys/fs/cgroup:ro" \
  --volume="/proc:/hostfs/proc:ro" \
  --volume="/:/hostfs:ro" \
  docker.elastic.co/beats/metricbeat:7.8.0 metricbeat -e \
  -E output.elasticsearch.hosts=["elasticsearch:9200"]  # 1⃣️ 2⃣️
```

​		1⃣️ 替换您的Elasticsearch主机和端口。

​		2⃣️ 如果要在Elastic Cloud中使用托管的Elasticsearch Service，请使用前面显示的语法用Cloud ID和Elastic Password替换`-E output.elasticsearch.hosts` 就行。

### 监控主机

在容器中执行 Metricbeat 时，如果要监视主机或其他容器，需要注意一些重要事项。让我们来看一些使用Docker作为我们的容器编排工具的示例。

本示例重点介绍了使系统模块在容器内正常运行所需的更改。这使 Metricbeat 可以从容器内部监视主机。

```bash
docker run \
  --mount type=bind,source=/proc,target=/hostfs/proc,readonly \ # 1⃣️
  --mount type=bind,source=/sys/fs/cgroup,target=/hostfs/sys/fs/cgroup,readonly \ # 2⃣️
  --mount type=bind,source=/,target=/hostfs,readonly \ # 3⃣️
  --net=host \ # 4⃣️
  docker.elastic.co/beats/metricbeat:7.8.0 -e -system.hostfs=/hostfs
```

* 1⃣️ Metricbeat 的 [系统模块](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-module-system.html) 通过 Linux proc文件系统收集其大部分数据，该文件系统通常位于 `/proc`。因为容器尽可能地与主机隔离，容器的 `/proc` 中的数据与主机的 `/proc` 中的数据不同。为此，您可以在容器中装入主机的 `/proc` 文件系统，并告诉 Metricbeat 在查找 `/proc` 时查看 `/hostfs` 目录 `-system.hostfs=/hostfs` CLI标志。

* 2⃣️ 默认情况下，为 [系统进程 metricset](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-metricset-system-process.html) 启用 cgroup 报告，因此您需要在容器中装载主机的 cgroup 装入点。它们需要安装在 `-system.hostfs` CLI标志指定的目录中。
* 3⃣️ 如果您希望能够通过使用 [系统文件系统指标集](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-metricset-system-filesystem.html) 来监视来自主机的文件系统，则需要将这些文件系统安装在容器内部。它们可以安装在任何位置。
* 4⃣️ [系统网络指标集](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-metricset-system-network.html) 在运行时使用来自 `/proc/net/dev` 或 `/hostfs/proc/net/dev` 的数据 `-system.hostfs=/hostfs` . 使该文件包含主机的网络设备的唯一方法是使用 `--net=host` 标志。这是由于Linux名称空间的缘故；仅仅将主机的 `/proc` 绑定到 `/hostfs/proc`是不够的。

> 提示：特殊文件系统 `/proc` 和 `/sys` 仅在主机系统运行 Linux 时可用。在 Windows 和 MacOS 上尝试绑定安装这些文件系统将失败。

如果在 Linux 上使用 [系统套接字指标集](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-metricset-system-socket.html) ，则需要向 Metricbeat 授予更多权限。这个 metricset 从` /proc` 读取文件，这些文件是其他用户拥有的内部对象的接口。默认情况下，在 Docker上，读取所有这些文件所需的功能（`sys_ptrace` 和`dac_read_search`）被禁用。要授予这些权限，还需要这些标志：

```bash
--user root --cap-add sys_ptrace --cap-add dac_read_search
```

