#!/bin/bash
set -e -o pipefail
DIR=$(dirname $0)
set -x
pushd ${DIR} > /dev/null
mkdir -p app
rm -rf app/counter.*
cp ../target/wasm/counter.wasm ./app
cp ../target/wasm/counter.toml ./app

APP=wws
# flyctl apps create --name ${APP} --machines
flyctl deploy -a ${APP}
popd > /dev/null
