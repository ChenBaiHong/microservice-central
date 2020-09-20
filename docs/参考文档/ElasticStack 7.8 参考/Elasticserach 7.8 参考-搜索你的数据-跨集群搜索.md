# Elasticsearch 7.8 参考

## 搜索你的数据-跨集群搜索（[Search across clusters](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-cross-cluster-search.html)）

跨集群搜索使您可以针对一个或多个 [远程集群](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-remote-clusters.html) 运行单个搜索请求。例如，您可以使用跨集群搜索来过滤和分析存储在不同数据中心的集群中的日志数据。

> 重点：跨集群搜索需要 [远程集群](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-remote-clusters.html) 。

### 支持的API

以下API支持跨集群搜索：

- [Search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html)
- [Multi search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-multi-search.html)
- [Search template](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-template.html)
- [Multi search template](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/multi-search-template.html)

### 跨集群搜索示例

#### 远程集群设置

要执行跨集群搜索，您必须至少配置一个远程集群

以下 [cluster update settings](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/cluster-update-settings.html) API 请求添加了三个远程集群：cluster_one，cluster_two和cluster_three。

```json
PUT _cluster/settings
{
  "persistent": {
    "cluster": {
      "remote": {
        "cluster_one": {
          "seeds": [
            "127.0.0.1:9300"
          ]
        },
        "cluster_two": {
          "seeds": [
            "127.0.0.1:9301"
          ]
        },
        "cluster_three": {
          "seeds": [
            "127.0.0.1:9302"
          ]
        }
      }
    }
  }
}
```

#### 搜索单个远程集群

以下 [search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html)  API请求在单个远程集群 cluster_one 上搜索 `twitter` 索引。

```json
GET /cluster_one:twitter/_search
{
  "query": {
    "match": {
      "user": "kimchy"
    }
  }
}
```

API返回以下响应：

```json
{
  "took": 150,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "failed": 0,
    "skipped": 0
  },
  "_clusters": {
    "total": 1,
    "successful": 1,
    "skipped": 0
  },
  "hits": {
    "total" : {
        "value": 1,
        "relation": "eq"
    },
    "max_score": 1,
    "hits": [
      {
        "_index": "cluster_one:twitter", // 1⃣️
        "_type": "_doc",
        "_id": "0",
        "_score": 1,
        "_source": {
          "user": "kimchy",
          "date": "2009-11-15T14:12:12",
          "message": "trying out Elasticsearch",
          "likes": 0
        }
      }
    ]
  }
}
```

​		1⃣️ 搜索响应正文在 `_index` 参数中包含远程集群的名称。

#### 搜索多个远程集群

以下 [search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html)  API请求在三个群集上搜索twitter索引：

* 您的本地集群 
* 两个远程集群cluster_one和cluster_two

```json
GET /twitter,cluster_one:twitter,cluster_two:twitter/_search
{
  "query": {
    "match": {
      "user": "kimchy"
    }
  }
}
```

API返回以下响应：

```json
{
  "took": 150,
  "timed_out": false,
  "num_reduce_phases": 4,
  "_shards": {
    "total": 3,
    "successful": 3,
    "failed": 0,
    "skipped": 0
  },
  "_clusters": {
    "total": 3,
    "successful": 3,
    "skipped": 0
  },
  "hits": {
    "total" : {
        "value": 3,
        "relation": "eq"
    },
    "max_score": 1,
    "hits": [
      {
        "_index": "twitter", // 1⃣️
        "_type": "_doc",
        "_id": "0",
        "_score": 2,
        "_source": {
          "user": "kimchy",
          "date": "2009-11-15T14:12:12",
          "message": "trying out Elasticsearch",
          "likes": 0
        }
      },
      {
        "_index": "cluster_one:twitter", // 2⃣️
        "_type": "_doc",
        "_id": "0",
        "_score": 1,
        "_source": {
          "user": "kimchy",
          "date": "2009-11-15T14:12:12",
          "message": "trying out Elasticsearch",
          "likes": 0
        }
      },
      {
        "_index": "cluster_two:twitter", // 3⃣️
        "_type": "_doc",
        "_id": "0",
        "_score": 1,
        "_source": {
          "user": "kimchy",
          "date": "2009-11-15T14:12:12",
          "message": "trying out Elasticsearch",
          "likes": 0
        }
      }
    ]
  }
}
```

​	1⃣️ 该文档的 `_index` 参数不包含群集名称。这意味着文档来自本地群集。

​    2⃣️ 该文档来自cluster_one。 

​	3⃣️ 该文档来自cluster_two。

#### 跳过不可用的群集

默认情况下，如果请求中的任何群集都不可用，则跨群集搜索将返回错误。

