package ikov.pdungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.Npc;

public class FightBoss implements Strategy {
	
	public boolean activate() {
		for(int i = 0; i < PDungeoneering.BOSS_IDS.length; i++){
			if (Npcs.getNearest(PDungeoneering.BOSS_IDS[i]) != null && !PDungeoneering.nulledBoss){
				return true;
			}
		}
		return false;
	}

	public void execute() {
		//System.out.println("Fighting boss");
		try{
			//if(Npcs.getNearest().length > 0)
			//	System.out.println("Nearest NPC: "+Npcs.getNearest()[0].getLocation());
			for(int i = 0; i < PDungeoneering.BOSS_IDS.length; i++){
				if (Npcs.getNearest(PDungeoneering.BOSS_IDS)[i] != null){
					final Npc boss = Npcs.getNearest(PDungeoneering.BOSS_IDS)[i];
					if(!boss.isInCombat()){
						boss.interact(Npcs.Option.ATTACK);
						Time.sleep(new SleepCondition(){
							public boolean isValid() {
								if(boss != null){
									if (boss.isInCombat())
										return true;
								}
								if (Npcs.getNearest(PDungeoneering.THOK_ID) != null){
									if(Npcs.getNearest(PDungeoneering.THOK_ID).length > 0)
										return true;
								}
								return false;
							}
						},3600);
					}
					PDungeoneering.nulledBossCheck = 0;
					//System.out.println("Boss: "+i+" ("+PDungeoneering.BOSS_IDS[i]+ ") "+boss.getLocation()+" animation: "+boss.getAnimation() + "  Health: "+boss.getHealth()+"%");
				}
			}
		}catch(Exception e){
			if(e.getMessage().contains("0")){
				//System.out.println("Possible nulled boss? ArrayIndex 0 error");
				PDungeoneering.nulledBossCheck ++;
			}
			Time.sleep(600);
		}
		if(PDungeoneering.nulledBossCheck > 10){
			PDungeoneering.nulledBoss = true;
			System.out.println("BOSS NULLED - QUITTING DUNGEON");
			//Menu.sendAction(315, 5832704, 493, 16035, 356, 1);//quit dung lose prestige
			Time.sleep(600);
		}
	}
}
