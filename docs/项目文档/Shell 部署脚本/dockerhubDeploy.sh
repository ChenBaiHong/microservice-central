#! /bin/sh
repository_name=$1
project_name=$2
tag=$3
port=$4

imageName=$repository_name/$project_name:$tag

echo "$imageName"

# 查询容器是否存在，存在删除
containerId=$(docker ps -a | grep -w "${project_name}":"${tag}" | awk '{print $1}')
echo "$containerId"
if [ "$containerId" != "" ]; then
    # 停掉容器
    docker stop "$containerId"
    # 删除容器
    docker rm "$containerId"
    echo "成功删除容器"
fi

# 查询镜像是否存在，存在则删除
imageId=$(docker images | grep -w "$project_name" | awk '{print $3}')
echo "$imageId"
if [ "$imageId" != "" ]; then
    # 删除镜像
    docker rmi -f "$imageId"
    echo "成功删除镜像"
fi
# 下载镜像
docker pull "$imageName"

# 启动容器
docker run -di -p "$port":"$port" "$imageName"
