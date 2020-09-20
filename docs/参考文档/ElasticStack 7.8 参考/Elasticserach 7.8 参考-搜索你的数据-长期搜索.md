# Elasticsearch 7.8 参考

## 搜索你的数据-长期搜索（[Long-running searches](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/async-search-intro.html)）

Elasticsearch 通常允许您快速搜索大量数据。在某些情况下，搜索会在许多碎片上执行，可能针对 [冻结索引](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/frozen-indices.html) 和 并跨越多个[远程集群](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/modules-remote-clusters.html),，因此预期结果不会在毫秒内返回。当您需要执行长时间运行的搜索时，同步等待返回结果是不理想的，相反，异步搜索使您可以提交异步执行的搜索请求，监视请求的进度并在以后的阶段检索结果。您也可以在部分结果可用时但在搜索完成之前检索它们。

您可以使用 [提交异步搜索](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/async-search.html#submit-async-search) API请求。[使用异步搜索](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/async-search.html#get-async-search) API ，您可以监视异步搜索请求的进度并检索其结果。正在进行的异步搜索也可以通过 [delete async search](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/async-search.html#delete-async-search) API 删除。

