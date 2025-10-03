package org.example.POOBet.Services;

import org.example.POOBet.Models.Credito;
import org.example.POOBet.Models.Jogador;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JogadorService {

    private final List<Jogador> jogadoresCadastrados;

    public JogadorService() {
        jogadoresCadastrados = new ArrayList<>();
    }

    public void cadastrarJogador(String nome, String cpf, String nacionalidade, LocalDate dataNascimento, double saldo, String moeda) {
        Credito c = new Credito(saldo, moeda);
        Jogador j = new Jogador(nome, cpf, nacionalidade, dataNascimento, c);
        jogadoresCadastrados.add(j);

        System.out.println("LOG DE SERVIÇO: Jogador " + nome + " adicionado à lista.");
    }

    public List<Jogador> getJogadoresCadastrados() {
        return jogadoresCadastrados;
    }

    public Jogador encontrar(int id){
        for (Jogador jogador : jogadoresCadastrados) {
            if(id == jogador.getIdJogador()){
                return jogador;
            }
        }
        return null;
    }

    public void listarJogadores(){
        for(Jogador jogador: jogadoresCadastrados){
            System.out.println("(" + jogador.getIdJogador() + ") " + jogador.getNome());
            System.out.println("CPF: " + jogador.getCpf());
            System.out.println("Nacionalidade: " + jogador.getNacionalidade());
            System.out.println("Saldo: " + jogador.getCredito().getSaldo());
        }
    }

    public Jogador buscarPorId(int id){
        for(Jogador jogador: jogadoresCadastrados){
            if(id == jogador.getIdJogador()){
                return jogador;
            }
        }
        return null;
    }


}