// Exercise 9 — a global step for a Shared Library.
// Put this repo's root as a "Global Pipeline Library" named 'itc-shared'
// (Manage Jenkins -> System -> Global Pipeline Libraries).
//
// Then in any Jenkinsfile:
//   @Library('itc-shared') _
//   sayHello('ITC')
//
// Files under vars/ become callable steps named after the file.
def call(String name = 'world') {
    echo "Hello, ${name}! (from the itc-shared library)"
}
