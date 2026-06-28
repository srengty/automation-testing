# Lab 09 — Jenkins Hands-On

Companion lab for **Lesson 09 · Jenkins** (`../jenkins.html`). Work through the
exercises in order; each maps to a feature in the lecture deck.

## Prerequisites

- **Java 17 or 21** on your `PATH` (`java -version`)
- One of: Docker, the `jenkins.war`, or a native install
- A Git repository you can push to (GitHub/GitLab) for the SCM exercises

## 0. Start Jenkins (pick one)

```bash
# A) Docker (recommended — reproducible)
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17

# B) Portable WAR
java -jar jenkins.war --httpPort=8080
```

Open <http://localhost:8080> and unlock with the initial admin password:

```bash
# Docker
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
# WAR
cat ~/.jenkins/secrets/initialAdminPassword
```

Install the **suggested plugins** and create an admin user.

## Exercise map

| # | Feature              | What you build                                          | File                         |
|---|----------------------|--------------------------------------------------------|------------------------------|
| 1 | Freestyle job        | `hello-freestyle` that prints build info               | —                            |
| 2 | Triggers             | Add `Build periodically` (`H/5 * * * *`)               | —                            |
| 3 | Declarative pipeline | 3-stage Build/Test/Deploy with `post`                  | `Jenkinsfile.declarative`    |
| 4 | Parameters           | `choice ENV` + `string VERSION`, conditional deploy    | `Jenkinsfile.parameters`     |
| 5 | Credentials          | Inject a masked secret with `credentials()`            | `Jenkinsfile.credentials`    |
| 6 | Agents               | Run a stage on a labelled agent                        | `Jenkinsfile.parallel`       |
| 7 | Reports & artifacts  | Publish JUnit + archive a jar                          | `Jenkinsfile.reports`        |
| 8 | Multibranch          | Auto-build every branch with a Jenkinsfile             | any of the above             |
| 9 | Shared library       | Reduce a Jenkinsfile to a couple of lines              | `vars/sayHello.groovy`       |

## How to use a Jenkinsfile

1. **New Item → Pipeline**.
2. Either paste a file's contents into the inline "Pipeline script" box, **or**
3. Choose "Pipeline script from SCM", point at your repo, and set the
   *Script Path* to the file name (e.g. `Lesson09/Lab09/Jenkinsfile.reports`).

## Capstone

Combine everything into a single **Multibranch Pipeline**:
Checkout → Build → parallel(Unit, Lint) → Package → (on `main`) Deploy,
publishing JUnit results, archiving the jar, injecting a credential, running on
a labelled agent, and notifying on failure. See the last slide of the deck.
