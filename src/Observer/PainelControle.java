package Observer;

import Factory.Caldeira;
import Factory.Resfriador;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PainelControle extends JFrame implements Subscriber {
    private DefaultListModel<String> alertListModel;
    private JPanel machinePanel;
    private ArrayList<Maquina> maquinas = new ArrayList<>();


    public PainelControle() {
        setTitle("Painel de Controle de Máquinas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Painel de máquinas com GridLayout de duas colunas e múltiplas linhas
        machinePanel = new JPanel(new GridLayout(0, 3, 10, 10));
        machinePanel.setBorder(BorderFactory.createTitledBorder("Máquinas"));
        JScrollPane machineScrollPane = new JScrollPane(machinePanel);
        machineScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Lista de alertas
        alertListModel = new DefaultListModel<>();
        JList<String> alertList = new JList<>(alertListModel);
        JScrollPane alertScrollPane = new JScrollPane(alertList);
        alertScrollPane.setPreferredSize(new Dimension(300, 0));
        alertScrollPane.setBorder(BorderFactory.createTitledBorder("Alertas"));

        // Painel de controle para adicionar máquinas
        JPanel controlPanel = new JPanel();
        String[] machineTypes = {"Caldeira", "Resfriador"};
        JComboBox<String> machineTypeCombo = new JComboBox<>(machineTypes);
        JButton addButton = new JButton("Adicionar Máquina");

        addButton.addActionListener(e -> addMachine((String) machineTypeCombo.getSelectedItem()));

        controlPanel.add(new JLabel("Tipo de Máquina:"));
        controlPanel.add(machineTypeCombo);
        controlPanel.add(addButton);

        // Organização dos componentes no painel principal
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(machineScrollPane, BorderLayout.CENTER);
        mainPanel.add(alertScrollPane, BorderLayout.EAST);
        add(mainPanel);
    }


    // metodo utilitário para criar ou clonar uma máquina
    private Maquina createOrCloneMachine(String type) {
        // Procura uma máquina do tipo solicitado para clonagem
        for (Maquina m : maquinas) {
            if ((type.equals("Caldeira") && m instanceof Caldeira) || (type.equals("Resfriador") && m instanceof Resfriador)) {
                try {
                    return ((Prototype.CloneableMaquina) m).clone();
                } catch (CloneNotSupportedException e) {
                    System.out.println("Máquina: " + e.getMessage() + " foi clonada!");
                    break;
                }
            }
        }

        // Cria uma nova instância caso não encontre uma máquina para clonar
        if (type.equals("Caldeira")) {
            return new Caldeira();
        } else if (type.equals("Resfriador")) {
            return new Resfriador();
        }

        return null; // Caso o tipo não seja encontrado
    }

    private void addMachine(String type) {
        Maquina maquina = createOrCloneMachine(type);
        if (maquina == null) {
            JOptionPane.showMessageDialog(this, "Tipo de máquina inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        maquina.subscribe(this);
        maquinas.add(maquina);

        // Painel da nova máquina clonada
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), type, TitledBorder.CENTER, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel tempLabel = new JLabel("Temperatura: 0.0");
        JLabel percentLabel = new JLabel(maquina.getLabelPercentual() + ": 0.0");
        JButton startButton = new JButton("Ligar Máquina");
        JButton removeButton = new JButton("Remover Máquina");

        final Timer[] timer = {null};

        Maquina finalMaquina = maquina;
        startButton.addActionListener(new ActionListener() {
            private boolean isRunning = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    isRunning = true;
                    startButton.setText("Desligar Máquina");

                    timer[0] = new Timer();
                    timer[0].schedule(new TimerTask() {
                        @Override
                        public void run() {
                            finalMaquina.monitorar();
                            tempLabel.setText("Temperatura: " + finalMaquina.getTemperatura());
                            percentLabel.setText(finalMaquina.getLabelPercentual() + ": " + finalMaquina.getPercentual());
                        }
                    }, 0, 3000);

                } else {
                    isRunning = false;
                    startButton.setText("Ligar Máquina");

                    if (timer[0] != null) {
                        timer[0].cancel();
                        timer[0] = null;
                    }

                    tempLabel.setText("Temperatura: 0.0");
                    percentLabel.setText(finalMaquina.getLabelPercentual() + ": 0.0");
                }
            }
        });

        removeButton.addActionListener(e -> {
            // Para o monitoramento e remove a máquina do sistema
            if (timer[0] != null) {
                timer[0].cancel();
            }
            finalMaquina.unsubscribe(this); // Remove do Observer
            maquinas.remove(finalMaquina);  // Remove da lista de máquinas
            machinePanel.remove(panel);     // Remove o painel da máquina

            // Atualiza o painel de máquinas na interface
            machinePanel.revalidate();
            machinePanel.repaint();
        });

        gbc.gridy = 0;
        panel.add(new JLabel(type), gbc);

        gbc.gridy++;
        panel.add(tempLabel, gbc);

        gbc.gridy++;
        panel.add(percentLabel, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(startButton, gbc);

        gbc.gridy++;
        panel.add(removeButton, gbc);

        machinePanel.add(panel);
        machinePanel.revalidate();
        machinePanel.repaint();
    }



    @Override
    public String update(Maquina maquina) {
        alertListModel.addElement(maquina.getAlerta());
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PainelControle painel = new PainelControle();
            painel.setVisible(true);
        });
    }
}