importClass(Packages.org.lwjgl.util.Point);
var p = new Array(new Point(0, 1));
if(step == 1)
	sm.play(p[0]);
if(step == 12 || step == 24 || step == 36)
{
	sm.hit(p, .6);
	if(step == 36)
		sm.stop();
}