package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum Type{
		STAZIONE_OCCUPATA, STAZIONE_LIBERATA
	}
	
	private double time;
	private Type type;
	private Food food;
	private double tempoNecessario;
	private Integer stazione;
	
	public Event(double time, Type type, Food food, double tempoNecessario, Integer stazione) {
		super();
		this.time = time;
		this.type = type;
		this.food = food;
		this.tempoNecessario = tempoNecessario;
		this.stazione = stazione;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Integer getStazione() {
		return stazione;
	}

	public void setStazione(Integer stazione) {
		this.stazione = stazione;
	}

	public double getTempoNecessario() {
		return tempoNecessario;
	}

	public void setTempoNecessario(double tempoNecessario) {
		this.tempoNecessario = tempoNecessario;
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + ", food=" + food + ", tempoNecessario=" + tempoNecessario
				+ ", stazione=" + stazione + "]";
	}

	@Override
	public int compareTo(Event o) {
		if(this.time>o.time)
			return 1;
		else if(this.time<o.time)
			return -1;
		else 
			return 0;
	}
	
	
}
