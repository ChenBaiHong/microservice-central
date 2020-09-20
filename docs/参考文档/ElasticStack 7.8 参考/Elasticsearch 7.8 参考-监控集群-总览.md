# Elasticsearch 7.8 参考

## 监控集群-总览

监视集群时，您将从集群中的 Elasticsearch 节点、Logstash节点、Kibana实例和 Beats 收集数据。您还可以使用 Filebeat收集 Elasticsearch 日志。

所有监控指标都存储在 Elasticsearch 中，这使您能够轻松地可视化 Kibana 中的数据。默认情况下，监控指标存储在本地索引中。

> 注意：我们强烈建议在生产中使用单独的监控。使用单独的监视群集可防止生产群集停机影响您访问监视数据的能力。它还可以防止监视活动影响生产集群的性能。出于同样的原因，我们还建议使用单独的 Kibana实例来查看监视数据

您可以使用 Metricbeat 来收集和发送有关 Elasticsearch、Kibana、Logstash 和 Beats 的数据，并将其直接发送到您的监控集群，而不是通过生产集群进行路由。下图说明了一个典型的监控体系结构，该体系结构具有独立的生产和监控集群：

<img src="imgrchitecture.png" alt="A typical monitoring environment" style="zoom:50%;" />

如果您具有适当的许可证，则可以将数据从多个生产群集路由到单个监视群集。有关不同订阅级别之间差异的详细信息，请参阅：https://www.elastic.co/subscriptions

> 重点：通常，监视集群和被监视的集群应该运行相同版本的堆栈。监视群集无法监视运行新版本堆栈的生产群集。如果需要，监视集群可以监视运行先前主要版本的最新版本的生产集群。

## 监控的工作方式？

每个Elasticsearch节点、Logstash节点、Kibana实例和Beat实例根据其持久 UUID 在集群中被认为是唯一的，它被写入 [`path.data`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/path-settings.html) 节点或实例启动时的目录。

监控文档只是普通的JSON文档，通过在指定的收集间隔监视每个弹性堆栈组件而构建。如果要更改这些索引的模板，请参阅[*配置用于监视的索引*](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/config-monitoring-indices.html)。

Metricbeat用于收集监视数据并将其直接发送到监视集群。

要了解如何收集监测数据，请参阅：

- [*Legacy collection methods*](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/collecting-monitoring-data.html)
- [*Collecting monitoring data with Metricbeat*](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/configuring-metricbeat.html)
- [Monitoring Kibana](https://www.elastic.co/guide/en/kibana/7.8/xpack-monitoring.html)
- [Monitoring Logstash](https://www.elastic.co/guide/en/logstash/7.8/configuring-logstash.html)
- Monitoring Beats:
  - [Auditbeat](https://www.elastic.co/guide/en/beats/auditbeat/7.8/monitoring.html)
  - [Filebeat](https://www.elastic.co/guide/en/beats/filebeat/7.8/monitoring.html)
  - [Functionbeat](https://www.elastic.co/guide/en/beats/functionbeat/7.8/monitoring.html)
  - [Heartbeat](https://www.elastic.co/guide/en/beats/heartbeat/7.8/monitoring.html)
  - [Metricbeat](https://www.elastic.co/guide/en/beats/metricbeat/7.8/monitoring.html)
  - [Packetbeat](https://www.elastic.co/guide/en/beats/packetbeat/7.8/monitoring.html)
  - [Winlogbeat](https://www.elastic.co/guide/en/beats/winlogbeat/7.8/monitoring.html)

