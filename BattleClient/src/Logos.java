
public class Logos {

	private String idLogo;
	private int logPositionX;
	private int logPositionY;
	private boolean estDispo;

	public Logos(String idLogo, int logPosX, int logPosY, boolean estDispo){
		this.idLogo=idLogo;
		this.logPositionX=logPosX;
		this.logPositionY=logPosY;
		this.estDispo=estDispo;
	}
		
	public String getIdLogo() {
		return idLogo;
	}

	public void setIdLogo(String idLogo) {
		this.idLogo = idLogo;
	}

	public int getLogPositionX() {
		return logPositionX;
	}

	public void setLogPositionX(int logPositionX) {
		this.logPositionX = logPositionX;
	}

	public int getLogPositionY() {
		return logPositionY;
	}

	public void setLogPositionY(int logPositionY) {
		this.logPositionY = logPositionY;
	}

	public boolean isEstDispo() {
		return estDispo;
	}

	public void setEstDispo(boolean estDispo) {
		this.estDispo = estDispo;
	}
	
}
