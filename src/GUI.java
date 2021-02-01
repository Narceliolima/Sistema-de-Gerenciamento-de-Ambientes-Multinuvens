import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

public class GUI {

	//---------------------------------------------/Variaveis/----------------------------------------------//
	private JFrame frame;
	private GUI window;
	private ArrayList<Nuvem> listaNuvem = new ArrayList<Nuvem>();
	private NuvemModel raiz;
	private JTree tree;
	private ActionListener ato;
	private int index = 0;
	private JDialog jdialog ;
	private String dialogoResposta;
	private String mensagemProcesso;
	private JScrollPane scrollTree;
	private JScrollPane scrollChat;
	private JTextArea painelChat;
	//--------------------------------------------/-------------/--------------------------------------------//
	
	//--------------------------------------------/Labels/Botoes/--------------------------------------------//
	private JButton adicionar;
	private JButton remover;
	private JButton enviarMsg;
	private JButton teste;
	//--------------------------------------------/-------------/--------------------------------------------//
	
	//-------------------------------------------/ComboBox/Botoes/-------------------------------------------//
	private JComboBox<String> nuvemBox;
	private JComboBox<String> hostBox;
	private JComboBox<String> vmBox;
	private JComboBox<String> processoBox;
	//--------------------------------------------/-------------/--------------------------------------------//
	
	public static void main(String[] args) {
		new GUI();
	}

	public GUI() {
		window = this;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		iniciaBotoes();
		iniciaArvore();
		createRunnable();
	}
	
	public void atualizaInterface() {
		//teste.setText(""+listaNuvem.size());
		//teste.setText("Teste"+index);
	}
	
	public void esreveChat(String mensagem) {
		painelChat.append(mensagem+"\n");
		painelChat.setCaretPosition(painelChat.getText().length());
	}
	
