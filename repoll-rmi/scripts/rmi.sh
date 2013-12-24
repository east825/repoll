#!/usr/bin/env bash
# rmiregistry is running on port 1099 by default
set -e

SERVER_JAR="server/target/server-0.1-full.jar"
CLIENT_JAR="client/target/client-0.1-full.jar"
CORE_JAR="core/target/core-0.1.jar"
CORE_SRC="server/target/classes/"
# SERVER_JAR="core/target/core-0.1.jar"
# SERVER_SRC="core/target/classes/"

SCRIPT_DIR="$(dirname $(readlink -e $0) )"
PROJECT_ROOT="$(dirname $SCRIPT_DIR)"

# Adjust path if necessary
cd "$PROJECT_ROOT"

run_registry() {
    local codebase="${1:?Codebase for rmiregistry is not specified}"
    if pgrep rmiregistry > /dev/null; then
        echo "Killing running instance of rmiregistry..."
        while pgrep rmiregistry > /dev/null; do
            pkill rmiregistry && sleep 1s
        done
    fi
    echo "Starting rmirgistry..."
    # rmiregistry -J-Djava.rmi.server.codebase="$codebase" -J-Djava.rmi.server.useCodebaseOnly=false
    rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false &
}

run_server() {
    if [[ ! -e "$SERVER_JAR" ]]; then 
        echo "Building server JAR with dependencies..."
        cd server
        mvn -Dmaven.test.skip=true assembly:single
        cd -
    fi
    # server is run from JAR
    run_registry "jar://${SERVER_JAR}" 
    echo "Starting server..."
    java -Djava.security.policy=policy \
        -Dlog4j.configuration=file:logging.properties \
        -Djava.rmi.server.codebase="file:${SERVER_JAR}" \
        -jar "$SERVER_JAR" rmi
}

run_client() {
    java -Djava.security.policy=policy \
        -Dlog4j.configuration=file:logging.properties \
        -jar "$CLIENT_JAR" 
}


# if [[ ! -d server ]]; then
    # echo "Error: Script should be run from the project root" >&2
    # exit 2
# fi

case "$1" in
    server) 
        run_server 
        ;;
    client) 
        run_client 
        ;;
    registry) 
        run_registry "file://${CORE_SRC}" 
        ;;
    *) 
        echo "Error: Unknown command '$1'. Should be one of: client, server, registry" >&2
        exit 3
        ;;
esac

