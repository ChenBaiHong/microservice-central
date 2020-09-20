##### Elasticsearch 7.8 参考

## 查询 DSL-复合查询（[Compound queries](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/compound-queries.html)）

复合查询包装其他复合查询或叶子查询，以组合其结果和分数，更改其行为或从查询切换到过滤器上下文。

该组中的查询是：

* [`bool` 查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-bool-query.html)

  用于组合多个叶子或复合查询子句的默认查询，如 `must`、`should`、`must_not`或 `filter`子句。`must`和 `should`子句的分数组合在一起 - - - - 匹配的子句越多越好，而 `must not`和 `filter` 子句在筛选器上下文中执行。

* [`boosting` 查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-boosting-query.html)

  返回 `positive` (肯定)查询匹配的文档，但降低 `negative`（否定）查询匹配的文档的分数。

* [`constant_score` 查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-constant-score-query.html)

  一个查询，它包含了另一个查询，但是在过滤器上下文中执行它。所有匹配的文档都被赋予相同的“constant” `_score`。

* [`dis_max` 查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-dis-max-query.html)

  一个查询，它接受多个查询，并返回与任何查询子句匹配的任何文档。当 `bool` 查询合并所有匹配查询的分数时，`dis_max` 查询使用单个最佳匹配查询子句的分数。

* [`function_score` 查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html)

  使用该功能修改主查询返回的分数，以考虑诸如受欢迎程度，流行度，距离或使用脚本实现的自定义算法等因素。

### Bool 查询

匹配其他查询的布尔组合的文档的查询。布尔查询映射到 Lucene `BooleanQuery`。它是使用一个或多个布尔子句构建的，每个子句都有一个类型化的出现。出现类型包括：

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <col width=175>
 <col width=177>
 <col width=185>
<tr height=23 style='height:17.0pt'>
  <td class=xl64 width=175 style='width:131pt;font-size:18px;font-weight:700;'>出现类型</td>
  <td class=xl64 width=177 style='width:133pt;font-size:18px;font-weight:700;'>描述</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>must</td>
  <td class=xl68 width=87 style='width:65pt'>子句（查询）必须出现在匹配的文档中，并将有助于得分。</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>filter</td>
  <td class=xl68 width=87 style='width:65pt'>子句（查询）必须出现在匹配的文档中。不管怎样，查询的分数将被忽略。过滤子句是在过滤器上下文中执行的，这意味着评分被忽略，子句被考虑用于缓存。</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>should</td>
  <td class=xl68 width=87 style='width:65pt'>子句（查询）应出现在匹配的文档中。</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>must_not</td>
  <td class=xl68 width=87 style='width:65pt'>子句（查询）应出现在匹配项中文件。文件子句（查询）不能出现在匹配的文档中。子句是在筛选器上下文中执行的，这意味着将忽略评分并考虑将子句用于缓存。因为忽略评分，所以返回所有文档的分数为0。</td>
 </tr>
</table>


bool 查询采用了一种匹配越多越好的方法，因此每个匹配的 `must` 或 `should` 子句的分数将被加在一起，以提供每个文档的最终得分。

```json
POST _search
{
  "query": {
    "bool" : {
      "must" : {
        "term" : { "user" : "kimchy" }
      },
      "filter": {
        "term" : { "tag" : "tech" }
      },
      "must_not" : {
        "range" : {
          "age" : { "gte" : 10, "lte" : 20 }
        }
      },
      "should" : [
        { "term" : { "tag" : "wow" } },
        { "term" : { "tag" : "elasticsearch" } }
      ],
      "minimum_should_match" : 1,
      "boost" : 1.0
    }
  }
}
```

#### 使用 `minimum_should_match`

可以使用 `minimum_should_match` 参数指定返回文档的 `should` 子句的数量或百分比。

如果 `bool` 查询至少包含一个 `should` 子句而没有 `must` 或 `filter`子句，则默认值为1。否则，默认值为0。

有关其他有效值，请参阅 [`minimum_should_match` parameter](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-minimum-should-match.html)参数。

#### 用 `bool.filter` 评分

在筛选器元素下指定的查询对评分没有影响 - 分数返回为0。分数只受指定查询的影响。例如，以下三个查询都返回 `status` 字段包含术语 `active`的所有文档。

* 由于未指定评分查询，因此第一个查询将为所有文档分配0分：

  ```json
  GET _search
  {
    "query": {
      "bool": {
        "filter": {
          "term": {
            "status": "active"
          }
        }
      }
    }
  }
  ```

