
package estacionamiento.java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Estacionamiento extends JFrame {
    private static Map<String, RegistroEstacionamiento> registros = new HashMap<>();
    private static final double COSTO_POR_MINUTO = 35.0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estacionamiento estacionamiento = new Estacionamiento();
            estacionamiento.setTitle("Estacionamiento IPChile");
            estacionamiento.setSize(400, 200);
            estacionamiento.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            estacionamiento.placeComponents();
            estacionamiento.setVisible(true);
        });
    }

    private void placeComponents() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel patenteLabel = new JLabel("Patente:");
        patenteLabel.setBounds(10, 20, 80, 25);
        panel.add(patenteLabel);

        JTextField patenteText = new JTextField(20);
        patenteText.setBounds(100, 20, 165, 25);
        panel.add(patenteText);

        JButton entradaButton = new JButton("Registrar Entrada");
        entradaButton.setBounds(10, 50, 150, 25);
        panel.add(entradaButton);

        JButton salidaButton = new JButton("Registrar Salida");
        salidaButton.setBounds(180, 50, 150, 25);
        panel.add(salidaButton);
        
        JLabel costoLabel = new JLabel("El costo por minuto es de $35 pesos.");
        costoLabel.setBounds(10, 100, 250, 25);
        panel.add(costoLabel);
        
        
        
        entradaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patente = patenteText.getText();
                LocalTime horaEntrada = LocalTime.now();
                registros.put(patente, new RegistroEstacionamiento(horaEntrada, null));
                JOptionPane.showMessageDialog(null, "Entrada registrada con éxito.");
            }
        });

        salidaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patente = patenteText.getText();

                if (registros.containsKey(patente)) {
                    LocalTime horaSalida = LocalTime.now();
                    RegistroEstacionamiento registro = registros.get(patente);
                    registro.setHoraSalida(horaSalida);

                    long minutosEstacionados = registro.calcularMinutosEstacionados();
                    double costoTotal = calcularCosto(minutosEstacionados);

                    JOptionPane.showMessageDialog(null,
                            "Hora de entrada: " + registro.getHoraEntrada() +
                                    "\nHora de salida: " + registro.getHoraSalida() +
                                    "\nTiempo estacionado: " + minutosEstacionados + " minutos" +
                                    "\nCosto total: $" + costoTotal);

                    registros.remove(patente);
                } else {
                    JOptionPane.showMessageDialog(null, "La patente no se encuentra registrada.");
                }
            }
        });
    }

    private double calcularCosto(long minutosEstacionados) {
        return minutosEstacionados * COSTO_POR_MINUTO;
    }
}

class RegistroEstacionamiento {
    private LocalTime horaEntrada;
    private LocalTime horaSalida;

    public RegistroEstacionamiento(LocalTime horaEntrada, LocalTime horaSalida) {
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public long calcularMinutosEstacionados() {
        return horaEntrada.until(horaSalida, java.time.temporal.ChronoUnit.MINUTES);
    }
}
