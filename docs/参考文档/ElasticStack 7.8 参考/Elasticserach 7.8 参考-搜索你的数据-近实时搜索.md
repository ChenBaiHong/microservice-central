# Elasticsearch 7.8 参考

## 搜索你的数据-近实时搜索（[Near real-time search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/near-real-time.html)）

在 [文档和索引](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/documents-indices.html) 的概述表明，将文档存储在 Elasticsearch 中时，便可以在1秒钟内几乎实时地对其进行索引和完全搜索。什么定义了近实时搜索？

Lucene 是 Elasticsearch 所基于的Java库，它引入了按段搜索的概念。段类似于反向索引，但是 Lucene 中的索引一词的意思是“段的集合加上提交点”。提交后，将新段添加到提交点并清除缓冲区。

文件系统缓存位于 Elasticsearch 和 磁盘 之间。内存中索引缓冲区（图1）中的文档被写入新段（图2）。新段首先写入文件系统缓存（时间成本低效率高），然后才刷新到磁盘（时间成本高效率低）。但是，将文件放入高速缓存后，可以像其他文件一样打开和读取该文件。

* 【图1.  Lucene索引在内存缓冲区中包含新文档】

![A Lucene index with new documents in the in-memory buffer](imgucene-in-memory-buffer.png)

Lucene允许编写和打开新的段，使包含的文档可见，无需执行完整的提交即可搜索。这比提交磁盘要轻得多，并且可以经常执行而不会降低性能。

【图2.缓冲区内容被写到一个段中，该段是可搜索的，但尚未提交】

![The buffer contents are written to a segment, which is searchable, but is not yet committed](imgucene-written-not-committed.png)

在Elasticsearch中，这种写入和打开新段的过程称为刷新。刷新使自上次刷新以来对索引执行的所有操作都可用于搜索。您可以通过以下方式控制刷新：

* 等待刷新间隔 
* 设置 [?refresh](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/docs-refresh.html) 选项 
* 使用 [Refresh API](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/indices-refresh.html)  明确完成刷新（POST _refresh）

默认情况下，Elasticsearch 每秒定期刷新一次索引，但仅在最近30秒内已收到一个或多个搜索请求的索引上刷新。这就是为什么我们说Elasticsearch具有几乎实时的搜索功能：文档更改不可见，无法立即搜索，但在此时间范围内将变为可见。

