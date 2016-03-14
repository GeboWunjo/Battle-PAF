

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import PackTest.Logos;


public class Client implements Runnable {

	enum Dir {
		NORD("N"), SUD("S"), EST("E"), OUEST("O"), JUMP_NORD("JN"), JUMP_SUD("JS"), JUMP_EST("JE"), JUMP_OUEST(
				"JO"), NORD2("N"), SUD2("S"), EST2("E"), OUEST2("O"), NORD3("N"), SUD3("S"), EST3("E"), OUEST3("O");
		String code;
		Dir(String dir) {
			code = dir;
		}
	}
	
	private String ipServer;
	private long teamId;
	private String secret;
	private int socketNumber;
	private long gameId;

	Random rand = new Random();

	public static Player monPlayer;
	public static ArrayList<Player> ListePlayers=new ArrayList<Player>();
	public static ArrayList<Logos> ListeLogos=new ArrayList<Logos>();

	public Client(String ipServer, long teamId, String secret, int socketNumber, long gameId) {
		this.ipServer = ipServer;
		this.teamId = teamId;
		this.secret = secret;
		this.socketNumber = socketNumber;
		this.gameId = gameId;
	}

	public void run() {
		System.out.println("Demarrage du client");
		Socket socket = null;
		String message;
		BufferedReader in;
		PrintWriter out;
		try {
			socket = new Socket(ipServer, socketNumber);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			System.out.println("Envoi de l'incription");
			out.println(secret + "%%inscription::" + gameId + ";" + teamId);
			out.flush();

			do {
				message = in.readLine();
				System.out.println("Message recu : " + message);
				if (message != null) {
					if (message.equalsIgnoreCase("Inscription OK")) {
						System.out.println("Je me suis bien inscrit a la battle");
					} else if (message.startsWith("worldstate::")) {
						String[] components = message.substring("worldstate::".length()).split(";", -1);
						int round = Integer.parseInt(components[0]);
						// On joue
//							initWorld(components);   
//							updateWorld(components);
						
						String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
								+ computeDirection().code;
//						String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
//						+ trouverLogoProche().code;
						System.out.println(action);
						out.println(action);
						out.flush();
					} else if (message.equalsIgnoreCase("Inscription KO")) {
						System.out.println("inscription KO");
					} else if (message.equalsIgnoreCase("game over")) {
						System.out.println("game over");
						System.exit(0);
					} else if (message.equalsIgnoreCase("action OK")) {
						System.out.println("Action bien pris en compte");
					}
				}
				System.out.println("Pour voir la partie : http://" + ipServer + ":8080/?gameId=" + gameId);
			} while (message != null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// Tant pis
				}
			}

		}
	}
