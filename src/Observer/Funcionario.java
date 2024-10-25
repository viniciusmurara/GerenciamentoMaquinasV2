package Observer;

public class Funcionario implements Subscriber {
    private String cargo;

    public Funcionario(String cargo){
        this.cargo = cargo;
    }

    @Override
    public String update(Maquina maquina) {
        return this.cargo + ": " + maquina.getAlerta();
    }
}
