package controller;

import java.util.concurrent.Semaphore;

public class ThreadCar extends Thread {

	private int idCarro;
	private static int posChegada;
	private static int posSaida;
	private Semaphore semaforo;
	private static int[] vetPosicoes = new int[3];
	private int vaga;
	
	public ThreadCar(int idCarro, Semaphore semaforo) {
		this.idCarro = idCarro;
		this.semaforo = semaforo;
	}

	@Override
	public void run() {
		carroAndando();		//Paralelizável
//		====Início da Seção Crítica====
		try {
			semaforo.acquire();
			carroEstacionado();	// No máximo 3
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforo.release();
		}
//		=====Fim da Seção Crítica=====
		carroSaindo();		//Paralelizável
	}

	private void carroAndando() {
		//S = S0 + v * t
		int distanciaFinal = (int)((Math.random() * 1001) + 1000);
		int distanciaPercorrida = 0;
		int deslocamento = 100;
		int tempo = 100;
		while (distanciaPercorrida < distanciaFinal) {
			//distanciaPercorrida = distanciaPercorrida + deslocamento
			distanciaPercorrida += deslocamento;
			try {
				sleep(tempo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Carro #"+idCarro+" percorreu "+
					distanciaPercorrida+" m.");
		}
		posChegada++;
		System.err.println("Carro #"+idCarro+" foi o "+
				posChegada+"o. a chegar");
	}

	private void carroEstacionado() {
		for (int i = 0 ; i < 3 ; i++) {
			if (vetPosicoes[i] == 0) {
				vetPosicoes[i] = idCarro;
				System.err.println("Carro #"+idCarro+" estacionou na vaga " 
						+i);
				vaga = i;
				break;
			}
		}
		int tempoParado = (int)((Math.random()* 2001) + 1000);
		try {
			sleep(tempoParado);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vetPosicoes[vaga] = 0;
	}

	private void carroSaindo() {
		posSaida++;
		System.err.println("Carro #"+idCarro+" foi o "+
				posSaida+"o. a sair. Vaga "+vaga+" livre");
	}

}
