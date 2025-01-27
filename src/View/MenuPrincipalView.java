package View;

import Controller.*;
import Model.Configuracao;

import java.util.Scanner;

public class MenuPrincipalView {
    private final Scanner scanner;
    private final SimulacaoDiaController simulacaoDiaController;
    private final ConfiguracaoController configuracaoController;
    private final MesaController mesaController;
    private final ReservaController reservaController;
    private final PratoController pratoController;
    private final MenuController menuController;

    public MenuPrincipalView() {
        this.scanner = new Scanner(System.in);
        this.configuracaoController = ConfiguracaoController.getInstancia();
        this.simulacaoDiaController = SimulacaoDiaController.getInstance();
        this.reservaController = new ReservaController(configuracaoController.getConfiguracao());
        this.mesaController = new MesaController(configuracaoController.getConfiguracao(), reservaController);
        this.pratoController = new PratoController();
        this.menuController = new MenuController();

        simulacaoDiaController.setReservaController(reservaController);

        verificarUnidadeTempo();
    }

    private void verificarUnidadeTempo() {
        if (configuracaoController.getConfiguracao().getUnidadesTempoDia() == 0) {
            solicitarNovaUnidadeTempo();
        }
    }

    private void solicitarNovaUnidadeTempo() {
        boolean valido = false;
        while (!valido) {
            System.out.println("O valor no ficheiro UnidadeTempo não é válido. Por favor, insira um número inteiro válido:");
            String input = scanner.nextLine();
            try {
                int novaUnidadeTempo = Integer.parseInt(input.trim());
                configuracaoController.alterarUnidadeTempoDia(String.valueOf(novaUnidadeTempo));
                valido = true;
            } catch (NumberFormatException e) {
                System.err.println("O valor fornecido não é um número inteiro válido: " + input);
            }
        }
    }

    public void exibirMenu() {
        int opcao;

        GerirMesasView gerirMesasView = new GerirMesasView(mesaController, reservaController, simulacaoDiaController, configuracaoController);
        GerirMenusView gerirMenusView = new GerirMenusView();
        RegistarPedidosView registarPedidosView = new RegistarPedidosView(mesaController, reservaController, simulacaoDiaController, configuracaoController, pratoController, menuController);
        ReservasView reservasView = new ReservasView(reservaController, gerirMesasView, simulacaoDiaController); // Corrigido aqui
        ConfiguracoesView configuracoesView = new ConfiguracoesView();
        EstatisticasView estatisticasView = new EstatisticasView();
        DesempenhoFinanceiroView desempenhoFinanceiroView = new DesempenhoFinanceiroView();
        SimulacaoDiaView simulacaoDiaView = new SimulacaoDiaView(simulacaoDiaController);
        LogsView logsView = new LogsView();

        do {
            System.out.println("\nBem-vindo ao Restaurante!");
            System.out.println("1. Gestão de Mesas");
            System.out.println("2. Gestão de Menus");
            System.out.println("3. Registo de Pedidos");
            System.out.println("4. Registo de Reservas");
            System.out.println("5. Simulação do Dia-a-Dia");
            System.out.println("6. Logs e Eventos");
            System.out.println("7. Gestão Financeira");
            System.out.println("8. Estatísticas Gerais");
            System.out.println("9. Configurações");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = obterOpcaoValida(0, 9);

            switch (opcao) {
                case 1:
                    gerirMesasView.exibirMenuGestaoMesas(scanner);
                    break;
                case 2:
                    gerirMenusView.exibirMenuGestaoMenus(scanner);
                    break;
                case 3:
                    registarPedidosView.exibirMenuRegistoPedidos(scanner);
                    break;
                case 4:
                    reservasView.exibirMenuReservas(scanner);
                    break;
                case 5:
                    simulacaoDiaView.exibirMenuSimulacaoDia(scanner);
                    break;
                case 6:
                    logsView.exibirMenuLogsEventos(scanner);
                    break;
                case 7:
                    desempenhoFinanceiroView.exibirMenuDesempenhoFinanceiro(scanner);
                    break;
                case 8:
                    estatisticasView.exibirMenuEstatisticasGerais(scanner);
                    break;
                case 9:
                    if (realizarLogin(scanner)) {
                        configuracoesView.exibirMenuConfiguracoes(scanner);
                    } else {
                        System.out.println("Senha incorreta. Acesso negado.");
                    }
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    scanner.close();
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private boolean realizarLogin(Scanner scanner) {
        System.out.print("Insira a senha para acessar as configurações: ");
        String senhaInserida = scanner.nextLine();
        String senhaConfigurada = configuracaoController.getConfiguracao().getPassword();
        return senhaInserida.equals(senhaConfigurada);
    }

    public int obterOpcaoValida(int min, int max) {
        int opcao;
        while (true) {
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                if (opcao >= min && opcao <= max) {
                    return opcao;
                } else {
                    System.out.println("Opção inválida! Digite um número entre " + min + " e " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
            }
        }
    }
}