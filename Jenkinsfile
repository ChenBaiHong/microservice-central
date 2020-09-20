// git 配置ID
def git_auth = "13ef9ccf-95f7-4409-b163-f2e6e1d5f037"
// git 仓库地址
def git_url = "http://gitlab.chenbaihoo.com/baihoo/microservice-central.git"
// docker 镜像私服地址
def harbor_url = "swr.ap-southeast-1.myhuaweicloud.com"
// docker 镜像私服镜像仓库
def repository_name = "microservice-central"
// 镜像标签 同 项目版本一致
def tag = "2.0.1"
node {
    stage('拉取代码'){
        checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: true]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${git_auth}", url: "${git_url}"]]])
    }
    stage('代码审查') {
        def scannerHome = tool 'sonarqube-scanner'
        withSonarQubeEnv('sonarqube') {
            sh """
                cd ${project_name}
                ${scannerHome}/bin/sonar-scanner
               """
        }
    }
    stage('编译，安装公共组件'){
        sh "gradle -q -p central-config clean install"
        sh "gradle -q -p component-integration clean install"
    }
    stage('编译，构建微服务工程，上传镜像'){
        // gradle 打包微服务工程并推送 docker 镜像
        sh "gradle -q -p ${project_name} clean build dockerBuildImage dockerPushImage"
    }
    stage('部署远程服务器应用'){
       //根据当前父子级项目结构，截取要部署的子项目名称
       def selectedProjects = "${project_name}".split('/')
       def sub_project_name = selectedProjects[selectedProjects.size()-1]
       sshPublisher(publishers: [sshPublisherDesc(configName: '192.168.1.88', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/home/myscript/jenkins_shell/hwDeploy.sh $harbor_url $repository_name $sub_project_name $tag $port", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
    }
}
