# Elasticsearch 7.8 参考

## 搜索你的数据

搜索查询是对一个或多个 `Elasticsearch` 索引中的数据信息的请求。您可以将查询视为一个问题，以 Elasticsearch 理解的方式编写。根据您的数据，您可以使用查询来获取问题的答案，例如：

* 我网站上的哪些页面包含特定的单词或短语？
* 服务器上的哪些进程需要超过500毫秒的响应时间？
* 过去一周内，我网络上的哪些用户运行了regsvr32.exe？
* 我有多少产品的价格高于20美元？

搜索包含一个或多个查询，这些查询被组合并发送到 Elasticsearch。与搜索查询匹配的文档会在响应的匹配数或搜索结果中返回。

搜索还可能包含用于更好地处理其查询的其他信息。例如，搜索可能仅限于特定索引或仅返回特定数量的结果。

### 运行搜索

您可以使用  [search API](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html)  搜索存储在一个或多个Elasticsearch索引中的数据。

该 API 可以运行 两种类型的搜索，具体取决于您提供 [查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-your-data.html#search-query) 的方式：

* [URI 搜索](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/run-a-search.html#run-uri-search)

  通过查询参数提供查询。 URI搜索往往更简单，最适合测试。

* [请求体搜索](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/run-a-search.html#run-request-body-search)

  通过API请求的JSON主体提供查询。这些查询是用  [Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl.html) 编写的。我们建议在大多数生产用例中使用请求正文搜索。

  > 注意：如果您在URI和请求正文中都指定了查询，则搜索API请求仅运行URI查询。

#### 运行一个 `URL 搜索`

您可以使用搜索API的 [`q` query string parameter](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html#search-api-query-params-q) 在请求的URI中运行搜索。 而这个 `q` 参数仅接受以 Lucene 的查询字符串语法 [query string syntax](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-query-string-query.html#query-string-syntax)。

* 案例

  首先，导入 或 向 Elasticsearch 索引添加一些数据。

  以下以 [`bulk API `请求](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/docs-bulk.html)  将一些示例的 用户日志数据添加到 user_logs_000001索引。

  ​	  控制台方式: 

  ```basic
  PUT /user_logs_000001/_bulk?refresh
  {"index":{"_index" : "user_logs_000001", "_id" : "1"}}
  { "@timestamp": "2020-12-06T11:04:05.000Z", "user": { "id": "vlb44hny" }, "message": "Login attempt failed" }
  {"index":{"_index" : "user_logs_000001", "_id" : "2"}}
  { "@timestamp": "2020-12-07T11:06:07.000Z", "user": { "id": "8a4f500d" }, "message": "Login successful" }
  {"index":{"_index" : "user_logs_000001", "_id" : "3"}}
  { "@timestamp": "2020-12-07T11:07:08.000Z", "user": { "id": "l7gk7f82" },
  ```

     	curl 方式：

  ```bash
  curl -X PUT "localhost:9200/user_logs_000001/_bulk?refresh&pretty" -H 'Content-Type: application/json' -d'
  {"index":{"_index" : "user_logs_000001", "_id" : "1"}}
  { "@timestamp": "2020-12-06T11:04:05.000Z", "user": { "id": "vlb44hny" }, "message": "Login attempt failed" }
  {"index":{"_index" : "user_logs_000001", "_id" : "2"}}
  { "@timestamp": "2020-12-07T11:06:07.000Z", "user": { "id": "8a4f500d" }, "message": "Login successful" }
  {"index":{"_index" : "user_logs_000001", "_id" : "3"}}
  { "@timestamp": "2020-12-07T11:07:08.000Z", "user": { "id": "l7gk7f82" }, "message": "Logout successful" }
  '
  ```

  现在，您可以使用搜索API在此索引上运行URI搜索。

  以下 URI搜索 将 user.id 值为 l7gk7f82 的文档进行匹配。请注意，查询是使用 `q` 查询字符串参数指定的。

  ​		控制台方式: 

  ```basic
  GET /user_logs_000001/_search?q=user.id:8a4f500d
  ```

  ​		curl 方式：

  ```bash
  curl -X GET "localhost:9200/user_logs_000001/_search?q=user.id:8a4f500d&pretty"
  ```

  API返回以下响应。请注意 `hits.hits` 属性包含与查询匹配的文档。

  ```json
  {
    "took": 2,
    "timed_out": false,
    "_shards": {
      "total": 1,
      "successful": 1,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": {
        "value": 1,
        "relation": "eq"
      },
      "max_score": 0.9808291,
      "hits": [
        {
          "_index": "user_logs_000001",
          "_type": "_doc",
          "_id": "2",
          "_score": 0.9808291,
          "_source": {
            "@timestamp": "2020-12-07T11:06:07.000Z",
            "user": {
              "id": "8a4f500d"
            },
            "message": "Login successful"
          }
        }
      ]
    }
  }
  ```

#### 运行一个 `请求体搜索`

您可以使用搜索API的 [`query` 查询请求正文参数](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-search.html#request-body-search-query)  来提供以 [Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl.html) 形式编写的查询作为JSON对象。

* 案例

  以下请求正文搜索使用 `match` 查询来匹配 “message” 值为 “login successful”的文档。请注意，匹配查询在 `query` 参数中指定为JSON对象。

  ​		控制台方式：

  ```basic
  GET /user_logs_000001/_search
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  ```

  ​		curl 方式：

  ```bash
  curl -X GET "localhost:9200/user_logs_000001/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  '
  ```

  API返回以下响应。`hits.hits` 属性包含匹配的文档。默认情况下，响应按 `_score` 排序这些匹配的文档，这是一个 [相关性](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-filter-context.html#relevance-scores)，用于衡量每个文档与查询的匹配程度。

  ```shell
  {
    "took": 1,
    "timed_out": false,
    "_shards": {
      "total": 1,
      "successful": 1,
      "skipped": 0,
      "failed": 0
    },
    "hits": {
      "total": {
        "value": 3,
        "relation": "eq"
      },
      "max_score": 0.9983525,
      "hits": [
        {
          "_index": "user_logs_000001",
          "_type": "_doc",
          "_id": "2",
          "_score": 0.9983525,
          "_source": {
            "@timestamp": "2020-12-07T11:06:07.000Z",
            "user": {
              "id": "8a4f500d"
            },
            "message": "Login successful"
          }
        },
        {
          "_index": "user_logs_000001",
          "_type": "_doc",
          "_id": "3",
          "_score": 0.49917626,
          "_source": {
            "@timestamp": "2020-12-07T11:07:08.000Z",
            "user": {
              "id": "l7gk7f82"
            },
            "message": "Logout successful"
          }
        },
        {
          "_index": "user_logs_000001",
          "_type": "_doc",
          "_id": "1",
          "_score": 0.42081726,
          "_source": {
            "@timestamp": "2020-12-06T11:04:05.000Z",
            "user": {
              "id": "vlb44hny"
            },
            "message": "Login attempt failed"
          }
        }
      ]
    }
  }
  ```

#### 搜索多个索引

要搜索多个索引，请将其作为 逗号分隔 的值添加到搜索API请求路径中。

* 案例

  以下请求搜索 user_logs_000001 和 user_logs_000002 索引。

  ​		控制台方式：

  ```basic
  GET /user_logs_000001,user_logs_000002/_search
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/user_logs_000001,user_logs_000002/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  '
  ```

您也可以使用索引 正则 搜索多个索引。

* 案例

  以下请求使用索引正则  `user_logs*` 代替索引名称。该请求搜索集群中以 `user_logs` 开头的所有索引。

  ​		控制台方式：

  ```basic
  GET /user_logs*/_search
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/user_logs*/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  '
  ```

要搜索集群中的所有索引，从请求路径中省略索引名称。或者，您可以使用 `_all` 或 `*` 代替索引名称。

* 案例

  以下请求是等效的，并搜索集群中的所有索引。

  ​		控制台方式：

  ```basic
  GET /_search
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  
  GET /_all/_search
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  
  GET /*/_search
  {
      "query" : {
          "match" : { "message" : "login" }
      }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  '
  curl -X GET "localhost:9200/_all/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match": {
        "message": "login successful"
      }
    }
  }
  '
  curl -X GET "localhost:9200/*/_search?pretty" -H 'Content-Type: application/json' -d'
  {
      "query" : {
          "match" : { "message" : "login" }
      }
  }
  '
  ```

#### 分页搜索结果集

默认情况下，搜索API返回前10个匹配的文档。要浏览更多结果，可以使用搜索API的 `size` 和 `from` 参数。`size` 参数是指要返回的匹配文档数。`from` 参数是指从完整结果集，下标为 0 开始的偏移量，该偏移量指示您 document 想要开始的位置。

* 案例

  以下搜索API请求将 `from` 偏移量设置为 5，这意味着请求将偏移量或跳过前五个匹配文档。

  `size` 参数为 20，表示请求可以从偏移量开始最多返回20个文档。

  ​		控制台方式：

  ```basic
  GET /_search
  {
    "from": 5,
    "size": 20,
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "from": 5,
    "size": 20,
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  '
  ```

  默认情况下，您不能使用 `from`和` size` 参数分页浏览超过10,000个文档。使用[`index.max_result_window`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/index-modules.html#index-max-result-window) 索引设置来设置此限制。

  深度分页或一次请求许多结果可能会导致搜索速度变慢。结果在返回之前先进行排序。由于搜索请求通常跨越多个分片，因此每个分片必须生成自己的排序结果。然后必须对这些单独的结果进行合并和排序，以确保总体排序顺序正确。

  作为深度分页的替代方法，我们建议使用 [`scroll`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-scroll)  或 [`search_after`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-search-after)  参数。

  > 警告：Elasticsearch 使用 Lucene 的内部文档 IDs 作为断路器。这些内部文档ID在相同数据的副本之间可能完全不同。在进行分页时，您有时可能会看到排序值相同的文档的顺序不一致。

#### 检索选定的字段

默认情况下，搜索响应中的每个匹配都包含 `_source` 文档，这是对文档建立索引时提供的整个JSON对象。如果您只需要搜索响应中的某些源字段,您可以使用 [source filtering](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/run-a-search.html#source-filtering) 来限制返回指定部分的源字段数据。

仅使用文档源返回字段会有一些限制，如下两点：

* `_source`字段不包含 [多字段](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/multi-fields.html) 或 [字段别名](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/alias.html)。同样，`source` 中的字段也不包含使用 [`copy_to`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/copy-to.html)  映射参数复制的值。
* 由于 `_source` 在 Lucene 中以分片存储，因此即使仅需要少量字段，也必须加载和解析整个源对象。

为避免这些限制，您可以做如下两点：

* 使用 [`docvalue_fields`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/run-a-search.html#docvalue-fields)  参数获取选定字段的值。当返回相当少量的支持 doc 值的字段（例如关键字和日期）时，这是一个不错的选择。
* 使用 [`stored_fields`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-stored-fields)  参数获取特定存储字段的值。 （使用 [`store`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/mapping-store.html)  映射选项的字段）

您可以在以下各节中找到有关每种方法的更多详细信息：

- [Source 过滤](#Source 过滤)
- [Stored fields](#Stored fields)
- [Stored fields](#Stored fields)

#### Source 过滤

您可以使用 `_source` 参数选择返回源的指定字段。这称为源过滤。

* 案例

  以下搜索API请求将 `_source` 请求主体参数设置为 `false`。该文档源不包含在响应中。

  ​		控制台方式：

  ```basic
  GET /_search
  {
    "_source": false,
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "_source": false,
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  '
  ```

  如下仅返回源字段的子集，可以在 `_source` 参数中指定通配符（*）模式。以下搜索API请求仅返回obj1和obj2字段及其属性的源。

  ​		控制台方式：

  ```basic
  GET /_search
  {
    "_source": "obj.*",
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "_source": "obj.*",
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  '
  ```

  您还可以在 `_source` 字段中指定通配符模式的数组。以下搜索API请求仅返回 obj1和 obj2 字段及其属性的源。

  ​		控制台方式：

  ```basic
  GET /_search
  {
    "_source": [ "obj1.*", "obj2.*" ],
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "_source": [ "obj1.*", "obj2.*" ],
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  '
  ```

  为了更好地控制，您可以在 `_source` 参数中指定一个包含 `includes` 和 `excludes` 的数组对象。

  如果指定了 `includes` 属性，则仅返回与其中之一匹配的源字段。您可以使用 `excludes` 属性从此子集中排除字段。

  如果未指定 `include` 属性，则返回整个文档源，不包括与 `excludes` 属性中与模式匹配的任何字段。

  以下搜索API请求仅返回 obj1 和 obj2 字段及其属性的源，不包括任何子描述字段。

  ​		控制台方式：

  ```basic
  GET /_search
  {
    "_source": {
      "includes": [ "obj1.*", "obj2.*" ],
      "excludes": [ "*.description" ]
    },
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  ```

  ​		curl方式：

  ```bash
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "_source": {
      "includes": [ "obj1.*", "obj2.*" ],
      "excludes": [ "*.description" ]
    },
    "query": {
      "term": {
        "user.id": "8a4f500d"
      }
    }
  }
  '
  ```

#### Doc value fields

您可以使用 [`docvalue_fields`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/run-a-search.html#docvalue-fields)  参数返回搜索响应中一个或多个字段的 [doc values](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/doc-values.html) 。

 [`doc values`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/doc-values.html) 存储与 `_source` 相同的值，但存储在磁盘上，专为排序和汇总而优化的基于列的结构。由于每个字段都是单独存储的，因此 Elasticsearch 仅读取请求的字段值，并且可以避免加载整个文档 `_source`。

默认情况下，是将支持的字段存储为 doc values 。但是 doc values 不支持 [`text`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/text.html)  或 [`text_annotated`](https://www.elastic.co/guide/en/elasticsearch/plugins/7.8/mapper-annotated-text-usage.html) 字段。

* 案例

  以下搜索请求使用 `docvalue_fields` 参数检索以下字段的 doc values：

  - 名称以 `my_ip` 开头的字段
  - `my_keyword_field` (我的关键字字段)
  - 名称以 `_date_field` 结尾的字段

  ​       控制台方式：

  ```json
  GET /_search
  {
    "query": {
      "match_all": {}
    },
    "docvalue_fields": [
      "my_ip*",											// 1⃣️                   
      {
        "field": "my_keyword_field" // 2⃣️
      },
      {
        "field": "*_date_field",
        "format": "epoch_millis"    // 3⃣️
      }
    ]
  }
  ```

  ​		curl方式：

  ```json
  curl -X GET "localhost:9200/_search?pretty" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match_all": {}
    },
    "docvalue_fields": [
      "my_ip*",  										// 1⃣️                   
      {
        "field": "my_keyword_field" // 2⃣️
      },
      {
        "field": "*_date_field",
        "format": "epoch_millis"    // 3⃣️
      }
    ]
  }
  '
  ```

  ​	1⃣️ 通配符模式，用于匹配指定 字符串 的字段名称。

  ​	2⃣️ 通配符模式，用于匹配指定 对象的字段名称。

  ​	3⃣️ 使用对象符号，您可以使用 `format` 参数为字段的返回文档值指定格式。[Date fields](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/date.html) 支持 [date `format`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/mapping-date-format.html)。[Numeric fields](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/number.html) 支持 [DecimalFormat pattern](https://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormat.html)。其他字段数据类型不支持 `format` 参数。

  > 提示：您不能使用 `docvalue_fields` 参数检索嵌套对象的 doc值。如果指定嵌套对象，则搜索将为该字段返回一个空数组（[]）。要访问嵌套字段，请使用 [`inner_hits`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-inner-hits) 参数的 `docvalue_fields` 属性。

#### Stored fields

也可以使用 [`store`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/mapping-store.html) 映射选项来存储单个字段的值。您可以使用 [`stored_fields`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-stored-fields) 参数将这些存储的值包括在搜索响应中。