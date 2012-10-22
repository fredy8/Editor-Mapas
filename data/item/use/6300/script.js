var hpRestore = 20;
if(im.getHP() >= im.getMaxHP()-hpRestore)
    im.setHP(im.getMaxHP());
else
    im.setHP(im.getHP() + hpRestore);