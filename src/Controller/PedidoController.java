package Controller;

import DAL.PedidoDAL;
import Model.Pedido;

public class PedidoController {

    private Pedido pedido;
    private PedidoDAL pedidoDAL;

    public PedidoController() {
        pedido = new Pedido();
        //pedido = pedidoDAL.carregarPedidos();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void atualizarPedido(Pedido novoPedido) {
        pedido = novoPedido;
        //pedidoDAL.salvarPedidos(pedido);
    }
}
