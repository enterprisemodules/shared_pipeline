def call(String rubyVersion = '2.1.10', String puppetVersion = '4.0.0') {
  node(label: 'testslave') {
    withCredentials([usernamePassword(credentialsId: 'githublogin', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
      checkout scm
      def String setup = """/bin/bash -lc 'echo Package installation && \
      export PUPPET_GEM_VERSION=${puppetVersion} && \
      rm Gemfile.lock && \
      rvm use ${rubyVersion} && ruby --version && \
      gem install bundler && \
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
