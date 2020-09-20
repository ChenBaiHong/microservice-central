#!/bin/bash
hub_url="swr.cn-north-1.myhuaweicloud.com"
hub_user="ap-southeast-1@XSFMSFYAW5J7PYHUC89J"
hub_pwd="1ff5fcd2aa0de1883aaea602a00be9d0b1a4c833d62918c2ffe5d138d67b5913"
hub_reps="baihoo"
VERSION="latest"

# shellcheck disable=SC2046
docker rmi $(docker images | grep "$hub_reps/gitbook" | awk '{print $3}')

docker build -t "$hub_url/$hub_reps/gitbook:$VERSION" ./

docker login -u "$hub_user" -p "$hub_pwd" "$hub_url"

docker push "$hub_url/$hub_reps/gitbook:$VERSION"
