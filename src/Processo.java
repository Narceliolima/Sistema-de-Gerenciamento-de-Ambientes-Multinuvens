
public class Processo extends ObjetoBase {

	private static final long serialVersionUID = 1L;
	private Espaco espaco;
	private Nuvem nuvem;
	private Host host;
	private Vm vm;
	private GUI gui;
	
	public Processo(String nome, Nuvem nuvem, Host host, Vm vm) {
		super(nome);
		this.nuvem = nuvem;
		this.host = host;
		this.vm = vm;
		iniciaEspaco();
	}
	
	public Processo(String nome, Nuvem nuvem, Host host, Vm vm,GUI gui) {
		super(nome);
		this.nuvem = nuvem;
		this.host = host;
		this.vm = vm;
		this.gui = gui;
		iniciaEspaco();
	}
	
	public void iniciaEspaco() {
		this.espaco = new Espaco(nuvem,host,vm,this);
		espaco.start();
	}
	
	public void enviarMensagem(String mensagem, String destino) {
		if(!destino.contentEquals(espaco.getReferencia())) {
			espaco.enviaMensagem(destino, nuvem+"/"+host+"/"+vm+"/"+this+": "+mensagem);
		}
		//gui.esreveChat("Mensagem enviada por "+nuvem+"/"+host+"/"+vm+"/"+this);
	}
	
	public void receberMensagem(String mensagem) {
		gui.esreveChat("Mensagem recebida por "+nuvem+"/"+host+"/"+vm+"/"+this);
		gui.esreveChat(mensagem);
	}
	
	public void finalizar() {
		espaco.finalizar();
	}
	
	public void reiniciar() {
		finalizar();
		iniciaEspaco();
	}

	public Nuvem getNuvem() {
		return nuvem;
	}

	public void setNuvem(Nuvem nuvem) {
		this.nuvem = nuvem;
	}

	public Host getHost() {
		return host;
	}

	public void setHost(Host host) {
		this.host = host;
	}

	public Vm getVm() {
		return vm;
	}

	public void setVm(Vm vm) {
		this.vm = vm;
	}
}
