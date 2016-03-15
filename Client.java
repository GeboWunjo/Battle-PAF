

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


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

	public static Player currentPlayer;
	public static ArrayList<Player> ListePlayers=new ArrayList<Player>();
	public static ArrayList<Logos> ListeLogos=new ArrayList<Logos>();
	public static int nbJump=0;

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
						
			/*********************** zone de modif***************************/
						double startTime = (double) System.currentTimeMillis();  //start temps d'execution
						// On joue
						updateWorld(components); //mise a jour du monde
						//Requete d'action
						String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
								+ moteurInference().code;
						System.out.println(action);
						out.println(action);
						//ajout du currentPlayer a la fin de ListePlayers
						ListePlayers.add(currentPlayer);
						
						double finTime = (double) System.currentTimeMillis(); 		//fin temps d'execution
				        double totalTime = (double) ((finTime - startTime) / 1000); //calcul temps d'execution
						
				        System.out.println("temps d'execution: "+totalTime +"s");
						
//							if(round==1){ // le round commence a 1...
//								initWorld(components); //construction du monde
//								String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
//										+ moteurInference().code;
//								System.out.println(action);
//								out.println(action);
//							}else{
//								updateWorld(components); //mise a jour du monde
//								String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
//										+ moteurInference().code;
//								System.out.println(action);
//								out.println(action);
//						}
						
//						String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
//								+ computeDirection().code;
//						String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
//						+ trouverLogoProche().code;
//						System.out.println(action);
//						out.println(action);
		/*********************** fin zone de modif***************************/			
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
	
/*********************** modif libre***************************/	
//Fonction de construction du monde
	public void initWorld(String[] tab){
				
		//Creation des logos
		String listeLogos=tab[2];
		String[] logos=listeLogos.split(":");
		//suppression des logos
		ListeLogos.clear();
		//remplissage de la liste de logo
		for(int j=0;j<logos.length;j++){
			String[] attributs=logos[j].split(",");
			ListeLogos.add(new Logos("Logo_"+j,Integer.parseInt(attributs[0]),Integer.parseInt(attributs[1])));
		}
		//Decoupage de la chaine d'entree
		String listePlayer=tab[1];
		String[] player=listePlayer.split(":");
		String listeCaddies=tab[3];
		String[] caddies=listeCaddies.split(":");
		//creation des players
		for(int i=0;i<player.length;i++){
			String[] attributsP=player[i].split(",");
			String[] attributsC=caddies[i].split(",");
			ListePlayers.add(new Player(attributsP[0], //nom
					Integer.parseInt(attributsP[1]), //posX
					Integer.parseInt(attributsP[2]), //posY
					Integer.parseInt(attributsP[3]), //score
					attributsP[4], //etat
					Integer.parseInt(attributsC[1]), //caddiePosX
					Integer.parseInt(attributsC[2]), //caddiesPosY
					false, //a un logo
					null) //dernier tape
				); 
		}
		//retirer l'element currentplayer de la listeplayer
		currentPlayer=ListePlayers.get(0);
		ListePlayers.remove(0);
	}
	
	//Fonction de mise a jour du monde
	public void updateWorld(String[] tab){
		//si pas initier, initialisation du monde
		if(ListePlayers.size()<1){
			initWorld(tab);
		}else{
			//mise a jour du monde
			//Repartition des differentes listes		
			String listePlayer=tab[1];
			String[] player=listePlayer.split(":");
			String listeCaddies=tab[3];
			String[] caddies=listeCaddies.split(":");
			
			//Mise a jour des logos
			String listeLogos=tab[2];
			String[] logos=listeLogos.split(":");
			
			//on vide la liste
			ListeLogos.clear();
			//creation des logos
			for(int j=0;j<logos.length;j++){
				String[] attributs=logos[j].split(",");
				ListeLogos.add(new Logos("Logo_"+j,Integer.parseInt(attributs[0]),Integer.parseInt(attributs[1])));
			}
			//compteur local de position des players dans la liste
			int count=0;
			//mise a jour des objets player
			for(int i=0;i<player.length;i++){
				String[] attributsP=player[i].split(",");  //attributs des players
				String[] attributsC=caddies[i].split(","); //attributs des players
						ListePlayers.get(count).setPositionX(Integer.parseInt(attributsP[1])); //posX
						ListePlayers.get(count).setPositionY(Integer.parseInt(attributsP[2])); //posY
						ListePlayers.get(count).setScrore(Integer.parseInt(attributsP[3])); //score
						ListePlayers.get(count).setEtat(attributsP[4]); //etat
						ListePlayers.get(count).setCaddiePosX(Integer.parseInt(attributsC[1])); //caddiePosX
						ListePlayers.get(count).setCaddiePosY(Integer.parseInt(attributsC[2])); //caddiesPosY
						
						//Les joueurs portent-ils des logos?
						for(Logos logo:ListeLogos){
							if(logo.getLogPositionX()==ListePlayers.get(count).getPositionX() && logo.getLogPositionY()==ListePlayers.get(count).getPositionY()){
								ListePlayers.get(count).setHasLogo(true);
							}
						}
						//les joueurs ont-ils dépose leur logo?
						if(ListePlayers.get(count).getHasLogo()){
							if(ListePlayers.get(count).getPositionX()==ListePlayers.get(count).getCaddiePosX() && ListePlayers.get(count).getPositionY()==ListePlayers.get(count).getCaddiePosY()){
								ListePlayers.get(count).setHasLogo(false);
							}
						}
//						System.out.println("ennemis"+ListePlayers.get(count).toString());
//						System.out.println("nom: "+ListePlayers.get(count).getNomPlayer()+"\tposX: "+ListePlayers.get(count).getPositionX()+"\tposY: "+ListePlayers.get(count).getPositionY()+"\tscore: "+ListePlayers.get(count).getScrore()+"\tetat: "+ListePlayers.get(count).getEtat());
						
						//incrementation du compteur local
						count++;
					}			
			}
//			System.out.println("player="+monPlayer.toString());
		//retirer l'element currentplayer de la listeplayer
			currentPlayer=ListePlayers.get(0);
			ListePlayers.remove(0);
	}
	
	public static Dir trouverLogoProche(){
		Dir reponse = null;
		int monPlayerPosX=currentPlayer.getPositionX();
		int monPlayerPosY=currentPlayer.getPositionY();
		int distanceLogoPlusProche=0;
		Logos logoPlusProche=ListeLogos.get(0);
		System.out.println(logoPlusProche);
		System.out.println("taille"+ListeLogos.size());
		for(Logos logo:ListeLogos){
			int calcul=Math.abs(monPlayerPosX-logo.getLogPositionX())+Math.abs(monPlayerPosY-logo.getLogPositionY());
			System.out.println("dist="+calcul);
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
		return reponse;
	}
	public static Dir ramenerLogo(){
		Dir reponse = null;
		int monPlayerPosX=currentPlayer.getPositionX();
		int monPlayerPosY=currentPlayer.getPositionY();
		int monCaddiePosX=currentPlayer.getCaddiePosX();
		int monCaddiePosY=currentPlayer.getCaddiePosY();
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
		return reponse;
	}
	
	public Dir moteurInference(){
		Dir reponse = null;
		if(!currentPlayer.getHasLogo()){
			reponse=trouverLogoProche();
		}else if(currentPlayer.getHasLogo()){
			reponse=ramenerLogo();
		}
		return reponse;
	}
	
	/******************code donne***************/
	public Dir computeDirection() {
		return Dir.values()[rand.nextInt(Dir.values().length)];
	}
}
