def call() {
  def rubyVersion = '2.4.0'
  def puppetVersion = '5.1.0'
  node(label: 'testslave') {
    retry(3) {
      withCredentials([usernamePassword(credentialsId: 'githublogin', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
        checkout scm
        def String setup = """/bin/bash -lc 'echo Package installation && \
        export PUPPET_GEM_VERSION=${puppetVersion} && \
        rm Gemfile.lock && \
        rvm use ${rubyVersion} && ruby --version && \
        gem install bundler && \
        bundle config mirror.https://rubygems.org http://ci01.enterprisemodules.com:9292 && \
        bundle install --without acceptance_test unit_test release && \
        ssh-keyscan -H github.com >> ~/.ssh/known_hosts && \
        echo Quality Check start && \
        git config --global user.name "jenkins Agent" && \
        git config --global user.email info@enterprisemodules.com && \
        bundle exec overcommit --sign && \
        bundle exec overcommit --run '"""
        sh setup
      }
    }    
  }
}
