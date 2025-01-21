package DAL;

import Model.Menu;
import Model.Prato;
import java.io.*;

public class MenuDAL {

    private static final String FILE_PATH = "resources/Menus.txt";
    private static final int MAX_MENUS = 100;

    public Menu[] carregarTodosMenus() {
        Menu[] menus = new Menu[MAX_MENUS];
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return menus;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int menuIndex = 0;
            Menu currentMenu = null;
            while ((line = br.readLine()) != null && menuIndex < MAX_MENUS) {
                if (line.trim().isEmpty()) {
                    if (currentMenu != null && currentMenu.getPratoCount() > 0) {
                        menus[menuIndex++] = currentMenu;
                    }
                    currentMenu = new Menu();
                } else {
                    String[] parts = line.split(";");
                    if (parts.length == 8) {
                        try {
                            Integer menuId = Integer.parseInt(parts[0].trim());
                            if (currentMenu == null || !menuId.equals(currentMenu.getId())) {
                                if (currentMenu != null && currentMenu.getPratoCount() > 0) {
                                    menus[menuIndex++] = currentMenu;
                                }
                                currentMenu = new Menu();
                                currentMenu.setId(menuId);
                            }
                            Integer id = Integer.parseInt(parts[1].trim());
                            String nome = parts[2].trim();
                            String categoria = parts[3].trim();
                            double precoCusto = Double.parseDouble(parts[4].trim());
                            double precoVenda = Double.parseDouble(parts[5].trim());
                            int tempoPreparacao = Integer.parseInt(parts[6].trim());
                            boolean disponivel = Boolean.parseBoolean(parts[7].trim());
                            Prato prato = new Prato(id, nome, categoria, precoCusto, precoVenda, tempoPreparacao, disponivel);
                            currentMenu.adicionarPrato(prato);
                        } catch (NumberFormatException e) {
                            System.err.println("Menu mal configurado ignorado: " + line);
                        }
                    } else {
                        System.err.println("Menu mal configurado ignorado: " + line);
                    }
                }
            }
            if (currentMenu != null && currentMenu.getPratoCount() > 0) {
                menus[menuIndex] = currentMenu;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return menus;
    }

    public void salvarMenus(Menu[] menus) {
        File file = new File(FILE_PATH);
        File directory = file.getParentFile();

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Menu menu : menus) {
                if (menu != null) {
                    for (Prato prato : menu.getPratos()) {
                        if (prato != null) {
                            bw.write(menu.getId() + ";" + prato.getId() + ";" + prato.getNome() + ";" + prato.getCategoria() + ";" + prato.getPrecoCusto() + ";" + prato.getPrecoVenda() + ";" + prato.getTempoPreparacao() + ";" + prato.isDisponivel());
                            bw.newLine();
                        }
                    }
                    bw.newLine(); // Separar menus com uma linha em branco
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}