import Factory.Caldeira;
import Factory.MaquinaFactory;
import Factory.Resfriador;
import Observer.Maquina;
import Observer.PainelControle;

public class Main {
    public static void main(String[] args) {
        // Exemplo com a fábrica de caldeira
        MaquinaFactory caldeiraFactory = new Caldeira();
        Maquina caldeira = caldeiraFactory.createMaquina();

        // Exemplo com a fábrica de refrigerador
        MaquinaFactory refrigeradorFactory = new Resfriador();
        Maquina refrigerador = refrigeradorFactory.createMaquina();

        // Inscrevendo PainelControle para receber notificações
        PainelControle painelControle = new PainelControle();
        caldeira.subscribe(painelControle);
        refrigerador.subscribe(painelControle);

        // Monitorando as máquinas
        caldeira.monitorar();
        refrigerador.monitorar();
    }
}
