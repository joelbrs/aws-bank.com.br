cdklocal destroy -y && awslocal logs delete-log-group \
                         --log-group-name /aws/lambda/keycloak-jwt-authorizer

sleep 5

cd ../../authorizer && mvn package

sleep 5

cd ../infra-as-code/local && cdklocal bootstrap

sleep 10

cdklocal deploy --all --require-approval never