	private void varreBotao() {
		
		ato = new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				int selected = tree.getLeadSelectionRow();
				String entradaDialogo = ""+tree.getLastSelectedPathComponent();
				if(arg0.getSource() == adicionar) {
					if(tree.getSelectionPath().getPathCount()==1) {
						listaNuvem.add(new Nuvem("nuvem"+index));
						index++;
					}
					else if(tree.getSelectionPath().getPathCount()==2) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						listaNuvem.get(indexNuvem).criaHost();
					}
					else if(tree.getSelectionPath().getPathCount()==3) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
						listaNuvem.get(indexNuvem).getListaHost().get(indexHost).criaVm();
					}
					else if(tree.getSelectionPath().getPathCount()==4) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
						int indexVm = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(2), tree.getSelectionPath().getPathComponent(3));
						listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).criaProcesso(window);
					}
					else if(tree.getSelectionPath().getPathCount()==5) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
						int indexVm = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(2), tree.getSelectionPath().getPathComponent(3));
						int indexProcesso = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(3), tree.getSelectionPath().getPathComponent(4));
						dialogoResposta = "";
				        criaDialogo(indexNuvem,indexHost,indexVm);
				        System.out.println(dialogoResposta);
						listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).getListaProcesso().get(indexProcesso).enviarMensagem(mensagemProcesso, dialogoResposta);
					}
					atualizaArvore();
				}
				else if(arg0.getSource() == remover) {
					if(tree.getSelectionPath().getPathCount()==2) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						if(listaNuvem.get(indexNuvem).getListaHost().isEmpty()) {
							listaNuvem.get(indexNuvem).finalizar();
							listaNuvem.remove(indexNuvem);
						}
						else {
							System.out.println("Num dá man, n insiste");
							Notificacao.deletarNaoVazio();
						}
					}
					else if(tree.getSelectionPath().getPathCount()==3) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
						listaNuvem.get(indexNuvem).removeHost(indexHost);
					}
					else if(tree.getSelectionPath().getPathCount()==4) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
						int indexVm = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(2), tree.getSelectionPath().getPathComponent(3));
						listaNuvem.get(indexNuvem).getListaHost().get(indexHost).removeVm(indexVm);
					}
					else if(tree.getSelectionPath().getPathCount()==5) {
						int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
						int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
						int indexVm = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(2), tree.getSelectionPath().getPathComponent(3));
						int indexProcesso = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(3), tree.getSelectionPath().getPathComponent(4));
						listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).getListaProcesso().remove(indexProcesso);
					}
					atualizaArvore();
				}
				else if(arg0.getSource() == enviarMsg) {
					if(entradaDialogo.contains("Universo")) {
						System.out.println("Não toque no universo");
						Notificacao.moverUniverso();
					}
					else if(entradaDialogo.contains("nuvem")) {
						System.out.println("Não pode enviar uma nuvem como dado");
						Notificacao.moverNuvem();
					}
					else {
						dialogoResposta = "";
				        criaDialogo(0);
				        System.out.println(entradaDialogo);
				        System.out.println(dialogoResposta);
				        if(dialogoResposta.contains("vm")) {
							int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
							int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
							int indexVm = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(2), tree.getSelectionPath().getPathComponent(3));
							System.out.println(listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).getListaProcesso());
							listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).moverProcesso(entradaDialogo, dialogoResposta);
				        }
				        else if(dialogoResposta.contains("host")) {
							int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
							int indexHost = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(1), tree.getSelectionPath().getPathComponent(2));
							System.out.println(listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm());
							listaNuvem.get(indexNuvem).getListaHost().get(indexHost).moverVm(entradaDialogo, dialogoResposta);
				        }
				        else if(dialogoResposta.contains("nuvem")) {
							int indexNuvem = raiz.getIndexOfChild(tree.getSelectionPath().getPathComponent(0), tree.getSelectionPath().getPathComponent(1));
							System.out.println(listaNuvem.get(indexNuvem).getListaHost());
							listaNuvem.get(indexNuvem).moverHost(entradaDialogo, dialogoResposta);
				        }
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					atualizaArvore();
				}
				else if(arg0.getSource() == teste) {
					
				}
				else if(arg0.getSource() == nuvemBox) {
					if(!entradaDialogo.contains("host")) {
						criaDialogo(nuvemBox.getSelectedIndex());
					}
					else {
						jdialog.dispose();
					}
				}
				else if(arg0.getSource() == hostBox) {
					if(!entradaDialogo.contains("vm")) {
						criaDialogo(hostBox.getSelectedIndex());
					}
					else {
						jdialog.dispose();
					}
				}
				else if(arg0.getSource() == vmBox) {
					if(!entradaDialogo.contains("processo")) {
						criaDialogo(vmBox.getSelectedIndex());
					}
					else {
						jdialog.dispose();
					}
				}
				atualizaInterface();
				tree.setSelectionRow(selected);
			}
		};
		
		adicionar.addActionListener(ato);
		remover.addActionListener(ato);
		enviarMsg.addActionListener(ato);
		//teste.addActionListener(ato);
	}
	
	private void criaDialogo(int index) {
		
		if(jdialog == null) {
			jdialog = new JDialog(frame, true);
	        jdialog.setSize(200, 200);
	        jdialog.getContentPane().setLayout(new FlowLayout());
	        jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	        
	        nuvemBox = new JComboBox<String>();
			for(int i=0;i<listaNuvem.size();i++) {
				nuvemBox.addItem(listaNuvem.get(i).getNome());
			}
			nuvemBox.addActionListener(ato);
			jdialog.getContentPane().add(nuvemBox);
			jdialog.setTitle("Escolha o destino");
			jdialog.setVisible(true);
			dialogoResposta += nuvemBox.getSelectedItem();
			if(hostBox!=null) {
				dialogoResposta += "/"+hostBox.getSelectedItem();
				if(vmBox!=null) {
					dialogoResposta += "/"+vmBox.getSelectedItem();
				}
			}
			jdialog = null;
			hostBox = null;
			vmBox = null;
		}
		else if(hostBox == null) {
			hostBox = new JComboBox<String>();
			for(int i=0;i<listaNuvem.get(index).getListaHost().size();i++) {
				hostBox.addItem(listaNuvem.get(index).getListaHost().get(i).getNome());
			}
			hostBox.addActionListener(ato);
			jdialog.getContentPane().add(hostBox);
			jdialog.setVisible(true);
		}
		else if(vmBox == null) {
			vmBox = new JComboBox<String>();
			for(int i=0;i<listaNuvem.get(nuvemBox.getSelectedIndex()).getListaHost().get(index).getListaVm().size();i++) {
				vmBox.addItem(listaNuvem.get(nuvemBox.getSelectedIndex()).getListaHost().get(index).getListaVm().get(i).getNome());
			}
			vmBox.addActionListener(ato);
			jdialog.getContentPane().add(vmBox);
			jdialog.setVisible(true);
		}
	}
	
	private void criaDialogo(int indexNuvem,int indexHost, int indexVm) {
		
		jdialog = new JDialog(frame, true);
        jdialog.setSize(280, 150);
        jdialog.getContentPane().setLayout(null);
        jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		processoBox = new JComboBox<String>();
		processoBox.setBounds(73, 20, 124, 24);
		for(int i=0;i<listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).getListaProcesso().size();i++) {
			processoBox.addItem(listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).getListaProcesso().get(i).getNome());
		}
		processoBox.addActionListener(ato);
		jdialog.getContentPane().add(processoBox);
		JTextField chat = new JTextField();
		chat.setBounds(12, 65, 257, 50);
		jdialog.getContentPane().add(chat);
		chat.setColumns(10);
		chat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mensagemProcesso = arg0.getActionCommand();
				chat.setText("");
				jdialog.dispose();
			}
		});
		jdialog.setTitle("Escrever mensagem");
		jdialog.setVisible(true);
		jdialog = null;
		dialogoResposta = listaNuvem.get(indexNuvem).getNome();
		dialogoResposta += "/"+listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getNome();
		dialogoResposta += "/"+listaNuvem.get(indexNuvem).getListaHost().get(indexHost).getListaVm().get(indexVm).getNome();
		dialogoResposta += "/"+processoBox.getSelectedItem();
	}
	
	private void iniciaArvore() {
		raiz = new NuvemModel(listaNuvem);
		tree = new JTree(raiz);
		scrollTree = new JScrollPane(tree);
		tree.setSelectionRow(0);
		frame.getContentPane().add(scrollTree);
	}
	
	private void atualizaArvore() {
		tree.setModel(new NuvemModel(listaNuvem));
		for (int i = 0; i < tree.getRowCount(); i++) {
		    tree.expandRow(i);
		}
	}
	
	private void iniciaBotoes() {
		adicionar = new JButton("Add/Enviar Msg");
		frame.getContentPane().add(adicionar, BorderLayout.WEST);
		
		remover = new JButton("Remover");
		frame.getContentPane().add(remover, BorderLayout.SOUTH);
		
		enviarMsg = new JButton("Mover Objeto");
		frame.getContentPane().add(enviarMsg, BorderLayout.NORTH);
		
		painelChat = new JTextArea();
		painelChat.setEditable(false);
		painelChat.setText("Mensagens entre processos aparecerão aqui  \n");
		scrollChat = new JScrollPane(painelChat);
		frame.getContentPane().add(scrollChat, BorderLayout.EAST);
		
		//teste = new JButton("Teste");
		//frame.getContentPane().add(teste, BorderLayout.SOUTH);
	}
	
	private void createRunnable() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
					window.varreBotao();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
