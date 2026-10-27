# 🎵 MusicTier 🎵

O **MusicTier** é um web app interativo criado para entretenimento e discussões musicais. A plataforma permite que os usuários criem sessões de avaliação, definam categorias personalizadas (tiers) com diferentes pesos e classifiquem músicas para gerar um ranking definitivo.

## Tecnologias Utilizadas

Este projeto foi construído utilizando uma arquitetura moderna dividida em Frontend e Backend:

* **Frontend:** React (construído com Vite)
* **Backend:** Java com Spring Boot
* **Banco de Dados:** PostgreSQL

## Como executar o projeto localmente

Para rodar o MusicTier na sua máquina, você precisará do Node.js, Java (JDK) e do PostgreSQL instalados.

### 1. Banco de Dados
Crie um banco de dados local no PostgreSQL chamado `musictier`.

### 2. Configurando e rodando o Backend (Spring Boot)
1. Navegue até a pasta `backend`.
2. Dentro de `src/main/resources`, crie um arquivo chamado `application-dev.properties`.
3. Adicione suas credenciais do banco de dados neste arquivo:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/musictier
   spring.datasource.username=SEU_USUARIO_POSTGRES
   spring.datasource.password=SUA_SENHA_POSTGRES
4. Execute a aplicação pela sua IDE ou via terminal com o Maven/Gradle. O backend rodará na porta 8080.

### 3. Configurando e rodando o Frontend (React/Vite)
1. Navegue até a pasta `frontend`
2. Crie um arquivo chamado `.env` na raiz da frontend
3. Escreva a linha no arquivo `.env`: VITE_API_BASE_URL=http://localhost:8080
4. Instale as dependências executando `npm install`
5. Inicie o servidor com `npm run dev`

## Road Map (Próximos passos)

### Futuras Features
* **Autenticação:** Implementação de um sistema de usuário com senha utilizando `Spring Security`.
* **Sessões Conjuntas:** Criação de salas multiplayer para que mais de um usuário avalie as músicas simultaneamente na mesma sessão.
* **UI/UX:** Mudanças estruturais com HTML/CSS e criação de um logotipo oficial para o site.

### Correção de Bugs
* **Estilo do Outline:** Corrigir o bug visual onde o outline de elementos selecionados está ficando com a cor branca
* **Pódio Customizado:** Atualizar a cor do Top 3 na lista final de ranking. 
* **Deleção em Cascata:** Corrigir a lógica de apagar sessões nop vanco de dados para garantir o apagamento em cascata de categorias e músicas atreladas à sessão excluída.


