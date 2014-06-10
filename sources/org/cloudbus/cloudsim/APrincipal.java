package org.cloudbus.cloudsim;

import javax.swing.*;

import org.cloudbus.cloudsim.brite.Brite;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Projeto REALCloudSim
 * 
 * Descricao: Simulador gráfico para políticas de
 * escalonamento em nuvens federadas utilizando
 * algoritmos genéticos.
 *
 * Autor: Lucio Agostinho Rocha
 * Ultima atualizacao: 18/11/2011
 *
 */

public class APrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private static String versao = "Novembro de 2011";

	private static String titulo = "REALCloudSim";

	private JTabbedPane tpResultado;

	private static JTextArea areaResultConfig;

	private static JScrollPane saidaComRolagem;

	private JButton botaoForward, botaoParar, botaoParaCima, botaoParaBaixo, botaoParaFrente, botaoParaTras, 
	botaoRotacionar, botaoGetPosition;
	private JTextField campoRotacionar, campoURL;

	private Container container;

	private String acaoAnterior="";
	private double anguloAnterior=0;

	public APrincipal(){

		super();

		criarBarraMenu();

		criarBarraFerramentas();

		//criarFrameInterno();

		//Nao finaliza a aplicacao a menos que a opcao 'Yes' seja escolhida
		/*setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		//Comportamento padrao ao fechar a janela
		addWindowListener(new WindowAdapter() {    

			public void windowClosing(WindowEvent e) {

				//Finaliza a aplicacao
				finalizar();

			}
		});		
		 */
	}//fim construtor	

	public void criarBarraMenu(){

		container = getContentPane();	    
		container.setLayout(new BorderLayout());

		JMenuBar menuBar = new JMenuBar();		

		//Menu Topology
		JMenu menuTopology = new JMenu("Topology");
		menuTopology.setMnemonic('T');
		menuBar.add(menuTopology);			

		//Arquivo -> Gerar Topologia
		JMenuItem itemTopologia = new JMenuItem("Generate Topology");
		itemTopologia.setMnemonic('t');
		menuTopology.add(itemTopologia);
		
/*		JMenu menuSimulation = new JMenu("Simulation");
		menuSimulation.setMnemonic('S');
		menuBar.add(menuSimulation);		

		//Arquivo -> Start Simulation
		JMenuItem itemStart = new JMenuItem("Start Simulation");
		itemStart.setMnemonic('s');
		menuSimulation.add(itemStart);
*/

/*		//Arquivo -> Caminho suave aleatorio...
		JMenuItem itemMenuCaminho = new JMenuItem("Caminho suave aleatorio...");
		itemMenuCaminho.setMnemonic('c');
		menuArquivo.add(itemMenuCaminho);		
*/
		menuTopology.addSeparator();

		//Arquivo -> Exit
		JMenuItem itemMenuSair = new JMenuItem("Exit");
		itemMenuSair.setMnemonic('E');
		menuTopology.add(itemMenuSair);

		// Menu Ajuda        
		JMenu menuAjuda = new JMenu("Ajuda");
		menuAjuda.setMnemonic('u');
		menuBar.add(menuAjuda);

		//Ajuda -> Sobre...
		JMenuItem itemMenuSobre = new JMenuItem("Sobre...");
		itemMenuSobre.setMnemonic('S');        
		menuAjuda.add(itemMenuSobre);

		//Insere a barra de Menu
		setJMenuBar(menuBar);

		itemTopologia.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				Brite g= new Brite();
				g.init();
				g.setVisible(true);
			}
		});

/*		itemExibirTopologia.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				//reiniciarPosicao();
			}
		});
*/		

/*		itemMenuCaminho.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				//caminhoSuaveAleatorio();
			}
		});		
*/
		itemMenuSair.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				finalizar();
			}
		});
		
