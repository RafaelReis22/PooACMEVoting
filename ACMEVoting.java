import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ACMEVoting {

	private Scanner entrada;
	private final String nomeArquivoEntrada = "input.txt";
	private PrintStream saidaPadrao;
	private final String nomeArquivoSaida = "output.txt";

	private CadastroPartido cadastroPartido = new CadastroPartido();
	private List<Partido> partidoList = new ArrayList<>();
	private Candidatura candidatura = new Candidatura();
	private List<Candidato> candidatoList = new ArrayList<>();

	public ACMEVoting() {
		redirecionaEntrada();
		redirecionaSaida();
	}

	private void redirecionaEntrada() {
		try {
			BufferedReader streamEntrada = new BufferedReader(new FileReader(nomeArquivoEntrada));
			entrada = new Scanner(streamEntrada);
		} catch (Exception e) {
			System.err.println("Erro ao redirecionar entrada: " + e.getMessage());
		}
		Locale.setDefault(Locale.ENGLISH);
		entrada.useLocale(Locale.ENGLISH);
	}

	private void redirecionaSaida() {
		try {
			PrintStream streamSaida = new PrintStream(new File(nomeArquivoSaida), Charset.forName("UTF-8"));
			System.setOut(streamSaida);
		} catch (Exception e) {
			System.err.println("Erro ao redirecionar saída: " + e.getMessage());
		}
		Locale.setDefault(Locale.ENGLISH);
	}

	private void restauraEntrada() {
		entrada = new Scanner(System.in);
	}

	private void restauraSaida() {
		System.setOut(saidaPadrao);
	}

	void executar() {
		mostrarPartidos();
		mostrarCandidatos();
		mostrarVotos();
		consultarInformacoes();
		mostrarPartidoComMaisVotosDeVereadores();
		mostrarMunicipioComMaiorQuantidadeDeVotos();
		restauraEntrada();
		restauraSaida();
	}

	private Partido buscaPartidoPorNumero(int numero) {
		for (Partido partido : partidoList) {
			if (partido.getNumero() == numero) {
				return partido;
			}
		}
		return null;
	}

	private Partido buscaPartidoPorNome(String nome) {
		for (Partido partido : partidoList) {
			if (partido.getNome().equals(nome)) {
				return partido;
			}
		}
		return null;
	}

	private Candidato buscaCandidatoPorNumero(int numero) {
		for (Candidato candidato : candidatoList) {
			if (candidato.getNumero() == numero) {
				return candidato;
			}
		}
		return null;
	}


	private void mostrarPartidos() {
		while (entrada.hasNextLine()) {
			String linha = entrada.nextLine();
			if (linha.equals("-1")) break;

			String nome = entrada.nextLine();
			int numero = Integer.parseInt(linha);

			Partido partido = new Partido(numero, nome);
			if (cadastroPartido.cadastraPartido(partido)) {
				partidoList.add(partido);
				System.out.println("1:" + numero + "," + nome);
			} else {
				System.out.println("1:Partido já cadastrado.");
			}
		}
	}

	private void mostrarCandidatos() {
		while (true) {
			String linha = entrada.nextLine();
			if (linha.equals("-1")) break;

			String nome = entrada.nextLine();
			String municipio = entrada.nextLine();
			int numero = Integer.parseInt(linha);

			Partido partido = buscaPartidoPorNumero(11);
			if (partido != null) {
				Candidato candidato = new Candidato(numero, nome, municipio, partido);
				if (candidatura.cadastraCandidato(candidato)) {
					candidatoList.add(candidato);
					partido.adicionaCandidato(candidato);
					System.out.println("2:" + numero + "," + nome + "," + municipio);
				} else {
					System.out.println("2:Candidato já cadastrado.");
				}
			} else {
				System.out.println("2:Partido não encontrado.");
			}
		}
	}


	private void mostrarVotos() {
		while (true) {
			String linha = entrada.nextLine();
			if (linha.equals("-1")) break;

			String municipio = entrada.nextLine();
			int numeroCandidato = Integer.parseInt(linha);
			int votos = Integer.parseInt(entrada.nextLine());

			Candidato candidato = buscaCandidatoPorNumero(numeroCandidato);
			if (candidato != null && candidato.getMunicipio().equals(municipio)) {
				candidato.adicionaVotos(votos);
				System.out.println("3:" + numeroCandidato + "," + municipio + "," + votos);
			} else {
				System.out.println("3:Candidato não encontrado.");
			}
		}
	}


	private void consultarInformacoes() {
		int numeroPartido = Integer.parseInt(entrada.nextLine());
		Partido partido = buscaPartidoPorNumero(numeroPartido);
		if (partido != null) {
			System.out.println("4:" + numeroPartido + "," + partido.getNome());
		} else {
			System.out.println("4:Partido não encontrado.");
		}

		int numeroCandidato = Integer.parseInt(entrada.nextLine());
		String municipio = entrada.nextLine();
		Candidato candidato = buscaCandidatoPorNumero(numeroCandidato);
		if (candidato != null && candidato.getMunicipio().equals(municipio)) {
			System.out.println("5:" + numeroCandidato + "," + candidato.getNome() + "," + municipio + "," + candidato.getVotos());
		} else {
			System.out.println("5:Candidato não encontrado.");
		}

		String nomePartido = entrada.nextLine();
		Partido partidoEncontrado = buscaPartidoPorNome(nomePartido);
		if (partidoEncontrado != null) {
			String resultado = "6:" + nomePartido;

			for (Candidato c : candidatoList) {
				if (c.getPartido().equals(partidoEncontrado)) {
					resultado += "," + c.getNumero() + "," + c.getNome() + "," + c.getMunicipio() + "," + c.getVotos();
					break;
				}
			}

			System.out.println(resultado);
		} else {
			System.out.println("6:Partido não encontrado.");
		}

		Partido partidoMaisCandidatos = buscaPartidoComMaisCandidatos();
		if (partidoMaisCandidatos != null) {
			System.out.println("7:" + partidoMaisCandidatos.getNumero() + "," + partidoMaisCandidatos.getNome() + "," + partidoMaisCandidatos.getCandidatos().size());
		} else {
			System.out.println("7:Partido com nenhum candidato.");
		}

		Candidato vereadorMaisVotado = getVereadorMaisVotado();
		Candidato prefeitoMaisVotado = getPrefeitoMaisVotado();

		if (vereadorMaisVotado != null) {
			System.out.println("8:" + vereadorMaisVotado.getNumero() + "," + vereadorMaisVotado.getNome() + "," + vereadorMaisVotado.getMunicipio() + "," + vereadorMaisVotado.getVotos());
		}
		if (prefeitoMaisVotado != null) {
			System.out.println("8:" + prefeitoMaisVotado.getNumero() + "," + prefeitoMaisVotado.getNome() + "," + prefeitoMaisVotado.getMunicipio() + "," + prefeitoMaisVotado.getVotos());
		}
		if (vereadorMaisVotado == null && prefeitoMaisVotado == null) {
			System.out.println("8:Candidato não encontrado.");
		}
	}

	private Candidato getVereadorMaisVotado() {
		Candidato maisVotado = null;
		int maxVotos = -1;
		for (Candidato c : candidatoList) {
			if (c.getNumero() < 10000 && c.getVotos() > maxVotos) {
				maisVotado = c;
				maxVotos = c.getVotos();
			}
		}
		return maisVotado;
	}


	private Partido buscaPartidoComMaisCandidatos() {
		Partido partidoMaisCandidatos = null;
		int maxCandidatos = 0;

		for (Partido partido : partidoList) {
			int count = 0;
			for (Candidato candidato : candidatoList) {
				if (candidato.getPartido().equals(partido)) {
					count++;
				}
			}
			if (count > maxCandidatos) {
				maxCandidatos = count;
				partidoMaisCandidatos = partido;
			}
		}
		return partidoMaisCandidatos;
	}

	private Candidato getPrefeitoMaisVotado() {
		Candidato maisVotado = null;
		int maxVotos = -1;
		for (Candidato c : candidatoList) {
			if (c.getNumero() >= 10000 && c.getVotos() > maxVotos) {
				maisVotado = c;
				maxVotos = c.getVotos();
			}
		}
		return maisVotado;
	}

	private void mostrarPartidoComMaisVotosDeVereadores() {
		int numeroMaximoPartidos = partidoList.size();
		int[] votosPorPartido = new int[numeroMaximoPartidos];
		int[] partidoNumeros = new int[numeroMaximoPartidos];

		for (int i = 0; i < numeroMaximoPartidos; i++) {
			partidoNumeros[i] = partidoList.get(i).getNumero();
			votosPorPartido[i] = 0;
		}

		for (Candidato c : candidatoList) {
			if (c.getNumero() < 10000) {
				int partidoNumero = c.getPartido().getNumero();
				for (int i = 0; i < numeroMaximoPartidos; i++) {
					if (partidoNumeros[i] == partidoNumero) {
						votosPorPartido[i] += c.getVotos();
						break;
					}
				}
			}
		}

		int maxVotos = 0;
		int partidoComMaisVotos = -1;
		for (int i = 0; i < numeroMaximoPartidos; i++) {
			if (votosPorPartido[i] > maxVotos) {
				maxVotos = votosPorPartido[i];
				partidoComMaisVotos = partidoNumeros[i];
			}
		}

		if (partidoComMaisVotos != -1) {
			Partido partido = buscaPartidoPorNumero(partidoComMaisVotos);
			if (partido != null) {
				System.out.println("9:" + partido.getNumero() + "," + partido.getNome() + "," + maxVotos);
			}
		} else {
			System.out.println("9:Nenhum voto encontrado.");
		}
	}
	private void mostrarMunicipioComMaiorQuantidadeDeVotos() {
		int numeroMaximoMunicipios = candidatoList.size();
		String[] municipios = new String[numeroMaximoMunicipios];
		int[] votosPorMunicipio = new int[numeroMaximoMunicipios];


		int municipioCount = 0;
		for (Candidato c : candidatoList) {
			String municipio = c.getMunicipio();
			boolean encontrado = false;
			for (int i = 0; i < municipioCount; i++) {
				if (municipios[i].equals(municipio)) {
					votosPorMunicipio[i] += c.getVotos();
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				municipios[municipioCount] = municipio;
				votosPorMunicipio[municipioCount] = c.getVotos();
				municipioCount++;
			}
		}

		int maxVotos = 0;
		String municipioComMaisVotos = null;
		for (int i = 0; i < municipioCount; i++) {
			if (votosPorMunicipio[i] > maxVotos) {
				maxVotos = votosPorMunicipio[i];
				municipioComMaisVotos = municipios[i];
			}
		}

		if (municipioComMaisVotos != null) {
			System.out.println("10:" + municipioComMaisVotos + "," + maxVotos);
		} else {
			System.out.println("10:Nenhum voto encontrado.");
		}
	}
}
