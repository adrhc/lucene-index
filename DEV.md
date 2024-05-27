# MacBook
```bash
ln -sv /usr/local/opt/sdkman-cli/libexec .sdkman
# .zshenv
# export JAVA_HOME='/usr/local/opt/sdkman-cli/libexec/candidates/java/17.0.11-zulu'
export SDKMAN_DIR=$(brew --prefix sdkman-cli)/libexec
# [[ -s "${SDKMAN_DIR}/bin/sdkman-init.sh" ]] && source "${SDKMAN_DIR}/bin/sdkman-init.sh"
sdk current java
```
