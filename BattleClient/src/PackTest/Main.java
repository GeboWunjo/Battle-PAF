package PackTest;

import java.util.ArrayList;

public class Main {

	enum Dir {
		NORD("N"), SUD("S"), EST("E"), OUEST("O"), JUMP_NORD("JN"), JUMP_SUD("JS"), JUMP_EST("JE"), JUMP_OUEST(
				"JO"), NORD2("N"), SUD2("S"), EST2("E"), OUEST2("O"), NORD3("N"), SUD3("S"), EST3("E"), OUEST3("O");
		String code;
		Dir(String dir) {
			code = dir;
		}
	}
	
	public static Player monPlayer;
	public static ArrayList<Player> ListePlayers=new ArrayList<Player>();
	public static ArrayList<Logos> ListeLogos=new ArrayList<Logos>();
	public static String message="worldstate::1;80,2,4,0,playing:15,5,10,0,stunned:18,10,5,4,playing;10,10:13,13:5,8;80,2,2:15,2,8:18,2,6;";  //message envoy� par le serveur 
	public static int nbJump=0;
	
	public static void main(String[] args) {
		String message="worldstate::1;80,2,4,0,playing:15,5,10,0,stunned:18,10,5,4,playing;10,10:13,13:5,8;80,2,2:15,2,8:18,2,6;";
		String[] components = message.substring("worldstate::".length()).split(";", -1);
		int round = Integer.parseInt(components[0]);
		/*********************** zone de modif***************************/
		// On joue
		if(round==1){ // pas la bonne m�thode si on d�marre au round 2...
			initWorld(components); //construction du monde
		}else{
			updateWorld(components); //mise a jour du monde
		}
//		String action = secret + "%%action::" + teamId + ";" + gameId + ";" + round + ";"
//		+ computeDirection().code;
		
		
		/*********************** fin zone de modif***************************/
		String action = "blabla%%action::80;gameId;" + round + ";"
		+ trouverLogoProche().code;
		System.out.println(action);
	}
	/*********************** modif libre***************************/
	//Fonction de construction du monde
	public static void initWorld(String[] tab){
		double startTime = (double) System.currentTimeMillis();  //start temps d'execution
		
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
		int count=0;
		//Cr�ation des joueurs
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
				System.out.println("player="+monPlayer.toString());
				System.out.println("nom: "+monPlayer.getNomPlayer()+"\tposX: "+monPlayer.getPositionX()+"\tposY: "+monPlayer.getPositionY()+"\tscore: "+monPlayer.getScrore()+"\tetat: "+monPlayer.getEtat());
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
					System.out.println("ennemis"+ListePlayers.get(count).toString());
					System.out.println("nom: "+ListePlayers.get(count).getNomPlayer()+"\tposX: "+ListePlayers.get(count).getPositionX()+"\tposY: "+ListePlayers.get(count).getPositionY()+"\tscore: "+ListePlayers.get(count).getScrore()+"\tetat: "+ListePlayers.get(count).getEtat());
					count++;
				}			
		}
		System.out.println("player="+monPlayer.toString());
		//Cr�ation des logos
		String listeLogos=tab[2];
		String[] logos=listeLogos.split(":");
		System.out.println("listeLogos="+listeLogos);
		
		
		for(int j=0;j<logos.length;j++){
			String[] attributs=logos[j].split(",");
			ListeLogos.add(new Logos("Logo_"+j,Integer.parseInt(attributs[0]),Integer.parseInt(attributs[1])));
			
		}
		double finTime = (double) System.currentTimeMillis();
        double totalTime = (double) ((finTime - startTime) / 1000); //calcul temps d'execution
		
        System.out.println("temps: "+totalTime +"s");
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
		//Cr�ation des logos
		String listeLogos=tab[2];
		String[] logos=listeLogos.split(":");
		System.out.println("listeLogos="+listeLogos);
		
		
		for(int j=0;j<logos.length;j++){
			String[] attributs=logos[j].split(",");
			Logos logo=new Logos("Logo_"+j,Integer.parseInt(attributs[0]),Integer.parseInt(attributs[1]));
			System.out.println("logo:"+logo.toString());
		}
		double finTime = (double) System.currentTimeMillis();
        double totalTime = (double) ((finTime - startTime) / 1000); //calcul temps d'execution
		
        System.out.println("temps: "+totalTime +"s");
	}
	
	//pour trouver le logo le plus proche
	public static Dir trouverLogoProche(){
		Dir reponse = null;
		if(!monPlayer.getHasLogo()){
			int monPlayerPosX=monPlayer.getPositionX();
			int monPlayerPosY=monPlayer.getPositionY();
			int distanceLogoPlusProche=0;
			Logos logoPlusProche=ListeLogos.get(0);
			System.out.println(logoPlusProche);
			System.out.println("taille"+ListeLogos.size());
			//pour tous les logos calculer le nombre de d�placements
			for(Logos logo:ListeLogos){
				
				int calcul=Math.abs(monPlayerPosX-logo.getLogPositionX())+Math.abs(monPlayerPosY-logo.getLogPositionY());
				System.out.println("dist="+calcul);
				if(calcul<distanceLogoPlusProche){
					logoPlusProche=logo;
					distanceLogoPlusProche=calcul;
				}
			}
			System.out.println(logoPlusProche.toString());
			//prioris� une recherche sur X puis Y
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
	
	//retour au caddie
	public static Dir ramenerLogo(){
		Dir reponse = null;
		
		if(monPlayer.getHasLogo()){
			int monPlayerPosX=monPlayer.getPositionX();
			int monPlayerPosY=monPlayer.getPositionY();
			int monCaddiePosX=monPlayer.getCaddiePosX();
			int monCaddiePosY=monPlayer.getCaddiePosY();
			//prioris� un retour sur Y puis sur X
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
	
}
