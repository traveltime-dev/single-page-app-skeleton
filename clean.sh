#!/bin/bash
set -euo pipefail

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

IMAGE=igeolise/sbt:0.13.12-openjdk-8
CLIENT_BUILD_DIR=$DIR/docker/client/app
BACKEND_BUILD_DIR=$DIR/docker/backend

CURRENT_USER_ID=$(id -u)

docker pull $IMAGE

$DIR/ensure-cache-dirs.sh

docker run --rm -v $DIR:/opt/app -v ~/.sbt:/home/user/.sbt -v ~/.ivy2:/home/user/.ivy2 -w /opt/app -e LOCAL_USER_ID=${CURRENT_USER_ID} $IMAGE sbt clean

rm -f $DIR/client/public/app/*
rm -rf ${CLIENT_BUILD_DIR}
rm -rf ${BACKEND_BUILD_DIR}/backend.tgz