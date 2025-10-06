package org.example.POOBet.Models;

import java.util.ArrayList;
import java.util.List;

public class Jogo {

    private static int contadorId = 0;
    private final int idJogo;
    private final String galo1;
    private final String galo2;
    private final double apostaMax;
    private final double apostaMin;
    private final List<Integer> apostas;

    public Jogo(String galo1, String galo2, double apostaMax, double apostaMin) {
        this.idJogo = ++contadorId;
        this.apostaMax = apostaMax;
        this.apostaMin = apostaMin;
        this.galo1 = galo1;
        this.galo2 = galo2;
        this.apostas = new ArrayList<>();
    }

    public int getIdJogo() {
        return idJogo;
    }

    public String getGalo1() {
        return galo1;
    }

    public String getGalo2() {
        return galo2;
    }

    public String getDescricao() {
        return this.galo1 + " vs " + this.galo2;
    }

    public double getApostaMax() {
        return apostaMax;
    }

    public double getApostaMin() {
        return apostaMin;
    }

    public List<Integer> getApostas() {
        return apostas;
    }

    public void adicionarAposta(Aposta aposta) {
        this.apostas.add(aposta.getIdAposta());
    }

    @Override
    public String toString() {
        return this.getDescricao() + " (ID: " + this.getIdJogo() + ")";
    }
}