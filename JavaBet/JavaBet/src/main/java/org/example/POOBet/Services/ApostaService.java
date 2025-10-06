package org.example.POOBet.Services;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.POOBet.Models.Aposta;
import org.example.POOBet.Models.Jogador;
import org.example.POOBet.Models.Jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApostaService {

    // =========================================================================
    // ======================== [ ESTILO / APARÊNCIA UI ] ======================
    // =========================================================================

    private static final String COR_FUNDO = "#2E2E2E";
    private static final String COR_TEXTO = "#E0E0E0";
    private static final String ESTILO_LABEL_FORM = "-fx-font-size: 14px; -fx-text-fill: " + COR_TEXTO + ";";
    private static final String ESTILO_BOTAO_PADRAO = "-fx-background-color: #555555; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_PADRAO_HOVER = "-fx-background-color: #007ACC; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_TEXT_FIELD = "-fx-background-color: #3E3E3E; -fx-text-fill: white; -fx-prompt-text-fill: #9E9E9E;";
    private static final String ESTILO_TEXT_AREA = "-fx-control-inner-background:#3E3E3E; -fx-font-family: Consolas; -fx-text-fill: white; -fx-font-size: 14px;";

    private void estilizarDialogo(DialogPane dialogPane) {
        dialogPane.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: " + COR_TEXTO + "; -fx-font-size: 14px;");
        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: " + COR_FUNDO + ";");
        dialogPane.getButtonTypes().stream()
                .map(dialogPane::lookupButton)
                .forEach(node -> {
                    if (node instanceof Button) estilizarBotaoPadrao((Button) node);
                });
    }

    private void estilizarBotaoPadrao(Button button) {
        button.setStyle(ESTILO_BOTAO_PADRAO);
        button.setPrefSize(100, 30);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_PADRAO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_PADRAO));
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        estilizarDialogo(alert.getDialogPane());
        alert.showAndWait();
    }

    // =========================================================================
    // ======================== [ REGRA DE NEGÓCIO ] ===========================
    // =========================================================================

    private final List<Aposta> apostasCadastradas = new ArrayList<>();
    private final JogoService jogoService;
    private final JogadorService jogadorService;

    public ApostaService(JogoService jogoService, JogadorService jogadorService) {
        this.jogoService = jogoService;
        this.jogadorService = jogadorService;
    }

    public List<Aposta> getApostasCadastradas() {
        return apostasCadastradas;
    }

    public void listarApostas() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Histórico de Apostas");
        dialog.setHeaderText("Todas as apostas registradas no sistema.");

        // ESTILIZAÇÃO DA UI
        estilizarDialogo(dialog.getDialogPane());

        if (apostasCadastradas.isEmpty()) {
            dialog.getDialogPane().setContent(new Label("Nenhuma aposta foi feita ainda."));
        } else {
            // CONSTRUÇÃO DO CONTEÚDO (UI)
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setStyle(ESTILO_TEXT_AREA);
            textArea.setPrefSize(450, 300);

            // REGRA: Geração de texto com base nas apostas registradas
            StringBuilder sb = new StringBuilder();
            for (Aposta aposta : apostasCadastradas) {
                sb.append("========================================\n");
                sb.append(" Aposta ID: ").append(aposta.getIdAposta()).append("\n");
                sb.append("----------------------------------------\n");
                sb.append(" Jogador:     ").append(aposta.getJogador().getNome()).append("\n");
                sb.append(" Valor:       R$ ").append(String.format("%.2f", aposta.getValorAposta())).append("\n");
                sb.append(" Jogo:        ").append(aposta.getJogo().getDescricao()).append("\n");
                sb.append(" Palpite:     ").append(aposta.getGalo()).append("\n");
                sb.append("========================================\n\n");
            }
            textArea.setText(sb.toString());

            dialog.getDialogPane().setContent(textArea);
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    public void criarAposta() {
        Dialog<Aposta> dialog = new Dialog<>();
        dialog.setTitle("Registrar Nova Aposta");
        dialog.setHeaderText("Selecione o jogador, o jogo e o valor da aposta.");

        // ESTILIZAÇÃO DA UI
        estilizarDialogo(dialog.getDialogPane());

        // Botões
        ButtonType apostarButtonType = new ButtonType("Apostar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(apostarButtonType, ButtonType.CANCEL);

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        // UI: Campos do formulário
        ComboBox<Jogador> jogadorCb = new ComboBox<>(FXCollections.observableArrayList(jogadorService.getJogadoresCadastrados()));
        ComboBox<Jogo> jogoCb = new ComboBox<>(FXCollections.observableArrayList(jogoService.getJogos()));
        TextField valorInput = new TextField();
        valorInput.setPromptText("Ex: 100.00");
        ComboBox<String> galoCb = new ComboBox<>();
        galoCb.setDisable(true);

        // REGRA: Quando um jogo é selecionado, mostra os galos disponíveis
        jogoCb.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            galoCb.getItems().clear();
            if (newValue != null) {
                galoCb.getItems().addAll(newValue.getGalo1(), newValue.getGalo2());
                galoCb.setDisable(false);
            } else {
                galoCb.setDisable(true);
            }
        });

        // Adiciona elementos no Grid (UI)
        grid.add(new Label("Jogador:"), 0, 0);
        grid.add(jogadorCb, 1, 0);
        grid.add(new Label("Jogo:"), 0, 1);
        grid.add(jogoCb, 1, 1);
        grid.add(new Label("Galo:"), 0, 2);
        grid.add(galoCb, 1, 2);
        grid.add(new Label("Valor (R$):"), 0, 3);
        grid.add(valorInput, 1, 3);

        // ESTILOS
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> node.setStyle(ESTILO_LABEL_FORM));
        valorInput.setStyle(ESTILO_TEXT_FIELD);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(jogadorCb::requestFocus);

        // REGRA: Validação dos dados de entrada
        Node apostarButton = dialog.getDialogPane().lookupButton(apostarButtonType);
        apostarButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                if (jogadorCb.getValue() == null || jogoCb.getValue() == null || galoCb.getValue() == null || valorInput.getText().isEmpty()) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Todos os campos devem ser preenchidos.");
                    event.consume(); return;
                }
                double valor = Double.parseDouble(valorInput.getText());
                if (valor <= 0) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", "O valor da aposta deve ser positivo.");
                    event.consume(); return;
                }
                // --> VALIDAÇÃO DE SALDO
                if (jogadorCb.getValue().getCredito().getSaldo() < valor) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Saldo Insuficiente", "O jogador não tem saldo para esta aposta.");
                    event.consume();
                }
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O valor da aposta deve ser um número.");
                event.consume();
            }
        });

        // REGRA: Criação da aposta após validação
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == apostarButtonType) {
                // CONSTROE UMA APOSTA
                return new Aposta(
                        Double.parseDouble(valorInput.getText()),
                        jogadorCb.getValue(),
                        jogoCb.getValue(),
                        galoCb.getValue()
                );
            }
            return null;
        });

        // REGRA: Salva aposta, se confirmada
        Optional<Aposta> resultado = dialog.showAndWait();
        resultado.ifPresent(aposta -> {
            apostasCadastradas.add(aposta);
            System.out.println("LOG DE SERVIÇO: Aposta " + aposta.getIdAposta() + " foi cadastrada.");
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Aposta registrada com sucesso!");
        });
    }
}
