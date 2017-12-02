def call( String fileName){
  def rubyVersion = '2.4.0'
  def puppetVersion = '5.1.0'
  node(label: 'testslave') {
    withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      echo "Running single Acceptance Test on ${fileName}."
      checkout scm
      def String setup = """/bin/bash -lc 'echo Package installation && \
      export PUPPET_GEM_VERSION=${puppetVersion} && \
      rm Gemfile.lock && \
      rvm use ${rubyVersion} && ruby --version && \
      mkdir -p spec/fixtures && \
      gem install bundler && \
      bundle install --without unit_test quality release && \
      export DOCKER_HOST=tcp://ci02.enterprisemodules.com:2376 DOCKER_TLS_VERIFY=1 DOCKER_CERT_PATH=/usr/local/etc/jenkins/certs/ PUPPET_INSTALL_TYPE=agent PUPPET_INSTALL_VERSION=1.10.8 && \
      echo $USERNAME && \
      docker login -u=$USERNAME -p=$PASSWORD && \
      docker pull centos:7 && \
      bundle exec rspec --tag ~@older_version ${fileName}'"""
      sh setup
    }
  }
}
