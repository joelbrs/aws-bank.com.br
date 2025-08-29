# ✅ Checklist de Funcionalidades

## 1. Cadastro de Usuário
- [ ] Cadastro com CPF, Nome, Sobrenome, E-mail
- [ ] Senha de 8 dígitos para acesso
- [ ] Senha de 6 dígitos para transações
- [ ] Envio de TOTP por e-mail (válido por 10 minutos)

## 2. Confirmação de Cadastro
- [ ] Envio de link de confirmação por e-mail
- [ ] Validação do link de confirmação
- [ ] Cadastro só é concluído se link for válido e não expirado

## 3. Login do Usuário
- [ ] Verificação de cadastro confirmado
- [ ] Login com Número da Conta e Senha de 8 dígitos
- [ ] Validação de TOTP por e-mail

## 4. Criação de Transação
- [ ] Inserção da conta de destino e valor da transação
- [ ] Renderização das informações da conta destino
- [ ] Confirmação das informações da conta destino
- [ ] Validação da senha de 6 dígitos
  - [ ] Máximo de 3 tentativas permitidas
  - [ ] Reset do contador após 2 erros consecutivos alternados
  - [ ] Bloqueio após 3 tentativas consecutivas
- [ ] Criação da transação com status "Em processamento"
- [ ] Renderização do extrato após transação

## 5. Detalhamento de Transações
- [ ] Visualizar detalhes de uma transação específica
- [ ] *(Opcional)* Gerar comprovante em PDF

## 6. Listagem de Transações
- [ ] Listagem de transações realizadas com paginação

## 7. Métricas de Transações Mensais
- [ ] Geração de métricas consolidadas mensalmente

## 8. Extrato Bancário por Período
- [ ] Solicitação de extrato por período específico
- [ ] Envio do extrato por e-mail (não imediato)
- [ ] *(Opcional)* Geração de extrato em PDF