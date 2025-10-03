package org.example.POOBet.Models;

import java.util.Random;

public class Aposta {
    private int idAposta;
    private double valorAposta;
    private Jogador jogador;
    private Jogo jogo;
    private String galo;

    public Aposta(double valorAposta, Jogador jogador, Jogo jogo, String galo) {
        this.idAposta = gerarIdAposta();
        this.valorAposta = valorAposta;
        this.jogador = jogador;
        this.jogo = jogo;
        this.galo = galo;
    }

    public String getGalo() {
        return galo;
    }

    public void setGalo(String galo) {
        this.galo = galo;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public int getIdAposta() {
        return idAposta;
    }

    public double getValorAposta() {
        return valorAposta;
    }

    public void setValorAposta(double valorAposta) {
        this.valorAposta = valorAposta;
    }

    public Jogador getJogadores() {
        return jogador;
    }

    public void setJogadores(Jogador jogador) {
        this.jogador = jogador;
    }

    public static int gerarIdAposta() {
        Random random = new Random();
        return random.nextInt(1_000_000);
    }
}