* 这个bool查询有一个`match_all`查询，它为所有文档分配1.0的分数。

  ```json
  GET _search
  {
    "query": {
      "bool": {
        "must": {
          "match_all": {}
        },
        "filter": {
          "term": {
            "status": "active"
          }
        }
      }
    }
  }
  ```

* 该 `constant_score` 查询的行为与上述第二个示例完全相同。 `constant_score` 查询为该过滤器匹配的所有文档分配1.0的分数。

  ```json
  GET _search
  {
    "query": {
      "constant_score": {
        "filter": {
          "term": {
            "status": "active"
          }
        }
      }
    }
  }
  ```

  

#### 使用命名查询查看匹配的子句

如果需要知道bool查询中的哪个子句与查询返回的文档匹配，可以使用 [命名查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-request-body.html#request-body-search-queries-and-filters) 为每个子句指定一个名称。

### boosting 查询

返回与`positive`（肯定）查询匹配的文档，同时减少也与`negative`（否定）查询匹配的文档的相关性分数。

您可以使用 boosting 查询降级某些文档，而不将它们从搜索结果中排除。

* 范例要求

  ```json
  GET /_search
  {
      "query": {
          "boosting" : {
              "positive" : {
                  "term" : {
                      "text" : "apple"
                  }
              },
              "negative" : {
                   "term" : {
                       "text" : "pie tart fruit crumble tree"
                  }
              },
              "negative_boost" : 0.5
          }
      }
  }
  ```

#### `boosting` 的顶级参数

* **positive**

  （必需，查询对象）要运行的查询。任何返回的文档必须与此查询匹配。

* **negative**

  （必输项，查询对象）用于降低匹配 [相关性评分](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-filter-context.html#relevance-scores)  的查询。

  如果返回的文档与 `positive` （肯定）查询和此查询匹配，`boosting` 查询计算文档的最终关联分数，如下所示：

  1. 从`positive`（肯定）查询中获取原始关联分数。
  2. 将分数乘以 `negative_boost` 值。

* **negative_boost**

  （必选，浮点数）0到1.0之间的浮点数，用于降低与否定查询匹配的文档的关联度。

### constant score 查询

包装筛选器查询并返回每个匹配的文档，其相关度分数等于boost参数值。

```json
GET /_search
{
    "query": {
        "constant_score" : {
            "filter" : {
                "term" : { "user" : "kimchy"}
            },
            "boost" : 1.2
        }
    }
}
```

#### `constant_score` 的顶级参数 

* **filter**

  （必需，查询对象） [筛选要运行的查询](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-bool-query.html)。任何返回的文档必须与此查询匹配。

  过滤查询不计算相关性得分。为了提高性能，Elasticsearch会自动缓存常用的筛选器查询。

* **boost**

  （可选，浮点）浮点数，用作匹配筛选器查询的每个文档的常量 [相关性分数](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-filter-context.html#relevance-scores)。默认为1.0。

### Disjunction max 查询

返回与一个或多个包装查询匹配的文档，称为查询子句或子句。

如果返回的文档匹配多个查询子句，dis_max查询将从任何匹配子句中为文档分配最高的相关性分数，并为任何其他匹配子查询分配一个打破平局的增量。

您可以使用 `dis_max` 在映射了不同 [提升](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/mapping-boost.html)  因子的字段中搜索术语。

#### 示例请求

```json
GET /_search
{
    "query": {
        "dis_max" : {
            "queries" : [
                { "term" : { "title" : "Quick pets" }},
                { "term" : { "body" : "Quick pets" }}
            ],
            "tie_breaker" : 0.7
        }
    }
}
```

#### `dis_max` 的顶级参数 

* **queries**

  （必需，查询对象数组）包含一个或多个查询子句。返回的文档必须与这些查询中的一个或多个匹配。如果一个文档匹配多个查询，Elasticsearch将使用最高的[相关性得分](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-filter-context.html#relevance-scores)。

* **tie_breaker**

  （可选，float）0到1.0之间的浮点数，用于增加匹配多个查询子句的文档的[相关性分数](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-filter-context.html#relevance-scores)。默认为0.0。

  可以使用`tie_breaker`值为多个字段中包含相同术语的文档分配更高的相关性分数，而不是将其与多个字段中两个不同术语的更好情况相混淆。

  如果一个文档匹配多个子句，`dis_max` 查询将按如下方式计算文档的相关性分数：

  1. 从得分最高的匹配从句中获取关联度得分。
  2. 将任何其他匹配子句的分数乘以 `tie_breaker` 值。
  3. 把最高分加到乘以的分数上。

  如果 `tie_breaker` 值大于0.0，则所有匹配的子句都计算在内，但得分最高的子句最重要。

### Function score 查询

`function_score` 允许您修改通过查询检索的文档的分数。例如，如果 score 函数的计算开销很大，并且它足以计算过滤后的文档集上的分数，那么这将非常有用。

要使用 `function_score`，用户必须定义一个查询和一个或多个函数，这些函数为查询返回的每个文档计算一个新的分数。

`function_score` 只能与以下一个函数一起使用：

```json
GET /_search
{
    "query": {
        "function_score": {
            "query": { "match_all": {} },
            "boost": "5",
            "random_score": {}, // 1⃣️
            "boost_mode":"multiply"
        }
    }
}
```

​		1⃣️ 有关支持的 [Function score](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#score-functions) ，请参见功能得分。

此外，还可以组合多种功能。在这种情况下，只有当文档与给定的筛选查询匹配时，才可以选择应用该函数

```json
GET /_search
{
    "query": {
        "function_score": {
          "query": { "match_all": {} },
          "boost": "5", // 1⃣️
          "functions": [
              {
                  "filter": { "match": { "test": "bar" } },
                  "random_score": {}, // 2⃣️
                  "weight": 23
              },
              {
                  "filter": { "match": { "test": "cat" } },
                  "weight": 42
              }
          ],
          "max_boost": 42,
          "score_mode": "max",
          "boost_mode": "multiply",
          "min_score" : 42
        }
    }
}
```

​		1⃣️ Boost 整个查询的 。

​		2⃣️ 有关支持的 [Function score](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#score-functions) ，请参见功能得分。

> 提示：每个函数的过滤查询产生的分数无关紧要。

如果函数中没有给定过滤器，这相当于指定 `"match_all":{}`

首先，根据定义的函数对每个文档进行评分。参数 `score_mode` 指定如何组合计算的分数：

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <col width=175>
 <col width=177>
 <col width=185>
<tr height=23 style='height:17.0pt'>
  <td class=xl64 width=175 style='width:131pt;'>multiply</td>
  <td class=xl64 width=177 style='width:133pt;'>分数相乘（默认）</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>sum</td>
  <td class=xl68 width=87 style='width:65pt'>分数相加</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>avg</td>
  <td class=xl68 width=87 style='width:65pt'>分数是平均值</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>first</td>
  <td class=xl68 width=87 style='width:65pt'>应用具有匹配过滤器的第一个函数</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>max</td>
  <td class=xl68 width=87 style='width:65pt'>使用最高分</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>min</td>
  <td class=xl68 width=87 style='width:65pt'>使用最低分数</td>
 </tr>
</table>

因为分数可以在不同的尺度上（例如，衰变函数在0和1之间，而对于 `field_value_factor` [场_值_因子] 则是任意的），而且有时函数对分数的不同影响是可取的，因此每个函数的分数可以用用户定义的 `weight` (权重) 进行调整。权重可以在函数数组（上例）中为每个函数定义，并与相应函数计算的分数相乘。如果在没有任何其他函数声明的情况下给定 `weight`，则 `weight` 充当一个函数，它只返回权重。

如果 `score_mode` 设置为 `avg`，则单个分数将由加权平均数组合而成。例如，如果两个函数返回分数1和2，并且它们各自的权重是 3 和 4，那么它们的分数将合并为 `(1*3+2*4)/(3+4)` 而不是 `(1*3+2*4)/2`。

通过设置 `max_boost` 参数，可以限制新分数不超过某个限制。`max_boost` 的默认值是 FLT_MAX。

新计算的分数与查询的分数相结合。参数 `boost_mode` 定义了：

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <col width=175>
 <col width=177>
 <col width=185>
<tr height=23 style='height:17.0pt'>
  <td class=xl64 width=175 style='width:131pt;'>multiply</td>
  <td class=xl64 width=177 style='width:133pt;'>查询分数和功能分数相乘（默认）</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>replace</td>
  <td class=xl68 width=87 style='width:65pt'>仅使用功能分数，查询分数将被忽略</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>avg</td>
  <td class=xl68 width=87 style='width:65pt'>分数是平均值</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>sum</td>
  <td class=xl68 width=87 style='width:65pt'>查询分数和功能分数相加</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>max</td>
  <td class=xl68 width=87 style='width:65pt'>查询分数和功能分数的最大值</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>min</td>
  <td class=xl68 width=87 style='width:65pt'>查询分数和功能分数的最小值</td>
 </tr>
</table>

默认情况下，修改分数不会更改匹配的文档。要排除不符合某个分数阈值的文档，可以将 `min_score` 参数设置为所需的分数阈值。

> 提示：要使 `min_score` 生效，需要对查询返回的所有文档进行评分，然后逐个筛选出来。

`function_score` 查询提供了几种类型的分数函数。

- [`script_score`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#function-script-score)
- [`weight`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#function-weight)
- [`random_score`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#function-random)
- [`field_value_factor`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#function-field-value-factor)
- [decay functions](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#function-decay): `gauss`, `linear`, `exp`

#### Script score（脚本评分）

`script_score` 函数允许您包装另一个查询，并通过使用脚本表达式从 doc 中的其他数字字段值派生的 计算自定义该查询的评分。下面是一个简单的示例： 

```json
GET /_search
{
    "query": {
        "function_score": {
            "query": {
                "match": { "message": "elasticsearch" }
            },
            "script_score" : {
                "script" : {
                  "source": "Math.log(2 + doc['likes'].value)"
                }
            }
        }
    }
}
```

> 重点：
>
> 在 Elasticsearch中，所有文档分数都是32位正浮点数。
>
> 如果 `script_score` 函数生成精度更高的分数，则将其转换为最接近的32位浮点。
>
> 同样，分数必须是非负的。否则，Elasticsearch将返回一个错误。

在不同的脚本字段值和表达式之上，`_score` script 参数可用于根据包装的查询检索分数。

脚本编译被缓存以加快执行速度。如果脚本有需要考虑的参数，最好重用同一个脚本，并为其提供参数：

```json
GET /_search
{
    "query": {
        "function_score": {
            "query": {
                "match": { "message": "elasticsearch" }
            },
            "script_score" : {
                "script" : {
                    "params": {
                        "a": 5,
                        "b": 1.2
                    },
                    "source": "params.a / Math.pow(params.b, doc['likes'].value)"
                }
            }
        }
    }
}
```

> 注意：请注意，与自定义的_score查询不同，查询的分数与脚本评分的结果相乘。如果您想禁用此功能，请设置 `"boost_mode": "replace"`

#### Weight（权重）

权重分数允许您将分数乘以所提供的 `weight`。这有时是需要的，因为在特定查询上设置的 `boost` 值得到了规范化，而对于这个 score 函数则没有。数值的类型是 float。

```json
"weight" : number
```

#### Random（随机）

`random_score` 生成的分数在0到1之间均匀分布，但不包括1。默认情况下，它使用内部 Lucene doc ids 作为随机性的来源，这是非常有效的，但不幸的是不可复制，因为文档可能会被合并重新编号。

如果你希望分数是可复制的，可以提供一个 `seed` 和 `field`。最后的分数将基于这个 `seed` 计算，考虑的文档字段的最小值和基于索引名和 shard id 计算的 salt，以便具有相同值但存储在不同索引中的文档得到不同的分数。请注意，在同一个shard 中并且具有相同的 `field` 值的文档将获得相同的分数，因此通常需要对所有文档使用具有唯一值的字段。一个好的默认选择可能是使用 `_seq_no` (序列号) 字段，其唯一的缺点是，如果更新文档，分数会发生变化，因为更新操作也会更新 `_seq_no` (序列号) 字段的值。

> 提示:
> 可以在不设置字段的情况下设置`seed`，但这已被弃用，因为这需要在消耗大量内存的 `_id`字段上加载 fielddata

```json
GET /_search
{
    "query": {
        "function_score": {
            "random_score": {
                "seed": 10,
                "field": "_seq_no"
            }
        }
    }
}
```

#### Field Value factor（字段值因子）

`field_value_factor` 函数允许您使用文档中的字段来影响分数。它类似于使用 `script_score` 函数，但是它避免了脚本的开销。如果用于多值字段，则在计算中仅使用字段的第一个值。

例如，假设您有一个用数字 likes 字段编制索引的文档，并希望用该字段影响文档的分数，这样做的示例如下：

```json
GET /_search
{
    "query": {
        "function_score": {
            "field_value_factor": {
                "field": "likes",
                "factor": 1.2,
                "modifier": "sqrt",
                "missing": 1
            }
        }
    }
}
```

它将转化为以下得分公式：

```mathematica
sqrt(1.2 * doc['likes'].value)
```

`field_value_factor` 函数有许多选项：

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <col width=175>
 <col width=177>
 <col width=185>
<tr height=23 style='height:17.0pt'>
  <td class=xl64 width=175 style='width:131pt;'>field</td>
  <td class=xl64 width=177 style='width:133pt;'>要从文档中提取的字段。</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>factor</td>
  <td class=xl68 width=87 style='width:65pt'>字段值乘以的可选因子，默认为1。</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt;font-weight: 700;'>modifier</td>
   <td class=xl68 width=87 style='width:65pt'>应用于字段值的修饰符可以是以下之一：<strong>none, log, log1p, log2p, ln, ln1p, ln2p, square, sqrt, 或 reciprocal</strong>。默认为 none。</td>
 </tr>
</table>

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <col width=175>
 <col width=177>
 <col width=185>
<tr height=23 style='height:17.0pt'>
  <td class=xl64 width=175 style='width:131pt;font-size:18px;font-weight: 700;'>modifier</td>
  <td class=xl64 width=177 style='width:133pt;font-size:18px;font-weight: 700;'>含义</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>none</td>
  <td class=xl68 width=87 style='width:65pt'>请勿对字段值应用任何乘数</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>log</td>
  <td class=xl68 width=87 style='width:65pt'>取字段值的公共对数。因为如果在0到1之间的值上使用此函数将返回负值并导致错误，因此建议改用log1p。</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>log1p</td>
  <td class=xl68 width=87 style='width:65pt'>将1加到字段值并取公共对数</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>log2p</td>
  <td class=xl68 width=87 style='width:65pt'>将2加到字段值并取公共对数</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>ln</td>
  <td class=xl68 width=87 style='width:65pt'>取字段值的自然对数。因为如果在0到1之间的值上使用此函数将返回负值并导致错误，因此建议改用ln1p。</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>ln1p</td>
  <td class=xl68 width=87 style='width:65pt'>字段值加1，取自然对数</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>ln2p</td>
  <td class=xl68 width=87 style='width:65pt'>字段值加2，取自然对数</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>square</td>
  <td class=xl68 width=87 style='width:65pt'>平方字段值（乘以它本身）</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>sqrt</td>
  <td class=xl68 width=87 style='width:65pt'>取字段值的平方根</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt'>reciprocal</td>
  <td class=xl68 width=87 style='width:65pt'>交换字段值，与 1/x 相同，其中 x 是字段的值</td>
 </tr>
</table>

> 提示：`field_value_score` 函数生成的分数必须为非负，否则将引发错误。如果对0到1之间的值使用 `log`和 `ln`修饰符将生成负值。一定要用范围过滤器限制字段的值以避免这种情况，或者使用 `log1p`和 `ln1p`。

> 提示：请记住，获取 0的  log() 或负数的平方根是非法操作，这将引发异常。一定要用范围过滤器限制字段的值以避免这种情况，或者使用 `log1p`和 `ln1p`。

#### Decay functions（衰变函数）

`DECAY_FUNCTION` (衰变函数)为文档评分，该函数根据文档的数值字段值与用户给定原点的距离衰减。这类似于范围查询，但边缘平滑，而不是方框。

**示例一**

要对具有数字字段的查询使用距离评分，用户必须为每个字段定义 `origin` (原点) 和 `scale` (刻度)。原点用于定义计算距离的“中心点”，以及定义衰减速率的 `sacle` (比例) 。衰变函数指定为

```json
"DECAY_FUNCTION": { // 1⃣️
    "FIELD_NAME": { // 2⃣️
          "origin": "11, 12",
          "scale": "2km",
          "offset": "0km",
          "decay": 0.33
    }
}
```

​		1⃣️ `DECAY_FUNCTION` (衰变函数)应为 `linear` (线性函数)、`exp` (经验函数)或 `gauss` (高斯函数)之一。

​		2⃣️ 指定的字段必须是数字、日期或地理点字段。

**示例二**

在上面的例子中，字段是一个[`geo_point`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/geo-point.html) ，并且可以以 geo 格式提供原点。在这种情况下，`scale`(刻度)和 `offset` (偏移量) 必须用单位表示。如果字段是日期字段，则可以将 `scale` 和 ``offset`` 设置为“天 (days) ”、“周 (weeks) ”等。例子：

```json
GET /_search
{
    "query": {
        "function_score": {
            "gauss": {
                "date": {
                      "origin": "2013-09-17", // 1⃣️
                      "scale": "10d",
                      "offset": "5d", 				// 2⃣️
                      "decay" : 0.5           // 2⃣️
                }
            }
        }
    }
}
```

​		1⃣️ 原点的日期格式取决于映射中定义的格式。如果不定义原点，则使用当前时间。

​		2⃣️ 偏移和衰退参数是可选的。

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>origin</td>
  <td class=xl68 width=87 style='width:65pt'>用于计算距离的原点。对于数字字段，必须以数字形式给出，对于日期字段必须以日期形式给出，对于地理字段必须以地理点的形式给出。地理和数字字段必需。对于日期字段，默认值为 now。origin 支持日期数学（例如 now-1h）。</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>scale</td>
  <td class=xl68 width=87 style='width:65pt'>所有类型都需要。定义距原点的 origin + offset(距离+偏移量)，在该距离上计算的分数将等于 decay (衰退) 参数。对于地理字段：可以定义为数字+单位（1km，12m，…）。默认单位是米。对于日期字段：可以定义为数字+单位（“1h”、“10d”…）。默认单位是毫秒。对于数字字段：任何数字。</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>offset</td>
  <td class=xl68 width=87 style='width:65pt'>如果定义了offset (偏移量)，衰减函数将只计算距离大于定义 offset (偏移量)的文档的衰减函数。默认值为0。</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>decay</td>
  <td class=xl68 width=87 style='width:65pt'>decay (衰退) 参数定义如何在给定的 scale (刻度距离)对文档进行评分。如果没有定义衰变，则距离刻度的文件将被评分0.5分。</td>
 </tr>
</table>

在第一个示例中，您的文档可能表示酒店并包含地理位置字段。你想根据酒店离给定地点的距离来计算衰减函数。您可能不会立即看到高斯函数要选择什么样的比例，但您可以这样说：“在距离所需位置2公里的地方，分数应该减少到1/3。”参数“scale”将自动调整，以确保 score 函数计算的分数为0.33，适用于距离2公里的酒店所需位置。

在第二个示例中，字段值在2013-09-12和2013-09-22之间的文档的权重为1.0，而从该日期起15天的文档的权重为0.5。

##### Supported decay functions（支持的衰变函数）

DECAY_FUNCTION 确定衰减的形状

###### **gauss**（高斯函数）

正常衰减，计算如下

![Gaussian](imgaussian.png)

其中 ![sigma](imgigma.png) 的计算是为了确保分数在距离 `origin`+-`offset ` (原点+-偏移量) 的尺度 (`scale`) 范围内衰减 (`decay`)

![sigma calc](imgigma_calc.png)

有关演示由高斯函数生成的曲线的图，请参见 [正态衰减, 关键字 `gauss`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#gauss-decay) 。

###### **exp**（经验函数）

指数衰减，计算如下

![Exponential](imgxponential.png)

其中再次计算参数 ![lambda](imgambda.png) ，以确保分数在距离 `origin`+-`offset ` (原点+-偏移量)的距离尺度 (`scale`)处取值衰减 (`decay`)

![lambda calc](imgambda_calc.png)

有关显示由exp函数生成的曲线的图，请参见 [指数衰减, 关键字 `exp`](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/query-dsl-function-score-query.html#exp-decay)。

###### **linear**（线性函数）

线性衰减，计算如下

![Linear](imginear.png).

再次计算参数 `s`，以确保得分采用距原点+偏移量 (`origin`+-`offset `) 的距离尺度 (`scale`)的值衰减 (`decay`)

![s calc](img_calc.png)

与正常衰减和指数衰减相反，如果字段值超过用户给定刻度值的两倍，此函数实际上会将分数设置为0。

对于单个函数，三个衰变函数及其参数可以这样可视化（本例中的字段称为“age”）：

![decay 2d](imgecay_2d.png)

##### Multi-values fields （多值字段）

如果用于计算衰退的字段包含多个值，则默认情况下选择最接近原点的值来确定距离。这可以通过设置 `multi_value_mode` 进行更改。

<hr>
<table border=0 cellpadding=0 cellspacing=0 width=714>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>min</td>
  <td class=xl68 width=87 style='width:65pt'>距离是最小距离</td>
 </tr>
 <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>max</td>
  <td class=xl68 width=87 style='width:65pt'>距离是最大距离</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>avg</td>
  <td class=xl68 width=87 style='width:65pt'>距离是平均距离</td>
 </tr>
   <tr height=57 style='height:43.0pt'>
  <td class=xl67 width=87 style='width:65pt:font-size:16px;font-weight: 700;'>sum</td>
  <td class=xl68 width=87 style='width:65pt'>距离是所有距离的总和</td>
 </tr>
</table>

示例:

```json
    "DECAY_FUNCTION": {
        "FIELD_NAME": {
              "origin": ...,
              "scale": ...
        },
        "multi_value_mode": "avg"
    }
```

##### 详细的例子

<hr>

假设你在某个镇上找旅馆。你的预算是有限的。此外，您希望酒店离市中心较近，因此酒店离所需位置越远，您入住的可能性就越小。

您希望查询结果符合您的标准（例如，“酒店、同性区、非吸烟区”）与市中心的距离以及价格相关。

直觉上，你会把市中心定义为起点，也许你愿意从酒店步行2公里到市中心。

在本例中，位置字段的 **origin**（原点）是城镇中心，**scale**（刻度距离）为~2km。

如果你的预算很低，你可能会喜欢便宜的东西而不是昂贵的东西。对于 `price`字段，初始值为 0 欧元，其比例取决于您愿意支付的金额，例如20欧元。

在本例中，对于酒店的价格，字段可能被称为“price”，而对于该酒店的坐标，这些字段可能被称为“location”。

<hr>

在这种情况下，`price` 的函数是

```json
"gauss": { // 1⃣️
    "price": {
          "origin": "0",
          "scale": "20"
    }
}
```

​		1⃣️ 该衰减函数也可以是 linear 或 exp。

以及 `location` 的函数是

```json
"gauss": { // 1⃣️
    "location": {
          "origin": "11, 12",
          "scale": "2km"
    }
}
```

​		1⃣️ 该衰减函数也可以是 linear 或 exp。

<hr>

假设要在原始分数上乘以这两个函数，则请求如下所示：

```json
GET /_search
{
    "query": {
        "function_score": {
          "functions": [
            {
              "gauss": {
                "price": {
                  "origin": "0",
                  "scale": "20"
                }
              }
            },
            {
              "gauss": {
                "location": {
                  "origin": "11, 12",
                  "scale": "2km"
                }
              }
            }
          ],
          "query": {
            "match": {
              "properties": "balcony"
            }
          },
          "score_mode": "multiply"
        }
    }
}
```

接下来，我们将展示三种可能的衰变函数的计算结果。

##### 正态衰减, 关键字 `gauss`

在上例中选择“高斯”作为衰减函数时，乘数的等高线和曲面图如下所示：

<img src="imgd0e18a6-e898-11e2-9b3c-f0145078bd6f.png" alt="cd0e18a6 e898 11e2 9b3c f0145078bd6f" style="zoom:50%;" />

<img src="imgc43c928-e898-11e2-8e0d-f3c4519dbd89.png" alt="ec43c928 e898 11e2 8e0d f3c4519dbd89" style="zoom:50%;" />

假设原始搜索结果与三家酒店匹配：

- "Backback Nap"   
- "Drink n Drive"
- "BnB Bellevue"

“Drink n Drive”距离您定义的位置（近2公里）相当远，而且价格不太便宜（约13欧元），因此它的系数较低，为 0.56。“BnB Bellevue”和“Backback Nap”都非常接近定义的位置，但是“BnB Bellevue”更便宜，因此它的乘数为0.86，而“Backback Nap”的值为0.66。

##### 指数衰减, 关键字 `exp`

在上例中选择“线性”作为衰减函数时，乘数的等高线和曲面图如下所示：

<img src="img775b0ca-e899-11e2-9f4a-776b406305c6.png" alt="1775b0ca e899 11e2 9f4a 776b406305c6" style="zoom:50%;" />

<img src="img9d8b1aa-e899-11e2-91bc-6b0553e8d722.png" alt="19d8b1aa e899 11e2 91bc 6b0553e8d722" style="zoom:50%;" />



#### 衰变函数支持的字段

仅支持数字，日期和地理位置字段。

#### 如果缺少字段怎么办？

如果文档中缺少数字字段，函数将返回1。

