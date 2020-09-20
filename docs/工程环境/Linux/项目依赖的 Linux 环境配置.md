# 项目依赖的 Linux 环境配置

## 1. 配置 nodejs 环境

### 1.1. yum 安装

```shell
$ yum install -y nodejs
```

### 1.2. 查看版本信息

```shell
$ npm -v
```

### 1.3. 解决存放在Github上的sass无法下载的问题

```shell
$ SASS_BINARY_SITE=https://npm.taobao.org/mirrors/node-sass/ npm install node-sass
```

### 1.4. 将镜像源替换为淘宝的加速访问

```shell
$ npm config set registry https://registry.npm.taobao.org
```

1.5. 卸载 nodejs

```shell
$ sudo npm uninstall npm -g
```

```shell
$ yum remove nodejs npm -y
```

> 进入 /usr/local/lib 删除所有 node 和 node_modules文件夹
>
> 进入 /usr/local/include 删除所有 node 和 node_modules 文件夹
>
> 进入 /usr/local/bin 删除 node 的可执行文件