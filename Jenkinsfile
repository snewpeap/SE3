node {
    stage("checkout") {
        checkout([
                $class                           : 'GitSCM',
                branches                         : [[name: '*/dev']],
                doGenerateSubmoduleConfigurations: false,
                extensions                       : [],
                submoduleCfg                     : [],
                userRemoteConfigs                : [
                        [credentialsId: 'se3-gitlab',
                         url          : 'http://212.129.149.40/171250558_teamnamecannotbeempty/backend-webtest.git']
                ]
        ])
    }
    def mvn = tool('maven3')
    env.PATH = "${mvn}/bin:@{env.PATH}"
    stage("mvn") {
        sh 'mvn clean package -Dmaven.test.skip=true'
        sh "cp {$WORKSPACE}/target/se3.jar /tmp/se3.jar"
    }
    def image
    stage("docker-build") {
        sh "docker build -f Dockerfile -t se3app:1.${BUILD_NUMBER} ."
    }
    stage("restart") {
        try {
            sh 'docker rm -f se3'
        } catch(ignored){
            echo('container\'s not running')
        }
        sh " docker run -p 9090:9090 -v /etc/localtime:/etc/localtime --link se3mysql:se3mysql --name se3 se3app:1.${BUILD_NUMBER}"
    }
}