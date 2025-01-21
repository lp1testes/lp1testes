package Controller;

import DAL.ConfiguracaoDAL;
import DAL.PratoDAL;
import Model.Configuracao;
import Model.Prato;
import java.util.Scanner;

public class PratoController {

    private Prato[] pratos;
    private PratoDAL pratoDAL;
    private ConfiguracaoDAL configuracaoDAL;
    private Configuracao configuracao;

    public PratoController() {
        pratoDAL = new PratoDAL();
        pratos = pratoDAL.carregarPratos();
        configuracaoDAL = new ConfiguracaoDAL();
        configuracao = new Configuracao();
        configuracaoDAL.carregarConfiguracoes(configuracao);
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
                pratoDAL.salvarPratos(pratos);
                return;
            }
        }
        System.out.println("Não é possível adicionar mais pratos. Capacidade máxima atingida.");
    }

    public void atualizarPrato(Prato pratoAtualizado) {
        for (int i = 0; i < pratos.length; i++) {
            if (pratos[i] != null && pratos[i].getId().equals(pratoAtualizado.getId())) {
                pratos[i] = pratoAtualizado;
                pratoDAL.salvarPratos(pratos);
                return;
            }
        }
        System.out.println("Prato não encontrado.");
    }

    public void removerPrato(int id) {
        for (int i = 0; i < pratos.length; i++) {
            if (pratos[i] != null && pratos[i].getId() == id) {
                pratos[i] = null;
                pratoDAL.salvarPratos(pratos);
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
}