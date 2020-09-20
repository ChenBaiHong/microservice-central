# Elasticsearch 7.8 参考

## 监控集群-配置监视索引（[Configuring indices for monitoring](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/config-monitoring-indices.html)）

[索引模版](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/indices-templates.html) 用于配置存储从集群收集的监视数据的索引。

您可以通过 ` _template` API检索模板：

```json
GET /_template/.monitoring-*
```

默认情况下，模板为监视索引配置一个分片和一个副本。要覆盖默认设置，请添加自己的模板

1. 将模板模式设置为 `.monitoring-*`。 

2. 将模板 顺序（`order`） 设置为1。这确保您的模板在默认模板（order 为0）之后应用。 

3. 在设置部分中指定 `number_of_shards` 和/或 `number_of_replicas`。 

例如，以下模板将分片的数量增加到5，将副本的数量增加到2。

```json
PUT /_template/custom_monitoring
{
    "index_patterns": ".monitoring-*",
    "order": 1,
    "settings": {
        "number_of_shards": 5,
        "number_of_replicas": 2
    }
}
```

> 重点：仅在 “设置” 部分设置 “碎片数（`number_of_shards`）”和 “副本数（`number_of_replicas`）”。覆盖其他监视模板设置可能会导致监视仪表板停止正常工作。

