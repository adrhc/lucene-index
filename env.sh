#!/bin/bash

# printf "HOME: $HOME\nSDKMAN_DIR: $SDKMAN_DIR"
[[ -s "$HOME/.sdkman/bin/sdkman-init.sh" ]] && source "$HOME/.sdkman/bin/sdkman-init.sh"

# export JAVA_HOME=$TOOLS/jdk1.8.0_141
# export JAVA_HOME=/usr/lib/jvm/java-8-oracle
# export JAVA_HOME=$HOME/.sdkman/candidates/java/11.0.2-open
# export JAVA_HOME=$HOME/.sdkman/candidates/java/17.0.1-tem/
# export JAVA_HOME=$HOME/.sdkman/candidates/java/18.0.1-oracle/
# export JAVA_HOME=$HOME/.sdkman/candidates/java/19.0.2-oracle/
# export JAVA_HOME=$HOME/.sdkman/candidates/java/20.0.1-oracle/
# export JAVA_HOME=$HOME/.sdkman/candidates/java/21.0.3-oracle/
# export JAVA_HOME=$HOME/.sdkman/candidates/java/21.0.6-graal/
# export PATH=$JAVA_HOME/bin:$PATH
#
# The above is replaced by .sdkmanrc!!! see it in the project's root.
[[ -s ".sdkmanrc" ]] && echo "applying sdkman environment" && sdk env install

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

if [ -e ./mvnw ]; then
	echo "using ./mvnw"
	MVN="./mvnw"
elif [ -e ../mvnw ]; then
	echo "using ../mvnw"
	MVN="../mvnw"
elif [ "$(which mvn)" != "" ]; then
	echo "using $(which mvn)"
	MVN="mvn"
else
    echo "Can't find any of \"mvn\" or \"mvnw\"!"
    exit 1
fi
export MVN="$MVN -e"

echo -e "\nUsing java:"
java -version

echo -e "\nUsing maven:"
$MVN --version

# export TARGET=/tmp/target-ro.go.adrhc.albums-webapp
