package org.example.POOBet.Services;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.POOBet.Models.Aposta;
import org.example.POOBet.Models.Jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class JogoService {

    // =========================================================================
    // ======================== [ ESTILO / APARÊNCIA UI ] ======================
    // =========================================================================

    private static final String COR_FUNDO = "#2E2E2E";
    private static final String COR_TEXTO = "#E0E0E0";
    private static final String ESTILO_LABEL_FORM = "-fx-font-size: 14px; -fx-text-fill: " + COR_TEXTO + ";";
    private static final String ESTILO_BOTAO_PADRAO = "-fx-background-color: #555555; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_PADRAO_HOVER = "-fx-background-color: #007ACC; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_TEXT_FIELD = "-fx-background-color: #3E3E3E; -fx-text-fill: white; -fx-prompt-text-fill: #9E9E9E;";

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

    private final List<Jogo> jogos = new ArrayList<>();
    private JogadorService jogadorService;
    private ApostaService apostaService;

    public JogoService() {}

    public void setApostaService(ApostaService apostaService) {
        this.apostaService = apostaService;
    }
    public void setJogadorService(JogadorService jogadorService) {
        this.jogadorService = jogadorService;
    }

    public void cadastrarJogo() {
        // Estilo
        Dialog<Jogo> dialog = new Dialog<>();
        dialog.setTitle("Cadastro de Novo Jogo");
        dialog.setHeaderText("Preencha as informações da rinha de galo.");
        estilizarDialogo(dialog.getDialogPane());

        ButtonType salvarButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField galo1Input = new TextField();
        galo1Input.setPromptText("Nome do galo desafiado");
        TextField galo2Input = new TextField();
        galo2Input.setPromptText("Nome do galo desafiante");
        TextField apostaMaxInput = new TextField();
        apostaMaxInput.setPromptText("Ex: 500.00");
        TextField apostaMinInput = new TextField();
        apostaMinInput.setPromptText("Ex: 50.00");

        galo1Input.setStyle(ESTILO_TEXT_FIELD);
        galo2Input.setStyle(ESTILO_TEXT_FIELD);
        apostaMaxInput.setStyle(ESTILO_TEXT_FIELD);
        apostaMinInput.setStyle(ESTILO_TEXT_FIELD);

        grid.add(new Label("Galo Desafiado:"), 0, 0);
        grid.add(galo1Input, 1, 0);
        grid.add(new Label("Galo Desafiante:"), 0, 1);
        grid.add(galo2Input, 1, 1);
        grid.add(new Label("Aposta Máxima:"), 0, 2);
        grid.add(apostaMaxInput, 1, 2);
        grid.add(new Label("Aposta Mínima:"), 0, 3);
        grid.add(apostaMinInput, 1, 3);
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> node.setStyle(ESTILO_LABEL_FORM));

        dialog.getDialogPane().setContent(grid);

        Node salvarButton = dialog.getDialogPane().lookupButton(salvarButtonType);
        salvarButton.setDisable(true);

        Runnable updateButtonState = () -> {
            boolean fieldsAreEmpty = galo1Input.getText().trim().isEmpty() || galo2Input.getText().trim().isEmpty() || apostaMaxInput.getText().trim().isEmpty() || apostaMinInput.getText().trim().isEmpty();
            salvarButton.setDisable(fieldsAreEmpty);
        };
        galo1Input.textProperty().addListener((obs, old, val) -> updateButtonState.run());
        galo2Input.textProperty().addListener((obs, old, val) -> updateButtonState.run());
        apostaMaxInput.textProperty().addListener((obs, old, val) -> updateButtonState.run());
        apostaMinInput.textProperty().addListener((obs, old, val) -> updateButtonState.run());

        salvarButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                double apostaMax = Double.parseDouble(apostaMaxInput.getText());
                double apostaMin = Double.parseDouble(apostaMinInput.getText());
                if (apostaMax <= apostaMin) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", "A aposta máxima deve ser maior que a mínima.");
                    event.consume();
                }
            } catch (NumberFormatException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "Os valores de aposta devem ser números válidos.");
                event.consume();
            }
        });

        Platform.runLater(galo1Input::requestFocus);











        // ---------> CRIAÇÃO DE UM NOVO JOGO <--------
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == salvarButtonType) {
                return new Jogo(galo1Input.getText(), galo2Input.getText(), Double.parseDouble(apostaMaxInput.getText()), Double.parseDouble(apostaMinInput.getText()));
            }
            return null;
        });

        Optional<Jogo> resultado = dialog.showAndWait();
        resultado.ifPresent(jogo -> {
            this.jogos.add(jogo);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Novo jogo cadastrado!");
        });
    }

    public void iniciarJogo(int id) {
        Jogo jogo = buscarJogo(id);
        if (jogo == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Jogo não encontrado.");
            return;
        }

        String galo1 = jogo.getGalo1();
        String galo2 = jogo.getGalo2();
        Random rand = new Random();
        String vencedor = (rand.nextInt(2) == 0) ? galo1 : galo2;

        List<Aposta> apostasDoJogo = apostaService.getApostasCadastradas().stream()
                .filter(aposta -> aposta.getJogo().getIdJogo() == id)
                .collect(Collectors.toList());

        StringBuilder resumoVencedores = new StringBuilder();
        for (Aposta aposta : apostasDoJogo) {
            if (aposta.getGalo().equals(vencedor)) {
                double premio = aposta.getValorAposta() * 2;
                var jogador = aposta.getJogador();
                var credito = jogador.getCredito();
                credito.setSaldo(credito.getSaldo() + premio);
                resumoVencedores.append(jogador.getNome()).append(" ganhou R$ ").append(String.format("%.2f", premio)).append("\n");
            }
        }

        String mensagemFinal = "O grande vencedor da rinha foi:\n\n" + vencedor.toUpperCase();
        if (resumoVencedores.length() > 0) {
            mensagemFinal += "\n\n--- Vencedores das Apostas ---\n" + resumoVencedores.toString();
        } else {
            mensagemFinal += "\n\nNinguém acertou o vencedor.";
        }
        mostrarAlerta(Alert.AlertType.INFORMATION, "Jogo Finalizado!", mensagemFinal);
    }

    public Jogo buscarJogo(int id) {
        for (Jogo jogo : jogos) {
            if (jogo.getIdJogo() == id) {
                return jogo;
            }
        }
        return null;
    }

    public List<Jogo> getJogos() {
        return jogos;
    }

    private void estilizarDialogo(DialogPane dialogPane) {
        dialogPane.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        Node contentLabel = dialogPane.lookup(".content.label");
        if(contentLabel != null) contentLabel.setStyle("-fx-text-fill: " + COR_TEXTO + "; -fx-font-size: 14px;");
        Node headerPanel = dialogPane.lookup(".header-panel");
        if(headerPanel != null) headerPanel.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        dialogPane.getButtonTypes().stream()
                .map(dialogPane::lookupButton)
                .forEach(node -> {
                    if (node instanceof Button) {
                        estilizarBotaoPadrao((Button) node);
                    }
                });
    }

    private void estilizarBotaoPadrao(Button button) {
        button.setStyle(ESTILO_BOTAO_PADRAO);
        button.setPrefSize(100, 30);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_PADRAO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_PADRAO));
    }
}