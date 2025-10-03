package org.example.POOBet.Models;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class Credito {
    private int idCredito;
    private double saldo;
    private LocalDate validade;
    private String moeda;
    private boolean bloqueado;
    private Jogador jogador;

    public Credito(double saldo, String moeda) {
        this.idCredito = gerarIdCredito();
        this.saldo = saldo;
        this.validade = LocalDate.now().plusMonths(3);;
        this.moeda = moeda;
        this.bloqueado = false;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public static int gerarIdCredito() {
        Random random = new Random();
        return random.nextInt(1_000_000);
    }
}
