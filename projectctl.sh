#!/bin/bash
set -e

# INTERNAL

internal_help() {
  echo "PROJECT ctl, v1.0.0"
  echo "Usage:"
  if [ -z "$1" -o "$1" = "help" ]; then
    echo " projectctl.sh help"
  fi
  # Build
  if [ -z "$1" -o "$1" = "build" -o "$1" = "build printVersion" ]; then
    echo " projectctl.sh build printVersion"
  fi
  if [ -z "$1" -o "$1" = "build" -o "$1" = "build withTests" ]; then
    echo " projectctl.sh build withTests"
  fi
  if [ -z "$1" -o "$1" = "build" -o "$1" = "build withoutTests" ]; then
    echo " projectctl.sh build withoutTests"
  fi
  if [ -z "$1" -o "$1" = "build" -o "$1" = "build testWithSonar" ]; then
    echo " projectctl.sh build testWithSonar"
  fi
  if [ -z "$1" -o "$1" = "build" -o "$1" = "build beforeCommit" ]; then
    echo " projectctl.sh build beforeCommit"
  fi
  if [ -z "$1" -o "$1" = "build" -o "$1" = "build pushImages" ]; then
    echo " projectctl.sh build pushImages"
  fi
  # Environment
  if [ -z "$1" -o "$1" = "environment" -o "$1" = "environment printCurrent" ]; then
    echo " projectctl.sh environment printCurrent"
  fi
  if [ -z "$1" -o "$1" = "environment" -o "$1" = "environment down" ]; then
    echo " projectctl.sh environment down"
  fi
  # Localtesting
  if [ -z "$1" -o "$1" = "localtesting" -o "$1" = "localtesting up" ]; then
    echo " projectctl.sh localtesting up"
  fi
  if [ -z "$1" -o "$1" = "localtesting" -o "$1" = "localtesting ps" ]; then
    echo " projectctl.sh localtesting ps"
  fi
  if [ -z "$1" -o "$1" = "localtesting" -o "$1" = "localtesting test" ]; then
    echo " projectctl.sh localtesting test"
  fi
  # Development
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development up" ]; then
    echo " projectctl.sh development up"
  fi
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development ps" ]; then
    echo " projectctl.sh development ps"
  fi
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development reset" ]; then
    echo " projectctl.sh development reset"
  fi
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development logs" ]; then
    echo " projectctl.sh development logs"
  fi
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development database" ]; then
    echo " projectctl.sh development database"
  fi
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development ctl" ]; then
    echo " projectctl.sh development ctl"
  fi
  if [ -z "$1" -o "$1" = "development" -o "$1" = "development test" ]; then
    echo " projectctl.sh development test"
  fi
  # Integration
  if [ -z "$1" -o "$1" = "integration" -o "$1" = "integration up" ]; then
    echo " projectctl.sh integration up"
  fi
  if [ -z "$1" -o "$1" = "integration" -o "$1" = "integration ps" ]; then
    echo " projectctl.sh integration ps"
  fi
  if [ -z "$1" -o "$1" = "integration" -o "$1" = "integration reset" ]; then
    echo " projectctl.sh integration reset"
  fi
  if [ -z "$1" -o "$1" = "integration" -o "$1" = "integration logs" ]; then
    echo " projectctl.sh integration logs"
  fi
  if [ -z "$1" -o "$1" = "integration" -o "$1" = "integration test" ]; then
    echo " projectctl.sh integration test"
  fi
  # Standalone
  if [ -z "$1" -o "$1" = "standalone" -o "$1" = "standalone up" ]; then
    echo " projectctl.sh standalone up"
  fi
  if [ -z "$1" -o "$1" = "standalone" -o "$1" = "standalone ps" ]; then
    echo " projectctl.sh standalone ps"
  fi
  if [ -z "$1" -o "$1" = "standalone" -o "$1" = "standalone reset" ]; then
    echo " projectctl.sh standalone reset"
  fi
  if [ -z "$1" -o "$1" = "standalone" -o "$1" = "standalone logs" ]; then
    echo " projectctl.sh standalone logs"
  fi
  if [ -z "$1" -o "$1" = "standalone" -o "$1" = "standalone test" ]; then
    echo " projectctl.sh standalone test"
  fi
}

