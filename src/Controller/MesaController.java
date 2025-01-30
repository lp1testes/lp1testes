package Controller;

import DAL.MesaDAL;
import Model.Configuracao;
import Model.Mesa;
import Model.Reserva;
import Model.MesaReserva;
import Model.Prato;
import Model.Menu;
import Model.Pedido;

import java.util.*;

public class MesaController {
    private static MesaController instance;
    private Mesa[] mesas;
    private MesaDAL mesaDAL;
    private ReservaController reservaController;
    private MesaReserva[] mesaReservas;
    private int mesaReservaCount;
    private List<Pedido> pedidos;

    private MesaController(Configuracao configuracao, ReservaController reservaController) {
        mesaDAL = new MesaDAL(configuracao);
        mesas = mesaDAL.carregarMesas();
        this.reservaController = reservaController;
        this.mesaReservas = new MesaReserva[100]; // Exemplo de tamanho fixo
        this.mesaReservaCount = 0;
        this.pedidos = new ArrayList<>();
    }

    public static synchronized MesaController getInstance(Configuracao configuracao, ReservaController reservaController) {
        if (instance == null) {
            instance = new MesaController(configuracao, reservaController);
        }
        return instance;
    }

    public void setReservaController(ReservaController reservaController) {
        this.reservaController = reservaController;

    }

    // Restante da classe permanece inalterada...

        public Mesa[] getMesas() {
        return mesas;
    }

