#!/bin/bash
set -euo pipefail

SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

#accepts all `docker-compose up` arguments, so you can do `./up -d`

export CURRENT_USER_ID=$(id -u)

$DIR/ensure_cache_dirs.sh

#ensure newest image versions are pulled
docker pull igeolise/scalajs-test-runner:latest
docker pull nginx:1.11.6

docker-compose -f $DIR/docker-compose.yml up $@