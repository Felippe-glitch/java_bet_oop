# 📌 Java Bet

Aplicação desktop simples para simular o gerenciamento de uma casa de apostas, desenvolvida em **Java com JavaFX**.

## 🖼️ Sobre o Projeto
Este projeto foi desenvolvido como parte da avaliação da disciplina de **Programação Orientada a Objetos I**.  

A arquitetura foi pensada para aplicar os conceitos fundamentais de **POO**, separando as responsabilidades em:

- **Models**: Classes que representam as entidades do sistema (Jogador, Jogo, Aposta, Credito).  
- **Services**: Classes que contêm a lógica de negócio e as regras da aplicação.  
- **View/Controller**: A classe principal (`Sistema.java`) que gerencia a interface gráfica e o fluxo de telas.  

---

## ✨ Funcionalidades
### 🎮 Gerenciamento de Jogadores
- Cadastro completo de novos apostadores.  
- Validação de dados de entrada.  

### 🏆 Gerenciamento de Jogos
- Criação de novos eventos com limites de aposta.  
- Início de jogos com sorteio aleatório de um vencedor.  

### 💰 Sistema de Apostas
- Interface para realizar apostas, com seleção de jogador, jogo e competidores.  
- Validação de saldo do jogador antes de confirmar a aposta.  
- Pagamento automático dos prêmios aos vencedores após o fim de um jogo.  

### 📊 Visualização
- Histórico detalhado de todas as apostas realizadas.  

---

## 🛠️ Tecnologias Utilizadas
- **Java (JDK 22)**  
- **JavaFX** (para a interface gráfica)  
- **IntelliJ IDEA**  
- **Maven** 

---

