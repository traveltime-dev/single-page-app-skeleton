#!/bin/bash
set -euo pipefail

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

STARTING_DIR=$(pwd)

BUILDS_DIR=$DIR/docker/builds
VERSION=$(cd $DIR && (git describe --tags --first-parent | awk -F "-g" '{print $1}') && cd ${STARTING_DIR})

$DIR/build.sh

docker build --pull --no-cache --tag igeolise/spa-client:${VERSION} $DIR/docker/client
docker build --pull --no-cache --tag igeolise/spa-backend:${VERSION} $DIR/docker/backend

docker push igeolise/spa-client:${VERSION}
docker push igeolise/spa-backend:${VERSION}