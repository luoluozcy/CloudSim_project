package org.cloudbus.cloudsim.brite;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.URL;

public class FasesDaLua implements ActionListener {
	final static int NUM_IMAGES = 8;
	final static int START_INDEX = 3;

	ImageIcon[] images = new ImageIcon[NUM_IMAGES];
	JPanel mainPanel, selectPanel, displayPanel;

	JComboBox phaseChoices = null;
	JLabel phaseIconLabel = null;

	public FasesDaLua() {
		//Cria a selecao das faaes e exibe no painel.
		selectPanel = new JPanel();
		displayPanel = new JPanel();

		//Adiciona varios widgets para o sub-painel.
		addWidgets();

		//Cria um painel contendo dois sub-paineis.
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		//Adiciona e seleciona painel de exibicao para o painel principal.
		mainPanel.add(selectPanel);
		mainPanel.add(displayPanel);
	}

	/*
	 * Obtem a imagem e organiza o widgets.
	 */
	private void addWidgets() {
		//Obtem a imagem e coloca no array de ImageIcons.
		for (int i = 0; i < NUM_IMAGES; i++) {
			images[i] = createImageIcon("image" + i + ".jpg");
		}

		/*
		 * Cria um rotulo para exibir as imagens das fases da lua e
		 * coloca uma bordar ao redor desta.
		 */
		phaseIconLabel = new JLabel();
		phaseIconLabel.setHorizontalAlignment(JLabel.CENTER);
		phaseIconLabel.setVerticalAlignment(JLabel.CENTER);
		phaseIconLabel.setVerticalTextPosition(JLabel.CENTER);
		phaseIconLabel.setHorizontalTextPosition(JLabel.CENTER);
		phaseIconLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(),
				BorderFactory.createEmptyBorder(5,5,5,5)));

		phaseIconLabel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0,0,10,0),
				phaseIconLabel.getBorder()));

		//Cria um combo box com cada selecao da fase lunar.
		String[] phases = { "Nova", "Maximo Crescente", "Primeiro Quarto", 
				"Waxing Gibbous", "Cheia", "Waning Gibbous", 
				"Terceiro Quarto", "Crescente" };
		phaseChoices = new JComboBox(phases);
		phaseChoices.setSelectedIndex(START_INDEX);

		//Exibe a primeira imagem.
		phaseIconLabel.setIcon(images[START_INDEX]);
		phaseIconLabel.setText("");

		//Adiciona uma borda em volta do painel de selecao.
		selectPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Selecione uma Fase"), 
				BorderFactory.createEmptyBorder(5,5,5,5)));

		//Adiciona borda em vola do painel de exibicao.
		displayPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Exibe Fases"), 
				BorderFactory.createEmptyBorder(5,5,5,5)));

		//Adiciona fases da lua no combo box para selecionar imagem no painel e rotulo da imagem.
		displayPanel.add(phaseIconLabel);
		selectPanel.add(phaseChoices);

		//Listen para o events do combo box.
		phaseChoices.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent event) {
		if ("comboBoxChanged".equals(event.getActionCommand())) {
			//Update the icon to display the new phase.
			phaseIconLabel.setIcon(images[phaseChoices.getSelectedIndex()]);
		}
	}

	/** Retorna um ImageIcon, ou null se o path for invalido. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imageURL = FasesDaLua.class.getResource(path);

		if (imageURL == null) {
			System.err.println("Caminho nao encontrado: "
					+ path);
			return null;
		} else {
			return new ImageIcon(imageURL);
		}
	}

	/**
	 * Cria um GUI e o exibe. Para thread safety,
	 * este metodo podera invocar para uma thread 
	 * de disparo de evento(event-dispatching thread).
	 */
	private static void criaExibeGUI() {
		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Cria uma nova instancia de FasesDaLua.
		FasesDaLua phases = new FasesDaLua();

		//Cria e organiza a janela.
		JFrame lunarPhasesFrame = new JFrame("Fases da Lua");
		lunarPhasesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		lunarPhasesFrame.setContentPane(phases.mainPanel);

		//Exibe janela.
		lunarPhasesFrame.pack();
		lunarPhasesFrame.setVisible(true);
	}
	public static void main(String[] args) {
		//Agenda um trabalho para o event-dispatching thread:
		//cria e exibe uma aplicacao GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				criaExibeGUI();
			}
		});
	}
}