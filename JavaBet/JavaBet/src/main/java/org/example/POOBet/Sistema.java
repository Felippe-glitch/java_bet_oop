package org.example.POOBet;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.POOBet.Models.Jogo;
import org.example.POOBet.Services.ApostaService;
import org.example.POOBet.Services.JogadorService;
import org.example.POOBet.Services.JogoService;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Sistema extends Application {

    // =================================================================================
    // DECLARAÇÕES E INICIALIZAÇÃO (DEPENDÊNCIAS)
    // =================================================================================

    // --- DEPENDÊNCIAS (CAMADA DE NEGÓCIO) ---
    private JogadorService jogadorService;
    private JogoService jogoService;
    private ApostaService apostaService;

    // =================================================================================
    // DEFINIÇÕES DE ESTILO
    // =================================================================================

    private Stage primaryStage;
    private BorderPane layoutRaiz;

    private static final String FONT_FAMILY = "'Georgia', 'Palatino', serif";
    private static final String COR_TEXTO = "#E0D8D0";
    private static final String COR_FUNDO_PAINEL_SEMITRANSPARENTE = "-fx-background-color: rgba(38, 26, 17, 0.85); -fx-background-radius: 15; -fx-border-color: #A1887F; -fx-border-width: 2; -fx-border-radius: 15;";
    private static final String COR_BORDA_BOTAO = "#4E342E";
    private static final String EFEITO_HOVER = "-fx-effect: dropshadow(gaussian, rgba(255, 204, 110, 0.4), 15, 0, 0, 0);";

    private static final String ESTILO_BOTAO_PADRAO = String.format("-fx-background-color: #6D4C41; -fx-text-fill: %s; -fx-font-family: %s; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 8;", COR_TEXTO, FONT_FAMILY, COR_BORDA_BOTAO);
    private static final String ESTILO_BOTAO_PADRAO_HOVER = String.format("-fx-background-color: #8D6E63; -fx-text-fill: %s; -fx-font-family: %s; -fx-font-weight: bold; -fx-font-size: 15px; -fx-background-radius: 8; -fx-border-color: %s; -fx-border-width: 2; -fx-border-radius: 8; %s", COR_TEXTO, FONT_FAMILY, COR_BORDA_BOTAO, EFEITO_HOVER);
    private static final String ESTILO_BOTAO_ACAO = ESTILO_BOTAO_PADRAO.replace("#6D4C41", "#388E3C");
    private static final String ESTILO_BOTAO_ACAO_HOVER = ESTILO_BOTAO_PADRAO_HOVER.replace("#8D6E63", "#4CAF50");
    private static final String ESTILO_BOTAO_PERIGO = ESTILO_BOTAO_PADRAO.replace("#6D4C41", "#C62828");
    private static final String ESTILO_BOTAO_PERIGO_HOVER = ESTILO_BOTAO_PADRAO_HOVER.replace("#8D6E63", "#E53935");
    private static final String ESTILO_TITULO = "-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + COR_TEXTO + "; -fx-font-family: " + FONT_FAMILY + ";";
    private static final String ESTILO_LABEL_FORM = "-fx-font-size: 16px; -fx-text-fill: " + COR_TEXTO + "; -fx-font-family: " + FONT_FAMILY + ";";
    private static final String ESTILO_TEXT_FIELD = "-fx-background-color: #4E342E; -fx-text-fill: " + COR_TEXTO + "; -fx-prompt-text-fill: #A1887F; -fx-background-radius: 5; -fx-border-color: #3E2723; -fx-border-radius: 5; -fx-font-size: 14px;";
    private static final String ESTILO_COMBO_BOX = "-fx-background-color: #4E342E; -fx-border-color: #3E2723; -fx-border-radius: 5; -fx-font-size: 14px;";

    // =================================================================================
    // PONTO DE ENTRADA E CONFIGURAÇÃO INICIAL (START)
    // =================================================================================

    @Override
    public void start(Stage primaryStage) {
        // --- INICIALIZAÇÃO DE SERVICES ---
        this.jogadorService = new JogadorService();
        this.jogoService = new JogoService();
        this.apostaService = new ApostaService(this.jogoService, this.jogadorService);
        this.jogoService.setApostaService(this.apostaService);
        this.jogoService.setJogadorService(this.jogadorService);




        // --- CONFIGURAÇÃO DA JANELA PRINCIPAL ---
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Casa de Apostas Clandestinas Rosalen");
        layoutRaiz = new BorderPane();
        configurarFundo();

        // --- EXIBIÇÃO DA TELA INICIAL ---
        layoutRaiz.setCenter(criarMenuPrincipal());
        Scene cenaPrincipal = new Scene(layoutRaiz, 800, 600);
        primaryStage.setScene(cenaPrincipal);
        primaryStage.show();
    }

    // =================================================================================
    // CONSTRUÇÃO DAS TELAS (UI LAYOUT)
    // =================================================================================

    private VBox criarMenuPrincipal() {
        // --- CRIAÇÃO DOS COMPONENTES ---
        Button btnCadastrarJogador = new Button("Cadastrar Jogador");
        Button btnCadastrarJogo = new Button("Cadastrar Jogo");
        Button btnApostar = new Button("Apostar");
        Button btnListarApostas = new Button("Visualizar Apostas");
        Button btnIniciarJogo = new Button("Iniciar Jogo");
        Button btnSair = new Button("Sair");

        // --- APLICAÇÃO DOS ESTILOS ---
        estilizarBotaoPadrao(btnCadastrarJogador);
        estilizarBotaoPadrao(btnCadastrarJogo);
        estilizarBotaoPadrao(btnApostar);
        estilizarBotaoPadrao(btnListarApostas);
        estilizarBotaoAcao(btnIniciarJogo);
        estilizarBotaoPerigo(btnSair);

        // --- DEFINIÇÃO DOS EVENTOS (AÇÕES) ---
        btnCadastrarJogador.setOnAction(e -> layoutRaiz.setCenter(criarTelaCadastroJogador()));
        btnIniciarJogo.setOnAction(e -> layoutRaiz.setCenter(criarTelaIniciarJogo()));
        btnSair.setOnAction(e -> this.primaryStage.close());

        // >>> CHAMADAS DE SERVIÇO (REGRAS DE NEGÓCIO) <<<
        btnCadastrarJogo.setOnAction(e -> jogoService.cadastrarJogo());
        btnApostar.setOnAction(e -> apostaService.criarAposta());
        btnListarApostas.setOnAction(e -> apostaService.listarApostas());

        // --- MONTAGEM E ESTILIZAÇÃO DO LAYOUT ---
        VBox menuLayout = new VBox(8, btnCadastrarJogador, btnCadastrarJogo, btnApostar, btnIniciarJogo, btnListarApostas, btnSair);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setMaxWidth(350);
        menuLayout.setMaxHeight(450);
        BorderPane.setMargin(menuLayout, new Insets(230, 0, 0, 0));
        menuLayout.setStyle(COR_FUNDO_PAINEL_SEMITRANSPARENTE);
        menuLayout.setPadding(new Insets(20));

        return menuLayout;
    }

    private GridPane criarTelaCadastroJogador() {
        // --- CRIAÇÃO DOS COMPONENTES ---
        GridPane grid = new GridPane();
        Label tituloForm = new Label("Cadastro de Jogador");
        TextField nomeInput = new TextField();
        TextField cpfInput = new TextField();
        TextField nacionalidadeInput = new TextField();
        DatePicker dataNascimentoPicker = new DatePicker();
        TextField saldoInput = new TextField();
        TextField moedaInput = new TextField("BRL");
        Button btnSalvar = new Button("Salvar Cadastro");
        Button btnVoltar = new Button("Voltar ao Menu");

        // --- CONFIGURAÇÃO DOS COMPONENTES ---
        nomeInput.setPromptText("Nome completo");
        cpfInput.setPromptText("Apenas números");
        nacionalidadeInput.setPromptText("Ex: Brasileira");
        saldoInput.setPromptText("Ex: 100.50");

        // --- MONTAGEM DO LAYOUT ---
        grid.add(tituloForm, 0, 0, 2, 1);
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
        VBox botoesLayout = new VBox(10, btnSalvar, btnVoltar);
        botoesLayout.setAlignment(Pos.CENTER);
        grid.add(botoesLayout, 0, 7, 2, 1);

        // --- APLICAÇÃO DE ESTILOS ---
        estilizarGridPadrao(grid);
        tituloForm.setStyle(ESTILO_TITULO);
        grid.getChildren().filtered(node -> node instanceof Label).forEach(node -> node.setStyle(ESTILO_LABEL_FORM));
        grid.getChildren().filtered(node -> node instanceof TextField).forEach(node -> node.setStyle(ESTILO_TEXT_FIELD));
        tituloForm.setStyle(ESTILO_TITULO); // Garante que o título não seja sobrescrito
        estilizarBotaoAcao(btnSalvar);
        estilizarBotaoPadrao(btnVoltar);

        // --- DEFINIÇÃO DOS EVENTOS (AÇÕES) ---
        btnVoltar.setOnAction(e -> layoutRaiz.setCenter(criarMenuPrincipal()));
        // ------------> EVENTO DE SAVE <------------
        btnSalvar.setOnAction(e -> lidarComSalvarJogador(nomeInput, cpfInput, nacionalidadeInput, dataNascimentoPicker, saldoInput, moedaInput));

        return grid;
    }

    private GridPane criarTelaIniciarJogo() {
        // --- CRIAÇÃO DOS COMPONENTES ---
        GridPane grid = new GridPane();
        Label tituloForm = new Label("Iniciar Jogo");
        Label jogoLabel = new Label("Selecione o Jogo:");





        // >>> CHAMADA DE SERVIÇO <<<
        List<Jogo> jogosDisponiveis = jogoService.getJogos();
        ComboBox<Jogo> jogosComboBox = new ComboBox<>();
        if (jogosDisponiveis != null) {
            jogosComboBox.setItems(FXCollections.observableArrayList(jogosDisponiveis));
        }

        Button btnIniciar = new Button("Iniciar Jogo Selecionado");
        Button btnVoltar = new Button("Voltar ao Menu");

        // --- MONTAGEM DO LAYOUT ---
        grid.add(tituloForm, 0, 0, 2, 1);
        grid.add(jogoLabel, 0, 1);
        grid.add(jogosComboBox, 1, 1);
        VBox botoesLayout = new VBox(10, btnIniciar, btnVoltar);
        botoesLayout.setAlignment(Pos.CENTER);
        grid.add(botoesLayout, 0, 2, 2, 1);

        // --- APLICAÇÃO DE ESTILOS ---
        estilizarGridPadrao(grid);
        tituloForm.setStyle(ESTILO_TITULO);
        jogoLabel.setStyle(ESTILO_LABEL_FORM);
        jogosComboBox.setPromptText("Escolha um jogo para iniciar");
        jogosComboBox.setPrefWidth(300);
        jogosComboBox.setStyle(ESTILO_COMBO_BOX);
        estilizarBotaoAcao(btnIniciar);
        estilizarBotaoPadrao(btnVoltar);

        // --- DEFINIÇÃO DOS EVENTOS (AÇÕES) ---
        btnVoltar.setOnAction(e -> layoutRaiz.setCenter(criarMenuPrincipal()));
        // Delega a lógica de iniciar o jogo para um método na Seção 5
        btnIniciar.setOnAction(e -> lidarComIniciarJogo(jogosComboBox));

        return grid;
    }












    // =================================================================================
    // LÓGICA DE EVENTOS (REGRAS DE NEGÓCIO)
    // =================================================================================

    /**
     * Lida com a lógica de salvar um novo jogador após o clique no botão.
     */
    private void lidarComSalvarJogador(TextField nomeInput, TextField cpfInput, TextField nacionalidadeInput, DatePicker dataNascimentoPicker, TextField saldoInput, TextField moedaInput) {
        try {
            // --- VALIDAÇÃO DE ENTRADA ---
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

            // --- CONFIRMAÇÃO DO USUÁRIO ---
            String mensagemConfirmacao = String.format("Confirmar cadastro do jogador?\n\nNome: %s\nCPF: %s\nSaldo: %.2f %s", nome, cpf, saldo, moeda);
            Optional<ButtonType> resultado = mostrarAlerta("Confirmação", mensagemConfirmacao, Alert.AlertType.CONFIRMATION);

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // >>> CHAMADA DE SERVIÇO (REGRA DE NEGÓCIO) <<<
                jogadorService.cadastrarJogador(nome, cpf, nacionalidade, dataNascimento, saldo, moeda);

                mostrarAlerta("Sucesso", "Jogador '" + nome + "' cadastrado com sucesso!", Alert.AlertType.INFORMATION);

                // --- LIMPEZA DO FORMULÁRIO ---
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
    }

    /**
     * Lida com a lógica de iniciar um jogo selecionado.
     */
    private void lidarComIniciarJogo(ComboBox<Jogo> jogosComboBox) {
        Jogo jogoSelecionado = jogosComboBox.getSelectionModel().getSelectedItem();
        if (jogoSelecionado == null) {
            mostrarAlerta("Erro", "Por favor, selecione um jogo.", Alert.AlertType.WARNING);
        } else {
            // >>> CHAMADA DE SERVIÇO (REGRA DE NEGÓCIO) <<<
            jogoService.iniciarJogo(jogoSelecionado.getIdJogo());

            layoutRaiz.setCenter(criarMenuPrincipal()); // Volta ao menu
        }
    }










    // =================================================================================
    // MÉTODOS AUXILIARES DE ESTILO
    // =================================================================================

    private void configurarFundo() {
        try {
            File file = new File("C:\\Users\\Usuario\\Downloads\\JavaBet\\JavaBet\\src\\main\\java\\fundo.png");
            Image image = new Image(file.toURI().toString());
            BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(1.0, 1.0, true, true, false, false));
            layoutRaiz.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem de fundo. Usando cor sólida.");
            layoutRaiz.setStyle("-fx-background-color: #1a1a1a;");
        }
    }

    private void estilizarGridPadrao(GridPane grid) {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(40));
        grid.setMaxWidth(550);
        grid.setStyle(COR_FUNDO_PAINEL_SEMITRANSPARENTE);
    }

    private Optional<ButtonType> mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        estilizarDialogo(alert.getDialogPane());
        return alert.showAndWait();
    }

    private void estilizarBotaoPadrao(Button button, boolean isDialogButton) {
        button.setStyle(ESTILO_BOTAO_PADRAO);
        button.setPrefSize(isDialogButton ? 120 : 250, isDialogButton ? 35 : 40);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_PADRAO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_PADRAO));
    }

    private void estilizarBotaoPadrao(Button button) {
        estilizarBotaoPadrao(button, false);
    }

    private void estilizarBotaoAcao(Button button) {
        button.setStyle(ESTILO_BOTAO_ACAO);
        button.setPrefSize(250, 45);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_ACAO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_ACAO));
    }

    private void estilizarBotaoPerigo(Button button) {
        button.setStyle(ESTILO_BOTAO_PERIGO);
        button.setPrefSize(250, 45);
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setStyle(ESTILO_BOTAO_PERIGO_HOVER));
        button.setOnMouseExited(e -> button.setStyle(ESTILO_BOTAO_PERIGO));
    }

    private void estilizarDialogo(DialogPane dialogPane) {
        dialogPane.setStyle("-fx-background-color: #3E2723; -fx-border-color: #A1887F; -fx-border-width: 2;");
        Node contentLabel = dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle("-fx-font-size: 14px; -fx-font-family: " + FONT_FAMILY + "; -fx-text-fill: " + COR_TEXTO + ";");
        }
        Node headerPanel = dialogPane.lookup(".header-panel");
        if (headerPanel != null) {
            headerPanel.setStyle("-fx-background-color: #4E342E;");
        }
        dialogPane.getButtonTypes().stream()
                .map(dialogPane::lookupButton)
                .filter(Objects::nonNull)
                .forEach(node -> {
                    if (node instanceof Button) {
                        estilizarBotaoPadrao((Button) node, true);
                    }
                });
    }

    // =================================================================================
    // MÉTODO PRINCIPAL
    // =================================================================================

    public static void main(String[] args) {
        launch(args);
    }
}