    public void adicionarMesa(Mesa novaMesa) {
        int proximoId = mesaDAL.obterProximoId(mesas);
        novaMesa.setId(proximoId);

        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] == null) {
                mesas[i] = novaMesa;
                mesaDAL.salvarMesas(mesas);
                return;
            }
        }
        System.out.println("Não é possível adicionar mais mesas. Capacidade máxima atingida.");
    }

    public void editarMesa(int id, int novaCapacidade, boolean novaOcupacao) {
        for (Mesa mesa : mesas) {
            if (mesa != null && mesa.getId() == id) {
                mesa.setCapacidade(novaCapacidade);
                mesa.setOcupada(novaOcupacao);
                mesaDAL.salvarMesas(mesas);
                return;
            }
        }
        System.out.println("Mesa não encontrada.");
    }

    public void removerMesa(int id) {
        for (int i = 0; i < mesas.length; i++) {
            if (mesas[i] != null && mesas[i].getId() == id) {
                mesas[i] = null;
                mesaDAL.salvarMesas(mesas);
                return;
            }
        }
        System.out.println("Mesa não encontrada.");
    }

    public void atribuirClientesAMesa(int idMesa, Reserva reserva, int tempoAtual) {
        if (reserva.isAssociada()) {
            System.out.println("Reserva " + reserva.getNome() + " já está associada a uma mesa.");
            return;
        }

        for (Mesa mesa : mesas) {
            if (mesa != null && mesa.getId() == idMesa) {
                if (!mesa.isOcupada() && reserva.getNumeroPessoas() <= mesa.getCapacidade()) {
                    mesa.setOcupada(true);
                    mesaDAL.salvarMesas(mesas);
                    MesaReserva mesaReserva = new MesaReserva(idMesa, reserva.getId(), tempoAtual);
                    mesaReservas[mesaReservaCount++] = mesaReserva; // Adicionando a nova reserva ao array
                    reserva.setAssociada(true); // Marcar a reserva como associada
                    System.out.println("Clientes da reserva " + reserva.getNome() + " atribuídos à mesa " + idMesa);
                    return;
                } else {
                    System.out.println("Mesa ocupada ou capacidade insuficiente.");
                    return;
                }
            }
        }
        System.out.println("Mesa não encontrada.");
    }

    public Reserva getClienteDaMesa(int idMesa) {
        for (int i = 0; i < mesaReservaCount; i++) {
            if (mesaReservas[i] != null && mesaReservas[i].getIdMesa() == idMesa) {
                return reservaController.getReservaById(mesaReservas[i].getIdReserva());
            }
        }
        return null;
    }

    public void removerReservaDaMesa(int idMesa) {
        for (int i = 0; i < mesaReservaCount; i++) {
            if (mesaReservas[i] != null && mesaReservas[i].getIdMesa() == idMesa) {
                mesaReservas[i] = null;
                mesaReservaCount--;
                return;
            }
        }
    }

    public Mesa[] listarMesasDisponiveis() {
        Mesa[] mesasDisponiveis = new Mesa[mesas.length];
        int index = 0;
        for (Mesa mesa : mesas) {
            if (mesa != null && !mesa.isOcupada()) {
                mesasDisponiveis[index++] = mesa;
            }
        }
        return Arrays.copyOf(mesasDisponiveis, index);
    }

    public Mesa[] listarMesasOcupadasComReservasNaoAtendidas(int tempoAtual, int unidadesTempoParaPedido) {
        Mesa[] mesasOcupadas = new Mesa[mesas.length];
        int index = 0;

        for (int i = 0; i < mesaReservaCount; i++) {
            if (mesaReservas[i] != null && !mesaReservas[i].isAtendida()) {
                Reserva reserva = reservaController.getReservaById(mesaReservas[i].getIdReserva());
                int tempoAssociacao = mesaReservas[i].getTempoAssociacao();
                int tempoLimite = tempoAssociacao + unidadesTempoParaPedido;

                if (tempoAtual <= tempoLimite) {
                    for (Mesa mesa : mesas) {
                        if (mesa != null && mesa.getId() == mesaReservas[i].getIdMesa() && mesa.isOcupada()) {
                            mesasOcupadas[index++] = mesa;
                        }
                    }
                }
            }
        }
        return Arrays.copyOf(mesasOcupadas, index);
    }

    public void marcarReservaComoAtendida(int idMesa) {
        for (int i = 0; i < mesaReservaCount; i++) {
            if (mesaReservas[i] != null && mesaReservas[i].getIdMesa() == idMesa) {
                mesaReservas[i].setAtendida(true);
                return;
            }
        }
    }

    public void registrarPedido(int idMesa, int tempoAtual, Scanner scanner) {
        MesaReserva mesaReserva = null;
        for (int i = 0; i < mesaReservaCount; i++) {
            if (mesaReservas[i] != null && mesaReservas[i].getIdMesa() == idMesa) {
                mesaReserva = mesaReservas[i];
                break;
            }
        }

        if (mesaReserva == null) {
            System.out.println("Nenhuma reserva encontrada para a mesa " + idMesa);
            return;
        }

        int tempoAssociacao = mesaReserva.getTempoAssociacao();

        if (tempoAtual < tempoAssociacao + 1) {
            System.out.println("O cliente terá que esperar uma unidade de tempo para fazer o pedido.");
            return;
        }

        Configuracao configuracao = ConfiguracaoController.getInstancia().getConfiguracao();
        int tempoLimite = tempoAssociacao + configuracao.getUnidadesTempoParaPedido();

        if (tempoAtual > tempoLimite) {
            System.out.println("Tempo limite para registrar o pedido expirou. Clientes da reserva " + mesaReserva.getIdReserva() + " foram embora.");
            removerReservaDaMesa(idMesa);
            return;
        }

        // Mover esta mensagem para depois da verificação
        System.out.println("Clientes da reserva " + mesaReserva.getIdReserva() + " estão prontos para fazer o pedido.");

        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null) {
            pedido = new Pedido();
            pedido.setMesa(getMesaById(idMesa));
            pedido.setTempoPedido(tempoAtual);
            pedidos.add(pedido);
        }

        int tempoRestanteDia = configuracao.getUnidadesTempoDia() - (tempoAtual + 1 + configuracao.getUnidadesTempoParaPagamento());

        for (int i = 0; i < reservaController.getReservaById(mesaReserva.getIdReserva()).getNumeroPessoas(); i++) {
            boolean pedidoValido = false;
            while (!pedidoValido) {
                System.out.println("Cliente " + (i + 1) + ": Escolha entre prato ou menu:");
                String escolha = scanner.nextLine().trim().toLowerCase();
                if (escolha.equals("prato")) {
                    pedidoValido = listarPratos(scanner, idMesa, pedido, tempoRestanteDia);
                } else if (escolha.equals("menu")) {
                    pedidoValido = listarMenus(scanner, idMesa, pedido, tempoRestanteDia);
                } else {
                    System.out.println("Escolha inválida.");
                }
            }
        }

        // Mostrar os totais após registrar o pedido
        System.out.printf("Total Custo: %.2f\n", pedido.getTotalCusto());
        System.out.printf("Total Venda: %.2f\n", pedido.getTotalVenda());
        System.out.printf("Lucro: %.2f\n", pedido.getLucro());
    }

    private boolean listarPratos(Scanner scanner, int idMesa, Pedido pedido, int tempoRestanteDia) {
        PratoController pratoController = new PratoController();
        Prato[] pratos = pratoController.getPratos();
        if (pratos.length == 0) {
            System.out.println("Não há pratos disponíveis no momento.");
            return false;
        }

        System.out.println("\n-- Pratos Disponíveis --");
        for (Prato prato : pratos) {
            if (prato != null) {
                System.out.println("ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda() + " - Tempo de Preparação: " + prato.getTempoPreparacao());
            }
        }

        System.out.print("Escolha o ID do prato: ");
        int idPrato = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Prato pratoSelecionado = pratoController.getPratoById(idPrato);
        if (pratoSelecionado != null && pratoSelecionado.isDisponivel() && pratoSelecionado.getTempoPreparacao() <= tempoRestanteDia) {
            pedido.adicionarPrato(pratoSelecionado);
            System.out.printf("Prato %s adicionado ao pedido.%n", pratoSelecionado.getNome());
            return true;
        } else if (pratoSelecionado != null && pratoSelecionado.getTempoPreparacao() > tempoRestanteDia) {
            System.out.println("Prato selecionado ultrapassa o tempo de preparação disponível no dia.");
            return false;
        } else {
            System.out.println("Prato inválido ou não disponível.");
            return false;
        }
    }

    private boolean listarMenus(Scanner scanner, int idMesa, Pedido pedido, int tempoRestanteDia) {
        MenuController menuController = new MenuController();
        Menu[] menus = menuController.obterTodosMenus();
        if (menus.length == 0) {
            System.out.println("Não há menus disponíveis no momento.");
            return false;
        }

        System.out.println("\n-- Menus Disponíveis --");
        for (Menu menu : menus) {
            if (menu != null) {
                System.out.println("ID: " + menu.getId() + " - Pratos:");
                for (Prato prato : menu.getPratos()) {
                    if (prato != null) {
                        System.out.println("   ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda() + " - Tempo de Preparação: " + prato.getTempoPreparacao());
                    }
                }
            }
        }

        System.out.print("Escolha o ID do menu: ");
        int idMenu = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Menu menuSelecionado = menuController.getMenuById(idMenu);
        if (menuSelecionado != null) {
            int maiorTempoPreparacao = 0;
            for (Prato prato : menuSelecionado.getPratos()) {
                if (prato != null && prato.getTempoPreparacao() > maiorTempoPreparacao) {
                    maiorTempoPreparacao = prato.getTempoPreparacao();
                }
            }

            if (maiorTempoPreparacao <= tempoRestanteDia) {
                pedido.adicionarMenu(menuSelecionado);
                System.out.printf("Menu %d adicionado ao pedido.%n", menuSelecionado.getId());
                return true;
            } else {
                System.out.println("Menu contém pratos que ultrapassam o tempo de preparação disponível no dia.");
                return false;
            }
        } else {
            System.out.println("Menu inválido.");
            return false;
        }
    }

    public void adicionarPratoAoPedido(int idMesa, Prato prato) {
        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null) {
            pedido = new Pedido();
            pedido.setMesa(getMesaById(idMesa));
            pedidos.add(pedido);
        }
        pedido.adicionarPrato(prato);
    }

    public void adicionarMenuAoPedido(int idMesa, Menu menu) {
        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null) {
            pedido = new Pedido();
            pedido.setMesa(getMesaById(idMesa));
            pedidos.add(pedido);
        }
        pedido.adicionarMenu(menu);
    }

    public Pedido getPedidoByMesa(int idMesa) {
        for (Pedido pedido : pedidos) {
            if (pedido.getMesa().getId() == idMesa) {
                return pedido;
            }
        }
        return null;
    }

    private Mesa getMesaById(int idMesa) {
        for (Mesa mesa : mesas) {
            if (mesa != null && mesa.getId() == idMesa) {
                return mesa;
            }
        }
        return null;
    }

    public void listarPedidosAtendidos(int idMesa) {
        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null || (pedido.getPratos().isEmpty() && pedido.getMenus().isEmpty())) {
            System.out.println("Não há pedidos atendidos para a mesa " + idMesa);
            return;
        }

        System.out.println("Pedidos atendidos para a mesa " + idMesa + ":");
        int clienteCount = 1;

        for (Prato prato : pedido.getPratos()) {
            System.out.println("Cliente " + clienteCount + ": Prato");
            System.out.println("   " + prato.getNome());
            clienteCount++;
        }

        for (Menu menu : pedido.getMenus()) {
            System.out.println("Cliente " + clienteCount + ": Menu");
            for (Prato prato : menu.getPratos()) {
                if (prato != null) {
                    System.out.println("   " + prato.getNome());
                }
            }
            clienteCount++;
        }

        System.out.printf("Total Custo: %.2f\n", pedido.getTotalCusto());
        System.out.printf("Total Venda: %.2f\n", pedido.getTotalVenda());
        System.out.printf("Lucro: %.2f\n", pedido.getLucro());
        System.out.println("Estado do pedido: " + (pedido.isPago() ? "Pago" : "Não Pago"));
    }

    public void efetuarPagamento(int idMesa, int tempoAtual) {
        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null) {
            System.out.println("Pedido não encontrado para a mesa " + idMesa);
            return;
        }
        if (pedido.isPago()) {
            System.out.println("O pedido já foi pago.");
            return;
        }

        if (tempoPagamentoUltrapassado(idMesa, tempoAtual)) {
            System.out.println("O tempo limite para pagamento foi ultrapassado. Clientes da mesa " + idMesa + " foram embora.");
            removerReservaDaMesa(idMesa);
            marcarMesaComoDisponivel(idMesa);
            return;
        }

        pedido.setPago(true);
        marcarMesaComoDisponivel(idMesa); // Marcar a mesa como disponível após o pagamento
        System.out.println("Pagamento efetuado com sucesso para a mesa " + idMesa);
    }

    public void marcarMesaComoDisponivel(int idMesa) {
        Mesa mesa = getMesaById(idMesa);
        if (mesa != null) {
            mesa.setOcupada(false);
        }
    }

    public boolean podeEfetuarPagamento(int idMesa, int tempoAtual) {
        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null) {
            return false;
        }

        int tempoMaximoPreparo = getTempoMaximoPreparo(pedido);
        return tempoAtual >= pedido.getTempoPedido() + tempoMaximoPreparo;
    }

    private int getTempoMaximoPreparo(Pedido pedido) {
        int maxTempo = 0;
        for (Prato prato : pedido.getPratos()) {
            if (prato != null && prato.getTempoPreparacao() > maxTempo) {
                maxTempo = prato.getTempoPreparacao();
            }
        }
        for (Menu menu : pedido.getMenus()) {
            for (Prato prato : menu.getPratos()) {
                if (prato != null && prato.getTempoPreparacao() > maxTempo) {
                    maxTempo = prato.getTempoPreparacao();
                }
            }
        }
        return maxTempo;
    }

    public boolean tempoPagamentoUltrapassado(int idMesa, int tempoAtual) {
        Pedido pedido = getPedidoByMesa(idMesa);
        if (pedido == null) {
            return false;
        }

        Configuracao configuracao = ConfiguracaoController.getInstancia().getConfiguracao();
        int tempoLimitePagamento = pedido.getTempoPedido() + getTempoMaximoPreparo(pedido) + configuracao.getUnidadesTempoParaPagamento();

        return tempoAtual > tempoLimitePagamento;
    }
    public boolean tempoReservaUltrapassado(int idMesa, int tempoAtual) {
        MesaReserva mesaReserva = getMesaReservaByIdMesa(idMesa);
        if (mesaReserva == null) {
            // Consider the reservation as not associated if there's no MesaReserva instance
            return true;
        }

        Configuracao configuracao = ConfiguracaoController.getInstancia().getConfiguracao();
        int tempoLimiteReserva = mesaReserva.getTempoAssociacao() + configuracao.getUnidadesTempoIrParaMesa();

        return tempoAtual > tempoLimiteReserva;
    }
    public boolean tempoPedidoUltrapassado(int idMesa, int tempoAtual) {
        MesaReserva mesaReserva = getMesaReservaByIdMesa(idMesa);
        if (mesaReserva == null) {
            return false;
        }

        Configuracao configuracao = ConfiguracaoController.getInstancia().getConfiguracao();
        int tempoLimitePedido = mesaReserva.getTempoAssociacao() + configuracao.getUnidadesTempoParaPedido();

        return tempoAtual > tempoLimitePedido;
    }

    private MesaReserva getMesaReservaByIdMesa(int idMesa) {
        for (int i = 0; i < mesaReservaCount; i++) {
            if (mesaReservas[i] != null && mesaReservas[i].getIdMesa() == idMesa) {
                return mesaReservas[i];
            }
        }
        return null;
    }
    public int contarPedidosAtendidos() {
        int count = 0;
        for (Pedido pedido : pedidos) {
            if (pedido.isPago()) {
                count++;
            }
        }
        return count;
    }

    public int contarPedidosNaoAtendidos() {
        int count = 0;
        for (Pedido pedido : pedidos) {
            if (!pedido.isPago()) {
                count++;
            }
        }
        return count;
    }
}

