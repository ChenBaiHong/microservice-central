# Elasticsearch 7.8 参考

## 查询 DSL-查询和过滤上下文（[Query and filter context](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-filter-context.html)）

### 相关性评分

默认情况下，Elasticsearch 按相关性评分对匹配的搜索结果进行排序，以衡量每个文档与查询的匹配程度。

相关性评分分数是一个正浮点数，在搜索API的 `_score` 元字段中返回。`_score` 越高，表示文档越相关性权重越多。虽然每种查询类型可以不同地计算相关性分数，但是分数计算还取决查询子句 是在 `query context` 还是在 `filter context` 中运行。

### 查询上下文（query context）

在查询上下文中，查询子句回答问题 “此文档与该查询子句的匹配程度如何？” 除了确定文档是否匹配之外，查询子句还计算 `_score` 元字段中的相关性得分。

只要将查询子句传递到查询参数（例如 [search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html#request-body-search-query) API中的查询参数），查询上下文就有效。

### 过滤上下文（filter context）

在过滤器上下文中，查询子句回答问题 “此文档是否与此查询子句匹配？” 答案是简单的 “是” 或 “否”，则不计算分数。过滤器上下文主要用于过滤结构化数据，例如：

* 此时间戳是否在2015年 到 2016年之间？
* 状态字段设置为“已发布”吗？

常用过滤器将由 Elasticsearch 自动缓存，以提高性能。

只要查询子句 给传递过 `filter `参数（例如 [`bool`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-bool-query.html) 查询中的 `filter` 或 `must_not` 参数，[`constant_score`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-constant-score-query.html)  查询中的 `filter` 参数或 [`filter` 聚合](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-aggregations-bucket-filter-aggregation.html) ），过滤器上下文即有效。

### 查询和过滤器上下文示例

以下示例 中查询子语句 的查询和过滤器上下文是使用 `search` API。此查询将匹配满足以下所有条件的文档：

* `title` 字段包含单词 `search`。
* `content` 字段包含单词 `elasticsearch`
* `status` 字段包含单词 `published`
* `publish_date` 字段包含日期大于2015年1月1日的

```json
GET /_search
{
  "query": { 			// 1⃣️
    "bool": { 
      "must": [ 	// 2⃣️
        { "match": { "title":   "Search"        }},
        { "match": { "content": "Elasticsearch" }}
      ],
      "filter": [ // 3⃣️
        { "term":  { "status": "published" }},
        { "range": { "publish_date": { "gte": "2015-01-01" }}}
      ]
    }
  }
}
```

1⃣️  `search` 参数指示查询上下文。

2⃣️  `bool` 和两个 `match` 子句在查询上下文中使用，这意味着它们用于对每个文档的匹配程度进行评分。

3⃣️  `filter` 参数指示过滤器上下文。其 `term` 和 ` range` 子句用于过滤器上下文。它们将过滤出不匹配的文档，但不会影响匹配文档的分数。

> 警告：在查询上下文中为查询计算的分数表示为单精度浮点数；它们只有 24 位才能表示有效的精度。超过有效位数的分数计算将转换为浮点数，而损失精度。

> 提示：在查询上下文中使用查询子句来确定会影响匹配文档的得分条件（即文档匹配程度），并在过滤器上下文中使用其他的所有查询子句。