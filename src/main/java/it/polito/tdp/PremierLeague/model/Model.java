package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
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
	int SP;
	int AS;
	int TP;

	
	private Graph<Player,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao= new PremierLeagueDAO();
		idMap=new HashMap<>();
		
	}
	
	public List<Match> listAllMatches(){
		return dao.listAllMatches();
	}
	
	public void listAllTeams(){
		for(Team t:dao.listAllTeams()) {
			idMap.put(t.teamID, t);
		}
	}
	
	public List<Player> listAllPlayersByMatch(Match m){
		return dao.listAllPlayersByMatch(m);
	}
	
	public Action actionsByPlayerAndMatch(Player p, Match m) {
		return dao.actionsByPlayerAndMatch(p, m);
	}
	
	public void calcolaEfficienza(Match m){
		
		for(Player p:this.listAllPlayersByMatch(m)) {
			
			SP=this.actionsByPlayerAndMatch(p, m).getTotalSuccessfulPassesAll();
			AS=this.actionsByPlayerAndMatch(p, m).getAssists();
			TP=this.actionsByPlayerAndMatch(p, m).getTimePlayed();
			Team t=idMap.get(this.actionsByPlayerAndMatch(p, m).getTeamID());
			p.calcolaEfficienza(SP, AS, TP);
			
			if(p.getTeam()==null)
				p.setTeam(t);
		}
	}
	
	public String creaGrafo(Match m) {
		
		grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Graphs.addAllVertices(grafo, this.listAllPlayersByMatch(m));
		
		this.calcolaEfficienza(m);
		
		for(Player p:this.listAllPlayersByMatch(m)) {
			for(Player pl:this.listAllPlayersByMatch(m)) {
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
		
		String s="#Vertici= "+this.grafo.vertexSet().size()+" #Archi= "+ this.grafo.edgeSet().size();
		
		return s;
	}
	
}
