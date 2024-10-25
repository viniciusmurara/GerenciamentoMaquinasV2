package Factory;

import Observer.Funcionario;
import Observer.Maquina;

public class Resfriador extends Maquina implements MaquinaFactory {

    Funcionario funcionario = new Funcionario("Gestor");

    @Override
    public Maquina createMaquina() {
        return new Resfriador();
    }

    @Override
    public void monitorar() {
        this.temperatura = arredondarValor(gerarTemperaturaAleatoria(-25, 25));
        this.percentual = arredondarValor(gerarPercentualAleatorio());

        if (temperatura >= 15) {
            this.alerta = "Alerta! Temperatura do Resfriador alta.";
            this.alerta = funcionario.update(this);
            notifySubscribers();
        } else if (temperatura <= -15) {
            this.alerta = "Alerta! Temperatura do Resfriador baixa.";
            this.alerta = funcionario.update(this);
            notifySubscribers();
        }
    }

    @Override
    public String getLabelPercentual() {
        return "Humidade";
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