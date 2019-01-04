def call(String rubyVersion = '1.9.3', String puppetVersion = '3.7.3') {
  node(label: 'testslave') {
    retry(3) {
      withCredentials([usernamePassword(credentialsId: 'githublogin', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
        checkout scm
        def String setup = """/bin/bash -lc 'echo Package installation && \
        export PUPPET_GEM_VERSION=${puppetVersion} && \
        rm Gemfile.lock && \
        rvm use ${rubyVersion} && ruby --version && \
        mkdir -p spec/fixtures && \
        bundle config mirror.https://rubygems.org http://ci01.enterprisemodules.com:9292 && \
        bundle install --without acceptance_test quality release && \
        ssh-keyscan -H github.com >> ~/.ssh/known_hosts && \
        echo unit test start && \
        bundle exec rake spec'"""
        sh setup
      }
    }
  }
}
