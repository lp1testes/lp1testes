package Controller;

import DAL.ConfiguracaoDAL;
import DAL.PratoDAL;
import Utils.Configuracao;
import Model.Prato;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PratoController {

    private static PratoController instance;
    private Prato[] pratos;
    private PratoDAL pratoDAL;
    private ConfiguracaoDAL configuracaoDAL;
    private Configuracao configuracao = Configuracao.getInstancia();
    private static final SimulacaoDiaController simulacaoDiaController = SimulacaoDiaController.getInstance();
    private static final LogsController logsController = LogsController.getInstance();
    private static final MenuController menuController = MenuController.getInstance();
    private static final MesaController mesaController = MesaController.getInstance();

    public PratoController() {
        pratoDAL                = new PratoDAL();
        pratos                  = pratoDAL.carregarPratos();
        configuracaoDAL         = new ConfiguracaoDAL();
        configuracaoDAL.carregarConfiguracoes(configuracao);
        //simulacaoDiaController  = new SimulacaoDiaController();
        //logsController          = new LogsController();
        //menuController          = new MenuController();
        //mesaController          = new MesaController();
    }

    public static synchronized PratoController getInstance() {
        if (instance == null) {
            instance = new PratoController();
        }
        return instance;
    }

    public Prato[] getPratos() {
        return pratos;
    }

    public void adicionarPrato(Prato novoPrato) {
        int proximoId = pratoDAL.obterProximoId(pratos);
        novoPrato.setId(proximoId);

        for (int i = 0; i < pratos.length; i++) {
            if (pratos[i] == null) {
                pratos[i] = novoPrato;
                return;
            }
        }
        System.out.println("Não é possível adicionar mais pratos. Capacidade máxima atingida.");
    }

    public void atualizarPrato(Prato pratoAtualizado) {
        for (int i = 0; i < pratos.length; i++) {
            if (pratos[i] != null && pratos[i].getId().equals(pratoAtualizado.getId())) {
                pratos[i] = pratoAtualizado;
                return;
            }
        }
        System.out.println("Prato não encontrado.");
    }

    public void removerPrato(int id) {
        for (int i = 0; i < pratos.length; i++) {
            if (pratos[i] != null && pratos[i].getId() == id) {
                pratos[i] = null;
                return;
            }
        }
        System.out.println("Prato não encontrado.");
    }

    public String validarCategoria(Scanner scanner) {
        String categoria = "";
        boolean categoriaValida = false;
        while (!categoriaValida) {
            System.out.print("Categoria do prato (entrada, prato principal, sobremesa): ");
            categoria = scanner.nextLine().toLowerCase();
            switch (categoria) {
                case "entrada":
                case "prato principal":
                case "sobremesa":
                    categoriaValida = true;
                    break;
                default:
                    System.out.println("Categoria inválida! Por favor, escolha entre 'entrada', 'prato principal' ou 'sobremesa'.");
            }
        }
        return categoria;
    }

    public double validarPreco(Scanner scanner, String tipoPreco) {
        double preco = 0;
        boolean precoValido = false;
        while (!precoValido) {
            System.out.print(tipoPreco + ": ");
            if (scanner.hasNextDouble()) {
                preco = scanner.nextDouble();
                precoValido = true;
            } else {
                System.out.println("Entrada inválida! Por favor, insira um número válido para o " + tipoPreco.toLowerCase() + ".");
                scanner.next(); // Consumir a entrada inválida
            }
        }
        scanner.nextLine(); // Consumir a nova linha
        return preco;
    }

    public boolean validarDisponibilidade(Scanner scanner) {
        boolean disponivel = false;
        boolean disponivelValido = false;
        while (!disponivelValido) {
            System.out.print("O prato está disponível para seleção? (true/false): ");
            if (scanner.hasNextBoolean()) {
                disponivel = scanner.nextBoolean();
                disponivelValido = true;
            } else {
                System.out.println("Entrada inválida! Por favor, insira 'true' ou 'false'.");
                scanner.next(); // Consumir a entrada inválida
            }
        }
        scanner.nextLine(); // Consumir a nova linha
        return disponivel;
    }

    public int validarTempoPreparacao(Scanner scanner) {
        int tempoMaximo = configuracao.getUnidadesTempoDia();
        int tempoPreparacao = 0;
        boolean tempoPreparacaoValido = false;
        while (!tempoPreparacaoValido) {
            System.out.print("Tempo de preparação (em minutos): ");
            if (scanner.hasNextInt()) {
                tempoPreparacao = scanner.nextInt();
                if (tempoPreparacao < tempoMaximo) {
                    tempoPreparacaoValido = true;
                } else {
                    System.out.println("O tempo de preparação deve ser menor que " + tempoMaximo + " minutos.");
                }
            } else {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro válido para o tempo de preparação.");
                scanner.next(); // Consumir a entrada inválida
            }
        }
        scanner.nextLine(); // Consumir a nova linha
        return tempoPreparacao;
    }

    public Prato validarPratoParaEditar(Scanner scanner) {
        System.out.print("ID do prato a editar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir a nova linha

        Prato[] pratos = getPratos();
        Prato pratoParaEditar = null;
        for (Prato prato : pratos) {
            if (prato != null && prato.getId() == id) {
                pratoParaEditar = prato;
                break;
            }
        }

        if (pratoParaEditar == null) {
            System.out.println("Prato não encontrado.");
        }
        return pratoParaEditar;
    }

    public void editarPrato(Scanner scanner, Prato pratoParaEditar) {
        int tempoMaximo = configuracao.getUnidadesTempoDia();

        System.out.print("Novo nome do prato (deixe em branco para manter): ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            pratoParaEditar.setNome(nome);
        }

        boolean categoriaValida = false;
        while (!categoriaValida) {
            System.out.print("Nova categoria do prato (deixe em branco para manter): ");
            String categoria = scanner.nextLine().toLowerCase();
            if (categoria.isEmpty()) {
                categoriaValida = true;
            } else {
                switch (categoria) {
                    case "entrada":
                    case "prato principal":
                    case "sobremesa":
                        pratoParaEditar.setCategoria(categoria);
                        categoriaValida = true;
                        break;
                    default:
                        System.out.println("Categoria inválida! Por favor, escolha entre 'entrada', 'prato principal' ou 'sobremesa'.");
                }
            }
        }

        boolean precoCustoValido = false;
        while (!precoCustoValido) {
            System.out.print("Novo preço de custo (deixe em branco para manter): ");
            String precoCustoStr = scanner.nextLine();
            if (precoCustoStr.isEmpty()) {
                precoCustoValido = true;
            } else {
                try {
                    double precoCusto = Double.parseDouble(precoCustoStr);
                    pratoParaEditar.setPrecoCusto(precoCusto);
                    precoCustoValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Por favor, insira um número válido para o preço de custo.");
                }
            }
        }

        boolean precoVendaValido = false;
        while (!precoVendaValido) {
            System.out.print("Novo preço de venda (deixe em branco para manter): ");
            String precoVendaStr = scanner.nextLine();
            if (precoVendaStr.isEmpty()) {
                precoVendaValido = true;
            } else {
                try {
                    double precoVenda = Double.parseDouble(precoVendaStr);
                    pratoParaEditar.setPrecoVenda(precoVenda);
                    precoVendaValido = true;
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Por favor, insira um número válido para o preço de venda.");
                }
            }
        }

        boolean tempoPreparacaoValido = false;
        while (!tempoPreparacaoValido) {
            System.out.print("Novo tempo de preparação (em minutos, deixe em branco para manter): ");
            String tempoPreparacaoStr = scanner.nextLine();
            if (tempoPreparacaoStr.isEmpty()) {
                tempoPreparacaoValido = true;
            } else {
                try {
                    int tempoPreparacao = Integer.parseInt(tempoPreparacaoStr);
                    if (tempoPreparacao < tempoMaximo) {
                        pratoParaEditar.setTempoPreparacao(tempoPreparacao);
                        tempoPreparacaoValido = true;
                    } else {
                        System.out.println("O tempo de preparação deve ser menor que " + tempoMaximo + " minutos.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida! Por favor, insira um número inteiro válido para o tempo de preparação.");
                }
            }
        }

        boolean disponivelValido = false;
        while (!disponivelValido) {
            System.out.print("O prato está disponível para seleção? (true/false, deixe em branco para manter): ");
            String disponivelStr = scanner.nextLine();
            if (disponivelStr.isEmpty()) {
                disponivelValido = true;
            } else {
                try {
                    boolean disponivel = Boolean.parseBoolean(disponivelStr);
                    pratoParaEditar.setDisponivel(disponivel);
                    disponivelValido = true;
                } catch (Exception e) {
                    System.out.println("Entrada inválida! Por favor, insira 'true' ou 'false'.");
                }
            }
        }

        atualizarPrato(pratoParaEditar);
        System.out.println("Prato editado com sucesso!");
    }

    public void listarPratosPorCategoria(String categoria) {
        for (Prato prato : pratos) {
            if (prato != null && prato.getCategoria().equalsIgnoreCase(categoria)) {
                System.out.println(prato.getId() + " " + prato.getNome() + ";");
            }
        }
    }

    public Prato getPratoById(int id) {
        for (Prato prato : pratos) {
            if (prato != null && prato.getId() == id) {
                return prato;
            }
        }
        return null;
    }

    public boolean criarPratoGerirMenusView(String nome, Scanner scanner) {

        String categoria    = validarCategoria(scanner);
        double precoCusto   = validarPreco(scanner, "Preço de custo");
        double precoVenda   = validarPreco(scanner, "Preço de venda");

        int tempoPreparacao = validarTempoPreparacao(scanner);

        boolean disponivel = validarDisponibilidade(scanner);

        Prato novoPrato = new Prato(null, nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);
        adicionarPrato(novoPrato);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Prato criado: %s, Categoria: %s, Custo: %.2f, Venda: %.2f, Tempo: %d, Disponível: %b",
                nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        return true;
    }

    public boolean editarPratoGerirMenusView(Scanner scanner){

        Prato[] pratos = getPratos();

        System.out.println("\n-- Lista de Pratos --");

        for (Prato prato : pratos) {
            if (prato != null) {
                System.out.println("ID: " + prato.getId() + ", Nome: " + prato.getNome() + ", Categoria: " + prato.getCategoria() + ", Preço de Custo: " + prato.getPrecoCusto() + ", Preço de Venda: " + prato.getPrecoVenda() + ", Tempo de Preparação: " + prato.getTempoPreparacao() + ", Disponível: " + prato.isDisponivel());
            }
        }

        Prato pratoParaEditar = validarPratoParaEditar(scanner);

        if (pratoParaEditar != null) {

            editarPrato(scanner, pratoParaEditar);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Prato editado: %s, Categoria: %s, Custo: %.2f, Venda: %.2f, Tempo: %d, Disponível: %b",
                    pratoParaEditar.getNome(), pratoParaEditar.getCategoria(), pratoParaEditar.getPrecoCusto(), pratoParaEditar.getPrecoVenda(), pratoParaEditar.getTempoPreparacao(), pratoParaEditar.isDisponivel());

            logsController.criarLog(currentDay, currentHour, logType, logDescription);

            return true;
        }
        return  false;
    }

    public boolean removerPratoGerirMenusView(Scanner scanner){

        Prato[] pratos = getPratos();

        System.out.println("\n-- Lista de Pratos --");

        for (Prato prato : pratos) {

            if (prato != null) {

                System.out.println("ID: " + prato.getId() + ", Nome: " + prato.getNome() + ", Categoria: " + prato.getCategoria() + ", Preço de Custo: " + prato.getPrecoCusto() + ", Preço de Venda: " + prato.getPrecoVenda() + ", Tempo de Preparação: " + prato.getTempoPreparacao() + ", Disponível: " + prato.isDisponivel());
            }
        }

        System.out.print("ID do prato a remover: ");

        int id = scanner.nextInt();
        scanner.nextLine();

        removerPrato(id);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "ACTION";
        String logDescription = String.format("Prato removido: ID %d", id);

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        return true;
    }

    public void listarPratosGerirMenusView(){

        Prato[] pratos = getPratos();

        System.out.println("\n-- Lista de Pratos --");

        for (Prato prato : pratos) {

            if (prato != null) {

                System.out.println("ID: " + prato.getId() + ", Nome: " + prato.getNome() + ", Categoria: " + prato.getCategoria() + ", Preço de Custo: " + prato.getPrecoCusto() + ", Preço de Venda: " + prato.getPrecoVenda() + ", Tempo de Preparação: " + prato.getTempoPreparacao() + ", Disponível: " + prato.isDisponivel());
            }
        }

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Criação do log
        String logType = "INFO";
        String logDescription = "Listagem de pratos exibida";

        logsController.criarLog(currentDay, currentHour, logType, logDescription);
    }

    public boolean editarPratoNoMenuGerirMenusView(Scanner scanner){

        System.out.print("\nID do menu: ");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("ID do prato a editar: ");
        int pratoId = scanner.nextInt();
        scanner.nextLine();

        Prato prato = getPratoById(pratoId);

        if (prato != null) {

            System.out.println("\nListando pratos da categoria " + prato.getCategoria() + ":");
            listarPratosPorCategoria(prato.getCategoria());

            System.out.print("Selecione o id do novo prato: ");
            String novoPratoId = scanner.nextLine();

            menuController.editarPratoNoMenu(menuId, pratoId, novoPratoId);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Prato editado no menu ID: %d, prato antigo ID: %d, novo prato ID: %s",
                    menuId, pratoId, novoPratoId);

            logsController.criarLog(currentDay, currentHour, logType, logDescription);

            return true;
        } else {
            return  false;
        }
    }

    public void listarPratosRegistarPedidosView(Scanner scanner, int idMesa, int clienteIndex){

        Prato[] pratos = getPratos();

        if (pratos.length == 0) {
            System.out.println("Não há pratos disponíveis no momento.");
            return;
        }

        System.out.println("\n-- Pratos Disponíveis --");
        for (Prato prato : pratos) {
            if (prato != null && prato.isDisponivel()) {
                System.out.println("ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda());
            }
        }

        int idPrato = -1;
        while (idPrato == -1) {
            System.out.print("Escolha o ID do prato: ");
            try {
                idPrato = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro.");
                scanner.nextLine(); // Limpar o buffer
            }
        }

        Prato pratoSelecionado = getPratoById(idPrato);
        if (pratoSelecionado != null && pratoSelecionado.isDisponivel()) {
            mesaController.adicionarPratoAoPedido(idMesa, pratoSelecionado);
            System.out.printf("Prato %s adicionado ao pedido do Cliente %d.%n", pratoSelecionado.getNome(), clienteIndex + 1);

            // Obter o dia atual e a unidade de tempo atual da simulação
            int currentDay = simulacaoDiaController.getDiaAtual();
            int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

            // Criação do log
            String logType = "ACTION";
            String logDescription = String.format("Prato %s (ID: %d) adicionado ao pedido da mesa ID: %d", pratoSelecionado.getNome(), pratoSelecionado.getId(), idMesa);

            logsController.criarLog(currentDay, currentHour, logType, logDescription);
        } else {
            System.out.println("Prato inválido ou não disponível.");
        }
    }
    public void salvarPratos() {

        pratoDAL.salvarPratos(pratos);
    }
}