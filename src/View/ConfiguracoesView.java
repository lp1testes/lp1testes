package View;

import java.util.Scanner;
import Controller.ConfiguracaoController;

public class ConfiguracoesView {

    public ConfiguracoesView() {
        configuracaoController = ConfiguracaoController.getInstancia();
    }

    private ConfiguracaoController configuracaoController;

    public void exibirMenuConfiguracoes(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Configurações --");
            System.out.println("1. Configurar Caminho de Ficheiros");
            System.out.println("2. Configurar Separador de Ficheiros");
            System.out.println("3. Configurar Unidades de Tempo por Dia");
            System.out.println("4. Configurar Unidades de Tempo por Ação");
            System.out.println("5. Configurar Custo por Cliente Não Atendido");
            System.out.println("6. Alterar Password de Configurações");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 6);

            switch (opcao) {
                case 1:
                    configurarCaminhoFicheiros(scanner);
                    break;
                case 2:
                    configurarSeparadorFicheiros(scanner);
                    break;
                case 3:
                    configurarUnidadesTempoDia(scanner);
                    break;
                case 4:
                    exibirMenuUnidadesDeTempoPorAcao(scanner);
                    break;
                case 5:
                    configurarCustoClienteNaoAtendido(scanner);
                    break;
                case 6:
                    alterarPasswordConfiguracoes(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    public void exibirMenuUnidadesDeTempoPorAcao(Scanner scanner) {
        int opcao;

        do {
            System.out.println("\n-- Configurações unidades de tempo por ação --");
            System.out.println("1. Configurar Unidades de Tempo para ir para Mesa");
            System.out.println("2. Configurar Unidades de Tempo para o Pedido");
            System.out.println("3. Configurar Unidades de Tempo para o Pagamento");
            System.out.println("0. Voltar ao Menu Anterior");
            System.out.print("Escolha uma opção: ");

            MenuPrincipalView menuPrincipalView = new MenuPrincipalView();
            opcao = menuPrincipalView.obterOpcaoValida(0, 3);

            switch (opcao) {
                case 1:
                    configurarUnidadesTempoIrParaMesa(scanner);
                    break;
                case 2:
                    configurarUnidadesTempoParaPedido(scanner);
                    break;
                case 3:
                    configurarUnidadesTempoParaPagamento(scanner);
                    break;
                case 0:
                    System.out.println("Voltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private void configurarCaminhoFicheiros(Scanner scanner) {
        System.out.print("Insira o novo caminho de ficheiros: ");
        String novoCaminho = scanner.nextLine();
        configuracaoController.alterarCaminhoFicheiros(novoCaminho);
        System.out.println("Caminho de ficheiros configurado com sucesso!");
    }

    private void configurarSeparadorFicheiros(Scanner scanner) {
        System.out.print("Insira o novo separador de ficheiros: ");
        String novoSeparador = scanner.nextLine();
        configuracaoController.alterarSeparadorFicheiros(novoSeparador);
        System.out.println("Separador de ficheiros configurado com sucesso!");
    }

    private void configurarUnidadesTempoDia(Scanner scanner) {
        System.out.print("Insira o novo valor para unidades de tempo por dia: ");
        String novaUnidadeTempoStr = scanner.nextLine();
        configuracaoController.alterarUnidadeTempoDia(novaUnidadeTempoStr);
        System.out.println("Unidades de tempo por dia configuradas com sucesso!");
    }

    private void configurarUnidadesTempoIrParaMesa(Scanner scanner) {
        System.out.print("Insira o novo valor para unidades de tempo para ir para a Mesa: ");
        String novaUnidadeTempoIrParaMesa = scanner.nextLine();
        configuracaoController.alterarUnidadeTempoIrParaMesa(novaUnidadeTempoIrParaMesa);
        System.out.println("Unidades de tempo para ir para a Mesa configurada com sucesso!");
    }

    private void configurarUnidadesTempoParaPedido(Scanner scanner) {
        System.out.print("Insira o novo valor para unidades de tempo para Pedido: ");
        String novaUnidadeTempoParaPedido = scanner.nextLine();
        configuracaoController.alterarUnidadeTempoParaPedido(novaUnidadeTempoParaPedido);
        System.out.println("Unidades de tempo para Pedido configurada com sucesso!");
    }

    private void configurarUnidadesTempoParaPagamento(Scanner scanner) {
        System.out.print("Insira o novo valor para unidades de tempo para Pagamento: ");
        String novaUnidadeTempoParaPagamento = scanner.nextLine();
        configuracaoController.alterarUnidadeTempoParaPagamento(novaUnidadeTempoParaPagamento);
        System.out.println("Unidades de tempo para Pagamento configurada com sucesso!");
    }

    private void configurarCustoClienteNaoAtendido(Scanner scanner) {
        System.out.print("Insira o novo valor para custo por cliente não atendido: ");
        String novoCustoStr = scanner.nextLine();
        configuracaoController.alterarCustoClienteNaoAtendido(novoCustoStr);
        System.out.println("Custo por cliente não atendido configurado com sucesso!");
    }

    private void alterarPasswordConfiguracoes(Scanner scanner) {
        System.out.print("Insira a nova password: ");
        configuracaoController.getConfiguracao().getPassword();
        String novaPassword = scanner.nextLine();
        configuracaoController.alterarPassword(novaPassword);
        System.out.println("Password de configurações alterada com sucesso!");
    }
}