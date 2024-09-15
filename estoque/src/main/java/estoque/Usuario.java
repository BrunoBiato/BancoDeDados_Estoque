package estoque;

public class Usuario {
    private int id;
    private String nome;
    private String username;
    private String senha;
    private TipoUsuario tipo;

    public Usuario(String nome, String username, String senha, TipoUsuario tipo) {
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Usuario(int id, String nome, String username, String senha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", username='" + username + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}