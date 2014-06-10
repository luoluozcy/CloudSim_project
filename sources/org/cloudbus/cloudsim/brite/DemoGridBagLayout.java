package org.cloudbus.cloudsim.brite;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DemoGridBagLayout {
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	public static void addComponentsToPane(Container pane) {
		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			//altura natural, largura maxima.
			c.fill = GridBagConstraints.HORIZONTAL;
		}		
		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		
	    JLabel label1 = new JLabel("Numero de Servidores: ");
	    JTextField campo1 = new JTextField(20);
	    campo1.setText("2");
	    label1.setLabelFor(campo1);
	    c.gridx = 0;
		c.gridy = 0;
	    pane.add(label1, c);
	    c.gridx = 1;
		c.gridy = 0;
	    pane.add(campo1, c);
	    
	    JLabel label1_1 = new JLabel("Numero de CPUs: ");
	    JTextField campo1_1 = new JTextField(20);
	    campo1_1.setText("2");
	    label1_1.setLabelFor(campo1_1);
	    c.gridx = 0;
		c.gridy = 0;
	    pane.add(label1_1, c);
	    c.gridx = 1;
		c.gridy = 0;
	    pane.add(campo1_1, c);
	    
	    JLabel label2 = new JLabel("Numero de VMs: ");
	    JTextField campo2 = new JTextField(20);
	    campo2.setText("3");
	    label2.setLabelFor(campo2);
	    c.gridx = 0;
		c.gridy = 1;
	    pane.add(label2, c);
	    c.gridx = 1;
		c.gridy = 1;
	    pane.add(campo2, c);
	    
	    JLabel label3 = new JLabel("Arquivo Topologia: ");
	    JTextField campo3 = new JTextField(30);
	    campo3.setText("/home/lucio/modeloRealCloudSimLingo.lg4");
	    label2.setLabelFor(campo3);
	    c.gridx = 0;
		c.gridy = 2;
	    pane.add(label3, c);
	    c.gridx = 1;
		c.gridy = 2;
	    pane.add(campo3, c);
	    	    		
		JButton button = new JButton("Gerar modelo Lingo");
		c.gridx = 0;
		c.gridy = 3;
		pane.add(button, c);

		/*button = new JButton("5");
		c.ipady = 0; //esvazia por padrao
		c.weighty = 1.0; //requisita um espaco vertical extra.
		c.anchor = GridBagConstraints.PAGE_END; //espaco em baixo.
		c.insets = new Insets(10,0,0,0); //top padding
		c.gridx = 1; //alinhamento do botao 2.
		c.gridwidth = 2; //2 colunas largas.
		c.gridy = 2; //terceira linha.
		pane.add(button, c);
*/		
	}

	/**
	 * Cria um GUI e o exibe. Para thread safety,
	 * este metodo podera invocar para uma thread 
	 * de disparo de evento(event-dispatching thread).
	 */
	private static void createAndShowGUI() {
		//Faz com que tenha uma decoracao de janela.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Cria e organiza a janela.
		JFrame frame = new JFrame("DemoGridBagLayout");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Organiza o conteudo no painel.
		addComponentsToPane(frame.getContentPane());

		//Exibe janela.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Agenda um trabalho para o event-dispatching thread:
		//cria e exibe uma aplicacao GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}