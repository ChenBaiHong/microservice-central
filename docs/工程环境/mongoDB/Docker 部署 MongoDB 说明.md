# Docker 安装 MongoDB

## 待办事项清单

1. 下载 MongoDB 镜像；
2. 创建文件用于初始化经过身份验证的数据库和用户
3. 编写 docker-compose 文件
4. 使用创建的凭据登录 MongoDB

## 创建文件用于初始化数据库和身份验证用户

```shell
$ cd /home/data/mongodb/setup
```

```shell
$ vim setup.js
```

```js
// 创建文件中心数据库
db = db.getSiblingDB('file-center');
// 创建一个用户，设置密码和授予数据所有权限
db.createUser(
    {
        user: "ArtLangdon",
        pwd: "ArtLangdon!@#",
        roles: [
            {
                role: "dbOwner",
                db: "file-center"
            }
        ]
    }
);
```

## 编写 docker-compose.yml 文件

```yaml
# docker-compose.yml
version: '3.5'
services:
  mongodb-server:
    image: mongo:4.4.0
    container_name: docker-mongodb
    restart: on-failure
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: admin!@#321
    volumes:
      - /home/data/mongodb/setup:/docker-entrypoint-initdb.d
      - /home/data/mongodb/db:/data/db
    ports:
      - 27017:27017
      - 27019:27019
    networks:
      - custom_net
networks:
  custom_net:
    external:
      name: overlay-net
```

## 执行

```shell
$ docker-compose up -d # 在后台运行容器
```

## 追加角色授权

```json
 db.grantRolesToUser("ArtLangdon",[{role:"readWrite",db:"file-center"}]);
```

## 结语

已创建 MongoDB 容器、MongoDB 用户、超级用户和数据库，使用以下链接连接：

```shell
mongodb://YourUsername:YourPasswordHere@127.0.0.1:27017/your-database-name
```



