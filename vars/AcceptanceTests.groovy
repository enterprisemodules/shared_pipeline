def call() {
  parallel(
    'Acceptance Tests \'spec/acceptance\'': { RunSingleAcceptanceTest('spec/acceptance') }
  )
}