//Fonction de construction du monde
	public void initWorld(String[] tab){
		
		//Cr�ation des joueurs
		String listePlayer=tab[1];
		String[] player=listePlayer.split(":");
		String listeCaddies=tab[3];
		String[] caddies=listeCaddies.split(":");
		for(int i=0;i<player.length;i++){
			String[] attributsP=player[i].split(",");
			String[] attributsC=caddies[i].split(",");
			if(attributsP[0].equals("80")){
				monPlayer=new Player(attributsP[0], //nom
						Integer.parseInt(attributsP[1]), //posX
						Integer.parseInt(attributsP[2]), //posY
						Integer.parseInt(attributsP[3]), //score
						attributsP[4], //etat
						Integer.parseInt(attributsC[1]), //caddiePosX
						Integer.parseInt(attributsC[2]),//caddiesPosY
						false,null); //a un logo
				}else{ 
					ListePlayers.add(new Player(attributsP[0], //nom
							Integer.parseInt(attributsP[1]), //posX
							Integer.parseInt(attributsP[2]), //posY
							Integer.parseInt(attributsP[3]), //score
							attributsP[4], //etat
							Integer.parseInt(attributsC[1]), //caddiePosX
							Integer.parseInt(attributsC[2]), //caddiesPosY
							false,null) //a un logo
						); 
				}
		}
		//Cr�ation des logos
		String listeLogos=tab[2];
		String[] logos=listeLogos.split(":");
		for(int j=0;j<logos.length;j++){
			String[] attributs=logos[j].split(",");
			ListeLogos.add(new Logos("Logo_"+j,Integer.parseInt(attributs[0]),Integer.parseInt(attributs[1])));
		}
		
	}
	//Fonction de mise a jour du monde
	public static void updateWorld(String[] tab){
		double startTime = (double) System.currentTimeMillis();  //start temps d'execution
		System.out.println("------------------------");
		//r�partition des listes		
		String listePlayer=tab[1];
		String[] player=listePlayer.split(":");
		String listeCaddies=tab[3];
		String[] caddies=listeCaddies.split(":");
		//v�rification des donn�es
		for(int k=0;k<4;k++){
			System.out.println("tab="+tab[k]);
		}
		System.out.println("listeplayer="+listePlayer);
		for(int k=0;k<3;k++){
			System.out.println("player="+player[k]);
		}
		System.out.println("length="+player.length);
		System.out.println("listecaddies="+listeCaddies);
		for(int k=0;k<3;k++){
			System.out.println("caddies="+caddies[k]);
		}
		//Mise a jour des logos
		String listeLogos=tab[2];
		String[] logos=listeLogos.split(":");
		System.out.println("listeLogos="+listeLogos);
		
		
		for(int j=0;j<logos.length;j++){
			String[] attributs=logos[j].split(",");
			Logos logo=new Logos("Logo_"+j,Integer.parseInt(attributs[0]),Integer.parseInt(attributs[1]));
			System.out.println("logo:"+logo.toString());
		}
		
		int count=0;
		//mise a jour des objets
		for(int i=0;i<player.length;i++){
			String[] attributsP=player[i].split(",");
			String[] attributsC=caddies[i].split(",");
			if(attributsP[0].equals("80")){
				monPlayer.setPositionX(Integer.parseInt(attributsP[1])); //posX
				monPlayer.setPositionY(Integer.parseInt(attributsP[2])); //posY
				monPlayer.setScrore(Integer.parseInt(attributsP[3])); //score
				monPlayer.setEtat(attributsP[4]); //etat
				monPlayer.setCaddiePosX(Integer.parseInt(attributsC[1])); //caddiePosX
				monPlayer.setCaddiePosY(Integer.parseInt(attributsC[2])); //caddiesPosY
				System.out.println("playerInfo=");
				System.out.println("nom: "+monPlayer.getNomPlayer()+"\tposX: "+monPlayer.getPositionX()+"\tposY: "+monPlayer.getPositionY()+"\tscore: "+monPlayer.getScrore()+"\tetat: "+monPlayer.getEtat());
				}else{ 
					ListePlayers.get(count).setPositionX(Integer.parseInt(attributsP[1])); //posX
					ListePlayers.get(count).setPositionY(Integer.parseInt(attributsP[2])); //posY
					ListePlayers.get(count).setScrore(Integer.parseInt(attributsP[3])); //score
					ListePlayers.get(count).setEtat(attributsP[4]); //etat
					ListePlayers.get(count).setCaddiePosX(Integer.parseInt(attributsC[1])); //caddiePosX
					ListePlayers.get(count).setCaddiePosY(Integer.parseInt(attributsC[2])); //caddiesPosY
					
					System.out.println("ennemis"+ListePlayers.get(count).toString());
					System.out.println("nom: "+ListePlayers.get(count).getNomPlayer()+"\tposX: "+ListePlayers.get(count).getPositionX()+"\tposY: "+ListePlayers.get(count).getPositionY()+"\tscore: "+ListePlayers.get(count).getScrore()+"\tetat: "+ListePlayers.get(count).getEtat());
					count++;
				}			
		}
		System.out.println("player="+monPlayer.toString());
		
		double finTime = (double) System.currentTimeMillis();
        double totalTime = (double) ((finTime - startTime) / 1000); //calcul temps d'execution
		
        System.out.println("temps: "+totalTime +"s");
	}
	
	public static Dir trouverLogoProche(){
		Dir reponse = null;
		if(!monPlayer.getHasLogo()){
			int monPlayerPosX=monPlayer.getPositionX();
			int monPlayerPosY=monPlayer.getPositionY();
			int distanceLogoPlusProche=0;
			Logos logoPlusProche=ListeLogos.get(0);
			for(Logos logo:ListeLogos){
				int calcul=Math.abs(monPlayerPosX-logo.getLogPositionX())+Math.abs(monPlayerPosY-logo.getLogPositionY());
				if(calcul<distanceLogoPlusProche){
					logoPlusProche=logo;
					distanceLogoPlusProche=calcul;
				}
			}
			if(monPlayerPosX<logoPlusProche.getLogPositionX()){
				reponse=Dir.EST;
			}else if(monPlayerPosX>logoPlusProche.getLogPositionX()){
				reponse=Dir.OUEST;
			}else{
				if(monPlayerPosY<logoPlusProche.getLogPositionY()){
					reponse=Dir.SUD;
				}else if(monPlayerPosY>logoPlusProche.getLogPositionY()){
					reponse=Dir.NORD;
				}
			}
		}else ramenerLogo();
		return reponse;
	}
	public static Dir ramenerLogo(){
		Dir reponse = null;
		if(monPlayer.getHasLogo()){
			int monPlayerPosX=monPlayer.getPositionX();
			int monPlayerPosY=monPlayer.getPositionY();
			int monCaddiePosX=monPlayer.getCaddiePosX();
			int monCaddiePosY=monPlayer.getCaddiePosY();
			if(monPlayerPosY<monCaddiePosY){
				reponse=Dir.SUD;
			}else if(monPlayerPosY>monCaddiePosY){
				reponse=Dir.NORD;
			}else{
				if(monPlayerPosX>monCaddiePosX){
					reponse=Dir.OUEST;
				}else if(monPlayerPosX<monCaddiePosX){
					reponse=Dir.EST;
				}
			}
		}else trouverLogoProche();
		return reponse;
	}
	
	public Dir computeDirection() {
		return Dir.values()[rand.nextInt(Dir.values().length)];
	}

}
