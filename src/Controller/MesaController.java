package Controller;

import DAL.MesaDAL;
import Model.Configuracao;
import Model.Mesa;
import Model.Reserva;
import Model.MesaReserva;
import Model.Prato;
import Model.Menu;
import Model.Pedido;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MesaController {

    private Mesa[] mesas;
    private MesaDAL mesaDAL;
    private ReservaController reservaController;
    private MesaReserva[] mesaReservas;
    private int mesaReservaCount;
    private List<Pedido> pedidos;

    public MesaController(Configuracao configuracao, ReservaController reservaController) {
        mesaDAL = new MesaDAL(configuracao);
        mesas = mesaDAL.carregarMesas();
        this.reservaController = reservaController;
        this.mesaReservas = new MesaReserva[100]; // Exemplo de tamanho fixo
        this.mesaReservaCount = 0;
        this.pedidos = new ArrayList<>();
    }

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

        // Verificar se o cliente pode fazer o pedido
        if (tempoAtual < tempoAssociacao + 1) {
            System.out.println("O cliente terá que esperar uma unidade de tempo para fazer o pedido.");
            return; // Adicione o return aqui para garantir que o pedido não seja registrado
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
            pedidos.add(pedido);
        }
        for (int i = 0; i < reservaController.getReservaById(mesaReserva.getIdReserva()).getNumeroPessoas(); i++) {
            System.out.println("Cliente " + (i + 1) + ": Escolha entre prato ou menu:");
            String escolha = scanner.nextLine().trim().toLowerCase();
            if (escolha.equals("prato")) {
                listarPratos(scanner, idMesa, pedido);
            } else if (escolha.equals("menu")) {
                listarMenus(scanner, idMesa, pedido);
            } else {
                System.out.println("Escolha inválida.");
                i--; // Pergunta novamente ao mesmo cliente
            }
        }

        // Mostrar os totais após registrar o pedido
        System.out.printf("Total Custo: %.2f\n", pedido.getTotalCusto());
        System.out.printf("Total Venda: %.2f\n", pedido.getTotalVenda());
        System.out.printf("Lucro: %.2f\n", pedido.getLucro());
    }


    private void listarPratos(Scanner scanner, int idMesa, Pedido pedido) {
        PratoController pratoController = new PratoController();
        Prato[] pratos = pratoController.getPratos();
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

        System.out.print("Escolha o ID do prato: ");
        int idPrato = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Prato pratoSelecionado = pratoController.getPratoById(idPrato);
        if (pratoSelecionado != null && pratoSelecionado.isDisponivel()) {
            pedido.adicionarPrato(pratoSelecionado);
            System.out.println("Prato " + pratoSelecionado.getNome() + " adicionado ao pedido.");
        } else {
            System.out.println("Prato inválido ou não disponível.");
        }
    }

    private void listarMenus(Scanner scanner, int idMesa, Pedido pedido) {
        MenuController menuController = new MenuController();
        Menu[] menus = menuController.obterTodosMenus();
        if (menus.length == 0) {
            System.out.println("Não há menus disponíveis no momento.");
            return;
        }

        System.out.println("\n-- Menus Disponíveis --");
        for (Menu menu : menus) {
            if (menu != null) {
                System.out.println("ID: " + menu.getId() + " - Pratos:");
                for (Prato prato : menu.getPratos()) {
                    if (prato != null && prato.isDisponivel()) {
                        System.out.println("   ID: " + prato.getId() + " - Nome: " + prato.getNome() + " - Preço: " + prato.getPrecoVenda());
                    }
                }
            }
        }

        System.out.print("Escolha o ID do menu: ");
        int idMenu = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer

        Menu menuSelecionado = menuController.getMenuById(idMenu);
        if (menuSelecionado != null) {
            pedido.adicionarMenu(menuSelecionado);
            System.out.println("Menu " + menuSelecionado.getId() + " adicionado ao pedido.");
        } else {
            System.out.println("Menu inválido.");
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

    private Pedido getPedidoByMesa(int idMesa) {
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

        // Mostrar os totais
        System.out.printf("Total Custo: %.2f\n", pedido.getTotalCusto());
        System.out.printf("Total Venda: %.2f\n", pedido.getTotalVenda());
        System.out.printf("Lucro: %.2f\n", pedido.getLucro());
    }  }