package Observer;

import java.util.ArrayList;

public abstract class Maquina {
    private ArrayList<Subscriber> subscribers = new ArrayList<>();
    protected double temperatura;
    protected double percentual;
    protected String alerta;

    public void subscribe(Subscriber s){
        this.subscribers.add(s);
    }

    public void unsubscribe(Subscriber s){
        this.subscribers.remove(s);
    }

    protected void notifySubscribers() {
        for (Subscriber s : subscribers) {
            s.update(this);
        }
    }

    public abstract void monitorar(); // metodo abstrato para lógica de monitoramento específica
    public abstract String getLabelPercentual(); // metodo que muda de maquina para maquina

    // Getters para atributos
    public double getTemperatura() {
        return temperatura;
    }

    public double getPercentual() {
        return percentual;
    }

    public String getAlerta() {
        return alerta;
    }
}