importClass(Packages.org.lwjgl.util.Point);
if(pm.getMapID() == 0)
{
	if(pm.playerFacingDir() == pm.UP)
		pm.setMap(0003, new Point(9,19));
}else if(pm.getMapID() == 3)
{
	if(pm.playerFacingDir() == pm.DOWN)
		pm.setMap(0000, new Point(8,0));
}