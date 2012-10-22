importClass(Packages.org.lwjgl.util.Point);
var p = new Array(new Point(0, step+1));
if(sm.hasMonsterAt(p[0])){
	sm.hit(p, 1.0);
	sm.stop();
}else if(sm.hasStrongEntityAt(p[0])){
	sm.stop();
}else{
	sm.play(p[0]);
}
if(step==10)
	sm.stop();