internal_ask_down() {
  ENVIRONMENTS=$1

  CURRENT_ENVIRONMENT=$(docker compose ls -q | grep -E "${ENVIRONMENTS}" || echo "")
  if [ -n "${CURRENT_ENVIRONMENT}" ]; then
    read -p "Does \"${CURRENT_ENVIRONMENT}\" has to be to down (y/n)? : " ANSWER
    if [ "${ANSWER}" == "y" ]; then
      docker compose -p ${CURRENT_ENVIRONMENT} down -v
    else
      echo "Operation was cancelled"
      exit 0
    fi
  fi
}

# HANDLERS

build_printVersion() {
  ./mvnw -B help:evaluate -Dexpression=project.version -q -DforceStdout
}

build_withTests() {
  ./mvnw -B install
}

build_withoutTests() {
  ./mvnw -B install -DskipTests
}

build_testWithSonar() {
  ./mvnw -B install org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=OMGSERVERS_omgservers
}

build_beforeCommit() {
  environment_down
  build_withTests
  integration_test

  environment_down
}

build_pushImages() {
  ./mvnw -B install -DskipTests -Dquarkus.container-image.push=true
}

environment_printCurrent() {
  docker compose ls
}

environment_down() {
  read -p 'Continue (y/n)? : ' ANSWER
  if [ "${ANSWER}" == "y" ]; then
    docker compose -p localtesting down -v
    docker compose -p development down -v
    docker compose -p integration down -v
    docker compose -p standalone down -v
  else
    echo "Operation was cancelled"
  fi
}

localtesting_up() {
  internal_ask_down "integration|development|standalone"

  OMGSERVERS_VERSION=$(build_printVersion)

  if [ -z "${OMGSERVERS_VERSION}" ]; then
    echo "$(date) ERROR: Current version was not detected, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"
    exit 1
  fi

  echo "$(date) Using version, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"

  OMGSERVERS_VERSION=${OMGSERVERS_VERSION} docker compose -p localtesting -f omgservers-environments/localtesting-environment/src/compose.yaml up --remove-orphans -d
  docker compose -p localtesting ps
}

localtesting_ps() {
  docker compose -p localtesting ps
}

localtesting_test() {
  OMGSERVERS_TESTER_ENVIRONMENT=LOCALTESTING \
    ./mvnw -B -Dquarkus.test.profile=test -DskipITs=false -f pom.xml verify
}

development_up() {
  internal_ask_down "localtesting|integration|standalone"

  OMGSERVERS_VERSION=$(build_printVersion)

  if [ -z "${OMGSERVERS_VERSION}" ]; then
    echo "$(date) ERROR: Current version was not detected, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"
    exit 1
  fi

  echo "$(date) Using version, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"

  OMGSERVERS_VERSION=${OMGSERVERS_VERSION} docker compose -p development -f omgservers-environments/development-environment/src/compose.yaml up --remove-orphans -d
  docker compose -p development ps
}

development_ps() {
  docker compose -p development ps
}

development_reset() {
  read -p 'Continue (y/n)? : ' ANSWER
  if [ "${ANSWER}" == "y" ]; then
    docker compose -p development down -v
    development_up
  else
    echo "Operation was cancelled"
  fi
}

development_logs() {
  docker compose -p development logs $@
}

development_database() {
  docker compose -p development exec database psql
}

development_ctl() {
  docker compose -p development exec ctl /bin/bash
}

development_test() {
  development_up

  OMGSERVERS_TESTER_ENVIRONMENT=DEVELOPMENT \
    ./mvnw -B -Dquarkus.test.profile=test -DskipITs=false -f pom.xml verify
}

integration_up() {
  internal_ask_down "localtesting|development|standalone"

  OMGSERVERS_VERSION=$(build_printVersion)

  if [ -z "${OMGSERVERS_VERSION}" ]; then
    echo "$(date) ERROR: Current version was not detected, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"
    exit 1
  fi

  echo "$(date) Using version, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"

  OMGSERVERS_VERSION=${OMGSERVERS_VERSION} docker compose -p integration -f omgservers-environments/integration-environment/src/compose.yaml up --remove-orphans -d
  docker compose -p integration ps
}

integration_ps() {
  docker compose -p integration ps
}

