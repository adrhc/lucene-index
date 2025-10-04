#!/bin/bash

if [ -e "env.sh" ]; then
	source env.sh
elif [ -e "../env.sh" ]; then
	source ../env.sh
elif [ -e "./mvnw" ]; then
	echo "env.sh missing, using \"./mvnw\""
	MVN="./mvnw"
elif [ -e ../mvnw ]; then
	echo "env.sh missing, using ../mvnw"
	MVN="../mvnw"
else
	echo "env.sh missing, using \"mvn\""
	MVN="mvn"
fi

# export MAVEN_OPTS="$MAVEN_OPTS --enable-preview"
# export M2_HOME=$TOOLS/maven-3.3.9
# export M2_CONF=$M2_HOME/conf/settings-jisr.xml
# export MVN="$M2_HOME/bin/mvn -s \"$M2_CONF\""

# With -e: The script exits immediately if any command returns a non-zero exit code.
# Without -e: A failed command is ignored (unless you explicitly check $?). The script continues, which can lead to misleading results.
# 
# With -u: Using an unset variable is treated as an error → the script stops.
# Without -u: Unset variables are treated as empty strings, which can cause subtle bugs.
# 
# With pipefail: The exit code of a pipeline (cmd1 | cmd2 | cmd3) is the first failing command, not just the last one.
# Without it: Only the exit status of the last command matters.
set -euo pipefail

# disable all tests:
# ./mvnw -e -Dmaven.javadoc.skip=true -Dmaven.test.skip=true clean install

# these require <forkCount> usage:
# ./clean.install.sh -P db-off	        -> equivalent to in-memory-only (default)
# ./clean.install.sh -P db-on
#
# these does not require <forkCount> usage:
# ./clean.install.sh -P staging-only
# ./clean.install.sh -P production-only
$MVN -e -Dmaven.javadoc.skip=true clean install "$@"
