def call( String fileName){
  def rubyVersion = '2.4.0'
  def puppetVersion = 'puppet6'
  node(label: 'beaker') {
    retry(2) {
      withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        echo "Running single Acceptance Test on ${fileName}."
        checkout scm
        def String setup = """/bin/bash -lc 'echo Package installation && \
        export BEAKER_PUPPET_COLLECTION=${puppetVersion} && \
        rm Gemfile.lock && \
        rvm use ${rubyVersion} && ruby --version && \
        gem install bundler && \
        mkdir -p spec/fixtures && \
        bundle config mirror.https://rubygems.org http://ci01.enterprisemodules.com:9292 && \
        bundle install --without unit_test quality release && \
        export DOCKER_HOST=tcp://ci02.enterprisemodules.com:2376 DOCKER_TLS_VERIFY=1 DOCKER_CERT_PATH=/usr/local/etc/jenkins/certs/ BEAKER_PUPPET_COLLECTION=puppet6 PUPPET_INSTALL_TYPE=agent && \
        echo $USERNAME && \
        docker login -u=$USERNAME -p=$PASSWORD && \
        docker pull centos:7 && \
        puppet --version &&\
        bundle exec rspec --tag ~@older_version ${fileName}'"""
        sh setup
      }
    }
  }
}
