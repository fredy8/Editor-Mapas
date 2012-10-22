var quest = 2304;
switch(cm.getState()){
case 0:
	if(cm.isQuestInProgress(quest)){
		if(cm.isQuestComplete(quest)){
			cm.turnQuestIn(quest);
			cm.sendOk("Thanks!!");
		}else{
			cm.sendOk("You haven't yet finished the quest.");
		}
	}else{
		if(cm.isQuestTurnedIn(quest) || !cm.isQuestAvailable(quest)){
			cm.sendOk("Have a nice day.");
		}else{
			cm.sendYesNo("Can you kill 3 monsters and bring me 3 HP potions?", 1, 2);
		}
	}
	break;
case 1:
	cm.sendOk("Thanks. Come back when you are finished.");
	cm.activateQuest(quest);
	break;
case 2:
	cm.sendOk("...");
	break;
}