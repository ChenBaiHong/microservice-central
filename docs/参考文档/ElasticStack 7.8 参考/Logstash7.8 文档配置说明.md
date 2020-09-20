# [安装 Logstash](https://www.elastic.co/guide/en/logstash/7.8/installing-logstash.html)

## [在 Docker 运行 Logstash](https://www.elastic.co/guide/en/logstash/7.8/docker.html)

### 拉取 Logstash 镜像

```bash
docker pull docker.elastic.co/logstash/logstash:7.8.0
```

### Docker 上配置 Logstash

必须将 pipeline 配置放在 Logstash 可以找到的位置。默认情况下 `/usr/share/logstash/pipeline/`，容器将在 中查找管道配置文件。

在此示例中，我们通过 docker run 命令使用 volume 数据卷绑定提供的配置：

```bash
docker run --rm -it -v ~/pipeline/:/usr/share/logstash/pipeline/ docker.elastic.co/logstash/logstash:7.8.0
```

然后，Logstash会将主机目录`~/pipeline/`中的每个文件通过管道配置解析。

如果您不为 Logstash 提供配置，它将以最小的配置运行，该配置可监听来自 [Beats input plugin](https://www.elastic.co/guide/en/logstash/7.8/plugins-inputs-beats.html) 的消息，并将收到的任何消息回显到 `stdout`。在这种情况下，启动日志将类似于以下内容：

```verilog
Sending Logstash logs to /usr/share/logstash/logs which is now configured via log4j2.properties.
[2016-10-26T05:11:34,992][INFO ][logstash.inputs.beats    ] Beats inputs: Starting input listener {:address=>"0.0.0.0:5044"}
[2016-10-26T05:11:35,068][INFO ][logstash.pipeline        ] Starting pipeline {"id"=>"main", "pipeline.workers"=>4, "pipeline.batch.size"=>125, "pipeline.batch.delay"=>5, "pipeline.max_inflight"=>500}
[2016-10-26T05:11:35,078][INFO ][org.logstash.beats.Server] Starting server on port: 5044
[2016-10-26T05:11:35,078][INFO ][logstash.pipeline        ] Pipeline main started
[2016-10-26T05:11:35,105][INFO ][logstash.agent           ] Successfully started Logstash API endpoint {:port=>9600}
```

这是 镜像 的默认配置，在 `/usr/share/logstash/pipeline/logstash.conf `中定义。如果这是您正在观察的行为，请确保正确选择了管道配置，并确保要替换 `logstash.conf` 或整个管道目录。

### 设置

该图像提供了几种配置设置的方法。传统方法是提供自定义的 logstash.yml文件，但也可以使用环境变量来定义设置。

#### 绑定挂载设置文件

设置文件也可以通过绑定安装提供。 Logstash 希望在 `/usr/share/logstash/config/` 中找到它们。

可以提供一个包含所有所需文件的完整目录：

```bash
docker run --rm -it -v ~/settings/:/usr/share/logstash/config/ docker.elastic.co/logstash/logstash:7.8.0
```

或者，可以挂载单个文件：

```bash
docker run --rm -it -v ~/settings/logstash.yml:/usr/share/logstash/config/logstash.yml docker.elastic.co/logstash/logstash:7.8.0
```

#### 环境变量配置

<hr/>
<table border="0" cellpadding="4px" summary="Example Docker Environment Variables">
<colgroup>
<col>
<col>
</colgroup>
<tbody valign="top">
<tr>
<td valign="top">
<p>
<span class="strong strong"><strong>Environment Variable</strong></span>
</p>
</td>
<td valign="top">
<p>
<span class="strong strong"><strong>Logstash Setting</strong></span>
</p>
</td>
</tr>
<tr>
<td valign="top">
<p>
<code class="literal">PIPELINE_WORKERS</code>
</p>
</td>
<td valign="top">
<p>
<code class="literal">pipeline.workers</code>
</p>
</td>
</tr>
<tr>
<td valign="top">
<p>
<code class="literal">LOG_LEVEL</code>
</p>
</td>
<td valign="top">
<p>
<code class="literal">log.level</code>
</p>
</td>
</tr>
<tr>
<td valign="top">
<p>
<code class="literal">MONITORING_ENABLED</code>
</p>
</td>
<td valign="top">
<p>
<code class="literal">monitoring.enabled</code>
</p>
</td>
</tr>
</tbody>
</table>
<hr/>



通常，可以[settings documentation](https://www.elastic.co/guide/en/logstash/7.8/logstash-settings-file.html) 中列出的任何设置。

> 提示：使用环境变量 causes 定义设置 logstash.yml在适当的地方进行修改。如果 logstash.yml 已从主机系统装入绑定。因此，不建议将绑定挂载技术与环境变量技术相结合。最好选择一种方法来定义 Logstash 设置。

#### Docker 默认

使用Docker映像时，以下设置具有不同的默认值：

<hr/>
<table border="0" cellpadding="4px">
<colgroup>
<col>
<col>
</colgroup>
<tbody valign="top">
<tr>
<td valign="top">
<p>
<code class="literal">http.host</code>
</p>
</td>
<td valign="top">
<p>
<code class="literal">0.0.0.0</code>
</p>
</td>
</tr>
<tr>
<td valign="top">
<p>
<code class="literal">monitoring.elasticsearch.hosts</code>
</p>
</td>
<td valign="top">
<p>
<code class="literal">http://elasticsearch:9200</code>
</p>
</td>
</tr>
</tbody>
</table>
<hr/>

这些设置在默认的 logstash.yml 中定义。可以使用 [自定义 `logstash.yml`](https://www.elastic.co/guide/en/logstash/7.8/docker-config.html#docker-bind-mount-settings)  或通过 [环境变量](https://www.elastic.co/guide/en/logstash/7.8/docker-config.html#docker-env-config) 覆盖它们。

> 重点：如果更换 logstash.yml 对于自定义版本，如果要保留上述默认值，请确保将它们复制到自定义文件中。如果没有，它们将被新文件“屏蔽”。

#### 记录配置

在Docker下，Logstash 日志默认情况的标准输出。要更改此行为，请使用上述任何技术替换 `/usr/share/logstash/config/log4j2.properties` 中的文件。