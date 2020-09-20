# Elasticsearch 7.8 参考

## 监控集群-使用 Metricbeat 收集监视数据（[Collecting monitoring data with Metricbeat](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/configuring-metricbeat.html)）

### 使用 Metricbeat 收集监视数据

在6.5及更高版本中，可以使用 `Metricbeat` 收集有关 `Elasticsearch` 的数据并将其发送到监视集群，而不是按照旧版收集方法中所述通过导出器进行路由。

![Example monitoring architecture](imgetricbeat.png)

1. 启用监视数据收集

   在生产集群上将 `xpack.monitoring.collection.enabled` 设置为 `true`。默认情况下，它是禁用的（false）。

   您可以使用以下API来查看和更改此设置：

   ```bash
   GET _cluster/settings
   
   PUT _cluster/settings
   {
     "persistent": {
       "xpack.monitoring.collection.enabled": true
     }
   }
   ```

   如果启用了 `Elasticsearch` 安全功能，则必须具有监视集群特权才能查看集群设置并管理集群特权才能更改它们。

   有关更多信息，请参阅 [Monitoring settings](https://www.elastic.co/guide/en/elasticsearch/reference/current/monitoring-settings.html) 和 [Cluster update settings](https://www.elastic.co/guide/en/elasticsearch/reference/current/cluster-update-settings.html)。

2. 在生产集群中的每个 `Elasticsearch` 节点上 [安装 Metricbeat](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-installation.html) 。

3. 在每个 `Elasticsearch` 节点上的 `Metricbeat` 中启用 `Elasticsearch X-Pack` 模块。

   例如，要在 `modules.d `目录中启用默认配置，请运行以下命令：

   ```bash
   metricbeat modules enable elasticsearch-xpack
   ```

   有关更多信息，请参阅 [Specify which modules to run](https://www.elastic.co/guide/en/beats/metricbeat/7.8/configuration-metricbeat.html) 和 [Elasticsearch module](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-module-elasticsearch.html)。

4. 在每个 Elasticsearch 节点上的 Metricbeat 中配置 Elasticsearch X-Pack 模块。 `modules.d/elasticsearch-xpack.yml` 文件包含以下设置：

   ```yml
     - module: elasticsearch
       metricsets:
         - ccr
         - cluster_stats
         - index
         - index_recovery
         - index_summary
         - ml_job
         - node_stats
         - shard
         - enrich
       period: 10s
       hosts: ["http://localhost:9200"]
       #username: "user"
       #password: "secret"
       xpack.enabled: true
   ```

   默认情况下，该模块从 http://localhost:9200 收集Elasticsearch监控指标。如果该主机和端口号不正确，则必须更新 `hosts` 设置。如果您将 `Elasticsearch` 配置为使用加密的通信，则必须通过 HTTPS 访问它。例如，使用主机 `hosts` ，例如 https://localhost:9200。

   如果启用了弹性安全功能，则还必须提供用户标识和密码，以便.Metricbeat 可以成功收集指标

   1. 在具有 [`remote_monitoring_collector` 内置角色](https://www.elastic.co/guide/en/elasticsearch/reference/current/built-in-roles.html) 的生产集群上创建一个用户。或者，使用 [`remote_monitoring_user` 内置用户](https://www.elastic.co/guide/en/elasticsearch/reference/current/built-in-users.html)。
   2. 将用户名和密码设置添加到 Elasticsearch 模块配置文件中。

5. 可选：在 Metricbeat 中禁用系统模块

   默认情况下，[system module](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-module-system.html) 处于启用状态。但是，它收集的信息不会显示在 Kibana 的“监视”页面上。除非您希望将该信息用于其他目的，否则请运行以下命令：

   ```shell
   metricbeat modules disable system
   ```

6. 确定将监视数据发送到哪里。

   > 在生产环境中，我们强烈建议使用单独的群集（称为监视群集）来存储数据。使用单独的监视集群可防止生产集群 中断影响您访问监视数据的能力。它还可以防止监视活动影响生产集群的性能

   例如，在 Metricbeat 配置文件（metricbeat.yml）中指定 Elasticsearch 输出信息：

   ```yaml
     # Array of hosts to connect to.
     hosts: ["http://es-mon-1:9200", "http://es-mon2:9200"] 
   
     # Optional protocol and basic auth credentials.
     #protocol: "https"
     #username: "elastic"
     #password: "changeme"
   ```

   > 在此示例中，数据存储在具有节点 `es-mon-1`和 `es-mon-2` 的监视群集上。

   如果将监视群集配置为使用加密的通信，则必须通过 HTTPS 访问它。例如，使用主机设置，例如 https://es-mon-1:9200。

   > 注意⚠️：Elasticsearch监视功能使用接收管道，因此存储监视数据的集群必须至少具有一个  [接收节点](https://www.elastic.co/guide/en/elasticsearch/reference/current/ingest.html)

   如果在监视群集上启用了 Elasticsearch 安全功能，则必须提供有效的 用户ID 和 密码，以便 Metricbeat 可以成功发送指标：

   1. 在具有[`remote_monitoring_agent` 内置角色](https://www.elastic.co/guide/en/elasticsearch/reference/current/built-in-roles.html)的监视群集上创建一个用户。或者，使用 [`remote_monitoring_user` 内置用户](https://www.elastic.co/guide/en/elasticsearch/reference/current/built-in-users.html)。
   2. 将`username`和`password`设置添加到 Metricbeat 配置文件中的 Elasticsearch 输出信息中

   有关这些配置选项的更多信息，请参阅配置  [Configure the Elasticsearch output](https://www.elastic.co/guide/en/beats/metricbeat/7.8/elasticsearch-output.html).。

7. 在每个节点上 [启动 Metricbeat](https://www.elastic.co/guide/en/beats/metricbeat/7.8/metricbeat-starting.html) 。

8. 禁用Elasticsearch监视指标的默认集合。

   在生产集群上将 `xpack.monitoring.elasticsearch.collection.enabled`设置为 `false`

   您可以使用以下API更改此设置：

   ```bash
   PUT _cluster/settings
   {
     "persistent": {
       "xpack.monitoring.elasticsearch.collection.enabled": false
     }
   }
   ```

   如果启用了Elasticsearch安全功能，则必须具有监视集群特权才能查看集群设置并管理集群特权才能更改它们。

9. [在Kibana中查看监视数据](https://www.elastic.co/guide/en/kibana/7.8/monitoring-data.html)。

   > 注意⚠️：确保正在监视当前选择的主机。除非主节点中的metricbeat正在发送数据，否则Kibana中的“监视”页面将不会显示生产集群指标。

