#!/bin/bash
set -euo pipefail

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

IMAGE=igeolise/scalajs-test-runner:latest
CLIENT_DIR=$DIR/client
BACKEND_DIR=$DIR/backend
CLIENT_BUILD_DIR=$DIR/docker/client/app
BACKEND_BUILD_FILE=$DIR/docker/backend/backend.tgz

CURRENT_USER_ID=$(id -u)

docker pull $IMAGE

$DIR/ensure_cache_dirs.sh
$DIR/clean.sh

docker run --rm -v $DIR:/opt/app -v ~/.sbt:/home/user/.sbt -v ~/.ivy2:/home/user/.ivy2 -w /opt/app -e LOCAL_USER_ID=${CURRENT_USER_ID} $IMAGE sbt release

cp ${BACKEND_DIR}/target/universal/*.tgz ${BACKEND_BUILD_FILE}
rm -rf ${CLIENT_BUILD_DIR}
cp -r ${CLIENT_DIR}/public ${CLIENT_BUILD_DIR}

rm ${CLIENT_BUILD_DIR}/index.html
mv ${CLIENT_BUILD_DIR}/index-prod.html ${CLIENT_BUILD_DIR}/index.html

chmod -R a+rX ${CLIENT_BUILD_DIR}
chmod a+rX ${BACKEND_BUILD_FILE}
