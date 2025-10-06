package org.example.POOBet.Models;

import java.time.LocalDate;
import java.util.Random;

public class Jogador {
    private int idJogador;
    private String nome;
    private String cpf;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private Credito credito;

    public void abrirLinhaCredito(Credito credito) {
        setCredito(credito);
    }

    public Jogador(){}

    public Jogador(String nome, String cpf, String nacionalidade, LocalDate dataNascimento, Credito credito) {
        this.idJogador = gerarId();
        this.nome = nome;
        this.cpf = cpf;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
        this.credito = credito;
    }

    public int getIdJogador() {
        return idJogador;
    }

    public void setIdJogador(int idJogador) {
        this.idJogador = idJogador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Credito getCredito() {
        return credito;
    }

    public void setCredito(Credito credito) {
        this.credito = credito;
    }

    public static int gerarId() {
        Random random = new Random();
        return random.nextInt(1_000_000);
    }

    @Override
    public String toString() {
        return this.getNome() + " (ID: " + this.getIdJogador() + ")";
    }
}
