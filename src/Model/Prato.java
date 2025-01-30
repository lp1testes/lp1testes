    package Model;

    public class Prato {

        private Integer id;
        private String nome;
        private String categoria;
        private double precoCusto;
        private double precoVenda;
        private int tempoPreparacao;
        private boolean disponivel;

        public Prato() {
        }

        public Prato(Integer id, String nome, String categoria, double precoCusto, double precoVenda, int tempoPreparacao, boolean disponivel) {
            this.id = id;
            this.nome = nome;
            this.categoria = categoria;
            this.precoCusto = precoCusto;
            this.precoVenda = precoVenda;
            this.tempoPreparacao = tempoPreparacao;
            this.disponivel = disponivel;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public double getPrecoCusto() {
            return precoCusto;
        }

        public void setPrecoCusto(double precoCusto) {
            this.precoCusto = precoCusto;
        }

        public double getPrecoVenda() {
            return precoVenda;
        }

        public void setPrecoVenda(double precoVenda) {
            this.precoVenda = precoVenda;
        }

        public int getTempoPreparacao() {
            return tempoPreparacao;
        }

        public void setTempoPreparacao(int tempoPreparacao) {
            this.tempoPreparacao = tempoPreparacao;
        }

        public boolean isDisponivel() {
            return disponivel;
        }

        public void setDisponivel(boolean disponivel) {
            this.disponivel = disponivel;
        }
    }
