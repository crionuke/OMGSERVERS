#!/bin/bash
set -e

# HELP

help() {
  echo "OMGSERVERS cli, v1.0.0"
  echo "Usage:"
  if [ -z "$1" -o "$1" = "help" ]; then
    echo " omgservers help"
  fi
  # Env
  if [ -z "$1" -o "$1" = "env" -o "$1" = "env print" ]; then
    echo " omgservers env print"
  fi
  if [ -z "$1" -o "$1" = "env" -o "$1" = "env useLocal" ]; then
    echo " omgservers env useLocal"
  fi
  # Admin
  if [ -z "$1" -o "$1" = "admin" -o "$1" = "admin createTenant" ]; then
    echo " omgservers admin createTenant"
  fi
  if [ -z "$1" -o "$1" = "admin" -o "$1" = "admin createDeveloper" ]; then
    echo " omgservers admin createDeveloper"
  fi
  # Developer
  if [ -z "$1" -o "$1" = "developer" -o "$1" = "developer createToken" ]; then
    echo " omgservers developer createToken"
  fi
  if [ -z "$1" -o "$1" = "developer" -o "$1" = "developer createProject" ]; then
    echo " omgservers developer createProject"
  fi
  exit 0
}

# ENV

env_print() {
  env | grep OMGSERVERS_
  exit 0
}

env_useLocal() {
  echo "export OMGSERVERS_ENVIRONMENT=local" >> ~/.omgservers/environment
  echo "export OMGSERVERS_URL=http://localhost:8080" >> ~/.omgservers/environment.local
  echo "export OMGSERVERS_ADMIN_USERNAME=admin" >> ~/.omgservers/environment.local
  echo "export OMGSERVERS_ADMIN_PASSWORD=admin" >> ~/.omgservers/environment.local

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Environment was selected, ENVIRONMENT=$OMGSERVERS_ENVIRONMENT"

  exit 0
}

# ADMIN

admin_createTenant() {
  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) PUT ${OMGSERVERS_URL}/omgservers/admin-api/v1/request/create-tenant" \
    >> ~/.omgservers/logs

  curl -s -S -X PUT --fail-with-body \
    "${OMGSERVERS_URL}/omgservers/admin-api/v1/request/create-tenant" \
    -H "Content-type: application/json" \
    -u "${OMGSERVERS_ADMIN_USERNAME}:${OMGSERVERS_ADMIN_PASSWORD}" \
    -o ~/.omgservers/tmp/admin-create-tenant

  TENANT_ID=$(cat ~/.omgservers/tmp/admin-create-tenant | jq .id)
  if [ -z "$TENANT_ID" -o "$TENANT_ID" == "null" ]; then
    echo "ERROR: TENANT_ID was not received"
    exit 1
  fi

  echo "export OMGSERVERS_TENANT_ID=$TENANT_ID" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Tenant was created, TENANT_ID=$TENANT_ID"

  exit 0
}

admin_createDeveloper() {
  TENANT_ID=$OMGSERVERS_TENANT_ID
  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Using tenant, TENANT_ID=$TENANT_ID"

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) PUT ${OMGSERVERS_URL}/omgservers/admin-api/v1/request/create-developer" \
    >> ~/.omgservers/logs

  curl -s -S -X PUT --fail-with-body \
    "${OMGSERVERS_URL}/omgservers/admin-api/v1/request/create-developer" \
    -H "Content-type: application/json" \
    -u "${OMGSERVERS_ADMIN_USERNAME}:${OMGSERVERS_ADMIN_PASSWORD}" \
    -d "{ \"tenantId\": $TENANT_ID }" \
    -o ~/.omgservers/tmp/admin-create-developer

  USER_ID=$(cat ~/.omgservers/tmp/admin-create-developer | jq .userId)
  if [ -z "$USER_ID" -o "$USER_ID" == "null" ]; then
    echo "USER_ID was not received"
    exit 1
  fi
  echo "export OMGSERVERS_DEVELOPER_USER_ID=$USER_ID" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  PASSWORD=$(cat ~/.omgservers/tmp/admin-create-developer | jq .password)
  if [ -z "$PASSWORD" -o "$PASSWORD" == "null" ]; then
    echo "PASSWORD was not received"
    exit 1
  fi
  echo "export OMGSERVERS_DEVELOPER_PASSWORD=$PASSWORD" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Developer was created, USER_ID=$USER_ID, PASSWORD=$PASSWORD"

  exit 0
}

# DEVELOPER

