var mpRestore = 20;
if(im.getMP() >= im.getMaxMP()-mpRestore)
    im.setMP(im.getMaxMP());
else
    im.setMP(im.getMP() + mpRestore);