/*		itemStart.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				startSimulation();
			}
		});
*/
		itemMenuSobre.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent e ){
				JOptionPane.showMessageDialog(null,						
						"Autor: Lucio Agostinho Rocha\n\n" +
						"Versao: "+versao+"\n\n",
						titulo, 
						JOptionPane.QUESTION_MESSAGE);	
			}
		});

	}//fim criarBarraMenu			

	public void startSimulation(){
		
		
		
	}//fim startSimulation	
	
	public void criarFrameInterno() {        	    

		//Area de texto com o resultado da configuracao        
		areaResultConfig = new JTextArea();
		saidaComRolagem = new JScrollPane(areaResultConfig);
		//TabbedPane para manter o resultado da configuracao
		tpResultado = new JTabbedPane();
		tpResultado.add("Resultados", saidaComRolagem);

		container.add(tpResultado);

	}//fim criarFrameInterno	

	/**
	 * Barra de ferramentas
	 * 
	 * @return JToolBar
	 */
	public void criarBarraFerramentas(){

		JToolBar barraFerramentas = new JToolBar();
		barraFerramentas.setFloatable(true);        		

		botaoForward = new JButton(">>");
		botaoForward.setToolTipText(">>");
		botaoForward.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//forward();

			}//fim actionPerformed

		});		

		botaoParar = new JButton("Parar");
		botaoParar.setToolTipText("Parar");	
		//Atribui comportamento		
		botaoParar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//parar();

			}//fim actionPerformed

		});		

		botaoParaCima = new JButton("/\\");
		botaoParaCima.setToolTipText("Mover para cima");	
		//Atribui comportamento		
		botaoParaCima.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//moverParaCima();

			}//fim actionPerformed

		});

		botaoParaBaixo = new JButton("\\/");
		botaoParaBaixo.setToolTipText("Mover para baixo");	
		//Atribui comportamento		
		botaoParaBaixo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//moverParaBaixo();

			}//fim actionPerformed

		});		

		botaoParaFrente = new JButton(">");
		botaoParaFrente.setToolTipText("Mover para frente");	
		//Atribui comportamento		
		botaoParaFrente.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//moverParaFrente();

			}//fim actionPerformed

		});		

		botaoParaTras = new JButton("<");
		botaoParaTras.setToolTipText("Mover para tras");	
		//Atribui comportamento		
		botaoParaTras.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//moverParaTras();

			}//fim actionPerformed

		});

		botaoRotacionar = new JButton("Rotacionar");
		botaoRotacionar.setToolTipText("Rotacionar");	
		//Atribui comportamento		
		botaoRotacionar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//rotacionar();

			}//fim actionPerformed

		});

		campoRotacionar = new JTextField(10);

		botaoGetPosition = new JButton("GetPosition");
		botaoGetPosition.setToolTipText("GetPosition");	
		//Atribui comportamento		
		botaoGetPosition.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

				//getPosition();

			}//fim actionPerformed

		});

		JLabel rotuloURL = new JLabel(" URL:");
		campoURL = new JTextField("http://localhost:4950");

		barraFerramentas.add(botaoForward);
		barraFerramentas.add(botaoParar);
		barraFerramentas.add(botaoParaCima);
		barraFerramentas.add(botaoParaBaixo);
		barraFerramentas.add(botaoParaFrente);
		barraFerramentas.add(botaoParaTras);
		barraFerramentas.add(botaoRotacionar);
		barraFerramentas.add(campoRotacionar);
		barraFerramentas.add(botaoGetPosition);
		barraFerramentas.add(rotuloURL);
		barraFerramentas.add(campoURL);

		//container.add(barraFerramentas, BorderLayout.NORTH);

	}//fim criarBarraFerramentas

	public void finalizar(){

		int opcao = JOptionPane.showConfirmDialog(
				this, 
				"Finalizar o programa?",
				titulo, JOptionPane.YES_NO_OPTION);

		if (opcao == JOptionPane.YES_OPTION)
			System.exit(0);

	}//fim finalizar

	public static void exibirLog(String mensagem){

		JScrollBar vertical = saidaComRolagem.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());

		areaResultConfig.append("\n------------------------------------------");
		areaResultConfig.append("\n" + mensagem);
		areaResultConfig.append("\n------------------------------------------");

		//Ajusta a exibicao da barra de rolagem
		vertical.setValue(vertical.getMaximum());


	}//fim exibirLog		

	public static void main(final String[] args) {

		Thread.UncaughtExceptionHandler handler =
			new StackWindow("Show Exception Stack", 600, 200);
		Thread.setDefaultUncaughtExceptionHandler(handler);
		new Thread() {
			public void run() {		

				APrincipal aplicacao = new APrincipal();
				aplicacao.setTitle(titulo + " - " + versao);
				aplicacao.setSize(400,200);				
				aplicacao.setVisible(true);

			}//fim else

		}.start();

	}//fim main
}
