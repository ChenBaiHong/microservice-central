# Elasticsearch 7.8 参考

## 搜索你的数据-Highlighting

高亮显示 可让您从搜索结果的一个或多个字段中获取突出显示的摘要，以便向用户显示查询匹配的位。当您请求突出显示时，响应将为每个搜索命中包含一个额外的突出显示元素，其中包括突出显示的字段和突出显示的片段。

> 提示：在提取要突出显示的字词时，高亮显示 不会反映查询的布尔逻辑。对于某些复杂的布尔查询（例如嵌套的布尔查询，使用 `minimum_should_match` 的查询等）,可能会突出显示与查询匹配不符的文档部分。

突出显示需要字段的实际内容。如果未存储该字段（映射未将 `store`设置为 `true`），则将加载实际 `_source` 并从`_source`中提取相关的字段。

例如，使用默认突出显示每个搜索命中的`content`字段，请在请求正文中包括一个指定 `centent` 字段里突出显示对象：

```json
GET /_search
{
    "query" : {
        "match": { "content": "kimchy" }
    },
    "highlight" : {
        "fields" : {
            "content" : {}
        }
    }
}
```

Elasticsearch 支持三种高亮 ：`unified`，`plain` 和 `fvh`（快速矢量高亮）。您可以为每个字段指定要使用的高亮类型。

### Unified 高亮

`unified` 使用来自Lucene 的 Unified Highlighter。该 Unified 高亮 是将文本分成句子，并使用BM25算法对单个句子进行评分，就好像它们是语料库中的文档一样

### Palin 高亮

`Palin` 是 Lucene 默认的高亮标准。它可以试图从理解单词重要性和短语查询中的任何单词定位标准方面反映查询匹配逻辑。

> 警告：Palin 高亮 最适合在单个字段中突出显示简单查询匹配项。为了准确反映查询逻辑，它会创建一个很小的内存索引，并通过 Lucene 的查询执行计划程序重新运行原始查询条件，以访问当前文档的低级匹配信息。对于每个字段和需要突出显示的每个文档就会重复此操作。如果想要使用复杂的查询突出显示很多文档中的很多字段，建议在 `postings` 或 `term_vector` 字段上使用 `unified` 来突出高亮显示。

### Fast vector highlighter



以下后续翻译。。。。。。

[Docs](https://www.elastic.co/guide/)

[Elasticsearch Reference [7.8\]](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/index.html) » [Search your data](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/search-your-data.html) » [Run a search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/run-a-search.html) » Highlighting

## On this page

- [Unified highlighter](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#unified-highlighter)
- [Plain highlighter](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#plain-highlighter)
- [Fast vector highlighter](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#fast-vector-highlighter)
- [Offsets strategy](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#offsets-strategy)
- [Highlighting settings](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#highlighting-settings)
- [Highlighting examples](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#highlighting-examples)
- [Override global settings](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#override-global-settings)
- [Specify a highlight query](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#specify-highlight-query)
- [Set highlighter type](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#set-highlighter-type)
- [Configure highlighting tags](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#configure-tags)
- [Highlight on source](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#highlight-source)
- [Highlight in all fields](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#highlight-all)
- [Combine matches on multiple fields](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#matched-fields)
- [Explicitly order highlighted fields](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#explicit-field-order)
- [Control highlighted fragments](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#control-highlighted-frags)
- [Highlight using the postings list](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#highlight-postings-list)
- [Specify a fragmenter for the plain highlighter](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/highlighting.html#specify-fragmenter)