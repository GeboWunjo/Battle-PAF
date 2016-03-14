
public class Logos {

	private String idLogo;
	private int logPositionX;
	private int logPositionY;

	public Logos(String idLogo, int logPosX, int logPosY){
		this.idLogo=idLogo;
		this.logPositionX=logPosX;
		this.logPositionY=logPosY;
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
	
}
