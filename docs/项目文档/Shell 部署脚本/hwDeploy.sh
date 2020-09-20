#! /bin/sh
harbor_url=$1
repository_name=$2
project_name=$3
tag=$4
port=$5

imageName=$harbor_url/$repository_name/$project_name:$tag

echo "$imageName"

# 查询容器是否存在，存在删除
containerId=$(docker ps -a | grep -w "${project_name}":"${tag}" | awk '{print $1}')
if [ "$containerId" != "" ]; then
    # 停掉容器
    docker stop "$containerId"
    # 删除容器
    docker rm "$containerId"
    echo "成功删除容器"
fi

# 查询镜像是否存在，存在则删除
imageId=$(docker images | grep -w "$project_name" | awk '{print $3}')
if [ "$imageId" != "" ]; then
    # 删除镜像
    docker rmi -f "$imageId"
    echo "成功删除镜像"
fi

# 登录Harbor私服
docker login -u ap-southeast-1@XSFMSFYAW5J7PYHUC89J -p 1ff5fcd2aa0de1883aaea602a00be9d0b1a4c833d62918c2ffe5d138d67b5913 "$harbor_url"

# 下载镜像
docker pull "$imageName"

# 启动容器
docker run -di -p "$port":"$port" "$imageName"
