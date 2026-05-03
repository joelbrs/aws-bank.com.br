# Authorizer

Descrição
1. O projeto contém o `authorizer`, que é uma AWS Lambda function escrita em Java responsável por validar tokens JWT e gerar políticas para API Gateway.
2. O empacotamento é feito como um *flat jar* (jar com dependências).

Pré-requisitos
1. Java 11+ e Maven instalados.
2. Para deploy local: `LocalStack` ou similar.
3. Para deploy em nuvem: credenciais AWS configuradas (CLI/Env).

Build e testes
1. Executar os testes:
    - `mvn -f authorizer/pom.xml test`
2. Gerar o flat jar (shade/fat jar):
    - `mvn -f authorizer/pom.xml clean package`
3. O artefato será gerado em `authorizer/target` (ex.: ``authorizer-<versão>-shaded.jar``).

Deploy
1. Existem duas opções: deploy em `LocalStack` ou na `AWS`.
2. Para deploy, utilizar o diretório `infra-as-code`. Cada environment está separado em pastas dentro de `infra-as-code`.
3. Selecionar a pasta do environment desejado e executar o script:
    - Exemplo:
        - `cd infra-as-code/dev`
        - `./run.sh`
4. O `run.sh` deve realizar o deploy da Lambda (subir/atualizar função) apontando para o jar gerado em `authorizer/target`.

Variáveis de ambiente importantes
- `ENVIRONMENT` (ex.: `LOCAL`, `DEV`, `PROD`)
- `KEYCLOAK_URL`, `KEYCLOAK_REALM`, `EXPECTED_AUDIENCE`
  Definir conforme necessário antes do deploy.

Observações
1. As instruções de deploy dependem do conteúdo de cada pasta dentro de `infra-as-code` (scripts de provisionamento, templates, etc.).
2. Garantir que o jar empacotado contenha todas as dependências (uso do `maven-shade-plugin`).