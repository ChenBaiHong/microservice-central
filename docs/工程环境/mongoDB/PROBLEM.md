## BUG-问题一
* 问题明细如下：

  > 前端读取压缩 图片显示问题，** net::err_content_length_mismatch 200

 * 解决问题：
    > 注意：二进流传输图片时，响应的 header 字段 Content-Length 也就是文件大小 ，是否与传输文件的大小一致

## BUG-问题二
 * 问题明细如下：
    > com.mongodb.MongoQueryException: 
        Query failed with error code 96 and error message 'Executor error during find command :: caused by :: Sort operation used more than the maximum 33554432 bytes of RAM. Add an index, or specify a smaller limit.' on server 10.15.2.11:27017 
 * 解决问题：
   ```bash
   $ db.yourCollection.createIndex({'id':<1 or -1>});  # 只执行这句就能解决bug  注意不能直接复制粘贴，要根据集合编写语句。
   $ db.fileserver.getIndexes();  #查看当前collection的索引
    
   $ db.three_province_poi_v9.find({ "sum_n.sum_4_x":{ $gt:0} } ).sort({"sum_n.sum_4_x":-1});
    
   $ db.three_province_poi_v9.createIndex({"sum_n.sum_4_x": -1});
   ```
   ```bash
   $ db.file.createIndex({'id':-1});
   ```
## BUG-问题三
 * 问题明细如下：
    
    > CentOS 7.7 安装 MongoDB 4.0 出现  获取 GPG 密钥失败：[Errno 14] curl#7 - "Failed connect to www.mongodb.org:443; 操作现在正在进行"
 * 解决问题：
   ```bash
   cat /etc/redhat-release
   ```
   ```text
   CentOS Linux release 7.7.1908 (Core)
   ```