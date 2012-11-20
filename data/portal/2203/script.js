importClass(Packages.org.lwjgl.util.Point);
if(pm.getMapID() == 0){
	if(pm.playerFacingDir() == pm.UP){
		
		pm.setMap(3);
	}
}else if(pm.getMapID() == 3){
	if(pm.playerFacingDir() == pm.DOWN){
		pm.setMap(0);
	}
}