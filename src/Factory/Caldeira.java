package Factory;

import Observer.Funcionario;
import Observer.Maquina;
import Prototype.CloneableMaquina;

public class Caldeira extends Maquina implements MaquinaFactory, CloneableMaquina {

    Funcionario funcionario = new Funcionario("Operador");

    @Override
    public Maquina createMaquina() {
        return new Caldeira();
    }

    @Override
    public void monitorar() {
        this.temperatura = arredondarValor(gerarTemperaturaAleatoria(50, 100));
        this.percentual = arredondarValor(gerarPercentualAleatorio());

        if (temperatura >= 90) {
            this.alerta = "Alerta: Temperatura da Caldeira alta!";
            this.alerta = funcionario.update(this);
            notifySubscribers();
        } else if(temperatura <= 60) {
            this.alerta = "Alerta! Temperatura da Caldeira baixa!";
            this.alerta = funcionario.update(this);
            notifySubscribers();
        }

        if(percentual < 15){
            this.alerta = "Alerta! Nível da Caldeira baixo!";
            this.alerta = funcionario.update(this);
            notifySubscribers();
        }
    }

    @Override
    public String getLabelPercentual() {
        return "Capacidade";
    }

    @Override
    public Caldeira clone() throws CloneNotSupportedException {
        return (Caldeira) super.clone();
    }

    private double gerarTemperaturaAleatoria(double min, double max) {
        return min + Math.random() * (max - min);
    }

    private double gerarPercentualAleatorio() {
        return Math.random() * 100;
    }

    private double arredondarValor(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
