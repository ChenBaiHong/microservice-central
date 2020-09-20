# Elasticsearch 7.8 参考

## 监控集群-生产环境监控（[Monitoring in a production environment](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/monitoring-production.html)）

在生产环境中监视在生产环境中，应将监视数据发送到单独的监视群集，以便在监视的节点不在时也可以使用历史数据。例如，可以使用 Metricbeat 将关于 Kibana、Elasticsearch、Logstash 和 Beats 的监控数据发送到监控集群。如果要将数据发送到ESMS，请参阅[弹性堆栈监视服务](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/esms.html).。

> 重点：Metricbeat是收集监视数据并将其传送到监视群集的推荐方法。
>
> 如果以前配置了旧的收集方法，则应迁移到使用 Metricbeat 收集。使用 Metricbeat 收集或旧收集方法；不要同时使用这两种方法。
>
> 了解有关使用Metricbeat收集监控数据的更多信息。

如果您至少有一个Gold订阅，那么使用专用监视集群还可以从一个中心位置监视多个集群。

要在单独的群集中存储监视数据，请执行以下操作：

1. 设置要用作监视群集的Elasticsearch群集。例如，可以使用节点es-mon-1和es-mon-2设置一个双主机集群。

   > 重点：
   >
   > * 要监视Elasticsearch 7.x群集，必须在监视群集上运行Elasticsearch 7.x。
   >
   > * 监视群集中必须至少有一个 [摄取节点](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/ingest.html) ；它不必是专用的摄取节点。
   1. （可选）验证是否已在监视群集上禁用监视数据收集。默认情况下 `xpack.monitoring.collection.enabled` 设置为 false。

      例如，可以使用以下API查看和更改此设置：

      ```json
      GET _cluster/settings
      
      PUT _cluster/settings
      {
        "persistent": {
          "xpack.monitoring.collection.enabled": false
        }
      }
      ```

   2. 如果在监视群集上启用了Elasticsearch安全功能，请创建可以发送和检索监视数据的用户。

      > 提示：如果计划使用 Kibana 查看监视数据，则用户名和密码凭据必须在 Kibana 服务器和监视群集上都有效。

      如果计划使用 Metricbeat 收集有关 Elasticsearch 或 Kibana 的数据，请创建一个具有 `remote_monitoring_collector` 内置角色的用户和一个`remote_monitoring_agent`的 [内置代理角色](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/built-in-roles.html#built-in-roles-remote-monitoring-agent)的用户。或者，使用 `remote_monitoring_user`  [内置用户](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/built-in-users.html)。

      如果计划使用 HTTP 通过生产集群路由导出数据，那么请创建一个具有 内置远程监控（`remote_monitoring_agent`）的 [内置代理角色](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/built-in-roles.html#built-in-roles-remote-monitoring-agent)的。

      例如，以下请求将创建一个远程监控(`remote_monitor`)用户，具有（`remote_monitoring_agent`）的 [内置代理角色](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/built-in-roles.html#built-in-roles-remote-monitoring-agent)：

      ```json
      POST /_security/user/remote_monitor
      {
        "password" : "changeme",
        "roles" : [ "remote_monitoring_agent"],
        "full_name" : "Internal Agent For Remote Monitoring"
      }
      ```

      或者，使用`remote_monitoring_user`  [内置用户](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/built-in-users.html)。

2. 配置生产集群以收集数据并将其发送到监视群集
   - [Metricbeat收集方法](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/configuring-metricbeat.html)
   - [旧版收集方法](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/collecting-monitoring-data.html)

3. （可选）[将Logstash配置为收集数据并将其发送到监视群集](https://www.elastic.co/guide/en/logstash/7.8/configuring-logstash.html)。

4. （可选）将Beats配置为收集数据并将其发送到监视群集。
   - [Auditbeat](https://www.elastic.co/guide/en/beats/auditbeat/7.8/monitoring.html)
   - [Filebeat](https://www.elastic.co/guide/en/beats/filebeat/7.8/monitoring.html)
   - [Heartbeat](https://www.elastic.co/guide/en/beats/heartbeat/7.8/monitoring.html)
   - [Metricbeat](https://www.elastic.co/guide/en/beats/metricbeat/7.8/monitoring.html)
   - [Packetbeat](https://www.elastic.co/guide/en/beats/packetbeat/7.8/monitoring.html)
   - [Winlogbeat](https://www.elastic.co/guide/en/beats/winlogbeat/7.8/monitoring.html)

5. 可选）配置Kibana以收集数据并将其发送到监视集群。
   - [Metricbeat collection methods](https://www.elastic.co/guide/en/kibana/7.8/monitoring-metricbeat.html)
   - [Legacy collection methods](https://www.elastic.co/guide/en/kibana/7.8/monitoring-kibana.html)

6. （可选）创建用于监视的专用Kibana实例，而不是使用单个Kibana实例来访问生产集群和监视集群。
   1. （可选）禁用此 Kibana 实例中监视数据的收集。在 kibana.yml文件中设置 `xpack.monitoring.kibana.collection.enabled`为`false`。有关此设置的详细信息，请参阅[Kibana中的监控设置](https://www.elastic.co/guide/en/kibana/7.8/monitoring-settings-kb.html).。

7. [配置 Kibana 去检索和显示监视数据](https://www.elastic.co/guide/en/kibana/7.8/monitoring-data.html)。

