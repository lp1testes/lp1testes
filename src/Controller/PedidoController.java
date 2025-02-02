package Controller;

import DAL.PedidoDAL;
import Model.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PedidoController {

    private Pedido pedido;
    private PedidoDAL pedidoDAL;
    private SimulacaoDiaController simulacaoDiaController;
    private MesaController mesaController;
    private ReservaController reservaController;
    private LogsController logsController;

    public PedidoController() {

        pedido = new Pedido();
        this.simulacaoDiaController = new SimulacaoDiaController();
        this.reservaController = new ReservaController();
        this.mesaController = new MesaController();
        this.logsController = new LogsController();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void atualizarPedido(Pedido novoPedido) {
        pedido = novoPedido;
    }

    public String efetuarPagamentoView(Scanner scanner){

        System.out.print("Digite o ID da mesa para efetuar o pagamento: ");

        int idMesa = -1;

        try {
            idMesa = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpar o buffer
            return "Entrada inválida! Por favor, insira um número inteiro.";
        }

        int tempoAtual = simulacaoDiaController.getUnidadeTempoAtual();

        if (!mesaController.podeEfetuarPagamento(idMesa, tempoAtual)) {
            return "O pagamento só pode ser efetuado quando o prato com o maior tempo de preparo tiver sido consumido.";
        }

        mesaController.efetuarPagamento(idMesa, tempoAtual);

        // Obter o dia atual e a unidade de tempo atual da simulação
        int currentDay = simulacaoDiaController.getDiaAtual();
        int currentHour = simulacaoDiaController.getUnidadeTempoAtual();

        // Obter o pedido associado à mesa
        Pedido pedido = mesaController.getPedidoByMesa(idMesa);
        if (pedido == null) {
            return "Pedido não encontrado para a mesa " + idMesa;
        }

        // Obter a reserva associada ao pedido
        Reserva reserva = mesaController.getClienteDaMesa(idMesa);
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

        logsController.criarLog(currentDay, currentHour, logType, logDescription);

        return "Pagamento efetuada com sucesso";
    }

}
