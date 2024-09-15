package estoque;

import java.util.Date;
import java.util.Objects;

public class Venda {
    private int id;
    private int produtoId;
    private int quantidade;
    private double valorTotal;
    private Date dataVenda;
    private int vendedorId;

    public Venda() {
    }

    public Venda(int id, int produtoId, int quantidade, double valorTotal, Date dataVenda, int vendedorId) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.dataVenda = dataVenda;
        this.vendedorId = vendedorId;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProdutoId() { return produtoId; }
    public void setProdutoId(int produtoId) { this.produtoId = produtoId; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    public Date getDataVenda() { return dataVenda; }
    public void setDataVenda(Date dataVenda) { this.dataVenda = dataVenda; }
    public int getVendedorId() { return vendedorId; }
    public void setVendedorId(int vendedorId) { this.vendedorId = vendedorId; }

    @Override
    public String toString() {
        return "Venda{" +
               "id=" + id +
               ", produtoId=" + produtoId +
               ", quantidade=" + quantidade +
               ", valorTotal=" + valorTotal +
               ", dataVenda=" + dataVenda +
               ", vendedorId=" + vendedorId +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return id == venda.id &&
               produtoId == venda.produtoId &&
               quantidade == venda.quantidade &&
               Double.compare(venda.valorTotal, valorTotal) == 0 &&
               vendedorId == venda.vendedorId &&
               Objects.equals(dataVenda, venda.dataVenda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, produtoId, quantidade, valorTotal, dataVenda, vendedorId);
    }
}