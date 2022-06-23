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
	double SP;
	double AS;
	double TP;

	
	private Graph<Player,DefaultWeightedEdge> grafo;
	
	public Model() {
		dao= new PremierLeagueDAO();
		idMap=new HashMap<>();
		giocatori=new LinkedList<>();
		
		
		
	}
	
	public List<Match> listAllMatches(){
		return dao.listAllMatches();
	}
	
	
	
	public List<Player> listAllPlayersByMatch(Match m){
		return dao.listAllPlayersByMatch(m);
	}
	
	public Action actionsByPlayerAndMatch(Player p, Match m) {
		return dao.actionsByPlayerAndMatch(p, m);
	}
	
	/*public void setSquadra(Match m) {
		for(Player p:giocatori) {
			if(p.getTeam()==null) {
				Action a=this.actionsByPlayerAndMatch(p, m);
				p.setTeam(idMap.get(a.getTeamID()));
			}
		}
	}*/
	
	/*public void calcolaEfficienza(Match m){
		
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
	}*/
	
	public String creaGrafo(Match m) {
		
		for(Team t:dao.listAllTeams()) {
			idMap.put(t.teamID, t);
		}
		
		giocatori=this.listAllPlayersByMatch(m);
		grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.listAllPlayersByMatch(m));
		
		for(Player p:giocatori) {
			Action a=this.actionsByPlayerAndMatch(p, m);
			if(p.getTeam()==null) {
				p.setTeam(idMap.get(a.getTeamID()));
			}
		
		
			
			SP=a.getTotalSuccessfulPassesAll();
			AS=a.getAssists();
			TP=a.getTimePlayed();
			//this.listAllTeams();
			
			double efficienza=0.0;
			double parziale=0;
			parziale=SP+AS;
			efficienza=(parziale/TP);
			p.setEfficienza(efficienza);
			
		}
		
		for(Player pll:giocatori) {
			for(Player pl:giocatori) {
				if(pll.getTeam()!=pl.getTeam()) {
					if(pll.getEfficienza()>pl.getEfficienza()) {
						Graphs.addEdgeWithVertices(grafo, pll, pl, pll.getEfficienza()-pl.getEfficienza());
					}
					else if(pl.getEfficienza()>pll.getEfficienza()) {
					Graphs.addEdgeWithVertices(grafo, pl, pll, pl.getEfficienza()-pll.getEfficienza());
					}
					else if(pl.getEfficienza()==pll.getEfficienza()) {
						Graphs.addEdgeWithVertices(grafo, pl, pll, pl.getEfficienza()-pll.getEfficienza());
					}
				}
			}
		}
		
		String s="#Vertici= "+this.grafo.vertexSet().size()+" #Archi= "+ this.grafo.edgeSet().size();//+this.grafo.edgeSet();
		
		return s;
	}
	
	public String giocatoreMigliore(Match m) {
		
		this.creaGrafo(m);
		Player best=null;
		double tot=0;
		
		for(Player g:grafo.vertexSet()) {
			double parz=0;
			
			for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(g)) 
				parz=parz + grafo.getEdgeWeight(e);
			
			for(DefaultWeightedEdge e: grafo.incomingEdgesOf(g))
				parz=parz + grafo.getEdgeWeight(e);
			
			if(parz>=tot) {
				tot=parz;
				best=g;
			}
		}
		
		return best+" "+tot;
		
	}
	
}
