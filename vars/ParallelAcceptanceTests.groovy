def call() {
  node(label: 'beaker') {
    withCredentials([usernamePassword(credentialsId: 'githublogin', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
      checkout scm
      def specs = findFiles(glob: 'spec/acceptance/**/*_spec.rb')
      hash = [:]
      specs.each {
        def specName = it.toString()
        hash["Acceptance Tests '${specName}'"] = { RunSingleAcceptanceTest(specName) }
      }
      hash['failFast'] = true
      parallel(hash)
    }
  }
}
