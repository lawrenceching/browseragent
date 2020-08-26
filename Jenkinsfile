pipeline {
  agent any
  environment {
    CODING_DOCKER_REG_HOST = "${env.CCI_CURRENT_TEAM}-docker.pkg.${env.CCI_CURRENT_DOMAIN}"
    CODING_DOCKER_IMAGE_NAME = "${env.PROJECT_NAME.toLowerCase()}/${env.DOCKER_REPO_NAME}/${env.DOCKER_IMAGE_NAME}"
  }
  stages {
    stage("Env Setup") {
      steps {
        sh 'apt-get install default-jdk -y'
      }
    }

    stage("检出") {
      steps {
        checkout(
          [$class: 'GitSCM',
          branches: [[name: env.GIT_BUILD_REF]],
          userRemoteConfigs: [[
            url: env.GIT_REPO_URL,
              credentialsId: env.CREDENTIALS_ID
            ]]]
        )
      }
    }

    stage('单元测试') {
      steps {
        sh "./mvnw test"
      }
      post {
        always {
          // 收集测试报告
          junit 'build/test-results/**/*.xml'
        }
      }
    }

    stage('编译') {
      steps {
        sh "./mvnw build -DskipTests=true"
      }
    }

//     stage("构建 Docker 镜像") {
//       steps {
//         // 执行命令构建打包成 Docker 镜像
//         sh "docker build -f ${env.DOCKERFILE_PATH} -t ${env.CODING_DOCKER_IMAGE_NAME}:${env.DOCKER_IMAGE_VERSION} ${env.DOCKER_BUILD_CONTEXT}"
//       }
//     }
//
//     stage("推送到 CODING Docker 制品库") {
//       steps {
//         script {
//           docker.withRegistry(
//             "${env.CCI_CURRENT_WEB_PROTOCOL}://${env.CODING_DOCKER_REG_HOST}",
//             "${env.CODING_ARTIFACTS_CREDENTIALS_ID}"
//           ) {
//             docker.image("${CODING_DOCKER_IMAGE_NAME}:${env.DOCKER_IMAGE_VERSION}").push()
//           }
//         }
//       }
//     }
//
  }
}