要在跨集群搜索期间跳过不可用的集群，请将[`skip_unavailable`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/cluster-remote-info.html#skip-unavailable) 集群设置设置为 `true`。

以下[cluster update settings](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/cluster-update-settings.html)API请求将 `cluster_two` 的 `skip_unavailable` 设置更改为 `true`。

```json
PUT _cluster/settings
{
  "persistent": {
    "cluster.remote.cluster_two.skip_unavailable": true
  }
}
```

如果在跨集群搜索期间 `cluster_two` 断开连接或不可用，Elasticsearch 将不在最终结果中包括该集群的匹配文档。

#### 在嗅探模式下选择网关和种子节点

对于使用 [嗅探连接](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-remote-clusters.html#sniff-mode) 模式的远程群集，需要通过网络从本地群集访问网关节点和种子节点。

默认情况下，任何不符合 [主机资格](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-node.html#master-node) 的节点都可以充当网关节点。如果需要，可以通过将 `cluster.remote.node.attr.gateway` 设置为 `true` 来定义集群的网关节点。

对于跨集群搜索，建议您使用网关节点，这些网关节点可以充当搜索请求的[协调节点](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-node.html#coordinating-node) 。如果需要，群集的种子节点可以是这些网关节点的子集。

#### 代理模式下的跨集群搜索

[代理模式](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-remote-clusters.html#proxy-mode) 远程群集连接支持跨群集搜索。所有远程连接都连接到已配置的 `proxy_address`。路由到网关或 [协调节点](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-node.html#coordinating-node) 的任何所需连接都必须由中间代理在此配置的地址处实现。

#### 跨集群搜索如何处理网络延迟

由于跨集群搜索涉及将请求发送到远程集群，因此任何网络延迟都可能影响搜索速度。为了避免慢速搜索，跨集群搜索提供了两个选项来处理网络延迟：

* [最小化网络返回](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-cross-cluster-search.html#ccs-min-roundtrips)

  默认情况下，Elasticsearch 会减少远程集群之间的网络往返次数。虽然这样可以减少网络延迟对搜索速度的影响，但是，Elasticsearch 不能减少大型搜索请求（例如包含[滚动](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-scroll) 或 [内部匹配](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-inner-hits)的搜索请求）的网络往返次数。

  请参阅[最小化网络返回](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-cross-cluster-search.html#ccs-min-roundtrips)以了解此选项的工作方式。

* [不要最小化网络往返](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-cross-cluster-search.html#ccs-unmin-roundtrips)

  对于包含滚动或内部匹配的搜索请求，Elasticsearch 会将多个传出和传入的请求发送到每个远程集群。您可以通过将[`ccs_minimize_roundtrips`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html#ccs-minimize-roundtrips) 参数设置为`false`。尽管通常速度较慢，但此方法可能适用于低延迟的网络。

  请参阅[不要最小化网络往返](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-cross-cluster-search.html#ccs-unmin-roundtrips)以了解此选项的工作方式。

#### 最小化网络往返

当您最小化网络往返次数时，这就是跨集群搜索的工作方式。

1. 将跨集群搜索请求发送到本地集群。该集群中的协调节点接收并解析请求。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-client-request.svg)

2. 协调节点向每个集群（包括本地群集）发送单个搜索请求。每个集群都独立执行搜索请求，将其自己的集群级别设置应用于请求。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-cluster-search.svg)

3. 每个远程群集将其搜索结果发送回协调节点。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-cluster-results.svg)

4. 从每个群集收集结果之后，协调节点将在跨群集搜索响应中返回最终结果。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-client-response.svg)



#### 不要最小化网络往返

当您没有最小化网络往返时，这就是跨集群搜索的工作方式。

1. 将跨集群搜索请求发送到本地集群。该集群中的协调节点接收并解析请求。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-client-request.svg)

2. 协调节点向每个远程集群发送搜索分片API请求。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-cluster-search.svg)

3. 每个远程群集将其响应发送回协调节点。该响应包含有关索引的信息，并将执行跨集群搜索请求的分片。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-cluster-results.svg)

4. 协调节点向每个分片（包括其自己的集群中的分片）发送搜索请求。每个分片独立执行搜索请求。

   > 警告：如果网络往返次数没有最小化，则执行搜索时就好像所有数据都在协调节点的群集中一样。我们建议更新限制搜索的群集级别设置，例如 设置 `action.search.shard_count.limit`, `pre_filter_shard_size`, and `max_concurrent_shard_requests`,来解决。如果这些限制太低，则搜索可能会被拒绝。

   ![ccs min roundtrip client request](imgcs-dont-min-roundtrip-shard-search.svg)

5. 每个分片将其搜索结果发送回协调节点。

   ![ccs min roundtrip client request](imgcs-dont-min-roundtrip-shard-results.svg)

6. 从每个群集收集结果之后，协调节点将在跨群集搜索响应中返回最终结果。

   ![ccs min roundtrip client request](imgcs-min-roundtrip-client-response.svg)

