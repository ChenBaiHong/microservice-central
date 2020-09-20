# Elasticsearch 7.8 参考

## 监控集群-使用 Filebeat 收集日志数据（[Collecting log data with Filebeat](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/configuring-filebeat.html)）

您可以使用 Filebeat 监视 Elasticsearch 日志文件，收集日志事件，并将它们发送到监视集群。您最近的日志可以在Kibana 的监控页面上看到。

1. 验证 Elasticsearch 是否正在运行，并且监视群集是否准备好从 Filebeat 接收数据。

   > 提示：在生产环境中，我们强烈建议使用单独的集群（称为监控集群）来存储数据。使用单独的监视群集可防止生产群集停机影响您访问监视数据的能力。它还可以防止监视活动影响生产集群的性能。请参见[*Monitoring in a production environment*](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/monitoring-production.html)。

2. 启用集群上监视数据的收集。

   在生产集群上将 `xpack.monitoring.collection.enabled` 设置为 `true`。默认情况下，它是禁用的（false）。

   您可以使用以下API来查看和更改此设置：

   ```json
   GET _cluster/settings
   
   PUT _cluster/settings
   {
     "persistent": {
       "xpack.monitoring.collection.enabled": true
     }
   }
   
   ```

   如果启用了Elasticsearch安全功能，则必须具有监视集群权限才能查看集群设置，并具有管理集群权限才能更改这些设置。

   有关更多信息，请参阅 [监视设置](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/monitoring-settings.html) 和 [集群更新设置](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/cluster-update-settings.html)。

3. 确定要监视的日志。

   Filebeat Elasticsearch 模块可以处理 [audit logs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/audit-log-output.html), [deprecation logs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/logging.html#deprecation-logging), [gc logs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/gc-logging.html), [server logs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/logging.html), 和 [slow logs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/index-modules-slowlog.html)。有关Elasticsearch日志位置的更多信息，请参阅[path.logs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/path-settings.html) 设置。

   > 重点：如果同时存在结构化（* .json）和非结构化（纯文本）版本的日志，则必须使用结构化日志。否则，它们可能不会出现在Kibana的适当上下文中。

4. 在包含要监视的日志的 Elasticsearch 节点上安装 [安装 Filebeat](https://www.elastic.co/guide/en/beats/filebeat/7.8/filebeat-installation.html) 。

5. 标识发送日志数据的位。

   例如，在 Filebeat 配置文件（filebeat.yml）中为您的监视集群指定 Elasticsearch 输出信息：

   ```yaml
   output.elasticsearch:
     # Array of hosts to connect to.
     hosts: ["http://es-mon-1:9200", "http://es-mon2:9200"] # 1⃣️
   
     # Optional protocol and basic auth credentials.
     #protocol: "https"
     #username: "elastic"
     #password: "changeme"
   ```

   ​		1⃣️ 在此示例中，数据存储在具有节点 `es-mon-1` 和 `es-mon-2` 的监视集群上。

   如果将监视群集配置为使用加密的通信，则必须通过 HTTPS 访问它。例如，使用主机设置，例如 https://es-mon-1:9200。

   > 重点：Elasticsearch监视功能使用接收管道，因此存储监视数据的集群上必须至少具有一个[接收节点](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/ingest.html)。

   如果在监视集群上启用了 Elasticsearch 安全功能，则必须提供有效的用户ID和密码，以便 Filebeat 能够成功发送度量。

   有关这些配置选项的详细信息，请参 [阅配置 Elasticsearch 输出](https://www.elastic.co/guide/en/beats/filebeat/7.8/elasticsearch-output.html)。

6. 可选：确定数据可视化的位置。

   Filebeat 提供了示例 Kibana 仪表板，可视化和搜索。要将仪表板加载到适当的Kibana实例中，请在每个节点上的 Filebeat 配置文件（filebeat.yml）中指定 setup.kibana 信息：

   ```yaml
   setup.kibana:
     host: "localhost:5601"
     #username: "my_kibana_user"
     #password: "YOUR_PASSWORD"
   ```

   > 提示：在生产环境中，我们强烈建议为您的监控集群使用一个专用的 Kibana 实例。

   如果启用了安全功能，则必须提供有效的用户ID和密码，以便 Filebeat 可以连接到Kibana：

   1. 在监视群集上创建一个具有[`kibana_admin` 内置角色](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/built-in-roles.html) 或等效权限的用户。
   2. 将用户名和密码设置添加到Filebeat配置文件中的Elasticsearch输出信息中。该示例显示了一个硬编码的密码，但是您应该在 [secrets keystore](https://www.elastic.co/guide/en/beats/filebeat/7.8/keystore.html) 中存储敏感值。

   详情参阅 [配置Kibana端点](https://www.elastic.co/guide/en/beats/filebeat/7.8/setup-kibana-endpoint.html)。

7. 启用Elasticsearch模块并在每个节点上设置初始Filebeat环境。

   例如：

   ```sh
   filebeat modules enable elasticsearch
   filebeat setup -e
   ```

   有关更多信息，请参阅 [Elasticsearch module](https://www.elastic.co/guide/en/beats/filebeat/7.8/filebeat-module-elasticsearch.html)。

8. 在每个节点上的 Filebeat 中配置Elasticsearch模块。

   如果要监视的日志不在默认位置，请在 `modules.d/elasticsearch.yml` 文件中设置适当的路径变量。请参阅[Configure the Elasticsearch module](https://www.elastic.co/guide/en/beats/filebeat/7.8/filebeat-module-elasticsearch.html#configuring-elasticsearch-module)。

   > 重点：如果有 JSON日志，请配置 `var.paths`设置以指向它们，而不是纯文本日志。

9. 在每个节点上[启动 Filebeat](https://www.elastic.co/guide/en/beats/filebeat/7.8/filebeat-starting.html)。

   > 提示：根据您安装Filebeat的方式，当您尝试运行Filebeat模块时，可能会看到与文件所有权或权限有关的错误。请参阅 [配置文件的所有权和权限](https://www.elastic.co/guide/en/beats/libbeat/7.8/config-file-permissions.html)

10. 检查监视集群上是否存在适当的索引。

    例如，使用[cat indices](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/cat-indices.html) 命令验证是否有新的 `filebeat-*`索引。

    > 提示：如果要在 Kibana 中使用 Monitoring UI，还必须有 `.monitoring-*`索引。这些索引是在您收集有关 Elastic Stack 产品的指标时生成的。例如，请参阅 [*使用 Metricbeat 收集监视数据*](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/configuring-metricbeat.html)。

11.  [在Kibana中查看监视数据](https://www.elastic.co/guide/en/kibana/7.8/monitoring-data.html)。

