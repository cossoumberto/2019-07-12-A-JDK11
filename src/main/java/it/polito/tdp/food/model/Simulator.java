package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.Type;

public class Simulator {
	
	//PARAMETRI INIZIALI
	private Model model;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private int nStazioni;
	private Food partenza;
	
	//OUTPUT DA CALCOLARE
	private List<Food> preparati;
	private double tempoTot;
	
	//STATO DEL SISTEMA
	List<Integer> stazioniLibere;
	
	//COSA DEGLI EVENTI
	Queue<Event> queue;
	
	public void init(Model model, int k, Food food) {
		this.model = model;
		grafo = model.getGrafo();
		nStazioni = k;
		partenza = food;
		preparati = new ArrayList<>();
		tempoTot = 0.0;
		stazioniLibere = new ArrayList<>();
		queue = new PriorityQueue<>();
		for(int i=1; i<=nStazioni; i++)
			stazioniLibere.add(i);
		System.out.println(stazioniLibere);
		Event e = null;
		for(FoodPeso fp : model.listFoodPeso(partenza)) {
			Integer stazioneOccupata = -1;
			for(Integer i : stazioniLibere) {
					e = new Event(0, Type.STAZIONE_OCCUPATA, fp.getFood(), fp.getPeso(), i);
					preparati.add(fp.getFood());
					queue.add(e);
					stazioneOccupata = i;
					break;
			}
			stazioniLibere.remove(stazioneOccupata);
		}
		System.out.println(queue);
		System.out.println(stazioniLibere);
	}
	
	public void simula() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			System.out.println(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
			case STAZIONE_OCCUPATA:
				Event newL = new Event(e.getTempoNecessario(), Type.STAZIONE_LIBERATA, e.getFood(), 0.0, e.getStazione());
				tempoTot += newL.getTime();
				stazioniLibere.add(e.getStazione());
				queue.add(newL);
				break;
			case STAZIONE_LIBERATA:
				for(FoodPeso fp : model.listFoodPeso(e.getFood())) {
					if(!preparati.contains(fp.getFood())) {
						Event newO = new Event(e.getTime(), Type.STAZIONE_OCCUPATA, fp.getFood(), fp.getPeso(), e.getStazione());
						preparati.add(newO.getFood());
						queue.add(e);
						stazioniLibere.remove(e.getStazione());
						break;
					}
				}
				break;
		}
	}

	public Integer numCibi() {
		return preparati.size();
	}

	public double tempoPrepaprazione() {
		return this.tempoTot;
	}

}