integration_reset() {
  read -p 'Continue (y/n)? : ' ANSWER
  if [ "${ANSWER}" == "y" ]; then
    docker compose -p integration down -v
    integration_up
  else
    echo "Operation was cancelled"
  fi
}

integration_logs() {
  docker compose -p integration logs $@
}

integration_test() {
  integration_up

  OMGSERVERS_TESTER_ENVIRONMENT=INTEGRATION \
      ./mvnw -B -Dquarkus.test.profile=test -DskipITs=false -f pom.xml verify
}

standalone_up() {
  internal_ask_down "localtesting|development|integration"

  OMGSERVERS_VERSION=$(build_printVersion)

  if [ -z "${OMGSERVERS_VERSION}" ]; then
    echo "$(date) ERROR: Current version was not detected, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"
    exit 1
  fi

  echo "$(date) Using version, OMGSERVERS_VERSION=${OMGSERVERS_VERSION}"

  OMGSERVERS_VERSION=${OMGSERVERS_VERSION} docker compose -p standalone -f omgservers-environments/standalone-environment/src/compose.yaml up --remove-orphans -d
  docker compose -p standalone ps
}

standalone_ps() {
  docker compose -p standalone ps
}

standalone_reset() {
  read -p 'Continue (y/n)? : ' ANSWER
  if [ "${ANSWER}" == "y" ]; then
    docker compose -p standalone down -v
    standalone_up
  else
    echo "Operation was cancelled"
  fi
}

standalone_logs() {
  docker compose -p standalone logs $@
}

standalone_test() {
  standalone_up

  OMGSERVERS_TESTER_ENVIRONMENT=STANDALONE \
        ./mvnw -B -Dquarkus.test.profile=test -DskipITs=false -f pom.xml verify
}

# Build
if [ "$1" = "build" ]; then
  if [ "$2" = "printVersion" ]; then
    build_printVersion
  elif [ "$2" = "withTests" ]; then
    build_withTests
  elif [ "$2" = "withoutTests" ]; then
    build_withoutTests
  elif [ "$2" = "testWithSonar" ]; then
    build_testWithSonar
  elif [ "$2" = "beforeCommit" ]; then
    build_beforeCommit
  elif [ "$2" = "pushImages" ]; then
    build_pushImages
  else
    internal_help "build"
  fi
# Environment
elif [ "$1" = "environment" ]; then
  if [ "$2" = "printCurrent" ]; then
    environment_printCurrent
  elif [ "$2" = "down" ]; then
    environment_down
  else
    internal_help "environment"
  fi
elif [ "$1" = "localtesting" ]; then
  if [ "$2" = "up" ]; then
    localtesting_up
  elif [ "$2" = "ps" ]; then
    localtesting_ps
  elif [ "$2" = "test" ]; then
    localtesting_test
  else
    internal_help "localtesting"
  fi
elif [ "$1" = "development" ]; then
  if [ "$2" = "up" ]; then
    development_up
  elif [ "$2" = "ps" ]; then
    development_ps
  elif [ "$2" = "reset" ]; then
    development_reset
  elif [ "$2" = "logs" ]; then
    development_logs "${@:3}"
  elif [ "$2" = "database" ]; then
    development_database
  elif [ "$2" = "ctl" ]; then
    development_ctl
  elif [ "$2" = "test" ]; then
    development_test
  else
    internal_help "development"
  fi
elif [ "$1" = "integration" ]; then
  if [ "$2" = "up" ]; then
    integration_up
  elif [ "$2" = "ps" ]; then
    integration_ps
  elif [ "$2" = "reset" ]; then
    integration_reset
  elif [ "$2" = "logs" ]; then
    integration_logs "${@:3}"
  elif [ "$2" = "test" ]; then
    integration_test
  else
    internal_help "integration"
  fi
elif [ "$1" = "standalone" ]; then
  if [ "$2" = "up" ]; then
    standalone_up
  elif [ "$2" = "ps" ]; then
    standalone_ps
  elif [ "$2" = "reset" ]; then
    standalone_reset
  elif [ "$2" = "logs" ]; then
    standalone_logs "${@:3}"
  elif [ "$2" = "test" ]; then
    standalone_test
  else
    internal_help "standalone"
  fi
else
  internal_help
fi