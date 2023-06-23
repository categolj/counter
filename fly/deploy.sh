#!/bin/bash
set -e -o pipefail
DIR=$(dirname $0)
set -x
pushd ${DIR} > /dev/null
rm -rf app/.wws
APP=wws
# flyctl apps create --name ${APP} --machines
flyctl deploy -a ${APP}
popd > /dev/null
