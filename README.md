# 💳 Sistema de Transações Bancárias

Este é um sistema bancário simplificado que permite cadastro de usuários, login seguro, criação e acompanhamento de transações financeiras, geração de extratos e métricas de movimentações mensais.

## 📋 Funcionalidades

### 1. Cadastro de Usuário

- Cadastro realizado com:
  - CPF
  - Nome
  - Sobrenome
  - E-mail
  - Senhas numéricas:
    - 8 dígitos para acesso (login)
    - 6 dígitos para transações
- Envio de um código TOTP (válido por 10 minutos) por e-mail para confirmação do cadastro.

### 2. Confirmação de Cadastro

- O usuário recebe um link de confirmação por e-mail.
- O cadastro só será realizado se:
  - O link de confirmação for válido.
  - O link não estiver expirado.
- Caso contrário, o cadastro será cancelado/ignorado.

### 3. Login do Usuário

- Acesso apenas para usuários previamente cadastrados e confirmados.
- Requer:
  - Número da Conta
  - Senha de 8 dígitos
  - Código TOTP enviado por e-mail

### 4. Criação de Transação

1. O usuário informa:
   - Conta de destino
   - Valor da transação
2. Os dados da conta de destino são exibidos para conferência.
3. O usuário confirma os dados.
4. O usuário insere a senha de 6 dígitos de transação.
   - Até 3 tentativas permitidas.
   - Após 2 erros consecutivos, o contador é zerado (tolerância alternada).
   - Se a senha estiver correta:
     - A transação é criada com status **"Em processamento"**
     - O extrato é renderizado

### 5. Detalhamento de Transações

- O usuário pode:
  - Ver detalhes de uma transação específica
  - Gerar comprovante em PDF (Opcional)

### 6. Listagem de Transações

- Lista de todas as transações realizadas
- Suporte a **paginação**

### 7. Métricas de Transações Mensais

- Geração de métricas agrupadas por mês com dados consolidados de transações.

### 8. Extrato Bancário por Período

- O usuário pode solicitar o extrato de um período personalizado.
- O extrato é enviado posteriormente **por e-mail**.

## 🛡️ Segurança

- Todas as senhas devem ser armazenadas com **hash** seguro.
- Os códigos TOTP têm validade limitada (10 minutos).
- Tentativas de senha são controladas para evitar brute-force.
- As informações de conta de destino são confirmadas antes da transação.