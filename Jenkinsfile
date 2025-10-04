pipeline {
  agent any

  options {
    skipDefaultCheckout(true)
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
  }

  environment {
    PROJECT_NAME = 'lucene-index'
    REPO_DIR     = "${env.PROJECTS_ADRHC}/${env.PROJECT_NAME}"
  }

  stages {
    stage('Git pull') {
      steps {
        sh 'test -d "${REPO_DIR}" || { echo "Missing ${REPO_DIR}"; exit 1; }'
        dir("${REPO_DIR}") { sh 'git pull --ff-only' }
      }
    }

    stage('Build') {
      steps {
        dir("${REPO_DIR}") { sh './clean.install.sh' }
      }
      post {
        always {
          dir("${REPO_DIR}") {
            sh '''
              set -euo pipefail
              WKSP_SUREFIRE="$WORKSPACE/target/surefire-reports"
              WKSP_FAILSAFE="$WORKSPACE/target/failsafe-reports"
              BUILD_DIR="$(./mvnw help:evaluate -Dexpression=project.build.directory -q -DforceStdout)"
              # proceed only if BUILD_DIR is NOT under WORKSPACE
              if [ "${BUILD_DIR#"$WORKSPACE"}" = "$BUILD_DIR" ]; then
                mkdir -p "$WORKSPACE/target"
                # remove target/surefire-reports if it's a real directory (not a symlink)
                [ ! -L "$WKSP_SUREFIRE" ] && [ -d "$WKSP_SUREFIRE" ] && rm -r "$WKSP_SUREFIRE"
                # remove target/failsafe-reports if it's a real directory (not a symlink)
                [ ! -L "$WKSP_FAILSAFE" ] && [ -d "$WKSP_FAILSAFE" ] && rm -r "$WKSP_FAILSAFE"
                # -n, --no-dereference
                [ -d "$BUILD_DIR/surefire-reports" ] && ln -sfn "${BUILD_DIR}/surefire-reports" "$WKSP_SUREFIRE" || true
                [ -d "$BUILD_DIR/failsafe-reports" ] && ln -sfn "${BUILD_DIR}/failsafe-reports" "$WKSP_FAILSAFE" || true
              fi
            '''
          }          
          junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml,target/failsafe-reports/*.xml'
        }
      }
    }
  }
}
