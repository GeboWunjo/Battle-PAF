
public class Player {
	
	private String nomPlayer;
	private int positionX;
	private int positionY;
	private int score;
	private String etat;
	private int caddiePosX;
	private int caddiePosY;
	private Boolean hasLogo;
	private String lastHit;
	

	public  Player(String nomPlayer, int posX, int posY, int score, String etat, int cadPosX, int cadPosY, Boolean hasLogo, String lastHit){
		this.nomPlayer=nomPlayer;
		this.positionX=posX;
		this.positionY=posY;
		this.score=score;
		this.etat=etat;
		this.caddiePosX=cadPosX;
		this.caddiePosY=cadPosY;
		this.hasLogo=hasLogo;
		this.lastHit=lastHit;
	}
	
	public String getLastHit() {
		return lastHit;
	}

	public void setLastHit(String lastHit) {
		this.lastHit = lastHit;
	}

	public String getNomPlayer() {
		return nomPlayer;
	}

	public void setNomPlayer(String nomPlayer) {
		this.nomPlayer = nomPlayer;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getScrore() {
		return score;
	}

	public void setScrore(int scrore) {
		this.score = scrore;
	}
	
	public int getCaddiePosX() {
		return caddiePosX;
	}

	public void setCaddiePosX(int caddiePosX) {
		this.caddiePosX = caddiePosX;
	}

	public int getCaddiePosY() {
		return caddiePosY;
	}

	public void setCaddiePosY(int caddiePosY) {
		this.caddiePosY = caddiePosY;
	}
	
	public Boolean getHasLogo() {
		return hasLogo;
	}

	public void setHasLogo(Boolean hasLogo) {
		this.hasLogo = hasLogo;
	}
}
