def call(String rubyVersion, String puppetVersion) {
  node(label: 'testslave') {
    withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
      checkout scm
      sh '''/bin/bash -lc "export PUPPET_GEM_VERSION=${puppetVersion} &&
      rm Gemfile.lock &&
      rvm use ${rubyVersion} && ruby --version &&
      gem install bundler"'''
      sh '''/bin/bash -lc "bundle install --without unit_test quality release"'''
      sh '''export DOCKER_HOST=tcp://ci02.enterprisemodules.com:2376 DOCKER_TLS_VERIFY=1 DOCKER_CERT_PATH=/usr/local/etc/jenkins/certs/ PUPPET_INSTALL_TYPE=agent PUPPET_INSTALL_VERSION=1.10.8 &&
      echo $USERNAME &&
      docker login -u=$USERNAME -p=$PASSWORD &&
      docker pull centos:7 &&
      rspec --tag ~@older_version spec/acceptance'''
    }
  }
}
