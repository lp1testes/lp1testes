package Controller;

import DAL.PedidoDAL;
import Model.*;

import java.util.*;

public class PedidoController {

    private static final int LIMITE = 100;
    private Pedido[] pedidos;
    private int pedidoCount;

    private static PedidoController instance;
    private PedidoDAL pedidoDAL;
    private SimulacaoDiaController simulacaoDiaController;
    private MesaController mesaController;
    private ReservaController reservaController;
    private LogsController logsController;

    public PedidoController() {
        pedidos = new Pedido[LIMITE];
        pedidoCount = 0;
        this.simulacaoDiaController = SimulacaoDiaController.getInstance();
        this.reservaController = ReservaController.getInstance();
        this.mesaController = MesaController.getInstance();
        this.logsController = LogsController.getInstance();
    }
    public static synchronized PedidoController getInstance() {
        if (instance == null) {
            instance = new PedidoController();
        }
        return instance;
    }


    public Pedido getPedidoByMesa(int idMesa) {
        for (int i = 0; i < pedidoCount; i++) {
            if (pedidos[i] != null && pedidos[i].getMesa().getId() == idMesa) {
                return pedidos[i];
            }
        }
        return null;
    }

    public Pedido[] getPedidos() {
        return Arrays.copyOf(pedidos, pedidoCount);
    }

    public void adicionarPedido(Pedido pedido) {
        if (pedidoCount < LIMITE) {
            pedidos[pedidoCount++] = pedido;
        } else {
            System.out.println("Limite de pedidos atingido!");
        }
    }

    public void atualizarPedido(Pedido novoPedido) {
        for (int i = 0; i < pedidoCount; i++) {
            if (pedidos[i] != null && pedidos[i].getMesa().getId() == novoPedido.getMesa().getId()) {
                pedidos[i] = novoPedido;
                return;
            }
        }
        System.out.println("Pedido não encontrado para atualização.");
    }

    public void removerPedido(int idMesa) {
        for (int i = 0; i < pedidoCount; i++) {
            if (pedidos[i] != null && pedidos[i].getMesa().getId() == idMesa) {
                pedidos[i] = null;
                // Reorganizar o array para remover o null
                System.arraycopy(pedidos, i + 1, pedidos, i, pedidoCount - i - 1);
                pedidos[--pedidoCount] = null;
                return;
            }
        }
        System.out.println("Pedido não encontrado para remoção.");
    }

    public String efetuarPagamentoView(Scanner scanner) {
        // Listar as mesas ocupadas com pedidos associados logo de início
        StringBuilder mesasOcupadas = new StringBuilder();
        mesasOcupadas.append("\nMesas ocupadas com pedidos associados:\n");
        for (Mesa mesa : MesaController.getInstance().getMesas()) {
            if (mesa != null && mesa.isOcupada() && MesaController.getInstance().getPedidoByMesa(mesa.getId()) != null) {
                mesasOcupadas.append("Mesa ID: ")
                        .append(MesaController.getInstance().getPedidoByMesa(mesa.getId()));
            }
        }
        System.out.println(mesasOcupadas.toString());

        System.out.print("Digite o ID da mesa para efetuar o pagamento: ");

        int idMesa = -1;
        try {
            idMesa = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpar o buffer
            return "Entrada inválida! Por favor, insira um número inteiro.";
        }

        int tempoAtual = SimulacaoDiaController.getInstance().getUnidadeTempoAtual();

        if (!MesaController.getInstance().podeEfetuarPagamento(idMesa, tempoAtual)) {
            return "O pagamento só pode ser efetuado quando o prato com o maior tempo de preparo tiver sido consumido.";
        }

        MesaController.getInstance().efetuarPagamento(idMesa, tempoAtual);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = SimulacaoDiaController.getInstance().getDiaAtual();
        int currentHour = SimulacaoDiaController.getInstance().getUnidadeTempoAtual();

        // Obter o pedido associado à mesa
        Pedido pedido = MesaController.getInstance().getPedidoByMesa(idMesa);
        if (pedido == null) {
            return "Pedido não encontrado para a mesa " + idMesa;
        }

        // Obter a reserva associada ao pedido
        Reserva reserva = MesaController.getInstance().getClienteDaMesa(idMesa);
        if (reserva == null) {
            return "Reserva não encontrada para a mesa " + idMesa;
        }

        // Criar a lista de pratos consumidos
        StringBuilder pratosConsumidos = new StringBuilder();
        for (Prato prato : pedido.getPratos()) {
            if (prato != null) {
                pratosConsumidos.append(prato.getNome()).append(", ");
            }
        }

        // Adicionar pratos dos menus ao log
        for (Menu menu : pedido.getMenus()) {
            if (menu != null) { // Verificação nula adicionada
                for (Prato prato : menu.getPratos()) {
                    if (prato != null) {
                        pratosConsumidos.append(prato.getNome()).append(", ");
                    }
                }
            }
        }

        // Remover a última vírgula e espaço, se houver
        if (pratosConsumidos.length() > 0) {
            pratosConsumidos.setLength(pratosConsumidos.length() - 2);
        }

        // Criar o log com o número de pessoas
        String logType = "ACTION";
        String logDescription = String.format("Pagamento efetuado para a mesa ID: %d. Número de Pessoas: %d, Pratos consumidos: %s",
                idMesa, reserva.getNumeroPessoas(), pratosConsumidos.toString());

        LogsController.getInstance().criarLog(currentDay, currentHour, logType, logDescription);

        return "Pagamento efetuado com sucesso";
    } }