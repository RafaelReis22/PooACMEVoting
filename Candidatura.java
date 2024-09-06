import java.util.ArrayList;


public class Candidatura {

	private ArrayList<Candidato> candidato;

	public Candidatura() {
		candidato = new ArrayList<>();
	}

	public ArrayList<Candidato> getCandidato() {
		return candidato;
	}

	public boolean cadastraCandidato(Candidato c) {
		for (Candidato candidato : candidato) {
			if (candidato.getNumero() == c.getNumero() && candidato.getMunicipio().equals(c.getMunicipio())) {
				return false;
			}
		}
		candidato.add(c);
		return true;
	}

	public Candidato consultaCandidato(int numero, String municipio) {
		for (Candidato candidato : candidato) {
			if (candidato.getNumero() == numero && candidato.getMunicipio().equals(municipio)) {
				return candidato;
			}
		}
		return null;
	}

	public Candidato getPrefeitoMaisVotado() {
		Candidato maisVotado = null;
		int maxVotos = 0;
		for (Candidato c : candidato) {
			if (c.getVotos() > maxVotos) {
				maxVotos = c.getVotos();
				maisVotado = c;
			}
		}
		return maisVotado;
	}

	public Candidato getVereadorMaisVotado() {

		return getPrefeitoMaisVotado(); // Placeholder
	}
}




