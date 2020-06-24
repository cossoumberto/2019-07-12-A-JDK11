package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private Map<Integer, Food> idMap;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Simulator s;
	
	public Model () {
		dao = new FoodDao();
		idMap = new HashMap<>();
		dao.listAllFoods(idMap);
	}
	
	public void creaGrafo (int portion) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.listFoodPortion(portion, idMap));
		for(CoppiaFood c : dao.listCoppieFood(portion, idMap))
			Graphs.addEdge(grafo, c.getF1(), c.getF2(), c.getPeso());
	}
	
	public Graph<Food, DefaultWeightedEdge> getGrafo(){
		return grafo;
	}

	public List<FoodPeso> listFoodPeso(Food food) {
		List<FoodPeso> list = new ArrayList<>();
		for(DefaultWeightedEdge e : grafo.edgesOf(food)) {
				list.add(new FoodPeso(Graphs.getOppositeVertex(grafo, e, food), grafo.getEdgeWeight(e)));
		}
		Collections.sort(list);
		return list;
	}
	
	public Integer simulazione(int k, Food food) {
		s = new Simulator();
		s.init(this, k, food);
		s.simula();
		return s.numCibi();
	}
	
	public double tempoPreparazione() {
		return s.tempoPrepaprazione();
	}
}
