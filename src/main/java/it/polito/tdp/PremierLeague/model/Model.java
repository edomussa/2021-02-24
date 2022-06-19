package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Map<Integer,Team> idMap;
	private List<Player> giocatori;
	int SP;
	int AS;
	int TP;

	
	private Graph<Player,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao= new PremierLeagueDAO();
		idMap=new HashMap<>();
		giocatori=new LinkedList<>();
		
		for(Team t:dao.listAllTeams()) {
			idMap.put(t.teamID, t);
		}
		
	}
	
	public List<Match> listAllMatches(){
		return dao.listAllMatches();
	}
	
	public void listAllTeams(){
		
	}
	
	public List<Player> listAllPlayersByMatch(Match m){
		return dao.listAllPlayersByMatch(m);
	}
	
	public Action actionsByPlayerAndMatch(Player p, Match m) {
		return dao.actionsByPlayerAndMatch(p, m);
	}
	
	public void setSquadra(Match m) {
		for(Player p:giocatori) {
			if(p.getTeam()==null) {
				Action a=this.actionsByPlayerAndMatch(p, m);
				p.setTeam(idMap.get(a.getTeamID()));
			}
		}
	}
	
	public void calcolaEfficienza(Match m){
		
		for(Player p:giocatori) {
			Action a=this.actionsByPlayerAndMatch(p, m);
			SP=a.getTotalSuccessfulPassesAll();
			AS=a.getAssists();
			TP=a.getTimePlayed();
			//this.listAllTeams();
			
			double efficienza=0.0;
			int parziale=0;
			parziale=SP+AS;
			efficienza=(parziale/TP);
			p.setEfficienza(efficienza);
			
			
		}
	}
	
	public String creaGrafo(Match m) {
		giocatori=this.listAllPlayersByMatch(m);
		grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.listAllPlayersByMatch(m));
		
		this.setSquadra(m);
		this.calcolaEfficienza(m);
		
		for(Player p:giocatori) {
			for(Player pl:giocatori) {
				if(p.getTeam()!=pl.getTeam()) {
					if(p.getEfficienza()>pl.getEfficienza()) {
						Graphs.addEdgeWithVertices(grafo, p, pl, p.getEfficienza()-pl.getEfficienza());
					}
					else if(pl.getEfficienza()>p.getEfficienza()) {
						Graphs.addEdgeWithVertices(grafo, pl, p, pl.getEfficienza()-p.getEfficienza());
					}
				}
			}
		}
		
		String s="#Vertici= "+this.grafo.vertexSet().size()+" #Archi= "+ this.grafo.edgeSet().size()+this.grafo.edgeSet();
		
		return s;
	}
	
}
