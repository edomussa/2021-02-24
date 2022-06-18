package it.polito.tdp.PremierLeague.db;

import it.polito.tdp.PremierLeague.model.Player;

public class TestDao {

	public static void main(String[] args) {
		TestDao testDao = new TestDao();
		testDao.run();
	}
	
	public void run() {
		PremierLeagueDAO dao = new PremierLeagueDAO();
		/*System.out.println("Players:");
		System.out.println(dao.listAllPlayers());
		System.out.println("Teams:");
		System.out.println(dao.listAllTeams());
		System.out.println("Actions:");
		System.out.println(dao.listAllActions());
		System.out.println("Matches:");
		System.out.println(dao.listAllMatches());*/
		System.out.println(dao.actionsByPlayerAndMatch(new Player(3, "Giggs Ryan"), 
				dao.listAllMatches().get(0)).getTotalSuccessfulPassesAll());
		System.out.println(dao.actionsByPlayerAndMatch(new Player(3, "Giggs Ryan"), 
				dao.listAllMatches().get(0)).getAssists());
		System.out.println(dao.actionsByPlayerAndMatch(new Player(3, "Giggs Ryan"), 
				dao.listAllMatches().get(0)).getTimePlayed());

}
}
