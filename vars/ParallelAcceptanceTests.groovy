def call() {
  node(label: 'beaker') {
    withCredentials([usernamePassword(credentialsId: 'githublogin', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
      checkout scm
      def specs = findFiles(glob: 'spec/acceptance/**/*_spec.rb')
      hash = [:]
      specs.each {
        def specName = it.toString()
        hash.put("Acceptance Tests '${specName}'", { RunSingleAcceptanceTest(specName) })
      }
//      hash.put('failFast', true)
      parallel(hash)
    }
  }
}
