package it.polito.tdp.PremierLeague.model;

public class TestModel {

	public static void main(String[] args) {
		Model m= new Model();
		System.out.println(m.listAllPlayersByMatch(m.listAllMatches().get(0)));
		m.calcolaEfficienza(m.listAllMatches().get(0));
		
		for(Player p :m.listAllPlayersByMatch(m.listAllMatches().get(0))) {
			System.out.println(p.getPlayerID()+p.getName()+p.getEfficienza()+p.getTeam());
		}
		
	}

}