developer_createToken() {
  DEVELOPER_USER_ID=$OMGSERVERS_DEVELOPER_USER_ID
  DEVELOPER_PASSWORD=$OMGSERVERS_DEVELOPER_PASSWORD

  if [ -z "${DEVELOPER_USER_ID}" -o -z "${DEVELOPER_PASSWORD}" ]; then
    echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) ERROR: Developer was not found"
    exit 1
  fi

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Using developer, USER_ID=$DEVELOPER_USER_ID"

  curl -s -S -X PUT --fail-with-body \
    "${OMGSERVERS_URL}/omgservers/developer-api/v1/request/create-token" \
    -H "Content-type: application/json" \
    -d "{ \"userId\": ${DEVELOPER_USER_ID}, \"password\": \"${DEVELOPER_PASSWORD}\" }" \
    -o ~/.omgservers/tmp/developer-create-token

  TOKEN=$(cat ~/.omgservers/tmp/developer-create-token | jq .rawToken)
  if [ -z "$TOKEN" -o "$TOKEN" == "null" ]; then
    echo "TOKEN was not received"
    exit 1
  fi
  echo "export OMGSERVERS_DEVELOPER_TOKEN=$TOKEN" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Token was created, USER_ID=$DEVELOPER_USER_ID"

  exit 0
}

developer_createProject() {
  TENANT_ID=$OMGSERVERS_TENANT_ID

  if [ -z "${TENANT_ID}" ]; then
    echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) ERROR: Tenant was not found"
    exit 1
  fi

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Using tenant, TENANT_ID=$TENANT_ID"

  DEVELOPER_TOKEN=$OMGSERVERS_DEVELOPER_TOKEN

  if [ -z "${DEVELOPER_TOKEN}" ]; then
    echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) ERROR: Developer token was not found"
    exit 1
  fi

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) PUT ${OMGSERVERS_URL}/omgservers/developer-api/v1/request/create-project" \
    >> ~/.omgservers/logs

  curl -s -S -X PUT --fail-with-body \
    "${OMGSERVERS_URL}/omgservers/developer-api/v1/request/create-project" \
    -H "Content-type: application/json" \
    -H "Authorization: Bearer ${DEVELOPER_TOKEN}" \
    -d "{ \"tenantId\": ${TENANT_ID} }" \
    -o ~/.omgservers/tmp/developer-create-project

  PROJECT_ID=$(cat ~/.omgservers/tmp/developer-create-project | jq .projectId)
  if [ -z "$PROJECT_ID" -o "$PROJECT_ID" == "null" ]; then
    echo "PROJECT_ID was not received"
    exit 1
  fi
  echo "export OMGSERVERS_PROJECT_ID=$PROJECT_ID" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  STAGE_ID=$(cat ~/.omgservers/tmp/developer-create-project | jq .stageId)
  if [ -z "$STAGE_ID" -o "$STAGE_ID" == "null" ]; then
    echo "STAGE_ID was not received"
    exit 1
  fi
  echo "export OMGSERVERS_STAGE_ID=$STAGE_ID" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  STAGE_SECRET=$(cat ~/.omgservers/tmp/developer-create-project | jq .secret)
  if [ -z "$STAGE_SECRET" -o "$STAGE_SECRET" == "null" ]; then
    echo "STAGE_SECRET was not received"
    exit 1
  fi
  echo "export OMGSERVERS_STAGE_SECRET=$STAGE_SECRET" >> ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}

  echo "$(date) $(echo $OMGSERVERS_ENVIRONMENT) Project was created, PROJECT_ID=$PROJECT_ID, STAGE_ID=$STAGE_ID"

  exit 0
}

# INTERNAL

internal_useEnv() {
  if [ ! -d "~/.omgservers/tmp" ]; then
    mkdir -p ~/.omgservers/tmp
  fi
  if [ ! -f "~/.omgservers/environment" ]; then
    touch ~/.omgservers/environment
  fi
  if [ ! -f "~/.omgservers/logs" ]; then
    touch ~/.omgservers/logs
  fi
  source ~/.omgservers/environment
  if [ -n "${OMGSERVERS_ENVIRONMENT}" ]; then
    source ~/.omgservers/environment.${OMGSERVERS_ENVIRONMENT}
  fi
}

# MAIN

internal_useEnv

if [ "$1" = "help" ]; then
  help
# Env
elif [ "$1" = "env" ]; then
  if [ "$2" = "print" ]; then
    env_print
  elif [ "$2" = "useLocal" ]; then
    env_useLocal
  else
    help env
  fi
# Admin
elif [ "$1" = "admin" ]; then
  if [ "$2" = "createTenant" ]; then
    admin_createTenant
  elif [ "$2" = "createDeveloper" ]; then
    admin_createDeveloper
  else
    help admin
  fi
# Developer
elif [ "$1" = "developer" ]; then
  if [ "$2" = "createToken" ]; then
    developer_createToken
  elif [ "$2" = "createProject" ]; then
    developer_createProject
  else
    help developer
  fi
else
  help
fi
