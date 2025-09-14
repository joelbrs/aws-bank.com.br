# ✅ Checklist de Funcionalidades

## 1. Cadastro de Usuário
- [ ] Cadastro com CPF, Nome, Sobrenome, E-mail
  - [ ] Front
  - [x] Back
- [ ] Senha de 8 dígitos para acesso
  - [ ] Front
  - [x] Back
- [ ] Senha de 6 dígitos para transações
  - [ ] Front
  - [x] Back
- [ ] Envio de TOTP por e-mail (válido por 10 minutos)
  - [ ] Front
  - [x] Back

## 2. Login do Usuário
- [ ] Verificação de cadastro confirmado
  - [ ] Front
  - [x] Back
- [ ] Login com CPF e Senha de 8 dígitos
  - [ ] Front
  - [x] Back
- [ ] Validação de TOTP por e-mail
  - [ ] Front
  - [ ] Back

## 3. Criação de Transação
- [ ] Inserção da conta de destino e valor da transação
  - [ ] Front
  - [x] Back
- [ ] Renderização das informações da conta destino
  - [ ] Front
  - [x] Back
- [ ] Confirmação das informações da conta destino
  - [ ] Front
  - [N/A] Back
- [ ] Validação da senha de 6 dígitos
  - [ ] Front
  - [ ] Back
  - [ ] Máximo de 3 tentativas permitidas
    - [ ] Front
    - [ ] Back
  - [ ] Reset do contador após 2 erros consecutivos alternados
    - [ ] Front
    - [ ] Back
  - [ ] Bloqueio após 3 tentativas consecutivas
    - [ ] Front
    - [ ] Back
- [ ] Criação da transação com status "Em processamento"
  - [N/A] Front
  - [x] Back
- [ ] Renderização do extrato após transação
  - [ ] Front
  - [x] Back

## 4. Detalhamento de Transações
- [ ] Visualizar detalhes de uma transação específica
  - [ ] Front
  - [x] Back

## 5. Listagem de Transações
- [ ] Listagem de transações realizadas com paginação
  - [ ] Front
  - [ ] Back

## 6. Métricas de Transações Mensais
- [ ] Geração de métricas consolidadas mensalmente
  - [ ] Front
  - [x] Back

## 7. Extrato Bancário por Período
- [ ] Solicitação de extrato por período específico
  - [ ] Front
  - [x] Back
- [ ] Envio do extrato por e-mail (não imediato)
  - [ ] Front
  - [ ] Back
- [ ] Geração de extrato em PDF
  - [ ] Front
  - [ ] Back