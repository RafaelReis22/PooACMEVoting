import java.util.ArrayList;

public class Partido {
	private int numero;
	private String nome;
	private ArrayList<Candidato> candidato;

	public Partido(int numero, String nome) {
		this.numero = numero;
		this.nome = nome;
		this.candidato = new ArrayList<>();
	}

	public int getNumero() {
		return numero;
	}

	public String getNome() {
		return nome;
	}

	public ArrayList<Candidato> getCandidatos() {
		return candidato;
	}

	public void adicionaCandidato(Candidato c) {
		candidato.add(c);
	}

	public String toString() {
		return numero + "," + nome + " (Candidatos: " + candidato.size() + ")";
	}
}
