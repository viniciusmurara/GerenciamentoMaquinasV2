package Factory;

import Observer.Funcionario;
import Observer.Maquina;

public class Caldeira extends Maquina implements MaquinaFactory {

    Funcionario funcionario = new Funcionario("Operador");

    @Override
    public Maquina createMaquina() {
        return new Caldeira();
    }

    @Override
    public void monitorar() {
        this.temperatura = arredondarValor(gerarTemperaturaAleatoria(50, 100));
        this.percentual = arredondarValor(gerarPercentualAleatorio());

        if (temperatura >= 90 || temperatura <= 60) {
            this.alerta = "Alerta: Caldeira em anomalia!";
            this.alerta = funcionario.update(this);
            notifySubscribers();
        }
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
