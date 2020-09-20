# Filebeat 7.8 部署安装

## 1. 从官网下载 Filebeat

https://www.elastic.co/cn/downloads/beats/filebeat

## 2. 修改或添加配置文件 `filebeat.yml`

### 2.1. 添加配置项 `filebeat.inputs` 并追加以下内容，其中 `paths` 为项目的日志路径

```yml
#========================== Filebeat filebeat ==============================
filebeat.inputs:
  # ======== 配置启动的微服务应用日志路径
  - type: log
    enabled: true
    paths:
      - /Users/baihooSRC2/Project-Program02/baike-company-project/logs/application/*/*.log
    exclude_lines: ['\sDEBUG\s\d']
    exclude_files: ['sc-admin.*.log$']
    fields:
      docType: sys-log
      project: microservice-central
    multiline:
      pattern: '^\[\S+:\S+:\d{2,}] '
      negate: true
      match: after
```

### 2.2. 添加配置项 `output.logstash` 并追加以下内容，其中`hosts`为`logstash`的部署地址

```yml
#========================== logstash output ===============================
output.logstash:
  hosts: ["192.168.1.88:5044"]
  bulk_max_size: 2048
```

## 3. 启动 filebeat

```bash
./filebeat -c filebeat.yml -e
```

