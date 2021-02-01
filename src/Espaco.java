import java.io.Serializable;
import java.rmi.RemoteException;

import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

public class Espaco extends Thread implements Serializable {
	
	private static final long serialVersionUID = 1L;
	//--------------------------------------------/Classes do programa/--------------------------------------------//
	private JavaSpace space;
	private Nuvem nuvem = null;
	private Host host = null;
	private Vm vm = null;
	private Processo processo = null;
	//--------------------------------------------/-------------/--------------------------------------------//
	
	//--------------------------------------------/Flag/--------------------------------------------//
	private boolean emExecusao = true;
	//--------------------------------------------/-------------/--------------------------------------------//
	
	
	public Espaco() {
		conecta();
	}
	
	public Espaco(Nuvem nuvem) {
		this();
		this.nuvem = nuvem;
		System.out.println("Espaco criado: "+this.nuvem.getNome());
	}
	
	public Espaco(Nuvem nuvem, Host host) {
		this(nuvem);
		this.host = host;
		System.out.println("Espaco criado: "+this.host.getNome());
	}
	
	public Espaco(Nuvem nuvem, Host host, Vm vm) {
		this(nuvem,host);
		this.vm = vm;
		System.out.println("Espaco criado: "+this.vm.getNome());
	}
	
	public Espaco(Nuvem nuvem, Host host, Vm vm, Processo processo) {
		this(nuvem,host,vm);
		this.processo = processo;
		System.out.println("Espaco criado: "+this.processo.getNome());
	}
	
	public boolean conecta() {
		System.out.println("Procurando pelo servico JavaSpace...");
		Lookup finder = new Lookup(JavaSpace.class);
        space = (JavaSpace) finder.getService();
        if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado.");
                return false;
                //System.exit(-1);
        } 
        System.out.println("O servico JavaSpace foi encontrado.");
        System.out.println(space);
        return true;
	}
	
	/**
	 * Função para envio de mensagens ao espaço de tuplas.
	 */
	public void enviaMensagem(String destino, Object mensagem) {
		try {
			System.out.println("Destino: "+destino);
			space.write(new Mensagem(destino,mensagem), null, 60 * 10000);
		} catch (RemoteException | TransactionException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Função para pegar a assinatura que irá compor o modelo de padrão da mensagem
	 */
	public String getReferencia() {
		
		String referencia = nuvem.getNome();
		
		if(host!=null) {
			referencia += "/"+host.getNome();
			if(vm!=null) {
				referencia += "/"+vm.getNome();
				if(processo!=null) {
					referencia += "/"+processo.getNome();
				}
			}
		}
		
		return referencia;
	}
	
	/**
	 * Função para pegar a assinatura que irá compor o modelo de padrão da mensagem
	 */
	public void adicionaObjeto(Object mensagem) {

		if(processo!=null) {
			processo.receberMensagem((String)mensagem);
		}
		else if(vm!=null) {
			vm.adicionaProcesso((Processo)mensagem);
		}
		else if(host!=null) {
			host.adicionaVm((Vm)mensagem);
		}
		else if(nuvem!=null) {
			nuvem.adicionaHost((Host)mensagem);
		}
		else {
			//Hoje não
		}
	}
	
	public void finalizar() {
		emExecusao = false;
	}
	
	@Override
	public void run() {
		super.run();
		while(emExecusao) {
			Mensagem msg;
			try {
				msg = (Mensagem) space.take(new Mensagem(getReferencia()), null, 10 * 1000);
				if(msg != null) {
					System.out.println("Mensagem recebida por "+getReferencia());
					adicionaObjeto(msg.mensagem);
				}
			} catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
