package org.example.POOBet;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.POOBet.Models.Jogo;
import org.example.POOBet.Services.ApostaService;
import org.example.POOBet.Services.JogadorService;
import org.example.POOBet.Services.JogoService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Sistema extends Application {

    // --> VARIÁVEIS DE DEPENDÊNCIAA
    private JogadorService jogadorService;
    private JogoService jogoService;
    private ApostaService apostaService;










    // --> VARIÁVEIS DE ESTILO
    private Stage primaryStage;
    private BorderPane layoutRaiz;

    private static final String COR_FUNDO = "#2E2E2E";
    private static final String COR_TEXTO = "#E0E0E0";
    private static final String ESTILO_TITULO = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + COR_TEXTO + ";";
    private static final String ESTILO_LABEL_FORM = "-fx-font-size: 14px; -fx-text-fill: " + COR_TEXTO + ";";
    private static final String ESTILO_BOTAO_PADRAO = "-fx-background-color: #555555; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_PADRAO_HOVER = "-fx-background-color: #007ACC; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_PERIGO = "-fx-background-color: #D32F2F; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_PERIGO_HOVER = "-fx-background-color: #F44336; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_ACAO = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_BOTAO_ACAO_HOVER = "-fx-background-color: #66BB6A; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_TEXT_FIELD = "-fx-background-color: #3E3E3E; -fx-text-fill: white; -fx-prompt-text-fill: #9E9E9E;";















    // --> FUNÇÃO STARTER
    public void start(Stage primaryStage) {
        this.jogadorService = new JogadorService();
        this.jogoService = new JogoService();
        this.apostaService = new ApostaService(this.jogoService, this.jogadorService);

        this.jogoService.setApostaService(this.apostaService);
        this.jogoService.setJogadorService(this.jogadorService);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Casa de Apostas Clandestinas Rosalen");
        layoutRaiz = new BorderPane();
        layoutRaiz.setStyle("-fx-background-color: " + COR_FUNDO + "; -fx-font-family: 'Segoe UI', Arial, sans-serif;");
        layoutRaiz.setCenter(criarMenuPrincipal());
        Scene cenaPrincipal = new Scene(layoutRaiz, 550, 550);
        primaryStage.setScene(cenaPrincipal);
        primaryStage.show();
    }




















    private VBox criarMenuPrincipal() {
        Label titulo = new Label("### Rinha de Galo ###");
        titulo.setStyle(ESTILO_TITULO);

        Button btnCadastrarJogador = new Button("Cadastrar Jogador");
        estilizarBotaoPadrao(btnCadastrarJogador);
        btnCadastrarJogador.setOnAction(e -> layoutRaiz.setCenter(criarTelaCadastroJogador()));

        Button btnCadastrarJogo = new Button("Cadastrar Jogo");
        estilizarBotaoPadrao(btnCadastrarJogo);
        btnCadastrarJogo.setOnAction(e -> jogoService.cadastrarJogo());

        Button btnApostar = new Button("Apostar");
        estilizarBotaoPadrao(btnApostar);
        btnApostar.setOnAction(e -> apostaService.criarAposta());

        Button btnListarApostas = new Button("Visualizar Apostas");
        estilizarBotaoPadrao(btnListarApostas);
        btnListarApostas.setOnAction(e -> apostaService.listarApostas());

        Button btnIniciarJogo = new Button("Iniciar Jogo");
        estilizarBotaoAcao(btnIniciarJogo);
        btnIniciarJogo.setOnAction(e -> layoutRaiz.setCenter(criarTelaIniciarJogo()));

        Button btnSair = new Button("Sair");
        estilizarBotaoPerigo(btnSair);
        btnSair.setOnAction(e -> this.primaryStage.close());

        VBox menuLayout = new VBox(15, titulo, btnCadastrarJogador, btnCadastrarJogo, btnApostar, btnIniciarJogo, btnListarApostas, btnSair);
        menuLayout.setAlignment(Pos.CENTER);
        return menuLayout;
    }

    // --> TELA DE CADASTRO PADRÃO
    private GridPane criarTelaCadastroJogador() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(25));

        Label tituloForm = new Label("Cadastro de Jogador");
        tituloForm.setStyle(ESTILO_TITULO);
        grid.add(tituloForm, 0, 0, 2, 1);

        TextField nomeInput = new TextField();
        nomeInput.setPromptText("Nome completo");
        TextField cpfInput = new TextField();
        cpfInput.setPromptText("Apenas números");
        TextField nacionalidadeInput = new TextField();
        nacionalidadeInput.setPromptText("Ex: Brasileira");
        DatePicker dataNascimentoPicker = new DatePicker();
        TextField saldoInput = new TextField();
        saldoInput.setPromptText("Ex: 100.50");
        TextField moedaInput = new TextField("BRL");

        grid.add(new Label("Nome:"), 0, 1);
        grid.add(nomeInput, 1, 1);
        grid.add(new Label("CPF:"), 0, 2);
        grid.add(cpfInput, 1, 2);
        grid.add(new Label("Nacionalidade:"), 0, 3);
        grid.add(nacionalidadeInput, 1, 3);
        grid.add(new Label("Data de Nascimento:"), 0, 4);
        grid.add(dataNascimentoPicker, 1, 4);
        grid.add(new Label("Saldo Inicial:"), 0, 5);
        grid.add(saldoInput, 1, 5);
        grid.add(new Label("Moeda:"), 0, 6);
        grid.add(moedaInput, 1, 6);

        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> node.setStyle(ESTILO_LABEL_FORM));
        grid.getChildren().filtered(node -> node instanceof TextField).forEach(node -> node.setStyle(ESTILO_TEXT_FIELD));
        tituloForm.setStyle(ESTILO_TITULO);

        Button btnSalvar = new Button("Salvar Cadastro");
        estilizarBotaoAcao(btnSalvar);
        btnSalvar.setOnAction(e -> {
            try {
                String nome = nomeInput.getText();
                String cpf = cpfInput.getText();
                String nacionalidade = nacionalidadeInput.getText();
                LocalDate dataNascimento = dataNascimentoPicker.getValue();
                String saldoTexto = saldoInput.getText();
                String moeda = moedaInput.getText();

                if (nome.isEmpty() || cpf.isEmpty() || nacionalidade.isEmpty() || moeda.isEmpty() || saldoTexto.isEmpty() || dataNascimento == null) {
                    mostrarAlerta("Erro de Validação", "Todos os campos devem ser preenchidos.", Alert.AlertType.WARNING);
                    return;
                }
                double saldo = Double.parseDouble(saldoTexto);

                String mensagemConfirmacao = String.format(
                        "Confirmar cadastro do jogador?\n\nNome: %s\nCPF: %s\nSaldo: %.2f %s",
                        nome, cpf, saldo, moeda
                );

                Optional<ButtonType> resultado = mostrarAlerta("Confirmação", mensagemConfirmacao, Alert.AlertType.CONFIRMATION);

                if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                    jogadorService.cadastrarJogador(nome, cpf, nacionalidade, dataNascimento, saldo, moeda);
                    mostrarAlerta("Sucesso", "Jogador '" + nome + "' cadastrado com sucesso!", Alert.AlertType.INFORMATION);

                    nomeInput.clear();
                    cpfInput.clear();
                    nacionalidadeInput.clear();
                    dataNascimentoPicker.setValue(null);
                    saldoInput.clear();
                    moedaInput.setText("BRL");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Erro de Formato", "O saldo deve ser um número válido (ex: 100.50).", Alert.AlertType.ERROR);
            }
        });

        Button btnVoltar = new Button("Voltar ao Menu");
        estilizarBotaoPadrao(btnVoltar);
        btnVoltar.setOnAction(e -> layoutRaiz.setCenter(criarMenuPrincipal()));

        VBox botoesLayout = new VBox(10, btnSalvar, btnVoltar);
        botoesLayout.setAlignment(Pos.CENTER);
        grid.add(botoesLayout, 0, 7, 2, 1);
        return grid;
    }

    private GridPane criarTelaIniciarJogo() {

        ComboBox<Jogo> jogosComboBox = new ComboBox<>();
        List<Jogo> jogosDisponiveis = jogoService.getJogos();

        Label tituloForm = new Label("Iniciar Jogo");
        GridPane grid = new GridPane();
        Label jogoLabel = new Label("Selecione o Jogo:");


        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20));
        tituloForm.setStyle(ESTILO_TITULO);
        grid.add(tituloForm, 0, 0, 2, 1);
        jogoLabel.setStyle(ESTILO_LABEL_FORM);

        if (jogosDisponiveis != null) {
            jogosComboBox.setItems(FXCollections.observableArrayList(jogosDisponiveis));
        }

        jogosComboBox.setPromptText("Escolha um jogo para iniciar");
        jogosComboBox.setPrefWidth(300);

        grid.add(jogoLabel, 0, 1);
        grid.add(jogosComboBox, 1, 1);

        Button btnIniciar = new Button("Iniciar Jogo Selecionado");
        estilizarBotaoAcao(btnIniciar);
        btnIniciar.setOnAction(e -> {
            Jogo jogoSelecionado = jogosComboBox.getSelectionModel().getSelectedItem();
            if (jogoSelecionado == null) {
                mostrarAlerta("Erro", "Por favor, selecione um jogo.", Alert.AlertType.WARNING);
            } else {
                jogoService.iniciarJogo(jogoSelecionado.getIdJogo());
                layoutRaiz.setCenter(criarMenuPrincipal());
            }
        });

        Button btnVoltar = new Button("Voltar ao Menu");
        estilizarBotaoPadrao(btnVoltar);
        btnVoltar.setOnAction(e -> layoutRaiz.setCenter(criarMenuPrincipal()));

        VBox botoesLayout = new VBox(10, btnIniciar, btnVoltar);
        botoesLayout.setAlignment(Pos.CENTER);
        grid.add(botoesLayout, 0, 2, 2, 1);
        return grid;
    }

    private Optional<ButtonType> mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        estilizarDialogo(alert.getDialogPane());
        return alert.showAndWait();
    }













    // --> ESTILOS PADRÕES / HOVERS

    private void estilizarBotaoPadrao(Button button, boolean isDialogButton) {
        button.setStyle(ESTILO_BOTAO_PADRAO);
        button.setPrefSize(isDialogButton ? 100 : 200, isDialogButton ? 30 : 40);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_PADRAO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_PADRAO));
    }
    private void estilizarBotaoPadrao(Button button) {
        estilizarBotaoPadrao(button, false);
    }

    private void estilizarBotaoAcao(Button button) {
        button.setStyle(ESTILO_BOTAO_ACAO);
        button.setPrefSize(200, 40);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_ACAO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_ACAO));
    }

    private void estilizarBotaoPerigo(Button button) {
        button.setStyle(ESTILO_BOTAO_PERIGO);
        button.setPrefSize(200, 40);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_PERIGO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_PERIGO));
    }

    private void estilizarDialogo(DialogPane dialogPane) {
        dialogPane.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        Node contentLabel = dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle("-fx-text-fill: " + COR_TEXTO + "; -fx-font-size: 14px;");
        }
        Node headerPanel = dialogPane.lookup(".header-panel");
        if (headerPanel != null) {
            headerPanel.setStyle("-fx-background-color: " + COR_FUNDO + ";");
        }
        dialogPane.getButtonTypes().stream()
                .map(dialogPane::lookupButton)
                .forEach(node -> {
                    if (node instanceof Button) {
                        estilizarBotaoPadrao((Button) node, true);
                    }
                });
    }

    public static void main(String[] args) {
        launch(args);
    }
}