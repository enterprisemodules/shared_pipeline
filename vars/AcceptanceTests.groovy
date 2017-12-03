def call() {
  failFast true
  parallel(
    'Acceptance Tests \'spec/acceptance\'': { RunSingleAcceptanceTest('spec/acceptance') }
  )
}
