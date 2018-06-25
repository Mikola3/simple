def GITHUB_REPOSITORY = "https://github.com/Mikola3/simple-java-maven-app.git"
def GITHUB_BRANCH = "master"
def Jobs = []
for (int i = 1; i <3; i++) {
    Jobs << "child${i}-job"
    job("${Jobs.last()}"){
      scm {
          git("${GITHUB_REPOSITORY}", "${GITHUB_BRANCH}")
      }
      triggers {
          scm('* * * * *')
      }
      steps {
          jdk('8u161')
      }
      steps {
          maven {
              goals('clean')
              goals('verify')
              mavenOpts('-Xms256m')
              mavenOpts('-Xmx512m')
              localRepository(LocalRepositoryLocation.LOCAL_TO_WORKSPACE)
              properties(skipTests: true)
              mavenInstallation('Maven 3.5.3')
          }
      }
      steps {
          shell("java -jar target/my-app-1.0-SNAPSHOT.jar")
      }
      queue("child${i}-job") // run job
  }
}
